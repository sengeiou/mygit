package com.beanu.l4_bottom_tab.ui.common;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.beanu.arad.utils.StringUtils;
import com.beanu.l4_bottom_tab.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * 简介说明页面
 */
public class IntroWithWebFragment extends Fragment {

    private static final String ARG_PARAM1 = "url";
    @BindView(R.id.webView) WebView mWebView;
    Unbinder unbinder;

    private String mUrl;


    public IntroWithWebFragment() {
        // Required empty public constructor
    }

    public static IntroWithWebFragment newInstance(String url) {
        IntroWithWebFragment fragment = new IntroWithWebFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUrl = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_intro, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mWebView.setWebViewClient(new WebViewClient() { //调用自身，不调用系统浏览器
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
        });
        mWebView.getSettings().setJavaScriptEnabled(true);
        if (!StringUtils.isEmpty(mUrl)) {
            mWebView.loadUrl(mUrl);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
