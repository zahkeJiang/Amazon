package amazon.jy.com.amazon.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import amazon.jy.com.amazon.R;
import amazon.jy.com.amazon.core.okhttp.OkHttp;

/**
 * Created by jiangy on 18-4-4.
 */

public class ResetPassActivity extends Activity implements View.OnClickListener {
    private EditText mPassword;
    private EditText mEnsurePass;
    String uPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);
        findView();
        init();
    }
    private void findView() {
        mPassword = findViewById(R.id.password);
        mEnsurePass = findViewById(R.id.ensure_pass);

        findViewById(R.id.save_change).setOnClickListener(this);
    }
    private void init() {
        Intent intent = getIntent();
        uPhone = intent.getStringExtra("uPhone");
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.save_change:
                if (!mPassword.getText().toString().equals(mEnsurePass.getText().toString())){
                    Toast.makeText(ResetPassActivity.this,"两次密码不一致",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mPassword.getText().toString().length()<8){
                    Toast.makeText(ResetPassActivity.this,"请输入8位以上的密码",Toast.LENGTH_SHORT).show();
                    return;
                }
                OkHttp.request("resetPass")
                        .addParameter("uPhone", uPhone)
                        .addParameter("uPassword", mPassword.getText().toString())
                        .send(new OkHttp.Callback<JSONObject>() {
                            @Override
                            public void onDataReceived(JSONObject result) {
                                if (result.optInt("status") == 0){
                                    Toast.makeText(ResetPassActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                                    finish();
                                }else {
                                    Toast.makeText(ResetPassActivity.this,"服务器异常",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                break;
        }
    }
}
