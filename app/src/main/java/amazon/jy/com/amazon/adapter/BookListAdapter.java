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

public class BookListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Book> books;

    public BookListAdapter(Context context, ArrayList<Book> books){
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
            convertView = LayoutInflater.from(context).inflate(R.layout.book_list_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Book book = books.get(position);

        Picasso.with(context)
                .load(book.getbPicture())
                .into(viewHolder.mImageCover);
        viewHolder.mId.setText(book.getbId());
        viewHolder.mBookName.setText(book.getbName());
        viewHolder.mPrice.setText("" + book.getbUnitprice());
        viewHolder.mRatingStar.setRating(Float.parseFloat(book.getbStar() + ""));
        return convertView;
    }

    private class ViewHolder {
        TextView mId;
        ImageView mImageCover;
        TextView mBookName;
        TextView mPrice;
        RatingBar mRatingStar;

        public ViewHolder(View view) {
            mId = view.findViewById(R.id.text_b_id);
            mImageCover = view.findViewById(R.id.book_cover);
            mBookName = view.findViewById(R.id.book_name);
            mPrice = view.findViewById(R.id.book_price);
            mRatingStar = view.findViewById(R.id.rating_star);
        }
    }
}
