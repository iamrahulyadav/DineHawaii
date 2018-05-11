package com.yberry.dinehawaii.Customer.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.customview.CustomButton;
import com.yberry.dinehawaii.customview.CustomTextView;

public class DialogActivity extends Activity {


    private ImageView popupclose;
    private CustomTextView tvTitle;
    private CustomButton btnCall;
    private String phone_no="1234567890";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        init();
        if (getIntent().hasExtra("msg"))
            tvTitle.setText(getIntent().getStringExtra("msg"));
        if (getIntent().hasExtra("phone_no"))
            phone_no = getIntent().getStringExtra("phone_no");

    }

    private void init() {
        popupclose = (ImageView) findViewById(R.id.popupclose);
        tvTitle = (CustomTextView) findViewById(R.id.tvTitle);
        btnCall = (CustomButton) findViewById(R.id.btnCall);
        popupclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!phone_no.equalsIgnoreCase("")) {
                    Intent intent2 = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone_no));
                    if (ActivityCompat.checkSelfPermission(DialogActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    startActivity(intent2);
                }
            }
        });
    }
}
