package com.yberry.dinehawaii.Customer.Activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.yberry.dinehawaii.Model.ListItem;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.Customer.Adapter.CustomInfoWindowAdapter;
import com.yberry.dinehawaii.customview.CustomTextView;

import java.util.ArrayList;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final String TAG = "MapsActivity";
    private static final long INTERVAL = 5000;//1000
    private static final long FASTEST_INTERVAL = 5000;//500
    ArrayList<ListItem> listItems;
    ImageView direction;
    Context locationActivity;
    CustomInfoWindowAdapter adapter;
    private GoogleMap mMap;
    private ProgressHUD progressHD;
    private LocationRequest locationRequest;
    private GoogleApiClient googleApiClient;
    private FusedLocationProviderApi fusedLocationProviderApi = LocationServices.FusedLocationApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setToolbar();

        setFusedLocationListener();
        direction = (ImageView) findViewById(R.id.getDirection);
        listItems = getIntent().getParcelableArrayListExtra("listItems");
       /* if (getIntent().getAction().equals("SearchedFragment")) {
            direction.setVisibility(View.GONE);
        } else if (getIntent().getAction().equals("Restaurant")) {
            direction.setVisibility(View.VISIBLE);
        } else {
            direction.setVisibility(View.GONE);
        }*/
        Log.e(TAG, "onCreate: listItems >> " + listItems);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        showProgressDialog();
    }

    private void setFusedLocationListener() {
        //setting fused location api
        locationRequest = new LocationRequest();
        locationRequest.setInterval(INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        googleApiClient = new GoogleApiClient.Builder(MapsActivity.this)
                .addApi(LocationServices.API).addConnectionCallbacks(MapsActivity.this)
                .addOnConnectionFailedListener(MapsActivity.this)
                .build();
        if (googleApiClient != null) {
            Log.d("GoogApiClient", googleApiClient.toString());
            googleApiClient.connect();
        }

    }

    private void showProgressDialog() {
        progressHD = ProgressHUD.show(MapsActivity.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
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

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_bar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
       /* getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);*/
        CustomTextView toolBarTextView = (CustomTextView) findViewById(R.id.headet_text);
        ImageView back = (ImageView) findViewById(R.id.back);
        toolBarTextView.setText("Browse On Map");
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
                for (final ListItem listItem : listItems) {
                    Log.e(TAG, "onMapLoaded: listItem >> " + listItem);
                    if (listItem.getLatitude() != null && !listItem.getLatitude().equalsIgnoreCase("") && listItem.getLongitude() != null && !listItem.getLongitude().equalsIgnoreCase("")) {
                        final String latatitude = listItem.getLatitude();
                        final String longtitude = listItem.getLongitude();
                        String name = "", address = "", contact = "";
                        name = listItem.getBusinessName();
                        address = listItem.getBusinessAddress();
                        contact = listItem.getBusinessContactNo();
                        LatLng latLng = new LatLng(Double.parseDouble(latatitude), Double.parseDouble(longtitude));
                        MarkerOptions markerOpt = new MarkerOptions();
                        markerOpt.position(latLng)
                                .title(name)
                                .snippet(address + "\nContact No. " + contact)
//                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_rest_green));
                                .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.marker_rest_green, listItem.getBusinessName())));

                        direction.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                              /*  String uri = String.format(Locale.ENGLISH, "geo:%f,%f", Float.parseFloat(latatitude), Float.parseFloat(longtitude));
                                String geoUri = "http://maps.google.com/maps?q=loc:" + Float.parseFloat(latatitude) + "," + Float.parseFloat(longtitude) + " (" + listItem.getBusinessName() + ")";
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                                startActivity(intent);*/

                                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                        Uri.parse("google.navigation:q=" + Float.parseFloat(latatitude)
                                                + "," + Float.parseFloat(longtitude) + ""));

                                startActivity(intent);
                            }
                        });
                        adapter = new CustomInfoWindowAdapter(MapsActivity.this);
                        mMap.setInfoWindowAdapter(adapter);
                        if (listItems.size() == 1) {
                            mMap.addMarker(markerOpt).showInfoWindow();
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(latatitude), Double.parseDouble(longtitude)), 15));
                            direction.setVisibility(View.VISIBLE);
                        } else if (listItems.size() > 1)
                            mMap.addMarker(markerOpt);


                       /* CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 13);
                        mMap.animateCamera(cameraUpdate);*/
                    }
                    dismissProgressDialog();
                }
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
}



















































/*
package com.yberry.dinehawaii.Customer.Fragment;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;
import com.yberry.dinehawaii.Model.ListItem;
import com.yberry.dinehawaii.services.FusedLocationService;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ArrayList<ListItem> listItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        new FusedLocationService(this);
        Log.e("checking898898898hggggggg",AppPreferencesBuss.getMapLatitude(MapsActivity.this));
        Log.e("checkinggggggg",AppPreferencesBuss.getMapLongitute(MapsActivity.this));
       // Toast.makeText(this, AppPreferencesBuss.getMapLatitude(MapsActivity.this), Toast.LENGTH_SHORT).show();
    //    Toast.makeText(this, AppPreferencesBuss.getLo(MapsActivity.this), Toast.LENGTH_SHORT).show();
        listItems = getIntent().getParcelableArrayListExtra("listItems");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera



        for (ListItem listItem : listItems){

 String latatitude = listItem.getLatitude() == null ? "0.0" : listItem.getLatitude();
 String longtitude = listItem.getLongitude() == null ? "0.0" : listItem.getLongitude();

           LatLng latLng = new LatLng(Double.parseDouble(longtitude),Double.parseDouble(latatitude));
            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.logo);
            mMap.addMarker(new MarkerOptions().position(latLng).icon(icon));
            //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
}

    }
}
*/
