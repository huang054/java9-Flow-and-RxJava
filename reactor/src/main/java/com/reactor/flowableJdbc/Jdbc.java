package com.reactor.flowableJdbc;

import io.reactivex.Emitter;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

import java.sql.*;
import java.util.List;
import java.util.concurrent.Callable;


public class Jdbc {
    public static void close(ResultSet resultSet){
        Statement statement = null;
        try {
            statement=resultSet.getStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (statement!=null){
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Connection connection = null;
            try {
                connection=statement.getConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
           if (connection!=null){
               try {
                   connection.close();
               } catch (SQLException e) {
                   e.printStackTrace();
               }
           }
        }
    }



    public static <T> Flowable<T> create(Callable<Connection> connectionCallable, List<Object> parameters, String sql,
                                         Function<? super ResultSet,T> mapper){
        Callable<ResultSet> resultSetCallable=()->{
            Connection connection=connectionCallable.call();
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            ResultSet resultSet=preparedStatement.executeQuery();
            return resultSet;
        };
        BiConsumer<ResultSet, Emitter<T>> biConsumer=(rs, emitter)->{
            try {
                if (rs.next()){
                    emitter.onNext(mapper.apply(rs));
                }else{
                    emitter.onComplete();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        };
        Consumer<ResultSet> consumer=Jdbc::close;

    //    Flowable.generate(resultSetCallable,biConsumer);
        return Flowable.generate(resultSetCallable,biConsumer,consumer);

    }
   public static void closeAll(Statement statement){
       try {
           statement.close();
       } catch (SQLException e) {
           e.printStackTrace();
       }
       Connection connection = null;
       try {
           connection=statement.getConnection();
       } catch (SQLException e) {
           e.printStackTrace();
       }
       if (connection!=null){
           try {
               connection.close();
           } catch (SQLException e) {
               e.printStackTrace();
           }
       }
   }
   public static Single<Integer> create(Callable<Connection> connectionCallable,List<Object> parameters,String sql){
        Callable<PreparedStatement> preparedStatementCallable=()->{
            Connection connection = connectionCallable.call();
            return  connection.prepareStatement(sql);
        };
        Function<PreparedStatement,Single<Integer>> singleFunction =ps->Single.just(ps.executeUpdate());
        Consumer<PreparedStatement> preparedStatementConsumer=Jdbc::closeAll;
        return Single.using(preparedStatementCallable,singleFunction,preparedStatementConsumer);
   }

   public static <T> Flowable<T> create(PreparedStatement preparedStatement,Function<? super ResultSet,T> function){
        Callable<ResultSet> resultSetCallable=()->{
            preparedStatement.execute();
            return preparedStatement.getGeneratedKeys();
        };
        BiConsumer<ResultSet,Emitter<T>> biConsumer=(rs,emitterBi)->{
            if (rs.next()){
                emitterBi.onNext(function.apply(rs));
            }else{
                emitterBi.onComplete();
            }
        };
        Consumer<ResultSet> resultSetConsumer =Jdbc::close;
       return Flowable.generate(resultSetCallable,biConsumer,resultSetConsumer);
   }
    public static <T> Flowable<T> create1(Callable<Connection> connectionCallable,List<Object> objects
                                          ,String sql,Function<? super ResultSet,T> function){
        Callable<PreparedStatement> preparedStatementCallable=()->{
            Connection connection = connectionCallable.call();
            return connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
        };
        Function<PreparedStatement,Flowable<T>> flowableFunction=ps->create(ps,function);
        Consumer<PreparedStatement> preparedStatementConsumer=Jdbc::closeAll;
        return Flowable.using(preparedStatementCallable,flowableFunction,preparedStatementConsumer);

    }
}
