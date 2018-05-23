package com.beanu.l4_clean.adapter.binder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.beanu.arad.Arad;
import com.beanu.l4_clean.R;
import com.beanu.l4_clean.model.bean.Banner;
import com.beanu.l4_clean.support.GlideImageLoaderForRound;
import com.youth.banner.BannerConfig;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by Beanu on 2017/12/5.
 */
public class BannerViewBinder extends ItemViewBinder<Banner, BannerViewBinder.ViewHolder> {

    private int w_screen;

    public BannerViewBinder() {
        w_screen = Arad.app.deviceInfo.getScreenWidth();

    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_banner, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Banner banner) {


        final Context context = holder.itemView.getContext();

        ViewGroup.LayoutParams params = holder.mLinearLayout.getLayoutParams();
        params.width = w_screen;
        params.height = w_screen / 8 * 4;
        holder.mLinearLayout.setLayoutParams(params);

//        holder.mBannerLayout.setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE);
        holder.mBannerLayout.setIndicatorGravity(BannerConfig.CENTER);
        holder.mBannerLayout.isAutoPlay(true);
        holder.mBannerLayout.setDelayTime(3000);

        //设置图片集合
        if (banner.getItemList() != null) {
            holder.mBannerLayout.setImages(banner.getItemList());

//            List<String> tities = new ArrayList<>();
//            for (BannerItem bannerItem : banner.getItemList()) {
//                tities.add(bannerItem.getTitle());
//            }
//
//            holder.mBannerLayout.setBannerTitles(tities);
        }
        holder.mBannerLayout.start();


    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ll_layout) LinearLayout mLinearLayout;
        @BindView(R.id.banner) com.youth.banner.Banner mBannerLayout;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mBannerLayout.setImageLoader(new GlideImageLoaderForRound());
        }
    }
}
