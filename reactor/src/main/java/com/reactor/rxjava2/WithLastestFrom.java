package com.reactor.rxjava2;

import io.reactivex.Observable;

import java.util.concurrent.TimeUnit;

public class WithLastestFrom {
    static void withLatestFrom_test(){
        Integer[] numbers={1,2,13,34,15,17};
        String[] fruits={"苹果","梨子","李子","番茄","芒果"};
        Observable<Integer> source1=Observable.fromArray(numbers);
        Observable<String> source2=Observable.fromArray(fruits);
        source1.withLatestFrom(source2,(item1,item2)->item1+item2).forEach(System.out::println);
        System.out.println("###############################");
        Observable<String> stringObservable=Observable.interval(2, TimeUnit.SECONDS).map(x->"java"+x);
        Observable<String> stringObservable1=Observable.interval(1, TimeUnit.SECONDS).map(x->"spring"+x);
        stringObservable1.withLatestFrom(stringObservable,(s,f)->s+":"+f).forEach(System.out::println);
        try {
            TimeUnit.SECONDS.sleep(6);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        withLatestFrom_test();
    }
}
