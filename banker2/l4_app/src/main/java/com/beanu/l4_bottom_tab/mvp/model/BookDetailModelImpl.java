package com.beanu.l4_bottom_tab.mvp.model;

import android.support.v4.util.ArrayMap;

import com.beanu.arad.http.RxHelper;
import com.beanu.l3_common.model.PageModel;
import com.beanu.l3_common.model.api.API;
import com.beanu.l3_common.util.Constants;
import com.beanu.l4_bottom_tab.model.ApiService;
import com.beanu.l4_bottom_tab.model.bean.BookImg;
import com.beanu.l4_bottom_tab.model.bean.Comment;
import com.beanu.l4_bottom_tab.mvp.contract.BookDetailContract;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Beanu on 2017/05/10
 */

public class BookDetailModelImpl implements BookDetailContract.Model {

    @Override
    public Observable<List<BookImg>> book_img_list(String bookId) {
        return API.getInstance(ApiService.class)
                .book_img_list(API.createHeader(), bookId)
                .compose(RxHelper.<List<BookImg>>handleResult());
    }

    @Override
    public Observable<PageModel<Comment>> book_comment_list(String bookId) {

        ArrayMap<String, Object> params = new ArrayMap<>();
        params.put("id", bookId);

        return API.getInstance(ApiService.class)
                .book_comment_list(API.createHeader(), params, 1, Constants.PAGE_SIZE)
                .compose(RxHelper.<PageModel<Comment>>handleResult());
    }
}