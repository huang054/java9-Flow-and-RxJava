package com.reactor.order;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.SubmissionPublisher;

public class Test {
    public static void main(String[] args) {

        Stock stock = new Stock();
        SubmissionPublisher<Order> submissionPublisher = new SubmissionPublisher<>();
        Product product = new Product();
        stock.store(product,40);
        Item item = new Item();
        item.setProduct(product);
        item.setAnount(10);
        Order order = new Order();
        List<Item> lists = new ArrayList<>();
        lists.add(item);
        order.setItems(lists);
        submissionPublisher.subscribe(new StockMaintain(stock));
        for (int i=0;i<10;i++){
            submissionPublisher.submit(order);

        }
        System.out.println("订单已经提交");
        for (int j=0;j<10;j++){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        submissionPublisher.close();
        System.out.println("处理完成");
    }
}
