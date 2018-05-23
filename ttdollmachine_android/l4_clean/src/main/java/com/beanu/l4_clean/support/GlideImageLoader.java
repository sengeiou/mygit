package com.beanu.l4_clean.support;

import android.content.Context;
import android.widget.ImageView;

import com.beanu.l4_clean.model.bean.BannerItem;
import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;

/**
 * banner 专用
 * Created by Beanu on 2017/7/10.
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