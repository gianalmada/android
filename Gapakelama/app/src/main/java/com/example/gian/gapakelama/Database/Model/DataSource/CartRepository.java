package com.example.gian.gapakelama.Database.Model.DataSource;

import com.example.gian.gapakelama.Database.Model.ModelDB.Cart;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by gian on 19/09/2018.
 */

public class CartRepository implements ICartDataSource {

    private ICartDataSource iCartDataSource;

    public CartRepository(ICartDataSource iCartDataSource){
        this.iCartDataSource = iCartDataSource;
    };

    private static CartRepository instance;

    public static CartRepository getInstance(ICartDataSource iCartDataSource){
        if(instance == null)
            instance = new CartRepository(iCartDataSource);
        return instance;
    }

    @Override
    public Flowable<List<Cart>> getCartList() {
        return iCartDataSource.getCartList();
    }

    @Override
    public Flowable<List<Cart>> getCartItemById(int cartItemId) {
        return iCartDataSource.getCartItemById(cartItemId);
    }

    @Override
    public int countCartItems() {
        return iCartDataSource.countCartItems();
    }

    @Override
    public void emptyCart() {
        iCartDataSource.emptyCart();
    }

    @Override
    public void insertToCart(Cart... carts) {
        iCartDataSource.insertToCart(carts);
    }

    @Override
    public void updateToCart(Cart... carts) {
        iCartDataSource.updateToCart(carts);
    }

    @Override
    public void deleteCartItem(Cart carts) {
        iCartDataSource.deleteCartItem(carts);
    }
}
