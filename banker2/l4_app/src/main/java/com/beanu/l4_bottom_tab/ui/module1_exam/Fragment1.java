package com.beanu.l4_bottom_tab.ui.module1_exam;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.beanu.arad.base.ToolBarFragment;
import com.beanu.arad.support.log.KLog;
import com.beanu.arad.utils.SizeUtils;
import com.beanu.arad.utils.ToastUtils;
import com.beanu.l3_common.util.AppHolder;
import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.adapter.provider.BannerViewProvider;
import com.beanu.l4_bottom_tab.adapter.provider.CourseViewProvider;
import com.beanu.l4_bottom_tab.adapter.provider.Home_Function;
import com.beanu.l4_bottom_tab.adapter.provider.Home_FunctionViewProvider;
import com.beanu.l4_bottom_tab.adapter.provider.Space;
import com.beanu.l4_bottom_tab.adapter.provider.SpaceViewProvider;
import com.beanu.l4_bottom_tab.model.bean.Banner;
import com.beanu.l4_bottom_tab.model.bean.BannerItem;
import com.beanu.l4_bottom_tab.model.bean.Course;
import com.beanu.l4_bottom_tab.mvp.contract.QuestionBankContract;
import com.beanu.l4_bottom_tab.mvp.model.QuestionBankModelImpl;
import com.beanu.l4_bottom_tab.mvp.presenter.QuestionBankPresenterImpl;

import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * 题库question bank
 */
public class Fragment1 extends ToolBarFragment<QuestionBankPresenterImpl, QuestionBankModelImpl> implements QuestionBankContract.View {

    @BindView(R.id.recycle_view) RecyclerView mRecycleView;
    @BindView(R.id.toolbar) Toolbar mToolbar;

    private static final int MAX_MOVE_DISTANCE = SizeUtils.dp2px(240);//当布局滑动50dp完成渐变
    private float scrolledDistance = 0;

    private Items items;
    private MultiTypeAdapter adapter;
    private Banner mBanner;

    public Fragment1() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        items = new Items();

        adapter = new MultiTypeAdapter();
        adapter.register(Space.class, new SpaceViewProvider());
        adapter.register(Banner.class, new BannerViewProvider());
        adapter.register(Home_Function.class, new Home_FunctionViewProvider());
        adapter.register(Course.class, new CourseViewProvider(new CourseViewProvider.OnClickEvent() {
            @Override
            public void onClick(int position) {

                if (items.get(position) instanceof Course) {
                    Course course = (Course) items.get(position);
                    if (course.isExpand()) {
                        //则关闭
                        closeNode(course, position);

                    } else {

                        //如果是一级节点，先关闭打开的
                        int count_expand = 0;//当前一级展开节点子节点的个数，一会删除
                        int expandPosition = 0;//当前一级展开节点的位置
                        if (course.getChildLevel() == 0) {

                            expandPosition = firstExpandNodePosition();
                            if (expandPosition >= 3) {
                                count_expand = closeNode((Course) items.get(expandPosition), expandPosition);
                                KLog.d("count" + count_expand);
                            }
                        }

                        //则打开
                        if (course.getList() != null && course.getList().size() > 0) {
                            course.setExpand(true);

                            for (int i = 0; i < course.getList().size(); i++) {
                                Course cc = course.getList().get(i);
                                cc.setChildLevel(course.getChildLevel() + 1);

                                //如果之前展开的一级节点在当前点击点的上方，则减去相应的子节点个数。如果在下方，则不需要删除子节点个数
                                items.add(position + i + 1 - (expandPosition >= position ? 0 : count_expand), cc);
                            }

                            //通知adapter
                            adapter.notifyItemChanged(position);
                            adapter.notifyItemRangeInserted(position + 1 - (expandPosition >= position ? 0 : count_expand), course.getList().size());

                        } else {

                            if (course.getTotal() > 0) {
                                Intent intent = new Intent(getActivity(), ExamActivity.class);
                                intent.putExtra(ExamActivity.TITLE, "专项智能练习（" + course.getName() + "）");
                                intent.putExtra(ExamActivity.EXAM_TYPE, 0);
                                intent.putExtra(ExamActivity.EXAM_COURSE_ID, course.getId());

                                startActivity(intent);
                            } else {
                                ToastUtils.showShort("专题下还没有维护题目");
                            }

                        }
                    }
                }

            }

            @Override
            public void gotoExam(int position) {
                if (items.size() > position) {
                    if (items.get(position) instanceof Course) {
                        Course course = (Course) items.get(position);
                        if (course.getTotal() > 0) {
                            Intent intent = new Intent(getActivity(), ExamActivity.class);
                            intent.putExtra(ExamActivity.TITLE, "专项智能练习（" + course.getName() + "）");
                            intent.putExtra(ExamActivity.EXAM_TYPE, 0);
                            intent.putExtra(ExamActivity.EXAM_COURSE_ID, course.getId());

                            startActivity(intent);
                        } else {
                            ToastUtils.showShort("专题下还没有维护题目");
                        }
                    }
                }
            }

        }));


