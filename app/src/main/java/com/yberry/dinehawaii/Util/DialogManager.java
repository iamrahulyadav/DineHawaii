package com.yberry.dinehawaii.Util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.util.Log;


public class DialogManager {

    public static boolean cancelable = false;
    ProgressHUD mProgressHUD;

    public void showProcessDialog(Context context, String title) {
        try {
            mProgressHUD = ProgressHUD.show(context, title, true, false, new OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    mProgressHUD.dismiss();
                }
            });
        } catch (Exception e) {
            Log.e("showProcessDialog: ", e.getMessage());
        }
    }

    public void stopProcessDialog() {
        if (mProgressHUD != null && mProgressHUD.isShowing()) mProgressHUD.dismiss();
    }
}
