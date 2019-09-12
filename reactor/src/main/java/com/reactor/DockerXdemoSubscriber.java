package com.reactor;


import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Flow;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class DockerXdemoSubscriber<T> implements Flow.Subscriber<T>{

    private String name;

    private Flow.Subscription subscription;
    final long bufferSize;

   // long count;

    public String getName() {
        return name;
    }

    public Flow.Subscription getSubscription() {
        return subscription;
    }

    public DockerXdemoSubscriber(long bufferSize, String name){
        this.bufferSize=bufferSize;
        this.name=name;
    }
    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        (this.subscription=subscription).request(bufferSize);
        System.out.println("开始onSubscribe订阅");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onNext(Object item) {

        System.out.println(Thread.currentThread().getName()+" name:"+name+" item: ");
        System.out.println(name+" received: "+item);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onError(Throwable throwable) {
            throwable.printStackTrace();
    }
    @Override
    public void onComplete() {
       System.out.println(name+" completed");
    }
    private static void demoSubscribe(DockerXDemoPublisher<Integer> publisher,String subscriberName){
        DockerXdemoSubscriber<Integer> subscriber = new DockerXdemoSubscriber<>(4L,subscriberName);
        publisher.subscribe(subscriber);
    }

    public static void main(String[] args) {
        ExecutorService executorService = ForkJoinPool.commonPool();
        try(DockerXDemoPublisher<Integer> publisher = new DockerXDemoPublisher<>(executorService)) {
            demoSubscribe(publisher,"One");
            demoSubscribe(publisher,"Two");
            demoSubscribe(publisher,"Three");
            IntStream.range(1,5).forEach(publisher::submit);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try{
                executorService.shutdown();
                int shutDownDelaySec=1;
                System.out.println("等待 "+shutDownDelaySec + " 秒后结束服务");
                executorService.awaitTermination(shutDownDelaySec, TimeUnit.SECONDS);
            }catch(Exception e){
                System.out.println("捕获到executorService.awaitTermination()方法的异常"+e.getClass().getName());
            }finally {
                System.out.println("调用executorService.shutdown()结束服务");
                List<Runnable> list=executorService.shutdownNow();
                System.out.println("还剩下"+list.size()+"个任务等待执行，服务已经关闭");
            }
        }
    }
}
