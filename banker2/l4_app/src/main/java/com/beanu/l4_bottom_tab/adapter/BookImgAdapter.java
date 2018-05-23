package com.beanu.l4_bottom_tab.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.model.bean.BookImg;
import com.bumptech.glide.Glide;

import java.util.List;


/**
 * 滚动大图
 * Created by yunhe on 2015/5/21.
 */
public class BookImgAdapter extends PagerAdapter {

    private List<BookImg> mPaths;
    private Context mContext;
    private RelativeLayout layout;
    int w_screen;

    public BookImgAdapter(Context cx, RelativeLayout layout) {
        mContext = cx;
        this.layout = layout;
        WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);
        w_screen = dm.widthPixels;
    }

    public void changeData(List<BookImg> paths) {
        mPaths = paths;
    }

    @Override
    public int getCount() {
        if (mPaths == null)
            return 0;
        return mPaths.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        final BookImg productItem = mPaths.get(position);

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_banner_viewpager, container, false);
        ImageView iv = (ImageView) view.findViewById(R.id.img_banner);

        //viewpager 自适应高度
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        params.width = w_screen;
        params.height = w_screen / 8 * 6;
        layout.setLayoutParams(params);

        if (iv != null) {
            Glide.with(mContext).load(productItem.getUrl()).into(iv);
            iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
            container.addView(view);
        }
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
