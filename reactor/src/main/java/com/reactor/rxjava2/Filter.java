package com.reactor.rxjava2;


import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class Filter {

    static  void filter_demo(){
        String[] monthArrat={"Jan","Feb","Mar","Apl","May","Jun"};
        Observable.fromArray(monthArrat)
                .filter(item->item.contains("p"))
                .count()
                .subscribe(item->System.out.println(item));
        Observable.fromArray(monthArrat)
                .filter(item->item.contains("p"))
                .subscribe(item->System.out.println(item),Throwable::printStackTrace,()->System.out.println("done"));
    }

    public static void main(String[] args) {
        mul_subject();
    }
    static void mul_subject(){
        PublishSubject<Object> publishSubject = PublishSubject.create();
        PublishSubject<Object> publishSubject1 = PublishSubject.create();
        PublishSubject<Object> publishSubject2 = PublishSubject.create();
        PublishSubject<Object> publishSubject3 = PublishSubject.create();
        publishSubject.subscribe(x->{
            System.out.println("一郎神："+x);
            publishSubject1.onNext(x);
        },throwable -> System.out.println("异常指向：一郎神->"+throwable.getMessage()),()->System.out.println("completed")
        ,disposable->System.out.println("onSubscribe"));
        publishSubject1.subscribe(x->{
                    System.out.println("二郎神："+x);
                    publishSubject2.onNext(x);
                },throwable ->publishSubject.onError(new RuntimeException("异常指向：二郎神->"+throwable.getMessage())),()->System.out.println("completed")
                ,disposable->System.out.println("onSubscribe"));
        publishSubject2.subscribe(x->{
                    System.out.println("三郎神："+x);
                    publishSubject3.onNext(x);
                },throwable ->publishSubject1.onError(new RuntimeException("异常指向：三郎神->"+throwable.getMessage())),()->System.out.println("completed")
                ,disposable->System.out.println("onSubscribe"));
        publishSubject3.subscribe(x->{
                    System.out.println("四郎神："+x);
                   if ((Long)x==2L){
                       throw new RuntimeException("四郎神得异常");
                   }
                },throwable ->publishSubject2.onError(new RuntimeException(throwable.getMessage())),()->System.out.println("completed")
                ,disposable->System.out.println("onSubscribe"));
        publishSubject.onNext(1L);
        publishSubject.onNext(2L);

    }
}
