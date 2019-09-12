package com.reactor;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Flow;
import java.util.concurrent.Future;

public class DockerXDemoPublisher<T> implements Flow.Publisher<T>,AutoCloseable {
    private final ExecutorService executorService;

    private CopyOnWriteArrayList<DockerXDemoSubscription> list = new CopyOnWriteArrayList();

    public DockerXDemoPublisher(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public void submit(T item){
        System.out.println("开始发布元素 item : "+item);
        list.forEach(e->{
            e.future=executorService.submit(()->{
                e.subscriber.onComplete();
            });
        });
    }
    @Override
    public void close() {

        list.forEach(e->{
            System.out.println("e :" +e);
            e.future=executorService.submit(()->{e.subscriber.onComplete();});
        });
    }

    @Override
    public void subscribe(Flow.Subscriber<? super T> subscriber) {
            subscriber.onSubscribe(new DockerXDemoSubscription(subscriber,executorService));
            list.add(new DockerXDemoSubscription(subscriber,executorService));
    }
    static class DockerXDemoSubscription<T> implements Flow.Subscription{
       private final Flow.Subscriber<? super T> subscriber;

       private final ExecutorService executorService;

       private Future<?> future;

       private T item;
       private boolean completed;

        DockerXDemoSubscription(Flow.Subscriber<? super T> subscriber, ExecutorService executorService) {
            this.subscriber = subscriber;
            this.executorService = executorService;
        }


        @Override
        public void request(long n) {
            if(n!=0&&!completed){
                if(n<0){
                    IllegalArgumentException exception = new IllegalArgumentException();
                    executorService.execute(()->subscriber.onError(exception));
                }else{
                    future = executorService.submit(()->{
                        subscriber.onNext(item);
                    });
                }
            }else{
                subscriber.onComplete();
            }
        }

        @Override
        public void cancel() {
            completed=true;
            if (future!=null&&!future.isCancelled()){
                this.future.cancel(true);
            }
        }
    }
}
