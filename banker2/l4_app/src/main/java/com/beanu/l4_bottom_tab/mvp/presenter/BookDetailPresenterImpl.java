package com.beanu.l4_bottom_tab.mvp.presenter;


import com.beanu.l3_common.model.PageModel;
import com.beanu.l4_bottom_tab.model.bean.BookImg;
import com.beanu.l4_bottom_tab.model.bean.Comment;
import com.beanu.l4_bottom_tab.mvp.contract.BookDetailContract;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Beanu on 2017/05/10
 */

public class BookDetailPresenterImpl extends BookDetailContract.Presenter {


    private List<BookImg> mBookImgList =  new ArrayList<>();;
    private List<Comment> mCommentList = new ArrayList<>();


    @Override
    public void requestData(String bookId) {
        //图书相册
        mModel.book_img_list(bookId).subscribe(new Observer<List<BookImg>>() {


            @Override
            public void onComplete() {
                mView.refreshBookImg();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onSubscribe(Disposable d) {
                mRxManage.add(d);
            }

            @Override
            public void onNext(List<BookImg> bookImgs) {
                mBookImgList.clear();
                mBookImgList.addAll(bookImgs);
            }
        });

        //图书评论
        mModel.book_comment_list(bookId).subscribe(new Observer<PageModel<Comment>>() {
            @Override
            public void onComplete() {
                mView.refreshBookComment();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onSubscribe(Disposable d) {
                mRxManage.add(d);
            }

            @Override
            public void onNext(PageModel<Comment> commentPageModel) {
                mCommentList.clear();
                mCommentList.addAll(commentPageModel.dataList);
            }
        });

    }

    public List<BookImg> getBookImgList() {
        return mBookImgList;
    }

    public List<Comment> getCommentList() {
        return mCommentList;
    }
}