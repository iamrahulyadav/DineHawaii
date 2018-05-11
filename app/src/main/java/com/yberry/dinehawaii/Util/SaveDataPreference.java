package com.yberry.dinehawaii.Util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by abc on 06-Apr-18.
 */

public class SaveDataPreference {
    public static final String PREFERENCES = "save_data_pref";
    private static final String SAVEBusID = "save_id_bus";
    private static final String SAVEBusPASSWD = "save_pass_bus";
    private static final String SAVECustID = "save_id_cust";
    private static final String SAVECustPASSWD = "save_pass_cust";
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;
    public SaveDataPreference(Context context) {
        String prefsFile = context.getPackageName();
        sharedPreferences = context.getSharedPreferences(prefsFile, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static String getBusRembEmailId(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(PREFERENCES, 0);
        return pereference.getString(SAVEBusID, "");
    }


    public static String setBusRembEmailId(Context context, String id) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SAVEBusID, id);
        editor.commit();
        return id;
    }

    public static String getBusRembPassword(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(PREFERENCES, 0);
        return pereference.getString(SAVEBusPASSWD, "");
    }


    public static String setBusRembPassword(Context context, String id) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SAVEBusPASSWD, id);
        editor.commit();
        return id;
    }

    public static String getCustRembEmailId(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(PREFERENCES, 0);
        return pereference.getString(SAVECustID, "");
    }


    public static String setCustRembEmailId(Context context, String id) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SAVECustID, id);
        editor.commit();
        return id;
    }

    public static String getCustRembPassword(Context context) {
        SharedPreferences pereference = context.getSharedPreferences(PREFERENCES, 0);
        return pereference.getString(SAVECustPASSWD, "");
    }


    public static String setCustRembPassword(Context context, String id) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SAVECustPASSWD, id);
        editor.commit();
        return id;
    }

}
