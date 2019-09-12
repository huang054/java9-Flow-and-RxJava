package com.reactor.rxjava;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

public class ObservableDemo3 {
    public static void main(String[] args) {
        //infinite_unsubscribed_cache_thread_test();
        hot_s_observable();
    }
    static void infinite_unsubscribed_cache_thread_test(){
        Observable<Object> objectObservable = Observable.create(observer->{
            Runnable runnable=()->{
                BigInteger i = BigInteger.ZERO;
                while (!observer.isDisposed()){
                    observer.onNext(i);
                    i=i.add(BigInteger.ONE);
                }
            };
            new Thread(runnable).start();
        }).cache();
        final Disposable disposable =objectObservable.subscribe(x->log("一郎:"+x));
        final Disposable disposable1 = objectObservable.subscribe(x->log("二郎:"+x));

        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        disposable.dispose();
        disposable1.dispose();
        System.out.println("我取消订阅了");

        try {
            TimeUnit.MILLISECONDS.sleep(500);
            System.out.println("程序结束");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private static void log(Object msg){
        System.out.println(Thread.currentThread().getName()+":"+msg);
    }
    static void hot_s_observable(){
        Observable<Object> objectObservable = Observable.create(observer->{
            Runnable runnable=()->{
                BigInteger i = BigInteger.ZERO;
                while (true){
                    observer.onNext(i);
                    i=i.add(BigInteger.ONE);
                    if (i.compareTo(BigInteger.valueOf(50))==0){
                        observer.onComplete();
                        break;
                    }
                    System.out.println("下一个消费数字"+i.toString());
                }
            };
            new Thread(runnable).start();
        }).cache();
        objectObservable.subscribe(x->log("一郎:"+x));
        objectObservable.subscribe(x->log("二郎:"+x));
        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("我取消订阅了");

        try {
            TimeUnit.MILLISECONDS.sleep(500);
            System.out.println("程序结束");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
