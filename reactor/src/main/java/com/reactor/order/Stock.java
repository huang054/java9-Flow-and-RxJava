package com.reactor.order;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Stock {
    private final Map<Product,StockItem> stockItemMap = new ConcurrentHashMap<>();

    private StockItem getItem(Product product){
        StockItem stockItem=stockItemMap.putIfAbsent(product,new StockItem());
        return stockItem==null?stockItemMap.get(product):stockItem;
    }

    public void store(Product product,long amount){
        getItem(product).store(amount);
    }

    public void remove(Product product,long amount) throws ProductIsOutOfStock {
        if (getItem(product).remove(amount)!=amount){
            throw  new ProductIsOutOfStock(product);
        }
    }
}
