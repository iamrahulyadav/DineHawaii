package com.yberry.dinehawaii.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yberry.dinehawaii.Model.CheckBoxPositionModel;
import com.yberry.dinehawaii.R;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Hvantage2 on 8/9/2017.
 */

public class SearchAdapterFood  extends RecyclerView.Adapter<SearchAdapterFood.ViewHolder> {
    ArrayList<CheckBoxPositionModel> sellerModelList, filterList;

    private Context context;
    private String TAG="SellerImageAdapter";

    public SearchAdapterFood(Context context, ArrayList<CheckBoxPositionModel> sellerModelList) {
        this.context = context;
        this.sellerModelList = sellerModelList;
        this.filterList = new ArrayList<CheckBoxPositionModel>();
        this.filterList.addAll(sellerModelList);
        Log.d(TAG, "SellerImageAdapter: SellerList"+sellerModelList.size());
        Log.d(TAG, "SellerImageAdapter: FilterList"+filterList);
    }

    @Override
    public SearchAdapterFood.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_image, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final CheckBoxPositionModel sellerModel = sellerModelList.get(position);
        //new AQuery(context).id(holder.sellerImage).image(sellerModel.getSeller_image(), true, true, 100, 0);
        holder.tvSellerName.setText(sellerModel.getName());
    }


    @Override
    public int getItemCount() {
        return sellerModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView sellerImage;
        TextView tvSellerName;
        public ViewHolder(View itemView) {
            super(itemView);


            //  sellerImage = (ImageView) itemView.findViewById(R.id.imgSellerImage);
            tvSellerName = (TextView) itemView.findViewById(R.id.tvSellerName);
        }


    }

    public void filterSeller(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        sellerModelList.clear();
        if (charText.length() == 0) {
            Log.d(TAG, "Here if"+"Here if");
            sellerModelList.addAll(filterList);
        } else {
            Log.d(TAG, "Here Else"+"Else");
            Log.d(TAG, "filterSeller: SellerList"+filterList.size());
            Log.d(TAG, "filterSeller: SellerList"+sellerModelList.size());
            for (CheckBoxPositionModel sellerModel : filterList) {
                if (sellerModel.getName() != null) {
                    if ((sellerModel.getName().toLowerCase(Locale.getDefault()).contains(charText))) {
                        Log.d(TAG, "filterSeller: "+sellerModel.getName());
                        Log.d(TAG, "filterSeller: CharText"+charText);
                        sellerModelList.add(sellerModel);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }
}
