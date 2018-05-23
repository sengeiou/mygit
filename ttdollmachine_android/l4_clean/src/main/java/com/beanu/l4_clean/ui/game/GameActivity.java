package com.beanu.l4_clean.ui.game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.support.log.KLog;
import com.beanu.arad.support.recyclerview.divider.HorizontalDividerItemDecoration;
import com.beanu.arad.support.recyclerview.divider.VerticalDividerItemDecoration;
import com.beanu.arad.utils.ScreenUtils;
import com.beanu.arad.utils.ToastUtils;
import com.beanu.l2_shareutil.ShareDialogUtil;
import com.beanu.l2_shareutil.share.ShareListener;
import com.beanu.l3_common.base.BaseTTActivity;
import com.beanu.l3_common.model.bean.EventModel;
import com.beanu.l3_common.util.AppHolder;
import com.beanu.l4_clean.R;
import com.beanu.l4_clean.adapter.IMMessageAdapter;
import com.beanu.l4_clean.adapter.OnlinePeopleAvatarAdapter;
import com.beanu.l4_clean.model.bean.DollsMachine;
import com.beanu.l4_clean.model.bean.IMMessage;
import com.beanu.l4_clean.model.bean.OnlinePeople;
import com.beanu.l4_clean.model.bean.SeizeResult;
import com.beanu.l4_clean.mvp.contract.GameContract;
import com.beanu.l4_clean.mvp.model.GameModelImpl;
import com.beanu.l4_clean.mvp.presenter.GamePresenterImpl;
import com.beanu.l4_clean.support.dialog.AlertDialogFragment;
import com.beanu.l4_clean.support.dialog.FreeCardDialogFragment;
import com.beanu.l4_clean.support.dialog.NeutralDialogFragment;
import com.beanu.l4_clean.support.dialog.ResultDialogFragment;
import com.beanu.l4_clean.support.dialog.WaitDialogFragment;
import com.beanu.l4_clean.support.mina.manager.ClientConnectManager;
import com.beanu.l4_clean.support.service.BGMService;
import com.beanu.l4_clean.support.service.MusicService;
import com.beanu.l4_clean.ui.user.RechargeActivity;
import com.beanu.l4_clean.util.KeyBoardUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.pili.pldroid.player.widget.PLVideoTextureView;
import com.qiniu.pili.droid.rtcstreaming.RTCVideoWindow;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import cn.tee3.avd.VideoRenderer;
import master.flame.danmaku.controller.DrawHandler;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDanmakus;
import master.flame.danmaku.danmaku.model.IDisplayer;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.ui.widget.DanmakuView;

import static com.beanu.arad.utils.SizeUtils.sp2px;

/**
 * 抓娃娃页面
 */
public class GameActivity extends BaseTTActivity<GamePresenterImpl, GameModelImpl> implements GameContract.View {

    private static final int FLIP_MSG = 101;
    @BindView(R.id.recycle_view_online) RecyclerView mRecycleViewOnline;
    @BindView(R.id.toolbar_right_people) TextView mPeopleOnline;
    @BindView(R.id.PLVideoView) PLVideoTextureView mPLVideoView;
    //    @BindView(R.id.tab_layout) TabLayout mTabLayout;
    @BindView(R.id.danmaku_view) DanmakuView mDanmakuView;
    @BindView(R.id.rl_crawer_info) RelativeLayout mRlCrawerInfo;
    @BindView(R.id.img_crawer_avatar) ImageView mImgCrawerAvatar;
    @BindView(R.id.txt_crawer_name) TextView mTxtCrawerName;
    @BindView(R.id.txt_crawer_status) TextView mTxtCrawerStatus;
    @BindView(R.id.recycle_view_message) RecyclerView mRecycleViewMessage;
    @BindView(R.id.img_message_control) ImageView mImgMessageControl;
    @BindView(R.id.rl_control1) RelativeLayout mRlControl1;
    @BindView(R.id.btn_up) ImageButton mBtnUp;
    @BindView(R.id.btn_left) ImageButton mBtnLeft;
    @BindView(R.id.btn_right) ImageButton mBtnRight;
    @BindView(R.id.btn_down) ImageButton mBtnDown;
    @BindView(R.id.btn_go) ImageButton mBtnGo;
    @BindView(R.id.txt_remaining_time) TextView mTxtRemainingTime;
    @BindView(R.id.rl_control2) ConstraintLayout mRlControl2;
    @BindView(R.id.btn_send) Button mBtnSend;
    @BindView(R.id.edit_send) EditText mEditSend;
    @BindView(R.id.rl_bottom_message) LinearLayout mRlBottomMessage;
    @BindView(R.id.img_switch) ImageView mSwitchVideo;
    @BindView(R.id.btn_begin) LinearLayout mBtnBegin;
    @BindView(R.id.txt_balance) TextView mTxtBalance;
    @BindView(R.id.txt_price) TextView mTxtPrice;
    @BindView(R.id.img_loading) ImageView mImgLoading;
    @BindView(R.id.img_music_switch) ImageView mImgMusicSwitch;
    @BindView(R.id.video_fragment) FrameLayout mFrameLayoutVideo;
    @BindView(R.id.img_cover) ImageView mImgCapture;
    @BindView(R.id.RemoteWindowA) FrameLayout mFrameLayoutA;
    @BindView(R.id.RemoteWindowB) FrameLayout mFrameLayoutB;
    @BindView(R.id.space) Space mSpace;
    @BindView(R.id.fragment_same) FrameLayout mFragmentSame;
    @BindView(R.id.fragment_record) FrameLayout mFragmentRecord;
    @BindView(R.id.fragment_intro) FrameLayout mFragmentIntro;


