package amazon.jy.com.amazon.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import amazon.jy.com.amazon.R;
import amazon.jy.com.amazon.core.App;
import amazon.jy.com.amazon.core.volley.VolleyApi;

public class SettlementActivity extends Activity implements View.OnClickListener {
    private TextView mUserName;
    private TextView mAddress;
    private TextView mAddressDetal;
    private TextView mZipCode;
    private TextView mPhone;
    private TextView mAddressId;
    SharedPreferences preferences;
    private int aId;
    private String zipcode;
    private String province;
    private String country;
    private String township;
    private String remarks;
    String param;
    Integer uId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settlement);
        findView();
        init();
    }
    private void findView() {
        mUserName = findViewById(R.id.user_name);
        mAddress = findViewById(R.id.address);
        mAddressDetal = findViewById(R.id.address_detail);
        mZipCode = findViewById(R.id.zip_code);
        mPhone = findViewById(R.id.phone);
        mAddressId = findViewById(R.id.address_id);
        findViewById(R.id.to_pay).setOnClickListener(this);
        findViewById(R.id.edit_address).setOnClickListener(this);
        findViewById(R.id.delete_address).setOnClickListener(this);
    }
    private void init() {
        Intent intent = getIntent();
        param = intent.getStringExtra("param");
        preferences = App.getApp().getPreferences();
        uId = preferences.getInt("uId", 0);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.to_pay:
                Intent pay = new Intent(SettlementActivity.this,PayActivity.class);
                param += "&aId=" + aId;
                pay.putExtra("param", param);
                startActivity(pay);
                break;
            case R.id.edit_address:
                Intent edit_address = new Intent(SettlementActivity.this, AddressActivity.class);
                edit_address.putExtra("type","edit");
                startActivity(edit_address);
                break;
            case R.id.delete_address:
                deleteAddress();
                break;
        }
    }
    //删除地址
    private void deleteAddress() {
        String param = "?aId=" + aId;
        VolleyApi.POST(SettlementActivity.this, "deleteAddress" + param, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject json = new JSONObject(response.toString());
                    if (json.optInt("status") == 0){
                        Intent add_address = new Intent(SettlementActivity.this, AddressActivity.class);
                        add_address.putExtra("type","add");
                        startActivity(add_address);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        getAddress();
    }
    public void getAddress() {
        //获取用户地址
        String para = "?uId=" + uId;
        VolleyApi.POST(this, "getAddress" + para, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject json = new JSONObject(response.toString());
                    if (json.optInt("status") == 0){
                        JSONObject data = json.optJSONObject("data");
                        aId = data.optInt("aId");
                        zipcode = data.optString("zipcode");
                        province = data.optString("province");
                        country = data.optString("country");
                        township = data.optString("township");
                        remarks = data.optString("remarks");
                        mUserName.setText(preferences.getString("uName","-.-"));
                        mAddress.setText(country + "  " + province + "  " + township);
                        mAddressDetal.setText(township + remarks);
                        mZipCode.setText(zipcode);
                        mPhone.setText(preferences.getString("uPhone","-.-"));
                        mAddressId.setText(aId + "");
                    }else if (json.optInt("status") == 400){  //没有地址
                        Intent address = new Intent(SettlementActivity.this, AddressActivity.class);
                        startActivity(address);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
