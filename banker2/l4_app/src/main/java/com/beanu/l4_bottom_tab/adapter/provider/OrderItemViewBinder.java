package com.beanu.l4_bottom_tab.adapter.provider;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beanu.arad.widget.LinearLayoutForListView;
import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.adapter.OrderListAdapter;
import com.beanu.l4_bottom_tab.model.bean.OrderItem;
import com.beanu.l4_bottom_tab.model.bean.OrderShop;
import com.beanu.l4_bottom_tab.ui.module2_liveLesson.LiveLessonDetailActivity;
import com.beanu.l4_bottom_tab.ui.module3_onlineLesson.OnlineLessonDetailActivity;
import com.beanu.l4_bottom_tab.ui.module4_book.BookDetailActivity;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewBinder;

/**
 * 订单 view holder
 * Created by Beanu on 2017/4/6.
 */
public class OrderItemViewBinder extends ItemViewBinder<OrderItem, OrderItemViewBinder.ViewHolder> {


    private ButtonListener mButtonListener;
    DecimalFormat df = new DecimalFormat("0.0");

    public interface ButtonListener {
        public void onDeleteClick(int position, String orderId);

        public void onPayClick(int position, String orderId);

        public void onCommentClick(int position, int child);

    }

    public OrderItemViewBinder(ButtonListener listener) {
        mButtonListener = listener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_order_item, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final OrderItem orderItem) {
        final Context context = holder.itemView.getContext();

        final OrderListAdapter orderListAdapter = new OrderListAdapter(context, orderItem.getOpList(), orderItem.getStatus(), orderItem.getOrderType(), getPosition(holder), mButtonListener);
        holder.mListView.setOnItemClickLinstener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int position = view.getId();
                OrderShop orderShop = orderItem.getOpList().get(position);

                switch (orderItem.getOrderType()) {
                    case 0:
                        String lessonId = orderShop.getId();
                        Intent intent = new Intent(context, LiveLessonDetailActivity.class);
                        intent.putExtra("lessonId", lessonId);
                        context.startActivity(intent);
                        break;
                    case 1:
                        String id = orderShop.getId();
                        Intent i = new Intent(context, OnlineLessonDetailActivity.class);
                        i.putExtra("lessonId", id);
                        context.startActivity(i);
                        break;
                    case 2:
                        String bookId = orderShop.getId();
                        Intent intent2 = new Intent(context, BookDetailActivity.class);
                        intent2.putExtra("bookId", bookId);
                        context.startActivity(intent2);
                        break;
                }

//                //去评价
//                if (orderListAdapter.getCount() > 1 && (orderItem.getStatus() == 2 || orderItem.getStatus() == 3)) {
//                    mButtonListener.onCommentClick(getPosition(holder), position);
//                }


            }
        });
        holder.mListView.setAdapter(orderListAdapter);
        holder.mTxtOrderNum.setText("订单单号：" + orderItem.getOrder_code());
        holder.mTxtOrderTime.setText("下单时间：" + orderItem.getCreatetime());


        switch (orderItem.getStatus()) {
            case 0:
                holder.mTxtOrderStatus.setText("待付款");
                holder.mTxtOrderStatus.setTextColor(context.getResources().getColor(R.color.cart_red));
                holder.mTxtOrderComment.setText("支付");
                holder.mTxtOrderDelete.setVisibility(View.VISIBLE);
                break;
            case 1:

                holder.mTxtOrderStatus.setTextColor(context.getResources().getColor(R.color.base_font_black));
                holder.mTxtOrderStatus.setText("已支付");
                holder.mTxtOrderComment.setText("已支付");
                holder.mTxtOrderDelete.setVisibility(View.GONE);
                break;
            case 2:
            case 3:
                holder.mTxtOrderStatus.setTextColor(context.getResources().getColor(R.color.base_font_black));
                holder.mTxtOrderStatus.setText("已支付");
                holder.mTxtOrderComment.setText("评价");
                holder.mTxtOrderDelete.setVisibility(View.VISIBLE);

                //多个商品的评价
                if (orderItem.getOpList().size() == 1) {
                    holder.mTxtOrderComment.setVisibility(View.VISIBLE);
                    if (orderItem.getOpList().get(0).getIs_comment() == 1) {
                        holder.mTxtOrderComment.setText("已评价");
                        holder.mTxtOrderComment.setEnabled(false);
                    } else {
                        holder.mTxtOrderComment.setEnabled(true);
                    }
                } else {
                    holder.mTxtOrderComment.setVisibility(View.GONE);
                }

                break;
            case 4:
                holder.mTxtOrderStatus.setTextColor(context.getResources().getColor(R.color.base_font_black));
                holder.mTxtOrderStatus.setText("失效");
                holder.mTxtOrderComment.setText("订单失效");
                holder.mTxtOrderDelete.setVisibility(View.VISIBLE);
                break;
        }

        int sumProduct = 0;
        if (orderItem.getOpList() != null) {
            for (OrderShop orderShop : orderItem.getOpList()) {
                sumProduct += orderShop.getNum();
            }
        }
        holder.mTxtTotal.setText(String.format("共%s件商品  小计：", sumProduct));
        holder.mTxtOrderSumPrice.setText("¥" + df.format(orderItem.getTotal()));
        holder.mTxtOrderComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (orderItem.getStatus() == 0) {
                    mButtonListener.onPayClick(getPosition(holder), orderItem.getId());
                } else if (orderItem.getStatus() == 2 || orderItem.getStatus() == 3) {
                    mButtonListener.onCommentClick(getPosition(holder), 0);
                }
            }
        });

        holder.mTxtOrderDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mButtonListener.onDeleteClick(getPosition(holder), orderItem.getId());
            }
        });

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.list_view) LinearLayoutForListView mListView;
        @BindView(R.id.txt_order_sum_price) TextView mTxtOrderSumPrice;
        @BindView(R.id.txt_order_delete) TextView mTxtOrderDelete;
        @BindView(R.id.txt_order_comment) TextView mTxtOrderComment;
        @BindView(R.id.txt_order_total) TextView mTxtTotal;

        @BindView(R.id.txt_order_num) TextView mTxtOrderNum;
        @BindView(R.id.txt_order_time) TextView mTxtOrderTime;
        @BindView(R.id.txt_order_status) TextView mTxtOrderStatus;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}