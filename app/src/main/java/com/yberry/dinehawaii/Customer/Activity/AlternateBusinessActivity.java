package com.yberry.dinehawaii.Customer.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.yberry.dinehawaii.Model.LIstItemAlternate;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.DialogManager;
import com.yberry.dinehawaii.Util.Function;
import com.yberry.dinehawaii.Util.GPSTracker;
import com.yberry.dinehawaii.Util.RecyclerItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AlternateBusinessActivity extends AppCompatActivity {

    private final String TAG = "AlternateBusinessActivity";
    private FragmentActivity context;
    private RecyclerView recycler_view;
    private ArrayList<LIstItemAlternate> list;
    private AlternateRestListAdapter adapter;
    private GPSTracker gpsTracker;
    private double latitude, longitude;
    private String party_size = "0";

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alternate_business);
        context = this;
        if (getIntent().hasExtra("party_size")) {
            party_size = getIntent().getStringExtra("party_size");
            Log.e(TAG, "onCreate: party_size >> " + party_size);
        }
        list = new ArrayList<LIstItemAlternate>();
        setAdapter();
        setToolbar();

        gpsTracker = new GPSTracker(context);
        if (gpsTracker.canGetLocation()) {
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
            Log.e(TAG, " Latitude :- " + latitude + " & Longitude:- " + longitude);
            if (Function.isConnectingToInternet(context)) {
                new getRestFromServer().execute();
            } else
                Toast.makeText(context, "Connect to internet", Toast.LENGTH_SHORT).show();
        } else {
            gpsTracker.showSettingsAlert();
        }
    }

    class getRestFromServer extends AsyncTask<Void, String, Void> {
        DialogManager dialogManager;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialogManager = new DialogManager();
            dialogManager.showProcessDialog(context, "Please wait...");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.GENERALAPI.GETALTERNATEBUSINESS);
            jsonObject.addProperty(AppConstants.KEY_LATITUDE, latitude);
            jsonObject.addProperty(AppConstants.KEY_LONGITUDE, longitude);
            jsonObject.addProperty("party_size", party_size);
            jsonObject.addProperty("distance", "15");
            Log.e(TAG, "Request GET ALL RESTAURANTS" + jsonObject.toString());

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .addInterceptor(logging)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(AppConstants.BASEURL.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            MyApiEndpointInterface apiService =
                    retrofit.create(MyApiEndpointInterface.class);


            Call<JsonObject> call = apiService.requestGeneral(jsonObject);
            call.enqueue(new Callback<JsonObject>() {
                @Override

                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.e(TAG, "Request GET ALL RESTAURANTS >>  " + call.request().toString());
                    Log.e(TAG, "Response GET ALL RESTAURANTS >> " + response.body().toString());
                    JSONObject jsonObject = null;
                    list = new ArrayList<LIstItemAlternate>();
                    try {
                        jsonObject = new JSONObject(response.body().toString());

                        if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject result = jsonArray.getJSONObject(i);
                                LIstItemAlternate newData = new Gson().fromJson(String.valueOf(result), LIstItemAlternate.class);
                                if (!newData.getTableName().equalsIgnoreCase("") && !newData.getTableId().equalsIgnoreCase("")) {
                                    list.add(newData);
                                    Log.e("newData", String.valueOf(newData));
                                }
                            }
                            publishProgress("200", "");
                        } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                            publishProgress("400", jsonObject.getJSONArray("result").getJSONObject(0).getString("msg"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        publishProgress("400", "Server Error");
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.e(TAG, "onFailure: " + t.getMessage());
                    publishProgress("400", "Server Error");
                }
            });
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            dialogManager.stopProcessDialog();
            adapter.notifyDataSetChanged();
            Log.e(TAG, "onProgressUpdate: list.size >> " + list.size());
            setAdapter();
            if (values[0].equalsIgnoreCase("200")) {
            } else if (values[0].equalsIgnoreCase("400")) {
                Toast.makeText(context, values[1], Toast.LENGTH_SHORT).show();
            }
        }
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
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        recycler_view.setLayoutManager(new LinearLayoutManager(context));
        adapter = new AlternateRestListAdapter(AlternateBusinessActivity.this, context, list);
        recycler_view.setAdapter(adapter);
        recycler_view.addOnItemTouchListener(new RecyclerItemClickListener(context, recycler_view, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent returnIntent = getIntent();
                returnIntent.putExtra("busi_id", list.get(position).getId());
                returnIntent.putExtra("table_id", list.get(position).getTableId());
                returnIntent.putExtra("reserve_amt", list.get(position).getReservationPrice());
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        }));
    }
}
