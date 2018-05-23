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
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beanu.arad.Arad;
import com.beanu.arad.support.log.KLog;
import com.beanu.l3_common.util.AppHolder;
import com.beanu.l3_gensee.fragement.ChatFragment;
import com.beanu.l3_gensee.fragement.DocFragment;
import com.beanu.l3_gensee.fragement.QaFragment;
import com.beanu.l3_gensee.fragement.ViedoFragment;
import com.beanu.l3_gensee.fragement.VoteFragment;
import com.beanu.l3_gensee.playerdemo.LogCatService;
import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.base.BaseSDActivity;
import com.gensee.common.ServiceType;
import com.gensee.entity.InitParam;
import com.gensee.entity.PingEntity;
import com.gensee.entity.UserInfo;
import com.gensee.net.AbsRtAction.ErrCode;
import com.gensee.player.OnPlayListener;
import com.gensee.player.Player;
import com.gensee.taskret.OnTaskRet;
import com.gensee.utils.GenseeLog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 直播课播放
 */
public class LiveLessonPlayActivity extends BaseSDActivity implements OnPlayListener {

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


    private String titleStatus = "";
    private String subjectTitle = "";
    private ServiceType serviceType = ServiceType.TRAINING;

    private boolean bJoinSuccess = false;
    private Intent serviceIntent;
    private Player mPlayer;

    private int inviteMediaType;
    private AlertDialog dialog;

    private FragmentManager mFragmentManager;
    private ChatFragment mChatFragment;//聊天
    private DocFragment mDocFragment;//文档
    private ViedoFragment mViedoFragment;//视频
    private QaFragment mQaFragment;//问答
    private VoteFragment mVoteFragment;//投票

    interface HANDlER {
        int USERINCREASE = 1;
        int USERDECREASE = 2;
        int USERUPDATE = 3;
        int SUCCESSJOIN = 4;
        int SUCCESSLEAVE = 5;
        int CACHING = 6;
        int CACHING_END = 7;
        int RECONNECTING = 8;
        int NOT_START = 9;
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case HANDlER.USERINCREASE:
//                    mChatAdapter.addInfo((UserInfo) (msg.obj));
                    break;
                case HANDlER.USERDECREASE:
//                    mChatAdapter.leaveInfo((UserInfo) (msg.obj));
                    break;
                case HANDlER.USERUPDATE:
//                    mChatAdapter.addInfo((UserInfo) (msg.obj));
                    break;
                case HANDlER.SUCCESSJOIN:
//                    String domain = mEditDomain.getText().toString();
//                    String number = mEditNumber.getText().toString();
//                    String account = mEidtAccount.getText().toString();
//                    String accountPwd = mEidtAccountPwd.getText().toString();
//                    String joinPwd = mEditJoinPwd.getText().toString();
//                    String nickName = mEditNickName.getText().toString();
//                    preferences.edit().putString(ConfigApp.PARAMS_DOMAIN, domain)
//                            .putString(ConfigApp.PARAMS_NUMBER, number)
//                            .putString(ConfigApp.PARAMS_ACCOUNT, account)
//                            .putString(ConfigApp.PARAMS_PWD, accountPwd)
//                            .putString(ConfigApp.PARAMS_NICKNAME, nickName)
//                            .putString(ConfigApp.PARAMS_JOINPWD, joinPwd).commit();
//
//                    mBtnDoc.setEnabled(true);
//                    mBtnQa.setEnabled(true);
//                    mBtnViedo.setEnabled(true);
//                    mBtnVote.setEnabled(true);
//                    mBtnPublicChat.setEnabled(true);
//                    mChatListview.setEnabled(true);
//                    mBtnJoin.setText(R.string.exit);
                    mProgressBar.setVisibility(View.GONE);
                    bJoinSuccess = true;
                    if (mViedoFragment != null) {
                        mViedoFragment.onJoin(bJoinSuccess);
                    }
                    break;
                case HANDlER.SUCCESSLEAVE:
                    dialog();
                    break;
                case HANDlER.CACHING:
                    showTip(true, "正在缓冲...");
                    relTip.setVisibility(View.VISIBLE);
                    break;
                case HANDlER.CACHING_END:
                    showTip(false, "");
                    break;
                case HANDlER.RECONNECTING:
                    showTip(true, "正在重连...");
                    break;
                case HANDlER.NOT_START:
                    refreshTitle();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }

    };


    public static void startActivity(Context context, String number, String password, String nickName) {

        Intent intent = new Intent(context, LiveLessonPlayActivity.class);
        intent.putExtra("number", number);
        intent.putExtra("password", password);
        intent.putExtra("nickName", nickName);
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

        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                initInitParam();
            }
        }, 100);

        disableSlideBack();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putBoolean(ConfigApp.PARAMS_JOINSUCCESS, bJoinSuccess);
