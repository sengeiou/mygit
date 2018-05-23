package com.beanu.l4_bottom_tab.mvp.model;

import com.beanu.arad.http.IPageModel;
import com.beanu.arad.http.RxHelper;
import com.beanu.l3_common.model.PageModel;
import com.beanu.l3_common.model.api.API;
import com.beanu.l3_common.util.Constants;
import com.beanu.l4_bottom_tab.model.ApiService;
import com.beanu.l4_bottom_tab.model.bean.BookItem;
import com.beanu.l4_bottom_tab.model.bean.Subject;
import com.beanu.l4_bottom_tab.mvp.contract.BookListContract;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by Beanu on 2017/03/09
 */

public class BookListModelImpl implements BookListContract.Model {

    @Override
    public Observable<? extends IPageModel<BookItem>> loadData(Map<String, Object> params, int pageIndex) {

        return API.getInstance(ApiService.class).book_list(API.createHeader(), params, pageIndex, Constants.PAGE_SIZE)
                .compose(RxHelper.<PageModel<BookItem>>handleResult());

    }

    @Override
    public Observable<List<Subject>> requestSubjectList() {
        return API.getInstance(ApiService.class).subject_list(API.createHeader())
                .compose(RxHelper.<List<Subject>>handleResult());
    }

    @Override
    public Observable<Integer> requestCartNum() {
        return API.getInstance(ApiService.class)
                .numOfShopCart(API.createHeader())
                .compose(RxHelper.<Integer>handleResult());
    }
}