package com.yberry.dinehawaii.Bussiness.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Model.CheckBoxPositionModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.adapter.CheckBoxOptionAdapter;
import com.yberry.dinehawaii.adapter.SearchAdapterFood;
import com.yberry.dinehawaii.customview.CustomTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodTypeActivity extends AppCompatActivity {
    private static final String TAG = "Food_Type_Activity";
    public static List<CheckBoxPositionModel> foodlist = new ArrayList<>();
    public static List<String> listAdd;
    RecyclerView recyclerView;
    CustomTextView titleText;
    private ListView mListView;
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    CheckBoxOptionAdapter ad;
    ArrayAdapter<CheckBoxPositionModel> adapter;
    ListView listview;
    private SearchAdapterFood imageadapter;
    private SearchView msearch;
    RelativeLayout layoutLinear;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_type);

        setToolbar();
        init();
        //backButtonListener();

        if (getIntent().getAction().equalsIgnoreCase("FoodFragment45")) {

            if (getIntent().getStringExtra("Foodtype") != null) {

                String data = getIntent().getStringExtra("Foodtype");
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        CheckBoxPositionModel model = new CheckBoxPositionModel();
                        JSONObject object = jsonArray.getJSONObject(i);
                        model.setName(object.getString("food_name"));
                        model.setId(object.getString("id"));
                        model.setChckStatus(false);
                        model.setEdit_status("");
                        foodlist.add(model);
                        Log.d("FoodService", String.valueOf(foodlist.size()));
                        for (int k = 0; k < foodlist.size(); k++) {
                            System.out.println("VAlue :" + foodlist.get(k).getName());
                        }
                    }

                    CheckBoxOptionAdapter ad = new CheckBoxOptionAdapter(getApplicationContext(), (ArrayList<CheckBoxPositionModel>) foodlist, new CheckBoxOptionAdapter.setOnClickListener() {
                        @Override
                        public void onItemClick(CheckBoxPositionModel checkBoxPositionModel) {

                        }
                    });

                    recyclerView.setAdapter(ad);
                    ad.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } else if (getIntent().getAction().equalsIgnoreCase("SecurityFragment")) {

            String jobTitle_id = "";
            if (!getIntent().getExtras().getString("jobTitle_id").equalsIgnoreCase("")) {
                jobTitle_id = getIntent().getExtras().getString("jobTitle_id");
            }
            Log.v(TAG, " Job_title_id :- " + jobTitle_id);
            layoutLinear.setVisibility(View.GONE);
            getAllJobDuties(jobTitle_id);
        }else if (getIntent().getAction().equalsIgnoreCase("serviceType")){
            String data = getIntent().getStringExtra("service");
            try {
                JSONObject jsonObject = new JSONObject(data);
                JSONArray jsonArray = jsonObject.getJSONArray("result");

                for (int i = 0; i < jsonArray.length(); i++) {

                    CheckBoxPositionModel model = new CheckBoxPositionModel();
                    JSONObject object = jsonArray.getJSONObject(i);
                    model.setName(object.getString("service_name"));
                    model.setId(object.getString("id"));
                    model.setChckStatus(false);
                    model.setEdit_status("");
                    foodlist.add(model);
                    Log.d("FoodService", String.valueOf(foodlist.size()));
                    for (int k = 0; k < foodlist.size(); k++) {
                        System.out.println("VAlue :" + foodlist.get(k).getName());
                    }
                }

                CheckBoxOptionAdapter ad = new CheckBoxOptionAdapter(getApplicationContext(), (ArrayList<CheckBoxPositionModel>) foodlist, new CheckBoxOptionAdapter.setOnClickListener() {
                    @Override
                    public void onItemClick(CheckBoxPositionModel checkBoxPositionModel) {

                    }
                });

                recyclerView.setAdapter(ad);
                ad.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    /*private void backButtonListener() {
        ImageView backImageView = (ImageView) findViewById(R.id.back);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }*/

    private void init() {
        layoutLinear = (RelativeLayout) findViewById(R.id.layoutLinear);
        titleText = (CustomTextView) findViewById(R.id.titleType);
        if (getIntent().getAction().equalsIgnoreCase("SecurityFragment"))
            titleText.setText("Select Job Duties");
        else if (getIntent().getAction().equalsIgnoreCase("FoodFragment45"))
            titleText.setText("Select Food Type");
        else if (getIntent().getAction().equalsIgnoreCase("serviceType"))
            titleText.setText("Select Service Type");
        else
            ((TextView) findViewById(R.id.headet_text)).setText("Select");
        for (CheckBoxPositionModel model : foodlist) {
            String name, id;
            name = model.getFood_name();
            id = model.getId();
            Log.v(TAG, "Id :- " + id + "\n Name :- " + name);
        }

        context = this;
        foodlist = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerViewlayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(FoodTypeActivity.this));
        recyclerView.setLayoutManager(recyclerViewlayoutManager);
        ad = new CheckBoxOptionAdapter(getApplicationContext(), (ArrayList<CheckBoxPositionModel>) foodlist, new CheckBoxOptionAdapter.setOnClickListener() {
            @Override
            public void onItemClick(CheckBoxPositionModel checkBoxPositionModel) {

            }
        });
        recyclerView.setAdapter(ad);
    }

    private void setToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (getIntent().getAction().equalsIgnoreCase("SecurityFragment"))
            ((TextView) findViewById(R.id.headet_text)).setText("Select Job Duties");
        else if (getIntent().getAction().equalsIgnoreCase("FoodFragment45"))
            ((TextView) findViewById(R.id.headet_text)).setText("Select Food Type");
        else if (getIntent().getAction().equalsIgnoreCase("serviceType"))
            ((TextView) findViewById(R.id.headet_text)).setText("Select Service Type");
        else
            ((TextView) findViewById(R.id.headet_text)).setText("Select");

        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
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

        if (id == android.R.id.home) {
            finish();
        }

        if (id == R.id.action_filter) {

            if (getIntent().getAction().equalsIgnoreCase("FoodFragment45")) {
                ArrayList<CheckBoxPositionModel> checkBoxOptionList = ad.getSelectedItem();
                Intent intent = new Intent();
                intent.putParcelableArrayListExtra("foodtype", checkBoxOptionList);
                setResult(-1, intent);
                finish();
                Log.d(TAG, "ListValue :- " + checkBoxOptionList.toString());
            } else if (getIntent().getAction().equalsIgnoreCase("serviceType")) {
                ArrayList<CheckBoxPositionModel> checkBoxOptionList = ad.getSelectedItem();
                Intent intent = new Intent();
                intent.putParcelableArrayListExtra("serviceType", checkBoxOptionList);
                setResult(-1, intent);
                finish();
                Log.d(TAG, "ListValue :- " + checkBoxOptionList.toString());
            } else if (getIntent().getAction().equalsIgnoreCase("SecurityFragment")) {

                ArrayList<CheckBoxPositionModel> checkBoxOptionList = ad.getSelectedItem();
                ArrayList<String> idsList = ad.getSelectedItemIds();
                Log.v(TAG, "Selected Job IDs :- " + idsList.toString());

                String jobIds = idsList.toString().replace("[", "").replace("]", "");
                Log.v(TAG, "Selected Job IDs :- " + jobIds);

                Intent intent = new Intent();
                intent.putParcelableArrayListExtra("all_job_duty", checkBoxOptionList);
                intent.putExtra("job_id", jobIds);
                setResult(-1, intent);
                finish();
                Log.d(TAG, "ListValue :- " + checkBoxOptionList.toString());

            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getAllJobDuties(String jobTitle_id) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", AppConstants.GENERALAPI.GETALLDUTIES);
//        jsonObject.addProperty("jobTitle_id", jobTitle_id);
        Log.e(TAG, "Json Request :- " + jsonObject.toString());
        getAllJobDutiesTask(jsonObject);
    }

    private void getAllJobDutiesTask(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(FoodTypeActivity.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });

        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.requestBusinessUserGeneralurl(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "onResponse" + response.body().toString());
                String s = response.body().toString();

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    for (int i = 0; i < jsonArray.length(); i++) {

                        CheckBoxPositionModel model = new CheckBoxPositionModel();
                        JSONObject object = jsonArray.getJSONObject(i);
                        model.setId(object.getString("duty_id"));
                        model.setName(object.getString("duty"));
                        model.setChckStatus(false);
                        foodlist.add(model);
                        Log.d(TAG, "Job Titles " + String.valueOf(foodlist.size()));
                    }

                    CheckBoxOptionAdapter adapter = new CheckBoxOptionAdapter(getApplicationContext(), (ArrayList<CheckBoxPositionModel>) foodlist, new CheckBoxOptionAdapter.setOnClickListener() {
                        @Override
                        public void onItemClick(CheckBoxPositionModel checkBoxPositionModel) {

                        }
                    });

                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "Error On failure :- " + Log.getStackTraceString(t));
                progressHD.dismiss();
            }
        });
    }
}





























































































