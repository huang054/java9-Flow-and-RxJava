package com.reactor.rxjava2;

import io.reactivex.Observable;

import java.util.ArrayList;
import java.util.List;

/**
 * take(n)获取前n个，skip(n)忽略前n个
 */
public class Take {
    public static void main(String[] args) {
        take_skip_test();
    }
    static void take_skip_test(){
        Observable<Integer> range=Observable.range(1,5);
        range.take(3)
                .collect(ArrayList::new, List::add)
                .subscribe(System.out::println);
        range.skip(3)
                .collect(ArrayList::new, List::add)
                .subscribe(System.out::println);
        range.skip(5)
                .collect(ArrayList::new, List::add)
                .subscribe(System.out::println);

        range.takeLast(2)
                .collect(ArrayList::new, List::add)
                .subscribe(System.out::println);
        range.skipLast(2)
                .collect(ArrayList::new, List::add)
                .subscribe(System.out::println);

        range.takeUntil(x->x==3)
                .collect(ArrayList::new, List::add)
                .subscribe(System.out::println);
        range.takeWhile(x->x!=3)
                .collect(ArrayList::new, List::add)
                .subscribe(System.out::println);
        range.all(x->x!=4).subscribe(System.out::println);
        range.contains(4).subscribe(System.out::println);
    }
}
