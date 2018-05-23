package com.beanu.l4_clean.ui.user;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.beanu.arad.http.IPageModel;
import com.beanu.arad.http.RxHelper;
import com.beanu.arad.support.recyclerview.divider.HorizontalDividerItemDecoration;
import com.beanu.l2_recyclerview.SimplestListFragment;
import com.beanu.l3_common.model.PageModel;
import com.beanu.l3_common.model.api.API;
import com.beanu.l3_common.util.Constants;
import com.beanu.l4_clean.R;
import com.beanu.l4_clean.adapter.binder.MessageViewBinder;
import com.beanu.l4_clean.model.APIService;
import com.beanu.l4_clean.model.bean.Message;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * 我的消息列表
 */
public class MyMessageFragment extends SimplestListFragment<Message> {

    private static final String ARG_PARAM1 = "param1";

    private int mParam1;
    private Items mItems = new Items();

    public MyMessageFragment() {
        // Required empty public constructor
    }

    public static MyMessageFragment newInstance(int position) {
        MyMessageFragment fragment = new MyMessageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getRecyclerView().addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).colorResId(android.R.color.transparent).sizeResId(R.dimen.grid_space).build());
    }

    @Override
    protected RecyclerView.Adapter<?> provideAdapter() {
        MultiTypeAdapter adapter = new MultiTypeAdapter(mItems);
        adapter.register(Message.class, new MessageViewBinder());
        return adapter;
    }

    @Override
    public Observable<? extends IPageModel<Message>> loadData(Map<String, Object> params, int pageIndex) {

        return API.getInstance(APIService.class).messageList(pageIndex, Constants.AmountOfPrePage).compose(RxHelper.<PageModel<Message>>handleResult());
    }

    @Override
    public void loadDataComplete(List<Message> beans) {
        mItems.clear();
        mItems.addAll(mPresenter.getList());
        getAdapter().notifyDataSetChanged();
    }
}
