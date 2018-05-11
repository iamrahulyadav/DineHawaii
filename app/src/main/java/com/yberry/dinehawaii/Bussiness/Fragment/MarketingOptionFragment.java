package com.yberry.dinehawaii.Bussiness.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Bussiness.Activity.BusinessNaviDrawer;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.FragmentIntraction;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import static com.yberry.dinehawaii.Bussiness.Activity.BusinessNaviDrawer.headText;

/**
 * Created by PRINCE 9977123453 on 04-02-17.
 */

public class MarketingOptionFragment extends Fragment implements View.OnClickListener {
    private ImageView imageView;
    private Context mContext;
    private TextView txt;
    private EditText etTextMsg, etDiscount;

    private String TAG = "MARKETING OPTION FRAGMENT ", data1;

    public MarketingOptionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    FragmentIntraction intraction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_marketing_option, container, false);
//        headText.setText("MARKETING OPTIONS");
        if (intraction != null) {
            intraction.actionbarsetTitle("Marketing Options");
        }
        viewData();
        init(view);

        return view;
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

    private void init(View view) {
        mContext = getActivity();

        imageView = (ImageView) view.findViewById(R.id.imageview);
        txt = (TextView) view.findViewById(R.id.btnSubmit);
        etTextMsg = (EditText) view.findViewById(R.id.etTextMsg);
        etDiscount = (EditText) view.findViewById(R.id.etDiscount);
        imageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.txt) {
            if (Util.isNetworkAvailable(getActivity())) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("method", AppConstants.BUSINESS_FEEDBACK_AND_MARKETING_API.ADD_BUSINESS_MARKETING_OPTION);
                jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
                jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));
                jsonObject.addProperty("text_message_to_customer", etTextMsg.getText().toString());//AppPreferencesBuss.getUserId(getApplicationContext()));
                jsonObject.addProperty("special_discount_message", etDiscount.getText().toString());//AppPreferencesBuss.getUserId(getApplicationContext()));

                Log.d("JSONOBJECTMarketing:", jsonObject.toString());

                //Log.e(TAG, jsonObject.toString());
                JsonCallMethod(jsonObject);

            } else {
                Toast.makeText(mContext, "Please Connect Your Internet", Toast.LENGTH_LONG).show();

            }

        } else if (v.getId() == R.id.imageview) {
            pacakgeData();
        }
    }


    public void pacakgeData() {
        String packageNo = AppPreferencesBuss.getBussiPackagelist(getActivity());
        //  String[] data = packageNo.replace("").split(",");
        String[] data = packageNo.replace(" ", "").split(",");
        List<String> list = Arrays.asList(data);

        //34
        Log.d("package_no", list.toString());
        if (list.contains("4")) {
            Log.d("package_no4", String.valueOf(list));
            Fragment fragment = new FoodServiceFragment45();
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.frame, fragment, fragment.getTag());
            fragmentTransaction.commitAllowingStateLoss();
            //break;
        } else {
            Intent intent = new Intent(getActivity(), BusinessNaviDrawer.class);
            startActivity(intent);
        }


    }

    private void JsonCallMethod(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(mContext, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });
        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.business_feedback_and_marketing_api(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG + "onResponseDeliverService", response.body().toString());
                String s = response.body().toString();

                try {
                    JSONObject jsonObject = new JSONObject(s);

                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int i = 0; i <= jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            jsonObject1.getString("customer_message");
                            jsonObject1.getString("discout_message");


                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG + "error", t.getMessage());
                t.getMessage();
                progressHD.dismiss();
            }
        });
    }

    private void viewData() {
       /* if (Util.isNetworkAvailable(mContext)) {
       */
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", "BusinessView57");
        jsonObject.addProperty("method", AppConstants.BUSSINES_USER_BUSINESSAPI.Buss_Mark);
        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));
           /* jsonObject.addProperty("business_id", "1");
            jsonObject.addProperty("user_id", "8");
*/

        Log.d("JSONOBJECTMarketingres:", jsonObject.toString());

        //Log.e(TAG, jsonObject.toString());
        JsonViewMethod(jsonObject);

    } /*else {
            Toast.makeText(mContext, "Please Connect Your Internet", Toast.LENGTH_LONG).show();

        }
    }*/

    private void JsonViewMethod(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(getActivity(), "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });
        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.view_service(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG + "onResponseDeliverService", response.body().toString());
                String s = response.body().toString();

                try {
                    JSONObject jsonObject = new JSONObject(s);

                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int i = 0; i <= jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String rateApp = jsonObject1.getString("rate_hawaii_app");
                            String rateAndroid = jsonObject1.getString("rate_hawaii_android");
                            etTextMsg.setText(rateApp);
                            etDiscount.setText(rateAndroid);

                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
//                Log.e(TAG + "error", t.getMessage());
                t.getMessage();
                progressHD.dismiss();
            }
        });
    }
}