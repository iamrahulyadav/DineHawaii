package com.yberry.dinehawaii.Customer.Fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.JsonObject;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferences;
import com.yberry.dinehawaii.Util.FragmentIntraction;
import com.yberry.dinehawaii.customview.CustomTextView;

import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CustomerHomeFragment extends Fragment {
    private final String TAG = "CustomerHomeFragment";
    TextView tvSearchBuss, tvNearest, tvPlaceOrder, tvMakingres;
    LinearLayout orderLayout;
    CustomTextView activity_making_reservation, order_history, fav_order, book_reservation, loyality_point, voucher, feedback;
    FragmentIntraction intraction;
    private double latitude, longitude;
    private CustomTextView completeOrder;
    private View view;
    private FloatingActionMenu floatingActionMenu;
    private Context context;
    private String walletAmt = "0";

    public CustomerHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_customer_home, container, false);
        context = getActivity();
        if (intraction != null) {
            intraction.actionbarsetTitle("Home");
        }
        setHasOptionsMenu(true);
        tvSearchBuss = (TextView) view.findViewById(R.id.tvSearchBuss);
        tvNearest = (TextView) view.findViewById(R.id.viewNearest);
        tvPlaceOrder = (TextView) view.findViewById(R.id.placeOrder);
        tvMakingres = (TextView) view.findViewById(R.id.activity_making_reservation);
        activity_making_reservation = (CustomTextView) view.findViewById(R.id.activity_making_reservation);
        orderLayout = (LinearLayout) view.findViewById(R.id.orderHistLayout);
        order_history = (CustomTextView) view.findViewById(R.id.order_history);
        completeOrder = (CustomTextView) view.findViewById(R.id.completeOrder);
        fav_order = (CustomTextView) view.findViewById(R.id.fav_order);
        book_reservation = (CustomTextView) view.findViewById(R.id.book_reservation);

        loyality_point = (CustomTextView) view.findViewById(R.id.loyality_point);
        voucher = (CustomTextView) view.findViewById(R.id.voucher);
        feedback = (CustomTextView) view.findViewById(R.id.feedback);

        setFloatingButton();

        loyality_point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new LoyaltyPointFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.frame, fragment, fragment.getTag());
                fragmentTransaction.commitAllowingStateLoss();
            }
        });

        order_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChildViews();
            }
        });

        voucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new EGiftAndCoupons();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.frame, fragment, fragment.getTag());
                fragmentTransaction.commitAllowingStateLoss();
            }
        });

        fav_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("to", "fav");
                Fragment fragment = new OrderHistory();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.frame, fragment, fragment.getTag());
                fragmentTransaction.commitAllowingStateLoss();
            }
        });

        completeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("to", "com");
                Fragment fragment = new OrderHistory();
                fragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.frame, fragment, fragment.getTag());
                fragmentTransaction.commitAllowingStateLoss();
            }
        });

        book_reservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new OrderHistory();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.frame, fragment, fragment.getTag());
                fragmentTransaction.commitAllowingStateLoss();
            }
        });


        activity_making_reservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new RestaurantListFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.frame, fragment, fragment.getTag());
                fragmentTransaction.commitAllowingStateLoss();
                //Toast.makeText(context, "Choose a restaurant", Toast.LENGTH_SHORT).show();

            }
        });
        tvSearchBuss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new RestaurantListFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.frame, fragment, fragment.getTag());
                fragmentTransaction.commitAllowingStateLoss();
            }
        });


        if (!AppPreferences.getLatitude(getActivity()).equalsIgnoreCase("") || !AppPreferences.getLongitude(getActivity()).equalsIgnoreCase("")) {
            latitude = Double.parseDouble(AppPreferences.getLatitude(getActivity()));
            longitude = Double.parseDouble(AppPreferences.getLongitude(getActivity()));
        }


        tvNearest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("latitude", String.valueOf(latitude));
                bundle.putString("longitutde", String.valueOf(longitude));
                bundle.putString("busname", "");
                bundle.putString("location", "");
                bundle.putString("distance", "5");
                bundle.putString("foodid", "");
                bundle.putString("serviceid", "");
                Fragment fragment = new RestaurantListFilterFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragment.setArguments(bundle);
                fragmentTransaction.addToBackStack(null);

                fragmentTransaction.replace(R.id.frame, fragment, fragment.getTag());
                fragmentTransaction.commitAllowingStateLoss();
            }
        });
        tvPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new RestaurantListFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.frame, fragment, fragment.getTag());
                fragmentTransaction.commitAllowingStateLoss();
                //Toast.makeText(context, "Choose a restaurant", Toast.LENGTH_SHORT).show();
            }
        });

        new GetWallet().execute();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
