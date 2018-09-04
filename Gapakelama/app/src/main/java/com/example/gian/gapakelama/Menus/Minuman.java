package com.example.gian.gapakelama.Menus;

/**
 * Created by gian on 04/09/2018.
 */

public class Minuman {
    private String id;
    private String nama;
    private double harga;
    private String image;


    public Minuman(String id, String nama, double harga, String image) {
        this.id = id;
        this.nama = nama;
        this.harga = harga;
        this.image = "http://gapakelama.net/assets/images/minuman/"+image;
    }

    public String getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public double getHarga() {
        return harga;
    }

    public String getImage() {
        return image;
    }
}
