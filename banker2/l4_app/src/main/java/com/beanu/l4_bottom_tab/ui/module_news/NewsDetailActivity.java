package com.beanu.l4_bottom_tab.ui.module_news;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.beanu.arad.http.RxHelper;
import com.beanu.arad.utils.StringUtils;
import com.beanu.arad.utils.ToastUtils;
import com.beanu.l2_shareutil.ShareDialogUtil;
import com.beanu.l2_shareutil.share.ShareListener;
import com.beanu.l3_common.model.api.API;
import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.base.BaseSDActivity;
import com.beanu.l4_bottom_tab.model.ApiService;
import com.beanu.l4_bottom_tab.model.bean.NewsItem;
import com.beanu.l4_bottom_tab.util.Subscriber;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 新闻详情
 */
@Route(path = "/app/news/detail")
public class NewsDetailActivity extends BaseSDActivity {

    @BindView(R.id.txt_news_title) TextView mTxtNewsTitle;
    @BindView(R.id.txt_news_auth) TextView mTxtNewsAuth;
    @BindView(R.id.txt_news_date) TextView mTxtNewsDate;
    @BindView(R.id.txt_news_look) TextView mTxtNewsLook;
    @BindView(R.id.webView) WebView mWebView;

    private NewsItem mNewsItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        ButterKnife.bind(this);

        String newsId = getIntent().getStringExtra("newsId");
        http_news_detail(newsId);
    }


    private void http_news_detail(String newsId) {
        API.getInstance(ApiService.class).news_detail(API.createHeader(), newsId)
                .compose(RxHelper.<NewsItem>handleResult())
                .subscribe(new Subscriber<NewsItem>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(NewsItem newsItem) {

                        mNewsItem = newsItem;
                        refreshView(newsItem);
                    }
                });
    }


    private void refreshView(NewsItem newsItem) {
        mTxtNewsTitle.setText(newsItem.getTitle());
        mTxtNewsAuth.setText(newsItem.getAuthor());
        mTxtNewsDate.setText(newsItem.getShowTime());
        mTxtNewsLook.setText(newsItem.getClick() + "");


//        mWebView.setWebViewClient(new WebViewClient() { //调用自身，不调用系统浏览器
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//                return true;
//            }
//        });

        mWebView.setWebChromeClient(new WebChromeClient() {
        });
        mWebView.getSettings().setJavaScriptEnabled(true);
        if (!StringUtils.isEmpty(newsItem.getUrl())) {
            mWebView.loadUrl(newsItem.getUrl());
        }
    }

    @Override
    public String setupToolBarTitle() {
        return "资讯";
    }

    @Override
    public boolean setupToolBarLeftButton(View leftButton) {
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        return true;
    }

    @Override
    public boolean setupToolBarRightButton1(View rightButton1) {
        return setupToolBarRightButton(rightButton1);
    }

    public boolean setupToolBarRightButton(View rightButton) {
        ((ImageView) rightButton).setImageResource(R.drawable.information_share);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareDialogUtil.showShareDialog(NewsDetailActivity.this, mNewsItem.getTitle(), mNewsItem.getAuthor(), mNewsItem.getUrl(), R.mipmap.ic_launcher, new ShareListener() {
                    @Override
                    public void shareSuccess() {

                    }

                    @Override
                    public void shareFailure(Exception e) {
                        e.printStackTrace();
                        ToastUtils.showShort( e.getLocalizedMessage());
                    }

                    @Override
                    public void shareCancel() {

                    }
                });
            }
        });
        return true;
    }
}