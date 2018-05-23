package com.beanu.l4_clean.ui.anchor;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.beanu.arad.http.IPageModel;
import com.beanu.arad.http.RxHelper;
import com.beanu.arad.support.recyclerview.divider.GridLayoutItemDecoration;
import com.beanu.l2_recyclerview.SimplestListActivity;
import com.beanu.l3_common.model.PageModel;
import com.beanu.l3_common.model.api.API;
import com.beanu.l3_common.util.AppHolder;
import com.beanu.l4_clean.R;
import com.beanu.l4_clean.adapter.binder.AnchorViewBinder;
import com.beanu.l4_clean.model.APIService;
import com.beanu.l4_clean.model.bean.Anchor;
import com.beanu.l4_clean.support.dialog.AnchorTipsFragment;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * 主播列表
 */
public class AnchorListActivity extends SimplestListActivity<Anchor> {

    private final Items items = new Items();
    private ImageView mImgLive;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImgLive = findViewById(R.id.img_live);
//        if (AppHolder.getInstance().user.getType() == 1) {
            mImgLive.setVisibility(View.GONE);
//        }
    }

    @Override
    protected void initList() {
        super.initList();
        getRecyclerView().addItemDecoration(new GridLayoutItemDecoration(getResources().getDimensionPixelSize(R.dimen.grid_space), 2));
    }

    @Override
    protected RecyclerView.Adapter<?> provideAdapter() {
        MultiTypeAdapter adapter = new MultiTypeAdapter(items);
        adapter.register(Anchor.class, new AnchorViewBinder());
        return adapter;
    }

    @Override
    protected RecyclerView.LayoutManager provideLayoutManager() {
        return new GridLayoutManager(this, 2);
    }

    @Override
    public Observable<? extends IPageModel<Anchor>> loadData(Map<String, Object> params, int pageIndex) {
        return API.getInstance(APIService.class).anchorList(pageIndex, 30).compose(RxHelper.<PageModel<Anchor>>handleResult());
    }

    @Override
    public void loadDataComplete(List<Anchor> beans) {
        items.clear();
        items.addAll(mPresenter.getList());
        getAdapter().notifyDataSetChanged();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_anchor_list;
    }

    public void onClickToLive(View view) {

        if (AppHolder.getInstance().user.getType() == 0) {
            //普通用户 提示
            AnchorTipsFragment.show(getSupportFragmentManager());
        } else {
            startActivity(AnchorLivingSettingActivity.class);
        }

    }

    @Override
    public String setupToolBarTitle() {
        return "主播带你抓";
    }

}