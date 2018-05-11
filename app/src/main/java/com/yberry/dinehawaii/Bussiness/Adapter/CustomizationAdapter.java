package com.yberry.dinehawaii.Bussiness.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yberry.dinehawaii.Model.CustomerModel;
import com.yberry.dinehawaii.R;

import java.util.List;

/**
 * Created by Hvantage2 on 7/17/2017.
 */

public class CustomizationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int HEADER = 0;
    private static final int CHILD = 1;
    private Context context;
    private List<CustomerModel> customOrderList;


    public CustomizationAdapter(Context context, List<CustomerModel> customOrderList) {

        this.context = context;
        this.customOrderList = customOrderList;

    }

    @Override
    public int getItemViewType(int position) {
        return customOrderList.get(position).getFood_name() == null ? CHILD : HEADER;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType)
        {
            case CHILD:
                View v1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.customization_view, parent, false);
                ViewHolder viewHolder1 = new ViewHolder(v1);
                return viewHolder1;
            case HEADER:
                View v2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.customiz_list_header, parent, false);
                ViewHolderHeader viewHolder2 = new ViewHolderHeader(v2);
                return viewHolder2;
        }


        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;
            final CustomerModel model = customOrderList.get(position);
            viewHolder.chkCustomitem.setText(model.getOrder_ItemCustomization());
        }
        if (holder instanceof ViewHolderHeader) {
            ViewHolderHeader viewHolder = (ViewHolderHeader) holder;
            final CustomerModel model = customOrderList.get(position);

            viewHolder.mHeader.setText(model.getFood_name());
        }


    }


    @Override
    public int getItemCount() {
        return customOrderList.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView chkCustomitem;


        public ViewHolder(View itemView) {
            super(itemView);
            chkCustomitem = (TextView) itemView.findViewById(R.id.orderCustomization);

        }
    }
    public class ViewHolderHeader extends RecyclerView.ViewHolder {
        TextView mHeader;


        public ViewHolderHeader(View itemView) {
            super(itemView);
            mHeader = (TextView) itemView.findViewById(R.id.mHeader);

        }
    }

}
