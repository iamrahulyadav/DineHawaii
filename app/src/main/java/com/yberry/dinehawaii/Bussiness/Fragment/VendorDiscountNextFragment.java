package com.yberry.dinehawaii.Bussiness.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.FragmentIntraction;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.customview.CustomEditText;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import static com.yberry.dinehawaii.Bussiness.Activity.BusinessNaviDrawer.headText;

/**
 * Created by PRINCE 9977123453 on 04-02-17.
 */

public class VendorDiscountNextFragment extends Fragment {
    private static final String TAG = "VendorDiscountNext_55";
    TextView confirm;
    CustomEditText originalAmountEt, adjustAmountET, taxET, deliveryFeeET, serviceFeeET, totalDueEt;
    private ImageView imageView;
    FragmentIntraction intraction;


    public VendorDiscountNextFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_vendor_discount_purchasing, container, false);
     //   headText.setText("VENDOR DISCOUNT PURCHASING SERVICE");
//        headText.setText("VENDOR SERVICE");
        if (intraction != null) {
            intraction.actionbarsetTitle("Vendor Service");
        }

        initViews(view);

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
    private void initViews(View view) {

        confirm = (TextView) view.findViewById(R.id.confirm);

        originalAmountEt = (CustomEditText) view.findViewById(R.id.originalAmountEt);
        adjustAmountET = (CustomEditText) view.findViewById(R.id.adjustAmountET);
        taxET = (CustomEditText) view.findViewById(R.id.taxET);
        deliveryFeeET = (CustomEditText) view.findViewById(R.id.deliveryFeeET);
        serviceFeeET = (CustomEditText) view.findViewById(R.id.serviceFeeET);
        totalDueEt = (CustomEditText) view.findViewById(R.id.totalDueEt);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                vendorDiscountService();
            }
        });

        imageView = (ImageView) view.findViewById(R.id.imageview);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new FeedbackandReviewFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.frame, fragment, fragment.getTag());
                fragmentTransaction.commitAllowingStateLoss();
                /*Fragment fragment = new FeedbackandReviewFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);



                fragmentTransaction.replace(R.id.frame, fragment, fragment.getTag());
                fragmentTransaction.commitAllowingStateLoss();*/
            }
        });

    }

    private void vendorDiscountService() {

        String originalAmount = originalAmountEt.getText().toString().trim();
        String adjustAmount = adjustAmountET.getText().toString().trim();
        String eTax = taxET.getText().toString().trim();
        String deliveryFee = deliveryFeeET.getText().toString().trim();
        String serviceFee = serviceFeeET.getText().toString().trim();
        String totalDue = totalDueEt.getText().toString().trim();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", AppConstants.BUSINESS_TABLE_SYSTEM_API.ADD_BUSINESS_VENDOR_DISCOUNT_SERVICE);
        jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(getActivity()));
        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(getActivity()));
        jsonObject.addProperty("originalAmount", originalAmount);
        jsonObject.addProperty("adjustAmount", adjustAmount);
        jsonObject.addProperty("eTax", eTax);
        jsonObject.addProperty("deliveryFee", deliveryFee);
        jsonObject.addProperty("serviceFee", serviceFee);
        jsonObject.addProperty("totalDue", totalDue);

        Log.v(TAG, "Request Json :- " + jsonObject.toString());

        vendorDiscountServiceTask(jsonObject);
    }

    private void vendorDiscountServiceTask(JsonObject jsonObject) {

        final ProgressHUD progressHD = ProgressHUD.show(getActivity(), "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });
        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.business_table_system_api(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG + "onResponseDeliverService", response.body().toString());
                String s = response.body().toString();

                try {
                    JSONObject jsonObject = new JSONObject(s);

                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {

                        JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                        String msg = jsonObject1.getString("msg");
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();

                        Fragment fragment = new FeedbackandReviewFragment();
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.replace(R.id.frame, fragment, fragment.getTag());
                        fragmentTransaction.commitAllowingStateLoss();
                    }
                    //  adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "Error on Failue :-" + Log.getStackTraceString(t));
                progressHD.dismiss();
            }
        });
    }
}
