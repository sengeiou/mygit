package com.beanu.l4_clean;


import android.content.Context;
import android.support.multidex.MultiDex;
import android.widget.ImageView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.beanu.arad.AradApplication;
import com.beanu.arad.AradApplicationConfig;
import com.beanu.arad.support.log.KLog;
import com.beanu.l2_shareutil.ShareConfig;
import com.beanu.l2_shareutil.ShareManager;
import com.beanu.l4_clean.support.push.PushManager;
import com.beanu.l4_clean.ui.PushListener;
import com.beanu.l4_clean.util.RongCloudEvent;
import com.bumptech.glide.Glide;
import com.qiniu.pili.droid.rtcstreaming.RTCConferenceManager;
import com.qiniu.pili.droid.rtcstreaming.RTCMediaStreamingManager;
import com.tendcloud.tenddata.TCAgent;
import com.yuyh.library.imgsel.ISNav;
import com.yuyh.library.imgsel.common.ImageLoader;

import cn.jpush.android.api.JPushInterface;
import io.rong.imlib.RongIMClient;

/**
 * 全局入口
 * Created by Beanu on 16/10/17.
 */

public class L4Application extends AradApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        if (getApplicationContext().getPackageName().equals(processName)) {

            //Log日志
            KLog.init(BuildConfig.DEBUG);

            //极光
            JPushInterface.setDebugMode(BuildConfig.DEBUG);
            JPushInterface.init(this);

            //初始化 融云
            RongIMClient.init(this);
            RongCloudEvent.init(this);

            //初始化ARouter
            if (BuildConfig.DEBUG) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
                ARouter.openLog();     // 打印日志
                ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
            }
            ARouter.init(this);

            //图片选择器 初始化
            ISNav.getInstance().init(new ImageLoader() {
                @Override
                public void displayImage(Context context, String path, ImageView imageView) {
                    Glide.with(context).load(path).into(imageView);
                }
            });

            //初始化七牛直播
            RTCMediaStreamingManager.init(getApplicationContext());
            RTCConferenceManager.init(getApplicationContext());

            //分享，登录
            ShareConfig config = ShareConfig.instance()
                    .qqId("1106753456")
                    .wxId("wxcc991d8553ebb4d6")
                    .wxSecret("e8f69f77f387e5a51ccd5ed13da13e74");
            ShareManager.init(config);

            //设计推送监听
            PushListener mPushListener = new PushListener();
            PushManager.register(this, true, mPushListener);

            //talkingdata
            TCAgent.LOG_ON = true;
            TCAgent.init(this);
            TCAgent.setReportUncaughtExceptions(true);
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    protected AradApplicationConfig appConfig() {
        return new AradApplicationConfig();
    }
}
