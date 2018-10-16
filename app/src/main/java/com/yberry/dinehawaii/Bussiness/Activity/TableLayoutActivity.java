package com.yberry.dinehawaii.Bussiness.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yberry.dinehawaii.Bussiness.Adapter.TableLayoutGridAdapter;
import com.yberry.dinehawaii.Bussiness.model.TableLayoutData;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.Util.GridSpacingItemDecoration;
import com.yberry.dinehawaii.Util.RecyclerItemClickListener;

import java.util.ArrayList;

public class TableLayoutActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "TableLayoutActivity";
    private ImageView back;
    private int spacing = 10, spanCount = 4;
    private boolean includeEdge = true;
    private TableLayoutGridAdapter adapter;
    private RecyclerView recylcer_view;
    private ArrayList<TableLayoutData> list;
    private TableLayoutActivity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_layout);
        context = this;
        list = new ArrayList<>();
        setToolbar();
        setRecyclerView();
        setStaticData();
    }

    private void setStaticData() {
        list.add(new Gson().fromJson("{\n" +
                "\t\"table_id\": \"1\",\n" +
                "\t\"reservation_id\": \"1\",\n" +
                "\t\"table_size\": \"5\",\n" +
                "\t\"table_name\": \"T1\",\n" +
                "\t\"seating_time\": \"60 min\",\n" +
                "\t\"status\": \"Booked\"\n" +
                "}", TableLayoutData.class));
        list.add(new Gson().fromJson("{\n" +
                "\t\"table_id\": \"2\",\n" +
                "\t\"reservation_id\": \"1\",\n" +
                "\t\"table_size\": \"5\",\n" +
                "\t\"table_name\": \"T2\",\n" +
                "\t\"seating_time\": \"60 min\",\n" +
                "\t\"status\": \"Booked\"\n" +
                "}", TableLayoutData.class));
        list.add(new Gson().fromJson("{\n" +
                "\t\"table_id\": \"3\",\n" +
                "\t\"reservation_id\": \"1\",\n" +
                "\t\"table_size\": \"5\",\n" +
                "\t\"table_name\": \"T3\",\n" +
                "\t\"seating_time\": \"60 min\",\n" +
                "\t\"status\": \"Booked\"\n" +
                "}", TableLayoutData.class));
        list.add(new Gson().fromJson("{\n" +
                "\t\"table_id\": \"4\",\n" +
                "\t\"reservation_id\": \"0\",\n" +
                "\t\"table_size\": \"5\",\n" +
                "\t\"table_name\": \"T4\",\n" +
                "\t\"seating_time\": \"60 min\",\n" +
                "\t\"status\": \"Free\"\n" +
                "}", TableLayoutData.class));
        list.add(new Gson().fromJson("{\n" +
                "\t\"table_id\": \"5\",\n" +
                "\t\"reservation_id\": \"0\",\n" +
                "\t\"table_size\": \"5\",\n" +
                "\t\"table_name\": \"T5\",\n" +
                "\t\"seating_time\": \"60 min\",\n" +
                "\t\"status\": \"Free\"\n" +
                "}", TableLayoutData.class));
        list.add(new Gson().fromJson("{\n" +
                "\t\"table_id\": \"4\",\n" +
                "\t\"reservation_id\": \"0\",\n" +
                "\t\"table_size\": \"5\",\n" +
                "\t\"table_name\": \"T4\",\n" +
                "\t\"seating_time\": \"60 min\",\n" +
                "\t\"status\": \"Free\"\n" +
                "}", TableLayoutData.class));
        list.add(new Gson().fromJson("{\n" +
                "\t\"table_id\": \"5\",\n" +
                "\t\"reservation_id\": \"0\",\n" +
                "\t\"table_size\": \"5\",\n" +
                "\t\"table_name\": \"T5\",\n" +
                "\t\"seating_time\": \"60 min\",\n" +
                "\t\"status\": \"Free\"\n" +
                "}", TableLayoutData.class));
        list.add(new Gson().fromJson("{\n" +
                "\t\"table_id\": \"3\",\n" +
                "\t\"reservation_id\": \"0\",\n" +
                "\t\"table_size\": \"5\",\n" +
                "\t\"table_name\": \"T3\",\n" +
                "\t\"seating_time\": \"60 min\",\n" +
                "\t\"status\": \"Booked\"\n" +
                "}", TableLayoutData.class));
        list.add(new Gson().fromJson("{\n" +
                "\t\"table_id\": \"4\",\n" +
                "\t\"reservation_id\": \"0\",\n" +
                "\t\"table_size\": \"5\",\n" +
                "\t\"table_name\": \"T4\",\n" +
                "\t\"seating_time\": \"60 min\",\n" +
                "\t\"status\": \"Free\"\n" +
                "}", TableLayoutData.class));
        list.add(new Gson().fromJson("{\n" +
                "\t\"table_id\": \"3\",\n" +
                "\t\"reservation_id\": \"1\",\n" +
                "\t\"table_size\": \"5\",\n" +
                "\t\"table_name\": \"T3\",\n" +
                "\t\"seating_time\": \"60 min\",\n" +
                "\t\"status\": \"Booked\"\n" +
                "}", TableLayoutData.class));
        list.add(new Gson().fromJson("{\n" +
                "\t\"table_id\": \"4\",\n" +
                "\t\"reservation_id\": \"0\",\n" +
                "\t\"table_size\": \"5\",\n" +
                "\t\"table_name\": \"T4\",\n" +
                "\t\"seating_time\": \"60 min\",\n" +
                "\t\"status\": \"Free\"\n" +
                "}", TableLayoutData.class));
    }

    private void setRecyclerView() {
        recylcer_view = (RecyclerView) findViewById(R.id.recycler_view);
        recylcer_view.setLayoutManager(new GridLayoutManager(context, spanCount));
        recylcer_view.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        recylcer_view.addOnItemTouchListener(new RecyclerItemClickListener(context, recylcer_view, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent intent;
                if (list.get(position).getStatus().equalsIgnoreCase("Free")) {
                    intent = new Intent(getApplicationContext(), BusReservationActivity.class);
                    intent.putExtra("reservation_id", list.get(position).getReservationId());
                    intent.setAction("NOSHOW");
                    startActivity(intent);
                } else {
                    intent = new Intent(getApplicationContext(), ReservationDetailsActivity.class);
                    intent.putExtra("reservation_id", list.get(position).getReservationId());
                    intent.setAction("NOSHOW");
                    startActivity(intent);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        }));
        adapter = new TableLayoutGridAdapter(context, context, list);
        recylcer_view.setAdapter(adapter);
    }

    private void setToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((TextView) findViewById(R.id.headet_text)).setText("Tables Layout");
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    @Override
    public void onClick(View v) {
    }


}
