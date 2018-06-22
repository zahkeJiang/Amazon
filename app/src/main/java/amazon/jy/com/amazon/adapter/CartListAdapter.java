package amazon.jy.com.amazon.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import amazon.jy.com.amazon.R;
import amazon.jy.com.amazon.core.Global;
import amazon.jy.com.amazon.core.volley.VolleyApi;
import amazon.jy.com.amazon.entity.Book;
import amazon.jy.com.amazon.entity.Cart;
import amazon.jy.com.amazon.ui.CartActivity;

/**
 * Created by jiangy on 18-4-4.
 */

public class CartListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Cart> carts;
    private Handler handler;
    public CartListAdapter(Context context, ArrayList<Cart> carts, Handler handler){
        this.context = context;
        this.carts = carts;
        this.handler = handler;
    }
    @Override
    public int getCount() {
        return carts.size();
    }
    @Override
    public Object getItem(int position) {
        return carts.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.cart_list_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Cart cart = carts.get(position);
        Picasso.with(context)
                .load(cart.getBook().getbPicture())
                .into(viewHolder.mBookPicture);
        viewHolder.mBookName.setText(cart.getBook().getbName());
        viewHolder.mPrice.setText("" + cart.getBook().getbUnitprice());
        viewHolder.mRatingStar.setRating(Float.parseFloat(cart.getBook().getbStar() + ""));
        viewHolder.mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String param = "?cId=" + cart.getcId();
                VolleyApi.POST(context, "deleteCart" + param, new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        try {
                            JSONObject json = new JSONObject(response.toString());
                            if (json.optInt("status") == 0){
                                Message message = new Message();
                                message.arg1 = Global.DELETE_CART;
                                message.what = cart.getcId();
                                handler.sendMessage(message);
                            }else {
                                Toast.makeText(context,"删除失败",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        return convertView;
    }
    private class ViewHolder {
        ImageView mBookPicture;
        TextView mBookName;
        TextView mPrice;
        RatingBar mRatingStar;
        Button mDelete;
        public ViewHolder(View view) {
            mBookPicture = view.findViewById(R.id.book_picture);
            mBookName = view.findViewById(R.id.book_name);
            mPrice = view.findViewById(R.id.book_price);
            mRatingStar = view.findViewById(R.id.rating_star);
            mDelete = view.findViewById(R.id.delete_btn);
        }
    }
}
