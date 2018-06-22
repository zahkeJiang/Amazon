package amazon.jy.com.amazon.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import amazon.jy.com.amazon.R;
import amazon.jy.com.amazon.adapter.CartListAdapter;
import amazon.jy.com.amazon.core.App;
import amazon.jy.com.amazon.core.Global;
import amazon.jy.com.amazon.core.volley.VolleyApi;
import amazon.jy.com.amazon.entity.Book;
import amazon.jy.com.amazon.entity.Cart;

public class CartActivity extends AppCompatActivity implements View.OnClickListener {
    SharedPreferences preferences;
    private ListView mCartList;
    private TextView mTotalPrice;
    int uId;
    ArrayList<Cart> carts;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.arg1 == Global.DELETE_CART) {
                for (int i = 0; i < carts.size(); i++) {
                    if (carts.get(i).getcId() == msg.what) {
                        carts.remove(i);
                        //从购物车删除
                        preferences.edit()
                                .putInt("carts", preferences.getInt("carts", 0) - 1)
                                .commit();
                    }
                }
                mCartList.setAdapter(new CartListAdapter(CartActivity.this, carts, handler));
                setTotalPrice(carts);
            }
        }
    };
    private ArrayList<Cart> totalPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        findView();
        init();
    }
    private void findView() {
        findViewById(R.id.settlement).setOnClickListener(this);
        mCartList = findViewById(R.id.cart_list);
        mTotalPrice = findViewById(R.id.total_price);
    }
    private void init() {
        preferences = App.getApp().getPreferences();
        uId = preferences.getInt("uId", -1);
        String param = "?uId=" + uId;
        VolleyApi.POST(this, "selectCartInfo" + param, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                System.out.println("uId:"+ uId + response.toString());
                carts = getCarts(response.toString());
                setListViewHeightBasedOnChildren(mCartList);
                mCartList.setAdapter(new CartListAdapter(CartActivity.this, carts, handler));
                setTotalPrice(carts);
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.settlement:
                if (carts.size() > 0){
                    Intent settlement = new Intent(CartActivity.this, SettlementActivity.class);
                    String uId = "?uId=" + this.uId;
                    String isCart = "isCart=1";
                    String bId = "";
                    for (int i =0;i<carts.size();i++){
                        bId += "bId="+carts.get(i).getBook().getbId() + "&quantity=1&";
                    }
                    String param = uId + "&" + bId + isCart;
                    settlement.putExtra("param", param);
                    startActivity(settlement);
                }else {
                    Toast.makeText(CartActivity.this, "请先添加物品", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    public ArrayList<Cart> getCarts(String response) {
        try {
            JSONObject json = new JSONObject(response);
            if (json.optInt("status") == 0){
                JSONArray data = json.optJSONArray("data");
                ArrayList<Cart> carts = new ArrayList<>();
                for (int i=0;i<data.length();i++){
                    Cart cart = new Cart();
                    int cId = data.getJSONObject(i).optInt("cId");
                    int uId = data.getJSONObject(i).optInt("uId");
                    String bId = data.getJSONObject(i).optString("bId");
                    JSONObject b = data.getJSONObject(i).optJSONObject("book");
                    String bName = b.optString("bName");
                    double bStar = b.optDouble("bStar");
                    double bUnitprice = b.optDouble("bUnitprice");
                    String bPicture = b.optString("bPicture");
                    Book book = new Book();
                    book.setbId(bId);
                    book.setbName(bName);
                    book.setbStar(bStar);
                    book.setbUnitprice(bUnitprice);
                    book.setbPicture(bPicture);
                    cart.setcId(cId);
                    cart.setuId(uId);
                    cart.setBook(book);
                    carts.add(cart);
                }
                return carts;
            }else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    //手动设置listView的高度
    private void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        totalHeight = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight;
        listView.setLayoutParams(params);
    }
    public void setTotalPrice(ArrayList<Cart> carts) {
        double totlePrice = 0;
        for (int i = 0; i < carts.size(); i++) {
            totlePrice += carts.get(i).getBook().getbUnitprice();
        }
        mTotalPrice.setText("¥ " + totlePrice);
    }
}