    String machineId;
    boolean isPlaying;
    String currentGameId;//当前游戏ID
    boolean isMusicOff = false;

    //弹幕
    private boolean showDanmaku;
    private DanmakuContext danmakuContext;
    private BaseDanmakuParser parser = new BaseDanmakuParser() {
        @Override
        protected IDanmakus parse() {
            return new Danmakus();
        }
    };

    //在线人数
    OnlinePeopleAvatarAdapter mOnlinePeopleAvatarAdapter;
    private List<OnlinePeople> mOnlinePeopleList;

    //聊天消息
    IMMessageAdapter mIMMessageAdapter;
    private List<IMMessage> mIMMessageList;

    protected Handler handler = new Handler();

    //游戏进行中25s倒计时
    CountDownTimer mCountDownTimer = new CountDownTimer(25 * 1000, 1000) {

        public void onTick(long millisUntilFinished) {
            long remaining = millisUntilFinished / 1000;
            mTxtRemainingTime.setText(remaining + "s");
        }

        public void onFinish() {
            mPresenter.machineGo();
        }
    };

    //是否重新游戏5s倒计时
    CountDownTimer mGameCountDownTimer = new CountDownTimer(5 * 1000, 1000) {
        @Override
        public void onTick(long l) {
            mRlControl2.setVisibility(View.GONE);
            mRlControl1.setVisibility(View.VISIBLE);
        }

        @Override
        public void onFinish() {
            mBtnBegin.setEnabled(true);
        }
    };

