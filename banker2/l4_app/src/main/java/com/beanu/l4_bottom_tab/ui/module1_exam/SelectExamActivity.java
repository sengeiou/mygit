package com.beanu.l4_bottom_tab.ui.module1_exam;

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.beanu.arad.Arad;
import com.beanu.arad.http.RxHelper;
import com.beanu.arad.support.log.KLog;
import com.beanu.l3_common.model.api.API;
import com.beanu.l3_common.model.bean.EventModel;
import com.beanu.l3_common.util.AppHolder;
import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.adapter.SelectExamAdapter;
import com.beanu.l4_bottom_tab.base.BaseSDActivity;
import com.beanu.l4_bottom_tab.model.ApiService;
import com.beanu.l4_bottom_tab.model.bean.Subject;
import com.beanu.l4_bottom_tab.ui.MainActivity;
import com.beanu.l4_bottom_tab.util.Subscriber;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

/**
 * 选择考试分类
 */
@Route(path = "/exam/select_subject")
public class SelectExamActivity extends BaseSDActivity {

    @BindView(R.id.expandable_listView_exam) ExpandableListView mExpandableListViewExam;

    SelectExamAdapter mSelectExamAdapter;
    List<Subject> mSubjectList;
    Disposable mSubscription;

    private boolean fromLoginPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_exam);
        ButterKnife.bind(this);

        fromLoginPage = getIntent().getBooleanExtra("from", false);

        mSubjectList = new ArrayList<>();
        mSelectExamAdapter = new SelectExamAdapter(this, mSubjectList);
        mExpandableListViewExam.setGroupIndicator(null);
        mExpandableListViewExam.setAdapter(mSelectExamAdapter);


        mExpandableListViewExam.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                Subject group = (Subject) mSelectExamAdapter.getGroup(i);
                if (group.getSecSubList() == null || group.getSecSubList().size() == 0) {
                    update2Server(group.getId());
                }
                KLog.d(group.getName());

                return false;
            }
        });

        mExpandableListViewExam.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {

                Subject child = (Subject) mSelectExamAdapter.getChild(i, i1);
                update2Server(child.getId());

                KLog.d(child.getName());
                return false;
            }
        });

        mExpandableListViewExam.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {

                Subject exam = (Subject) mSelectExamAdapter.getGroup(groupPosition);
                //折叠起其他的
                if (exam.getSecSubList() != null && exam.getSecSubList().size() > 0) {

                    for (int i1 = 0; i1 < mExpandableListViewExam.getCount(); i1++) {
                        if (i1 != groupPosition) {
                            mExpandableListViewExam.collapseGroup(i1);
                        }
                    }

                    mSelectExamAdapter.notifyDataSetChanged();
                }
            }
        });


        //请求数据
       API.getInstance(ApiService.class).subject_list(API.createHeader())
                .compose(RxHelper.<List<Subject>>handleResult())
                .subscribe(new Subscriber<List<Subject>>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        mSubscription = d;
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<Subject> subjects) {
                        mSubjectList.clear();
                        mSubjectList.addAll(subjects);
                        mSelectExamAdapter.notifyDataSetChanged();
                    }
                });
    }

    //更新用户选的学科
    private void update2Server(final String subjectId) {
        API.getInstance(ApiService.class).updateUserSelectedSubject(API.createHeader(), subjectId)
                .compose(RxHelper.<String>handleResult())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        if (fromLoginPage) {
                            startActivity(MainActivity.class);
                        }
                        Arad.bus.post(new EventModel.ChangeSubject());
                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        AppHolder.getInstance().user.setSubjectId(subjectId);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSubscription != null && mSubscription.isDisposed())
            mSubscription.dispose();
    }

    @Override
    public String setupToolBarTitle() {
        return "考试类别";
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
