package com.beanu.l4_bottom_tab.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.beanu.l4_bottom_tab.model.bean.BookItem;
import com.beanu.l4_bottom_tab.ui.common.CommentFragment;
import com.beanu.l4_bottom_tab.ui.common.IntroWithWebFragment;
import com.beanu.l4_bottom_tab.ui.module4_book.BookDetailFragment;

/**
 * 图书详情  页面
 * Created by Beanu on 2017/3/7.
 */

public class BookFragmentAdapter extends FragmentStatePagerAdapter {

    String[] mTitleList = new String[]{"商品", "详情", "评论"};
    BookItem mBookItem;

    public BookFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setData(BookItem bookItem) {
        this.mBookItem = bookItem;
    }

    @Override
    public Fragment getItem(int position) {

        if (position == 1) {
            return IntroWithWebFragment.newInstance(mBookItem.getDetailUrl());
        } else if (position == 2) {
//            double rate = 0;
//            if (mBookItem.getEvaluate_num() > 0) {
//                rate = mBookItem.getStar_rating() / mBookItem.getEvaluate_num();
//            }
            return CommentFragment.newInstance(mBookItem.getId(), 2, mBookItem.getEvaluate_num(), mBookItem.getStar_rating());
        }

        return BookDetailFragment.newInstance(mBookItem);
    }

    @Override
    public int getCount() {
        if (mBookItem == null) {
            return 0;
        }
        return mTitleList.length;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return mTitleList[position];
    }


}
