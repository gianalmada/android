package com.example.gian.gapakelama.Orders;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gian on 16/09/2018.
 */

public class OrderArray {

    private String id;
    private String nama;
    private double harga;
    private int qty;

    private List<Orders> productList;

    public OrderArray(String id, String nama, double harga) {
        this.id = id;
        this.nama = nama;
        this.harga = harga;

        productList = new ArrayList<>();

        Orders orders = new Orders(this.id,this.nama,this.harga);
        productList.add(orders);
    }

    public List<Orders> getProductList() {
        return productList;
    }

    public void SetQty(int qty){
        this.qty = qty;
    }

    public String getId_orders() {
        return id;
    }

    public String getNama_orders() {
        return nama;
    }

    public double getHarga_orders() {
        return harga;
    }

    public int getQty_orders() {
        return qty;
    }
}
