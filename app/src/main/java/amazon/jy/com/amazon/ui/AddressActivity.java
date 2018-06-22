package amazon.jy.com.amazon.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import amazon.jy.com.amazon.R;
import amazon.jy.com.amazon.core.App;
import amazon.jy.com.amazon.core.volley.VolleyApi;

public class AddressActivity extends AppCompatActivity implements View.OnClickListener {
    private Spinner mCountry;
    private Spinner mProvince;
    private EditText mTownship;
    private EditText mStreet;
    private EditText mZipCode;
    private EditText mTNumber;
    private EditText mRemark;
    private TextView mAId;
    
    SharedPreferences preferences;

    String api = "";
    String country;
    String province;
    Integer uId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        findView();
        init();
    }

    private void findView() {
        mAId = findViewById(R.id.a_id);
        mCountry = findViewById(R.id.country);
        mProvince = findViewById(R.id.province);
        mTownship = findViewById(R.id.township);
        mStreet = findViewById(R.id.street);
        mZipCode = findViewById(R.id.zip_code);
        mTNumber = findViewById(R.id.t_number);
        mRemark = findViewById(R.id.remark);
        findViewById(R.id.save).setOnClickListener(this);
    }

    private void init() {
        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        preferences = App.getApp().getPreferences();
        uId = preferences.getInt("uId", 0);

        mCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] type = getResources().getStringArray(R.array.country);

                country = type[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                country = getResources().getStringArray(R.array.country)[0];
            }
        });

        mProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] type = getResources().getStringArray(R.array.province);

                province = type[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                province = getResources().getStringArray(R.array.province)[0];
            }
        });

        String param = "?uId=" + uId;
        if (type.equals("edit")){
            api = "updateAddress";
            VolleyApi.POST(AddressActivity.this, "getAddress" + param, new Response.Listener() {
                @Override
                public void onResponse(Object response) {
                    try {
                        JSONObject json = new JSONObject(response.toString());
                        if (json.optInt("status") == 0){
                            JSONObject data = json.optJSONObject("data");
                            showAddressInfo(data);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }else if (type.equals("add")){
            api = "insertAddress";
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.save:
                String township = mTownship.getText().toString();
                String zipcode = mZipCode.getText().toString();
                String street = mStreet.getText().toString();
                String tNumber = mTNumber.getText().toString();
                String remarks = mRemark.getText().toString();

                String param = "?uId=" + uId + "&country=" + country + "&province=" + province +
                        "&zipCode=" + zipcode + "&township=" + township + "&street=" + street +
                        "&tNumber=" + tNumber + "&remarks=" + remarks;
                VolleyApi.POST(AddressActivity.this, api + param, new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        try {
                            JSONObject json = new JSONObject(response.toString());
                            if (json.optInt("status") == 0){
                                finish();
                            }else if (json.optInt("status") == 300){
                                Toast.makeText(AddressActivity.this, json.optString("msg"), Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(AddressActivity.this, "服务器异常", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;
        }
    }

    public void showAddressInfo(JSONObject data) {
        String[] countrys = getResources().getStringArray(R.array.country);
        for (int i=0;i<countrys.length;i++){
            if (countrys[i].equals(data.optString("country"))){
                mCountry.setSelection(i);
            }
        }
        String[] provinces = getResources().getStringArray(R.array.province);
        for (int i=0;i<provinces.length;i++){
            if (provinces[i].equals(data.optString("province"))){
                mProvince.setSelection(i);
            }
        }
        mTownship.setText(data.optString("township"));
        mStreet.setText(data.optString("street"));
        mZipCode.setText(data.optString("zipcode"));
        mTNumber.setText(data.optString("tNumber"));
        mRemark.setText(data.optString("remarks"));

        mAId.setText(data.optInt("aId") + "");
    }
}
