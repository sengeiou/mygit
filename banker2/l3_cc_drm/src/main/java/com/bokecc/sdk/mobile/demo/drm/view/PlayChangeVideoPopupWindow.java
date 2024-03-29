package com.bokecc.sdk.mobile.demo.drm.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.bokecc.sdk.mobile.demo.drm.R;
import com.bokecc.sdk.mobile.demo.drm.util.ParamsUtil;

import java.util.List;

/**
 * 弹出菜单
 *
 * @author CC视频
 */
public class PlayChangeVideoPopupWindow {

    private Context context;
    private PopupWindow popupWindow;
    private ListView listView;
    private VideosAdapter adapter;

    public PlayChangeVideoPopupWindow(Context context, int height, List<String> videoIds) {
        this.context = context;

        RelativeLayout view = new RelativeLayout(context);

        listView = new ListView(context);
        listView.setPadding(0, ParamsUtil.dpToPx(context, 3), 0, ParamsUtil.dpToPx(context, 3));
        view.addView(listView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        adapter = new VideosAdapter(videoIds);
        listView.setAdapter(adapter);

        popupWindow = new PopupWindow(view, height * 2 / 3, height);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.argb(178, 0, 0, 0)));
    }

    public void setItem(OnItemClickListener listener) {
        listView.setOnItemClickListener(listener);
    }

    public void showAsDropDown(View parent) {
        popupWindow.showAtLocation(parent, Gravity.RIGHT, 0, 0);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        listView.setSelection(currentSelectedPosition);
    }

    public void dismiss() {
        popupWindow.dismiss();
    }

    class VideosAdapter extends BaseAdapter {

        List<String> videoIdList;

        public VideosAdapter(List<String> videoIdList) {
            this.videoIdList = videoIdList;
        }

        @Override
        public int getCount() {
            return videoIdList.size();
        }

        @Override
        public Object getItem(int position) {

            return videoIdList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            ViewHolder holder = null;
            if (convertView != null) {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            } else {
                view = LayoutInflater.from(context).inflate(R.layout.single_video_info, null);
                holder = new ViewHolder();
                holder.tv = (TextView) view.findViewById(R.id.tv_single_video_info);
                holder.desc = (TextView) view.findViewById(R.id.txt_date);
                view.setTag(holder);
            }

            String videoInfo = videoIdList.get(position);
            String[] infos = videoInfo.split(",");
            if (infos.length == 3) {
                holder.tv.setText(infos[0]);
                holder.desc.setText("主讲：" + infos[1] + "   时长：" + infos[2]);
            }


            if (position == currentSelectedPosition) {
                holder.tv.setTextColor(context.getResources().getColor(R.color.rb_text_check));
            } else {
                holder.tv.setTextColor(context.getResources().getColor(R.color.white));
            }

            return view;
        }

        class ViewHolder {
            public TextView tv;
            public TextView desc;
        }

    }

    private int currentSelectedPosition;

    public PlayChangeVideoPopupWindow setSelectedPosition(int currentSelectedPosition) {
        this.currentSelectedPosition = currentSelectedPosition;
        return this;
    }

    public void refreshView() {
        listView.smoothScrollToPosition(currentSelectedPosition);
        adapter.notifyDataSetChanged();
        listView.invalidate();
    }
}
