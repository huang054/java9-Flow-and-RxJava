package com.reactor.rxjava2;


import io.reactivex.Observable;

public class Merg {
    static void merg_test(){
        Observable.merge(Observable.range(1,5),Observable.range(10,3))
                .subscribe(System.out::println);
    }

    public static void main(String[] args) {
        merg_test();
    }
}
