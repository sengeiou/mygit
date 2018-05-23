package com.beanu.l4_bottom_tab.ui.module1_exam;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.base.BaseSDActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 题库 更多功能列表
 */
public class ToolsExamActivity extends BaseSDActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_more);
        ButterKnife.bind(this);
    }

    @Override
    public String setupToolBarTitle() {
        return "更多";
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

    @OnClick({R.id.rl_tool_report, R.id.rl_tool_exam, R.id.rl_tool_ai, R.id.rl_tool_wrong_topic, R.id.rl_tool_history, R.id.rl_tool_note, R.id.rl_tool_collect})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_tool_report:
                startActivity(ToolsReportActivity.class);
                break;
            case R.id.rl_tool_exam:
                startActivity(ToolsTestPaperActivity.class);
                break;
            case R.id.rl_tool_ai:
                Intent intent_ai = new Intent(this, ExamActivity.class);
                intent_ai.putExtra(ExamActivity.TITLE, "智能练习");
                intent_ai.putExtra(ExamActivity.EXAM_TYPE, 1);
                startActivity(intent_ai);
                break;
            case R.id.rl_tool_wrong_topic:
                Intent intent = new Intent(ToolsExamActivity.this, ToolsCourseListActivity.class);
                intent.putExtra("type", 0);
                intent.putExtra("title", "错题本");
                startActivity(intent);
                break;
            case R.id.rl_tool_history:
                Intent intent1 = new Intent(ToolsExamActivity.this, ToolsHistoryActivity.class);
                startActivity(intent1);
                break;
            case R.id.rl_tool_note:
                Intent intent_note = new Intent(ToolsExamActivity.this, ToolsCourseListActivity.class);
                intent_note.putExtra("type", 2);
                intent_note.putExtra("title", "笔记题目");
                startActivity(intent_note);
                break;
            case R.id.rl_tool_collect:
                Intent intent_collect = new Intent(ToolsExamActivity.this, ToolsCourseListActivity.class);
                intent_collect.putExtra("type", 1);
                intent_collect.putExtra("title", "收藏题目");
                startActivity(intent_collect);
                break;
        }
    }
}
