package amazon.jy.com.amazon.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import amazon.jy.com.amazon.R;
import amazon.jy.com.amazon.adapter.BooksAdapter;
import amazon.jy.com.amazon.core.volley.VolleyApi;
import amazon.jy.com.amazon.entity.Book;

public class OrderDetailActivity extends Activity {
    private TextView mOrderNumber;
    private TextView mOrderDate;
    private TextView mOrderPrice;
    private TextView mOrderStatus;
    private TextView mPayType;
    private TextView mUInvoiceTitle;
    private ListView mBookList;
    private TextView textOrder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        findView();
        init();
    }
    private void findView() {
        mOrderNumber = findViewById(R.id.order_number);
        mOrderDate = findViewById(R.id.order_date);
        mOrderPrice = findViewById(R.id.order_price);
        mPayType = findViewById(R.id.pay_type);
        mOrderStatus = findViewById(R.id.order_status);
        mUInvoiceTitle = findViewById(R.id.uInvoiceTitle);
        mBookList = findViewById(R.id.book_list);
        textOrder = findViewById(R.id.to_order_detail_text);
    }
    private void init() {
        Intent intent = getIntent();
        int oId = intent.getIntExtra("oId", -1);
        String param = "?oId=" + oId;
        VolleyApi.POST(this, "getOrderDetails" + param, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                try {
                    JSONObject json = new JSONObject(response.toString());
                    System.out.println("response :" + response.toString());
                    if (json.optInt("status") == 0){
                        JSONObject data = json.optJSONObject("data");
                        mOrderNumber.setText(data.optString("bussinessId"));
                        mOrderDate.setText(data.optString("oDate"));
                        mOrderPrice.setText("" + data.optDouble("oCount"));
                        mOrderStatus.setText(data.optString("oStatus"));
                        mPayType.setText(data.optString("uPay"));
                        mUInvoiceTitle.setText(data.optString("uInvoiceTitle"));

                        mBookList.setAdapter(new BooksAdapter(OrderDetailActivity.this,getBoooks(data.getJSONArray("books"))));
                        setListViewHeightBasedOnChildren(mBookList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        //将最顶部的空间获取焦点
        textOrder.setFocusable(true);
        textOrder.setFocusableInTouchMode(true);
        textOrder.requestFocus();
    }
    private ArrayList<Book> getBoooks(JSONArray books) throws JSONException {
        ArrayList<Book> books1 = new ArrayList<>();
        for (int i=0;i<books.length();i++){
            Book book = new Book();
            book.setbId(books.getJSONObject(i).optString("bId"));
            book.setbPicture(books.getJSONObject(i).optString("bPicture"));
            book.setbName(books.getJSONObject(i).optString("bName"));
            book.setbStar(books.getJSONObject(i).optDouble("bStar"));
            book.setbUnitprice(books.getJSONObject(i).optDouble("bUnitprice"));
            book.setQuantity(books.getJSONObject(i).optInt("quantity"));

            books1.add(book);
        }
        return books1;
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

        ViewGroup.LayoutParams params = listView.getLayoutParams();
//        params.height = totalHeight;
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
