package com.beanu.l4_bottom_tab.ui.module4_book;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beanu.arad.base.ToolBarFragment;
import com.beanu.arad.widget.LinearLayoutForListView;
import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.adapter.BookImgAdapter;
import com.beanu.l4_bottom_tab.model.bean.BookItem;
import com.beanu.l4_bottom_tab.model.bean.Comment;
import com.beanu.l4_bottom_tab.mvp.contract.BookDetailContract;
import com.beanu.l4_bottom_tab.mvp.model.BookDetailModelImpl;
import com.beanu.l4_bottom_tab.mvp.presenter.BookDetailPresenterImpl;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * 图书详情
 */
public class BookDetailFragment extends ToolBarFragment<BookDetailPresenterImpl, BookDetailModelImpl> implements BookDetailContract.View {

    private static final String ARG_PARAM1 = "param1";

    @BindView(R.id.viewPager) ViewPager mViewPager;
   // @BindView(R.id.page_indicator) CirclePageIndicator mPageIndicator;
    @BindView(R.id.txt_book_name) TextView mTxtBookName;
    @BindView(R.id.txt_book_price) TextView mTxtBookPrice;
    @BindView(R.id.txt_book_express) TextView mTxtBookExpress;
    @BindView(R.id.txt_book_sale) TextView mTxtBookSale;
    @BindView(R.id.txt_header_total) TextView mTxtHeaderTotal;
    @BindView(R.id.ratingBar) RatingBar mRatingBar;
    @BindView(R.id.list_view) LinearLayoutForListView mListView;
    @BindView(R.id.relativeLayout) RelativeLayout mRelativeLayout;
    Unbinder unbinder;

    private BookItem mBookItem;

    public BookDetailFragment() {
    }

    public static BookDetailFragment newInstance(BookItem bookItem) {
        BookDetailFragment fragment = new BookDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, bookItem);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mBookItem = getArguments().getParcelable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mPresenter.getBookImgList().size() == 0 && mBookItem != null) {
            mPresenter.requestData(mBookItem.getId());
        }
        refreshUI();
    }

    private void refreshUI() {
        if (mBookItem != null) {
            mTxtBookName.setText(mBookItem.getName());
            mTxtBookPrice.setText("¥" + mBookItem.getPrice());
            mTxtBookExpress.setText("快递：" + mBookItem.getPostage());
            mTxtBookSale.setText("月销量：" + mBookItem.getSold_num());

            mTxtHeaderTotal.setText(String.format("共有%s条评论", mBookItem.getEvaluate_num()));
            mRatingBar.setRating(mBookItem.getStar_rating());

        }

    }

    @Override
    public void refreshBookImg() {

        BookImgAdapter adapter = new BookImgAdapter(getActivity(), mRelativeLayout);
        adapter.changeData(mPresenter.getBookImgList());
        mViewPager.setAdapter(adapter);
       // mPageIndicator.setViewPager(mViewPager);
    }

    @Override
    public void refreshBookComment() {
        BookHotCommentAdapter adapter = new BookHotCommentAdapter(getActivity(), mPresenter.getCommentList());
        mListView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * 图书 热门评论
     */
    public class BookHotCommentAdapter extends BaseAdapter {
        private Context context;
        private List<Comment> list;


        public BookHotCommentAdapter(Context context, List<Comment> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView != null) {
                viewHolder = (ViewHolder) convertView.getTag();
            } else {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_comment, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            }
            fillValue(position, viewHolder);
            return convertView;
        }

        private void fillValue(int position, ViewHolder viewHolder) {
            Comment comment = list.get(position);
            viewHolder.mTxtName.setText(comment.getNickName());
            viewHolder.mTxtContent.setText(comment.getContent());
            viewHolder.mTxtDate.setText(comment.getCreatetime());
            viewHolder.mRatingBar.setRating(comment.getStar_rating());

        }

        class ViewHolder {

            @BindView(R.id.txt_name) TextView mTxtName;
            @BindView(R.id.ratingBar) RatingBar mRatingBar;
            @BindView(R.id.txt_date) TextView mTxtDate;
            @BindView(R.id.txt_content) TextView mTxtContent;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }

}
