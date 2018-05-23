package com.beanu.l4_bottom_tab;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.beanu.arad.support.log.KLog;
import com.beanu.l3_common.util.Constants;
import com.beanu.l4_bottom_tab.support.push.IPushListener;
import com.beanu.l4_bottom_tab.support.push.PushMessage;
import com.beanu.l4_bottom_tab.ui.MainActivity;
import com.beanu.l4_bottom_tab.ui.module2_liveLesson.LiveLessonDetailActivity;
import com.beanu.l4_bottom_tab.ui.module3_onlineLesson.OnlineLessonDetailActivity;
import com.beanu.l4_bottom_tab.ui.module_news.NewsDetailActivity;
import com.beanu.l4_bottom_tab.util.SystemUtils;

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
        String type, albumId;
        if (message != null && message.getExtra() != null) {
            type = message.getExtra().get("type");
            albumId = message.getExtra().get("id");

            if ("0".equals(type)) {
                //资讯
                if (SystemUtils.isAppAlive(context, context.getPackageName())) {

                    Intent mainIntent = new Intent(context, MainActivity.class);
                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    //打开自定义的Activity
                    Intent i = new Intent(context, NewsDetailActivity.class);
                    i.putExtra("newsId", albumId);

                    Intent[] intents = {mainIntent, i};
                    context.startActivities(intents);

                    KLog.d("live");
                } else {
                    String packageName = context.getApplicationContext().getPackageName();
                    Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
                    launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    Bundle args = new Bundle();
                    args.putString("newsId", albumId);
                    args.putString("type", type);
                    launchIntent.putExtra(Constants.EXTRA_BUNDLE, args);
                    context.startActivity(launchIntent);

                    KLog.d("die");
                }


            } else if ("1".equals(type)) {
                //直播课

                if (SystemUtils.isAppAlive(context, context.getPackageName())) {
                    Intent mainIntent = new Intent(context, MainActivity.class);
                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    //打开自定义的Activity
                    Intent i = new Intent(context, LiveLessonDetailActivity.class);
                    i.putExtra("lessonId", albumId);

                    Intent[] intents = {mainIntent, i};

                    context.startActivities(intents);

                    KLog.d("live");
                } else {
                    String packageName = context.getApplicationContext().getPackageName();
                    Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
                    launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    Bundle args = new Bundle();
                    args.putString("lessonId", albumId);
                    args.putString("type", type);
                    launchIntent.putExtra(Constants.EXTRA_BUNDLE, args);
                    context.startActivity(launchIntent);

                    KLog.d("die");
                }

            } else if ("2".equals(type)) {
                //高清网课

                if (SystemUtils.isAppAlive(context, context.getPackageName())) {
                    Intent mainIntent = new Intent(context, MainActivity.class);
                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    Intent i = new Intent(context, OnlineLessonDetailActivity.class);
                    i.putExtra("lessonId", albumId);

                    Intent[] intents = {mainIntent, i};
                    context.startActivities(intents);
                    KLog.d("live");
                } else {
                    String packageName = context.getApplicationContext().getPackageName();
                    Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
                    launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    Bundle args = new Bundle();
                    args.putString("lessonId", albumId);
                    args.putString("type", type);

                    launchIntent.putExtra(Constants.EXTRA_BUNDLE, args);
                    context.startActivity(launchIntent);
                    KLog.d("die");
                }


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
