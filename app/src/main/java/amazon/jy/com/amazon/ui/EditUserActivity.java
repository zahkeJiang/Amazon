package amazon.jy.com.amazon.ui;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONObject;

import amazon.jy.com.amazon.R;
import amazon.jy.com.amazon.core.App;
import amazon.jy.com.amazon.core.okhttp.OkHttp;

public class EditUserActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mRealName;
    private EditText mNickname;
    private EditText mAccountNum;
    private Spinner mSex;
    private EditText mQQNum;

    SharedPreferences preferences;

    String sex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        findView();

        init();
    }

    private void findView() {
        mAccountNum = findViewById(R.id.account_num);
        mNickname = findViewById(R.id.nickname);
        mRealName = findViewById(R.id.real_name);
        mSex = findViewById(R.id.sex);
        mQQNum = findViewById(R.id.QQ_num);

        findViewById(R.id.save).setOnClickListener(this);
    }

    private void init() {
        preferences = App.getApp().getPreferences();

        String uName = preferences.getString("uName", "");
        String uPhone = preferences.getString("uPhone", "");
        String uRegister = preferences.getString("uRegister", "");
        String uSex = preferences.getString("uSex", "");
        String uQQ = preferences.getString("uQQ", "");


        mRealName.setText(uName);
        mNickname.setText(uRegister);
        mAccountNum.setText(uPhone);
        mQQNum.setText(uQQ);

        mSex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] type = getResources().getStringArray(R.array.sex);

                sex = type[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                sex = getResources().getStringArray(R.array.sex)[0];
            }
        });

        String[] sexs = getResources().getStringArray(R.array.sex);
        for (int i=0;i<sexs.length;i++){
            if (sexs[i].equals(uSex)){
                mSex.setSelection(i);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.save:

                OkHttp.request("updateUser")
                        .addParameter("uPhone", mAccountNum.getText().toString())
                        .addParameter("uRegister", mNickname.getText().toString())
                        .addParameter("uName", mRealName.getText().toString())
                        .addParameter("uSex", sex)
                        .addParameter("uQQ", mQQNum.getText().toString())
                        .send(new OkHttp.Callback<JSONObject>() {
                            @Override
                            public void onDataReceived(JSONObject result) {
                                System.out.println("result edit:" + result.toString());
                                if (result.optInt("status") == 0){
                                    preferences.edit()
                                            .putString("uPhone", mAccountNum.getText().toString())
                                            .putString("uRegister", mNickname.getText().toString())
                                            .putString("uName", mRealName.getText().toString())
                                            .putString("uSex", sex)
                                            .putString("uQQ", mQQNum.getText().toString())
                                            .commit();
                                    Toast.makeText(EditUserActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                }else {
                                    Toast.makeText(EditUserActivity.this, "服务器异常", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                break;
        }
    }
}
