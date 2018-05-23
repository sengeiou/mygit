package com.beanu.l4_bottom_tab.support;

import android.content.Context;
import android.widget.ImageView;

import com.beanu.l4_bottom_tab.model.bean.BannerItem;
import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;

/**
 * banner图片加载器
 * Created by Beanu on 2017/5/25.
 */

public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        //Glide 加载图片简单用法
        if (path instanceof BannerItem) {
            BannerItem item = (BannerItem) path;
            Glide.with(context).load(item.getImgUrl()).into(imageView);
        }

    }
}
