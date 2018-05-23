package com.beanu.l2_imageselector;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yancy.imageselector.ImageLoader;

/**
 * 图片加载器
 * Created by Beanu on 2016/12/15.
 */

public class GlideLoader implements ImageLoader {
    @Override
    public void displayImage(Context context, String path, ImageView imageView) {
        Glide.with(context)
                .load(path)
                .apply(new RequestOptions().circleCrop()).into(imageView);
    }
}
