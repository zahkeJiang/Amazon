package amazon.jy.com.amazon.ui;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import amazon.jy.com.amazon.R;
import amazon.jy.com.amazon.core.App;
import amazon.jy.com.amazon.core.volley.VolleyApi;

/**
 * Created by jiangy on 18-4-7.
 */

public class BookInfoActivity extends Activity implements View.OnClickListener {
    private TextView mBookName;         //书名
    private RatingBar mRatingBar;
    private ImageView mBookCover;
    private TextView mPrice;
    private TextView mDescription;
    private TextView mPublish;
    private TextView mWeight;
    private TextView mLanguage;
    private TextView mPackageSize;
    private TextView mRank;
    private TextView mAuthor;
    private String bookId;
    private TextView mAddress;
    private TextView mAddressId;

    SharedPreferences preferences;
    Integer uId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);
        findView();
        init();
    }

    private void findView() {
        mBookName = findViewById(R.id.book_name);
        mRatingBar = findViewById(R.id.rating_star);
        mBookCover = findViewById(R.id.book_cover);
        mPrice = findViewById(R.id.book_price);
        mDescription = findViewById(R.id.text_description);
        mPublish = findViewById(R.id.publish);
        mWeight = findViewById(R.id.weight);
        mLanguage = findViewById(R.id.language);
        mPackageSize = findViewById(R.id.package_size);
        mRank = findViewById(R.id.rank);
        mAuthor = findViewById(R.id.author);
        mAddress = findViewById(R.id.address);
        mAddressId = findViewById(R.id.address_id);

        findViewById(R.id.select_address).setOnClickListener(this);
        findViewById(R.id.add_to_cart).setOnClickListener(this);
        findViewById(R.id.buy_now).setOnClickListener(this);
    }

    private void init() {
        preferences = App.getApp().getPreferences();
        uId = preferences.getInt("uId", 0);
        Intent intent = getIntent();
        String bId = intent.getStringExtra("bId");
        bookId = bId;
        //添加字段
        String param = "?" + "bId=" + bId;
        VolleyApi.POST(this, "getBook" + param, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                Log.d("response is :", response.toString());
                try {
                    JSONObject json = new JSONObject(response.toString());
                    System.out.println("status" + json.optInt("status"));
                    if (json.optInt("status") == 0){
                        JSONObject data = json.optJSONObject("data");
                        String bIsbn = data.optString("bIsbn");
                        String bPublish = data.optString("bPublish");
                        String bName = data.optString("bName");
                        String bAuthorOne = data.optString("bAuthorOne");
                        String bAuthorTwo = data.optString("bAuthorTwo");
                        String bAuthorThree = data.optString("bAuthorThree");
                        String bAuthorFour = data.optString("bAuthorFour");
                        String bAuthorFive = data.optString("bAuthorFive");
                        String bLanguage = data.optString("bLanguage");
                        int bFormat = data.optInt("bFormat");
                        String bSize = data.optString("bSize");
                        String bWeight = data.optString("bWeight");
                        double bStar = data.optDouble("bStar");
                        int bRank = data.optInt("bRank");
                        double bUnitprice = data.optDouble("bUnitprice");
                        String bDiscription = data.optString("bDiscription");
                        String bStatus = data.optString("bStatus");
                        String bType = data.optString("bType");
                        String bPicture = data.optString("bPicture");

                        mBookName.setText(bName);
                        mRatingBar.setRating((float) bStar);
                        Picasso.with(BookInfoActivity.this).load(bPicture)
                                .into(mBookCover);
                        mPrice.setText(bUnitprice + "");
                        mDescription.setText(bDiscription);
                        mPublish.setText(bPublish);
                        mWeight.setText(bWeight);
                        mLanguage.setText(bLanguage);
                        mPackageSize.setText(bSize);
                        mRank.setText(bRank + "");
                        mAuthor.setText(bAuthorOne + "  " + bAuthorTwo + "  " + bAuthorThree + "  " + bAuthorFour + "  " + bAuthorFive);
                    }else {
                        Toast.makeText(BookInfoActivity.this,json.optString("msg"),Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.select_address:
                if (uId == 0){
                    //请先登录
                    Intent toLogin = new Intent(BookInfoActivity.this,LoginActivity.class);
                    startActivity(toLogin);
                }else {
                    Intent selectAddress = new Intent(BookInfoActivity.this,AddressActivity.class);
                    selectAddress.putExtra("type", "edit");
                    startActivity(selectAddress);
                }
                break;
            case R.id.add_to_cart:
                if (uId == 0){
                    //请先登录
                    Intent toLogin = new Intent(BookInfoActivity.this,LoginActivity.class);
                    startActivity(toLogin);
                }else {
                    String param = "?uId="+ uId +"&bId=" + bookId;
                    VolleyApi.POST(BookInfoActivity.this, "insertCart" + param, new Response.Listener() {
                        @Override
                        public void onResponse(Object response) {
                            try {
                                JSONObject json = new JSONObject(response.toString());
                                if (json.optInt("status") == 0){
                                    //添加到购物车成功
                                    preferences.edit()
                                            .putInt("carts",preferences.getInt("carts",0) + 1)
                                            .commit();
                                    Toast.makeText(BookInfoActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                                }else {
                                    //添加到购物车失败
                                    Toast.makeText(BookInfoActivity.this, json.optString("msg"),Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

                break;
            //立即购买
            case R.id.buy_now:
                if (uId == 0){
                    //请先登录
                    Intent toLogin = new Intent(BookInfoActivity.this,LoginActivity.class);
                    startActivity(toLogin);
                }else {
                    Intent pay = new Intent(BookInfoActivity.this, PayActivity.class);
                    String param = "?uId=" + uId + "&bId=" + bookId +"&quantity=1" + "&isCart=0" +
                            "&aId=" + mAddressId.getText().toString();
                    pay.putExtra("param", param);
                    startActivity(pay);
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String param = "?uId=" + uId;
        VolleyApi.POST(BookInfoActivity.this, "getAddress" + param, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject json = new JSONObject(response.toString());
                    if (json.optInt("status") == 0){
                        JSONObject data = json.optJSONObject("data");
                        mAddress.setText(data.optString("township") + " " + data.optString("remarks"));
                        mAddressId.setText(data.optInt("aId") + "");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}