package com.yberry.dinehawaii.Util;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
/**
 * Created by MAX on 23-Feb-17.
 */

public class AppControler extends Application {

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }
/*
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
    }*/

}

 /*public class MainActivity extends Activity {
    *//** Called when the activity is first created. *//*
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final LinearLayout layout = (LinearLayout)findViewById(R.id.linearMain);
        final Button btn = (Button)findViewById(R.id.second);
        final Controller ct = (Controller) getApplicationContext();
        ModelProducts products = null;
        for(int i= 1; i<=7;i++){
            int Price = 15+ i;
            products = new ModelProducts("Product Item" +i, "Description"+i, Price);
            ct.setProducts(products);
        }
        int productsize = ct.getProductArraylistsize();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        for (int j=0;j< productsize;j++){
            String pName = ct.getProducts(j).getProductName();
            int pPrice = ct.getProducts(j).getProductPrice();
            LinearLayout la = new LinearLayout(this);
            la.setOrientation(LinearLayout.HORIZONTAL);
            TextView tv = new TextView(this);
            tv.setText(" "+pName+" ");
            la.addView(tv);
            TextView tv1 = new TextView(this);
            tv1.setText("$"+pPrice+" ");
            la.addView(tv1);
            final Button btn1 = new Button(this);
            btn1.setId(j+1);
            btn1.setText("Add to Cart");
            btn1.setLayoutParams(params);
            final int index = j;
            btn1.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
// TODO Auto-generated method stub
                    Log.i("TAG", "index:"+index);
                    ModelProducts productsObject = ct.getProducts(index);
                    if(!ct.getCart().CheckProductInCart(productsObject)){
                        btn1.setText("Item Added");
                        ct.getCart().setProducts(productsObject);
                        Toast.makeText(getApplicationContext(), "New CartSize:" +ct.getCart().getCartsize(),Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "Products"+(index+1)+"Already Added",Toast.LENGTH_LONG ).show();
                    }
                }
            });
            la.addView(btn1);
            layout.addView(la);
        }
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
// TODO Auto-generated method stub
                Intent in = new Intent(getBaseContext(),Screen2.class);
                startActivity(in);
            }
        });
    }
}*/
