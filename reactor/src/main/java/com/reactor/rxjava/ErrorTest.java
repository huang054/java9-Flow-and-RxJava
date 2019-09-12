package com.reactor.rxjava;


import io.reactivex.Observable;

public class ErrorTest {
    static void error_test(int n){
        if (n==5){
            throw new RuntimeException("异常");

        }
        System.out.println("我的消费元素是"+n);
    }
    static void observable_error_test(int n){
        Observable.create(obserber->{
            try{
                obserber.onNext(n);
                obserber.onComplete();
            }catch (Exception e){
                obserber.onError(e);
            }
        }).subscribe(x->error_test((int)x),Throwable::printStackTrace, () -> System.out.println("end"));
    }

    public static void main(String[] args) {
        observable_error_test(1);
        observable_error_test(5);
    }
    }

