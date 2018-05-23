package com.beanu.l4_bottom_tab.ui.module1_exam;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beanu.arad.base.ToolBarActivity;
import com.beanu.arad.http.RxHelper;
import com.beanu.arad.support.recyclerview.divider.GridLayoutItemDecoration;
import com.beanu.l3_common.model.api.API;
import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.adapter.ExamResultAdapter;
import com.beanu.l4_bottom_tab.model.ApiService;
import com.beanu.l4_bottom_tab.model.bean.AnswerRecordDetailJson;
import com.beanu.l4_bottom_tab.model.bean.AnswerRecordJson;
import com.beanu.l4_bottom_tab.support.dialog.AlertDialogCustom;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.beanu.l4_bottom_tab.util.Subscriber;

/**
 * 预览做题情况
 */
public class ExamPreviewFragment extends Fragment {

    private static final String ARG_PARAM1 = "title";
    private static final String ARG_PARAM2 = "answer_json";
    private static final String ARG_PARAM3 = "duration";
    private static final String ARG_PARAM4 = "type";

    @BindView(R.id.txt_exam_title) TextView mTxtExamTitle;
    @BindView(R.id.recycle_view) RecyclerView mRecycleView;
    Unbinder unbinder;

    private String mTitle;
    private AnswerRecordJson mAnswer;
    private int totalDuration;
    private int type;//0练习题 1 智能练习 2真题

    private ExamResultAdapter mExamResultAdapter;

    public ExamPreviewFragment() {
        // Required empty public constructor
    }

    /**
     * 实例化
     */
    public static ExamPreviewFragment newInstance(String param1, AnswerRecordJson param2, int totalDuration, int type) {
        ExamPreviewFragment fragment = new ExamPreviewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putParcelable(ARG_PARAM2, param2);
        args.putInt(ARG_PARAM3, totalDuration);
        args.putInt(ARG_PARAM4, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString(ARG_PARAM1);
            mAnswer = getArguments().getParcelable(ARG_PARAM2);
            totalDuration = getArguments().getInt(ARG_PARAM3);
            type = getArguments().getInt(ARG_PARAM4);

            mExamResultAdapter = new ExamResultAdapter(getActivity(), mAnswer.getArdj());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exam_preview, container, false);
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


    @OnClick(R.id.btn_submit_result)
    public void onViewClicked() {
        showDialog();
    }


    private void preprocessorJSON() {
        boolean isFinish = true;
        int answerRight = 0;
        if (mAnswer.getArdj() != null) {
            for (AnswerRecordDetailJson recordDetailJson : mAnswer.getArdj()) {
                if (recordDetailJson.getIsRealy() == 2) {
                    isFinish = false;
                }
                if (recordDetailJson.getIsRealy() == 1) {
                    answerRight++;
                }
            }

            mAnswer.setIsFinish(isFinish ? 1 : 0);
            mAnswer.setAnswerTotal(mAnswer.getArdj().size());
            mAnswer.setAnswerRealy(answerRight);
            mAnswer.setAnswerTime(String.valueOf(totalDuration));
        }
    }

    private void showDialog() {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        Fragment prev = getChildFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        ft.commit();

        boolean answerALL = true;
        if (mAnswer != null && mAnswer.getArdj() != null) {
            for (AnswerRecordDetailJson recordDetailJson : mAnswer.getArdj()) {
                if (recordDetailJson.getIsRealy() == 2) {
                    answerALL = false;
                    break;
                }
            }
        }

        AlertDialogCustom dialog = AlertDialogCustom.newInstance((answerALL ? "" : "您还有题目未做完，\n    ") + "确定交卷吗？");
        dialog.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((ToolBarActivity) getActivity()).showProgress();

                preprocessorJSON();
                Gson gson = new Gson();
                String json = gson.toJson(mAnswer);

                switch (type) {
                    case 0:
                    case 1:
                        API.getInstance(ApiService.class).postExamResult(API.createHeader(), json)
                                .compose(RxHelper.<String>handleResult())
                                .subscribe(new Subscriber<String>() {
                                    @Override
                                    public void onCompleted() {
                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onNext(String id) {
                                        ((ToolBarActivity) getActivity()).hideProgress();
                                        Intent intent = new Intent(getActivity(), ExamResultActivity.class);
                                        intent.putExtra(ExamResultActivity.ARG_PARAM_ID, id);
                                        intent.putExtra(ExamResultActivity.ARG_PARAM_TYPE, type);
                                        startActivity(intent);
                                        getActivity().finish();
                                    }
                                });
                        break;
                    case 2:

                        API.getInstance(ApiService.class).post_test_paper_result(API.createHeader(), json)
                                .compose(RxHelper.<String>handleResult())
                                .subscribe(new Subscriber<String>() {
                                    @Override
                                    public void onCompleted() {
                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onNext(String id) {
                                        ((ToolBarActivity) getActivity()).hideProgress();
                                        Intent intent = new Intent(getActivity(), ExamResultActivity.class);
                                        intent.putExtra(ExamResultActivity.ARG_PARAM_ID, id);
                                        intent.putExtra(ExamResultActivity.ARG_PARAM_TYPE, type);
                                        startActivity(intent);
                                        getActivity().finish();
                                    }
                                });
                        break;
                }


            }
        });
        dialog.show(getChildFragmentManager(), "dialog");
    }
}
