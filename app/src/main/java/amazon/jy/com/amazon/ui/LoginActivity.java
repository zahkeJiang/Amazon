package amazon.jy.com.amazon.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import amazon.jy.com.amazon.R;
import amazon.jy.com.amazon.core.App;
import amazon.jy.com.amazon.core.Global;
import amazon.jy.com.amazon.core.volley.VolleyApi;

/**
 * Created by 蒋圆 on 2018/1/26.
 */

public class LoginActivity extends Activity implements View.OnClickListener {
    private EditText mMobile;
    private EditText mPassword;
    private CheckBox mShowPass;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findView();

        init();
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

    private void findView() {
        findViewById(R.id.btn_create).setOnClickListener(this);
        findViewById(R.id.text_forget_pass).setOnClickListener(this);
        findViewById(R.id.login_btn).setOnClickListener(this);

        mMobile = findViewById(R.id.mobile);
        mPassword = findViewById(R.id.password);
        mShowPass = findViewById(R.id.show_pass);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                //使EditText触发一次失去焦点事件
                v.setFocusable(false);
//                v.setFocusable(true); //这里不需要是因为下面一句代码会同时实现这个功能
                v.setFocusableInTouchMode(true);
                return true;
            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_create:
                Intent register = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivityForResult(register, Global.CREATE_SUCCESS);
                break;
            case R.id.text_forget_pass://忘记密码
                Intent forgetPass = new Intent(LoginActivity.this,ForgetPassActivity.class);
                startActivity(forgetPass);
                break;
            case R.id.login_btn:
                String mobile = mMobile.getText().toString();
                String password = mPassword.getText().toString();
                if ("".equals(mobile)){
                    Toast.makeText(LoginActivity.this,"手机号不能为空",Toast.LENGTH_SHORT).show();
                }else if ("".equals(password)){
                    Toast.makeText(LoginActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                }else {
                    String param = "?uPhone=" + mobile + "&uPassword=" + password;
                    VolleyApi.POST(this, "login" + param, new Response.Listener() {
                        @Override
                        public void onResponse(Object response) {
                            try {
                                JSONObject json = new JSONObject(response.toString());
                                if (json.optInt("status") == 0){
                                    JSONObject data = json.optJSONObject("data");
                                    preferences = App.getApp().getPreferences();
                                    SharedPreferences.Editor editor = preferences.edit();

                                    editor.putInt("uId", data.optInt("uId"));
                                    editor.putString("uRegister", data.optString("uRegister"));
                                    editor.putString("uName", data.optString("uName"));
                                    editor.putString("uSex", data.optString("uSex"));
                                    editor.putString("uPhone", data.optString("uPhone"));
                                    editor.putString("uQQ", data.optString("uQQ"));
                                    editor.putInt("carts", data.optInt("carts"));
                                    editor.commit();
                                    Toast.makeText(LoginActivity.this,R.string.login_success,Toast.LENGTH_SHORT).show();
                                    finish();
                                }else {
                                    Toast.makeText(LoginActivity.this,json.optString("msg"),Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
     if (resultCode == Global.CREATE_SUCCESS){
         mMobile.setText(data.getStringExtra("phone"));
         mPassword.setText(data.getStringExtra("password"));
     }
    }
}
