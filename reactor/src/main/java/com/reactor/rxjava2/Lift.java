package com.reactor.rxjava2;

import io.reactivex.Observable;
import io.reactivex.ObservableOperator;
import io.reactivex.functions.Action;
import io.reactivex.observers.DisposableObserver;

public class Lift {
    public static void main(String[] args) {
        lift_test();
    }
    static void lift_test(){
        Observable.range(1,5)
                .lift(doInEmpty(()->System.out.println("operation1: empty")))
                .subscribe(v->System.out.println("operation1:"+v));
        Observable.<Integer>empty()
                .lift(doInEmpty(()->System.out.println("operation2: empty")))
                .subscribe(v->System.out.println("operation2:"+v));

    }
    public static <T>ObservableOperator<T,T> doInEmpty(Action action){
        return observer->new DisposableObserver<T>() {
            boolean isEmpty=true;
            @Override
            public void onNext(T t) {
                isEmpty=false;
                observer.onNext(t);
            }

            @Override
            public void onError(Throwable throwable) {
                 observer.onError(throwable);
            }

            @Override
            public void onComplete() {
                  if (isEmpty){
                      try {
                          action.run();
                      } catch (Exception e) {
                          onError(e);
                          return;
                      }
                  }
                  observer.onComplete();
            }
        };
    }
}
