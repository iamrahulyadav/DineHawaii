package com.yberry.dinehawaii.Customer.Fragment;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Customer.Activity.RestaurentDetailActivity;
import com.yberry.dinehawaii.Customer.Adapter.RestaurantListAdapter;
import com.yberry.dinehawaii.Model.ListItem;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferences;
import com.yberry.dinehawaii.Util.DialogManager;
import com.yberry.dinehawaii.Util.FragmentIntraction;
import com.yberry.dinehawaii.Util.GPSTracker;
import com.yberry.dinehawaii.customview.CustomTextView;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//import static com.yberry.dinehawaii.Customer.Activity.CustomerNaviDrawer.headText;

/**
 * A simple {@link Fragment} subclass.
 */
public class RestaurantListFilterFragment extends Fragment {
    private static final String TAG = "SearchedFilterClass";
    View rootView;
    ArrayList<ListItem> listItems;
    RestaurantListAdapter mainScreenAdapter;
    RecyclerView recyclerview;
    String loc = "", buss = "", dist = "", foodid = "", serviceid = "";
    Double latitude, longitude;
    FragmentIntraction intraction;
    private LinearLayoutManager mLayoutManager;
    private String pricerange = "";
    private CustomTextView tvNoData;

    public RestaurantListFilterFragment() {
        // Required empty public constructor
    }

    private void gps() {
        GPSTracker gpsTracker = new GPSTracker(getActivity());
        latitude = gpsTracker.getLatitude();
        longitude = gpsTracker.getLongitude();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_restaurant_filter, container, false);
        if (intraction != null) {
            intraction.actionbarsetTitle("Restaurants");
        }

        init();
        gps();
        setHasOptionsMenu(true);
        new getRestFilterFromServer().execute();
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setHasOptionsMenu(true);

        if (context instanceof FragmentIntraction) {
            intraction = (FragmentIntraction) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        intraction = null;
    }

    private void init() {
        listItems = new ArrayList<ListItem>();
        recyclerview = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        tvNoData = (CustomTextView) rootView.findViewById(R.id.mNodata);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        mainScreenAdapter = new RestaurantListAdapter(getActivity(), listItems);
        recyclerview.setAdapter(mainScreenAdapter);

        mainScreenAdapter.setOnItemClickListener(new RestaurantListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ListItem listItem) {
                AppPreferences.setBussPackageList(getActivity(), listItem.getBusinessPackage());
                AppPreferences.setBusiID(getActivity(), listItem.getId());
                AppPreferences.setReservationAmount(getActivity(), listItem.getReservationPrice());
                Log.e("Sel-Business Id", AppPreferences.getBusiID(getActivity()));
                Log.e("Sel-Business Packages", AppPreferences.getBussPackageList(getActivity()));
                Log.e("Sel-Business rAmount", AppPreferences.getReservationAmount(getActivity()));
                Intent intent = new Intent(getActivity(), RestaurentDetailActivity.class);
                intent.putExtra("data", listItem);
                startActivity(intent);
            }
        });

        Bundle bundle = getArguments();
        if (bundle != null) {
            buss = bundle.getString("busname");
            dist = bundle.getString("distance");
            loc = bundle.getString("location");
            serviceid = bundle.getString("serviceid");
            foodid = bundle.getString("foodid");
            pricerange = bundle.getString("price_range");
        } else {

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.filter_list, menu);
        MenuItem menuItem = menu.findItem(R.id.reset_filter);
        menuItem.setVisible(true);

        MenuItem item = menu.findItem(R.id.filter_list);
        if (item != null) item.setVisible(false);

        MenuItem filter_cancel = menu.findItem(R.id.filter_cancel);
        if (filter_cancel != null) filter_cancel.setVisible(false);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.reset_filter:
                Fragment fragment = new RestaurantListFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment, fragment.getTag());
                fragmentTransaction.commitAllowingStateLoss();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    class getRestFilterFromServer extends AsyncTask<Void, String, Void> {
        DialogManager dialogManager;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tvNoData.setText("");
            dialogManager = new DialogManager();
            dialogManager.showProcessDialog(getActivity(), "Please wait...");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.GENERALAPI.GETALLBUSINESS);
            jsonObject.addProperty(AppConstants.KEY_USERID, AppPreferences.getCustomerid(getActivity())); //7
            jsonObject.addProperty(AppConstants.KEY_LATITUDE, latitude);
            jsonObject.addProperty(AppConstants.KEY_LONGITUDE, longitude);
            jsonObject.addProperty(AppConstants.KEY_BUSINESS_NAME, buss);
            jsonObject.addProperty(AppConstants.KEY_LOCATION, loc);
            jsonObject.addProperty(AppConstants.KEY_DISTANCE, dist);
            jsonObject.addProperty(AppConstants.KEY_FOOD_TYPE_ID, foodid);
            jsonObject.addProperty(AppConstants.KEY_SERVICE_TYPE_ID, serviceid);
            jsonObject.addProperty("average_price", pricerange);
            Log.e(TAG, "Request GET ALL RESTAURANTS" + jsonObject.toString());

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(logging);
            Retrofit retrofit = new Retrofit.Builder()
                    .client(httpClient.build())
                    .baseUrl(AppConstants.BASEURL.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            MyApiEndpointInterface apiService =
                    retrofit.create(MyApiEndpointInterface.class);

            Call<JsonObject> call = apiService.requestGeneral(jsonObject);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.d(TAG, "Request  ALL RESTAURANTS >>  " + call.request().toString());
                    Log.d(TAG, "Response  ALL RESTAURANTS >> " + response.body().toString());
                    JsonObject jsonObject = response.body();
                    if (jsonObject.get("status").getAsString().equals("200")) {
                        JsonArray jsonArray = jsonObject.getAsJsonArray("result");
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JsonObject result = jsonArray.get(i).getAsJsonObject();
                            Log.d("listItemresult", String.valueOf(result));
                            ListItem listItem = new Gson().fromJson(result, ListItem.class);
                            String average = listItem.getAvgPrice();
                            Log.d("average", average);
                            listItem.setDisplayLogo(false);
                            listItems.add(listItem);
                            Log.d("listItem", String.valueOf(listItem));
                            Log.d("reseVAmount:", listItem.getReservationPrice());
                        }
                        publishProgress("200");
                    } else if (jsonObject.get("status").getAsString().equals("400")) {
                        publishProgress("400");
                    } else if (jsonObject.get("status").getAsString().equals("600")) {
                        publishProgress("600");
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.e(TAG, "onFailure: " + t.getMessage());
                    publishProgress("400");
                }
            });
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            dialogManager.stopProcessDialog();
            mainScreenAdapter.notifyDataSetChanged();
            if (values[0].equalsIgnoreCase("400")) {
                tvNoData.setText("");
                //showAlertDialog();
            } else if (values[0].equalsIgnoreCase("600")) {
            }

            if (mainScreenAdapter.getItemCount() == 0) {
                tvNoData.setText("No record found for the applied filters");
            }
        }
    }

}
