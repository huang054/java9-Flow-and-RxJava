package com.reactor.order;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Flow;
import java.util.concurrent.ForkJoinPool;

public class StockMaintain implements Flow.Subscriber<Order> {

    private Stock stock;

    private Flow.Subscription subscription =null;

    private ExecutorService executorService = ForkJoinPool.commonPool();

   public  StockMaintain(Stock stock){
       this.stock = stock;
   }
    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        System.out.println("调用 onSubscribe");
        subscription.request(3);
        this.subscription=subscription;
    }

    @Override
    public void onNext(Order order) {
       executorService.submit(()->{
          System.out.println(Thread.currentThread().getName());
          order.getItems().forEach(item->{
              try {
                  stock.remove(item.getProduct(),item.getAnount());
                  System.out.println("有"+item.getAnount()+"件商品从库存消耗");
              } catch (ProductIsOutOfStock productIsOutOfStock) {
                //  productIsOutOfStock.printStackTrace();
                  System.out.println("库存不足");
              }
          });
          subscription.request(1);
       });
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onComplete() {

    }
}
