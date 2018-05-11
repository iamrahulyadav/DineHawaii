package com.yberry.dinehawaii.Util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.util.Log;

import com.yberry.dinehawaii.R;


public class DialogManager {

    ProgressHUD mProgressHUD;
    public static boolean cancelable = false;

    @SuppressWarnings("deprecation")
    public void showAlertDialog(final Context context, String title, String message,
                                DialogInterface.OnClickListener ok, DialogInterface.OnClickListener cancel) {
        //final Boolean localStatus = status;
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
//		if(status != null)
//			alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);
        alertDialog.setButton("OK", ok);
        alertDialog.setButton("CANCLE", cancel);
        try {
            alertDialog.show();
        } catch (Exception e) {

        }
    }

    @SuppressWarnings("deprecation")
    public void showAlertDialog(final Context context, String title, String message,
                                DialogInterface.OnClickListener ok) {
        //final Boolean localStatus = status;
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setIcon(R.drawable.ic_launcher);
        alertDialog.setCancelable(cancelable);
//		if(status != null)
//			alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);
        alertDialog.setButton("OK", ok);
        try {
            alertDialog.show();
        } catch (Exception e) {

        }
    }

    public void showProcessDialog(Context context, String title) {
        try {
            mProgressHUD = ProgressHUD.show(context, title, true, false, new OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    mProgressHUD.dismiss();
                }
            });
        } catch (Exception e) {
            Log.e( "showProcessDialog: ",e.getMessage());
        }
    }

    public void stopProcessDialog() {
        if (mProgressHUD != null && mProgressHUD.isShowing()) mProgressHUD.dismiss();
    }
}
