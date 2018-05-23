package com.beanu.l4_bottom_tab.mvp.contract;

import com.beanu.arad.base.BaseModel;
import com.beanu.arad.base.BasePresenter;
import com.beanu.arad.base.BaseView;
import com.beanu.l3_common.model.PageModel;
import com.beanu.l4_bottom_tab.model.bean.BookImg;
import com.beanu.l4_bottom_tab.model.bean.Comment;

import java.util.List;

import io.reactivex.Observable;


/**
 * 图书详情
 * Created by Beanu on 2017/5/10.
 */

public interface BookDetailContract {

    public interface View extends BaseView {

        public void refreshBookImg();

        public void refreshBookComment();
    }

    public abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void requestData(String bookId);
    }

    public interface Model extends BaseModel {
        Observable<List<BookImg>> book_img_list(String bookId);

        Observable<PageModel<Comment>> book_comment_list(String bookId);
    }

}