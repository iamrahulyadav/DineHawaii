package com.yberry.dinehawaii.vendor.Fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.FragmentIntraction;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Util.RecyclerItemClickListener;
import com.yberry.dinehawaii.Util.Util;
import com.yberry.dinehawaii.customview.CustomTextView;
import com.yberry.dinehawaii.vendor.Activity.VendorListActivity;
import com.yberry.dinehawaii.vendor.Adapter.ManageVendorAdapter;
import com.yberry.dinehawaii.vendor.Model.VendorMasterData;
import com.yberry.dinehawaii.vendor.Model.VendorModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ManageVendorsFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "ManageVendorsFragment";
    Context context;
    ArrayList<VendorModel> list;
    ArrayList<VendorMasterData> masterlist;
    FragmentIntraction intraction;
    CustomTextView noData;
    private RecyclerView recycler_view;
    private ManageVendorAdapter adapter;

    public ManageVendorsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_vendors, container, false);
        context = getActivity();
        if (intraction != null) {
            intraction.actionbarsetTitle("Vendors/Suppliers");
        }
        list = new ArrayList<VendorModel>();
        masterlist = new ArrayList<VendorMasterData>();
        init(view);
        setAdapter();
        return view;
    }


    private void init(View view) {
        recycler_view = (RecyclerView) view.findViewById(R.id.recycler_view);
        noData = (CustomTextView) view.findViewById(R.id.noData);
    }

    private void setAdapter() {
        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ManageVendorAdapter(context, masterlist);
        recycler_view.setAdapter(adapter);
        recycler_view.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recycler_view, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(context, VendorListActivity.class);
                intent.putExtra("category_id", masterlist.get(position).getMaster_vendor_id());
                intent.putExtra("category_name", masterlist.get(position).getMaster_vendor_name());
                context.startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.isNetworkAvailable(getActivity())) {
            getAllMasterVendor();
            //  getAllVendors();
        } else
            Toast.makeText(context, getResources().getString(R.string.msg_no_internet), Toast.LENGTH_SHORT).show();
    }

    private void getAllMasterVendor() {
        final ProgressHUD progressHD = ProgressHUD.show(getActivity(), "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.BUSSINES_USER_BUSINESSAPI.MASTERVENDORLIST);
        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));
        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
        Log.e(TAG, "getAllVendors: Request >> " + jsonObject);

        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.vendors_list_api(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String resp = response.body().toString();
                Log.e(TAG, "onResponse: Response >> " + resp);
                try {
                    masterlist.clear();
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        noData.setVisibility(View.GONE);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject objresult = jsonArray.getJSONObject(i);
                            VendorMasterData model = new VendorMasterData();
                            model.setMaster_vendor_id(objresult.getString("cat_id"));
                            model.setMaster_vendor_addedon(objresult.getString("addedOn"));
                            model.setMaster_vendor_name(objresult.getString("name"));
                            masterlist.add(model);
                        }
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        masterlist.clear();
                        noData.setVisibility(View.VISIBLE);
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    noData.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "error :- " + Log.getStackTraceString(t));
                masterlist.clear();
                noData.setVisibility(View.VISIBLE);
                progressHD.dismiss();
                Toast.makeText(context, getResources().getString(R.string.msg_no_internet), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getAllVendors() {
        final ProgressHUD progressHD = ProgressHUD.show(getActivity(), "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.BUSSINES_USER_BUSINESSAPI.GETVENDORSLIST);
        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));
        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
        Log.e(TAG, "getAllVendors: Request >> " + jsonObject);

        MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.vendors_list_api(jsonObject);

        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String resp = response.body().toString();
                Log.e(TAG, "onResponse: Response >> " + resp);
                try {
                    list.clear();
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Gson gson = new Gson();
                            VendorModel model = gson.fromJson(jsonArray.getJSONObject(i).toString(), VendorModel.class);
                            list.add(model);
                        }
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        list.clear();
                        Toast.makeText(getActivity(), "no record found", Toast.LENGTH_SHORT).show();
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "error :- " + Log.getStackTraceString(t));
                progressHD.dismiss();
                Toast.makeText(getActivity(), "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });

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
    public void onClick(View v) {

    }

}