/*
package com.yberry.dinehawaii.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.yberry.dinehawaii.Model.CheckBoxPositionModel;
import com.yberry.dinehawaii.R;
import com.yberry.dinehawaii.RetrofitClasses.ApiClient;
import com.yberry.dinehawaii.RetrofitClasses.MyApiEndpointInterface;
import com.yberry.dinehawaii.Util.AppConstants;
import com.yberry.dinehawaii.Util.ProgressHUD;
import com.yberry.dinehawaii.adapter.CheckBoxOptionAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodTypeActivity extends AppCompatActivity {
    private static final String TAG = "Food_Type_Activity";
    public static List<CheckBoxPositionModel> foodlist = new ArrayList<>();
    RecyclerView recyclerView;
    LinearLayout layoutLinear;
    ArrayList<String> nameList;
    ArrayAdapter<String> adapter;
   // SearchAdapter adapter;
    EditText inputSearch;
    ArrayList<HashMap<String, String>> productList;
    String autoEdit;
    CheckBox checkBox;
    private Context context;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_type);
        setToolbar();
        init();
        search();
        backButtonListener();

        if (getIntent().getAction().equalsIgnoreCase("FoodFragment45")) {

            if (getIntent().getStringExtra("Foodtype") != null) {

                String data = getIntent().getStringExtra("Foodtype");
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    nameList = new ArrayList<>();
                    CheckBoxPositionModel model = null;
                    for (int i = 0; i < jsonArray.length(); i++) {

                        model = new CheckBoxPositionModel();
                        JSONObject object = jsonArray.getJSONObject(i);
                        String fname = object.getString("food_name");
                        model.setName(object.getString("food_name"));
                        model.setId(object.getString("id"));
                        model.setChckStatus(false);
                        foodlist.add(model);
                        nameList.add(fname);
                        model.setFoodNameList(nameList);
                        Log.d("food420", String.valueOf(model.getFoodNameList()));
                        Log.d("Fname", fname);
                        Log.d("Fname1", String.valueOf(nameList));
                        Log.d("Fname02", nameList.toString());
                        Log.d("FoodService", String.valueOf(foodlist.size()));
                    }
                     adapter = new ArrayAdapter<String>(FoodTypeActivity.this, R.layout.list_item_search, R.id.tab, nameList);
                    //adapter = new SearchAdapter(FoodTypeActivity.this, nameList);
                    lv.setAdapter(adapter);
                    adapter.notifyDataSetChanged();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } else if (getIntent().getAction().equalsIgnoreCase("SecurityFragment")) {

            String jobTitle_id = "";
            if (!getIntent().getExtras().getString("jobTitle_id").equalsIgnoreCase("")) {
                jobTitle_id = getIntent().getExtras().getString("jobTitle_id");
            }
            Log.v(TAG, " Job_title_id :- " + jobTitle_id);
            layoutLinear.setVisibility(View.GONE);
            getAllJobDuties(jobTitle_id);
        }
    }

    private void search() {
        lv = (ListView) findViewById(R.id.list_view);

        inputSearch = (EditText) findViewById(R.id.inputSearch);
        CheckBox chk = (CheckBox) findViewById(R.id.tab);
        final CheckBoxPositionModel optionData = new CheckBoxPositionModel();

        chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                optionData.setChckStatus(isChecked);
                if(isChecked) {
                    Toast.makeText(context, "checked", Toast.LENGTH_SHORT).show();
                   */
