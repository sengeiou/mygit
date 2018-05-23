package com.beanu.l4_bottom_tab.support;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.qiyukf.unicorn.api.ImageLoaderListener;
import com.qiyukf.unicorn.api.UnicornImageLoader;

/**
 * 网易 七鱼 专用
 * Created by Beanu on 2017/6/19.
 */

public class GlideImageLoaderForUnicorn implements UnicornImageLoader {

    private Context context;

    public GlideImageLoaderForUnicorn(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public Bitmap loadImageSync(String uri, int width, int height) {
        return null;
    }

    @Override
    public void loadImage(String uri, int width, int height, final ImageLoaderListener listener) {
        Glide.with(context)
                .asBitmap()
                .load(uri)
                .apply(RequestOptions.overrideOf(width, height))
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        if (listener != null) {
                            listener.onLoadComplete(resource);
                        }
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        if (listener != null) {
                            listener.onLoadFailed(new Exception("image load failed"));
                        }
                    }
                });
    }
}