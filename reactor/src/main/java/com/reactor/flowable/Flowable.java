package com.reactor.flowable;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

public class Flowable {
    static final class Customer{
        final int id;

        Customer(int id) {
            this.id = id;
            System.out.println("正在构造"+id+" Customer");
        }
    }
    static void Observable_Customer_test(){
        Observable.range(1,999_999)
                .map(Customer::new)
                .subscribe(customer -> {
                    Thread.sleep(20);
                    System.out.println("所获取到的id:"+customer.id);
                });
    }

    static void Observable_Customer_test1() {
        Observable.range(1,999)
                .map(Customer::new)
                .observeOn(Schedulers.io())
                .subscribe(customer -> {
                    Thread.sleep(20);
                    System.out.println("所获取到的id:"+customer.id);
                });
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static void Observable_Customer_test2() {
     io.reactivex.Flowable.range(1,999)
                .map(Customer::new)
                .observeOn(Schedulers.io())
                .subscribe(customer -> {
                    Thread.sleep(20);
                    System.out.println("所获取到的id:"+customer.id);
                });
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

  static void obserrvable_test(){
        Observable<Integer> integerObservable =Observable.create(emitter->{
            for (int i=0;i<=100;i++){
                if (emitter.isDisposed()){
                    return;

                }
                emitter.onNext(i);
            }
            emitter.onComplete();
        });
        integerObservable.observeOn(Schedulers.io())
                .subscribe(System.out::println,Throwable::printStackTrace,()->System.out.println("completed"));
      try {
          TimeUnit.SECONDS.sleep(10);
      } catch (InterruptedException e) {
          e.printStackTrace();
      }
  }
    static void flowable_test(){
        io.reactivex.Flowable<Object> integerObservable = io.reactivex.Flowable.create(emitter->{
            for (int i=0;i<=100;i++){
                if (emitter.isCancelled()){
                    return;

                }
                emitter.onNext(i);
            }
            emitter.onComplete();
        }, BackpressureStrategy.BUFFER);
        integerObservable.observeOn(Schedulers.computation())
                .subscribe(System.out::println,Throwable::printStackTrace,()->System.out.println("completed"));
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static void flowablr_creatr(){
        io.reactivex.Flowable<Object> objectFlowable = io.reactivex.Flowable.create(emitter->{
            for (int i=0;i<=1000;i++){
                if (emitter.isCancelled()){
                    return;
                }
                new Customer(i);
                emitter.onNext(i);
            }
        },BackpressureStrategy.BUFFER);
        objectFlowable.doOnNext(s->System.out.println("push:"+s))
                .observeOn(Schedulers.computation())
                .subscribe(customer->{
                    TimeUnit.MILLISECONDS.sleep(20);
                    System.out.println("id:"+customer);
                });
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    static void flowablr_creatr1(){
        io.reactivex.Flowable<Object> objectFlowable = io.reactivex.Flowable.create(emitter->{
            for (int i=0;i<=1000;i++){
                if (emitter.isCancelled()){
                    return;
                }
                new Customer(i);
                emitter.onNext(i);
            }
        },BackpressureStrategy.LATEST);
        objectFlowable.doOnNext(s->System.out.println("push:"+s))
                .observeOn(Schedulers.computation())
                .subscribe(customer->{
                    TimeUnit.MILLISECONDS.sleep(20);
                    System.out.println("id:"+customer);
                });
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    static void flowablr_creatr2(){
        io.reactivex.Flowable<Object> objectFlowable = io.reactivex.Flowable.create(emitter->{
            for (int i=0;i<=1000;i++){
                if (emitter.isCancelled()){
                    return;
                }
                new Customer(i);
                emitter.onNext(i);
            }
        },BackpressureStrategy.DROP);
        objectFlowable.doOnNext(s->System.out.println("push:"+s))
                .observeOn(Schedulers.computation())
                .subscribe(customer->{
                    TimeUnit.MILLISECONDS.sleep(20);
                    System.out.println("id:"+customer);
                });
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        flowablr_creatr2();
    }
}
