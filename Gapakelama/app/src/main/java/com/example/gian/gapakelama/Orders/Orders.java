package com.example.gian.gapakelama.Orders;

/**
 * Created by gian on 16/09/2018.
 */

public class Orders {

    private String id;
    private String nama;
    private double harga;
    private int qty;

    public Orders(String id, String nama, double harga) {
        this.id = id;
        this.nama = nama;
        this.harga = harga;
    }

    public void SetQty(int qty){
        this.qty = qty;
    }

    public String getId_order() {
        return id;
    }

    public String getNama_order() {
        return nama;
    }

    public double getHarga_order() {
        return harga;
    }

    public int getQty_order() {
        return qty;
    }
}
