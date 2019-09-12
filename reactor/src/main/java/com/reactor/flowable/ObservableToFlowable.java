package com.reactor.flowable;

import io.reactivex.*;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

public class ObservableToFlowable {
    static void observableToFlowable_test(){
        Observable.range(1,1000)
                .toFlowable(BackpressureStrategy.BUFFER)
                .observeOn(Schedulers.computation())
                .subscribe(System.out::println);
        try {
            Thread.sleep(1111);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static void observableMixFlowable_test(){
        Flowable<Integer> integerFlowable=Flowable.range(1,10)
                .subscribeOn(Schedulers.computation());
        Observable.just("Apple","Orange","Appla","Eatla")
                .flatMap(item->integerFlowable.map(i->item.toUpperCase()+"-"+i).toObservable())
                .subscribe(System.out::println);
        try {
            Thread.sleep(1111);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    static void interval_test(){
        Flowable.interval(1, TimeUnit.SECONDS)
                //.onBackpressureBuffer()
                .observeOn(Schedulers.io())
                .subscribe(System.out::println);
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static void interval_buffer_test(){
        Flowable.interval(1,TimeUnit.SECONDS)
                .onBackpressureBuffer()
                .observeOn(Schedulers.computation())
                .subscribe(item->{
                    TimeUnit.MILLISECONDS.sleep(20);
                    System.out.println("item:"+item);
                });
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    static void interval_buffer_custom_test(){
        Flowable.interval(1,TimeUnit.SECONDS)
                .onBackpressureBuffer(10,()->System.out.println("overflow!"), BackpressureOverflowStrategy.ERROR)
                .observeOn(Schedulers.computation())
                .subscribe(item->{
                    TimeUnit.MILLISECONDS.sleep(20);
                    System.out.println("item:"+item);
                });
        try {
            TimeUnit.MILLISECONDS.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static void last_test(){
        Flowable.interval(1,TimeUnit.SECONDS)
                .onBackpressureLatest()
                .observeOn(Schedulers.computation())
                .subscribe(item->{
                    TimeUnit.MILLISECONDS.sleep(20);
                    System.out.println("item:"+item);
                });
        try {
            TimeUnit.MILLISECONDS.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static void drop_test(){
        Flowable.interval(1,TimeUnit.SECONDS)
                .onBackpressureDrop(i->System.out.println("舍弃的元素:"+i))
                .observeOn(Schedulers.computation())
                .subscribe(item->{
                    TimeUnit.MILLISECONDS.sleep(20);
                    System.out.println("item:"+item);
                });
        try {
            TimeUnit.MILLISECONDS.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        drop_test();
    }
}
