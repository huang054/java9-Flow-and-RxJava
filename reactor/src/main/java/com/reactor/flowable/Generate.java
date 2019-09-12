package com.reactor.flowable;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Generate {
   static Flowable<Integer> randomGenerator(int min,int max){
       return Flowable.generate(emitter->emitter.onNext(ThreadLocalRandom.current().nextInt(min,max)));
   }
   static void generate_test(){
       randomGenerator(1,10000)
               .subscribeOn(Schedulers.computation())
               .doOnNext(i->System.out.println("所发的元素:"+i))
               .observeOn(Schedulers.io())
               .subscribe(i->{
                   TimeUnit.MILLISECONDS.sleep(50);
                   System.out.println("接受到的元素"+i);
               });
       try {
           Thread.sleep(1000);
       } catch (InterruptedException e) {
           e.printStackTrace();
       }
   }
   static Flowable<Integer> rangeDescend(int upperBound,int lowerBound){
       return Flowable.generate(()->new AtomicInteger(upperBound+1),(state,emitter)->{
           int current=state.decrementAndGet();
           emitter.onNext(current);
           if (current==lowerBound){
               emitter.onComplete();
           }
       });
   }
   static void generate_test2(){
       randomGenerator(-50,50)
               .doOnNext(i->System.out.println("发的元素:"+i))
               .observeOn(Schedulers.io())
               .subscribe(i->{
                   TimeUnit.MILLISECONDS.sleep(50);
                   System.out.println("所接受到的元素:"+i);
               });
       try {
           Thread.sleep(1000);
       } catch (InterruptedException e) {
           e.printStackTrace();
       }
   }

    public static void main(String[] args) {
        generate_test2();
    }
}
