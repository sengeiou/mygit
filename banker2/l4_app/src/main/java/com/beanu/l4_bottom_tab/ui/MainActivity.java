package com.beanu.l4_bottom_tab.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.beanu.arad.Arad;
import com.beanu.arad.http.RxHelper;
import com.beanu.arad.support.log.KLog;
import com.beanu.arad.support.updateversion.UpdateChecker;
import com.beanu.l3_common.model.api.API;
import com.beanu.l3_common.model.bean.EventModel;
import com.beanu.l3_common.model.bean.Version;
import com.beanu.l3_common.util.AppHolder;
import com.beanu.l3_common.util.Constants;
import com.beanu.l3_login.SignIn2Activity;
import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.base.NavBarActivity;
import com.beanu.l4_bottom_tab.model.ApiService;
import com.beanu.l4_bottom_tab.support.push.PushManager;
import com.beanu.l4_bottom_tab.ui.module1_exam.Fragment1;
import com.beanu.l4_bottom_tab.ui.module2_liveLesson.Fragment2;
import com.beanu.l4_bottom_tab.ui.module2_liveLesson.LiveLessonDetailActivity;
import com.beanu.l4_bottom_tab.ui.module3_onlineLesson.Fragment3;
import com.beanu.l4_bottom_tab.ui.module3_onlineLesson.OnlineLessonDetailActivity;
import com.beanu.l4_bottom_tab.ui.module4_book.Fragment4;
import com.beanu.l4_bottom_tab.ui.module5_my.Fragment5;
import com.beanu.l4_bottom_tab.ui.module_news.NewsDetailActivity;
import com.bokecc.sdk.mobile.demo.drm.util.DataSet;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.disposables.Disposable;

@Route(path = "/app/main")
public class MainActivity extends NavBarActivity {

    public static final int EXIT_CODE = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Arad.bus.register(this);

        //视频 初始化数数据库
        DataSet.init(this);

        //如果已经登录，极光设置别名 标签
        if (!TextUtils.isEmpty(AppHolder.getInstance().user.getId())) {
            setAlias(AppHolder.getInstance().user.getId());
        }

        //如果应用被杀死，需要先启动MainActivity，然后启动点击项目
        //启动相册详情
        Bundle bundle = getIntent().getBundleExtra(Constants.EXTRA_BUNDLE);
        if (bundle != null) {
            //如果bundle存在，取出其中的参数，启动DetailActivity
            String type = bundle.getString("type");
            if ("0".equals(type)) {
                String id = bundle.getString("newsId");
                Intent i = new Intent(this, NewsDetailActivity.class);
                i.putExtra("newsId", id);
                startActivity(i);

            } else if ("1".equals(type)) {

                String lessonId = bundle.getString("lessonId");
                Intent i = new Intent(this, LiveLessonDetailActivity.class);
                i.putExtra("lessonId", lessonId);
                startActivity(i);

            } else if ("2".equals(type)) {
                String lessonId = bundle.getString("lessonId");
                Intent i = new Intent(this, OnlineLessonDetailActivity.class);
                i.putExtra("lessonId", lessonId);
                startActivity(i);

            }
        }


        //请求版本更新
        API.getInstance(ApiService.class).initProgram(API.createHeader())
                .compose(RxHelper.<Version>handleResult())
                .subscribe(new io.reactivex.Observer<Version>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mRxManage.add(d);
                    }

                    @Override
                    public void onNext(Version version) {
                        AppHolder.getInstance().setVersion(version);

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        updateVersionDialog();

                    }
                });
    }

//    @Override
//    protected void setStatusBar() {
//        StatusBarUtil.setTransparentForImageViewInFragment(this, null);
//    }

    @Override
    protected TabInfo[] createTabInfo() {
        TabInfo[] tabInfos = new TabInfo[5];//数组数为底部导航栏按钮数量
        tabInfos[0] = new TabInfo("home", R.drawable.tab_home_btn, R.string.tab_title_1, Fragment1.class);
        tabInfos[1] = new TabInfo("live", R.drawable.tab_live_lesson_btn, R.string.tab_title_2, Fragment2.class);
        tabInfos[2] = new TabInfo("online", R.drawable.tab_online_lesson_btn, R.string.tab_title_3, Fragment3.class);
        tabInfos[3] = new TabInfo("book", R.drawable.tab_book_shop_btn, R.string.tab_title_4, Fragment4.class);
        tabInfos[4] = new TabInfo("my", R.drawable.tab_user_center_btn, R.string.tab_title_5, Fragment5.class);

        return tabInfos;
    }

    @Override
    protected void onQuit() {
        //退出app
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        KLog.d("MainActivity onDestroy");
        Arad.bus.unregister(this);

//        AppHolder.getInstance().reset();
        //数据库保存
        DataSet.saveData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == EXIT_CODE) {
            //退出账号，重新登录
            startActivity(SignIn2Activity.class);
            finish();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginEvent(EventModel.LoginEvent event) {
        //接收到登陆成功事件之后，初始化 融云 极光等

        PushManager.resume(getApplicationContext());

        //设置极光别名＋tag
        setAlias(event.mUser.getId());
//        PushManager.setTag(getApplicationContext(), "VIP");

    }


    private void setAlias(String alias) {
        PushManager.setAlias(getApplicationContext(), alias);
    }

    //版本更新提示
    private void updateVersionDialog() {
        if (!TextUtils.isEmpty(AppHolder.getInstance().mVersion.getApkversion())) {
            try {
                int version = Integer.valueOf(AppHolder.getInstance().mVersion.getApkversion());
                UpdateChecker.checkForDialog(MainActivity.this, AppHolder.getInstance().mVersion.getDetail(), AppHolder.getInstance().mVersion.getApkurl(), version);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}