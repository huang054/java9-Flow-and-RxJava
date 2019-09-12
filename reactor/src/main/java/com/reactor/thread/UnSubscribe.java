package com.reactor.thread;

import com.google.common.util.concurrent.AbstractScheduledService;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.reactivex.Observable;

import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import java.util.concurrent.*;

public class UnSubscribe {
    static void unsubscribe_test1(){
        Disposable disposable= Observable.interval(1, TimeUnit.SECONDS)
                .map(i->"receive"+i)
                .doOnDispose(()->System.out.println(Thread.currentThread().getName()))
             //   .unsubscribeOn(Schedulers.io())
                .subscribe(System.out::println);
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        disposable.dispose();
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        custom_scheduler_test();
    }
    static Scheduler custom_Scheduler(){
        ThreadFactory threadFactory=new ThreadFactoryBuilder()
                .setNameFormat("自定义线程-%d")
                .build();
        Executor executor = new ThreadPoolExecutor(10,10,0,TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<>(1000),threadFactory);
        return Schedulers.from(executor);
    }
    static void custom_scheduler_test(){
        Scheduler scheduler =custom_Scheduler();
        Observable.just("Apple","Orange","Appla")
                .subscribeOn(scheduler)
                .map(String::length)
                .observeOn(scheduler)
                .subscribe(System.out::println);
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
