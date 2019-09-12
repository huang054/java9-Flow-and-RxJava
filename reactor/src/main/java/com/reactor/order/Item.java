package com.reactor.order;

public class Item {

    private Product product;

    private long anount;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public long getAnount() {
        return anount;
    }

    public void setAnount(long anount) {
        this.anount = anount;
    }
}
