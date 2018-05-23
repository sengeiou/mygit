package com.beanu.l4_bottom_tab.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.beanu.l3_common.adapter.BaseLoadMoreAdapter;
import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.model.bean.BookItem;
import com.beanu.l4_bottom_tab.ui.module4_book.BookDetailActivity;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 图书  列表适配器
 * Created by Beanu on 2016/12/16.
 */

public class BookListAdapter extends BaseLoadMoreAdapter<BookItem, BookListAdapter.ViewHolder> {

    public BookListAdapter(Context context, List<BookItem> list) {
        super(context, list);
    }

    @Override
    public ViewHolder onCreateItemViewHolder(ViewGroup parent) {
        return new ViewHolder(inflater.inflate(R.layout.item_books, parent, false));
    }

    @Override
    public void onBindItemViewHolder(ViewHolder holder, final int position) {
        holder.bind(getItem(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, BookDetailActivity.class);
                intent.putExtra("bookId", getItem(position).getId());
                mContext.startActivity(intent);
            }
        });


    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_book_face) ImageView mImgBookFace;
        @BindView(R.id.txt_book_title) TextView mTxtBookTitle;
        @BindView(R.id.txt_book_concern) TextView mTxtBookConcern;
        @BindView(R.id.rate_bar_book) RatingBar mRateBarBook;
        @BindView(R.id.txt_book_price) TextView mTxtBookPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bind(BookItem item) {
            if (!TextUtils.isEmpty(item.getList_img())) {
                Glide.with(mContext).load(item.getList_img()).into(mImgBookFace);
            }
            mTxtBookTitle.setText(item.getName());
            mTxtBookConcern.setText(item.getPress());
            mRateBarBook.setRating(item.getStar_rating());
//            mTxtBookPrice.setText("¥" + item.getPrice());
            mTxtBookPrice.setText(Html.fromHtml("<small>¥</small><font>" + item.getPrice() + "</font>"));

//            if (item.getEvaluate_num() > 0) {
//                mRateBarBook.setRating(item.getStar_rating() / item.getEvaluate_num());
//            } else {
            mRateBarBook.setRating(item.getStar_rating());
//            }
        }
    }
}