package com.bokecc.sdk.mobile.demo.drm.play;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.bokecc.sdk.mobile.demo.drm.R;
import com.bokecc.sdk.mobile.demo.drm.adapter.VideoListViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 播放列表标签页，用于展示待播放的视频ID
 *
 * @author CC视频
 */
public class PlayFragment extends Fragment {

    private List<Pair<String, Integer>> pairs;

    private VideoListViewAdapter videoIdListViewAdapter;

    //TODO 待播放视频ID列表，可根据需求自定义
    public static String[] playVideoIds = new String[]{"0292E4271FC495819C33DC5901307461"};
    private ListView playListView;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity().getApplicationContext();
        RelativeLayout playLayout = new RelativeLayout(context);
        playLayout.setBackgroundColor(Color.WHITE);
        LayoutParams playLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        playLayout.setLayoutParams(playLayoutParams);

        playListView = new ListView(context);
        playListView.setDivider(getResources().getDrawable(R.drawable.line));
        playListView.setDividerHeight(2);
        playListView.setPadding(10, 10, 10, 10);
        LayoutParams playListLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        playLayout.addView(playListView, playListLayoutParams);

        // 生成动态数组，加入数据
        pairs = new ArrayList<Pair<String, Integer>>();
        for (int i = 0; i < playVideoIds.length; i++) {
            Pair<String, Integer> pair = new Pair<String, Integer>(playVideoIds[i], R.drawable.play);
            pairs.add(pair);
        }

        videoIdListViewAdapter = new VideoListViewAdapter(context, pairs);
        playListView.setAdapter(videoIdListViewAdapter);
        playListView.setOnItemClickListener(onItemClickListener);

        return playLayout;
    }

    OnItemClickListener onItemClickListener = new OnItemClickListener() {

        @SuppressWarnings("unchecked")
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Pair<String, Integer> pair = (Pair<String, Integer>) parent.getItemAtPosition(position);
            Intent intent = new Intent(context, MediaPlayActivity.class);
            intent.putExtra("videoId", pair.first);
            startActivity(intent);
        }
    };

}
