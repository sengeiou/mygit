package com.beanu.l4_bottom_tab.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.model.bean.ExamOption;
import com.beanu.l4_bottom_tab.model.bean.ExamQuestion;

import java.util.List;

/**
 * 考题 多选
 * Created by Beanu on 2017/3/21.
 */

public class ExamCheckboxAdapter extends BaseAdapter {

    List<ExamOption> mOptionList;
    private ExamQuestion mExamQuestion;
    private boolean examMode;
    private Context mContext;
    private int[] optionDrawable = new int[]{R.drawable.exam_checkbox_a, R.drawable.exam_checkbox_b, R.drawable.exam_checkbox_c, R.drawable.exam_checkbox_d, R.drawable.exam_checkbox_e, R.drawable.exam_checkbox_f, R.drawable.exam_checkbox_g, R.drawable.exam_checkbox_h};

    private int[] errorDrawable = new int[]{R.drawable.wrong_a, R.drawable.wrong_b, R.drawable.wrong_c, R.drawable.wrong_d, R.drawable.wrong_e, R.drawable.wrong_f, R.drawable.wrong_g, R.drawable.wrong_h};
    private int[] rightDrawable = new int[]{R.drawable.right_a, R.drawable.right_b, R.drawable.right_c, R.drawable.right_d, R.drawable.right_e, R.drawable.right_f, R.drawable.right_g, R.drawable.right_h};
    private int[] noChooseDrawable = new int[]{R.drawable.nochoose_a, R.drawable.nochoose_b, R.drawable.nochoose_c, R.drawable.nochoose_d, R.drawable.nochoose_e, R.drawable.nochoose_f, R.drawable.nochoose_g, R.drawable.nochoose_h};


    public ExamCheckboxAdapter(Context context, ExamQuestion examQuestion, boolean examMode) {
        mExamQuestion = examQuestion;
        mContext = context;
        mOptionList = examQuestion.getDataList();
        this.examMode = examMode;
    }

    @Override
    public int getCount() {
        return mOptionList == null ? 0 : mOptionList.size();
    }

    @Override
    public Object getItem(int i) {
        return mOptionList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(mContext).inflate(R.layout.item_exam_checkbox, viewGroup, false);

        CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox_item);

        ExamOption option = mOptionList.get(i);
        checkBox.setText(option.getContent());

        if (!examMode) {
            //分析模式
            if (option.getIs_correct() == 1) {
                checkBox.setCompoundDrawablesWithIntrinsicBounds(rightDrawable[i], 0, 0, 0);
            } else {
                checkBox.setCompoundDrawablesWithIntrinsicBounds(noChooseDrawable[i], 0, 0, 0);
            }

            if (mExamQuestion.getIsRealy() == 0) {
                if (mExamQuestion.getAnswer() != null && mExamQuestion.getAnswer().contains(option.getId())) {
                    checkBox.setCompoundDrawablesWithIntrinsicBounds(errorDrawable[i], 0, 0, 0);
                }
            }

        } else {
            //答题模式
            if (i < 8) {
                checkBox.setCompoundDrawablesWithIntrinsicBounds(optionDrawable[i], 0, 0, 0);
            }
        }


        return view;
    }
}
