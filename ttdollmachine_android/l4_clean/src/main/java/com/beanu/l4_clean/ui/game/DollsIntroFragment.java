package com.beanu.l4_clean.ui.game;


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
import com.beanu.l4_clean.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 娃娃web介绍
 */
public class DollsIntroFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";

    @BindView(R.id.webView) WebView mWebView;
    Unbinder unbinder;

    private String mURL;

    public DollsIntroFragment() {
        // Required empty public constructor
    }

    public static DollsIntroFragment newInstance(String param1) {
        DollsIntroFragment fragment = new DollsIntroFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mURL = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dolls_intro, container, false);
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
        if (!StringUtils.isEmpty(mURL)) {
            mWebView.loadUrl(mURL);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}