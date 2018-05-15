package com.yberry.dinehawaii.Customer.Activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Model.OrderItemsDetailsModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferences;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.customview.CustomTextView;
import com.yberry.dinehawaii.database.DatabaseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.yberry.dinehawaii.Util.Util.context;

public class ThankYouScreenActivity extends AppCompatActivity {
    public static final String TAG = "ThankYouScreenActivity";
    ArrayList<OrderItemsDetailsModel> orderItemsDetailsModels = new ArrayList<>();
    OrderItemsDetailsModel orderModel = new OrderItemsDetailsModel();
    String coupon = "", couponId = "0", couponAmount = "0", order_id = "";
    DatabaseHandler mydb;
    ArrayList<OrderItemsDetailsModel> arrayList;
    Button gotoh, feedback_btn;
    CustomTextView foodpreptime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you_screen);
        context = this;
        arrayList = getIntent().getParcelableArrayListExtra("arrayList");
        coupon = getIntent().getStringExtra("couponCode");

        if (!getIntent().getStringExtra("couponId").equalsIgnoreCase("") && getIntent().getStringExtra("couponId") != null) {
            couponId = getIntent().getStringExtra("couponId");
        }

        if (!getIntent().getStringExtra("couponAmount").equalsIgnoreCase("") && getIntent().getStringExtra("couponAmount") != null) {
            couponAmount = getIntent().getStringExtra("couponAmount");
        }

        placeOrder();
        init();
    }

    private void init() {

        Log.e("check_data012", AppPreferences.getOrderType(ThankYouScreenActivity.this));
        gotoh = (Button) findViewById(R.id.gotoHome);
        feedback_btn = (Button) findViewById(R.id.feedback_btn);
        foodpreptime = (CustomTextView) findViewById(R.id.foodtime);
        foodpreptime.setText(AppPreferences.getPrepTime(ThankYouScreenActivity.this) + " " + getString(R.string.thanku2));
        gotoh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppPreferences.setDeliveryName(context, "");
                AppPreferences.setDeliveryContact(context, "");
                AppPreferences.setDeliveryAddress(context, "");

                Intent intent = new Intent(getApplicationContext(), CustomerNaviDrawer.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        feedback_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), CustomerFeedbackActivity.class);
                intent1.setAction("Order");
                intent1.putExtra("orderid", order_id);
                intent1.putExtra("busid", AppPreferences.getBusiID(ThankYouScreenActivity.this));
                startActivity(intent1);
                finish();
            }
        });

        Log.e(TAG, AppPreferences.getRadioValue(ThankYouScreenActivity.this));
    }

    private void placeOrder() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", AppConstants.CUSTOMER_USER.GET_ORDER_DETAILS);
        JsonObject object = new JsonObject();
        object.addProperty("business_id", AppPreferences.getBusiID(ThankYouScreenActivity.this));
        object.addProperty("user_id", AppPreferences.getCustomerid(ThankYouScreenActivity.this));
        object.addProperty("delivery_name", AppPreferences.getDeliveryName(ThankYouScreenActivity.this));//, AppPreferencesBuss.getBussiId(getActivity()));
        object.addProperty("delivery_address", AppPreferences.getDeliveryAddress(ThankYouScreenActivity.this));
        object.addProperty("delivery_mobile", AppPreferences.getDeliveryContact(ThankYouScreenActivity.this));
        object.addProperty("adderess_save_status", AppPreferences.getRadioValue(ThankYouScreenActivity.this));
        object.addProperty("paymentType", "paypal");
        object.addProperty("txn_id", AppPreferences.getTranctionId(ThankYouScreenActivity.this));
        object.addProperty("payment_gross", AppPreferences.getPrice(ThankYouScreenActivity.this));
        object.addProperty("currency_code", "");
        object.addProperty("payment_status", "pending");
        object.addProperty("e_gift_balance", couponAmount);
        object.addProperty("e_gift_id", couponId);
        object.addProperty("grandtotal", AppPreferencesBuss.getGrandTotal(context));
        object.addProperty("loyalty_points", (AppPreferencesBuss.getFinalLoyalityPoint(context)));
        object.addProperty("gratuity", (AppPreferencesBuss.getGratuity(context)));
        object.addProperty("credits", (AppPreferencesBuss.getCredit(context)));
        object.addProperty("order_type", AppPreferences.getOrderType(ThankYouScreenActivity.this)); //  AppPreferences.getOrderType(ThankYouScreenActivity.this)
        object.addProperty("today_time", AppPreferences.getOrderTime(ThankYouScreenActivity.this)); //order time
        object.addProperty("future_time", AppPreferences.getOrderTime(ThankYouScreenActivity.this));
        jsonObject.add("contactDetails", object);
        Log.e("Place order request", object.toString());
        JsonArray jsonArray = new JsonArray();
        for (int i = 0; i < arrayList.size(); i++) {
            OrderItemsDetailsModel model = arrayList.get(i);
            JsonObject orderDetailsObject = new JsonObject();
            orderDetailsObject.addProperty("menu_id", model.getMenu_id());
            orderDetailsObject.addProperty("cat_id", model.getCat_id());
            orderDetailsObject.addProperty("qty", model.getItemQuantity());
            orderDetailsObject.addProperty("price", model.getItemPrice());
            orderDetailsObject.addProperty("item_customization", model.getItemCustomiationList());
            orderDetailsObject.addProperty("iteam_message", model.getMessage());
            jsonArray.add(orderDetailsObject);
            Log.e(TAG, orderDetailsObject.toString());
        }

        jsonObject.add("orderDetails", jsonArray);

        Log.e(TAG, "Request Place Order >>> " + jsonObject.toString());

        placeOrderTask(jsonObject);
    }

    private void placeOrderTask(JsonObject jsonObject) {
        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.order_details(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                Log.e(TAG, "Response Place Order >>> " + response.body().toString());
                String s = response.body().toString();

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            order_id = jsonObject1.getString("id");
                            feedback_btn.setEnabled(true);
                            //cart items remove
                            mydb = new DatabaseHandler(ThankYouScreenActivity.this);
                            mydb.deleteCartitem();
                        }
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject object = jsonArray.getJSONObject(0);
                        //nodata.setVisibility(View.VISIBLE);
                        //  showAlertDialog();
                        Toast.makeText(ThankYouScreenActivity.this, object.getString("msg"), Toast.LENGTH_LONG).show();
                        Log.e("onResponse", object.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // progressHD.dismiss();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("ERROR", "Error On failure :- " + Log.getStackTraceString(t));
                showAlertDialog();
                //progressHD.dismiss();
            }
        });
    }

    private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ThankYouScreenActivity.this);
        alertDialog.setMessage("Order Didn't Placed!");
        alertDialog.setIcon(R.drawable.ic_launcher_app);
        alertDialog.setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                placeOrder();
            }
        });

        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
