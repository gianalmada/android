package com.example.gian.gapakelama.Database.Model.ModelDB;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by gian on 19/09/2018.
 */
@Entity(tableName = "Cart")
public class Cart {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "id_menu")
    public String id_menu;

    @ColumnInfo(name = "nama_menu")
    public String nama_menu;

    @ColumnInfo(name = "harga_menu")
    public double harga_menu;

    @ColumnInfo(name = "qty_menu")
    public int qty_menu;

    @ColumnInfo(name = "catatan_menu")
    public String catatan_menu;

}
