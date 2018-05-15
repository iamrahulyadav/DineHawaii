package com.yberry.dinehawaii.Bussiness.Activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;
import com.yberry.dinehawaii.Bussiness.Fragment.BusinessHomeFragment41;
import com.yberry.dinehawaii.Bussiness.Fragment.BusinessProfileFragment;
import com.yberry.dinehawaii.Bussiness.Fragment.FeedbackByCustomerFragment;
import com.yberry.dinehawaii.Bussiness.Fragment.ManageOrdersFragment;
import com.yberry.dinehawaii.Bussiness.Fragment.NotificationBusinessFragment;
import com.yberry.dinehawaii.Bussiness.Fragment.ReportsFragment;
import com.yberry.dinehawaii.Bussiness.Fragment.TableManagmentFragment;
import com.yberry.dinehawaii.HomeScreenActivity;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.AppPreferences;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Util.FragmentIntraction;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.customview.CustomTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BusinessNaviDrawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, FragmentIntraction {

    private static final String TAG = "BusinessNaviDrawer";
    public static ImageView mUserImage;
    public TextView headText;
    ImageView headLogo;
    NavigationView navigationView;
    View headerView;
    Toolbar toolbar;
    private TextView customerName;

    @Override
    public void actionbarsetTitle(String title) {
        headText.setText(title);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_navi_drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        headText = (CustomTextView) findViewById(R.id.headText);
        headLogo = (ImageView) findViewById(R.id.headLogo);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        LocalBroadcastManager.getInstance(this).registerReceiver(new Receiver(), new IntentFilter("updated_profile_bus"));
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);
        headerView = navigationView.getHeaderView(0);
        customerName = (CustomTextView) headerView.findViewById(R.id.customerName);
        Log.e("imge04", AppPreferencesBuss.getProfileImage(BusinessNaviDrawer.this));
        Log.e("nameof person", AppPreferencesBuss.getfirstname(BusinessNaviDrawer.this));
        mUserImage = (ImageView) headerView.findViewById(R.id.mUserImage);

        if (!AppPreferencesBuss.getfirstname(BusinessNaviDrawer.this).equalsIgnoreCase("")) {
            customerName.setText(AppPreferencesBuss.getfirstname(BusinessNaviDrawer.this) + "\n"
                    + AppPreferencesBuss.getEmpPosition(BusinessNaviDrawer.this));
        } else
            customerName.setText("No Name");
        setProfileImage();
        Log.e(TAG, "onCreate: BusinessID >> " + AppPreferencesBuss.getBussiId(this));
        Log.e(TAG, "onCreate: UserID >> " + AppPreferencesBuss.getUserId(this));

        if (AppPreferences.getUserType(this).equalsIgnoreCase(AppConstants.BUSS_LOGIN_TYPE.BUSINESS_USER)) {
            checkPaymentStatus();
        } else if (AppPreferences.getUserType(this).equalsIgnoreCase(AppConstants.BUSS_LOGIN_TYPE.BUSSINESS_LOCAL_USER)) {
            checkLoggedInUser();
            callToDefault();
        }
    }

    private void checkPaymentStatus() {
        new CheckPaymentTask().execute();
    }

    private void checkLoggedInUser() {
        String duties = AppPreferencesBuss.getAllottedDuties(BusinessNaviDrawer.this);
        if (AppPreferences.getUserType(BusinessNaviDrawer.this).equalsIgnoreCase(AppConstants.BUSS_LOGIN_TYPE.BUSSINESS_LOCAL_USER)) {
            if (!TextUtils.isEmpty(duties)) {
                Menu nav_Menu = navigationView.getMenu();
                nav_Menu.findItem(R.id.nav_e_gift).setVisible(false);
                nav_Menu.findItem(R.id.nav_neworder).setVisible(false);
                nav_Menu.findItem(R.id.nav_edit_reservation).setVisible(false);
                nav_Menu.findItem(R.id.nav_profile).setVisible(true);
                nav_Menu.findItem(R.id.notification).setVisible(false);
                nav_Menu.findItem(R.id.nav_myreview).setVisible(false);
                nav_Menu.findItem(R.id.nav_orderhistory).setVisible(false);
                nav_Menu.findItem(R.id.nav_logout).setVisible(true);

              /*  if (duties.contains("1"));

                if (duties.contains("2"));

                if (duties.contains("3"));

                if (duties.contains("4"));

                if (duties.contains("5"));
                if (duties.contains("6"));

                if (duties.contains("7"))
                    nav_Menu.findItem(R.id.nav_edit_reservation).setVisible(false);
                if (duties.contains("8"))
                    nav_Menu.findItem(R.id.nav_edit_reservation).setVisible(true);
                if (duties.contains("9"))
                    nav_Menu.findItem(R.id.nav_edit_reservation).setVisible(false);
                if (duties.contains("10"))
                   // manage_table.setEnabled(true);

                if (duties.contains("11"));
                if (duties.contains("12"));
                if (duties.contains("13"));
                if (duties.contains("14"));
                if (duties.contains("15"));*/

            }
        }
    }

    private void setProfileImage() {
        if (!AppPreferencesBuss.getProfileImage(BusinessNaviDrawer.this).equalsIgnoreCase("")) {
            Picasso.with(BusinessNaviDrawer.this)
                    .load(AppPreferencesBuss.getProfileImage(BusinessNaviDrawer.this))
                    .placeholder(R.drawable.pic)
                    .error(R.drawable.pic)
                    .into(mUserImage);
        }
    }

    private void checkPackageOptions() {
        String packages = AppPreferencesBuss.getBussiPackagelist(this);
        Log.e(TAG, "checkPackage: " + packages);
        Log.e(TAG, "checkOptions: " + AppPreferencesBuss.getBussiOptionlist(this));
        if (!packages.contains("1")) {
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.nav_neworder).setVisible(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //headText.setText("Home");
    }

    private void callToDefault() {
        Fragment fragment = new BusinessHomeFragment41();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment, fragment.getTag());
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            getSupportFragmentManager().popBackStack();
            showExitAlert();
        } else {
            super.onBackPressed();
        }
    }

    private void showExitAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(BusinessNaviDrawer.this);
        alertDialog.setIcon(R.drawable.ic_launcher_app);
        alertDialog.setMessage("Do you want to exit?");
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        alertDialog.show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = null;
        if (id == R.id.nav_home) {
            headText.setText("Home");
            fragment = new BusinessHomeFragment41();
        } else if (id == R.id.nav_orderhistory) {
            headText.setText("Reports");
            fragment = new ReportsFragment();
        } else if (id == R.id.nav_profile) {
            headText.setText("My Profile");
            fragment = new BusinessProfileFragment();
        } else if (id == R.id.nav_e_gift) {
            headText.setText("Orders");
            fragment = new ManageOrdersFragment();
        } else if (id == R.id.nav_myreview) {
            headText.setText("Customer Feedbacks");
            fragment = new FeedbackByCustomerFragment();
        } else if (id == R.id.nav_neworder) {
            headText.setText("Reservations");
            fragment = new TableManagmentFragment();
        } else if (id == R.id.nav_edit_reservation) {
            //headText.setText("HOME");
            Intent intent = new Intent(BusinessNaviDrawer.this, BusiSelectPackageActivity.class);
            intent.putExtra("title", "EDIT PACKAGE");
            startActivity(intent);
        } else if (id == R.id.notification) {
            headText.setText("Notifications");
            fragment = new NotificationBusinessFragment();
        } else if (id == R.id.nav_logout) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(BusinessNaviDrawer.this);
            alertDialog.setMessage("Do you want to logout?");
            alertDialog.setIcon(R.drawable.ic_launcher_app);
            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    logoutData();
                }
            });

            alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        if (fragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame, fragment, fragment.getTag());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commitAllowingStateLoss();
        }
        return true;
    }

    private void logoutData() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", AppConstants.GENERALAPI.LOGOUT);
        jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(BusinessNaviDrawer.this));
        jsonObject.addProperty("user_type", AppPreferencesBuss.getUsertypeid(BusinessNaviDrawer.this));
        Log.e(TAG + "json", jsonObject.toString());
        logoutApi(jsonObject);
    }

    private void logoutApi(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(BusinessNaviDrawer.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

                // TODO Auto-generated method stub
            }
        });


        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.request(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "logout response" + response.body().toString());
                String resp = response.body().toString();
                try {
                    JSONObject jsonObject = new JSONObject(resp);
                    if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                        AppPreferences.clearPreference(BusinessNaviDrawer.this);
                        AppPreferencesBuss.clearPreference(BusinessNaviDrawer.this);
                        AppPreferences.clearPreference(BusinessNaviDrawer.this);
                        AppPreferencesBuss.clearPreference(BusinessNaviDrawer.this);
                        Toast.makeText(BusinessNaviDrawer.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(BusinessNaviDrawer.this, HomeScreenActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject object = jsonArray.getJSONObject(0);
                        Toast.makeText(BusinessNaviDrawer.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "error :- " + Log.getStackTraceString(t));
                progressHD.dismiss();
                Toast.makeText(BusinessNaviDrawer.this, "Server not Responding", Toast.LENGTH_SHORT).show();
            }
        });
    }

    class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            customerName.setText(AppPreferencesBuss.getfirstname(BusinessNaviDrawer.this));
            setProfileImage();
        }
    }

    class CheckPaymentTask extends AsyncTask<Void, Void, Void> {
        ProgressHUD progressHD;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressHD = ProgressHUD.show(BusinessNaviDrawer.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                }
            });
        }

        @Override
        protected Void doInBackground(Void... voids) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("method", AppConstants.REGISTRATION.CHECK_PAYMENT_STATUS);
            jsonObject.addProperty("business_id", AppPreferencesBuss.getBussiId(BusinessNaviDrawer.this));
            jsonObject.addProperty("user_id", AppPreferencesBuss.getUserId(BusinessNaviDrawer.this));
            Log.e(TAG, "Request CHECK PAYMENT STATUS >> " + jsonObject.toString());
            MyApiEndpointInterface apiService = ApiClient.getClient().create(MyApiEndpointInterface.class);
            Call<JsonObject> call = apiService.requestBusinessUserGeneralurl(jsonObject);
            call.enqueue(new Callback<JsonObject>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    Log.e(TAG, "Response CHECK PAYMENT STATUS >> " + response.body().toString());
                    String s = response.body().toString();
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        if (jsonObject.getString("status").equalsIgnoreCase("200")) {
//                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            publishProgress(200, "");
                        } else if (jsonObject.getString("status").equalsIgnoreCase("400")) {
                            String message = jsonObject.getString("message");
                            publishProgress(400, message);
                        } else publishProgress(0, "");
                    } catch (JSONException e) {
                        e.printStackTrace();
                        publishProgress(0, "");
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.e(TAG, "onFailure: " + t.getMessage());
                    publishProgress(0, "");
                }
            });
            return null;
        }

        private void publishProgress(int status, String message) {
            if (progressHD != null && progressHD.isShowing())
                progressHD.dismiss();
            if (status == 200) {
                checkPackageOptions();
                callToDefault();
            } else if (status == 400) {
                Toast.makeText(BusinessNaviDrawer.this, "Attention : " + message, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(BusinessNaviDrawer.this, BusiSelectPackageActivity.class);
                intent.setAction("register");
                intent.putExtra("title", "BUSINESS INFORMATION");
                startActivity(intent);
            } else {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(BusinessNaviDrawer.this);
                alertDialog.setMessage("Some Error Occurred. Try Again");
                alertDialog.setIcon(R.drawable.ic_launcher_app);
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        new CheckPaymentTask().execute();
                    }
                });

                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
            }
        }
    }


}
