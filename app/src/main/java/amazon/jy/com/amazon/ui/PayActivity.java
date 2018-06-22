package amazon.jy.com.amazon.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import amazon.jy.com.amazon.R;
import amazon.jy.com.amazon.core.App;
import amazon.jy.com.amazon.core.volley.VolleyApi;

public class PayActivity extends AppCompatActivity implements View.OnClickListener {
    private RadioGroup mRadio;
    private EditText mInvoiceTitle;
    private Spinner mInvoiceType;

    SharedPreferences preferences;

    String invoiceType;
    String pay_type;
    String param;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        findView();
        init();
    }

    private void findView() {
        findViewById(R.id.ensure_pay).setOnClickListener(this);

        mInvoiceTitle = findViewById(R.id.invoice_title);
        mInvoiceType = findViewById(R.id.invoice_type);
        mRadio = findViewById(R.id.radio);
    }

    private void init() {
        Intent intent = getIntent();
        param = intent.getStringExtra("param");

        preferences = App.getApp().getPreferences();
        mInvoiceType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] type = getResources().getStringArray(R.array.invoice_type);

                invoiceType = type[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                invoiceType = getResources().getStringArray(R.array.invoice_type)[0];
            }
        });
        mRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = PayActivity.this.findViewById(mRadio.getCheckedRadioButtonId());
                pay_type = radioButton.getText().toString();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ensure_pay:
                String invoiceTitle = mInvoiceTitle.getText().toString();
                param += "&uPay=" + pay_type + "&invoiceTitle=" + invoiceTitle + "&invoiceType=" +
                        invoiceType + "&deliverFee=5";
                System.out.println("payOrder:" + param);
                VolleyApi.POST(PayActivity.this, "insertOrder" + param, new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        try {
                            JSONObject json = new JSONObject(response.toString());
                            Intent payResult = new Intent(PayActivity.this, PayResultActivity.class);
                            if (json.optInt("status") == 0){
                                preferences.edit().putInt("carts", 0).commit();
                                finish();
                            }else {
                                Toast.makeText(PayActivity.this, json.optString("msg"), Toast.LENGTH_SHORT).show();
                            }
                            startActivity(payResult);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            break;
        }
    }
}
