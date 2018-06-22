package amazon.jy.com.amazon.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jude.rollviewpager.adapter.StaticPagerAdapter;

public class TestNomalAdapter extends StaticPagerAdapter {
    private int[] imgs;

    public TestNomalAdapter(int[] imgs){
        this.imgs = imgs;
    }

    @Override
    public View getView(ViewGroup container, int position) {
        ImageView view = new ImageView(container.getContext());
        view.setImageResource(imgs[position]);
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return view;
    }
    
    @Override
    public int getCount() {
        return imgs.length;
    }
}