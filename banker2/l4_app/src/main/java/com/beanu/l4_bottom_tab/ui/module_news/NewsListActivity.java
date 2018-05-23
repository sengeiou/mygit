package com.beanu.l4_bottom_tab.ui.module_news;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.http.RxHelper;
import com.beanu.l3_common.model.api.API;
import com.beanu.l3_common.util.AppHolder;
import com.beanu.l3_common.util.Constants;
import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.adapter.NewsFragmentAdapter;
import com.beanu.l4_bottom_tab.base.BaseSDActivity;
import com.beanu.l4_bottom_tab.model.ApiService;
import com.beanu.l4_bottom_tab.model.bean.NewsTitle;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.beanu.l4_bottom_tab.util.Subscriber;

public class NewsListActivity extends BaseSDActivity {

    private final static int PICKER_PROVINCE_CODE = 1;
    @BindView(R.id.tab_layout_news) TabLayout mTabLayoutNews;
    @BindView(R.id.viewPager_news) ViewPager mViewPagerNews;
    @BindView(R.id.rightbtn) TextView mTxtRight;

    NewsFragmentAdapter mNewsFragmentAdapter;
    List<NewsTitle> mNewsTitleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);
        ButterKnife.bind(this);

        String provinceId = Arad.preferences.getString(Constants.P_PROVINCE_ID);
        String provinceName = Arad.preferences.getString(Constants.P_PROVINCE_NAME);
        mNewsTitleList = new ArrayList<>();

        mNewsFragmentAdapter = new NewsFragmentAdapter(getSupportFragmentManager(), mNewsTitleList);
        if (!TextUtils.isEmpty(provinceId) && !TextUtils.isEmpty(provinceName)) {
            mNewsFragmentAdapter.setProvinceId(provinceId);
            mTxtRight.setText(provinceName);
        }

        mViewPagerNews.setAdapter(mNewsFragmentAdapter);
        mTabLayoutNews.setupWithViewPager(mViewPagerNews);
        mTabLayoutNews.setTabMode(TabLayout.MODE_SCROLLABLE);

        //请求网络
        String subjectId = AppHolder.getInstance().user.getSubjectId();
        API.getInstance(ApiService.class).news_type_list(API.createHeader(), subjectId)
                .compose(RxHelper.<List<NewsTitle>>handleResult())
                .subscribe(new Subscriber<List<NewsTitle>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<NewsTitle> list) {
                        if (mTabLayoutNews != null) {
                            mNewsTitleList.clear();
                            mNewsTitleList.addAll(list);

                            mNewsFragmentAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICKER_PROVINCE_CODE) {
            String provinceId = data.getStringExtra("provinceId");
            String provinceName = data.getStringExtra("provinceName");
            mTxtRight.setText(provinceName);

            Arad.preferences.putString(Constants.P_PROVINCE_ID, provinceId);
            Arad.preferences.putString(Constants.P_PROVINCE_NAME, provinceName);
            Arad.preferences.flush();

            mNewsFragmentAdapter.setProvinceId(provinceId);
            mNewsFragmentAdapter.notifyDataSetChanged();
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

    @OnClick(R.id.rightbtn)
    public void onViewClicked() {
        Intent intent = new Intent(NewsListActivity.this, PickerProvinceActivity.class);
        startActivityForResult(intent, PICKER_PROVINCE_CODE);
    }
}
