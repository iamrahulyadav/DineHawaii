package com.yberry.dinehawaii.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.yberry.dinehawaii.Model.CheckBoxPositionModel;
import com.yberry.dinehawaii.R;

import java.util.ArrayList;
import java.util.List;

/*  setContentView(R.layout.activity_text_view_with_edit_text_adapter);
 */
/*public class TextViewWithEditTextAdapter extends AppCompatActivity {

   */

public class TextViewWithEditTextAdapter extends RecyclerView.Adapter<TextViewWithEditTextAdapter.MyViewHolder> {

    private List<CheckBoxPositionModel> listValue;
    private Context mContext;
    private String value;

    public TextViewWithEditTextAdapter(Context context, ArrayList<CheckBoxPositionModel> listValue) {
        this.listValue = listValue;
        this.mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_text_view_with_edit_text_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        CheckBoxPositionModel checkBoxPositionModel = listValue.get(position);
        holder.tvlistItem.setText(checkBoxPositionModel.getDish_name());
        //holder.etlistPrice.setText("555");


        //checkBoxPositionModel.setAmount("5555");
        holder.etlistPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                listValue.get(position).setAmount(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return listValue.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView tvlistItem;
        public EditText etlistPrice;
        CheckBox chkSelected;
        CheckBoxPositionModel checkBoxPositionModel;


        public MyViewHolder(View view) {
            super(view);
            tvlistItem = (TextView) view.findViewById(R.id.tvlistitem);
            etlistPrice = (EditText) view.findViewById(R.id.etlistPrice);
            chkSelected = (CheckBox) view.findViewById(R.id.chkSelected);
        }

    }

    public List<CheckBoxPositionModel> getListValue(){
        Log.e("samir",listValue.toString());
        return listValue;
    }

}