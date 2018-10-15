package com.yberry.dinehawaii.Util;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class GoogleMatrixRequest extends AsyncTask<Void, Double, Void> {

    private static final String TAG = "GoogleMatrixRequest";
    private static final String API_KEY = AppConstants.GOOGLE_MATRIX_API_KEY;
    double origin_lat = 0.0, origin_long = 0.0;
    double dest_lat = 0.0, dest_long = 0.0;
    String url_request = "";
    OkHttpClient client = new OkHttpClient();

    public GoogleMatrixRequest(double origin_lat, double origin_long, double dest_lat, double dest_long) {
        this.origin_lat = origin_lat;
        this.origin_long = origin_long;
        this.dest_lat = dest_lat;
        this.dest_long = dest_long;
        url_request = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + origin_lat + "," + origin_long + "&destinations=" + dest_lat + "," + dest_long + "&mode=driving&units=imperial&key=" + API_KEY;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Log.e(TAG, "GoogleMatrixRequest: doInBackground: Request >> " + url_request);
        double distance = 0.0;
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
                String[] str = elementsObject.getJSONObject("distance").getString("text").split("\\s");
                if (str != null)
                    distance = Double.parseDouble(str[0]);
            }

            Log.e(TAG, "GoogleMatrixRequest: doInBackground: distance >> " + distance);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "doInBackground: Exc >> " + e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "doInBackground: Exc >> " + e.getMessage());
        }
        return null;
    }
}
