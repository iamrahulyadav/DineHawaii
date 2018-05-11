package com.yberry.dinehawaii.services;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.yberry.dinehawaii.Util.AppPreferencesBuss;



public class FusedLocationService implements LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG="FusedLocationService";
    private static final long INTERVAL = 0;//1000
    private static final long FASTEST_INTERVAL = 0;//500
    //	Activity locationActivity;
    Context locationActivity;
    private LocationRequest locationRequest;
    private GoogleApiClient googleApiClient;
    private Location location;
    private FusedLocationProviderApi fusedLocationProviderApi = LocationServices.FusedLocationApi;

    public FusedLocationService(Context context) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        this.locationActivity = context;

        googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();

        if (googleApiClient != null) {
            Log.d("GoogApiClient", googleApiClient.toString());
            googleApiClient.connect();
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        try {
            Location currentLocation = fusedLocationProviderApi
                    .getLastLocation(googleApiClient);

            if (currentLocation != null) {

                String lat = String.valueOf(currentLocation.getLatitude());
                String lon = String.valueOf(currentLocation.getLongitude());
                Log.d("check", lat);
                Log.d("check1", lon);


                // PreferenceClass.setLatitude(locationActivity,""+currentLocation.getLatitude());
                // PreferenceClass.setLongitute(locationActivity,""+currentLocation.getLongitude());
            }
            fusedLocationProviderApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onLocationChanged(Location location) {

        String lat = String.valueOf(location.getLatitude());
        String lon = String.valueOf(location.getLongitude());
        Log.d("check2", lat);
        Log.d("check3", lon);


        Log.e(TAG+"LatLONg",""+location.getLatitude());
        System.out.println("Current Long is: " + location.getLongitude());
        AppPreferencesBuss.setMapLongitute(locationActivity,lon);
        AppPreferencesBuss.setMapLatitude(locationActivity,lat);
        Log.d("checking898898898",AppPreferencesBuss.getMapLatitude(locationActivity));
       /* AppPreferencesBuss.setMapLatitude(FusedLocationService.this,lat);
        AppPreferencesBuss.setMapLongitute(FusedLocationService.this,lon);
      */  //PreferenceClass.setLatitude(locationActivity,""+location.getLatitude());
        // PreferenceClass.setLatitude(locationActivity,""+location.getLongitude());
    }



    public Location getLocation() {
        return this.location;
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        googleApiClient.connect();
    }
}