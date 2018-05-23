package com.beanu.l4_bottom_tab.adapter.provider;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.model.bean.Course;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.ItemViewBinder;

/**
 * 科目
 * Created by Beanu on 2017/2/27.
 */
public class CourseViewProvider extends ItemViewBinder<Course, CourseViewProvider.ViewHolder> {

    OnClickEvent mOnClickListener;

    public CourseViewProvider(OnClickEvent onClickListener) {
        mOnClickListener = onClickListener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(
            @NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_course, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final Course course) {
        Context context = holder.itemView.getContext();

        holder.mRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnClickListener.onClick(getPosition(holder));
            }
        });


        holder.mRlRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnClickListener.gotoExam(getPosition(holder));
            }
        });


        int paddingLeft = context.getResources().getDimensionPixelSize(R.dimen.course_margin);
        paddingLeft = paddingLeft * course.getChildLevel();

        holder.mImgItemCourseTag.setPadding(paddingLeft, 0, 0, 0);


        if (course.isExpand()) {
            holder.mImgItemCourseTag.setImageResource(R.drawable.home_page_unfold);
        } else {
            holder.mImgItemCourseTag.setImageResource(R.drawable.home_page_close_up);
        }

        if (course.getList() == null || course.getList().size() == 0) {
            holder.mImgItemCourseTag.setImageResource(R.drawable.home_page_y);
        }

        if (course.getChildLevel() > 0) {
            holder.mImgItemCourseTag.setScaleX(0.9f);
            holder.mImgItemCourseTag.setScaleY(0.9f);
        } else {
            holder.mImgItemCourseTag.setScaleX(1f);
            holder.mImgItemCourseTag.setScaleY(1f);
        }

        holder.mTxtItemCourseName.setText(course.getName());

        float rating = (float) course.getAnswerNum() / (float) course.getTotal();
        holder.mRateItemCourseProgress.setRating(rating * 5);
        holder.mTxtItemCourseProgress.setText(course.getAnswerNum() + "/" + course.getTotal());
    }


    public static interface OnClickEvent {
        public void onClick(int position);

        public void gotoExam(int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_item_course_tag) ImageView mImgItemCourseTag;
        @BindView(R.id.txt_item_course_name) TextView mTxtItemCourseName;
        @BindView(R.id.rate_item_course_progress) RatingBar mRateItemCourseProgress;
        @BindView(R.id.txt_item_course_progress) TextView mTxtItemCourseProgress;
        @BindView(R.id.rl_item_course) RelativeLayout mRelativeLayout;
        @BindView(R.id.txt_go_on) TextView mTxtGoOn;
        @BindView(R.id.rl_right) RelativeLayout mRlRight;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}