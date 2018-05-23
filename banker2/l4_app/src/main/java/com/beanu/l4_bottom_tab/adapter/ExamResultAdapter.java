package com.beanu.l4_bottom_tab.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.l3_common.adapter.BaseAdapter;
import com.beanu.l3_common.model.bean.EventModel;
import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.model.bean.AnswerRecordDetailJson;

import java.util.List;


/**
 * 预览 答题情况
 * Created by Beanu on 2016/12/16.
 */

public class ExamResultAdapter extends BaseAdapter<AnswerRecordDetailJson, ExamResultAdapter.ViewHolder> {

    public ExamResultAdapter(Context context, List<AnswerRecordDetailJson> list) {
        super(context, list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_exam_result, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ((ViewHolder) holder).bind(getItem(position), position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Arad.bus.post(new EventModel.ExamPreviewQuesionSelectEvent(position));
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTxtNum;

        public ViewHolder(View itemView) {
            super(itemView);
            mTxtNum = (TextView) itemView.findViewById(R.id.txt_num);
        }

        private void bind(AnswerRecordDetailJson item, int position) {
            mTxtNum.setText((position + 1) + "");

            switch (item.getIsRealy()) {
                case 0:
                case 1:
                    mTxtNum.setBackgroundResource(R.drawable.answer_sheet_yes);
                    mTxtNum.setSelected(true);
                    break;
                case 2:
                    mTxtNum.setBackgroundResource(R.drawable.answer_sheet_no);
                    mTxtNum.setSelected(false);
                    break;
            }
        }
    }
}
