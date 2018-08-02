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
import com.yberry.dinehawaii.Util.AppConstants;

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
            if (jsonObject.has("notification_data")) {
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
            }

            if (remoteMessage.getData().size() > 0) {
                String BUSINESS_DELIVERY_COMPLETED = "" + remoteMessage.getData().get(AppConstants.NOTIFICATION_KEY.BUSINESS_DELIVERY_COMPLETED);
                String BUSINESS_DELIVERY_PICKEDUP = "" + remoteMessage.getData().get(AppConstants.NOTIFICATION_KEY.BUSINESS_DELIVERY_PICKEDUP);
                String CUSTOMER_DELIVERY_COMPLETED = "" + remoteMessage.getData().get(AppConstants.NOTIFICATION_KEY.CUSTOMER_DELIVERY_COMPLETED);
                String CUSTOMER_DELIVERY_PICKEDUP = "" + remoteMessage.getData().get(AppConstants.NOTIFICATION_KEY.CUSTOMER_DELIVERY_PICKEDUP);
                String CUSTOMER_OTHER_MSG = "" + remoteMessage.getData().get(AppConstants.NOTIFICATION_KEY.CUSTOMER_OTHER_MSG);
                String BUSINESS_OTHER_MSG = "" + remoteMessage.getData().get(AppConstants.NOTIFICATION_KEY.BUSINESS_OTHER_MSG);
                String DRIVER_ARRIVED = "" + remoteMessage.getData().get(AppConstants.NOTIFICATION_KEY.DRIVER_ARRIVED);
                String REPLY_REVIEW = "" + remoteMessage.getData().get(AppConstants.NOTIFICATION_KEY.REPLY_REVIEW);
                String REPLY_ORDER = "" + remoteMessage.getData().get(AppConstants.NOTIFICATION_KEY.REPLY_ORDER);
                String REPLY_RESERVATION = "" + remoteMessage.getData().get(AppConstants.NOTIFICATION_KEY.REPLY_RESERVATION);
                String NEW_ORDER_FEEDBACK = "" + remoteMessage.getData().get(AppConstants.NOTIFICATION_KEY.NEW_ORDER_FEEDBACK);
                String NEW_RESERVATION_FEEDBACK = "" + remoteMessage.getData().get(AppConstants.NOTIFICATION_KEY.NEW_RESERVATION_FEEDBACK);
                if (!BUSINESS_DELIVERY_PICKEDUP.equalsIgnoreCase("null")) {
                    JSONObject jsonObj = null;
                    try {
                        jsonObj = new JSONObject(BUSINESS_DELIVERY_PICKEDUP);
                        sendNotification(new Intent(), jsonObj.getString("msg"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (!CUSTOMER_DELIVERY_PICKEDUP.equalsIgnoreCase("null")) {
                    JSONObject jsonObj = null;
                    try {
                        jsonObj = new JSONObject(CUSTOMER_DELIVERY_PICKEDUP);
                        sendNotification(new Intent(), jsonObj.getString("msg"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (!BUSINESS_DELIVERY_COMPLETED.equalsIgnoreCase("null")) {
                    JSONObject jsonObj = null;
                    try {
                        jsonObj = new JSONObject(BUSINESS_DELIVERY_COMPLETED);
                        sendNotification(new Intent(), jsonObj.getString("msg"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (!CUSTOMER_DELIVERY_COMPLETED.equalsIgnoreCase("null")) {
                    JSONObject jsonObj = null;
                    try {
                        jsonObj = new JSONObject(CUSTOMER_DELIVERY_COMPLETED);
                        sendNotification(new Intent(), jsonObj.getString("msg"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (!DRIVER_ARRIVED.equalsIgnoreCase("null")) {
                    JSONObject jsonObj = null;
                    try {
                        jsonObj = new JSONObject(DRIVER_ARRIVED);
                        sendNotification(new Intent(), jsonObj.getString("msg"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (!CUSTOMER_OTHER_MSG.equalsIgnoreCase("null")) {
                    JSONObject jsonObj = null;
                    try {
                        jsonObj = new JSONObject(CUSTOMER_OTHER_MSG);
                        sendExNotification(new Intent(), jsonObj.getString("msg_title"), jsonObj.getString("msg_body"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (!BUSINESS_OTHER_MSG.equalsIgnoreCase("null")) {
                    JSONObject jsonObj = null;
                    try {
                        jsonObj = new JSONObject(BUSINESS_OTHER_MSG);
                        sendExNotification(new Intent(), jsonObj.getString("msg_title"), jsonObj.getString("msg_body"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (!REPLY_ORDER.equalsIgnoreCase("null")) {
                    JSONObject jsonObj = null;
                    try {
                        jsonObj = new JSONObject(REPLY_ORDER);
                        sendExNotification(new Intent(), jsonObj.getString("msg_title"), jsonObj.getString("msg_body"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (!REPLY_RESERVATION.equalsIgnoreCase("null")) {
                    JSONObject jsonObj = null;
                    try {
                        jsonObj = new JSONObject(REPLY_RESERVATION);
                        sendExNotification(new Intent(), jsonObj.getString("msg_title"), jsonObj.getString("msg_body"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (!NEW_ORDER_FEEDBACK.equalsIgnoreCase("null")) {
                    JSONObject jsonObj = null;
                    try {
                        jsonObj = new JSONObject(NEW_ORDER_FEEDBACK);
                        sendExNotification(new Intent(), jsonObj.getString("msg_title"), jsonObj.getString("msg_body"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (!NEW_RESERVATION_FEEDBACK.equalsIgnoreCase("null")) {
                    JSONObject jsonObj = null;
                    try {
                        jsonObj = new JSONObject(NEW_RESERVATION_FEEDBACK);
                        sendExNotification(new Intent(), jsonObj.getString("msg_title"), jsonObj.getString("msg_body"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (!REPLY_REVIEW.equalsIgnoreCase("null")) {
                    JSONObject jsonObj = null;
                    try {
                        jsonObj = new JSONObject(REPLY_REVIEW);
                        sendExNotification(new Intent(), jsonObj.getString("msg_title"), jsonObj.getString("msg_body"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
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
                    .setContentTitle("Dine Hawaii Notification")
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher))
                    .setSound(defaultSoundUri)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setContentIntent(pendingIntent).setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(messageBody));
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notificationBuilder.build());
        } catch (Exception e) {
            Log.e("Notification Ex", e.getMessage());
        }
    }

    private void sendExNotification(Intent intent, String msg_title, String msg_body) {
        try {
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.not_icon)
                    .setContentTitle(msg_title)
                    .setContentText(msg_body)
                    .setAutoCancel(true)
                    .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher))
                    .setSound(defaultSoundUri)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setContentIntent(pendingIntent)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(msg_body));

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
