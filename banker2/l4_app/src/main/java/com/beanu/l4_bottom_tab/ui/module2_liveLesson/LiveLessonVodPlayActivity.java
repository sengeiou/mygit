package com.beanu.l4_bottom_tab.ui.module2_liveLesson;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.support.log.KLog;
import com.beanu.l3_common.util.AppHolder;
import com.beanu.l3_gensee.fragement.ChatFragment;
import com.beanu.l3_gensee.fragement.QaFragment;
import com.beanu.l3_gensee.fragement.VoteFragment;
import com.beanu.l3_gensee.fragement.vod.VodDocFragment;
import com.beanu.l3_gensee.fragement.vod.VodViedoFragment;
import com.beanu.l3_gensee.playerdemo.LogCatService;
import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.base.BaseSDActivity;
import com.gensee.common.ServiceType;
import com.gensee.entity.ChatMsg;
import com.gensee.entity.InitParam;
import com.gensee.entity.QAMsg;
import com.gensee.entity.VodObject;
import com.gensee.media.VODPlayer;
import com.gensee.taskret.OnTaskRet;
import com.gensee.vod.VodSite;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 直播课回放
 */
public class LiveLessonVodPlayActivity extends BaseSDActivity implements VodSite.OnVodListener {

    private static final String TAG = "LiveLessonPlayActivity";

    @BindView(R.id.fragment_video_portrait) FrameLayout mFragmentVideo;
    @BindView(R.id.progress) ProgressBar mProgress;
    @BindView(R.id.fragement_update) FrameLayout mFragementUpdate;
    @BindView(R.id.top3_rl) RelativeLayout mTop3Rl;
    @BindView(R.id.progress_bar) ProgressBar mProgressBar;
    @BindView(R.id.tv_tip) TextView txtTip;
    @BindView(R.id.ly_loadingBar) LinearLayout mLyLoadingBar;
    @BindView(R.id.rl_tip) RelativeLayout relTip;
    @BindView(R.id.radio_group) RadioGroup mRadioGroup;
    @BindView(R.id.top2_ll) View mTop2Ll;


    private String subjectTitle = "";
    private ServiceType serviceType = ServiceType.TRAINING;

    private Intent serviceIntent;
    private VodSite vodSite;
    private VODPlayer mPlayer;

    private boolean bJoinSuccess = false;

    private int inviteMediaType;
    private AlertDialog dialog;

    private FragmentManager mFragmentManager;
    private ChatFragment mChatFragment;//聊天
    private VodDocFragment mDocFragment;//文档
    private VodViedoFragment mViedoFragment;//视频
    private QaFragment mQaFragment;//问答
    private VoteFragment mVoteFragment;//投票


    public interface RESULT {
        int DOWNLOAD_ERROR = 2;
        int DOWNLOAD_STOP = 3;
        int DOWNLOADER_INIT = 4;
        int DOWNLOAD_START = 5;
        int ON_GET_VODOBJ = 100;
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case RESULT.ON_GET_VODOBJ:

                    bJoinSuccess = true;
                    String vodId = (String) msg.obj;

                    vodSite.getVodDetail(vodId);
                    mViedoFragment.play(vodId);

