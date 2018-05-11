package com.yberry.dinehawaii.Model;

import java.util.ArrayList;

/**
 * Created by Peter on 20-Apr-17.
 */

public class MenuCart {

    public ArrayList<OrderItemsDetailsModel> cartItems = new ArrayList<OrderItemsDetailsModel>();

    public OrderItemsDetailsModel getProducts(int position){
        return cartItems.get(position);
    }
    public  void setProducts(OrderItemsDetailsModel Products){
        cartItems.add(Products);
    }
    public  int getCartsize(){

        return cartItems.size();
    }
    public  boolean CheckProductInCart(OrderItemsDetailsModel aproduct){
        return cartItems.contains(aproduct);
    }

    public ArrayList<OrderItemsDetailsModel> getCartItems() {
        return cartItems;
    }
}

