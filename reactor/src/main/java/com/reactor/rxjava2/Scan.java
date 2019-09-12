package com.reactor.rxjava2;


import io.reactivex.Observable;

public class Scan {

    static void scan_test(){
        Integer[] prices={100,200,300,15,15};
        Observable.fromArray(prices)
                .scan((item1,item2)->item1+item2)
                .subscribe(count->System.out.println(count),Throwable::printStackTrace
                ,()->System.out.println("completed"));
    }

    public static void main(String[] args) {
        scan_test();
    }
}
