package com.reactor.rxjava;

import io.reactivex.Observable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ObsercablePool {
    public static void main(String[] args) {
        pool_push();
    }
    private static void pool_push(){
        List<Integer> integers = new ArrayList<>();
        integers.add(1);
        integers.add(2);
        integers.add(3);
        ForkJoinPool forkJoinPool=ForkJoinPool.commonPool();
        try{
            pushCollection(integers,forkJoinPool).subscribe(x->log(x));
            pushCollection(integers,forkJoinPool).subscribe(x->log(x));
        }finally {
            try{
                forkJoinPool.shutdown();
                int shutDownDelaySec=1;
                System.out.println("等待 "+shutDownDelaySec + " 秒后结束服务");
                forkJoinPool.awaitTermination(shutDownDelaySec, TimeUnit.SECONDS);
            }catch(Exception e){
                System.out.println("捕获到forkJoinPool.awaitTermination()方法的异常"+e.getClass().getName());
            }finally {
                System.out.println("调用forkJoinPool.shutdown()结束服务");
                List<Runnable> list=forkJoinPool.shutdownNow();
                System.out.println("还剩下"+list.size()+"个任务等待执行，服务已经关闭");
            }
        }

    }

    private static Observable<Integer> pushCollection(List<Integer> integers, ForkJoinPool forkJoinPool) {
        return Observable.create(observer->{
            AtomicInteger atomicInteger = new AtomicInteger(integers.size());
            forkJoinPool.submit(()->{
                integers.forEach(id->{
                    observer.onNext(id);
                    if (atomicInteger.decrementAndGet()==0){
                        observer.onComplete();
                    }
                });
            });
        });

    }

    private static void log(Object msg){
        System.out.println(Thread.currentThread().getName()+":"+msg);
    }
}
