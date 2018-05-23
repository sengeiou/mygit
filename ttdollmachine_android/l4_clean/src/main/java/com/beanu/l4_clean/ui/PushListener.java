package com.beanu.l4_clean.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.beanu.arad.support.log.KLog;
import com.beanu.l3_common.util.AppHolder;
import com.beanu.l3_common.util.Constants;
import com.beanu.l4_clean.support.push.IPushListener;
import com.beanu.l4_clean.support.push.PushMessage;
import com.beanu.l4_clean.util.SystemUtils;

/**
 * 推送监听
 * Created by Beanu on 2017/6/16.
 */

public class PushListener implements IPushListener {

    @Override
    public void onRegister(Context context, String registerID) {

    }

    @Override
    public void onUnRegister(Context context) {

    }

    @Override
    public void onPaused(Context context) {

    }

    @Override
    public void onResume(Context context) {

    }

    @Override
    public void onMessage(Context context, PushMessage message) {

    }

    @Override
    public void onMessageClicked(Context context, PushMessage message) {
        String type, id, title;
        if (message != null && message.getExtra() != null) {
            type = message.getExtra().get("type");
            id = message.getExtra().get("id");
            title = message.getExtra().get("title");

            //判断app进程是否存活
            if (SystemUtils.isAppAlive(context, context.getPackageName())) {
                //如果存活的话，就直接启动DetailActivity，但要考虑一种情况，就是app的进程虽然仍然在
                //但Task栈已经空了，比如用户点击Back键退出应用，但进程还没有被系统回收，如果直接启动
                //DetailActivity,再按Back键就不会返回MainActivity了。所以在启动
                //DetailActivity前，要先启动MainActivity。
                Log.i("NotificationReceiver", "the app process is alive");
                Intent mainIntent = new Intent(context, MainActivity.class);
                //将MainAtivity的launchMode设置成SingleTask, 或者在下面flag中加上Intent.FLAG_CLEAR_TOP,
                //如果Task栈中有MainActivity的实例，就会把它移到栈顶，把在它之上的Activity都清理出栈，
                //如果Task栈不存在MainActivity实例，则在栈顶创建
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                String userId = AppHolder.getInstance().user.getId();
                //打开自定义的Activity
                Intent i = new Intent(context, WebActivity.class);
                i.putExtra("title", title);
                i.putExtra("url", Constants.URL + "messageDetails?messageId=" + id + "userId=" + userId);

                Intent[] intents = {mainIntent, i};

                context.startActivities(intents);

                KLog.d("live");
            } else {
                //如果app进程已经被杀死，先重新启动app，将DetailActivity的启动参数传入Intent中，参数经过
                //SplashActivity传入MainActivity，此时app的初始化已经完成，在MainActivity中就可以根据传入             //参数跳转到DetailActivity中去了
                Log.i("NotificationReceiver", "the app process is dead");
                String packageName = context.getApplicationContext().getPackageName();
                Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
                launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                Bundle args = new Bundle();
                args.putString("title", title);
                args.putString("url", Constants.URL + "messageDetails?messageId=" + id + "userId=");

                launchIntent.putExtra(Constants.EXTRA_BUNDLE, args);
                context.startActivity(launchIntent);

                KLog.d("die");
            }


        }
    }

    @Override
    public void onCustomMessage(Context context, PushMessage message) {

    }

    @Override
    public void onAlias(Context context, String alias) {

    }
}