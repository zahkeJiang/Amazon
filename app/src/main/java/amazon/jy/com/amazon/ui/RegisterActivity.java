package amazon.jy.com.amazon.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import amazon.jy.com.amazon.R;
import amazon.jy.com.amazon.core.Global;
import amazon.jy.com.amazon.core.okhttp.OkHttp;

/**
 * Created by jiangy on 18-4-3.
 */

public class RegisterActivity extends Activity implements View.OnClickListener {
    private EditText mRealName;
    private EditText mPhone;
    private EditText mPassword;
    private CheckBox mShowPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        findView();

        init();
    }

    private void findView() {
        mRealName = findViewById(R.id.real_name);
        mPhone = findViewById(R.id.phone);
        mPassword = findViewById(R.id.password);
        mShowPass = findViewById(R.id.show_pass);

        findViewById(R.id.to_login).setOnClickListener(this);
        findViewById(R.id.create_new).setOnClickListener(this);
    }

    private void init() {
        mShowPass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                        mPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }else {
                    mPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.to_login:
                finish();
                break;
            case R.id.create_new:
                String uPhone = mPhone.getText().toString();
                String uPassword = mPassword.getText().toString();
                String uName = mRealName.getText().toString();
                if ("".equals(uPhone) && uPhone == null){//验证输入的手机号是否为空
                    Toast.makeText(RegisterActivity.this, "请输入手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if ("".equals(uPassword) && uPassword == null){//验证输入的密码是否为空
                    Toast.makeText(RegisterActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if ("".equals(uName) && uName == null){//验证两次输入的密码是否一致
                    Toast.makeText(RegisterActivity.this, "请输入真实姓名", Toast.LENGTH_SHORT).show();
                    return;
                }
                OkHttp.request("insertUser")
                        .addParameter("uPhone", uPhone)
                        .addParameter("uPassword", uPassword)
                        .addParameter("uName", uName)
                        .send(new OkHttp.Callback<JSONObject>() {
                            @Override
                            public void onDataReceived(JSONObject result) {
                                if (result.optInt("status") == 0) {
                                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                    Intent success = new Intent();
                                    success.putExtra("phone", mPhone.getText().toString());
                                    success.putExtra("password", mPassword.getText().toString());
                                    setResult(Global.CREATE_SUCCESS, success);
                                    finish();
                                }else {
                                    Toast.makeText(RegisterActivity.this, result.optString("msg"), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                break;
        }
    }
}
