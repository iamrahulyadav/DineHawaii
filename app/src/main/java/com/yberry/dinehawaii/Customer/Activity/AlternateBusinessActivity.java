package com.yberry.dinehawaii.Customer.Activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Customer.Adapter.AlternateRestListAdapter;
import com.yberry.dinehawaii.Model.ListItem;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Util.RecyclerItemClickListener;
import com.yberry.dinehawaii.Util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlternateBusinessActivity extends AppCompatActivity {

    private final String TAG = "AlternateBusinessActivity";
    private FragmentActivity context;
    private RecyclerView recycler_view;
    private ArrayList<ListItem> list = new ArrayList<ListItem>();
    private AlternateRestListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alternate_business);
        context = this;
        setAdapter();
        setToolbar();
    }

    private void setToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView tvTitle = (TextView) findViewById(R.id.headet_text);
        tvTitle.setText("Alternate Restaurants");
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setAdapter() {
        list.add(new Gson().fromJson("{\n" +
                "id\": \"501\"," +
                "avgPrice\": \"$\"," +
                "reservation_price\": \"88\"," +
                "healthCardStstus\": \"Poor\"," +
                "distance\": 1.08," +
                "cover_image\": \"https:\\/\\/take007.co.in\\/Projects-Work\\/Hawaii\\/APP\\/food_service_image\\/buss_pic\\/buss_pic501_service__647_5b34775e66950.png\"," +
                "logoImg\": \"https:\\/\\/take007.co.in\\/Projects-Work\\/Hawaii\\/APP\\/food_service_image\\/logo\\/logo501_service__647_5b8d081f01d87.png\"," +
                "business_name\": \"Sayaji Indore\"," +
                "business_package\": \"1, 2, 3, 4\"," +
                "business_option\": \"1, 2, 3\"," +
                "business_address\": \"Vijay Nagar  Vijay Nagar  Indore  Madhya Pradesh 452010  India\"," +
                "latitude\": \"22.718202590942\"," +
                "longitude\": \"75.87565612793\"," +
                "business_contact_no\": \"8008889995\"," +
                "rating\": \"3\"," +
                "type\": \"1\"\n" +
                "}", ListItem.class));
        list.add(new Gson().fromJson("{\n" +
                "id\": \"502\"," +
                "avgPrice\": \"250\"," +
                "reservation_price\": \"0\"," +
                "healthCardStstus\": \"Poor\"," +
                "distance\": 4.16," +
                "cover_image\": \"\"," +
                "logoImg\": \"\"," +
                "business_name\": \"Shree Maya Hotel Indore\"," +
                "business_package\": \"1, 2, 3\"," +
                "business_option\": \"1, 2\"," +
                "business_address\": \"Ab Road, Indore, Madhya Pradesh, India\"," +
                "latitude\": \"22.6465637\"," +
                "longitude\": \"75.818713\"," +
                "business_contact_no\": \"9713891878\"," +
                "rating\": \"4\"," +
                "type\": \"1\"\n" +
                "}", ListItem.class));
        list.add(new Gson().fromJson("{\n" +
                "id\": \"503\"," +
                "avgPrice\": \"$$\"," +
                "reservation_price\": \"0\"," +
                "healthCardStstus\": \"Poor\"," +
                "distance\": 2.19," +
                "cover_image\": \"\"," +
                "logoImg\": \"\"," +
                "business_name\": \"Hvantage\"," +
                "business_package\": \"1, 2\"," +
                "business_option\": \"\"," +
                "business_address\": \"mp nagar\"," +
                "latitude\": \"23.2599\"," +
                "longitude\": \"77.4126\"," +
                "business_contact_no\": \"8269262610\"," +
                "rating\": \"3\"," +
                "type\": \"1\"\n" +
                "}", ListItem.class));
        list.add(new Gson().fromJson("{\n" +
                "id\": \"508\"," +
                "avgPrice\": \"100\"," +
                "reservation_price\": \"0\"," +
                "healthCardStstus\": \"Poor\"," +
                "distance\": 0.16," +
                "cover_image\": \"\"," +
                "logoImg\": \"\"," +
                "business_name\": \"Ashu Restaurant\"," +
                "business_package\": \"1, 2\"," +
                "business_option\": \"1, 2\"," +
                "business_address\": \"\"," +
                "latitude\": \"22.6465637\"," +
                "longitude\": \"75.818713\"," +
                "business_contact_no\": \"88998859999\"," +
                "rating\": \"4\"," +
                "type\": \"1\"\n" +
                "}", ListItem.class));

        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        recycler_view.setLayoutManager(new LinearLayoutManager(context));
        adapter = new AlternateRestListAdapter(AlternateBusinessActivity.this, context, list);
        recycler_view.setAdapter(adapter);
        recycler_view.addOnItemTouchListener(new RecyclerItemClickListener(context, recycler_view, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                finish();
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        }));
    }

    @SuppressLint("LongLogTag")
    private void getData() {
        if (Util.isNetworkAvailable(context)) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.BUSSINES_USER_BUSINESSAPI.GET_ALTERNATE_BUSINESS);
            jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(context));
            jsonObject.addProperty("party_size", "50");
            Log.e(TAG, "getData: Request >> " + jsonObject.toString());

            final ProgressHUD progressHD = ProgressHUD.show(context, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    // TODO Auto-generated method stub
                }
            });
            MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
            Call<JsonObject> call = apiService.n_business_new_api(jsonObject);
            call.enqueue(new Callback<JsonObject>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.e(TAG, "getData: Response >> " + response.body().toString());
                    String s = response.body().toString();
                    list.clear();
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                ListItem model = new Gson().fromJson(String.valueOf(jsonObject1), ListItem.class);
                                list.add(model);
                            }
                            adapter.notifyDataSetChanged();
                        } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                            String msg = jsonObject.getJSONArray("result").getJSONObject(0).getString("msg");
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        progressHD.dismiss();
                        e.printStackTrace();
                    }
                    progressHD.dismiss();
                }

                @SuppressLint("LongLogTag")
                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.e(TAG, " Error :- " + Log.getStackTraceString(t));
                    progressHD.dismiss();
                }
            });

        } else {
            Toast.makeText(context, "Please Connect Your Internet", Toast.LENGTH_SHORT).show();
        }
    }
}