//        menu.clear();
    }

    private void setFloatingButton() {
        new FloatingButton().showFloatingButton(view, getActivity());
        new FloatingButton().setFloatingButtonControls(view);
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

    private void openChildViews() {
        if (orderLayout.getVisibility() == View.VISIBLE) {
            orderLayout.setVisibility(View.GONE);
            order_history.setCompoundDrawablesWithIntrinsicBounds(R.drawable.drawable_green_dot, 0, R.drawable.ic_keyboard_arrow_right_green_24dp, 0);

        } else {
            orderLayout.setVisibility(View.VISIBLE);
            order_history.setCompoundDrawablesWithIntrinsicBounds(R.drawable.drawable_green_dot, 0, R.drawable.ic_keyboard_arrow_down_green_24dp, 0);


        }
    }

    private void createCustomAnimation() {
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator scaleOutX = ObjectAnimator.ofFloat(floatingActionMenu.getMenuIconView(), "scaleX", 1.0f, 0.2f);
        ObjectAnimator scaleOutY = ObjectAnimator.ofFloat(floatingActionMenu.getMenuIconView(), "scaleY", 1.0f, 0.2f);
        ObjectAnimator scaleInX = ObjectAnimator.ofFloat(floatingActionMenu.getMenuIconView(), "scaleX", 0.2f, 1.0f);
        ObjectAnimator scaleInY = ObjectAnimator.ofFloat(floatingActionMenu.getMenuIconView(), "scaleY", 0.2f, 1.0f);

        scaleOutX.setDuration(50);
        scaleOutY.setDuration(50);

        scaleInX.setDuration(150);
        scaleInY.setDuration(150);

        scaleInX.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                floatingActionMenu.getMenuIconView().setImageResource(floatingActionMenu.isOpened()
                        ? R.drawable.ic_arrow_back_white_24dp : R.drawable.ic_add_white_24dp);
            }
        });

        set.play(scaleOutX).with(scaleOutY);
        set.play(scaleInX).with(scaleInY).after(scaleOutX);
        set.setInterpolator(new OvershootInterpolator(2));

        floatingActionMenu.setIconToggleAnimatorSet(set);

    }

    public class FloatingButton {
        FrameLayout bckgroundDimmer;
        FloatingActionButton button1, button2;

        public void showFloatingButton(final View activity, final Context mContext) {

            floatingActionMenu = (FloatingActionMenu) activity.findViewById(R.id.material_design_android_floating_action_menu);
            button1 = (FloatingActionButton) activity.findViewById(R.id.material_design_floating_action_menu_orders);
            button2 = (FloatingActionButton) activity.findViewById(R.id.material_design_floating_action_menu_reservations);
            createCustomAnimation();

            button1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("to", "pen");
                    Fragment fragment = new OrderHistory();
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.addToBackStack(null);
                    fragment.setArguments(bundle);
                    fragmentTransaction.replace(R.id.frame, fragment, fragment.getTag());
                    fragmentTransaction.commitAllowingStateLoss();
                }
            });
            button2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Fragment fragment = new ReservationOrWaitListManagement();
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.replace(R.id.frame, fragment, fragment.getTag());
                    fragmentTransaction.commitAllowingStateLoss();
                }
            });

        }

        public void setFloatingButtonControls(View activity) {
            bckgroundDimmer = (FrameLayout) activity.findViewById(R.id.background_dimmer);
            floatingActionMenu = (FloatingActionMenu) activity.findViewById(R.id.material_design_android_floating_action_menu);
            floatingActionMenu.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
                @Override
                public void onMenuToggle(boolean opened) {
                    if (opened) {
                        bckgroundDimmer.setVisibility(View.VISIBLE);
                    } else {
                        bckgroundDimmer.setVisibility(View.GONE);
                    }
                }
            });
            bckgroundDimmer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (floatingActionMenu.isOpened()) {
                        floatingActionMenu.close(true);
                        bckgroundDimmer.setVisibility(View.GONE);
                        //menu opened
                    }
                }
            });
        }
    }

    class GetWallet extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
                        } else {
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.e("Response: onFailure 1", t.toString());
                }
            });
            return null;
        }
    }
}
