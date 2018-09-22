package com.example.gian.gapakelama.Database.Model.Local;

import com.example.gian.gapakelama.Database.Model.DataSource.ICartDataSource;
import com.example.gian.gapakelama.Database.Model.ModelDB.Cart;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by gian on 19/09/2018.
 */

public class CartDataSource implements ICartDataSource {

    private CartDAO cartDAO;

    private static CartDataSource instance;

    public CartDataSource(CartDAO cartDAO){
        this.cartDAO = cartDAO;
    }

    public static CartDataSource getInstance(CartDAO cartDAO){
        if(instance == null)
            instance = new CartDataSource(cartDAO);
        return instance;
    }

    @Override
    public Flowable<List<Cart>> getCartList() {
        return cartDAO.getCartList();
    }

    @Override
    public Flowable<List<Cart>> getCartItemById(int cartItemId) {
        return cartDAO.getCartItemById(cartItemId);
    }

    @Override
    public int countCartItems() {
        return cartDAO.countCartItems();
    }

    @Override
    public void emptyCart() {
        cartDAO.emptyCart();
    }

    @Override
    public void insertToCart(Cart... carts) {
        cartDAO.insertToCart(carts);
    }

    @Override
    public void updateToCart(Cart... carts) {
        cartDAO.updateToCart(carts);
    }

    @Override
    public void deleteCartItem(Cart carts) {
        cartDAO.deleteCartItem(carts);
    }
}
