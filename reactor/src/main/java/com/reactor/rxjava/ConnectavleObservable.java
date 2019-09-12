package com.reactor.rxjava;


import io.reactivex.Observable;
import io.reactivex.internal.operators.observable.ObservablePublish;
import io.reactivex.observables.ConnectableObservable;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ConnectavleObservable {
    public static void main(String[] args) {
        pool_punlish_pushCollection();
    }
    static void infinite_publish_test(){
        ConnectableObservable<Object> objectConnectavleObservable = Observable.create(observer-> {
            BigInteger integer=BigInteger.ZERO;
            while (true){
                observer.onNext(integer);
                integer=integer.add(BigInteger.ONE);
            }
        }).publish();
        objectConnectavleObservable.subscribe(x->log(x));
        objectConnectavleObservable.connect();
    }
    private static void log(Object msg){
        System.out.println(Thread.currentThread().getName()+":"+msg);
    }
    static void hot_publish_Observable(){
        ConnectableObservable<Object> objectConnectableObservable=Observable.create(observer->{
            System.out.println("connection");
            observer.onNext("处理得数字是:"+Math.random()*100);
            observer.onNext("处理得数字是:"+Math.random()*100);
            observer.onComplete();
        }).publish();
        objectConnectableObservable.subscribe(consumer->System.out.println("一郎神:"+consumer));
        objectConnectableObservable.subscribe(consumer->System.out.println("二郎神:"+consumer));
        objectConnectableObservable.connect();
    }
    static void pool_punlish_pushCollection(){
        List<Integer> list = new ArrayList<>();
        for (int i=0;i<2;i++){
            list.add(i);
        }
        ForkJoinPool forkJoinPool =ForkJoinPool.commonPool();
        try {
            ObservablePublish<Integer> observablePublish=(ObservablePublish<Integer>)pushCollectionDANGER(list,forkJoinPool).publish();
            observablePublish.subscribe(x-> {
                System.out.println("一郎神:" + x);
                TimeUnit.SECONDS.sleep(2);
            },Throwable::printStackTrace,()->System.out.println("conpleted"));
            observablePublish.subscribe(x-> {
                System.out.println("二郎神:" + x);
                TimeUnit.SECONDS.sleep(2);
            },Throwable::printStackTrace,()->System.out.println("conpleted"));
            observablePublish.connect();
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            observablePublish.connect(ps->ps.dispose());
        }finally {
            try {
                forkJoinPool.shutdown();
                int shutdown=15;
                System.out.println("等待"+shutdown+"秒后结束服务");
                forkJoinPool.awaitTermination(shutdown,TimeUnit.SECONDS);
            }catch (Exception e){
                System.out.println("捕获到 forkJoinPool.awaitTermination异常"+e.getClass().getName());
           }finally {
                System.out.println("调用forkJoinPool.shutdown()结束服务");
                List<Runnable> lists=forkJoinPool.shutdownNow();
                System.out.println("还剩下"+lists.size()+"个任务等待执行，服务已经关闭");
            }

        }
    }

    private static Observable<Integer> pushCollectionDANGER(List<Integer> list, ForkJoinPool forkJoinPool) {
        return Observable.create(observer->{
            System.out.println("connection");
            AtomicInteger atomicInteger = new AtomicInteger(list.size());
            list.forEach(id->forkJoinPool.submit(()->{
                observer.onNext(id);
                if (atomicInteger.decrementAndGet()==0){
                    forkJoinPool.shutdownNow();
                    observer.onComplete();
                }
            }));
        });
    }
}
