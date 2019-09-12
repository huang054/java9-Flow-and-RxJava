package com.reactor.rxjava;


import io.reactivex.Observable;

public class ObservableDemo {
    public static void main(String[] args) {
        Observable<Object> observable = Observable.create(observer->{
           observer.onNext("处理的数字"+Math.random()*100);
            observer.onNext("处理的数字"+Math.random()*100);
           observer.onComplete();
        });
        observable.subscribe(consumer->{
           System.out.println("我处理的元素"+consumer);
        });
        observable.subscribe(consumer->{
            System.out.println("我处理的元素"+consumer);
        });
    }
}
