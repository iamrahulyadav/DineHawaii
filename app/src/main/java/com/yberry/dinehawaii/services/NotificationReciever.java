package com.yberry.dinehawaii.services;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.WindowManager;

import com.yberry.dinehawaii.Customer.Activity.DialogActivity;

public class NotificationReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("NotificationReciever", "onReceive: msg >> "+intent.getStringExtra("msg") );
        Log.e("NotificationReciever", "onReceive: phone_no >> "+intent.getStringExtra("phone_no") );
        Log.e("NotificationReciever", "onReceive: reserv_id >>"+intent.getStringExtra("reserv_id") );
        intent.setClass(context,DialogActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
