package com.beanu.l4_bottom_tab.ui.module1_exam;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.beanu.arad.http.RxHelper;
import com.beanu.arad.support.recyclerview.divider.GridLayoutItemDecoration;
import com.beanu.l3_common.model.api.API;
import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.adapter.provider.Exam_Report_HeaderViewBinder;
import com.beanu.l4_bottom_tab.adapter.provider.Exam_Report_Title;
import com.beanu.l4_bottom_tab.adapter.provider.Exam_Report_TitleViewBinder;
import com.beanu.l4_bottom_tab.adapter.provider.Exam_Report_ViewBinder;
import com.beanu.l4_bottom_tab.base.BaseSDActivity;
import com.beanu.l4_bottom_tab.model.ApiService;
import com.beanu.l4_bottom_tab.model.bean.AnswerRecordDetailJson;
import com.beanu.l4_bottom_tab.model.bean.AnswerRecordJson;
import com.beanu.l4_bottom_tab.model.bean.AnswerRecordModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;
import com.beanu.l4_bottom_tab.util.Subscriber;

/**
 * 考试 报告
 */
public class ExamResultActivity extends BaseSDActivity {

    @BindView(R.id.recycle_view) RecyclerView mRecycleView;

    public final static String ARG_PARAM_ID = "id";
    public final static String ARG_PARAM_TYPE = "type";

    private String mId;
    private int mType;//0练习题 1智能练习 2真题

    private Items mItems;
    private MultiTypeAdapter mTypeAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_result);
        ButterKnife.bind(this);

        mId = getIntent().getStringExtra(ARG_PARAM_ID);
        mType = getIntent().getIntExtra(ARG_PARAM_TYPE, 0);

        mItems = new Items();
        mTypeAdapter = new MultiTypeAdapter(mItems);
        mTypeAdapter.register(AnswerRecordJson.class, new Exam_Report_HeaderViewBinder());
        mTypeAdapter.register(Exam_Report_Title.class, new Exam_Report_TitleViewBinder());
        mTypeAdapter.register(AnswerRecordDetailJson.class, new Exam_Report_ViewBinder());

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 5);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0) {
                    return 5;
                }
                if (mItems.get(position) instanceof Exam_Report_Title) {
                    return 5;
                }

                return 1;
            }
        });

        mRecycleView.setLayoutManager(gridLayoutManager);
        mRecycleView.setAdapter(mTypeAdapter);
        mRecycleView.addItemDecoration(new GridLayoutItemDecoration(20, 5));

        //请求数据
        switch (mType) {
            case 0:
            case 1:
                API.getInstance(ApiService.class).exam_report_detail(API.createHeader(), mId, mType)
                        .compose(RxHelper.<AnswerRecordJson>handleResult())
                        .subscribe(new Subscriber<AnswerRecordJson>() {
                            @Override
                            public void onCompleted() {
                                mTypeAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onNext(AnswerRecordJson answerRecordJson) {
                                mItems.clear();
                                mItems.add(answerRecordJson);
                                mItems.addAll(answerRecordJson.getArdj());
                            }
                        });
                break;
            case 2:
                API.getInstance(ApiService.class).test_paper_report_detail(API.createHeader(), mId)
                        .compose(RxHelper.<AnswerRecordJson>handleResult())
                        .subscribe(new Subscriber<AnswerRecordJson>() {
                            @Override
                            public void onCompleted() {
                                mTypeAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onNext(AnswerRecordJson answerRecordJson) {
                                mItems.clear();
                                mItems.add(answerRecordJson);
                                if (answerRecordJson.getModelList() != null) {
                                    for (AnswerRecordModel model : answerRecordJson.getModelList()) {
                                        Exam_Report_Title title = new Exam_Report_Title();
                                        title.setTitle(model.getName());
                                        mItems.add(title);
                                        if (model.getQuestionList() != null) {
                                            mItems.addAll(model.getQuestionList());
                                        }
                                    }
                                }
                            }
                        });
                break;
        }


    }

    @Override
    public boolean setupToolBarLeftButton(View leftButton) {
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        return true;
    }

    @Override
    public String setupToolBarTitle() {
        return "练习报告";
    }

    @OnClick({R.id.btn_error_analyze, R.id.btn_all_analyze})
    public void onViewClicked(View view) {

        int source = 0;
        switch (view.getId()) {
            case R.id.btn_error_analyze:
                source = 1;
                break;
            case R.id.btn_all_analyze:
                source = 0;
                break;
        }

        if (mItems.size() > 0) {
            AnswerRecordJson answerRecordJson = (AnswerRecordJson) mItems.get(0);

            Intent intent = new Intent(this, ErrorAnalysisActivity.class);
            intent.putExtra(ErrorAnalysisActivity.TITLE, answerRecordJson.getCourseName());
            intent.putExtra(ErrorAnalysisActivity.ANALYSIS_TYPE, answerRecordJson.getType());
            intent.putExtra(ErrorAnalysisActivity.ANALYSIS_ID, answerRecordJson.getId());
            intent.putExtra(ErrorAnalysisActivity.ANALYSIS_SOURCE, source);
            startActivity(intent);
        }
    }
}