/* listValue.add(optionData);
                    Log.d("DataList", listValue.toString());*//*

                }
                else
                {

                    Toast.makeText(context, "unchecked", Toast.LENGTH_SHORT).show();
                */
/*    int pos =listValue.indexOf(optionData);
                    listValue.remove(pos);
                    Log.d("DataListReomve", listValue.toString());*//*

                }
            }
        });
        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text

                FoodTypeActivity.this.adapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {

                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });
    }


    private void backButtonListener() {
        ImageView backImageView = (ImageView) findViewById(R.id.back);

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void init() {


        layoutLinear = (LinearLayout) findViewById(R.id.layoutLinear);
        for (CheckBoxPositionModel model : foodlist) {

            String name, id;
            name = model.getFood_name();
            id = model.getId();
            Log.v(TAG, "Id :- " + id + "\n Name :- " + name);
        }

        context = this;
        foodlist = new ArrayList<>();

    }

    private void setToolbar() {

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if (getIntent().getAction().equalsIgnoreCase("SecurityFragment"))
            ((TextView) findViewById(R.id.headet_text)).setText("Select Job Duties");
        else if (getIntent().getAction().equalsIgnoreCase("FoodFragment45"))
            ((TextView) findViewById(R.id.headet_text)).setText("Food Type");
        // mToolbar.setNavigationIcon(R.mipmap.back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
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

        if (id == android.R.id.home) {
            finish();
        }

        if (id == R.id.action_filter) {

          */
