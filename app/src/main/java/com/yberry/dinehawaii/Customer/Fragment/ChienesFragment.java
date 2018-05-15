package com.yberry.dinehawaii.Customer.Fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yberry.dinehawaii.Customer.Activity.CartActivity;
import com.yberry.dinehawaii.Customer.Adapter.ChienesAdapter;
import com.yberry.dinehawaii.Model.MenuDetail;
import com.yberry.dinehawaii.Model.OrderItemsDetailsModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.Util.AppPreferences;
import com.yberry.dinehawaii.customview.CustomTextView;
import com.yberry.dinehawaii.database.DatabaseHandler;

import java.util.ArrayList;

public class ChienesFragment extends Fragment {
    ArrayList<MenuDetail> menuDetails = new ArrayList<>();
    private View rootView;
    private Context mContext;
    private ArrayList<MenuDetail> arrayList;
    private LinearLayoutManager mLayoutManager;
    private ChienesAdapter adapter;
    private ArrayList<String> list;
    private CardView floatingActionButton;
    private CustomTextView tvCountBadge;

    public ChienesFragment() {
    }


    @SuppressLint("ValidFragment")
    public ChienesFragment(Context mContext, ArrayList<MenuDetail> arrayList) {
        this.mContext = mContext;
        this.arrayList = arrayList;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_chienes, container, false);
        mContext = getActivity();
        initCompnent();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(new CounterReciever(), new IntentFilter("update_cart_count"));
        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void initCompnent() {
        menuDetails.clear();
        //menuDetails.add(arrayList);
        adapter = new ChienesAdapter(mContext, arrayList);
        RecyclerView mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new ChienesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(OrderItemsDetailsModel listItem) {
            }
        });
        adapter.notifyDataSetChanged();

        floatingActionButton = (CardView) rootView.findViewById(R.id.cardViewCart);
        tvCountBadge = (CustomTextView) rootView.findViewById(R.id.tvCountBadge);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CartActivity.class);
                Log.d("itemsDetailsModels 222", adapter.getCartItems().size() + "");
                intent.putExtra("array_model", adapter.getCartItems());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getCounterData(mContext);
    }

    private void getCounterData(Context context) {
        if (new DatabaseHandler(context).hasCartData()) {
            ArrayList<OrderItemsDetailsModel> cartItems = new DatabaseHandler(context).getCartItems(AppPreferences.getBusiID(context));  //database data
            Log.d("cartItems", String.valueOf(cartItems.size()));
            tvCountBadge.setText(String.valueOf(cartItems.size()));
        } else {
            tvCountBadge.setText("0");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    class CounterReciever extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("CounterReciever", "onReceive:");
            getCounterData(context);
        }
    }

}
