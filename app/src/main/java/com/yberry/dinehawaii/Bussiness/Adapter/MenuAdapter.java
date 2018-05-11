package com.yberry.dinehawaii.Bussiness.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Model.CheckBoxPositionModel;
import com.yberry.dinehawaii.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by JAI on 2/9/2017.
 */

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> implements View.OnClickListener{
    private Context mCotnext;
    private ArrayList<CheckBoxPositionModel> list;
    public MenuAdapter(Context mCotnext, ArrayList<CheckBoxPositionModel> list) {
        this.mCotnext = mCotnext;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pricelist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final CheckBoxPositionModel model = list.get(position);
        holder.item.setText(model.getName());

        holder.price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                list.get(position).setAmount(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onClick(View view) {
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView item;
        private EditText price;

        public ViewHolder(View itemView) {
            super(itemView);
            item = (TextView) itemView.findViewById(R.id.item);
            price = (EditText) itemView.findViewById(R.id.price);
        }
    }

    private void checkEditText(){

    }

    public JsonArray getList(){
        JsonArray jsonArray = new JsonArray();
        for(int i=0; i<list.size(); i++){
            CheckBoxPositionModel model = list.get(i);
                JsonObject itemList = new JsonObject();
                itemList.addProperty("food_name", model.getName());
                itemList.addProperty("price", model.getAmount());
                jsonArray.add(itemList);
        }
        return jsonArray;
    }
}
