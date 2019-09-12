package com.reactor.rxjava;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.internal.functions.ObjectHelper;

public class ObservableDemo2 {
    public static void main(String[] args) {
        String[] monthArray={"Jan","Feb","Mar","Apl","May","Jun"};
        Observable.defer(()->{
           return Observable.fromArray(monthArray);
        }).subscribe(System.out::println,Throwable::printStackTrace,
                ()->System.out.println("completed"));

        Observable<Object> empty=Observable.empty();
        empty.subscribe(System.out::println,Throwable::printStackTrace,
                ()->System.out.println("COMPLETED NORAMLLY"));
        Observable<String> error = Observable.error(new Exception("wo got an exception"));
        error.subscribe(System.out::println,System.out::println,
                ()->System.out.println("end"));
        range_test();
        just_test();
        just_test1();
    }
    private static void log(Object msg){
        System.out.println(Thread.currentThread().getName()+":"+msg);
    }
    private static void range_test(){
        log("range_test_begin");
        Observable.range(5,3).subscribe(ObservableDemo2::log);
        log("range_test_end");
    }

    private static void just_test(){
        log("just_test_begin");
        Observable.just("Jan","Feb").subscribe(ObservableDemo2::log);
        log("just_test_end");
    }

    private static void just_test1(){
        log("just_test_begin");
        just("Jan").subscribe(ObservableDemo2::log);
        log("just_test_end");
    }

    public static <T> Observable<T> just(T item){
        ObjectHelper.requireNonNull(item,"this item is null");
        return Observable.create(subscriber->{
            subscriber.onNext(item);
            subscriber.onComplete();
        });
    }
}