/*  if (getIntent().getAction().equalsIgnoreCase("FoodFragment45")) {
                ArrayList<CheckBoxPositionModel> checkBoxOptionList = adapter. ;
                Intent intent = new Intent();
                intent.putParcelableArrayListExtra("foodtype", checkBoxOptionList);
                setResult(-1, intent);
                finish();
                Log.d(TAG, "ListValue :- " + checkBoxOptionList.toString());
            } else if (getIntent().getAction().equalsIgnoreCase("SecurityFragment")) {

                ArrayList<CheckBoxPositionModel> checkBoxOptionList = ad.getSelectedItem();
                ArrayList<String> idsList = ad.getSelectedItemIds();
                Log.v(TAG, "Selected Job IDs :- " + idsList.toString());

                String jobIds = idsList.toString().replace("[", "").replace("]", "");
                Log.v(TAG, "Selected Job IDs :- " + jobIds);

                Intent intent = new Intent();
                intent.putParcelableArrayListExtra("all_job_duty", checkBoxOptionList);
                intent.putExtra("job_id", jobIds);
                setResult(-1, intent);
                finish();
                Log.d(TAG, "ListValue :- " + checkBoxOptionList.toString());

            }*//*

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getAllJobDuties(String jobTitle_id) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("method", AppConstants.GENERALAPI.GETALLDUTIES);
//        jsonObject.addProperty("jobTitle_id", jobTitle_id);
        Log.e(TAG, "Json Request :- " + jsonObject.toString());
        getAllJobDutiesTask(jsonObject);
    }

    private void getAllJobDutiesTask(JsonObject jsonObject) {
        final ProgressHUD progressHD = ProgressHUD.show(FoodTypeActivity.this, "Please wait...", true, false, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
            }
        });

        MyApiEndpointInterface apiService =
                ApiClient.getClient().create(MyApiEndpointInterface.class);
        Call<JsonObject> call = apiService.requestBusinessUserGeneralurl(jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e(TAG, "onResponse" + response.body().toString());
                String s = response.body().toString();

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    for (int i = 0; i < jsonArray.length(); i++) {

                        CheckBoxPositionModel model = new CheckBoxPositionModel();
                        JSONObject object = jsonArray.getJSONObject(i);
                        model.setId(object.getString("duty_id"));
                        model.setName(object.getString("duty"));
                        model.setChckStatus(false);
                        foodlist.add(model);
                        Log.d(TAG, "Job Titles " + String.valueOf(foodlist.size()));
                    }

                    CheckBoxOptionAdapter adapter = new CheckBoxOptionAdapter(getApplicationContext(), (ArrayList<CheckBoxPositionModel>) foodlist, new CheckBoxOptionAdapter.setOnClickListener() {
                        @Override
                        public void onItemClick(CheckBoxPositionModel checkBoxPositionModel) {

                        }
                    });

                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressHD.dismiss();
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "Error On failure :- " + Log.getStackTraceString(t));
                progressHD.dismiss();
            }
        });
    }


    class SearchAdapter extends ArrayAdapter implements CompoundButton.OnCheckedChangeListener  {

        ArrayList<CheckBoxPositionModel> tableList = new ArrayList<CheckBoxPositionModel>();
        Context context;


        SearchAdapter(Context context, ArrayList<String> tableList) {
            super(context, R.layout.list_item_search, tableList);
            this.context = context;
            // this.tableList = tableList;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View v = inflater.inflate(R.layout.list_item_search, parent, false);
            CheckBox cb = (CheckBox) FoodTypeActivity.this.findViewById(R.id.tab);
            cb.setOnCheckedChangeListener(this);
//            CheckBoxPositionModel table = tableList.get(position);
            // textViewTableName.setText(table.getTable_name());

            return v;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(buttonView.getId()==R.id.tab){
                if(isChecked) {

                }
                else
                {
                }
            }
        }

    }

}
*/
