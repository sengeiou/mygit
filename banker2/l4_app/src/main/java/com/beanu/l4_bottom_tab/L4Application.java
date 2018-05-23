package com.beanu.l4_bottom_tab;


import android.content.Context;
import android.support.multidex.MultiDex;

import com.alibaba.android.arouter.launcher.ARouter;
import com.beanu.arad.AradApplicationConfig;
import com.beanu.arad.support.log.KLog;
import com.beanu.l2_shareutil.ShareConfig;
import com.beanu.l2_shareutil.ShareManager;
import com.beanu.l4_bottom_tab.support.GlideImageLoaderForUnicorn;
import com.beanu.l4_bottom_tab.support.push.PushManager;
import com.beanu.l4_bottom_tab.support.push.util.Const;
import com.bokecc.sdk.mobile.demo.drm.CCApplication;
import com.qiyukf.unicorn.api.SavePowerConfig;
import com.qiyukf.unicorn.api.StatusBarNotificationConfig;
import com.qiyukf.unicorn.api.UICustomization;
import com.qiyukf.unicorn.api.Unicorn;
import com.qiyukf.unicorn.api.YSFOptions;
import com.tencent.bugly.crashreport.CrashReport;
import com.tendcloud.tenddata.TCAgent;



/**
 * 全局入口
 * Created by Beanu on 16/10/17.
 */

public class L4Application extends CCApplication {

    @Override
    public void onCreate() {
        super.onCreate();
//        disableCrashHandler();

        if (getApplicationContext().getPackageName().equals(processName)) {

            //注册全局的recycle item
//            GlobalMultiTypePool.register(Space.class, new SpaceViewProvider());

            //初始化ARouter
            if (BuildConfig.DEBUG) {
                ARouter.openLog();     // 打印日志
                ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
            }
            ARouter.init(this); // 尽可能早，推荐在Application中初始化

            //Bugly 错误日志收集
            Context context = getApplicationContext();
            String packageName = context.getPackageName();
            CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
            strategy.setUploadProcess(processName == null || processName.equals(packageName));
            strategy.setAppChannel("android");

            CrashReport.initCrashReport(getApplicationContext(), "fb18fd2ba6", false, strategy);

            //Log日志
            KLog.init(BuildConfig.DEBUG);

            //分享
            String QQ_ID = "1105963583";
            String WX_ID = "wx3ea8c6e4a466d599";
            String WEIBO_ID = "440352374";

            String WX_SECRET = "99c821f3847fde101f0cf92794cc5833";
            String REDIRECT_URL = "http://open.weibo.com/apps/440352374/privilege/oauth";

            ShareConfig config = ShareConfig.instance()
                    .qqId(QQ_ID)
                    .wxId(WX_ID)
                    .weiboId(WEIBO_ID)
                    // 下面两个，如果不需要登录功能，可不填写
                    .weiboRedirectUrl(REDIRECT_URL)
                    .debug(BuildConfig.DEBUG)
                    .wxSecret(WX_SECRET);

            ShareManager.init(config);

            //推送
            Const.setMiUI_APP("2882303761517600143", "5141760082143");//小米推送 正确的APPID
            PushListener mPushListener = new PushListener();
            PushManager.register(this, BuildConfig.DEBUG, mPushListener);

            //网易七鱼
            Unicorn.init(this, "0a1c70f822f67c2fba45cd19bb1e857c", options(), new GlideImageLoaderForUnicorn(this));

            //TalkingData
            TCAgent.LOG_ON = BuildConfig.DEBUG;
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

    private YSFOptions options() {
        YSFOptions options = new YSFOptions();
        options.statusBarNotificationConfig = new StatusBarNotificationConfig();
        options.savePowerConfig = new SavePowerConfig();

        UICustomization uiCustomization = new UICustomization();
        uiCustomization.titleBarStyle = 1;
        options.uiCustomization = uiCustomization;

        return options;
    }
}
