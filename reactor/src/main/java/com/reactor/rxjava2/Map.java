package com.reactor.rxjava2;


import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.internal.operators.observable.ObservableError;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Map {

    static void map_test(){
        String[] monthArrat={"Jan","Feb","Mar","Apl","May","Jun"};
        Observable.fromArray(monthArrat)
                .map(String::toUpperCase)
                .subscribe(System.out::println);
        Observable.just(1,2,3,4,5,6,7,8,9,10)
                .filter(i->i%3>0)
                .map(i->"#"+i*10)
                .filter(s->s.length()<4)
                .subscribe(System.out::println);
    }

    public static void main(String[] args) {
        flatmap_test4();
    }
    static void flatmap_test(){
        Observable.range(1,3).flatMap(item-> Observable.range(item,3))
                .subscribe(value->System.out.println("value:"+value));
    }
    static void flatmap_test2(){
        List<List<String>> lists = new ArrayList<>();
        for (int i=1;i<4;i++){
            List main_list = new ArrayList();

            for (int j=1;j<3;j++){
                main_list.add(i+"-打呼-"+j);
            }
            lists.add(main_list);
        }
        Observable.fromArray(lists.toArray())
                .map(item->((List)item).toArray())
                .flatMap(Observable::fromArray)
                .subscribe(System.out::println);
        Observable.fromArray(lists.toArray())
                .flatMap(item->Observable.fromArray(((List)item).toArray()))
                .subscribe(System.out::println);
        Observable.fromArray(lists.toArray())
                .map(item->Observable.fromArray(((List)item).toArray()))
                .subscribe(System.out::println);

    }
    static void flatmap_test3(){
        List<List<String>> lists = new ArrayList<>();
        for (int i=1;i<4;i++){
            List main_list = new ArrayList();

            for (int j=1;j<3;j++){
                main_list.add(i+"-打呼-"+j);
            }
            lists.add(main_list);
        }
        Observable.fromArray(lists.toArray())
                .flatMapIterable(item->(List)item)
                .subscribe(System.out::println);
    }
    static Observable<Long> upload(int id){
        return Observable.just(1L,2L,3L,4L,5L,6L,7L,8L,9L,10L);

    }
    static Observable<String> rate(Integer id){
        return Observable.just("上传完毕！");
    }
    static void flatmap_test4(){
        int id= new Random().nextInt(100);
        System.out.println(id);
        upload(id)
                .map(item->{
                    if((id%2)==1){
                        return item;
                    }else{
                        throw new RuntimeException("我就是个bug");
                    }
                }).flatMap(bety->{
                    return  Observable.empty();
        },e->Observable.just(e.getMessage()),()->rate(id)
        ).subscribe(item->System.out.println("we got :"+item.toString()+
                "from the observable"),throwable -> System.out.println("异常->"+throwable.getMessage())
       ,()->System.out.println("completed") );
    }
}
