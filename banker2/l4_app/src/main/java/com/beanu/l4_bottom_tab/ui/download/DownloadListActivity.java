package com.beanu.l4_bottom_tab.ui.download;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beanu.arad.http.RxHelper;
import com.beanu.arad.support.recyclerview.divider.HorizontalDividerItemDecoration;
import com.beanu.l3_common.model.api.API;
import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.adapter.provider.DownPeriodViewBinder;
import com.beanu.l4_bottom_tab.base.BaseSDActivity;
import com.beanu.l4_bottom_tab.model.ApiService;
import com.beanu.l4_bottom_tab.model.bean.OnlineLesson;
import com.beanu.l4_bottom_tab.model.bean.Period;
import com.beanu.l4_bottom_tab.util.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

public class DownloadListActivity extends BaseSDActivity {

    @BindView(R.id.toolbar_left_btn)
    ImageView toolbarLeftBtn;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.recycle_view)
    RecyclerView mRecycleView;


    MultiTypeAdapter mDownLoadPeriodAdapter;
    Items mDownPeriodItems;

    private OnlineLesson mOnlineLesson;
    private String mLessonId;

    List<Period> periodLists = new ArrayList<>();

    //课时的下载的ID列表
    private List<String> videoIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_list);
        ButterKnife.bind(this);

        mLessonId = getIntent().getStringExtra("lessonId");


        //初始化
        mDownPeriodItems = new Items();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycleView.setLayoutManager(linearLayoutManager);
        mRecycleView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).colorResId(R.color.color_line).size(1).build());

        mDownLoadPeriodAdapter = new MultiTypeAdapter();
        mDownLoadPeriodAdapter.register(Period.class,new DownPeriodViewBinder(new DownPeriodViewBinder.DownPeriodClickEvent() {
            @Override
            public void onClick(int position) {


            }
        }));
        mRecycleView.setAdapter(mDownLoadPeriodAdapter);

        //mDownPeriodItems.addAll(videoIds );
        // periodLists.addAll();

        //mDownLoadPeriodAdapter.notifyDataSetChanged();

        //请求数据

        API.getInstance(ApiService.class).online_lesson_period_list(API.createHeader(),mLessonId)
                .compose(RxHelper.<List<Period>>handleResult())
                .subscribe(new Subscriber<List<Period>>() {
                    @Override
                    public void onNext(List<Period> periods) {
                        //super.onNext(periods);
                        mDownPeriodItems.addAll(periods);
                        mDownLoadPeriodAdapter.setItems(mDownPeriodItems);
                        mDownLoadPeriodAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                    }
                });


    }





    @Override
    protected void onDestroy() {
        super.onDestroy();

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
    public boolean setupToolBarRightButton3(View rightButton3) {

        rightButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //点击全选

            }
        });
        return true;
    }

    @Override
    public String setupToolBarTitle() {
        return "选择课程";
    }

}
