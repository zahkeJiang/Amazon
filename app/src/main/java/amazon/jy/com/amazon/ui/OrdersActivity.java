package amazon.jy.com.amazon.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import amazon.jy.com.amazon.R;
import amazon.jy.com.amazon.adapter.OrderListAdapter;
import amazon.jy.com.amazon.core.App;
import amazon.jy.com.amazon.core.volley.VolleyApi;
import amazon.jy.com.amazon.entity.Order;

public class OrdersActivity extends AppCompatActivity {
    private ListView mListView;
    private SharedPreferences preferences;
    ArrayList<Order> orders;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        findView();
        init();
    }
    private void findView() {
        mListView = findViewById(R.id.orders_list);
    }
    private void init() {
        preferences = App.getApp().getPreferences();
        String param = "?uId=" + preferences.getInt("uId",0);
        VolleyApi.POST(OrdersActivity.this, "getOrderList" + param, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject json = new JSONObject(response.toString());
                    if (json.optInt("status") == 0){
                        JSONArray data = json.optJSONArray("data");
                        orders = getOrders(data);
                        mListView.setAdapter(new OrderListAdapter(OrdersActivity.this, orders));
                    }else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent orderDetail = new Intent(OrdersActivity.this, OrderDetailActivity.class);
                orderDetail.putExtra("oId", orders.get(position).getoId());
                startActivity(orderDetail);
            }
        });
    }
    private ArrayList<Order> getOrders(JSONArray data) throws JSONException {
        ArrayList<Order> orderList = new ArrayList<>();
        for (int i=0;i<data.length();i++){
            Order order = new Order();
            order.setoId(data.getJSONObject(i).optInt("oId"));
            order.setBussinessId(data.getJSONObject(i).optString("bussinessId"));
            order.setoCount(data.getJSONObject(i).optDouble("oCount"));
            order.setoDate(data.getJSONObject(i).optString("oDate"));
            order.setoStatus(data.getJSONObject(i).optString("oStatus"));
            order.setoDeliver(data.getJSONObject(i).optString("oDeliver"));
            order.setoDeliverFee(data.getJSONObject(i).optInt("oDeliverFee"));
            order.setuPay(data.getJSONObject(i).optString("uPay"));
            order.setuInvoiceType(data.getJSONObject(i).optString("uInvoiceType"));
            order.setuInvoiceTitle(data.getJSONObject(i).optString("uInvoiceTitle"));
            orderList.add(order);
        }
        return orderList;
    }
}
