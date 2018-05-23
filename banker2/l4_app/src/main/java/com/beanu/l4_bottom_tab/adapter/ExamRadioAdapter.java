package com.beanu.l4_bottom_tab.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;

import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.model.bean.ExamOption;
import com.beanu.l4_bottom_tab.model.bean.ExamQuestion;

import java.util.List;

/**
 * 考题 单选
 * Created by Beanu on 2017/3/21.
 */

public class ExamRadioAdapter extends BaseAdapter {

    private List<ExamOption> mOptionList;
    private ExamQuestion mExamQuestion;
    private boolean examMode;
    private Context mContext;
    private int[] optionDrawable = new int[]{R.drawable.exam_radio_a, R.drawable.exam_radio_b, R.drawable.exam_radio_c, R.drawable.exam_radio_d, R.drawable.exam_radio_e, R.drawable.exam_radio_f, R.drawable.exam_radio_g, R.drawable.exam_radio_h};
    private int[] errorDrawable = new int[]{R.drawable.analysis_choose_wrong_a, R.drawable.analysis_choose_wrong_b, R.drawable.analysis_choose_wrong_c, R.drawable.analysis_choose_wrong_d, R.drawable.analysis_choose_wrong_e, R.drawable.analysis_choose_wrong_f, R.drawable.analysis_choose_wrong_g, R.drawable.analysis_choose_wrong_h};
    private int[] rightDrawable = new int[]{R.drawable.analysis_choose_right_a, R.drawable.analysis_choose_right_b, R.drawable.analysis_choose_right_c, R.drawable.analysis_choose_right_d, R.drawable.analysis_choose_right_e, R.drawable.analysis_choose_right_f, R.drawable.analysis_choose_right_g, R.drawable.analysis_choose_right_h};
    private int[] noChooseDrawable = new int[]{R.drawable.analysis_no_choose_a, R.drawable.analysis_no_choose_b, R.drawable.analysis_no_choose_c, R.drawable.analysis_no_choose_d, R.drawable.analysis_no_choose_e, R.drawable.analysis_no_choose_f, R.drawable.analysis_no_choose_g, R.drawable.analysis_no_choose_h};

    public ExamRadioAdapter(Context context, ExamQuestion examQuestion, boolean examMode) {
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
        view = LayoutInflater.from(mContext).inflate(R.layout.item_exam_radio, viewGroup, false);


        RadioButton radioButton = (RadioButton) view.findViewById(R.id.radio_button);

        ExamOption option = mOptionList.get(i);
        radioButton.setText(option.getContent());

        if (!examMode) {
            //分析模式
            if (option.getIs_correct() == 1) {
                radioButton.setCompoundDrawablesWithIntrinsicBounds(rightDrawable[i], 0, 0, 0);
            } else {
                radioButton.setCompoundDrawablesWithIntrinsicBounds(noChooseDrawable[i], 0, 0, 0);
            }

            if (mExamQuestion.getIsRealy() == 0 && mExamQuestion.getAnswer() != null) {
                if (option.getId().equals(mExamQuestion.getAnswer())) {
                    radioButton.setCompoundDrawablesWithIntrinsicBounds(errorDrawable[i], 0, 0, 0);
                }
            }

        } else {
            //答题模式
            if (i < 8) {
                radioButton.setCompoundDrawablesWithIntrinsicBounds(optionDrawable[i], 0, 0, 0);
            }
        }

        return view;
    }
}
