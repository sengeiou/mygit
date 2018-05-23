package com.beanu.l4_bottom_tab.ui.module4_book;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.base.ToolBarFragment;
import com.beanu.arad.support.recyclerview.adapter.EndlessRecyclerOnScrollListener;
import com.beanu.arad.support.recyclerview.divider.HorizontalDividerItemDecoration;
import com.beanu.l3_common.model.bean.EventModel;
import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.adapter.BookListAdapter;
import com.beanu.l4_bottom_tab.adapter.ListDropDownAdapter;
import com.beanu.l4_bottom_tab.model.bean.BookItem;
import com.beanu.l4_bottom_tab.model.bean.Subject;
import com.beanu.l4_bottom_tab.mvp.contract.BookListContract;
import com.beanu.l4_bottom_tab.mvp.model.BookListModelImpl;
import com.beanu.l4_bottom_tab.mvp.presenter.BookListPresenterImpl;
import com.yyydjk.library.DropDownMenu;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 图书商城
 */
public class Fragment4 extends ToolBarFragment<BookListPresenterImpl, BookListModelImpl> implements BookListContract.View {

    @BindView(R.id.dropDownMenu) DropDownMenu mDropDownMenu;
    @BindView(R.id.txt_cart_num) TextView mTxtCartNum;

    RecyclerView mRecycleView;

    private String headers[] = {"学科", "排序", "价格"};
    private List<View> popupViews = new ArrayList<>();

    private List<String> mNameSubject = new ArrayList<>();
    private List<String> mNameSort = new ArrayList<>();
    private List<String> mNamePrice = new ArrayList<>();
    private List<Subject> subjectList = new ArrayList<>();

    private ListDropDownAdapter mSubjectAdapter;
    private ListDropDownAdapter mSortAdapter;
    private ListDropDownAdapter mPriceAdapter;

    //recycle view
    BookListAdapter mBookListAdapter;

    public Fragment4() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBookListAdapter = new BookListAdapter(getActivity(), mPresenter.getList());
        Arad.bus.register(this);

        mNameSubject.add("不限");

        mNameSort.add("默认");
        mNameSort.add("上架时间");

        mNamePrice.add("默认");
        mNamePrice.add("从高到低");
        mNamePrice.add("从低到高");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_4, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ListView cityView = new ListView(getActivity());
        mSubjectAdapter = new ListDropDownAdapter(getActivity(), 0, mNameSubject);
        cityView.setDividerHeight(0);
        cityView.setAdapter(mSubjectAdapter);

        final ListView ageView = new ListView(getActivity());
        ageView.setDividerHeight(0);
        mPriceAdapter = new ListDropDownAdapter(getActivity(), 1, mNameSort);
        ageView.setAdapter(mPriceAdapter);


        //init sex menu
        final ListView sexView = new ListView(getActivity());
        sexView.setDividerHeight(0);
        mSortAdapter = new ListDropDownAdapter(getActivity(), 2, mNamePrice);
        sexView.setAdapter(mSortAdapter);

        popupViews.clear();
        popupViews.add(cityView);
        popupViews.add(ageView);
        popupViews.add(sexView);

        mRecycleView = new RecyclerView(getActivity());
        mDropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews, mRecycleView);

        //recycle view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecycleView.setLayoutManager(linearLayoutManager);
        mRecycleView.setAdapter(mBookListAdapter);
        mRecycleView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).colorResId(R.color.color_line).build());

        //上拉监听
        mRecycleView.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager, mPresenter) {
            @Override
            public void onLoadMore() {
                mPresenter.loadDataNext();
            }
        });

        //第一次加载数据
        if (mPresenter.getList().size() == 0) {
            mPresenter.loadDataFirst();
        }

        //请求学科列表
        if (mNameSubject.size() == 1) {
            mPresenter.requestSubjectList();
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        //请求购物车数量
        mPresenter.requestCartNum();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Arad.bus.unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventModel.BookFilterEvent event) {

        ArrayMap<String, Object> params = new ArrayMap<>();

        int position = event.position;
        switch (event.type) {
            case 0:

                if (position > 0) {
                    String id = subjectList.get(position - 1).getId();
                    params.put("subjectId", id);
                }

                mSubjectAdapter.setCheckItem(position);
                mDropDownMenu.setTabText(position == 0 ? headers[0] : mNameSubject.get(position));

                break;
            case 1:

                if (position > 0) {
                    params.put("showTimeSort", "asc");
                }

                mDropDownMenu.setTabText(position == 0 ? headers[1] : mNameSort.get(position));
                mPriceAdapter.setCheckItem(position);
                break;
            case 2:

                String desc2 = null;
                if (position == 0) {
                    desc2 = null;
                } else if (position == 1) {
                    desc2 = "desc";
                } else {
                    desc2 = "asc";
                }

                params.put("priceSort", desc2);
                mDropDownMenu.setTabText(position == 0 ? headers[2] : mNamePrice.get(position));
                mSortAdapter.setCheckItem(position);
                break;
        }

        mPresenter.initLoadDataParams(params);
        mPresenter.loadDataFirst();
        mDropDownMenu.closeMenu();
    }

    @Override
    public void loadDataComplete(List<BookItem> beans) {
        mBookListAdapter.notifyDataSetChanged();
    }

    @Override
    public String setupToolBarTitle() {
        return "图书";
    }

    @Override
    public boolean setupToolBarRightButton1(View rightButton1) {
        rightButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(ShoppingCartActivity.class);
            }
        });
        return false;
    }


    @Override
    public void refreshSubjectList(List<Subject> subjectList) {

        this.subjectList.clear();
        this.subjectList.addAll(subjectList);

        for (Subject subject : subjectList) {
            mNameSubject.add(subject.getName());
        }
        mSubjectAdapter.notifyDataSetChanged();
    }

    @Override
    public void refreshCartNum(int number) {
        if (number > 0) {
            mTxtCartNum.setText(number + "");
            mTxtCartNum.setVisibility(View.GONE);
        } else {
            mTxtCartNum.setVisibility(View.GONE);
        }

    }
}
