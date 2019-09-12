package com.reactor.rxjava;

import io.reactivex.Observable;

import java.util.concurrent.TimeUnit;

public class TimeTest {
    public static void main(String[] args) throws InterruptedException {
        Observable.timer(2, TimeUnit.SECONDS).subscribe(x->log(10));
        TimeUnit.SECONDS.sleep(10);
        Observable.interval(2, TimeUnit.SECONDS).subscribe(x->log(10));
        TimeUnit.SECONDS.sleep(10);
    }
    private static void log(Object msg){
        System.out.println(Thread.currentThread().getName()+":"+msg);
    }
}
