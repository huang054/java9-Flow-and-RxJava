package com.reactor.rxjava2;

import io.reactivex.Observable;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ScanReduceCollect {
    static void scan_test(){
        Observable<BigInteger> scan=Observable.range(2,10)
                .scan(BigInteger.ONE,(big,cur)->big.multiply(BigInteger.valueOf(cur)));
        scan.forEach(System.out::println);
    }

    public static void main(String[] args) {
        distinctUntilChage_test();
    }
    static void reduce_test(){
        Observable.range(10,20)
                .reduce(new ArrayList<>(),(list,item)->{
                    list.add(item);
                    return list;
                }).subscribe(System.out::println);
    }
    static void collect_test(){
        Observable.range(10,20).collect(ArrayList::new, List::add)
                .subscribe(System.out::println);
    }
    static void redece_test2(){
        Observable.range(10,20)
                .reduce(BigInteger.ONE,(big,cur)->big.multiply(BigInteger.valueOf(cur)))
                .subscribe(System.out::println);
    }
    static void distinct_test(){
        Observable<LocalDate> dateObservable = Observable.just(
          LocalDate.of(2018,1,3),
                LocalDate.of(2018,3,3),
                LocalDate.of(2018,1,13),
                LocalDate.of(2018,11,3)
                );
        dateObservable.map(LocalDate::getMonth)
                .distinct()
                .subscribe(System.out::println);
        dateObservable.distinct(LocalDate::getMonth)
                .subscribe(System.out::println);
    }
    static void distinctUntilChage_test(){
        Observable.just(1,1,1,2,2,3,3,2,1,1)
                .distinctUntilChanged().subscribe(System.out::println);
    }
    static void distinctUntilChage_test2(){
        Observable.just("January","Feb","March","Apl")
                .distinctUntilChanged(String::length).subscribe(System.out::println);
    }

}