                    break;
                default:
                    break;
            }
        }

    };


    public static void startActivity(Context context, String number, String password, String nickName, String title) {

        Intent intent = new Intent(context, LiveLessonVodPlayActivity.class);
        intent.putExtra("number", number);
        intent.putExtra("password", password);
        intent.putExtra("nickName", nickName);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //屏幕常亮
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_live_lesson_play);
        ButterKnife.bind(this);

        startLogService();
        initWidget();

        initVod();
        initInitParam();
        disableSlideBack();
    }

    private void initVod() {
        /**
         * 优先调用进行组件加载，有条件的情况下可以放到application启动时候的恰当时机调用
         */
        VodSite.init(this, new OnTaskRet() {

            @Override
            public void onTaskRet(boolean arg0, int arg1, String arg2) {
                // TODO Auto-generated method stub

            }
        });

    }

    private void startLogService() {
        serviceIntent = new Intent(this, LogCatService.class);
        startService(serviceIntent);
    }

    private void stopLogService() {
        if (null != serviceIntent) {
            stopService(serviceIntent);
        }
    }

    private void initWidget() {

        //切换不同的功能
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if (i == R.id.radio_doc) {
                    FragmentTransaction ftdoc = mFragmentManager.beginTransaction();
                    hideFragment(ftdoc);
                    processDocFragment(ftdoc);
                    ftdoc.commit();
                } else if (i == R.id.radio_chat) {
                    FragmentTransaction ftChat = mFragmentManager.beginTransaction();
                    hideFragment(ftChat);
                    processChatFragment(ftChat);
                    ftChat.commit();
                } else if (i == R.id.radio_qa) {
                    FragmentTransaction ftqa = mFragmentManager.beginTransaction();
                    hideFragment(ftqa);
                    processQaFragment(ftqa);
                    ftqa.commit();
                }
            }
        });
        findViewById(R.id.radio_chat).setVisibility(View.GONE);
        findViewById(R.id.radio_qa).setVisibility(View.GONE);

        //设置视频的高度
        int screenWidth = Arad.app.deviceInfo.getScreenWidth();
        mFragmentVideo.getLayoutParams().height = screenWidth / 4 * 3;

        mPlayer = new VODPlayer();
        mFragmentManager = getSupportFragmentManager();

        FragmentTransaction ft = mFragmentManager.beginTransaction();
        processChatFragment(ft);
        processQaFragment(ft);
        processVoteFragment(ft);
        hideFragment(ft);
        processDocFragment(ft);
        processVideoFragment(ft);
        ft.commit();
    }

    private void processChatFragment(FragmentTransaction ft) {
//        if (mChatFragment == null) {
//            mChatFragment = new ChatFragment(mPlayer);
//            ft.add(R.id.fragement_update, mChatFragment);
//        } else {
//            ft.show(mChatFragment);
//        }
    }

    private void processVideoFragment(FragmentTransaction ft) {
        if (mViedoFragment == null) {
            mViedoFragment = new VodViedoFragment(mPlayer);
            ft.add(R.id.fragment_video_portrait, mViedoFragment);
        } else {
            ft.show(mViedoFragment);
        }

        if (null != mViedoFragment) {
            mViedoFragment.setVideoViewVisible(true);
        }
    }

    private void processQaFragment(FragmentTransaction ft) {
//        if (mQaFragment == null) {
//            mQaFragment = new QaFragment(mPlayer);
//            ft.add(R.id.fragement_update, mQaFragment);
//        } else {
//            ft.show(mQaFragment);
//        }
    }

    private void processVoteFragment(FragmentTransaction ft) {
//        if (mVoteFragment == null) {
//            mVoteFragment = new VoteFragment(mPlayer);
//            ft.add(R.id.fragement_update, mVoteFragment);
//        } else {
//            ft.show(mVoteFragment);
//        }
    }

    private void processDocFragment(FragmentTransaction ft) {
        if (mDocFragment == null) {
            mDocFragment = new VodDocFragment(mPlayer);
            ft.add(R.id.fragement_update, mDocFragment);
        } else {
            ft.show(mDocFragment);
        }
    }

    public void hideFragment(FragmentTransaction ft) {

//        if (mViedoFragment != null) {
//            ft.hide(mViedoFragment);
//        }
        if (mVoteFragment != null) {
            ft.hide(mVoteFragment);
        }
        if (mChatFragment != null) {
            ft.hide(mChatFragment);
        }
        if (mQaFragment != null) {
            ft.hide(mQaFragment);
        }
        if (mDocFragment != null) {
            ft.hide(mDocFragment);
        }
    }

    public void initInitParam() {
        String domain = "sdlbedu.gensee.com";
        String number = getIntent().getStringExtra("number");//"91216993"
        String account = "";
        String pwd = "";
        String nickName = getIntent().getStringExtra("nickName");
        String joinPwd = getIntent().getStringExtra("password");//918210
        String k = "";
        if ("".equals(domain) || "".equals(number) || "".equals(nickName)) {
            KLog.d("域名/编号/昵称 都不能为空");
            return;
        }

        InitParam initParam = new InitParam();
        // 设置域名
        initParam.setDomain(domain);
        if (number.length() == 8 && isNumber(number)) {//此判断是为了测试方便，请勿模仿，实际使用时直接使用其中一种设置
            // 设置编号,8位数字字符串，
            initParam.setNumber(number);
        } else {
            // 如果只有直播间id（混合字符串
            // 如：8d5261f20870499782fb74be021a7e49）可以使用setLiveId("")代替setNumber()
            String liveId = number;
            initParam.setLiveId(liveId);

        }
        initParam.setLoginAccount(account);
        initParam.setLoginPwd(pwd);
        initParam.setNickName(nickName);
        initParam.setVodPwd(joinPwd);
        initParam.setServiceType(serviceType);
        initParam.setK(k);

        try {
            String id = AppHolder.getInstance().user.getId();

            int userId = Integer.parseInt(id);
            initParam.setUserId(1000000000 + userId);

        } catch (Exception e) {
            e.printStackTrace();
        }

        initPlayer(initParam);
    }

    private boolean isNumber(String number) {
        try {
            Long.parseLong(number);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void initPlayer(InitParam p) {
        vodSite = new VodSite(this);
        vodSite.setVodListener(this);
        vodSite.getVodObject(p);
    }


    @Override
    public void onChatHistory(String s, List<ChatMsg> list, int i, boolean b) {

    }

    @Override
    public void onQaHistory(String s, List<QAMsg> list, int i, boolean b) {

    }

    @Override
    public void onVodErr(int i) {

    }

    @Override
    public void onVodObject(String vodId) {
        mHandler.sendMessage(mHandler.obtainMessage(RESULT.ON_GET_VODOBJ, vodId));
    }

    @Override
    public void onVodDetail(VodObject vodObject) {
        subjectTitle = vodObject.getVodSubject();
        if (TextUtils.isEmpty(subjectTitle)) {
            if (getIntent() != null) {
                subjectTitle = getIntent().getStringExtra("title");
            }
        }
        refreshTitle();
    }


    public void dialogLeave() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LiveLessonVodPlayActivity.this);
        builder.setMessage("确定离开");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                // release();
                dialog.dismiss();
                finish();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // mPlayer.leave();
                // mPlayer.release();
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            videoFullScreen();
        } else {
            videoNormalScreen();
        }
    }

    private void videoFullScreen() {
        mRadioGroup.setVisibility(View.GONE);
        mTop2Ll.setVisibility(View.GONE);
        mTop3Rl.setVisibility(View.GONE);

        //设置视频的高度
        int screenWidth = Arad.app.deviceInfo.getScreenWidth();
        mFragmentVideo.getLayoutParams().height = screenWidth;

    }

    private void videoNormalScreen() {

        mRadioGroup.setVisibility(View.VISIBLE);
        mTop2Ll.setVisibility(View.VISIBLE);
        mTop3Rl.setVisibility(View.VISIBLE);

        //设置视频的高度
        int screenWidth = Arad.app.deviceInfo.getScreenWidth();
        mFragmentVideo.getLayoutParams().height = screenWidth * 3 / 4;
    }

    @Override
    public void onBackPressed() {
        if (bJoinSuccess) {
            dialogLeave();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        stopLogService();
        releasePlayer();
        super.onDestroy();
    }

    private void stopPlay() {
        if (mPlayer != null) {
            mPlayer.stop();
        }
    }

    private void releasePlayer() {
        stopPlay();
        if (null != mPlayer) {
            mPlayer.release();
        }
    }

    private void refreshTitle() {
        if (mViedoFragment != null) {
            mViedoFragment.setToolbarTitle(subjectTitle);
        }
    }


}