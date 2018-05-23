package com.beanu.l4_bottom_tab.mvp.contract;

import com.beanu.arad.support.recyclerview.loadmore.ILoadMoreModel;
import com.beanu.arad.support.recyclerview.loadmore.ILoadMoreView;
import com.beanu.arad.support.recyclerview.loadmore.LoadMorePresenterImpl;
import com.beanu.l4_bottom_tab.model.bean.BookItem;
import com.beanu.l4_bottom_tab.model.bean.Subject;

import java.util.List;

import io.reactivex.Observable;

/**
 * 图书列表
 * Created by Beanu on 2017/3/9.
 */

public interface BookListContract {


    public interface View extends ILoadMoreView<BookItem> {
        public void refreshSubjectList(List<Subject> subjectList);

        public void refreshCartNum(int number);
    }

    public abstract class Presenter extends LoadMorePresenterImpl<BookItem, View, Model> {

        public abstract void requestSubjectList();

        public abstract void requestCartNum();
    }

    public interface Model extends ILoadMoreModel<BookItem> {
        Observable<List<Subject>> requestSubjectList();

        Observable<Integer> requestCartNum();
    }


}