package com.yberry.dinehawaii.Util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class PreferenceClass {
    public static Context appContext;
    private static String DINEHAWAII_PREFRENCE;

    public static void setStringPreference(Context context, String name, String value) {
        appContext = context;
        SharedPreferences settings = context.getSharedPreferences(DINEHAWAII_PREFRENCE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(name, value);
        editor.commit();
    }


    public static void setStringEmailPreference(Context context, String name, String value) {
        appContext = context;
        SharedPreferences settings = context.getSharedPreferences(DINEHAWAII_PREFRENCE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(name, value);
        editor.commit();
    }

    public static void setStringMobilePreference(Context context, String name, String value) {
        appContext = context;
        SharedPreferences settings = context.getSharedPreferences(DINEHAWAII_PREFRENCE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(name, value);
        editor.commit();
    }


    public static void setDistanceStringPreference(Context context, String name, String value) {
        appContext = context;
        SharedPreferences settings = context.getSharedPreferences(DINEHAWAII_PREFRENCE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(name, value);
        editor.commit();
    }


    public static void setIntegerPreference(Context context, String name, int value) {
        appContext = context;
        SharedPreferences settings = context.getSharedPreferences(DINEHAWAII_PREFRENCE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(name, value);
        editor.commit();
    }

    public static void setBooleanPreference(Context context, String name, boolean value) {
        appContext = context;
        SharedPreferences settings = context.getSharedPreferences(DINEHAWAII_PREFRENCE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(name, value);
        editor.commit();

    }


    public static String getStringPreferences(Context context, String name) {
        SharedPreferences settings = context.getSharedPreferences(DINEHAWAII_PREFRENCE, 0);
        return settings.getString(name, "");
    }

    public static String getStringEmailPreferences(Context context, String name) {
        SharedPreferences settings = context.getSharedPreferences(DINEHAWAII_PREFRENCE, 0);
        return settings.getString(name, "Login/Signup");
    }

    public static String getStringMobilePreferences(Context context, String name) {
        SharedPreferences settings = context.getSharedPreferences(DINEHAWAII_PREFRENCE, 0);
        return settings.getString(name, "Welcome");
    }

    public static String getDistanceStringPreferences(Context context, String name) {
        SharedPreferences settings = context.getSharedPreferences(DINEHAWAII_PREFRENCE, 0);
        return settings.getString(name, "50");
    }

    public static int getIntegerPreferences(Context context, String name) {
        SharedPreferences settings = context.getSharedPreferences(DINEHAWAII_PREFRENCE, 0);
        return settings.getInt(name, 0);
    }

    public static boolean getBooleanPreferences(Context context, String name) {
        SharedPreferences settings = context.getSharedPreferences(DINEHAWAII_PREFRENCE, 0);
        return settings.getBoolean(name, false);
    }

    public static void clearPreference(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(DINEHAWAII_PREFRENCE, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

    public static void setListStringPrefrence(Context context, String name, Set<String> set){
        appContext = context;
        Set<String> list=new HashSet<>();
        list.addAll(set);
        SharedPreferences settings = context.getSharedPreferences(DINEHAWAII_PREFRENCE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putStringSet(name, list);
        editor.commit();}

    public static Set<String> getListStringPrefrence(Context context, String name){
        SharedPreferences settings = context.getSharedPreferences(DINEHAWAII_PREFRENCE, 0);
        return settings.getStringSet(name, null);

    }
}
