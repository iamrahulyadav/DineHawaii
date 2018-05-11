package com.yberry.dinehawaii.Bussiness.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.yberry.dinehawaii.Bussiness.model.TableChairData;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.customview.CustomButton;
import com.yberry.dinehawaii.customview.CustomEditText;
import com.yberry.dinehawaii.customview.CustomTextView;

import java.util.ArrayList;

public class ChairAdapter extends RecyclerView.Adapter<ChairAdapter.ViewHolder> {

    private Context context;
    private ArrayList<TableChairData> chairDataList;

    public ChairAdapter(Context context) {
        this.context = context;
    }

    public ChairAdapter(Context context, ArrayList<TableChairData> chairDataList) {
        this.context = context;
        this.chairDataList = chairDataList;
    }

    @Override
    public ChairAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chair_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final TableChairData model = chairDataList.get(position);

        holder.textViewDescr.setText(model.getChair_desc());
        holder.textViewChairNo.setText(String.valueOf(model.getS_no()));

        if(model.isSaved())
            holder.buttonSave.setText("SAVED");
        else
            holder.buttonSave.setText("SAVE");

        if(model.getChair_type().equalsIgnoreCase("Handicapped"))
               holder.radio_handicapped.setChecked(true);
        else
            holder.radio_normal.setChecked(true);

        holder.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selected_id= holder.radioGroup.getCheckedRadioButtonId();
                RadioButton button= itemView1.findViewById(selected_id);
                Log.e("ChairAdapter", "onClick: " + holder.textViewDescr.getText().toString());
                Intent intent = new Intent("send");
                intent.putExtra("position", position);
                intent.putExtra("description", holder.textViewDescr.getText().toString());
                intent.putExtra("type", button.getText().toString());
                intent.putExtra("saved", true);
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                model.setSaved(true);
                holder.buttonSave.setText("SAVED");
                holder.textViewDescr.setEnabled(false);
                holder.buttonSave.setEnabled(false);
                holder.radio_handicapped.setEnabled(false);
                holder.radio_normal.setEnabled(false);
                Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return chairDataList.size();
    }

    View itemView1;

    public class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView textViewChairNo;
        CustomEditText textViewDescr;
        CustomButton buttonSave;
        RadioGroup radioGroup;
        RadioButton radio_handicapped,radio_normal;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView1=itemView;
            textViewChairNo = (CustomTextView) itemView.findViewById(R.id.textViewChairNo);
            textViewDescr = (CustomEditText) itemView.findViewById(R.id.textViewDescr);
            buttonSave = (CustomButton) itemView.findViewById(R.id.buttonSave);
            radioGroup = (RadioGroup)itemView.findViewById(R.id.rgroup_type);
            radio_normal = (RadioButton)itemView.findViewById(R.id.radio_normal);
            radio_handicapped = (RadioButton)itemView.findViewById(R.id.radio_handicapped);
        }
    }
}


