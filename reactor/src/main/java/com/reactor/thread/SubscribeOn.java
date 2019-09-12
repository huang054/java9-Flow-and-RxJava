package com.reactor.thread;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

public class SubscribeOn {
    static void blockingSubscribe_test1(){
        Observable.just("Apple","Orange","Appla","Eatle","HOHO","Meta")
                .subscribeOn(Schedulers.computation())
                .map(String::length)
                .subscribeOn(Schedulers.io())

                .subscribe(System.out::println);
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        blockingSubscribe_test1();
    }
}
