package com.example.gian.gapakelama.Database.Model.DataSource;

import com.example.gian.gapakelama.Database.Model.ModelDB.Cart;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by gian on 19/09/2018.
 */

public interface ICartDataSource {

    Flowable<List<Cart>> getCartList();
    Flowable<List<Cart>> getCartItemById(int cartItemId);
    int countCartItems();
    void emptyCart();
    void insertToCart(Cart...carts);
    void updateToCart(Cart...carts);
    void deleteCartItem(Cart carts);
}
