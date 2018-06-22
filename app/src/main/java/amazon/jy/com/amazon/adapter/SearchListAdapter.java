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
import amazon.jy.com.amazon.entity.Book;

/**
 * Created by jiangy on 18-4-4.
 */

public class SearchListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Book> books;

    public SearchListAdapter(Context context, ArrayList<Book> books){
        this.context = context;
        this.books = books;
    }

    @Override
    public int getCount() {
        return books.size();
    }

    @Override
    public Object getItem(int position) {
        return books.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.search_list_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Book book = books.get(position);

        viewHolder.mId.setText(book.getbId());
        viewHolder.mBookName.setText(book.getbName());
        return convertView;
    }

    private class ViewHolder {
        TextView mId;
        TextView mBookName;

        public ViewHolder(View view) {
            mId = view.findViewById(R.id.result_id);
            mBookName = view.findViewById(R.id.result_name);
        }
    }
}
