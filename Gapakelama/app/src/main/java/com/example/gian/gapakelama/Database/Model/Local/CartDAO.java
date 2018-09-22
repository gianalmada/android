package com.example.gian.gapakelama.Database.Model.Local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.gian.gapakelama.Database.Model.ModelDB.Cart;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by gian on 19/09/2018.
 */
@Dao
public interface CartDAO {

    @Query("SELECT * FROM Cart")
    Flowable<List<Cart>> getCartList();

    @Query("SELECT * FROM Cart WHERE id=:cartItemId")
    Flowable<List<Cart>> getCartItemById(int cartItemId);

    @Query("SELECT COUNT(*) FROM Cart")
    int countCartItems();

    @Query("DELETE FROM Cart")
    void emptyCart();

    @Insert
    void insertToCart(Cart...carts);

    @Update
    void updateToCart(Cart...carts);

    @Delete
    void deleteCartItem(Cart carts);

}
