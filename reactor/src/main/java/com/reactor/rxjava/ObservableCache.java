package com.reactor.rxjava;


import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

public class ObservableCache {
    public static void main(String[] args) {
        Observable<Object> objectObservable=Observable.create(observer->{
            observer.onNext("处理的数字是:"+Math.random()*100);
            observer.onComplete();
        }).cache();
        objectObservable.subscribe(consumer->{
           System.out.println(consumer);
        });
        objectObservable.subscribe(consumer->{
            System.out.println(consumer);
        });
        infinite_unsunbscribed_thread_test();
    }
    private static void omfinite_test(){
        Observable<Object> objectObservable = Observable.create(observer->{
            BigInteger integer = BigInteger.ZERO;
            while (true){
                observer.onNext(integer);
                integer=integer.add(BigInteger.ONE);
            }
        });
        objectObservable.subscribe(x->log(x));
        objectObservable.subscribe(x->log(x));
    }
    private static void omfinite_thread_test(){
        Observable<Object> objectObservable = Observable.create(observer->{
            Runnable runnable=()->{
            BigInteger integer = BigInteger.ZERO;
            while (true){
                observer.onNext(integer);
                integer=integer.add(BigInteger.ONE);
            }
            };
            new Thread(runnable).start();
        });
        objectObservable.subscribe(x->log(x));
        objectObservable.subscribe(x->log(x));
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    private static void infinite_unsunbscribed_thread_test(){
        Observable<Object> objectObservable = Observable.create(observer->{
            Runnable runnable=()->{
                BigInteger integer = BigInteger.ZERO;
                while (!observer.isDisposed()){
                    observer.onNext(integer);
                    integer=integer.add(BigInteger.ONE);
                }
            };
            new Thread(runnable).start();
        });
       final Disposable disposable1= objectObservable.subscribe(x->log(x));
        final Disposable disposable2= objectObservable.subscribe(x->log(x));
        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        disposable1.dispose();;
        disposable2.dispose();
        System.out.println("我取消了");
        try {
            TimeUnit.MILLISECONDS.sleep(500);
            System.out.println("程序结束了");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void log(Object msg){
        System.out.println(Thread.currentThread().getName()+":"+msg);
    }
}
