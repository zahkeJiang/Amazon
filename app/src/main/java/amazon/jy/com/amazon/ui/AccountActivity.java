package amazon.jy.com.amazon.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import amazon.jy.com.amazon.R;
import amazon.jy.com.amazon.core.App;
import amazon.jy.com.amazon.core.okhttp.OkHttp;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mRealName;
    private TextView mAccountNum;
    private TextView mNickname;
    private TextView mSex;

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        findView();
        init();
    }

    private void findView() {
        mRealName = findViewById(R.id.real_name);
        mAccountNum = findViewById(R.id.account_num);
        mNickname = findViewById(R.id.nickname);
        mSex = findViewById(R.id.sex);

        findViewById(R.id.exit_login).setOnClickListener(this);
        findViewById(R.id.edit_user).setOnClickListener(this);
    }

    private void init() {
        preferences = App.getApp().getPreferences();

        String uName = preferences.getString("uName", "");
        String uPhone = preferences.getString("uPhone", "");
        String uRegister = preferences.getString("uRegister", "");
        String uSex = preferences.getString("uSex", "");

        mRealName.setText(uName);
        mSex.setText(uSex);
        mNickname.setText(uRegister);
        mAccountNum.setText(uPhone);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.exit_login:
                preferences.edit().clear().commit();
                finish();
                break;
            case R.id.edit_user:
                Intent editUser = new Intent(AccountActivity.this, EditUserActivity.class);
                startActivity(editUser);
                break;
        }
    }
}
