package com.reactor.rxjava2;


import io.reactivex.Observable;

public class Group {
    static void groupby_test(){
        String[] monthArray={"January","Feb","March","Apl","May","Jun","July","Aug","Sept","Oct"
                ,"Nov","Dec"};
        Observable.fromArray(monthArray)
                   .groupBy(item->item.length()<=3?"THREE":item.length()<6?">4":"DEFAULT")
                   .subscribe(observable->{
                       System.out.println("##################################");
                       observable.subscribe(item->System.out.println(item+":"+observable.getKey()));
                   });
    }

    public static void main(String[] args) {
        groupby_test();
    }
}
