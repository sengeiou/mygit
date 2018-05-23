package com.beanu.l4_bottom_tab.ui.module2_liveLesson;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.beanu.arad.base.ToolBarFragment;
import com.beanu.arad.support.recyclerview.adapter.EndlessRecyclerOnScrollListener;
import com.beanu.arad.support.recyclerview.adapter.LoadMoreAdapterWrapper;
import com.beanu.arad.support.recyclerview.divider.HorizontalDividerItemDecoration;
import com.beanu.l3_common.util.AppHolder;
import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.adapter.provider.BannerViewProvider;
import com.beanu.l4_bottom_tab.adapter.provider.LiveLessonViewProvider;
import com.beanu.l4_bottom_tab.adapter.provider.Live_MyCourse;
import com.beanu.l4_bottom_tab.adapter.provider.Live_MyCourseViewProvider;
import com.beanu.l4_bottom_tab.model.bean.Banner;
import com.beanu.l4_bottom_tab.model.bean.BannerItem;
import com.beanu.l4_bottom_tab.model.bean.LiveLesson;
import com.beanu.l4_bottom_tab.mvp.contract.LiveLessonListContract;
import com.beanu.l4_bottom_tab.mvp.model.LiveLessonListModelImpl;
import com.beanu.l4_bottom_tab.mvp.presenter.LiveLessonListPresenterImpl;

import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * 直播课live lesson
 */
public class Fragment2 extends ToolBarFragment<LiveLessonListPresenterImpl, LiveLessonListModelImpl> implements LiveLessonListContract.View {

    @BindView(R.id.recycle_view) RecyclerView mRecycleView;
    @BindView(R.id.toolbar) Toolbar mToolbar;

    private Items items;
    private MultiTypeAdapter adapter;
    private LoadMoreAdapterWrapper mAdapterWrapper;
    private Banner mBanner;

    public Fragment2() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new MultiTypeAdapter();
        adapter.register(Banner.class, new BannerViewProvider());
        adapter.register(Live_MyCourse.class, new Live_MyCourseViewProvider());
        adapter.register(LiveLesson.class, new LiveLessonViewProvider());

        items = new Items();

        mBanner = new Banner();
        mBanner.setItemList(mPresenter.getBannerList());
        items.add(mBanner);
        items.add(new Live_MyCourse());
        items.addAll(mPresenter.getList());

        adapter.setItems(items);

        //请求数据
        mPresenter.requestHeaderBanner(1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_2, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //设置toolbar在4.4版本以下的高度
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            int height = getResources().getDimensionPixelSize(R.dimen.toolbar);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
            mToolbar.setLayoutParams(layoutParams);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecycleView.setLayoutManager(linearLayoutManager);
        mAdapterWrapper = new LoadMoreAdapterWrapper(getActivity(), adapter, mPresenter);
        mRecycleView.setAdapter(mAdapterWrapper);
        mRecycleView.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager, mPresenter) {
            @Override
            public void onLoadMore() {
                mPresenter.loadDataNext();
            }
        });
        mRecycleView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).color(getResources().getColor(android.R.color.transparent)).sizeResId(R.dimen.list_margin).build());

        //第一次加载数据
        if (mPresenter.getList().size() == 0) {

            ArrayMap<String, Object> params = new ArrayMap<>();
            params.put("subjectId", AppHolder.getInstance().user.getSubjectId());
            mPresenter.initLoadDataParams(params);

            mPresenter.loadDataFirst();
        }
    }

    @Override
    public String setupToolBarTitle() {
        return "直播课";
    }

    @Override
    public void loadDataComplete(List<LiveLesson> beans) {

        //先清空LiveLesson数据
        Iterator<Object> iterator = items.iterator();
        while (iterator.hasNext()) {
            Object item = iterator.next();
            if (item instanceof LiveLesson) {
                iterator.remove();
            }
        }

        //添加新数据
        items.addAll(beans);

        adapter.notifyDataSetChanged();
    }

    @Override
    public void refreshHeaderBanner(List<BannerItem> list) {

        mBanner.setItemList(mPresenter.getBannerList());
        adapter.notifyItemChanged(0);

//        ItemViewBinder provider = adapter.getProviderByClass(Banner.class);
//
//        if (provider instanceof BannerViewProvider) {
////            BannerViewPagerAdapter viewPagerAdapter = ((BannerViewProvider) provider).getBannerViewPagerAdapter();
////            if (viewPagerAdapter != null) {
////                viewPagerAdapter.notifyDataSetChanged();
////            }
//        }
    }
}
