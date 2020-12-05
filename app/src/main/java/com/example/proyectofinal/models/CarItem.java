package com.example.proyectofinal.models;

public class CarItem {
    private Product product;
    private int quantity;

    public CarItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public CarItem() {
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
