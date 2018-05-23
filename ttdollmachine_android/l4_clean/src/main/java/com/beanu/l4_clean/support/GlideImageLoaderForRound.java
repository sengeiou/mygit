package com.beanu.l4_clean.support;

import android.content.Context;
import android.widget.ImageView;

import com.beanu.l4_clean.model.bean.BannerItem;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.youth.banner.loader.ImageLoader;

/**
 * banner 专用
 * Created by Beanu on 2017/7/10.
 */

public class GlideImageLoaderForRound extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        //Glide 加载图片简单用法
        if (path instanceof BannerItem) {
            BannerItem item = (BannerItem) path;

            RequestOptions options = new RequestOptions();
            options.centerCrop().transform(new RoundedCorners(20));
            Glide.with(context).load(item.getImgUrl()).apply(options).into(imageView);
        }

    }
}