package com.beanu.l4_clean.mvp.contract;

import com.beanu.arad.support.recyclerview.loadmore.ILoadMoreModel;
import com.beanu.arad.support.recyclerview.loadmore.ILoadMoreView;
import com.beanu.arad.support.recyclerview.loadmore.LoadMorePresenterImpl;
import com.beanu.l3_common.model.PageModel;
import com.beanu.l4_clean.model.bean.BannerItem;
import com.beanu.l4_clean.model.bean.Dolls;

import java.util.List;

import io.reactivex.Observable;

/**
 * 机器列表
 * Created by Beanu on 2017/12/5.
 */

public interface GameMachineListContract {

    public interface View extends ILoadMoreView<Dolls> {
        public void uiRefreshBanner(List<BannerItem> list);

        public void uiRefreshTOP(List<Dolls> pageModel);
    }

    public abstract class Presenter extends LoadMorePresenterImpl<Dolls, View, Model> {
        public abstract void bannerList();

        public abstract void topDolls();
    }

    public interface Model extends ILoadMoreModel<Dolls> {
        Observable<List<BannerItem>> bannerList();

        Observable<PageModel<Dolls>> topDolls();
    }


}