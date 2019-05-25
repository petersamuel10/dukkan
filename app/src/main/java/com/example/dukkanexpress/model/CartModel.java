package com.example.dukkanexpress.model;

import java.util.ArrayList;

public class CartModel {

    private ProductModel product;
    private  float total;

    public CartModel() {
    }

    public CartModel(ProductModel product, float total) {
        this.product = product;
        this.total = total;
    }

    public ProductModel getProduct() {
        return product;
    }

    public void setProduct(ProductModel product) {
        this.product = product;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }
}
