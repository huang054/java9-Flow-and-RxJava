package com.reactor.rxjava;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.operators.observable.ObservablePublish;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.AsyncSubject;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.ReplaySubject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
//refcount and subject
public class RefCount {
    public static void main(String[] args) {
        replaySubject_conf_test();
    }
    static void infinite_refCount_publish_test(){

            List<Integer> list = new ArrayList<>();
            for (int i=0;i<10;i++){
                list.add(i);
            }
            ForkJoinPool forkJoinPool =ForkJoinPool.commonPool();
            try {
                Observable<Integer> observablePublish=pushCollectionDANGER(list,forkJoinPool).publish().refCount();
                observablePublish.subscribe(x-> {
                    System.out.println("一郎神:" + x);
                    TimeUnit.SECONDS.sleep(2);
                },Throwable::printStackTrace,()->System.out.println("conpleted"));
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                observablePublish.subscribe(x-> {
                    System.out.println("二郎神:" + x);
                    TimeUnit.SECONDS.sleep(1);
                },Throwable::printStackTrace,()->System.out.println("conpleted"));

                try {
                    TimeUnit.SECONDS.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }finally {
                try {
                    forkJoinPool.shutdown();
                    int shutdown=2;
                    System.out.println("等待"+shutdown+"秒后结束服务");
                    forkJoinPool.awaitTermination(shutdown,TimeUnit.SECONDS);
                }catch (Exception e){
                    System.out.println("捕获到 forkJoinPool.awaitTermination异常"+e.getClass().getName());
                }finally {
                    System.out.println("调用forkJoinPool.shutdown()结束服务");
                    List<Runnable> lists=forkJoinPool.shutdownNow();
                    System.out.println("还剩下"+lists.size()+"个任务等待执行，服务已经关闭");
                }

            }
        }

        private static Observable<Integer> pushCollectionDANGER(List<Integer> list, ForkJoinPool forkJoinPool) {
            return Observable.create(observer->{
                System.out.println("connection");
                AtomicInteger atomicInteger = new AtomicInteger(list.size());
                list.forEach(id->forkJoinPool.submit(()->{
                    observer.onNext(id);
                    if (atomicInteger.decrementAndGet()==0){
                        forkJoinPool.shutdownNow();
                        observer.onComplete();
                    }
                }));
            });
        }
    static void replay_punlishSubject_test(){
        PublishSubject<Object> publishSubject = PublishSubject.create();
        ConnectableObservable<Object> replay=publishSubject.replay();
        ForkJoinPool forkJoinPool=ForkJoinPool.commonPool();
        List<Integer> list = new ArrayList<>();
        for (int i=0;i<10;i++){
            list.add(i);
        }
        Disposable disposable = replay.subscribe(x->{
            System.out.println("一郎神:"+x);
        },Throwable::printStackTrace,()->System.out.println("completed"));
        Disposable disposable1 = replay.subscribe(x->{
            System.out.println("二郎神:"+x);
        },Throwable::printStackTrace,()->System.out.println("completed"));
        Disposable disposable2 = replay.subscribe(x->{
            System.out.println("三郎神:"+x);
        },Throwable::printStackTrace,()->System.out.println("completed"));
        AtomicInteger atomicInteger = new AtomicInteger(list.size());
        try{
            forkJoinPool.submit(()->{
                list.forEach(id->{
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    publishSubject.onNext(id);
                    if (atomicInteger.decrementAndGet()==0){
                        publishSubject.onComplete();
                    }
                });
            });
            replay.connect();
            TimeUnit.SECONDS.sleep(2);
            disposable.dispose();
            TimeUnit.SECONDS.sleep(1);
            publishSubject.onComplete();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                forkJoinPool.shutdown();
                int shutdown=15;
                System.out.println("等待"+shutdown+"秒后结束服务");
                forkJoinPool.awaitTermination(shutdown,TimeUnit.SECONDS);
            }catch (Exception e){
                System.out.println("捕获到 forkJoinPool.awaitTermination异常"+e.getClass().getName());
            }finally {
                System.out.println("调用forkJoinPool.shutdown()结束服务");
                List<Runnable> lists=forkJoinPool.shutdownNow();
                System.out.println("还剩下"+lists.size()+"个任务等待执行，服务已经关闭");
            }
        }
     }

     static void asyncSubject_test(){
         AsyncSubject<Object> asyncSubject =AsyncSubject.create();
         asyncSubject.subscribe(x-> log("一郎神:"+x),Throwable::printStackTrace,()->System.out.println("completed"));
         asyncSubject.onNext(1L);
         asyncSubject.onNext(2L);
         asyncSubject.onNext(10L);
         asyncSubject.onComplete();
     }
    private static void log(Object msg){
        System.out.println(Thread.currentThread().getName()+":"+msg);
    }

    static void behaviorSubject_test(){
        BehaviorSubject<Object> behaviorSubject =BehaviorSubject.create();
        behaviorSubject.onNext(1L);
        behaviorSubject.onNext(2L);
        behaviorSubject.onNext(10L);
        //可注释
        behaviorSubject.onError(new RuntimeException("error"));
        behaviorSubject.subscribe(x->log("一郎神:"+x),Throwable::printStackTrace,()->System.out.println("completed"),
                disposable -> System.out.println("disposable")
        );
        behaviorSubject.onNext(11L);
        behaviorSubject.onComplete();
    }
    static void replaySubject_test(){
        ReplaySubject<Object> replaySubject = ReplaySubject.create();
        replaySubject.onNext(1L);
        replaySubject.onNext(2L);
        replaySubject.onNext(10L);
        replaySubject.subscribe(x->log("一郎神:"+x),Throwable::printStackTrace,()->System.out.println("completed"),
                disposable -> System.out.println("disposable")
        );
        replaySubject.onNext(101L);
        replaySubject.onComplete();
    }
    static void replaySubject_conf_test() {
        ReplaySubject<Object> replaySubject = ReplaySubject.createWithSize(5);
        ReplaySubject<Object> replaySubject1=ReplaySubject.createWithTime(5,TimeUnit.SECONDS, Schedulers.computation());
        ReplaySubject<Object> replaySubject2=ReplaySubject.createWithTimeAndSize(5,TimeUnit.SECONDS, Schedulers.computation(),5);
        for (int i=1;i<10;i++){
            replaySubject2.onNext(i);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        replaySubject2.subscribe(x->log("一郎神:"+x),Throwable::printStackTrace,()->System.out.println("completed"),
                disposable -> System.out.println("disposable")
        );
        replaySubject2.onNext(10L);
        replaySubject2.onComplete();
    }
}
