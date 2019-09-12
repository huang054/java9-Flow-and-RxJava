package com.reactor.rxjava2;

import io.reactivex.Observable;

import java.util.concurrent.TimeUnit;

public class Amb {
    static void amb_test(){
        Integer[] numbers={1,2,13,34,15,17};
        String[] fruits={"苹果","梨子","李子","番茄","芒果"};
        Observable<Integer> integerObservable=Observable.fromArray(numbers).delay(1, TimeUnit.SECONDS);
        Observable<String> stringObservable=Observable.fromArray(fruits);
        Observable.ambArray(integerObservable,stringObservable).forEach(System.out::println);
    }

    public static void main(String[] args) {
        amb_test();
    }
}
