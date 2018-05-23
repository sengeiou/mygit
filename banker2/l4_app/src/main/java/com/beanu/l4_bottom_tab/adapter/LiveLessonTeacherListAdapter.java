package com.beanu.l4_bottom_tab.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.model.bean.TeacherIntro;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 直播课列表中的老师列表
 * Created by Beanu on 2017/3/9.
 */

public class LiveLessonTeacherListAdapter extends BaseAdapter {
    private Context context;
    private List<TeacherIntro> list;


    public LiveLessonTeacherListAdapter(Context context, List<TeacherIntro> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_live_lesson_teacher, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        fillValue(position, viewHolder);
        return convertView;
    }

    private void fillValue(int position, ViewHolder viewHolder) {
        TeacherIntro teacher = list.get(position);

        if (!TextUtils.isEmpty(teacher.getHead_portrait())) {
            Glide.with(context).load(teacher.getHead_portrait()).apply(RequestOptions.circleCropTransform()).into(viewHolder.mImgTeacher);
        }
        viewHolder.mTxtTeacherName.setText(teacher.getName());
    }

    static class ViewHolder {

        @BindView(R.id.img_teacher) ImageView mImgTeacher;
        @BindView(R.id.txt_teacher_name) TextView mTxtTeacherName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
