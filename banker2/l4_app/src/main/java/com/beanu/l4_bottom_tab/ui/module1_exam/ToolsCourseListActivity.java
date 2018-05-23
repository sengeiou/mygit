package com.beanu.l4_bottom_tab.ui.module1_exam;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.beanu.arad.support.log.KLog;
import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.adapter.provider.ToolsCourseProvider;
import com.beanu.l4_bottom_tab.base.BaseSDActivity;
import com.beanu.l4_bottom_tab.model.bean.Course;
import com.beanu.l4_bottom_tab.mvp.contract.ToolsCourceListContract;
import com.beanu.l4_bottom_tab.mvp.model.ToolsCourceListModelImpl;
import com.beanu.l4_bottom_tab.mvp.presenter.ToolsCourceListPresenterImpl;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;


/**
 * 工具-科目列表-通用
 */
public class ToolsCourseListActivity extends BaseSDActivity<ToolsCourceListPresenterImpl, ToolsCourceListModelImpl> implements ToolsCourceListContract.View {

    @BindView(R.id.recycle_view) RecyclerView mRecycleView;
    @BindView(R.id.arad_content) PtrClassicFrameLayout mPtr;

    private Items items;
    private MultiTypeAdapter adapter;
    private int chosePostion;
    private int type_model;//0错题本 1收藏  2笔记题目
    private String mTitle;//页面标题
    private boolean enableOpen = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools_course_list);
        ButterKnife.bind(this);
        type_model = getIntent().getIntExtra("type", 0);
        mTitle = getIntent().getStringExtra("title");

        items = new Items();

        adapter = new MultiTypeAdapter();
        adapter.register(Course.class, new ToolsCourseProvider(new ToolsCourseProvider.OnClickEvent() {
            @Override
            public void onClick(int position) {

                if (enableOpen) {

                    chosePostion = position;

                    if (items.get(position) instanceof Course) {

                        Course course = (Course) items.get(position);
                        if (course.getChildren() == 0) {
                            //没有下一级
                            Intent intent = new Intent(ToolsCourseListActivity.this, ErrorAnalysisActivity.class);
                            intent.putExtra(ErrorAnalysisActivity.TITLE, "专项智能练习（" + course.getName() + "）");
                            intent.putExtra(ErrorAnalysisActivity.ANALYSIS_TYPE, 90 + type_model);
                            intent.putExtra(ErrorAnalysisActivity.ANALYSIS_ID, course.getId());
                            startActivity(intent);

                        } else {
                            //可以打开下一级
                            if (course.isExpand()) {
                                //则关闭
                                closeNode(course, position);

                            } else {

                                //然后打开
                                mPresenter.requestCourseList(type_model, course.getId());
                                enableOpen = false;

                            }

                        }
                    }
                }

            }
        }));

        adapter.setItems(items);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecycleView.setLayoutManager(linearLayoutManager);
        mRecycleView.setAdapter(adapter);
        //防止更新某个item 闪烁的问题，让动画时间为0
        mRecycleView.getItemAnimator().setChangeDuration(0);
        //设置不同的动画时间
        mRecycleView.getItemAnimator().setRemoveDuration(60);
        mRecycleView.getItemAnimator().setAddDuration(60);
        mRecycleView.getItemAnimator().setMoveDuration(120);

        //下拉刷新监听
        mPtr.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {

                mPresenter.requestCourseList(type_model, "0");
            }
        });


        //请求数据
        contentLoading();
        mPresenter.requestCourseList(type_model, "0");
    }

    @Override
    public void refreshCourseList(List<Course> list) {
        //先清空course数据
        items.clear();

        //加入新数据
        for (Course course : mPresenter.getCourseList()) {
            course.setChildLevel(0);
            items.add(course);
        }

        //更新adapter
        adapter.notifyDataSetChanged();
        mPtr.refreshComplete();
    }

    @Override
    public void refreshNextChild(List<Course> list) {

        Course course = (Course) items.get(chosePostion);
        course.setList(list);

        //如果是一级节点，先关闭打开的
        int count_expand = 0;//当前一级展开节点子节点的个数，一会删除
        int expandPosition = 0;//当前一级展开节点的位置
        if (course.getChildLevel() == 0) {

            expandPosition = firstExpandNodePosition();
            if (expandPosition > 0) {
                count_expand = closeNode((Course) items.get(expandPosition), expandPosition);
                KLog.d("count" + count_expand);
            }
        }

        if (course.getList() != null && course.getList().size() > 0) {
            course.setExpand(true);

            for (int i = 0; i < course.getList().size(); i++) {
                Course cc = course.getList().get(i);
                cc.setChildLevel(course.getChildLevel() + 1);

                //如果之前展开的一级节点在当前点击点的上方，则减去相应的子节点个数。如果在下方，则不需要删除子节点个数
                items.add(chosePostion + i + 1 - (expandPosition >= chosePostion ? 0 : count_expand), cc);
            }

            //通知adapter
            adapter.notifyItemChanged(chosePostion);
            adapter.notifyItemRangeInserted(chosePostion + 1 - (expandPosition >= chosePostion ? 0 : count_expand), course.getList().size());

        }

        enableOpen = true;
    }


    //递归计算当前节点下面有多少个展开的数据
    private int expandChildCount(Course course) {

        int num = 0;

        if (course.getList() == null || course.getList().size() == 0) {
            return 1;
        }

        for (Course child : course.getList()) {
            if (child.isExpand()) {
                num += 1 + expandChildCount(child);
                child.setExpand(false);
            } else
                num++;
        }

        return num;
    }

    //关闭某个节点
    private int closeNode(Course course, int beginPosition) {
        int count_expand = expandChildCount(course);
        course.setExpand(false);

        if (course.getList() != null && course.getList().size() > 0) {

            for (int i = count_expand; i > 0; i--) {
                items.remove(beginPosition + i);
            }
        }

        //通知adapter
        adapter.notifyItemChanged(beginPosition);
        adapter.notifyItemRangeRemoved(beginPosition + 1, count_expand);
        return count_expand;

    }

    //当前展开的一级节点位置
    private int firstExpandNodePosition() {

        int position = 0;
        for (int i = 0; i < items.size(); i++) {
            Object item = items.get(i);
            if (item instanceof Course) {
                Course temp = (Course) item;
                if (temp.isExpand() && temp.getChildLevel() == 0) {
                    position = i;
                    break;
                }
            }
        }
        return position;

    }


    @Override
    public String setupToolBarTitle() {
        return mTitle;
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
