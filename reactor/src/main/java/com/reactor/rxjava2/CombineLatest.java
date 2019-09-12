package com.reactor.rxjava2;

import io.reactivex.Observable;

import java.util.concurrent.TimeUnit;

public class CombineLatest {
    static void combineLatest_test(){
        Integer[] numbers={1,2,13,34,15,17};
        String[] fruits={"苹果","梨子","李子","番茄","芒果"};
        Observable<Integer> source1=Observable.fromArray(numbers);
        Observable<String> source2=Observable.fromArray(fruits);
        Observable.combineLatest(source1,source2,(item1,item2)->item1+item2)
                .subscribe(item->System.out.println("we got:"+item+"from the observable")
                        ,throwable -> System.out.println("异常->"+throwable.getMessage()),()->
                                System.out.println("completed"));//取前面source的最后一个，后面遍历
    }

    public static void main(String[] args) {
        combineLatest_test2();
    }

    static void combineLatest_test2(){
        Observable.combineLatest(Observable.interval(2, TimeUnit.SECONDS).map(x->"java"+x),
                Observable.interval(2, TimeUnit.SECONDS).map(x->"spring"+x),(s,f)->s+":"+f)
                .forEach(System.out::println);
        try {
            TimeUnit.SECONDS.sleep(6);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
