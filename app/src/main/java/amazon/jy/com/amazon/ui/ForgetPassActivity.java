package amazon.jy.com.amazon.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import amazon.jy.com.amazon.R;
import amazon.jy.com.amazon.utils.CodeUtil;
import amazon.jy.com.amazon.utils.JuheSMS;

/**
 * Created by jiangy on 18-4-3.
 */

public class ForgetPassActivity extends Activity implements View.OnClickListener {
    private EditText mPhone;
    private EditText mSmsCode;
    private Button mSendSms;

    String Code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);

        findView();
    }

    private void findView() {
        mPhone = findViewById(R.id.u_phone);
        mSmsCode = findViewById(R.id.sms);
        mSendSms = findViewById(R.id.send_sms);
        findViewById(R.id.btn_go_on).setOnClickListener(this);

        findViewById(R.id.send_sms).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_go_on:
                if (mSmsCode.getText().toString().equals(Code)){
                    Intent resetPass = new Intent(ForgetPassActivity.this,ResetPassActivity.class);
                    resetPass.putExtra("uPhone", mPhone.getText().toString());
                    startActivity(resetPass);
                    finish();
                }else {
                    Toast.makeText(ForgetPassActivity.this,"错误的验证码",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.send_sms:
                Code = CodeUtil.getRandom(4);
                JuheSMS.getRequest2(ForgetPassActivity.this,
                        mPhone.getText().toString(), 75049, Code);
                //设置倒计时
                new CountDownTimer(60000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                mSendSms.setClickable(false);
                                mSendSms.setText("重新获取(" + millisUntilFinished/1000 +"s)");
                            }

                            @Override
                            public void onFinish() {
                                mSendSms.setText("重新获取验证码");
                                mSendSms.setClickable(true);
                            }
                        }.start();
                break;
        }
    }
}
