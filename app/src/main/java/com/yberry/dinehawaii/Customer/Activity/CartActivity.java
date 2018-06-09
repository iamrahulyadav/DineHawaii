package com.yberry.dinehawaii.Customer.Activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.yberry.dinehawaii.Customer.Adapter.CartItemAdapter;
import com.yberry.dinehawaii.Model.OrderItemsDetailsModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.Util.AppPreferences;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.customview.CustomTextView;
import com.yberry.dinehawaii.database.DatabaseHandler;

import java.util.ArrayList;
import java.util.Calendar;

public class CartActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    private static final String TAG = "CartActivity";
    CustomTextView total_amount, noItems;
    CartItemAdapter adapter;
    Button sub;
    ArrayList<OrderItemsDetailsModel> cartItems;
    Context context;
    double amount, totalPrice = 0;
    LinearLayout mainView;
    RecyclerView mRecyclerView;
    RelativeLayout mainView2;
    String cateringDateTime = "";
    TextView tvordertime;
    BroadcastReceiver updatePrice = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            amount = adapter.getTotalCost();
            totalPrice = adapter.getTotalCost();
            total_amount.setText("" + totalPrice);
            AppPreferences.setPrice(CartActivity.this, "" + totalPrice);
            if (adapter.getTotalCost() == 0.0) {
                mainView.setVisibility(View.GONE);
                mainView2.setVisibility(View.GONE);
                noItems.setVisibility(View.VISIBLE);
            }

        }
    };
    private DatabaseHandler mydb;
    private LinearLayoutManager mLayoutManager;

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        context = this;
        mydb = new DatabaseHandler(context);
        setToolbar();
        initViews();
        LocalBroadcastManager.getInstance(context).registerReceiver(updatePrice, new IntentFilter("updateTotalprice"));
        mainView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view = getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                return false;
            }
        });

        if (mydb.hasCartData()) {
            cartItems = new DatabaseHandler(context).getCartItems(AppPreferences.getBusiID(context));  //database data
            Log.d("CARTITEMS", cartItems.toString());
            setCartAdapter();
            amount = adapter.getTotalCost();
            totalPrice = adapter.getTotalCost();
            total_amount.setText("" + totalPrice);
            AppPreferences.setPrice(CartActivity.this, "" + totalPrice);

        } else {
            mainView.setVisibility(View.GONE);
            mainView2.setVisibility(View.GONE);
            noItems.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_cart) {
            Intent intent = new Intent(getApplicationContext(), PlaceAnOrder.class);
            intent.putExtra("business_id", AppPreferences.getBusiID(context));
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        // String date = (monthOfYear +1)+ "/" + (dayOfMonth) + "/" + year;
        cateringDateTime = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
        Log.v(TAG, "onDateSet: cateringDateTime >> " + cateringDateTime);
        //datePicker.setText(date);
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);
        com.wdullaer.materialdatetimepicker.time.TimePickerDialog timePickerDialog = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(new com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(com.wdullaer.materialdatetimepicker.time.TimePickerDialog timePickerDialog, int hourOfDay, int minute, int i2) {
                boolean isPM = (hourOfDay >= 12);
                String selectedTime = String.format("%02d:%02d %s", (hourOfDay == 12 || hourOfDay == 0) ? 12 : hourOfDay % 12, minute, isPM ? "PM" : "AM");
                // timePicker.setText(selectedTime);
                Log.d("selectedTime", selectedTime);
                tvordertime.setText(cateringDateTime + " " + selectedTime);
                AppPreferences.setOrderTime(context, tvordertime.getText().toString());
            }
        }, mHour, mMinute, false);
        timePickerDialog.show(getFragmentManager(), "timepickerdialog");

        timePickerDialog.setAccentColor(getResources().getColor(R.color.colorPrimary));
        timePickerDialog.setCancelColor(getResources().getColor(R.color.colorPrimary));
        timePickerDialog.setOkColor(getResources().getColor(R.color.colorPrimary));
    }

    private void setCartAdapter() {

        adapter = new CartItemAdapter(CartActivity.this, cartItems, total_amount);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(CartActivity.this);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);
    }

    @SuppressLint("LongLogTag")
    private void initViews() {
        sub = (Button) findViewById(R.id.btnSub);
        sub.setOnClickListener(this);
        total_amount = (CustomTextView) findViewById(R.id.total_amount);
        noItems = (CustomTextView) findViewById(R.id.noitems);
        mainView2 = (RelativeLayout) findViewById(R.id.mainView2);
        mainView = (LinearLayout) findViewById(R.id.main_view);
    }

    private void setToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((TextView) findViewById(R.id.headet_text)).setText("Cart");
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSub) {
            String minAmount = AppPreferencesBuss.getAveragePrice(CartActivity.this);
            Log.e(TAG, "onClick: minAmount >> " + minAmount);
            if (minAmount.equalsIgnoreCase("$")) {
                if (totalPrice < 10) {
                    Toast.makeText(CartActivity.this, "Your order amount should be min 10$", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), CheckOutActivity.class);
                    intent.putExtra("totalamount", total_amount.getText().toString());
                    startActivity(intent);
                }
            } else if (minAmount.equalsIgnoreCase("$$")) {
                if (totalPrice < 20) {
                    Toast.makeText(CartActivity.this, "Your order amount should be min 20$", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), CheckOutActivity.class);
                    intent.putExtra("totalamount", total_amount.getText().toString());
                    startActivity(intent);
                }
            } else if (minAmount.equalsIgnoreCase("$$$")) {
                if (totalPrice < 30) {
                    Toast.makeText(CartActivity.this, "Your order amount should be min 30$", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), CheckOutActivity.class);
                    intent.putExtra("totalamount", total_amount.getText().toString());
                    startActivity(intent);
                }
            } else if (minAmount.equalsIgnoreCase("$$$$")) {
                if (totalPrice < 40) {
                    Toast.makeText(CartActivity.this, "Your order amount should be over 40$", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), CheckOutActivity.class);
                    intent.putExtra("totalamount", total_amount.getText().toString());
                    startActivity(intent);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(context).unregisterReceiver(updatePrice);
    }
}