//        outState.putBoolean(
//                ConfigApp.PARAMS_VIDEO_FULLSCREEN,
//                getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
//        if (bJoinSuccess) {
//            outState.putString(ConfigApp.PARAMS_DOMAIN, mEditDomain.getText()
//                    .toString());
//            outState.putString(ConfigApp.PARAMS_NUMBER, mEditNumber.getText()
//                    .toString());
//            outState.putString(ConfigApp.PARAMS_ACCOUNT, mEidtAccount.getText()
//                    .toString());
//            outState.putString(ConfigApp.PARAMS_PWD, mEidtAccountPwd.getText()
//                    .toString());
//            outState.putString(ConfigApp.PARAMS_NICKNAME, mEditNickName
//                    .getText().toString());
//            outState.putString(ConfigApp.PARAMS_JOINPWD, mEditJoinPwd.getText()
//                    .toString());
//            if (serviceType == ServiceType.WEBCAST) {
//                outState.putString(ConfigApp.PARAMS_TYPE, ConfigApp.WEBCAST);
//            } else if (serviceType == ServiceType.TRAINING) {
//                outState.putString(ConfigApp.PARAMS_TYPE, ConfigApp.TRAINING);
//            }
//        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
//        boolean bJoinSuccess = savedInstanceState.getBoolean(ConfigApp.PARAMS_JOINSUCCESS);
//        boolean bVideoFullScreen = savedInstanceState.getBoolean(ConfigApp.PARAMS_VIDEO_FULLSCREEN);
//        if (bVideoFullScreen) {
//            videoFullScreen();
//        }
//        if (bJoinSuccess) {
//            mEditDomain.setText(savedInstanceState
//                    .getString(ConfigApp.PARAMS_DOMAIN));
//            mEditNumber.setText(savedInstanceState
//                    .getString(ConfigApp.PARAMS_NUMBER));
//            mEidtAccount.setText(savedInstanceState
//                    .getString(ConfigApp.PARAMS_ACCOUNT));
//            mEidtAccountPwd.setText(savedInstanceState
//                    .getString(ConfigApp.PARAMS_PWD));
//            mEditNickName.setText(savedInstanceState
//                    .getString(ConfigApp.PARAMS_NICKNAME));
//            mEditJoinPwd.setText(savedInstanceState
//                    .getString(ConfigApp.PARAMS_JOINPWD));
//            String type = (String) savedInstanceState
//                    .get(ConfigApp.PARAMS_TYPE);
//            if (type.equals(ConfigApp.WEBCAST)) {
//                serviceType = ServiceType.WEBCAST;
//            } else if (type.equals(ConfigApp.TRAINING)) {
//                serviceType = ServiceType.TRAINING;
//            }
//            initInitParam();
//        }

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

        //设置视频的高度
        int screenWidth = Arad.app.deviceInfo.getScreenWidth();
        mFragmentVideo.getLayoutParams().height = screenWidth / 4 * 3;

        mPlayer = new Player();
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
        if (mChatFragment == null) {
            mChatFragment = new ChatFragment(mPlayer);
            ft.add(R.id.fragement_update, mChatFragment);
        } else {
            ft.show(mChatFragment);
        }
    }

    private void processVideoFragment(FragmentTransaction ft) {
        if (mViedoFragment == null) {
            mViedoFragment = new ViedoFragment(mPlayer);
            ft.add(R.id.fragment_video_portrait, mViedoFragment);
        } else {
            ft.show(mViedoFragment);
        }

        if (null != mViedoFragment) {
            mViedoFragment.setVideoViewVisible(true);
        }
    }

    private void processQaFragment(FragmentTransaction ft) {
        if (mQaFragment == null) {
            mQaFragment = new QaFragment(mPlayer);
            ft.add(R.id.fragement_update, mQaFragment);
        } else {
            ft.show(mQaFragment);
        }
    }

    private void processVoteFragment(FragmentTransaction ft) {
        if (mVoteFragment == null) {
            mVoteFragment = new VoteFragment(mPlayer);
            ft.add(R.id.fragement_update, mVoteFragment);
        } else {
            ft.show(mVoteFragment);
        }
    }

    private void processDocFragment(FragmentTransaction ft) {
        if (mDocFragment == null) {
            mDocFragment = new DocFragment(mPlayer);
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
        // 设置站点登录帐号（根据配置可选）
        initParam.setLoginAccount(account);
        // 设置站点登录密码（根据配置可选）
        initParam.setLoginPwd(pwd);
        // 设置显示昵称 不能为空,请传入正确的昵称，有显示和统计的作用
        // 设置显示昵称，如果设置为空，请确保
        initParam.setNickName(nickName);
        // 设置加入口令（根据配置可选）
        initParam.setJoinPwd(joinPwd);
        // 设置服务类型，如果站点是webcast类型则设置为ServiceType.ST_CASTLINE，
        // training类型则设置为ServiceType.ST_TRAINING
        initParam.setServiceType(serviceType);
        //如果启用第三方认证，必填项，且要正确有效

//		initParam.setUserId(1000000001);//大于1000000000有效
        //站点 系统设置 的 第三方集成 中直播模块 “认证“  启用时请确保”第三方K值“（你们的k值）的正确性 ；如果没有启用则忽略这个参数
        initParam.setK(k);
        try {
            String id = AppHolder.getInstance().user.getId();

            int userId = Integer.parseInt(id);
            initParam.setUserId(1000000000 + userId);

        } catch (Exception e) {
            e.printStackTrace();
        }
//        showTip(true, "正在玩命加入...");

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

    private void showTip(final boolean isShow, final String tip) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (isShow) {
                    if (relTip.getVisibility() != View.VISIBLE) {
                        relTip.setVisibility(View.VISIBLE);
                    }
                    txtTip.setText(tip);
                } else {
                    relTip.setVisibility(View.GONE);
                }

            }
        });
    }

    public void initPlayer(InitParam p) {
        mPlayer.join(getApplicationContext(), p, this);
    }

    public void exit() {
        Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);
        mHomeIntent.addCategory(Intent.CATEGORY_HOME);
        mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        startActivity(mHomeIntent);
    }

    protected void dialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("你已经被踢出");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                finish();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    public void dialogLeave() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LiveLessonPlayActivity.this);
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

    private void showErrorMsg(final String sMsg) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        LiveLessonPlayActivity.this);
                builder.setTitle("提示");
                builder.setMessage(sMsg);
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        finish();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.setCanceledOnTouchOutside(false);
                if (!LiveLessonPlayActivity.this.getFragmentManager().isDestroyed()) {
                    alertDialog.show();
                }
            }
        });

    }

    private void toastMsg(final String msg) {
        if (msg != null) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), msg,
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
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
//        if (null != mChatAdapter) {
//            mChatAdapter.clear();
//        }
        releasePlayer();
        super.onDestroy();
    }

    private void releasePlayer() {
        if (null != mPlayer && bJoinSuccess) {
            mPlayer.leave();
            mPlayer.release(this);
            bJoinSuccess = false;
        }

    }

    private void refreshTitle() {
        if (mViedoFragment != null) {
            mViedoFragment.setToolbarTitle("<font size=10 color=#111ee>" + titleStatus + " </font>" + subjectTitle);
        }
    }


    /**************************************** OnPlayListener ********************************************/
    @Override
    public void onJoin(int result) {
        GenseeLog.d(TAG, "onJoin result = " + result);
        Log.i(TAG, "onJoin result = " + result);
        String msg = null;
        switch (result) {
            case JOIN_OK:
                msg = "加入成功";
                mHandler.sendEmptyMessage(HANDlER.SUCCESSJOIN);
                break;
            case JOIN_CONNECTING:
                msg = "正在加入";
                break;
            case JOIN_CONNECT_FAILED:
                msg = "连接失败";
                break;
            case JOIN_RTMP_FAILED:
                msg = "连接服务器失败";
                break;
            case JOIN_TOO_EARLY:
                msg = "直播还未开始";
                titleStatus = "未开始";
                mHandler.sendEmptyMessage(HANDlER.NOT_START);
                break;
            case JOIN_LICENSE:
                msg = "人数已满";
                break;
            default:
                msg = "加入返回错误" + result;
                break;
        }
        showTip(false, "");
        toastMsg(msg);
    }

    @Override
    public void onUserJoin(UserInfo info) {
        // 用户加入
        mHandler.sendMessage(mHandler.obtainMessage(HANDlER.USERINCREASE, info));
    }

    @Override
    public void onUserLeave(UserInfo info) {
        // 用户离开
        mHandler.sendMessage(mHandler.obtainMessage(HANDlER.USERDECREASE, info));
    }

    @Override
    public void onUserUpdate(UserInfo info) {
        // 用户更新
        mHandler.sendMessage(mHandler.obtainMessage(HANDlER.USERUPDATE, info));
    }

    @Override
    public void onReconnecting() {
        Log.d(TAG, "onReconnecting");
        //断线重连
        mHandler.sendEmptyMessage(HANDlER.RECONNECTING);
    }

    @Override
    public void onLeave(int reason) {
        // 当前用户退出
        // bJoinSuccess = false;
        String msg = null;
        switch (reason) {
            case LEAVE_NORMAL:
                msg = "您已经退出直播间";
                break;
            case LEAVE_KICKOUT:
                msg = "您已被踢出直播间";
                mHandler.sendEmptyMessage(HANDlER.SUCCESSLEAVE);
                break;
            case LEAVE_TIMEOUT:
                msg = "连接超时，您已经退出直播间";
                break;
            case LEAVE_CLOSE:
                msg = "直播已经停止";
                break;
            case LEAVE_UNKNOWN:
                msg = "您已退出直播间，请检查网络、直播间等状态";
                break;
            case LEAVE_RELOGIN:
                msg = "被踢出直播间（相同用户在其他设备上加入）";
                break;
            default:
                break;
        }
        if (null != msg) {
            showErrorMsg(msg);
        }
        // if (mPlayer != null) {
        // mPlayer.release(getApplicationContext());
        // }
        // toastMsg(msg);
    }

    @Override
    public void onCaching(boolean isCaching) {
        Log.d(TAG, "onCaching isCaching = " + isCaching);
//		mHandler.sendEmptyMessage(isCaching ? HANDlER.CACHING
//				: HANDlER.CACHING_END);
//        toastMsg(isCaching ? "正在缓冲" : "缓冲完成");
    }

    @Override
    public void onErr(int errCode) {
        String msg = null;
        switch (errCode) {
            case ErrCode.ERR_DOMAIN:
                msg = "域名domain不正确";
                break;
            case ErrCode.ERR_TIME_OUT:
                msg = "请求超时，稍后重试";
                break;
            case ErrCode.ERR_SITE_UNUSED:
                msg = "站点不可用，请联系客服或相关人员";
                break;
            case ErrCode.ERR_UN_NET:
                msg = "网络不可用，请检查网络连接正常后再试";
                break;
            case ErrCode.ERR_SERVICE:
                msg = "service  错误，请确认是webcast还是training";
                break;
            case ErrCode.ERR_PARAM:
                msg = "initparam参数不全";
                break;
            case ErrCode.ERR_THIRD_CERTIFICATION_AUTHORITY:
                msg = "第三方认证失败";
                break;
            case ErrCode.ERR_NUMBER_UNEXIST:
                msg = "编号不存在";
                break;
            case ErrCode.ERR_TOKEN:
                msg = "口令错误";
                break;
            case ErrCode.ERR_LOGIN:
                msg = "站点登录帐号或登录密码错误";
                break;
            default:
                msg = "错误：errCode = " + errCode;
                break;
        }
        showTip(false, "");
        if (msg != null) {
            toastMsg(msg);
        }
    }

    @Override
    public void onDocSwitch(int i, String s) {

    }

    @Override
    public void onVideoBegin() {
        Log.d(TAG, "onVideoBegin");
//        toastMsg("视频开始");
    }

    @Override
    public void onVideoEnd() {
        Log.d(TAG, "onVideoEnd");
//        toastMsg("视频已停止");
    }

    @Override
    public void onPublish(boolean isPlaying) {
        Log.d(TAG, isPlaying ? "直播（上课）中" : "直播暂停（下课）");
        toastMsg(isPlaying ? "直播（上课）中" : "直播暂停（下课）");
        titleStatus = isPlaying ? "直播中" : "直播暂停";
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                refreshTitle();
            }
        });
    }

    @Override
    public void onPageSize(int pos, int w, int h) {
        //文档开始显示
//        toastMsg("文档分辨率 w = " + w + " h = " + h);
    }

    /**
     * 直播主题
     */
    @Override
    public void onSubject(String subject) {
        Log.d(TAG, "onSubject subject = " + subject);
        subjectTitle = subject;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                refreshTitle();
            }
        });
    }

    /**
     * 在线人数
     *
     * @param total
     */
    public void onRosterTotal(int total) {
        Log.d(TAG, "onRosterTotal total = " + total);
    }

    /**
     * 系统广播消息
     */
    @Override
    public void onPublicMsg(long userId, String msg) {
        Log.d(TAG, "广播消息：" + msg);
        toastMsg("广播消息：" + msg);
    }

    // int INVITE_AUIDO = 1;//only audio
    // int INVITE_VIDEO = 2;//only video
    // int INVITE_MUTI = 3;//both audio and video
    @Override
    public void onInvite(final int type, final boolean isOpen) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                postInvite(type, isOpen);
            }
        });
    }

    private void postInvite(int type, boolean isOpen) {
        if (isOpen) {
            inviteMediaType = type;
            String media = "音频";
            if (type == INVITE_AUIDO) {
                media = "音频";
            } else if (type == INVITE_VIDEO) {
                media = "视频";
            } else {
                media = "音视频";
            }
            if (dialog == null) {
                dialog = new AlertDialog.Builder(this)
                        .setPositiveButton("接受",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        accept(true);
                                    }
                                })
                        .setNegativeButton("拒绝",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        accept(false);
                                    }
                                }).create();
            }
            dialog.setMessage("老师邀请你打开" + media);
            dialog.show();
        } else {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            accept(false);
        }
    }

    private void accept(boolean isAccept) {
        mPlayer.openMic(this, isAccept, null);
    }

    @Override
    public void onMicNotify(int notify) {
        Log.d(TAG, "onMicNotify notify = " + notify);
        switch (notify) {
            case MicNotify.MIC_COLSED:
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        mViedoFragment.onMicColesed();
                    }
                });
                mPlayer.inviteAck(inviteMediaType, false, null);
                break;
            case MicNotify.MIC_OPENED:
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        mViedoFragment.onMicOpened(inviteMediaType);
                    }
                });
                mPlayer.inviteAck(inviteMediaType, true, null);

                break;
            case MicNotify.MIC_OPEN_FAILED:
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        toastMsg("麦克风打开失败，请重试并允许程序打开麦克风");
                    }
                });
                mPlayer.openMic(this, false, null);
                mPlayer.inviteAck(inviteMediaType, false, null);
                break;

            default:
                break;
        }
    }

    @Override
    public void onCameraNotify(int i) {

    }

    @Override
    public void onLiveText(String language, String text) {
        toastMsg("文字直播\n语言：" + language + "\n内容：" + text);
    }

    @Override
    public void onLottery(int cmd, String info) {
        //cmd 1:start, 2: stop, 3: abort
        toastMsg("抽奖\n指令：" + (cmd == 1 ? "开始" : (cmd == 2 ? "结束" : "取消"))
                + "\n结果：" + info);
    }

    @Override
    public void onRollcall(final int timeOut) {
        mHandler.post(new Runnable() {
            private AlertDialog dialog = null;
            private int itimeOut;

            private void rollcallAck(final boolean isAccept) {
                mHandler.removeCallbacks(this);
                mPlayer.rollCallAck(isAccept, new OnTaskRet() {

                    @Override
                    public void onTaskRet(boolean arg0, int arg1, String arg2) {
                        toastMsg(arg0 ? (isAccept ? "本次签到成功" : "您本次未签到") : "操作失败");
                    }
                });
            }

            @Override
            public void run() {
                if (dialog == null) {
                    this.itimeOut = timeOut;
                    dialog = new AlertDialog.Builder(LiveLessonPlayActivity.this).setMessage("").setPositiveButton("签到", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            rollcallAck(true);
                        }
                    }).setCancelable(false).create();
                    dialog.show();
                }
                dialog.setMessage("点名倒计时剩余秒数：" + itimeOut);
                itimeOut--;
                if (itimeOut < 0) {
                    dialog.dismiss();
                    rollcallAck(false);
                } else {
                    mHandler.postDelayed(this, 1000);
                }
            }
        });

    }

    @Override
    public void onFileShare(int cmd, String fileName, String fileUrl) {
        //cmd:1:add, 2: remove
        //TODO 应用层根据需要进行界面显示后可以调用  player的
    }

    @Override
    public void onFileShareDl(int ret, String fileUrl, String filePath) {

    }

    @Override
    public void onVideoSize(int width, int height, boolean iaAs) {
        Log.d(TAG, "onVideoSize");
//        toastMsg("onVideoSize width = " + width + " height = " + height + " isAs = " + iaAs);
    }

    @Override
    public void onAudioLevel(int i) {

    }

    @Override
    public void onModuleFocus(int arg0) {

    }

    @Override
    public void onScreenStatus(boolean isAs) {
        toastMsg("onScreenStatus isAs = " + isAs);
    }

    @Override
    public void onIdcList(List<PingEntity> arg0) {
        mViedoFragment.onIdcList(arg0);
    }

}