    //等待结果倒计时10s，10s内没有结果，将去请求接口
    CountDownTimer mGameResultTimer = new CountDownTimer(15 * 1000, 1000) {
        @Override
        public void onTick(long l) {

        }

        @Override
        public void onFinish() {
            if (isPlaying) {
                mPresenter.gameResult(currentGameId);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);

        int height = ScreenUtils.getScreenWidth() * 16 / 12;
        ViewGroup.LayoutParams layoutParams = mFrameLayoutVideo.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = height;
        mFrameLayoutVideo.setLayoutParams(layoutParams);

        ViewGroup.LayoutParams layoutParams_space = mSpace.getLayoutParams();
        layoutParams_space.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams_space.height = height;
        mSpace.setLayoutParams(layoutParams);

        //弹幕
        mDanmakuView.enableDanmakuDrawingCache(true);
        mDanmakuView.setCallback(new DrawHandler.Callback() {
            @Override
            public void prepared() {
                showDanmaku = true;
                mDanmakuView.start();
            }

            @Override
            public void updateTimer(DanmakuTimer timer) {

            }

            @Override
            public void danmakuShown(BaseDanmaku danmaku) {

            }

            @Override
            public void drawingFinished() {

            }
        });

        HashMap<Integer, Integer> maxLinesPair = new HashMap<Integer, Integer>();
        maxLinesPair.put(BaseDanmaku.TYPE_SCROLL_RL, 5); // 滚动弹幕最大显示5行

        danmakuContext = DanmakuContext.create();
        danmakuContext.setDanmakuStyle(IDisplayer.DANMAKU_STYLE_STROKEN, 3)
                .setDanmakuBold(true)
                .setDuplicateMergingEnabled(false)
                .setScrollSpeedFactor(1.2f)
                .setMaximumLines(maxLinesPair);
        mDanmakuView.prepare(parser, danmakuContext);


        //获取机器详情
        machineId = getIntent().getStringExtra("id");
        mPresenter.initRTCMedia();
        mPresenter.initMachineDetail(machineId);

        roateRomoteView(mPresenter.getRTCVideoWindowA().b());
        roateRomoteView(mPresenter.getRTCVideoWindowB().b());

        //在线人数
        mOnlinePeopleList = new ArrayList<>();
        mOnlinePeopleAvatarAdapter = new OnlinePeopleAvatarAdapter(this, mOnlinePeopleList);
        mRecycleViewOnline.setAdapter(mOnlinePeopleAvatarAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecycleViewOnline.setLayoutManager(linearLayoutManager);
        mRecycleViewOnline.addItemDecoration(new VerticalDividerItemDecoration.Builder(this).colorResId(android.R.color.transparent).sizeResId(R.dimen.grid_space).build());


        //聊天记录
        mIMMessageList = new ArrayList<>();
        mIMMessageAdapter = new IMMessageAdapter(this, mIMMessageList);
        mRecycleViewMessage.setAdapter(mIMMessageAdapter);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecycleViewMessage.setLayoutManager(linearLayoutManager1);
        mRecycleViewMessage.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).colorResId(android.R.color.transparent).sizeResId(R.dimen.grid_space).build());


        //开启背景音乐
        Intent ser = new Intent(this, MusicService.class);
        startService(ser);

        Arad.bus.register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Arad.bus.unregister(this);

        //停止音乐服务
        stopMusic();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isPlaying) {
                showPlayingDialog();
                return false;
            } else {
                onBackPressed();
                return true;
            }

        }

        return false;
    }

    public static void startActivity(Context context, String id) {
        Intent intent = new Intent(context, GameActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    @Override
    protected void setStatusBar() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventListener(EventModel.SocketConnect socketConnect) {
        if (socketConnect.success) {
            mPresenter.machineStart();
            if (mPresenter.isConferenceSuccess()) {
                playPrepare();
            }
        }
    }

    private void playPrepare() {

        //设置操作按钮可用
        mBtnUp.setClickable(true);
        mBtnDown.setClickable(true);
        mBtnLeft.setClickable(true);
        mBtnRight.setClickable(true);
        mBtnGo.setClickable(true);

        //开始倒计时
        mCountDownTimer.start();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventListener(EventModel.ChatRoomInfoUpdate chatRoom) {
        if ("C0".equals(chatRoom.extra) || "C1".equals(chatRoom.extra)) {
            mPresenter.getChatRoomInfo(machineId);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventListener(EventModel.ChatRoomMessage message) {

        //弹幕
        addDanmaku(message.content);

        mIMMessageList.add(new IMMessage(message.userName, message.content));
        if (mIMMessageList.size() > 30) {
            mIMMessageList.subList(mIMMessageList.size() - 30, 30);
        }

        mIMMessageAdapter.notifyDataSetChanged();
        mRecycleViewMessage.smoothScrollToPosition(mIMMessageList.size() - 1);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventListener(EventModel.ChatRoomFormServer chatRoom) {

        HashMap<String, String> map = chatRoom.message;
        String userId = map.get("userId");
        String icon = map.get("icon");
        String nickName = map.get("nickName");
        String result = map.get("result");


        if ("S0".equals(chatRoom.extra)) {
            //某个玩家正在玩
            mGameCountDownTimer.cancel();
            showCurrentPlayer(userId, nickName, icon);
        } else if ("S1".equals(chatRoom.extra)) {
            //当前机器结束了
            hideCurrentPlayer();
            showCrawResult(userId, nickName, result);
        } else if ("S3".equals(chatRoom.extra)) {
            //用户释放超时，释放娃娃机
            mBtnGo.setClickable(true);
        }
    }


    @Override
    public boolean setupToolBarLeftButton(View leftButton) {
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isPlaying) {
                    showPlayingDialog();
                } else {
                    onBackPressed();
                }

            }
        });
        return true;
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == FLIP_MSG) {

                int action = msg.arg1;

                switch (action) {
                    case 0:
                        mPresenter.machineDown();
                        break;
                    case 1:
                        mPresenter.machineUp();
                        break;
                    case 2:
                        mPresenter.machineLeft();
                        break;
                    case 3:
                        mPresenter.machineRight();
                        break;
                }
                msg = obtainMessage(FLIP_MSG);
                msg.arg1 = action;
                sendMessageDelayed(msg, 300);//关键处

            }

        }
    };

    private void sendMessage(MotionEvent event, int action) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            Message msg = mHandler.obtainMessage(FLIP_MSG);
            msg.arg1 = action;
            mHandler.sendMessage(msg);

        } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
            mHandler.removeMessages(FLIP_MSG);
        }
    }


    /**
     * 方向键控制
     */
    @OnTouch({R.id.btn_up, R.id.btn_down, R.id.btn_left, R.id.btn_right})
    public boolean onViewTouch(View view, MotionEvent event) {

        switch (view.getId()) {
            case R.id.btn_down:
                sendMessage(event, 0);
                break;
            case R.id.btn_up:
                sendMessage(event, 1);
                break;
            case R.id.btn_left:
                sendMessage(event, 2);
                break;
            case R.id.btn_right:
                sendMessage(event, 3);
                break;
        }

        return false;
    }

    @OnClick({R.id.img_music_switch, R.id.ll_charge, R.id.img_switch, R.id.btn_send, R.id.rl_bottom_message, R.id.btn_begin, R.id.img_message_control, R.id.btn_message, R.id.btn_go})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_music_switch:

                if (!isMusicOff) {
                    isMusicOff = true;
                    mImgMusicSwitch.setImageResource(R.drawable.music_no);
                    stopMusic();
                } else {
                    isMusicOff = false;
                    mImgMusicSwitch.setImageResource(R.drawable.music);
                    startMusic();
                }

                break;
            case R.id.ll_charge:
                startActivity(RechargeActivity.class);

                break;
            case R.id.btn_begin:

                mPresenter.beginGame();

                break;
            case R.id.img_message_control:
                if (mRecycleViewMessage.getVisibility() == View.GONE) {
                    mRecycleViewMessage.setVisibility(View.VISIBLE);

                    mImgMessageControl.setImageResource(R.drawable.shouqi);
                } else {
                    mRecycleViewMessage.setVisibility(View.GONE);
                    mImgMessageControl.setImageResource(R.drawable.zhankai);
                }
                break;
            case R.id.btn_message:
                mRlBottomMessage.setVisibility(View.VISIBLE);

                mEditSend.requestFocus();
                mEditSend.requestFocusFromTouch();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        KeyBoardUtils.showKeyboard(mEditSend);
                    }
                }, 200);


                break;
            case R.id.rl_bottom_message:
                mRlBottomMessage.setVisibility(View.GONE);
                KeyBoardUtils.hideKeyboard(mBtnSend);
                break;

            case R.id.btn_send:

                String message = mEditSend.getText().toString();
                if (!TextUtils.isEmpty(message)) {
                    mPresenter.selfSendMessage(message);
                    mEditSend.setText("");
                }

                break;

            case R.id.img_switch:

                mPresenter.machineSwitch(view);

                break;
            case R.id.btn_go:
                mPresenter.machineGo();

                mCountDownTimer.cancel();
                mGameResultTimer.start();
                mBtnGo.setClickable(false);

                //去掉循环消息
                mHandler.removeMessages(FLIP_MSG);
                break;
        }
    }

    @Override
    public void initUI(DollsMachine dollsMachine) {

        uiUserBalance();
        mTxtPrice.setText(dollsMachine.getPrice() + "/次");

        //更新底部adapter
        Fragment fragment1 = DollsIntroFragment.newInstance(dollsMachine.getDetailUrl());
        Fragment fragment2 = GamePeopleFragment.newInstance(machineId);
        Fragment fragment3 = SameListFragment.newInstance(dollsMachine.getType_id(), dollsMachine.getDoll_id());

        getSupportFragmentManager().beginTransaction().add(R.id.fragment_intro, fragment1, "")
                .add(R.id.fragment_record, fragment2)
                .add(R.id.fragment_same, fragment3).commit();


        if (dollsMachine.getGame_status() == 1) {//有人正在游戏中
            showCurrentPlayer(dollsMachine.getUserId(), dollsMachine.getUser_nickname(), dollsMachine.getUser_icon());
        } else {
            hideCurrentPlayer();
        }

        if (dollsMachine.getGame_status() == 1) {
            mBtnBegin.setEnabled(false);
        } else {
            mBtnBegin.setEnabled(true);
        }

    }

    @Override
    public void refreshOnlinePeople(List<OnlinePeople> onlinePeopleList, int amount) {
        mOnlinePeopleList.clear();
        mOnlinePeopleList.addAll(onlinePeopleList);
        mOnlinePeopleAvatarAdapter.notifyDataSetChanged();
        mPeopleOnline.setText(amount + "人");
    }

    @Override
    public void refreshSeize(SeizeResult seize) {

        if (seize.isResult()) {//抢到机器了

            mSwitchVideo.setVisibility(View.VISIBLE);

            currentGameId = seize.getId();
            mBtnBegin.setEnabled(false);

            mRlControl1.setVisibility(View.GONE);
            mRlControl2.setVisibility(View.VISIBLE);

            //启动机器连接
            ClientConnectManager.getInstance().init(this);
            ClientConnectManager.getInstance().connect(this);
            isPlaying = true;

            startMusic();

        } else {//没有抢到

            mSwitchVideo.setVisibility(View.GONE);
            mBtnBegin.setEnabled(false);

            AlertDialogFragment.show(getSupportFragmentManager(), getResources().getString(R.string.txt_craw_fail), R.drawable.tanchuangface, "换个机器", "继续等待", new AlertDialogFragment.Listener() {
                @Override
                public void onClickLeft(View view) {
                    finish();
                }

                @Override
                public void onClickRight(View view) {
                    //TODO nothing
                }
            });

        }
    }

    @Override
    public void uiUserBalance() {
        mTxtBalance.setText("" + AppHolder.getInstance().user.getCoins());
    }

    @Override
    public void uiGameResult(boolean win) {
        //当前机器结束了
        hideCurrentPlayer();
        if (isPlaying) {
            showCrawResult(AppHolder.getInstance().user.getId(), "", win ? "1" : "0");
        }

        startMusic();
    }

    @Override
    public void activityFinish() {
        finish();
    }

    @Override
    public RTCVideoWindow createRTCVideoWindow() {
        return new RTCVideoWindow(findViewById(R.id.RemoteWindowA), (GLSurfaceView) findViewById(R.id.RemoteGLSurfaceViewA));
    }

    @Override
    public RTCVideoWindow createRTCVideoWindowB() {
        return new RTCVideoWindow(findViewById(R.id.RemoteWindowB), (GLSurfaceView) findViewById(R.id.RemoteGLSurfaceViewB));
    }

    @Override
    public PLVideoTextureView findPLVideo() {
        return mPLVideoView;
    }

    @Override
    public ImageView findLoadingView() {
        return mImgLoading;
    }

    @Override
    public ImageView findImgCapture() {
        return mImgCapture;
    }


    @Override
    public void conferenceLoading(final boolean loading) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (loading) {
                    findPLVideo().setVisibility(View.VISIBLE);
                    mImgLoading.setVisibility(View.INVISIBLE);
                    mImgCapture.setVisibility(View.GONE);
                } else {
                    mImgLoading.setVisibility(View.INVISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mPLVideoView.setVisibility(View.GONE);
                            mImgCapture.setVisibility(View.GONE);
                            KLog.d("连麦成功，取消截图显示");
                        }
                    }, 1000);

                }
            }
        });
    }

    @Override
    public void showRechargeDialog() {
        AlertDialogFragment.show(getSupportFragmentManager(), getResources().getString(R.string.txt_balance_bot_enough), R.drawable.tanhcuang_1, "不玩了", "去充值", new AlertDialogFragment.Listener() {
            @Override
            public void onClickLeft(View view) {

            }

            @Override
            public void onClickRight(View view) {
                startActivity(RechargeActivity.class);
            }
        });
    }

    @Override
    public void conferenceStart() {
        int height = ScreenUtils.getScreenWidth() * 9 / 12;
        ViewGroup.LayoutParams layoutParams = mFrameLayoutA.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = height;
        mFrameLayoutA.setLayoutParams(layoutParams);

        WaitDialogFragment.show(getSupportFragmentManager());
    }

    @Override
    public void conferenceSuccess() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                playPrepare();
                int height1 = ScreenUtils.getScreenWidth() * 16 / 12;
                ViewGroup.LayoutParams layoutParams1 = mFrameLayoutA.getLayoutParams();
                layoutParams1.width = ViewGroup.LayoutParams.MATCH_PARENT;
                layoutParams1.height = height1;
                mFrameLayoutA.setLayoutParams(layoutParams1);

                Fragment fragment = getSupportFragmentManager().findFragmentByTag("waitDialog");
                if (fragment != null && fragment instanceof DialogFragment) {
                    ((DialogFragment) fragment).dismiss();
                }
            }
        });

    }

    @Override
    public void conferenceFaild() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                playPrepare();
                Fragment fragment = getSupportFragmentManager().findFragmentByTag("waitDialog");
                if (fragment != null && fragment instanceof DialogFragment) {
                    ((DialogFragment) fragment).dismiss();
                }
            }
        });
    }

    @Override
    public void returnView() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                mPresenter.getRTCVideoWindowA().setVisibility(View.GONE);
                mPresenter.getRTCVideoWindowB().setVisibility(View.GONE);
                findPLVideo().setVisibility(View.VISIBLE);
                mSwitchVideo.setVisibility(View.GONE);
            }
        }, 1000);
    }


    private void roateRomoteView(VideoRenderer videoRenderer) {

//        VideoRenderer videoRenderer = mPresenter.getRTCVideoWindowA().b();
        videoRenderer.setScalingType(VideoRenderer.ScalingType.Scale_Fill);
        Class myObjectClass = videoRenderer.getClass();
        try {
            Field fieldRendView = myObjectClass.getDeclaredField("glsurfaceViewRenderer");
            fieldRendView.setAccessible(true);
            Object videoClass = fieldRendView.get(videoRenderer);

            Field fieldRender = videoClass.getClass().getDeclaredField("yuvImageRenderer");
            fieldRender.setAccessible(true);
            Object render = fieldRender.get(videoClass);

            Method method = render.getClass().getDeclaredMethod("onRotation", int.class);
            method.setAccessible(true);
            method.invoke(render, 270);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示当前玩家
     */
    private void showCurrentPlayer(String id, String name, String icon) {
        //当前抓娃娃的人
        if (!TextUtils.isEmpty(icon)) {
            Glide.with(this).load(icon).apply(RequestOptions.circleCropTransform()).into(mImgCrawerAvatar);
        }
        mTxtCrawerName.setText(name);
        mTxtCrawerStatus.setText("热抓中");
        mRlCrawerInfo.setVisibility(View.VISIBLE);

        if (!id.equals(AppHolder.getInstance().user.getId())) {
            mBtnBegin.setEnabled(false);
        }
    }

    /**
     * 隐藏当前玩家
     */
    private void hideCurrentPlayer() {
        mRlCrawerInfo.setVisibility(View.GONE);
    }


    /**
     * 展示抓中结果
     */
    private void showCrawResult(String userId, String userName, String result) {

        mGameCountDownTimer.start();
        isPlaying = false;

        //如果是自己反馈
        if (userId.equals(AppHolder.getInstance().user.getId())) {

            mSwitchVideo.setVisibility(View.GONE);

            if ("0".equals(result)) {

                ResultDialogFragment.show(getSupportFragmentManager(), R.drawable.zhua_lose, new ResultDialogFragment.Listener() {
                    @Override
                    public void onClickLeft(View view) {
                        mRlControl2.setVisibility(View.GONE);
                        mRlControl1.setVisibility(View.VISIBLE);
                        mGameResultTimer.cancel();

                    }

                    @Override
                    public void onClickRight(View view) {
                        mGameCountDownTimer.cancel();
                        mGameResultTimer.cancel();

                        mPresenter.startSeize(machineId);
                    }

                    @Override
                    public void onFinish() {
                        mGameResultTimer.cancel();

                        mRlControl2.setVisibility(View.GONE);
                        mRlControl1.setVisibility(View.VISIBLE);
                    }
                });

            } else {
                ResultDialogFragment.show(getSupportFragmentManager(), R.drawable.zhua_success, new ResultDialogFragment.Listener() {
                    @Override
                    public void onClickLeft(View view) {
                        mRlControl2.setVisibility(View.GONE);
                        mRlControl1.setVisibility(View.VISIBLE);
                        mGameResultTimer.cancel();
                        //炫耀一下
//                        onShare();

                    }

                    @Override
                    public void onClickRight(View view) {
                        mGameCountDownTimer.cancel();
                        mGameResultTimer.cancel();

                        mPresenter.startSeize(machineId);
                    }

                    @Override
                    public void onFinish() {
                        mGameResultTimer.cancel();
                        mRlControl2.setVisibility(View.GONE);
                        mRlControl1.setVisibility(View.VISIBLE);
                    }
                });
            }

        } else {
            //TODO 其他用户看到的结果


        }
    }


    //抓中之后 分享
    public void onShare() {

        if (mPresenter.getDollsMachine() != null) {
            String dollUrl = mPresenter.getDollsMachine().getImage_cover();

            Glide.with(this).asBitmap().load(dollUrl).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {

                    Bitmap backBitmap = getBitmapFromAsset(GameActivity.this, "share_bg.png");
                    Bitmap bitmapResult = overlay(backBitmap, resource);

                    ShareDialogUtil.showSharePicDialog(GameActivity.this, bitmapResult, new ShareListener() {
                        @Override
                        public void shareSuccess() {
                            mPresenter.shareGame(currentGameId);
                        }

                        @Override
                        public void shareFailure(Exception e) {
                            e.printStackTrace();
                            ToastUtils.showShort(e.getLocalizedMessage());
                        }

                        @Override
                        public void shareCancel() {

                        }
                    });
                }
            });
        }
    }

    public static Bitmap getBitmapFromAsset(Context context, String filePath) {
        AssetManager assetManager = context.getAssets();

        InputStream istr;
        Bitmap bitmap = null;
        try {
            istr = assetManager.open(filePath);
            bitmap = BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
            // handle exception
        }

        return bitmap;
    }

    /**
     * 把两个位图覆盖合成为一个位图，以底层位图的长宽为基准
     *
     * @return
     */
    public static Bitmap overlay(Bitmap bmp1, Bitmap bmp2) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);

        Bitmap bitmapScal = Bitmap.createScaledBitmap(bmp2, (int) (bmp2.getWidth() * 1.7), (int) (bmp2.getHeight() * 1.7f), true);

        int x = (bmp1.getWidth() - bitmapScal.getWidth()) / 2;
        int y = 780;

        canvas.drawBitmap(bitmapScal, x, y, null);
        canvas.drawBitmap(bmp1, new Matrix(), null);

        return bmOverlay;
    }

    private void showPlayingDialog() {
        if (isPlaying) {
            NeutralDialogFragment dialogFragment = new NeutralDialogFragment();
            dialogFragment.show("提示", "当前游戏未结束", "确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }, getSupportFragmentManager());


            FreeCardDialogFragment.show(getSupportFragmentManager(), 1, new FreeCardDialogFragment.Listener() {
                @Override
                public void onClickLeft(View view) {

                }

                @Override
                public void onClickRight(View view) {

                }
            });

        }
    }


    private void stopMusic() {
        Intent ser = new Intent(this, BGMService.class);
        stopService(ser);

        Intent music = new Intent(this, MusicService.class);
        stopService(music);
    }

    private void startMusic() {

        if (!isMusicOff) {
            if (isPlaying) {
                Intent music = new Intent(this, MusicService.class);
                stopService(music);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent ser = new Intent(GameActivity.this, BGMService.class);
                        startService(ser);
                    }
                }, 1000);

            } else {

                Intent ser = new Intent(this, BGMService.class);
                stopService(ser);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent music = new Intent(GameActivity.this, MusicService.class);
                        startService(music);
                    }
                }, 1000);
            }
        }

    }


    private void addDanmaku(String content) {
        BaseDanmaku danmaku = danmakuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        danmaku.text = content;
        danmaku.padding = 5;
        danmaku.textSize = sp2px(20) * (float) (1 + Math.random());

        danmaku.textColor = Color.BLACK;
        danmaku.setTime(mDanmakuView.getCurrentTime());
        danmaku.textShadowColor = Color.GREEN;

        mDanmakuView.addDanmaku(danmaku);
    }
}