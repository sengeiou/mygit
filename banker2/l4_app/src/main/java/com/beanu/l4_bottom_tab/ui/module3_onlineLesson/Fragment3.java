package com.beanu.l4_bottom_tab.ui.module3_onlineLesson;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.beanu.arad.Arad;
import com.beanu.arad.base.ToolBarFragment;
import com.beanu.arad.support.recyclerview.adapter.EndlessRecyclerOnScrollListener;
import com.beanu.arad.support.recyclerview.adapter.LoadMoreAdapterWrapper;
import com.beanu.l3_common.model.bean.EventModel;
import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.adapter.provider.BannerViewProvider;
import com.beanu.l4_bottom_tab.adapter.provider.OnlineLessonViewProvider;
import com.beanu.l4_bottom_tab.adapter.provider.Online_HotLesson;
import com.beanu.l4_bottom_tab.adapter.provider.Online_HotLessonViewProvider;
import com.beanu.l4_bottom_tab.model.bean.Banner;
import com.beanu.l4_bottom_tab.model.bean.BannerItem;
import com.beanu.l4_bottom_tab.model.bean.OnlineLesson;
import com.beanu.l4_bottom_tab.mvp.contract.OnlineLessonListContract;
import com.beanu.l4_bottom_tab.mvp.model.OnlineLessonListModelImpl;
import com.beanu.l4_bottom_tab.mvp.presenter.OnlineLessonListPresenterImpl;
import com.beanu.l4_bottom_tab.support.decoration.GridLayoutOLDecoration;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;


/**
 * 高清网课
 */
public class Fragment3 extends ToolBarFragment<OnlineLessonListPresenterImpl, OnlineLessonListModelImpl> implements OnlineLessonListContract.View {

    @BindView(R.id.recycle_view) RecyclerView mRecycleView;
    @BindView(R.id.toolbar) Toolbar mToolbar;

    private Items items;
    private MultiTypeAdapter mMultiAdapter;
    private LoadMoreAdapterWrapper mAdapterWrapper;
    private Banner mBanner;

    public Fragment3() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        items = new Items();

        mMultiAdapter = new MultiTypeAdapter();
        mMultiAdapter.register(Banner.class, new BannerViewProvider());
        mMultiAdapter.register(Online_HotLesson.class, new Online_HotLessonViewProvider());
        mMultiAdapter.register(OnlineLesson.class, new OnlineLessonViewProvider());

        mBanner = new Banner();
        mBanner.setItemList(mPresenter.getBannerList());
        items.add(mBanner);

        mMultiAdapter.setItems(items);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_3, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Arad.bus.register(this);
        //设置toolbar在4.4版本以下的高度
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            int height = getResources().getDimensionPixelSize(R.dimen.toolbar);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
            mToolbar.setLayoutParams(layoutParams);
        }


        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {

                if (items.size() > position) {
                    if (items.get(position) instanceof OnlineLesson)
                        return 1;
                }
                return 2;

            }
        });
        mRecycleView.addItemDecoration(new GridLayoutOLDecoration(16, 2));
        mRecycleView.setLayoutManager(gridLayoutManager);
        mAdapterWrapper = new LoadMoreAdapterWrapper(getActivity(), mMultiAdapter, mPresenter);
        mRecycleView.setAdapter(mAdapterWrapper);
        mRecycleView.addOnScrollListener(new EndlessRecyclerOnScrollListener(gridLayoutManager, mPresenter) {
            @Override
            public void onLoadMore() {
                mPresenter.loadDataNext();
            }
        });

        //刷新头部数据
        refreshHeaderBanner(mPresenter.getBannerList());

        //第一次加载数据
        if (mPresenter.getList().size() == 0) {
//            mPresenter.loadDataFirst();
            mPresenter.requestHttpData(2);
        }


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventModel.ChangeSubject item) {

//        Iterator iterator = items.iterator();
//        while (iterator.hasNext()) {
//            Object object = iterator.next();
//            if (object instanceof OnlineLesson) {
//                iterator.remove();
//            }
//        }

        mPresenter.requestHttpData(2);
//        mPresenter.loadDataFirst();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Arad.bus.unregister(this);
    }

    @Override
    public String setupToolBarTitle() {
        return "高清网课";
    }


    @Override
    public void loadDataComplete(List<OnlineLesson> beans) {

        //先清空LiveLesson数据
//        Iterator<Object> iterator = items.iterator();
//        while (iterator.hasNext()) {
//            Object item = iterator.next();
//            if (item instanceof OnlineLesson) {
//                iterator.remove();
//            }
//        }

        //添加新数据
        items.addAll(beans);

        mMultiAdapter.notifyDataSetChanged();
    }


    @Override
    public void refreshHeaderBanner(List<BannerItem> list) {
        mBanner.setItemList(mPresenter.getBannerList());
        mMultiAdapter.notifyItemChanged(0);
    }

    @Override
    public void refreshHotLesson(boolean show, Online_HotLesson hotLesson) {
        items.clear();
        items.add(mBanner);

        if (show) {

            items.add(hotLesson);
//            mMultiAdapter.notifyItemInserted(1);
        }
    }
}
