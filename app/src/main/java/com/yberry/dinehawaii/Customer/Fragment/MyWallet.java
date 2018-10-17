package com.yberry.dinehawaii.Customer.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonObject;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferences;
import com.yberry.dinehawaii.Util.FragmentIntraction;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.customview.CustomTextView;

import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyWallet extends Fragment implements View.OnClickListener {
    private static final String TAG = "MyWallet";
    private Context context;
    View rootView;
    FragmentIntraction intraction;
    private CustomTextView tvWalletAmt;
    private String walletAmt = "0.0";

    public MyWallet() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_my_wallet, container, false);
        context = getActivity();
        if (intraction != null) {
            intraction.actionbarsetTitle("My Wallet");
        }
        init();
        new GetWallet().execute();
        return rootView;
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

    @SuppressLint("LongLogTag")
    private void init() {
        tvWalletAmt = rootView.findViewById(R.id.tvWalletAmt);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.update) {
        } else if (v.getId() == R.id.btn_change_photo) {
        } else if (v.getId() == R.id.email) {
        }
    }

    class GetWallet extends AsyncTask<Void, Void, Void> {
        ProgressHUD progressHD;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressHD = ProgressHUD.show(context, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                }
            });
        }

        @Override
        protected Void doInBackground(Void... voids) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.CUSTOMER_USER.GETUSERWALLET);
            jsonObject.addProperty("user_id", AppPreferences.getCustomerid(context));
            Log.e(TAG, "GetWallet: Request >> " + jsonObject.toString());

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(logging);
            Retrofit retrofit = new Retrofit.Builder()
                    .client(httpClient.build())
                    .baseUrl(AppConstants.BASEURL.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            MyApiEndpointInterface apiService = retrofit.create(MyApiEndpointInterface.class);

            Call<JsonObject> call = apiService.customerprofile(jsonObject);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.e(TAG, "GetWallet: Response >> " + response.body());
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.body().toString());
                        if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                            walletAmt = jsonObject.getJSONArray("result").getJSONObject(0).getString("wallet");
                            AppPreferences.setWalletAmt(context, walletAmt);
                            publishProgress(200, "");
                        } else {
                            publishProgress(400, jsonObject.getJSONArray("result").getJSONObject(0).getString("msg"));
                        }
                    } catch (Exception e) {
                        progressHD.dismiss();
                        e.printStackTrace();
                        publishProgress(400, "Server not responding...");
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    progressHD.dismiss();
                    Log.e("Response: onFailure 1", t.toString());
                    publishProgress(400, "Server not responding...");
                }
            });
            return null;
        }

        private void publishProgress(int i, String msg) {
            if (progressHD != null && progressHD.isShowing())
                progressHD.dismiss();
            tvWalletAmt.setText("$" + walletAmt);
            if (i == 400) {
                new android.support.v7.app.AlertDialog.Builder(context)
                        .setMessage(msg)
                        .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new GetWallet().execute();
                            }
                        }).setNegativeButton("Go Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
            }
        }
    }
}