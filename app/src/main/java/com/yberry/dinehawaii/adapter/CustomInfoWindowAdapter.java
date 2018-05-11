package com.yberry.dinehawaii.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.yberry.dinehawaii.Customer.Activity.MapsActivity;
import com.yberry.dinehawaii.R;

/**
 * Created by RK on 11/02/2018.
 */

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private Activity context;

    public CustomInfoWindowAdapter(MapsActivity mapsActivity) {
        this.context = mapsActivity;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = context.getLayoutInflater().inflate(R.layout.custominfowindow, null);
        TextView tvName = (TextView) view.findViewById(R.id.tvName);
        TextView tvAddress = (TextView) view.findViewById(R.id.tvAddress);
        tvName.setText(marker.getTitle());
        tvAddress.setText(marker.getSnippet());
        return view;
    }
}