        //初始化banner
        mBanner = new Banner();
        mBanner.setItemList(mPresenter.getBannerList());

        items.add(mBanner);
        items.add(new Home_Function());
        items.add(new Space(8));
        adapter.setItems(items);

        //请求数据
        mPresenter.requestHttpData(0, AppHolder.getInstance().user.getSubjectId());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_1, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //设置toolbar在4.4版本以下的高度
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            int height = getResources().getDimensionPixelSize(R.dimen.toolbar);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height);
            mToolbar.setLayoutParams(layoutParams);
        }


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecycleView.setLayoutManager(linearLayoutManager);
        mRecycleView.setAdapter(adapter);
        //防止更新某个item 闪烁的问题，让动画时间为0
        mRecycleView.getItemAnimator().setChangeDuration(0);
        //设置不同的动画时间
        mRecycleView.getItemAnimator().setRemoveDuration(60);
        mRecycleView.getItemAnimator().setAddDuration(60);
        mRecycleView.getItemAnimator().setMoveDuration(120);

        //设定滑动的距离，显示toolbar
        mRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                scrolledDistance += dy;

//                KLog.d("distance" + scrolledDistance + "dy" + dy);
                if (scrolledDistance >= 0 && scrolledDistance < MAX_MOVE_DISTANCE) {
                    float alpha = scrolledDistance / MAX_MOVE_DISTANCE;
                    mToolbar.setAlpha(alpha);
                } else if (scrolledDistance >= MAX_MOVE_DISTANCE) {
                    mToolbar.setAlpha(1);
                }

            }
        });

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 100) {
                mPresenter.requestHttpData(0, AppHolder.getInstance().user.getSubjectId());
            }
        }
    }

    @Override
    public boolean setupToolBarLeftButton(View leftButton) {
        ((ImageView) leftButton).setImageResource(R.drawable.home_page_l);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SelectExamActivity.class);
                startActivityForResult(intent, 100);
            }
        });
        return true;
    }

    @Override
    public boolean setupToolBarRightButton1(View rightButton1) {
        return setupToolBarRightButton(rightButton1);
    }

    public boolean setupToolBarRightButton(View rightButton) {
        ((ImageView) rightButton).setImageResource(R.drawable.home_page_r);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ToolsExamActivity.class);
                startActivity(intent);
            }
        });
        return true;
    }

    @Override
    public String setupToolBarTitle() {
        return "银行人";
    }

    @Override
    public void refreshCourseList(List<Course> list) {

        int rangeCount = items.size() - 3;

        //先清空course数据
        Iterator<Object> iterator = items.iterator();
        while (iterator.hasNext()) {
            Object item = iterator.next();
            if (item instanceof Course) {
                iterator.remove();
            }
        }

        //加入新数据
        for (Course course : mPresenter.getCourseList()) {
            course.setChildLevel(0);
            items.add(course);
        }

        //更新adapter 先去掉之前的，在插入新的
        if (rangeCount > 0) {
            adapter.notifyItemRangeRemoved(3, rangeCount);
        }

        int newDataCount = mPresenter.getCourseList().size();
        if (newDataCount > 0) {
            adapter.notifyItemRangeInserted(3, newDataCount);
        }


    }

    @Override
    public void refreshHeaderBanner(List<BannerItem> list) {
        mBanner.setItemList(mPresenter.getBannerList());
        adapter.notifyItemChanged(0);
    }

}
