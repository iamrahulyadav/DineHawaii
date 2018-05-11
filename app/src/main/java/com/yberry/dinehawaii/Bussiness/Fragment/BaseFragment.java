package com.yberry.dinehawaii.Bussiness.Fragment;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.widget.Toast;

/**
 * Created by MAX on 08-Mar-17.
 */

public class BaseFragment extends Fragment {

    public ProgressDialog mProgressDialog;

    public void showProgressDialog(String title) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage(title.equalsIgnoreCase("")?"Loading": title);
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }

    public void showToast(String message){
        Toast.makeText(getActivity(), message.equalsIgnoreCase("")?"Enter your message": message, Toast.LENGTH_LONG).show();
    }
}
