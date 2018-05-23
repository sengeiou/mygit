package com.beanu.l4_bottom_tab.adapter.provider;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.model.bean.Period;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewBinder;

public class DownPeriodViewBinder extends ItemViewBinder<Period, DownPeriodViewBinder.ViewHolder> {

    //用map保存checkbox是否被选中
    private Map<Integer,Boolean> map = new HashMap<>();

    private DownPeriodClickEvent mClickEvent;

    public interface DownPeriodClickEvent {
        public void onClick(int position);


    }


    public DownPeriodViewBinder( DownPeriodClickEvent clickEvent) {
        mClickEvent = clickEvent;

    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_online_lesson_period_download, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull Period downPeriod) {



        Context context = holder.itemView.getContext();
        holder.mTxtTitle.setText(downPeriod.getName());
        holder.mTxtDesc.setText("主讲：" + downPeriod.getTeacher()+ "   时长：" + downPeriod.getLongTime());

      /*  holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(holder.mCheckbox.isChecked()){
                    holder.mCheckbox.setChecked(false);
                }else {
                    holder.mCheckbox.setChecked(true);
                }
            }
        });*/


      holder.mCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


          }
      });

      holder.itemView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

          }
      });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.download_item)
        LinearLayout mLLItem;
        @BindView(R.id.checkbox_download)
        CheckBox mCheckbox;
        @BindView(R.id.txt_title)
        TextView mTxtTitle;
        @BindView(R.id.txt_date)
        TextView mTxtDesc;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
