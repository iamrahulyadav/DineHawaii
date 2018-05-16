package com.yberry.dinehawaii.Bussiness.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.yberry.dinehawaii.Model.CheckBoxPositionModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.Bussiness.Adapter.CheckBoxMultipleServiceAdapter;

import java.util.ArrayList;
import java.util.List;

public class MultipleServiceType_52B extends AppCompatActivity {
    public static List<CheckBoxPositionModel> servicelist = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    Context context;
    CheckBoxMultipleServiceAdapter ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_service_type_52_b);
        init();


    }
    private void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Select Service");

        context = this;
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewService);
        recyclerView.setHasFixedSize(true);
        recyclerViewlayoutManager = new LinearLayoutManager(this);
        //recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setLayoutManager(recyclerViewlayoutManager);
        intentDataValue();
        Log.d("IntentVslue",servicelist.toString());
        ad = new CheckBoxMultipleServiceAdapter(getApplicationContext(),(ArrayList<CheckBoxPositionModel>) servicelist, new CheckBoxMultipleServiceAdapter.setOnClickListener() {
            @Override
            public void onItemClick(CheckBoxPositionModel checkBoxPositionModel) {

            }
        });
        recyclerView.setAdapter(ad);
        ad.notifyDataSetChanged();

    }

    private void intentDataValue() {
        if(getIntent().getParcelableArrayListExtra("ServiceType")!=null){
            servicelist=getIntent().getParcelableArrayListExtra("ServiceType");
            Log.d("Parsable",servicelist.toString());

            /*    recyclerView.setAdapter(ad);
                ad.notifyDataSetChanged();
*/
//                CheckBoxMultipleServiceAdapter ad = new CheckBoxMultipleServiceAdapter(getApplicationContext(), (ArrayList<CheckBoxPositionModel>) servicelist, new CheckBoxMultipleServiceAdapter.setOnClickListener() {
//                    @Override
//                    public void onItemClick(CheckBoxPositionModel checkBoxPositionModel) {
//
//                    }
//                });

        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.right_icon_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_filter) {
            ArrayList<CheckBoxPositionModel> checkBoxOptionList = ad.getSelectedItem();
            Intent intent = new Intent();
            intent.putParcelableArrayListExtra("servicetype",checkBoxOptionList);
            setResult(-1,intent);
            finish();
            Log.d("ListValue",checkBoxOptionList.toString());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
