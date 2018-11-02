package com.yberry.dinehawaii.common.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Customer.Adapter.CustomInfoWindowAdapter;
import com.yberry.dinehawaii.Model.ListItem;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.customview.CustomTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OrderMapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final String TAG = "MapsActivity";
    private static final long INTERVAL = 15000;//1000
    private static final long FASTEST_INTERVAL = 15000;//500
    ArrayList<ListItem> listItems;
    ImageView direction;
    Context locationActivity;
    CustomInfoWindowAdapter adapter;
    private GoogleMap mMap;
    private LocationRequest locationRequest;
    private GoogleApiClient googleApiClient;
    private FusedLocationProviderApi fusedLocationProviderApi = LocationServices.FusedLocationApi;
    private double cust_lat = 0, cust_long = 0;
    private double busi_lat = 0, busi_long = 0;
    private double driver_lat = 0, driver_long = 0;
    private String cust_address = "", busi_address = "";
    private String order_id = "0";
    private OrderMapActivity context;
    private Marker markerDriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setToolbar();
        context = this;
        cust_lat = Double.parseDouble(getIntent().getStringExtra("cust_lat"));
        cust_long = Double.parseDouble(getIntent().getStringExtra("cust_long"));
        cust_address = getIntent().getStringExtra("cust_add");
        busi_lat = Double.parseDouble(getIntent().getStringExtra("busi_lat"));
        busi_long = Double.parseDouble(getIntent().getStringExtra("busi_long"));
        busi_address = getIntent().getStringExtra("busi_address");
        order_id = getIntent().getStringExtra("order_id");


        Log.e(TAG, "onCreate: cust_lat >> " + cust_lat);
        Log.e(TAG, "onCreate: cust_long >> " + cust_long);
        Log.e(TAG, "onCreate: cust_address >> " + cust_address);
        Log.e(TAG, "onCreate: busi_lat >> " + busi_lat);
        Log.e(TAG, "onCreate: busi_long >> " + busi_long);
        Log.e(TAG, "onCreate: busi_address >> " + busi_address);
        Log.e(TAG, "onCreate: order_id >> " + order_id);
        setFusedLocationListener();
        direction = (ImageView) findViewById(R.id.getDirection);
        tvDistance = findViewById(R.id.tvDistance);
        tvTime = findViewById(R.id.tvTime);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        /*showProgressDialog();*/

        new DriverTask().execute();
    }

    private void setFusedLocationListener() {
        //setting fused location api
        locationRequest = new LocationRequest();
        locationRequest.setInterval(INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).addConnectionCallbacks(context)
                .addOnConnectionFailedListener(context)
                .build();
        if (googleApiClient != null) {
            Log.d("GoogApiClient", googleApiClient.toString());
            googleApiClient.connect();
        }

    }

    /*private void showProgressDialog() {
        progressHD = ProgressHUD.show(context, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void dismissProgressDialog() {
        if (progressHD != null && progressHD.isShowing()) {
            progressHD.dismiss();
        }
    }
*/
    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_bar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
       /* getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);*/
        CustomTextView toolBarTextView = (CustomTextView) findViewById(R.id.headet_text);
        ImageView back = (ImageView) findViewById(R.id.back);
        toolBarTextView.setText("Track Order");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                LatLng custLatLng = new LatLng(cust_lat, cust_long);
                LatLng busiLatLng = new LatLng(busi_lat, busi_long);
                LatLng driverLatLng = new LatLng(driver_lat, driver_long);

                if (cust_lat != 0 && cust_long != 0) {
                    mMap.addMarker(new MarkerOptions().position(custLatLng)
                            .title("Customer")
                            .snippet(cust_address)
//                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_rest_green));
                            .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.marker_red_customer, cust_address))));
                }
                if (busi_lat != 0 && busi_long != 0) {
                    mMap.addMarker(new MarkerOptions().position(busiLatLng)
                            .title("Restaurant")
                            .snippet(busi_address)
//                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_rest_green));
                            .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.marker_rest_green, busi_address))));
                }

                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(custLatLng, 13);
                mMap.animateCamera(cameraUpdate);
                //dismissProgressDialog();
            }
        });


    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try {
            Location currentLocation = fusedLocationProviderApi.getLastLocation(googleApiClient);
            if (currentLocation != null) {
                Log.e(TAG, "onConnected: Location >> " + currentLocation);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 10));
            }
            fusedLocationProviderApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            Log.e(TAG, "onLocationChanged: Location >> " + location);
            new DriverTask().execute();
        }
    }

    private Bitmap getMarkerBitmapFromView(@DrawableRes int resId, String businessName) {

        View customMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_custom_marker, null);
        ImageView markerImageView = (ImageView) customMarkerView.findViewById(R.id.profile_image);
        CustomTextView tvName = (CustomTextView) customMarkerView.findViewById(R.id.tvName);
        markerImageView.setImageResource(resId);
        tvName.setText(businessName);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }


    class DriverTask extends AsyncTask<Void, String, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(AppConstants.KEY_METHOD, AppConstants.BUSSINES_USER_BUSINESSAPI.TRACK_ORDER);
            jsonObject.addProperty("order_id", order_id);
            Log.e(TAG, "DriverTask: Request >> " + jsonObject.toString());
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

            Call<JsonObject> call = apiService.today_orders(jsonObject);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.body().toString());

                        Log.e(TAG, "DriverTask: Response >> " + jsonObject);
                        if (jsonObject.getString("status").equalsIgnoreCase("200")) {
                            driver_lat = Double.parseDouble(jsonObject.getJSONArray("result").getJSONObject(0).getString("driver_lat"));
                            driver_long = Double.parseDouble(jsonObject.getJSONArray("result").getJSONObject(0).getString("driver_long"));
                            Log.e(TAG, "onResponse: driver_lat >> " + driver_lat);
                            Log.e(TAG, "onResponse: driver_long >> " + driver_long);
                            publishProgress("200", "");
                        } else {
                            String msg = jsonObject.getJSONArray("result").getJSONObject(0).getString("msg");
                            publishProgress("400", msg);
                        }

                    } catch (Exception e) {
                        Log.e(TAG, "DriverTask: onFailure >> " + e.getMessage());
                        publishProgress("400", "Server not responding...");
                    }

                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.e(TAG, "DriverTask: onFailure >> " + t.getMessage());
                    publishProgress("400", "Server not responding...");

                }
            });
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

            if (markerDriver == null) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(new LatLng(driver_lat, driver_long));
                markerOptions.title("Driver");
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.marker_red_driver, cust_address)));
                markerDriver = mMap.addMarker(markerOptions);
            } else {
                animateMarker(markerDriver, new LatLng(driver_lat, driver_long), false);
//            markerDriver.setPosition(new LatLng(location.getLatitude(),location.getLongitude()));
            }
            if (values[0].equalsIgnoreCase("400")) {
                new AlertDialog.Builder(context)
                        .setMessage(values[1])
                        .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new DriverTask().execute();
                            }
                        }).
                        setNegativeButton("Go Back", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                onBackPressed();
                            }
                        }).show();
            }

            new GoogleMatrixRequest().execute();


        }

        private void animateMarker(final Marker marker, final LatLng toPosition, final boolean hideMarker) {

            final Handler handler = new Handler();
            final long start = SystemClock.uptimeMillis();
            Projection proj = mMap.getProjection();
            Point startPoint = proj.toScreenLocation(marker.getPosition());
            final LatLng startLatLng = proj.fromScreenLocation(startPoint);
            final long duration = 1000;

            final LinearInterpolator interpolator = new LinearInterpolator();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    long elapsed = SystemClock.uptimeMillis() - start;
                    float t = interpolator.getInterpolation((float) elapsed / duration);
                    double lng = t * toPosition.longitude + (1 - t) * startLatLng.longitude;
                    double lat = t * toPosition.latitude + (1 - t) * startLatLng.latitude;
                    marker.setPosition(new LatLng(lat, lng));

                    if (t < 1.0) {
                        // Post again 16ms later.
                        handler.postDelayed(this, 16);
                    } else {
                        if (hideMarker) {
                            marker.setVisible(false);
                        } else {
                            marker.setVisible(true);
                        }
                    }
                }
            });
        }
    }

    private TextView tvDistance, tvTime;

    public class GoogleMatrixRequest extends AsyncTask<Void, String, Void> {

        private static final String API_KEY = AppConstants.GOOGLE_MATRIX_API_KEY;
        String url_request = "";
        OkHttpClient client = new OkHttpClient();

        public GoogleMatrixRequest() {
            url_request = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + driver_lat + "," + driver_long + "&destinations=" + cust_lat + "," + cust_long + "&mode=driving&units=imperial&key=" + API_KEY;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.e(TAG, "GoogleMatrixRequest: doInBackground: Request >> " + url_request);
            Request request = new Request.Builder()
                    .url(url_request)
                    .build();

            okhttp3.Response response = null;
            try {
                response = client.newCall(request).execute();
                String response_str = response.body().string();
                Log.e(TAG, "GoogleMatrixRequest: doInBackground: Response >> " + response_str);
                JSONObject jsonObject = new JSONObject(response_str);
                JSONObject elementsObject = jsonObject.getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0);
                if (elementsObject.getString("status").equalsIgnoreCase("OK")) {
                    String dist = elementsObject.getJSONObject("distance").getString("text");
                    String time = elementsObject.getJSONObject("duration").getString("text");
                    publishProgress(dist, time);
                } else if (elementsObject.getString("status").equalsIgnoreCase("ZERO_RESULTS")) {
                    publishProgress("Calculating", "Calculating");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            tvDistance.setText("Driver Distance : " + values[0]);
            tvTime.setText("Driver Arrive in  : " + values[1]);
            Log.e(TAG, "GoogleMatrixRequest: onProgressUpdate: distance >> " + values[0]);
            Log.e(TAG, "GoogleMatrixRequest: onProgressUpdate: time >> " + values[1]);
        }
    }
}