package com.beanu.l4_clean.ui.anchor;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.base.ToolBarActivity;
import com.beanu.arad.support.recyclerview.divider.HorizontalDividerItemDecoration;
import com.beanu.arad.support.recyclerview.divider.VerticalDividerItemDecoration;
import com.beanu.arad.utils.ScreenUtils;
import com.beanu.arad.utils.UIUtils;
import com.beanu.l3_common.model.bean.EventModel;
import com.beanu.l3_common.model.bean.User;
import com.beanu.l3_common.util.AppHolder;
import com.beanu.l4_clean.R;
import com.beanu.l4_clean.adapter.IMMessageAdapter;
import com.beanu.l4_clean.adapter.OnlinePeopleAvatarAdapter;
import com.beanu.l4_clean.adapter.binder.HelpMeCrawViewBinder;
import com.beanu.l4_clean.model.bean.HelpMeCraw;
import com.beanu.l4_clean.model.bean.HelpUser;
import com.beanu.l4_clean.model.bean.IMMessage;
import com.beanu.l4_clean.model.bean.LiveRoom;
import com.beanu.l4_clean.model.bean.OnlinePeople;
import com.beanu.l4_clean.mvp.contract.AnchorLivingContract;
import com.beanu.l4_clean.mvp.model.AnchorLivingModelImpl;
import com.beanu.l4_clean.mvp.presenter.AnchorLivingPresenterImpl;
import com.beanu.l4_clean.support.dialog.ResultDialogFragment;
import com.beanu.l4_clean.support.droid.StreamUtils;
import com.beanu.l4_clean.support.mina.manager.ClientConnectManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.qiniu.pili.droid.rtcstreaming.RTCVideoWindow;
import com.qiniu.pili.droid.streaming.widget.AspectFrameLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * 主播 直播页面
 */
public class AnchorLivingActivity extends ToolBarActivity<AnchorLivingPresenterImpl, AnchorLivingModelImpl> implements AnchorLivingContract.View {


    @BindView(R.id.recycle_view_online) RecyclerView mRecycleViewOnline;
    @BindView(R.id.recycle_view_message) RecyclerView mRecycleViewMessage;
    @BindView(R.id.layout_bottom_help_list) RecyclerView mRecycleViewHelpList;
    @BindView(R.id.toolbar_right_people) TextView mPeopleOnline;
    @BindView(R.id.img_crawer_avatar) ImageView mImgCrawerAvatar;
    @BindView(R.id.txt_crawer_name) TextView mTxtCrawerName;
    @BindView(R.id.txt_crawer_status) TextView mTxtCrawerStatus;
    @BindView(R.id.rl_anchor_info) RelativeLayout mRlAnchorInfo;
    @BindView(R.id.layout_control) ConstraintLayout mLayoutControl;
    @BindView(R.id.txt_remaining_time) TextView mTxtRemainingTime;
    @BindView(R.id.rl_control2) ConstraintLayout mRlControl2;
    @BindView(R.id.btn_start) Button mBtnStart;
    @BindView(R.id.btn_up) ImageButton mBtnUp;
    @BindView(R.id.btn_left) ImageButton mBtnLeft;
    @BindView(R.id.btn_right) ImageButton mBtnRight;
    @BindView(R.id.btn_down) ImageButton mBtnDown;
    @BindView(R.id.btn_go) ImageButton mBtnGo;
    @BindView(R.id.img_loading) ImageView mImgLoading;
    @BindView(R.id.img_help_bg) ImageView mImgHelpBg;
    @BindView(R.id.img_help_avatar) ImageView mImgHelpAvatar;
    @BindView(R.id.txt_help_name) TextView mTxtHelpName;
    @BindView(R.id.txt_chat_hit) TextView mTxtChatHit;
    @BindView(R.id.img_chat_avatar) ImageView mImgChatAvatar;
    @BindView(R.id.txt_helpme) TextView mTxtHelpCount;
    @BindView(R.id.LocalPreviewSurface) SurfaceView mSurfaceView;


    private static final int FLIP_MSG = 101;

