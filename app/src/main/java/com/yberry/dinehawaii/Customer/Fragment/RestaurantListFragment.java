package com.yberry.dinehawaii.Customer.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.ImageView;
import android.widget.ScrollView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Customer.Activity.CustomerNaviDrawer;
import com.yberry.dinehawaii.Customer.Activity.MapsActivity;
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
import com.yberry.dinehawaii.adapter.SearchViewPagerAdapter;
import com.yberry.dinehawaii.customview.CustomTextView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestaurantListFragment extends Fragment {
    private static final String TAG = "RestaurantListFragment";
    SearchViewPagerAdapter adapter;
    GPSTracker gpsTracker;
    FragmentIntraction intraction;
    private double latitude, longitude;
    private ArrayList<ListItem> listItems;
    private RecyclerView recyclerview;
    private LinearLayoutManager mLayoutManager;
    private RestaurantListAdapter mainScreenAdapter;

    private int current_page = 0;
    private int previsibleItemPos = 0;
    private FloatingActionButton mMap;
    private CustomTextView tvNoData;
    private ImageView progressBar;
    private CustomTextView loadMore;
    private ScrollView scrollView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.customersearchby, container, false);
        if (intraction != null) {
            intraction.actionbarsetTitle("Restaurants");
        }
        setHasOptionsMenu(true);
        gpsTracker = new GPSTracker(getActivity());
        if (gpsTracker.canGetLocation()) {

            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
            Log.v(TAG, " Latitude :- " + latitude + " & Longitude:- " + longitude);
        } else {
            gpsTracker.showSettingsAlert();
        }

        init(view);
        gps();
        new getRestFromServer().execute();
        return view;
    }


    private void init(View rootView) {
        listItems = new ArrayList<ListItem>();
        mMap = (FloatingActionButton) rootView.findViewById(R.id.mMap);
        recyclerview = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        tvNoData = (CustomTextView) rootView.findViewById(R.id.mNodata);
        loadMore = (CustomTextView) rootView.findViewById(R.id.loadMore);
        progressBar = (ImageView) rootView.findViewById(R.id.progressBar);
        AnimationDrawable spinner = (AnimationDrawable) progressBar.getBackground();
        spinner.start();

        scrollView = (ScrollView) rootView.findViewById(R.id.scrollView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        mainScreenAdapter = new RestaurantListAdapter(getActivity(), listItems);
        recyclerview.setNestedScrollingEnabled(false);
        recyclerview.setAdapter(mainScreenAdapter);
        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItemPos = mLayoutManager.findLastVisibleItemPosition();
                int totalItemCount = mLayoutManager.getItemCount();
                Log.e("addOnScrollListener", lastVisibleItemPos + ":::" + totalItemCount);
                previsibleItemPos = lastVisibleItemPos;
                /*if ((lastVisibleItemPos + 1) == totalItemCount) {
                    current_page++;
                    new getRestFromServer().execute();
                    Log.e("current_page", String.valueOf(current_page));
                } else {
                    if (previsibleItemPos != lastVisibleItemPos) {
                    }
                }*/
            }
        });

        loadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int lastVisibleItemPos = mLayoutManager.findLastVisibleItemPosition();
                int totalItemCount = mLayoutManager.getItemCount();
                Log.e("addOnScrollListener", lastVisibleItemPos + ":::" + totalItemCount);
                previsibleItemPos = lastVisibleItemPos;
                if ((lastVisibleItemPos + 1) == totalItemCount) {
                    current_page++;
                    new getRestFromServer().execute();
                    Log.e("current_page", String.valueOf(current_page));
                } else {
                    if (previsibleItemPos != lastVisibleItemPos) {
                    }
                }
                loadMore.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                scrollView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                }, 100);
            }
        });

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
        mMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                intent.setAction("SearchedFragment");
                intent.putParcelableArrayListExtra("listItems", listItems);
                startActivity(intent);
            }
        });
    }

    private void gps() {
        GPSTracker gpsTracker = new GPSTracker(getActivity());
        latitude = gpsTracker.getLatitude();
        longitude = gpsTracker.getLongitude();
    }

    private void showAlertDialog() {
        new AlertDialog.Builder(getActivity())
                .setMessage("Something went wrong!")
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new getRestFromServer().execute();
                    }
                }).setNegativeButton("Go Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(getActivity(), CustomerNaviDrawer.class));

            }
        }).show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.filter_list, menu);
        MenuItem menuItem = menu.findItem(R.id.filter_list);
        MenuItem menuItem2 = menu.findItem(R.id.reset_filter);
        menuItem.setVisible(true);
        menuItem2.setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filter_list:
                loadSearchFragment();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        setHasOptionsMenu(true);
    }

    private void loadSearchFragment() {
        Fragment fragment = new RestaurantFilterFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.frameSearch, fragment, fragment.getTag());
        fragmentTransaction.commit();
//        mMap.setVisibility(View.GONE);

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            getTargetFragment().setMenuVisibility(false);
        } else {
            getTargetFragment().setMenuVisibility(true);
        }
    }

    class getRestFromServer extends AsyncTask<Void, String, Void> {
        DialogManager dialogManager;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tvNoData.setText("");
            loadMore.setVisibility(View.GONE);
            dialogManager = new DialogManager();
            progressBar.setVisibility(View.VISIBLE);
            //dialogManager.showProcessDialog(getActivity(), "Please wait...");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.GENERALAPI.GETALLBUSINESS);
            jsonObject.addProperty(AppConstants.KEY_USERID, AppPreferences.getCustomerid(getActivity())); //7
            jsonObject.addProperty(AppConstants.KEY_LATITUDE, latitude);
            jsonObject.addProperty(AppConstants.KEY_LONGITUDE, longitude);
            jsonObject.addProperty("distance", "0");
            jsonObject.addProperty(AppConstants.KEY_PAGE, current_page);
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
                    JsonObject jsonObject = response.body();
                    if (jsonObject.get("status").getAsString().equals("200")) {
                        JsonArray jsonArray = jsonObject.getAsJsonArray("result");
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JsonObject result = jsonArray.get(i).getAsJsonObject();
                            Log.e("listItemresult", String.valueOf(result));
                            ListItem listItem = new Gson().fromJson(result, ListItem.class);
                            String average = listItem.getAvgPrice();
                            Log.e("average", average);
                            listItem.setDisplayLogo(false);
                            listItems.add(listItem);
                            Log.e("listItem", String.valueOf(listItem));
                            Log.e("reseVAmount:", listItem.getReservationPrice());
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
            progressBar.setVisibility(View.GONE);
            loadMore.setVisibility(View.GONE);
            mainScreenAdapter.notifyDataSetChanged();
            if (values[0].equalsIgnoreCase("200")) {
                loadMore.setVisibility(View.VISIBLE);
            }
            if (values[0].equalsIgnoreCase("400")) {
                tvNoData.setText("");
                showAlertDialog();
            } else if (values[0].equalsIgnoreCase("600")) {
            }
            if (mainScreenAdapter.getItemCount() == 0) {
                tvNoData.setText("No record found");
            }
        }
    }
}
