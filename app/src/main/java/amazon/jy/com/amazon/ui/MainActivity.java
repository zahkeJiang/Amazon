package amazon.jy.com.amazon.ui;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Response;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.hintview.IconHintView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import amazon.jy.com.amazon.R;
import amazon.jy.com.amazon.adapter.BookListAdapter;
import amazon.jy.com.amazon.adapter.SearchListAdapter;
import amazon.jy.com.amazon.adapter.TestNomalAdapter;
import amazon.jy.com.amazon.core.App;
import amazon.jy.com.amazon.core.okhttp.OkHttp;
import amazon.jy.com.amazon.core.volley.VolleyApi;
import amazon.jy.com.amazon.entity.Book;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    SharedPreferences preferences;
    private EditText mSearchPro; //搜索商品框
    private GridView mGradView;//商品列表
    private DrawerLayout mDrawer;  //侧滑栏
    private LinearLayout mLeftDrawer; //左侧滑动栏
    private RollPagerView mRollPagerView;//轮播图
    private SwipeRefreshLayout mSwipeRefresh;

    private ListView mSearchList;  //搜索结果列表

    private ScrollView mScrollView;

    private Integer uId;

    private int[] imgs = {
            R.drawable.banner_one,
            R.drawable.banner_two,
            R.drawable.banner_one
    };

    ArrayList<Book> books = new ArrayList<>();

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.arg1 == BOOK_LIST){
                mGradView.setAdapter(new BookListAdapter(MainActivity.this, books));
                setGridViewHeightBasedOnChildren(mGradView);
                mGradView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        LinearLayout layout = (LinearLayout) mGradView.getChildAt(position);
                        //获取到被点击的item的textview控件
                        TextView mId = layout.findViewById(R.id.text_b_id);
                        Intent bookInfo = new Intent(MainActivity.this,BookInfoActivity.class);
                        bookInfo.putExtra("bId",mId.getText());
                        startActivity(bookInfo);

                    }
                });

                //判断是否在下拉刷新中
                if (mSwipeRefresh.isRefreshing()){
                    mSwipeRefresh.setRefreshing(false);
                }
                //判断ScrollView是否滑到顶部
                if (mScrollView != null) {
                    mScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                        @Override
                        public void onScrollChanged() {
                            if (mSwipeRefresh != null) {
                                mSwipeRefresh.setEnabled(mScrollView.getScrollY() == 0);
                            }
                        }
                    });
                }
            }
        }
    };
    private int BOOK_LIST = 1;

    private TextView mCartNumber;
    private TextView mLoginText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        init();
    }

    private void findView() {
        mSearchPro = findViewById(R.id.edit_search_pro);
        mDrawer = findViewById(R.id.layout_drawer);
        mLeftDrawer = findViewById(R.id.layout_left_drawer);
        mRollPagerView = findViewById(R.id.roll_pager);
        mScrollView = findViewById(R.id.scroll_view);
        findViewById(R.id.image_menu).setOnClickListener(this);
        findViewById(R.id.my_order).setOnClickListener(this);
        findViewById(R.id.home).setOnClickListener(this);
        findViewById(R.id.my_account).setOnClickListener(this);
        findViewById(R.id.my_address).setOnClickListener(this);
        mLoginText = findViewById(R.id.text_login);
        mLoginText.setOnClickListener(this);
        mSwipeRefresh = findViewById(R.id.swipe_refresh);
        mSearchList = findViewById(R.id.search_list);


        mGradView = findViewById(R.id.grid_view_book);

        mCartNumber = findViewById(R.id.cart_number);
        mCartNumber.setOnClickListener(this);

    }

    //初始化方法
    private void init() {
        preferences = App.getApp().getPreferences();
        uId = preferences.getInt("uId", 0);

        initDrawer();
        mSearchPro.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, final int before, int count) {
                if (!"".equals(s.toString())){
                    OkHttp.request("searchBook")
                            .addParameter("bName", s.toString())
                            .send(new OkHttp.Callback<JSONObject>() {
                                @Override
                                public void onDataReceived(JSONObject result) {

                                    if (result.optInt("status") == 0){
                                        JSONArray data = result.optJSONArray("data");
                                        ArrayList<Book> books = new ArrayList();
                                        try {
                                            for (int i =0;i<data.length();i++){
                                                Book book = new Book();
                                                book.setbId(data.getJSONObject(i).optString("bId"));
                                                book.setbName(data.getJSONObject(i).optString("bName"));
                                                System.out.println("search list: " + book.getbName());
                                                books.add(book);
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        mSearchList.setAdapter(new SearchListAdapter(MainActivity.this,books));
                                        mSearchList.setVisibility(View.VISIBLE);
                                    }
                                }
                            });
                }else {
                    mSearchList.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mSearchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LinearLayout layout = (LinearLayout) mSearchList.getChildAt(position);
                //获取到被点击的item的textview控件
                TextView bId = layout.findViewById(R.id.result_id);
                Intent bookInfo = new Intent(MainActivity.this, BookInfoActivity.class);
                bookInfo.putExtra("bId", bId.getText().toString());
                startActivity(bookInfo);
            }
        });

        mRollPagerView.setHintView(new IconHintView(this,R.drawable.login_point_selected,R.drawable.login_point));
        mRollPagerView.setAdapter(new TestNomalAdapter(imgs));
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                httpRequest();
            }
        });

        httpRequest();
    }

    private void httpRequest() {
        books.clear();
        VolleyApi.GET(this, "getBookList", new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                getBooks(response.toString());
                Message m = new Message();
                m.arg1 = BOOK_LIST;
                handler.sendMessage(m);

            }
        });

    }

    private void initDrawer() {
        mDrawer.setScrimColor(Color.TRANSPARENT);
        mDrawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                //设置侧栏可点击
                mLeftDrawer.setClickable(true);
            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edit_search_pro://搜索按钮点击事件
                Toast.makeText(this, "搜索", Toast.LENGTH_SHORT).show();
                break;
            case R.id.image_menu:  //顶部菜单栏点击事件
                openLeftLayout();
                break;
            case R.id.text_login:
                Intent login = new Intent(this,LoginActivity.class);
                startActivity(login);
                break;
            case R.id.cart_number:
                if (uId == 0){
                    Intent toLogin = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(toLogin);
                }else {
                    Intent toCart = new Intent(MainActivity.this, CartActivity.class);
                    startActivity(toCart);
                }
                break;
            case R.id.my_order:
                if (uId == 0){
                    Intent toLogin = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(toLogin);
                }else {
                    //订单列表
                    Intent orders = new Intent(MainActivity.this, OrdersActivity.class);
                    startActivity(orders);
                }
                break;
            case R.id.home:
                mDrawer.closeDrawer(mLeftDrawer);
                break;
            case R.id.my_account:
                if (uId == 0){
                    Intent toLogin = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(toLogin);
                }else {
                    Intent account = new Intent(MainActivity.this, AccountActivity.class);
                    startActivity(account);
                }
                break;
            case R.id.my_address:
                if (uId == 0){
                    Intent toLogin = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(toLogin);
                }else {
                    Intent address = new Intent(MainActivity.this, AddressActivity.class);
                    address.putExtra("type", "edit");
                    startActivity(address);
                }
                break;
        }
    }

    //左边菜单开关事件
    public void openLeftLayout() {
        if (mDrawer.isDrawerOpen(mLeftDrawer)) {
            mDrawer.closeDrawer(mLeftDrawer);
        } else {
            mDrawer.openDrawer(mLeftDrawer);
        }
    }

    //手动设置gridView的高度
    private void setGridViewHeightBasedOnChildren(GridView gridView) {

        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        int index;
        if (listAdapter.getCount()%2 == 0){
            index = listAdapter.getCount()/2;
        }else {
            index = listAdapter.getCount()/2 + 1;
        }
        for (int i = 0; i < index; i++) {
            View listItem = listAdapter.getView(i, null, gridView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight;
        gridView.setLayoutParams(params);
    }

    public void getBooks(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            System.out.println("status:"+jsonObject.optInt("status"));
            if (jsonObject.optInt("status") == 0){
                JSONArray data = jsonObject.optJSONArray("data");
                for (int i=0;i<data.length();i++){
                    JSONObject jsonObject1 = data.getJSONObject(i);
                    Book book = new Book();
                    book.setbId(jsonObject1.optString("bId"));
                    book.setbName(jsonObject1.optString("bName"));
                    book.setbStar(jsonObject1.optDouble("bStar"));
                    book.setbUnitprice(jsonObject1.optDouble("bUnitprice"));
                    book.setbPicture(jsonObject1.optString("bPicture"));

                    books.add(book);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        int carts = preferences.getInt("carts",0);
        uId = preferences.getInt("uId", 0);
        String uName = preferences.getString("uName","登录");
        mCartNumber.setText(carts + "");
        mLoginText.setText("你好," + uName);
        if (uName.equals("登录")){
            mLoginText.setClickable(true);
        }else {
            mLoginText.setClickable(false);
        }
    }
}
