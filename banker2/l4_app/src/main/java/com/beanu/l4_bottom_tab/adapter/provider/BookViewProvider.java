package com.beanu.l4_bottom_tab.adapter.provider;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.model.bean.BookItem;
import com.beanu.l4_bottom_tab.ui.module4_book.BookDetailActivity;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewBinder;

/**
 * 图书
 * Created by Beanu on 2017/3/8.
 */
public class BookViewProvider
        extends ItemViewBinder<BookItem, BookViewProvider.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_books, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final BookItem bookItem) {
        final Context context = holder.itemView.getContext();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BookDetailActivity.class);
                intent.putExtra("bookId", bookItem.getId());
                context.startActivity(intent);
            }
        });

        if (!TextUtils.isEmpty(bookItem.getList_img())) {
            Glide.with(context).load(bookItem.getList_img()).into(holder.mImgBookFace);
        }
        holder.mTxtBookTitle.setText(bookItem.getName());
        holder.mTxtBookConcern.setText(bookItem.getPress());
        holder.mRateBarBook.setRating(bookItem.getStar_rating());
        holder.mTxtBookPrice.setText("¥" + bookItem.getPrice());

        if (bookItem.getEvaluate_num() > 0) {
            holder.mRateBarBook.setRating(bookItem.getStar_rating() / bookItem.getEvaluate_num());
        } else {
            holder.mRateBarBook.setRating(0);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_book_face) ImageView mImgBookFace;
        @BindView(R.id.txt_book_title) TextView mTxtBookTitle;
        @BindView(R.id.txt_book_concern) TextView mTxtBookConcern;
        @BindView(R.id.rate_bar_book) RatingBar mRateBarBook;
        @BindView(R.id.txt_book_price) TextView mTxtBookPrice;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}