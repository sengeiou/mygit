package com.beanu.l4_bottom_tab.ui.module5_my.online_lesson;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beanu.arad.support.recyclerview.divider.GridLayoutItemDecoration;
import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.adapter.provider.OnlineLessonViewProvider;
import com.beanu.l4_bottom_tab.base.BaseSDActivity;
import com.beanu.l4_bottom_tab.model.bean.OnlineLesson;
import com.beanu.l4_bottom_tab.mvp.contract.MyOnlineLessonContract;
import com.beanu.l4_bottom_tab.mvp.model.MyOnlineLessonModelImpl;
import com.beanu.l4_bottom_tab.mvp.presenter.MyOnlineLessonPresenterImpl;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;


/**
 * 我的网课
 */
public class MyOnlineLessonActivity extends BaseSDActivity<MyOnlineLessonPresenterImpl, MyOnlineLessonModelImpl> implements MyOnlineLessonContract.View {

    @BindView(R.id.txt_continue) TextView mTxtContinue;
    @BindView(R.id.recycle_view) RecyclerView mRecycleView;
    @BindView(R.id.txt_lesson_detail) TextView mTxtLessonDetail;
    @BindView(R.id.img_close) ImageView mImgClose;
    @BindView(R.id.rl_continue) RelativeLayout mRlContinue;

    Items mItems;
    MultiTypeAdapter mMultiAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_online_lesson);
        ButterKnife.bind(this);

        mItems = new Items();
        mMultiAdapter = new MultiTypeAdapter(mItems);
        mMultiAdapter.register(OnlineLesson.class, new OnlineLessonViewProvider());

        //定义recycle view 样式
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mRecycleView.setLayoutManager(gridLayoutManager);
        mRecycleView.addItemDecoration(new GridLayoutItemDecoration(8, 2));
        mRecycleView.setAdapter(mMultiAdapter);

//        //上拉监听
//        mRecycleView.addOnScrollListener(new EndlessRecyclerOnScrollListener(gridLayoutManager, mPresenter) {
//            @Override
//            public void onLoadMore() {
//                mPresenter.loadDataNext();
//            }
//        });

        //请求数据
        mPresenter.myOnlineLesson();
    }

    @Override
    public String setupToolBarTitle() {
        return "我的网课";
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
    public void refreshUI(List<OnlineLesson> result) {
        mItems.clear();
        mItems.addAll(result);

        mMultiAdapter.notifyDataSetChanged();
    }
}
