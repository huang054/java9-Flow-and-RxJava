package com.reactor.rxjava2;

import com.google.common.collect.ImmutableList;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;

import javax.management.ImmutableDescriptor;
import java.util.Collections;
import java.util.stream.Collectors;

import static com.google.common.collect.ImmutableList.toImmutableList;

public class Compose {
    static void compuse_test(){
        Observable.just("Apple","Orange","Appla","Eatla","HOHO","mETA")
                .collect(ImmutableList::builder,ImmutableList.Builder::add)
                .map(ImmutableList.Builder::build)
                .subscribe(System.out::println);
        Observable.range(1,5)
                .collect(ImmutableList::builder,ImmutableList.Builder::add)
                .map(ImmutableList.Builder::build)
                .subscribe(System.out::println);
    }

    static void compose_test2(){
        Observable.just("Apple","Orange","Appla","Eatla","HOHO","mETA")
                .compose(toImmutableList())
                .subscribe(System.out::println);
        Observable.range(1,5)
                .compose(toImmutableList())
                .subscribe(System.out::println);
    }
    public static <T>ObservableTransformer<T,ImmutableList<T>> toImmutableList(){
        return upstream->upstream.collect(ImmutableList::<T>builder,ImmutableList.Builder::add)
                .map(ImmutableList.Builder::build).toObservable();
    }

    public static void main(String[] args) {
        compose_test2();
    }
}
