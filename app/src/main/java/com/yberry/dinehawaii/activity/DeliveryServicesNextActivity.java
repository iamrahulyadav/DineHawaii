package com.yberry.dinehawaii.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yberry.dinehawaii.R;

public class DeliveryServicesNextActivity extends AppCompatActivity {
    ImageView next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_services_next);

        next = (ImageView) findViewById(R.id.imageview);
        ((TextView) findViewById(R.id.headet_text)).setText("Delivery Service-Distributor & Food Suppliers");
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1= new Intent(DeliveryServicesNextActivity.this,DeliveryFoodActivity.class);
                startActivity(i1);
            }
        });
    }
}