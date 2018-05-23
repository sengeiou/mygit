package com.beanu.l4_bottom_tab.ui.module1_exam;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.beanu.arad.http.RxHelper;
import com.beanu.l3_common.model.api.API;
import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.base.BaseSDActivity;
import com.beanu.l4_bottom_tab.model.ApiService;
import com.beanu.l4_bottom_tab.model.bean.ToolsReport;
import com.beanu.l4_bottom_tab.support.widget.RoundProgressBar;
import com.jaychang.st.SimpleText;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.beanu.l4_bottom_tab.util.Subscriber;

/**
 * 学习报告
 */
public class ToolsReportActivity extends BaseSDActivity {

    @BindView(R.id.progress_bar) RoundProgressBar mProgressBar;
    @BindView(R.id.txt_top) TextView mTxtTop;
    @BindView(R.id.txt_rank) TextView mTxtRank;
    @BindView(R.id.txt_answer_total) TextView mTxtAnswerTotal;
    @BindView(R.id.txt_accuracy) TextView mTxtAccuracy;
    @BindView(R.id.txt_days) TextView mTxtDays;
    @BindView(R.id.txt_time) TextView mTxtTime;
    @BindView(R.id.txt_score) TextView mTxtScore;

    private int mTotalProgress;
    private int mCurrentProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools_report);
        ButterKnife.bind(this);

        requestHttpData();
        mTotalProgress = 100;
        mCurrentProgress = 0;
    }


    //请求数据
    private void requestHttpData() {
        API.getInstance(ApiService.class).tools_practice_report(API.createHeader()).compose(RxHelper.<ToolsReport>handleResult())
                .subscribe(new Subscriber<ToolsReport>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ToolsReport toolsReport) {
                        refreshUI(toolsReport);
                    }
                });
    }

    //刷新UI
    private void refreshUI(ToolsReport toolsReport) {
        mTxtTop.setText(toolsReport.getMaxScore() + "");
        mTxtRank.setText(toolsReport.getPm() + "");
        mTxtAnswerTotal.setText(SimpleText.create(this, toolsReport.getAnswer_total() + "道").first("道").size(13));
        mTxtAccuracy.setText(SimpleText.create(this, toolsReport.getZql()).first("%").size(13));
        mTxtDays.setText(SimpleText.create(this, toolsReport.getAnswer_day() + "天").first("天").size(13));

        String text = toolsReport.getAnswer_time();
        SimpleText simpleText = SimpleText.create(this, text);
        if (text.contains("秒")) {
            simpleText.first("秒").size(13);
        } else if (text.contains("分钟")) {
            simpleText.first("分钟").size(13);
        } else if (text.contains("小时")) {
            simpleText.first("小时").size(13);
        } else if (text.contains("天")) {
            simpleText.first("天").size(13);
        } else if (text.contains("年")) {
            simpleText.first("年").size(13);
        }
        mTxtTime.setText(simpleText);

        mTxtScore.setText(toolsReport.getForecastScore() + "");
        mTotalProgress = (int) toolsReport.getForecastScore();
        new Thread(new ProgressRunable()).start();
    }

    private class ProgressRunable implements Runnable {

        @Override
        public void run() {

            while (mCurrentProgress < mTotalProgress) {
                mCurrentProgress += 1;

                mProgressBar.setProgress(mCurrentProgress);

                try {
                    Thread.sleep(50);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public String setupToolBarTitle() {
        return "学习报告";
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
}
