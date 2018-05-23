package com.beanu.l4_bottom_tab.ui.module1_exam;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beanu.arad.support.recyclerview.divider.GridLayoutItemDecoration;
import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.adapter.ExamResultAdapter;
import com.beanu.l4_bottom_tab.model.bean.AnswerRecordDetailJson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 预览错题情况
 */
public class ErrorAnalysisPreviewFragment extends Fragment {

    private static final String ARG_PARAM1 = "title";
    private static final String ARG_PARAM2 = "count";

    @BindView(R.id.txt_exam_title) TextView mTxtExamTitle;
    @BindView(R.id.recycle_view) RecyclerView mRecycleView;
    Unbinder unbinder;

    private String mTitle;

    private ExamResultAdapter mExamResultAdapter;

    public ErrorAnalysisPreviewFragment() {
        // Required empty public constructor
    }

    /**
     * 实例化
     */
    public static ErrorAnalysisPreviewFragment newInstance(String param1, int count) {
        ErrorAnalysisPreviewFragment fragment = new ErrorAnalysisPreviewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, count);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString(ARG_PARAM1);
            int count = getArguments().getInt(ARG_PARAM2);

            List<AnswerRecordDetailJson> list = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                AnswerRecordDetailJson question = new AnswerRecordDetailJson();
                question.setIsRealy(1);
                list.add(question);
            }

            mExamResultAdapter = new ExamResultAdapter(getActivity(), list);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_error_analysis_preview, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTxtExamTitle.setText(mTitle);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 5);
        mRecycleView.setLayoutManager(gridLayoutManager);
        mRecycleView.addItemDecoration(new GridLayoutItemDecoration(16, 5));

        if (mExamResultAdapter != null) {
            mRecycleView.setAdapter(mExamResultAdapter);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
