package com.yberry.dinehawaii.services;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.yberry.dinehawaii.Bussiness.Activity.OrderDetailActivty;
import com.yberry.dinehawaii.Bussiness.Activity.ReservationDeailsActivity;
import com.yberry.dinehawaii.Customer.Activity.CustomerOrderDetailActivity;
import com.yberry.dinehawaii.Customer.Activity.CustomerResDetailActivity;
import com.yberry.dinehawaii.R;

import org.json.JSONException;
import org.json.JSONObject;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    String messageBody = "", order_id = "", reserv_id = "";

    @SuppressLint("LongLogTag")
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        try {
            Log.e(TAG, "Notification Data >> " + remoteMessage.getData());
            JSONObject jsonObject = new JSONObject(remoteMessage.getData().toString());
            JSONObject jsonObject1 = jsonObject.getJSONObject("notification_data");
            if (jsonObject1.has("new_order")) {
                messageBody = jsonObject1.getString("new_order");
                order_id = jsonObject1.getString("order_id");
                Intent intent = new Intent(this, OrderDetailActivty.class);
                intent.putExtra("order_id", order_id);
                sendNotification(intent, messageBody);

            } else if (jsonObject1.has("Approve_UnApprove_Notify")) {
                messageBody = jsonObject1.getString("Approve_UnApprove_Notify");
                sendNotification(new Intent(), messageBody);

            } else if (jsonObject1.has("new_reservation")) {
                messageBody = jsonObject1.getString("new_reservation");
                reserv_id = jsonObject1.getString("reserv_id");
                Intent intent = new Intent(this, ReservationDeailsActivity.class);
                intent.setAction("SHOW");
                intent.putExtra("reservation_id", reserv_id);
                sendNotification(intent, messageBody);

            } else if (jsonObject1.has("reservation_confirmed")) {
                messageBody = jsonObject1.getString("reservation_confirmed");
                reserv_id = jsonObject1.getString("reserv_id");
                Intent intent = new Intent(this, CustomerResDetailActivity.class);
                intent.putExtra("reserv_id", reserv_id);
                intent.setAction("current_reserv");
                sendNotification(intent, messageBody);

            } else if (jsonObject1.has("reservation_cancelled")) {
                messageBody = jsonObject1.getString("reservation_cancelled");
                reserv_id = jsonObject1.getString("reserv_id");
                Intent intent = new Intent(this, CustomerResDetailActivity.class);
                intent.putExtra("reserv_id", reserv_id);
                intent.setAction("current_reserv");
                sendNotification(intent, messageBody);

            } else if (jsonObject1.has("order_completed")) {
                messageBody = jsonObject1.getString("order_completed");
                order_id = jsonObject1.getString("order_id");
                Intent intent = new Intent(this, CustomerOrderDetailActivity.class);
                intent.putExtra("order_id", order_id);
                sendNotification(intent, messageBody);

            } else if (jsonObject1.has("Recive_Coupon")) {
                messageBody = jsonObject1.getString("Recive_Coupon");
                sendNotification(new Intent(), messageBody);

            } else if (jsonObject1.has("extended_reservation_time")) {
                messageBody = jsonObject1.getString("extended_reservation_time");
                reserv_id = jsonObject1.getString("reserv_id");
                Intent intent = new Intent(this, CustomerResDetailActivity.class);
                intent.putExtra("reserv_id", reserv_id);
                intent.setAction("current_reserv");
                sendNotification(intent, messageBody);

            } else if (jsonObject1.has("waitlist_reservation_confirmed")) {
                messageBody = jsonObject1.getString("waitlist_reservation_confirmed");
                reserv_id = jsonObject1.getString("reserv_id");
                Intent intent = new Intent(this, CustomerResDetailActivity.class);
                intent.putExtra("reserv_id", reserv_id);
                intent.setAction("current_reserv");
                sendNotification(intent, messageBody);

            } else if (jsonObject1.has("noshow_reservation")) {
                messageBody = jsonObject1.getString("noshow_reservation");
                sendNotification(new Intent(), messageBody);
                Intent intent = new Intent(this, NotificationReciever.class);
                intent.putExtra("msg", jsonObject1.getString("noshow_reservation"));
                intent.putExtra("phone_no", jsonObject1.getString("phone_no"));
                intent.putExtra("reserv_id", jsonObject1.getString("reserv_id"));
                ((AlarmManager) getSystemService(Context.ALARM_SERVICE)).set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), PendingIntent.getBroadcast(getApplicationContext(), Integer.parseInt(jsonObject1.getString("reserv_id")), intent, PendingIntent.FLAG_UPDATE_CURRENT));

            } else if (jsonObject1.has("noshow_reservation_business")) {
                messageBody = jsonObject1.getString("noshow_reservation_business");
                Intent intent = new Intent(this, ReservationDeailsActivity.class);
                intent.setAction("NOSHOW");
                intent.putExtra("reservation_id", jsonObject1.getString("reserv_id"));
                sendNotification(intent, messageBody);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendNotification(Intent intent, String messageBody) {
        try {
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.not_icon)
                    .setContentTitle(messageBody)
                    .setContentText("Dine Hawaii Notification")
                    .setAutoCancel(true)
                    .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher))
                    .setSound(defaultSoundUri)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notificationBuilder.build());
        } catch (Exception e) {
            Log.e("Notification Ex", e.getMessage());
        }
    }

//    private void sendNotification1(String messageBody, String id) {
//        try {
//            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, .setAction("NOSHOW"),
//                    PendingIntent.FLAG_UPDATE_CURRENT);
//            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                    .setSmallIcon(R.drawable.not_icon)
//                    .setContentTitle(messageBody)
//                    .setContentText("Dine Hawaii Notification")
//                    .setAutoCancel(true)
//                    .setOngoing(false)
//                    .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher))
//                    .setSound(defaultSoundUri)
//                    .setPriority(Notification.PRIORITY_HIGH)
//                    .setPriority(Notification.PRIORITY_MAX)
//                    .setContentIntent(pendingIntent);
//            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//            notificationManager.notify(Integer.parseInt(id), notificationBuilder.build());
//        } catch (Exception e) {
//            Log.e("Notification Ex", e.getMessage());
//        }
//    }
}
