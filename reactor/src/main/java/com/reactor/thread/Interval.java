package com.reactor.thread;

import io.reactivex.Observable;

import java.util.concurrent.TimeUnit;

public class Interval {
    static void demo1(){
        Observable.interval(1, TimeUnit.SECONDS)
                .map(i->i+"次").subscribe(System.out::println);
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        demo3();
    }

    static void demo2(){
        Observable.interval(1, TimeUnit.SECONDS)
                .map(i->{
                    System.out.println(i);
                    return i;
                }).subscribe(System.out::println);
        Observable.interval(1, TimeUnit.SECONDS)
                .map(i->{
                    System.out.println(i);
                    return i;
                }).subscribe(System.out::println);
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static void demo3(){
        Observable.intervalRange(1, 2,1,1,TimeUnit.SECONDS)
                .map(i->{
                    System.out.println(i);
                    return i;
                }).blockingSubscribe(System.out::println);
        Observable.intervalRange(1, 2,1,1,TimeUnit.SECONDS)
                .map(i->{
                    System.out.println(i);
                    return i;
                }).blockingSubscribe(System.out::println);
    }
}
