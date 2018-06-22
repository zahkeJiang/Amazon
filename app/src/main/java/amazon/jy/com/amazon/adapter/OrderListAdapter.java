package amazon.jy.com.amazon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import amazon.jy.com.amazon.R;
import amazon.jy.com.amazon.entity.Order;

/**
 * Created by jiangy on 18-4-4.
 */

public class OrderListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Order> orders;
    public OrderListAdapter(Context context, ArrayList<Order> orders){
        this.context = context;
        this.orders = orders;
    }
    @Override
    public int getCount() {
        return orders.size();
    }
    @Override
    public Object getItem(int position) {
        return orders.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.order_list_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Order order = orders.get(position);
        viewHolder.mOrderId.setText(order.getoId() + "");
        viewHolder.mOrderNumber.setText(order.getBussinessId());
        viewHolder.mOrderDate.setText(order.getoDate());
        viewHolder.mOrderPrice.setText("" + order.getoCount());
        viewHolder.mOrderStatus.setText("" + order.getoStatus());
        return convertView;
    }
    private class ViewHolder {
        TextView mOrderId;
        TextView mOrderNumber;
        TextView mOrderDate;
        TextView mOrderPrice;
        TextView mOrderStatus;
        public ViewHolder(View view) {
            mOrderId = view.findViewById(R.id.order_id);
            mOrderNumber = view.findViewById(R.id.order_number);
            mOrderDate = view.findViewById(R.id.order_date);
            mOrderPrice = view.findViewById(R.id.order_price);
            mOrderStatus = view.findViewById(R.id.order_status);
        }
    }
}