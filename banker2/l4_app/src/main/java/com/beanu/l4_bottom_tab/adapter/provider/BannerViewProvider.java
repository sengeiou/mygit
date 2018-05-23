package com.beanu.l4_bottom_tab.adapter.provider;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.beanu.arad.Arad;
import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.model.bean.Banner;
import com.beanu.l4_bottom_tab.model.bean.BannerItem;
import com.beanu.l4_bottom_tab.support.GlideImageLoader;
import com.beanu.l4_bottom_tab.ui.common.WebActivity;
import com.beanu.l4_bottom_tab.ui.module2_liveLesson.LiveLessonDetailActivity;
import com.beanu.l4_bottom_tab.ui.module3_onlineLesson.OnlineLessonDetailActivity;
import com.beanu.l4_bottom_tab.ui.module4_book.BookDetailActivity;
import com.beanu.l4_bottom_tab.ui.module_news.NewsDetailActivity;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewBinder;

/**
 * banner 视图
 * Created by Beanu on 2017/2/27.
 */
public class BannerViewProvider extends ItemViewBinder<Banner, BannerViewProvider.ViewHolder> {

    private int w_screen;

    public BannerViewProvider() {
        w_screen = Arad.app.deviceInfo.getScreenWidth();
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_banner, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final Banner banner) {

        final Context context = holder.itemView.getContext();

        ViewGroup.LayoutParams params = holder.mRelativeLayout.getLayoutParams();
        params.width = w_screen;
        params.height = w_screen / 8 * 4;
        holder.mRelativeLayout.setLayoutParams(params);

        holder.mBannerLayout.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        holder.mBannerLayout.setIndicatorGravity(BannerConfig.CENTER);
        holder.mBannerLayout.isAutoPlay(true);

        //设置图片集合
        holder.mBannerLayout.setImages(banner.getItemList());
        holder.mBannerLayout.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                BannerItem bannerItem = banner.getItemList().get(position);
                switch (bannerItem.getType()) {
                    case 0:
                    case 1:
                        WebActivity.startActivity(context, bannerItem.getUrl(), bannerItem.getTitle());
                        break;

                    case 2:
                        Intent intent = new Intent(context, OnlineLessonDetailActivity.class);
                        intent.putExtra("lessonId", bannerItem.getObjectId()+"");
                        context.startActivity(intent);
                        break;
                    case 3:
                        Intent intent3 = new Intent(context, LiveLessonDetailActivity.class);
                        intent3.putExtra("lessonId", bannerItem.getObjectId() + "");
                        context.startActivity(intent3);
                        break;
                    case 4:
                        Intent intent4 = new Intent(context, BookDetailActivity.class);
                        intent4.putExtra("bookId", bannerItem.getObjectId()+"");
                        context.startActivity(intent4);
                        break;
                    case 5:
                        Intent intent5 = new Intent(context, NewsDetailActivity.class);
                        intent5.putExtra("newsId", bannerItem.getObjectId()+"");
                        context.startActivity(intent5);
                        break;

                }

            }
        });
        //banner设置方法全部调用完毕时最后调用
        holder.mBannerLayout.start();

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.rl_item_banner) RelativeLayout mRelativeLayout;
        @BindView(R.id.banner) com.youth.banner.Banner mBannerLayout;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mBannerLayout.setImageLoader(new GlideImageLoader());
        }
    }
}