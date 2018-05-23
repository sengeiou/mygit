package com.beanu.l4_bottom_tab.adapter;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.adapter.provider.OrderItemViewBinder;
import com.beanu.l4_bottom_tab.model.bean.OrderShop;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 订单  货品  列表
 * Created by Beanu on 2017/3/9.
 */

public class OrderListAdapter extends BaseAdapter {
    private Context context;
    private List<OrderShop> list;
    private int orderStatus;//订单状态
    private int mOrderType;

    private int listPostion;
    private OrderItemViewBinder.ButtonListener mButtonListener;

    public OrderListAdapter(Context context, List<OrderShop> list, int orderStatus, int orderType, int position, OrderItemViewBinder.ButtonListener buttonListener) {
        this.context = context;
        this.list = list;
        this.orderStatus = orderStatus;
        this.mOrderType = orderType;

        this.listPostion = position;
        this.mButtonListener = buttonListener;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_order_content, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        fillValue(position, viewHolder);
        return convertView;
    }

    private void fillValue(final int position, ViewHolder viewHolder) {
        OrderShop orderShop = list.get(position);
        if (!TextUtils.isEmpty(orderShop.getCoverAPP())) {
            Glide.with(context).load(orderShop.getCoverAPP()).into(viewHolder.mImgOrderItem);
        }

        viewHolder.mTxtOrderItemTitle.setText(orderShop.getName());
        viewHolder.mTxtOrderItemSku.setText(orderShop.getPress());
        if (mOrderType == 2) {
            viewHolder.mTxtOrderItemSku2.setText(String.format("出版社（%s)", orderShop.getPress()));
        } else {
            viewHolder.mTxtOrderItemSku2.setText(String.format("授课老师（%s)", orderShop.getTeacher()));
        }

        if (mOrderType == 2) {

            Spanned Spanned = Html.fromHtml("¥" + orderShop.getPrice() + "  <font color='gray'>x" + orderShop.getNum() + "</font>");
            viewHolder.mTxtOrderItemPrice.setText(Spanned);


        } else {
            viewHolder.mTxtOrderItemPrice.setText("¥" + orderShop.getPrice());
        }

        if (getCount() > 1 && (orderStatus == 2 || orderStatus == 3)) {
            viewHolder.mTxtOrderItemComment.setVisibility(View.VISIBLE);
            if (orderShop.getIs_comment() == 1) {
                viewHolder.mTxtOrderItemComment.setText("已评价");
                viewHolder.mTxtOrderItemComment.setEnabled(false);
            } else {
                viewHolder.mTxtOrderItemComment.setEnabled(true);
            }
        } else {
            viewHolder.mTxtOrderItemComment.setVisibility(View.GONE);
        }

        viewHolder.mTxtOrderItemComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mButtonListener.onCommentClick(listPostion, position);
            }
        });

    }

    static class ViewHolder {

        @BindView(R.id.img_order_item) ImageView mImgOrderItem;
        @BindView(R.id.txt_order_item_title) TextView mTxtOrderItemTitle;
        @BindView(R.id.txt_order_item_sku) TextView mTxtOrderItemSku;
        @BindView(R.id.txt_order_item_sku2) TextView mTxtOrderItemSku2;
        @BindView(R.id.txt_order_item_price) TextView mTxtOrderItemPrice;
        @BindView(R.id.txt_comment) TextView mTxtOrderItemComment;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}