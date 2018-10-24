package com.yberry.dinehawaii.Customer.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Customer.Adapter.FavouriteFragmentAdapter98;
import com.yberry.dinehawaii.Model.CustomerModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferences;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Util.Util;
import com.yberry.dinehawaii.customview.CustomTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FavouriteOrdersFragment extends Fragment {
    private static final String TAG = "FavouriteOrdersFragment";
    public static FavouriteFragmentAdapter98 adapter;
    public static List<CustomerModel> list = new ArrayList<>();
    Context context;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private CustomTextView nodata;
    private View rootView;


    public FavouriteOrdersFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_favourite, container, false);
        context = getActivity();
        initComponent();
        repeatFavOrder();
        return rootView;
    }


    private void initComponent() {
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_repeatOrder);
        nodata = (CustomTextView) rootView.findViewById(R.id.nodata);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        adapter = new FavouriteFragmentAdapter98(context, list);
        mRecyclerView.setAdapter(adapter);
    }

    private void repeatFavOrder() {
        if (Util.isNetworkAvailable(getActivity())) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.CUSTOMER_USER.GETFAVOURITEORDERS);
            jsonObject.addProperty("user_id", AppPreferences.getCustomerid(getActivity()));//8
            Log.e(TAG, "Request FAVOURITE ORDERS" + jsonObject.toString());
            getRepeatFavData(jsonObject);

        } else {
            Toast.makeText(getActivity(), "Please Connect Your Internet", Toast.LENGTH_LONG).show();

        }

    }

    private void getRepeatFavData(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(getActivity(), "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

                // TODO Auto-generated method stub
            }
        });


        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.order_detail_api(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                Log.e(TAG, "Json Response :- " + response.body().toString());
                String resp = response.body().toString();
                Log.e(TAG, "Response FAVOURITE ORDERS >> " + resp);
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        list.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            CustomerModel customerModel = new CustomerModel();
                            JSONObject object = jsonArray.getJSONObject(i);
                            customerModel.setOrder_id(object.getString("order_id"));
                            customerModel.setTotal_price(object.getString("total_price"));
                            customerModel.setFood_name(object.getString("food_name"));
                            customerModel.setOrder_quantity(object.getString("quantity"));
                            customerModel.setOrder_ItemCustomization(object.getString("item_customization"));
                            customerModel.setOrder_date(object.getString("order_date"));
                            customerModel.setOrder(object.getString("order"));
                            customerModel.setCat_id(object.getString("cat_id"));
                            customerModel.setMenu_id(object.getString("menu_id"));
                            customerModel.setItem_customization(object.getString("item_customization"));
                            customerModel.setIteam_message(object.getString("iteam_message"));
                            customerModel.setItem_price(object.getString("item_price"));
                            customerModel.setBusiness_id(object.getString("business_id"));
                            customerModel.setAvgPrice(object.getString("avgPrice"));
                            customerModel.setId(object.getString("id"));

                           /* customerModel.setBus_lattitude(object.getString("latitude"));
                            customerModel.setBus_longitude(object.getString("longitude"));
                            customerModel.setBus_package(object.getString("business_package"));*/
                            if (i >= 20) {
                                break;
                            }
                            list.add(customerModel);
                            Log.v(TAG, "Inside Favroite Fragment!!!!");
                            Log.d("FAVFOOD NMAE:", object.getString("food_name"));
                        }

                        adapter.notifyDataSetChanged();


                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        list.clear();
                        nodata.setVisibility(View.VISIBLE);
                    }
                    adapter.notifyDataSetChanged();
                    progressHD.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    list.clear();
                    adapter.notifyDataSetChanged();
                    progressHD.dismiss();
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "error :- " + Log.getStackTraceString(t));
                list.clear();
                adapter.notifyDataSetChanged();
                progressHD.dismiss();
                Toast.makeText(getActivity(), "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });
    }
}