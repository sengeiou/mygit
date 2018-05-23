package com.beanu.l4_bottom_tab.mvp.model;

import com.beanu.arad.http.IPageModel;
import com.beanu.arad.http.RxHelper;
import com.beanu.l3_common.model.PageModel;
import com.beanu.l3_common.model.api.API;
import com.beanu.l3_common.util.Constants;
import com.beanu.l4_bottom_tab.model.ApiService;
import com.beanu.l4_bottom_tab.model.bean.Comment;
import com.beanu.l4_bottom_tab.mvp.contract.CommentContract;

import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by Beanu on 2017/04/06
 */

public class CommentModelImpl implements CommentContract.Model {


    @Override
    public Observable<? extends IPageModel<Comment>> loadData(Map<String, Object> params, int pageIndex) {
        int type = 0;
        if (params.get("type") != null) {
            type = (int) params.get("type");
        }

        switch (type) {
            case 0:

            case 1:
                String lessonId = null;
                if (params.get("lessonId") != null) {
                    lessonId = (String) params.get("lessonId");
                }

                return API.getInstance(ApiService.class)
                        .comment_list(API.createHeader(), lessonId, type, pageIndex, Constants.PAGE_SIZE)
                        .compose(RxHelper.<PageModel<Comment>>handleResult());
            case 2:

                return API.getInstance(ApiService.class).book_comment_list(API.createHeader(), params, pageIndex, Constants.PAGE_SIZE)
                        .compose(RxHelper.<PageModel<Comment>>handleResult());
            case 3:
                return API.getInstance(ApiService.class).teacher_comment_list(API.createHeader(), params, pageIndex, Constants.PAGE_SIZE)
                        .compose(RxHelper.<PageModel<Comment>>handleResult());
        }


        return null;
    }
}