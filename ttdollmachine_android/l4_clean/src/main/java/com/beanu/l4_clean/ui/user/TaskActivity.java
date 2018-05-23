package com.beanu.l4_clean.ui.user;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.beanu.arad.http.IPageModel;
import com.beanu.arad.http.RxHelper;
import com.beanu.arad.support.recyclerview.divider.HorizontalDividerItemDecoration;
import com.beanu.l3_common.model.HttpModel;
import com.beanu.l3_common.model.PageModel;
import com.beanu.l3_common.model.api.API;
import com.beanu.l4_clean.R;
import com.beanu.l4_clean.adapter.binder.TaskViewBinder;
import com.beanu.l4_clean.base.SimplestListActivity;
import com.beanu.l4_clean.model.APIService;
import com.beanu.l4_clean.model.bean.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * 任务中心
 */
public class TaskActivity extends SimplestListActivity<Task> implements TaskViewBinder.OnClickListener {

    private Items mItems = new Items();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getRecyclerView().addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).colorResId(android.R.color.transparent).sizeResId(R.dimen.grid_space).build());
    }

    @Override
    protected RecyclerView.Adapter<?> provideAdapter() {
        MultiTypeAdapter adapter = new MultiTypeAdapter(mItems);
        adapter.register(Task.class, new TaskViewBinder(this));
        return adapter;
    }

    @Override
    public Observable<? extends IPageModel<Task>> loadData(Map<String, Object> params, int pageIndex) {

        return Observable.create(new ObservableOnSubscribe<HttpModel<IPageModel<Task>>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<HttpModel<IPageModel<Task>>> subscriber) throws Exception {
                HttpModel<IPageModel<Task>> baseModel = new HttpModel<>();
                baseModel.succeed = "000";
                PageModel<Task> pageModel = new PageModel<>();
                pageModel.pageNumber = 1;
                pageModel.totalPage = 1;
                pageModel.dataList = new ArrayList<>();

                Task task1 = new Task("累计游戏30场", "+30", false, 0);
                Task task2 = new Task("累计游戏50场", "+50", false, 0);
                Task task3 = new Task("累计游戏100场", "+100", false, 0);
                Task task4 = new Task("分享到微信好友/朋友圈", "+2/次", false, 1);
                Task task5 = new Task("分享的邀请码被填写", "+10/次", false, -1);
                Task task6 = new Task("填写邀请码", "+10", false, 2);


                pageModel.dataList.add(task1);
                pageModel.dataList.add(task2);
                pageModel.dataList.add(task3);
                pageModel.dataList.add(task4);
                pageModel.dataList.add(task5);
                pageModel.dataList.add(task6);

                baseModel.dataInfo = pageModel;

                subscriber.onNext(baseModel);
                subscriber.onComplete();
            }
        })
                .compose(RxHelper.<IPageModel<Task>>handleResult())
                .zipWith(API.getInstance(APIService.class).taskProgress().compose(RxHelper.<Map<String, Integer>>handleResult()), new BiFunction<IPageModel<Task>, Map<String, Integer>, IPageModel<Task>>() {
                    @Override
                    public IPageModel<Task> apply(IPageModel<Task> taskIPageModel, Map<String, Integer> map) throws Exception {

                        try {
                            int task1 = map.get("totalNumTh");
                            int task2 = map.get("totalNumF");
                            int task3 = map.get("totalNumH");
                            int task4 = map.get("writeCode");
                            int task5 = map.get("shareTimes");

                            taskIPageModel.getDataList().get(0).setComplete(task1 == 1);
                            taskIPageModel.getDataList().get(1).setComplete(task2 == 1);
                            taskIPageModel.getDataList().get(2).setComplete(task3 == 1);

                            taskIPageModel.getDataList().get(4).setComplete(task5 == 1);
                            taskIPageModel.getDataList().get(5).setComplete(task4 == 1);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        return taskIPageModel;
                    }
                });

    }

    @Override
    public void loadDataComplete(List<Task> beans) {
        mItems.clear();
        mItems.addAll(beans);
        getAdapter().notifyDataSetChanged();
    }

    @Override
    public String setupToolBarTitle() {
        return "任务中心";
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
    public void onClickPlayGame() {

        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onClickShare() {
        startActivity(ShareActivity.class);
    }

    @Override
    public void onClickInvite() {

        startActivity(ShareActivity.class);
    }
}