    //在线人数
    OnlinePeopleAvatarAdapter mOnlinePeopleAvatarAdapter;
    private List<OnlinePeople> mOnlinePeopleList;

    //聊天消息
    IMMessageAdapter mIMMessageAdapter;
    private List<IMMessage> mIMMessageList;

    //帮我抓列表
    MultiTypeAdapter mHelpMeAdater;
    private Items mHelpList;
    private int mHelpCount = 0;

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
            mLayoutControl.setVisibility(View.VISIBLE);
        }

        @Override
        public void onFinish() {
            mBtnStart.setEnabled(true);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_anchor_living);
        ButterKnife.bind(this);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        disableSlideBack();

        //获取角色
        int mRole = getIntent().getIntExtra("role", StreamUtils.RTC_ROLE_ANCHOR);
        mPresenter.setRole(mRole);

        //获取房间信息
        LiveRoom mLiveRoom = getIntent().getParcelableExtra("room");
        mPresenter.setLiveRoom(mLiveRoom);

        //加入聊天室
        mPresenter.joinChatRoom();

        //初始化直播
        mPresenter.initRTCMedia();

        VideoRenderer videoRenderer = mPresenter.getRTCVideoWindowA().b();
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


        //=======================================================================================
        //在线人数
        mOnlinePeopleList = new ArrayList<>();
        mOnlinePeopleAvatarAdapter = new OnlinePeopleAvatarAdapter(this, mOnlinePeopleList);
        mRecycleViewOnline.setAdapter(mOnlinePeopleAvatarAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecycleViewOnline.setLayoutManager(linearLayoutManager);
        mRecycleViewOnline.addItemDecoration(new VerticalDividerItemDecoration.Builder(this).colorResId(android.R.color.transparent).sizeResId(R.dimen.grid_space).build());

        //当前主播
        User user = AppHolder.getInstance().user;
        if (!TextUtils.isEmpty(user.getIcon())) {
            Glide.with(this).load(user.getIcon()).apply(RequestOptions.circleCropTransform()).into(mImgCrawerAvatar);
        }
        mTxtCrawerName.setText(user.getNickname());
        mTxtCrawerStatus.setText("抓中" + user.getSuccess_num() + "次");

        //聊天记录
        mIMMessageList = new ArrayList<>();
        mIMMessageAdapter = new IMMessageAdapter(this, mIMMessageList);
        mRecycleViewMessage.setAdapter(mIMMessageAdapter);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecycleViewMessage.setLayoutManager(linearLayoutManager1);
        mRecycleViewMessage.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).colorResId(android.R.color.transparent).sizeResId(R.dimen.grid_space).build());


        //帮我抓列表 初始化
        mHelpList = new Items();
        mHelpMeAdater = new MultiTypeAdapter(mHelpList);
        mHelpMeAdater.register(HelpMeCraw.class, new HelpMeCrawViewBinder(new HelpMeCrawViewBinder.OnHelpClickListener() {
            @Override
            public void onHelpCLick(int position) {
                HelpMeCraw helpMeCraw = (HelpMeCraw) mHelpList.get(position);
                mPresenter.anchorHelpCraw(helpMeCraw.getReId());
            }
        }));

        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecycleViewHelpList.setLayoutManager(manager);
        mRecycleViewHelpList.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).margin(16, 16).colorResId(R.color.white).size(1).build());
        mRecycleViewHelpList.setAdapter(mHelpMeAdater);


        Arad.bus.register(this);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showExitDialog();
        }
        return false;
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

        } else if (event.getAction() == KeyEvent.ACTION_UP) {
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

    @OnClick({R.id.btn_start, R.id.btn_go, R.id.cameraPreview_afl, R.id.img_back, R.id.btn_help, R.id.RemoteWindowA})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_start:
                mPresenter.startSeize();
                break;
            case R.id.btn_go:
                mPresenter.machineGo();
                break;
            case R.id.cameraPreview_afl://切换主播和娃娃机
                mPresenter.machineSwitch(view);
                break;
            case R.id.img_back:
                showExitDialog();
                break;
            case R.id.btn_help:
                loadHelpList();
                break;
            case R.id.RemoteWindowA:
                mRecycleViewHelpList.setVisibility(View.GONE);
                break;
        }
    }

    private void loadHelpList() {
        mPresenter.helpMeList();
    }

    private void showExitDialog() {
        UIUtils.showAlertDialog(getSupportFragmentManager(), "提示", "当前正在直播中，是否要退出？", "确定", "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                mPresenter.exitLive();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
    }

    @Override
    public void refreshOnlinePeople(List<OnlinePeople> onlinePeopleList, int amount) {
        mOnlinePeopleList.clear();
        mOnlinePeopleList.addAll(onlinePeopleList);
        mOnlinePeopleAvatarAdapter.notifyDataSetChanged();
        mPeopleOnline.setText(amount + "人");
    }

    @Override
    public void uiHelpMeList(List<HelpMeCraw> crawList) {
        mHelpList.clear();
        mHelpList.addAll(crawList);
        mHelpMeAdater.notifyDataSetChanged();
        mRecycleViewHelpList.setVisibility(View.VISIBLE);
    }

    @Override
    public void uiAcceptCraw(HelpUser helpUser) {

        for (Object o : mHelpList) {
            if (o instanceof HelpMeCraw) {
                if (((HelpMeCraw) o).getUserId().equals(helpUser.getUsers_id())) {
                    mHelpList.remove(o);
                    break;
                }
            }
        }
        mHelpMeAdater.notifyDataSetChanged();
        mRecycleViewHelpList.setVisibility(View.GONE);

        mHelpCount--;
        uiUpdateHelpCount();

        if (!TextUtils.isEmpty(helpUser.getUsers_icon())) {
            Glide.with(this).load(helpUser.getUsers_icon()).apply(RequestOptions.circleCropTransform()).into(mImgHelpAvatar);
        }
        mTxtHelpName.setText(helpUser.getUsers_nickName());
        showHelperUser();

        mPresenter.startSeize();
    }

    @Override
    public void refreshSeize(boolean seize) {

        if (seize) {//抢到机器了
            mBtnStart.setEnabled(false);
            mLayoutControl.setVisibility(View.GONE);
            mRlControl2.setVisibility(View.VISIBLE);

            //启动TCP
            ClientConnectManager.getInstance().init(this);
            ClientConnectManager.getInstance().connect(this);
        } else {//没有抢到

        }
    }

    @Override
    public void actionMachineGO() {
        mCountDownTimer.cancel();
    }

    @Override
    public void activityFinish() {
        finish();
    }

    @Override
    public void mediaLoading(final boolean loading) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (loading) {
                    mPresenter.getRTCVideoWindowA().setVisibility(View.INVISIBLE);
                    mImgLoading.setVisibility(View.VISIBLE);
                } else {
                    mPresenter.getRTCVideoWindowA().setVisibility(View.VISIBLE);
                    mImgLoading.setVisibility(View.GONE);
                }
            }
        });

    }

    @Override
    public RTCVideoWindow createRTCVideoWindow() {
        return new RTCVideoWindow(findViewById(R.id.RemoteWindowA), (GLSurfaceView) findViewById(R.id.RemoteGLSurfaceViewA));
    }

    @Override
    public GLSurfaceView findSurfaceView() {
        return findViewById(R.id.cameraPreview_surfaceView);
    }

    @Override
    public AspectFrameLayout findAspectLayout() {
        return findViewById(R.id.cameraPreview_afl);
    }

    @Override
    public SurfaceView findLocalPreview() {
        return mSurfaceView;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventListener(EventModel.ChatRoomInfoUpdate chatRoom) {
        if ("C0".equals(chatRoom.extra) || "C1".equals(chatRoom.extra)) {
            mPresenter.getChatRoomInfo();
        }

        if ("C0".equals(chatRoom.extra)) {
            showPeopleHelpTipAnim(chatRoom.userName + "进来了", chatRoom.userIcon);
        }

        //申请抓娃娃
        if ("CZB0".equals(chatRoom.extra)) {
            showPeopleHelpTipAnim(chatRoom.userName + "申请了抓娃娃", chatRoom.userIcon);
            mHelpCount++;
            uiUpdateHelpCount();
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventListener(EventModel.ChatRoomMessage message) {
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
//            showCurrentPlayer(userId, nickName, icon);
        } else if ("S1".equals(chatRoom.extra)) {
            //当前机器结束了
            showCrawResult(userId, nickName, result);
            hideHelperUser();
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventListener(EventModel.SocketConnect socketConnect) {
        if (socketConnect.success) {

            mPresenter.machineStart();
            //设置操作按钮可用
            mBtnUp.setClickable(true);
            mBtnDown.setClickable(true);
            mBtnLeft.setClickable(true);
            mBtnRight.setClickable(true);
            mBtnGo.setClickable(true);

            //开始倒计时
            mCountDownTimer.start();
        }
    }


    /**
     * 展示抓中结果
     */
    private void showCrawResult(String userId, String userName, String result) {

        mGameCountDownTimer.start();

        if (userId.equals(AppHolder.getInstance().user.getId())) {
            //如果是自己反馈
            if ("0".equals(result)) {

                ResultDialogFragment.show(getSupportFragmentManager(),  R.drawable.dialog_craw_failure, new ResultDialogFragment.Listener() {
                    @Override
                    public void onClickLeft(View view) {
                        mRlControl2.setVisibility(View.GONE);
                        mLayoutControl.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onClickRight(View view) {
                        mGameCountDownTimer.cancel();
                        mPresenter.startSeize();
                    }

                    @Override
                    public void onFinish() {
                        mRlControl2.setVisibility(View.GONE);
                        mLayoutControl.setVisibility(View.VISIBLE);
                    }
                });
            } else {
                ResultDialogFragment.show(getSupportFragmentManager(),  R.drawable.dialog_craw_success, new ResultDialogFragment.Listener() {
                    @Override
                    public void onClickLeft(View view) {
                        mRlControl2.setVisibility(View.GONE);
                        mLayoutControl.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onClickRight(View view) {
                        mGameCountDownTimer.cancel();
                        mPresenter.startSeize();
                    }

                    @Override
                    public void onFinish() {
                        mRlControl2.setVisibility(View.GONE);
                        mLayoutControl.setVisibility(View.VISIBLE);
                    }
                });
            }

        } else {
            //TODO 其他用户看到的结果

        }
    }


    private void uiUpdateHelpCount() {

        if (mHelpCount <= 0) {
            mTxtHelpCount.setVisibility(View.GONE);
        } else {
            mTxtHelpCount.setText(mHelpCount + "");
            mTxtHelpCount.setVisibility(View.VISIBLE);
        }
    }

    private void showHelperUser() {
        mImgHelpBg.setVisibility(View.VISIBLE);
        mImgHelpAvatar.setVisibility(View.VISIBLE);
        mTxtHelpName.setVisibility(View.VISIBLE);
    }


    private void hideHelperUser() {
        mImgHelpBg.setVisibility(View.GONE);
        mImgHelpAvatar.setVisibility(View.GONE);
        mTxtHelpName.setVisibility(View.GONE);
    }

    private void showPeopleHelpTipAnim(String title, String headerUrl) {

        mTxtChatHit.setVisibility(View.VISIBLE);
        mImgChatAvatar.setVisibility(View.VISIBLE);

        if (!TextUtils.isEmpty(headerUrl)) {
            Glide.with(this).load(headerUrl).apply(RequestOptions.circleCropTransform()).into(mImgChatAvatar);
        }

        int width = ScreenUtils.getScreenWidth();

        mTxtChatHit.setX(width);
        mImgChatAvatar.setX(width);

        mTxtChatHit.setText(title);


        int transX = width - 100;
        mTxtChatHit.animate().translationXBy(-transX).setDuration(1000).start();
        mImgChatAvatar.animate().translationXBy(-transX).setDuration(1000).start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mTxtChatHit.setVisibility(View.GONE);
                mImgChatAvatar.setVisibility(View.GONE);
            }
        }, 2000);
    }

}