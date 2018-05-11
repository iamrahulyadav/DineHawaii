package com.yberry.dinehawaii;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.yberry.dinehawaii.Model.MenuCart;
import com.yberry.dinehawaii.Model.OrderItemsDetailsModel;

import io.fabric.sdk.android.Fabric;
import java.util.ArrayList;

/**
 * Created by PRINCE 9977123453 on 13-02-17.
 */

public class  MyApplication extends android.support.multidex.MultiDexApplication {
    private static GoogleAnalytics sAnalytics;
    private static Tracker sTracker;
    private static MyApplication instance;
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());


        instance = this;
    }
    public static MyApplication getInstance(){
        return instance;
    }

    private ArrayList<OrderItemsDetailsModel> myproducts = new ArrayList<OrderItemsDetailsModel>();

    private MenuCart myCart = new MenuCart();

    public OrderItemsDetailsModel getProducts(int pPosition){
        return myproducts.get(pPosition);
    }
    public void  setProducts(OrderItemsDetailsModel products){
        myproducts.add(products);
    }
    public MenuCart getCart(){
        return myCart;
    }

    public int  getProductArraylistsize(){
        return myproducts.size();
    }

}
