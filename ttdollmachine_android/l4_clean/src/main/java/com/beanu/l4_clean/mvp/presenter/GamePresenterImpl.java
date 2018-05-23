package com.beanu.l4_clean.mvp.presenter;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Handler;
import android.view.View;

import com.beanu.arad.Arad;
import com.beanu.arad.support.log.KLog;
import com.beanu.arad.utils.ToastUtils;
import com.beanu.l3_common.model.bean.EventModel;
import com.beanu.l3_common.util.AppHolder;
import com.beanu.l4_clean.BuildConfig;
import com.beanu.l4_clean.R;
import com.beanu.l4_clean.model.bean.DollsMachine;
import com.beanu.l4_clean.model.bean.Friend;
import com.beanu.l4_clean.model.bean.OnlinePeople;
import com.beanu.l4_clean.model.bean.SeizeResult;
import com.beanu.l4_clean.mvp.contract.GameContract;
import com.beanu.l4_clean.support.mina.manager.SessionManager;
import com.beanu.l4_clean.util.RongChatRoomUtil;
import com.beanu.l4_clean.util.RongCloudEvent;
import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLMediaPlayer;
import com.pili.pldroid.player.widget.PLVideoView;
import com.qiniu.pili.droid.rtcstreaming.RTCConferenceManager;
import com.qiniu.pili.droid.rtcstreaming.RTCConferenceOptions;
import com.qiniu.pili.droid.rtcstreaming.RTCConferenceState;
import com.qiniu.pili.droid.rtcstreaming.RTCConferenceStateChangedListener;
import com.qiniu.pili.droid.rtcstreaming.RTCMediaSubscribeCallback;
import com.qiniu.pili.droid.rtcstreaming.RTCRemoteAudioEventListener;
import com.qiniu.pili.droid.rtcstreaming.RTCRemoteWindowEventListener;
import com.qiniu.pili.droid.rtcstreaming.RTCStartConferenceCallback;
import com.qiniu.pili.droid.rtcstreaming.RTCUserEventListener;
import com.qiniu.pili.droid.rtcstreaming.RTCVideoWindow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.ChatRoomInfo;
import io.rong.imlib.model.ChatRoomMemberInfo;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;

/**
 * 普通玩法
 * Created by Beanu on 2017/11/18
 */

public class GamePresenterImpl extends GameContract.Presenter implements PLMediaPlayer.OnPreparedListener,
        PLMediaPlayer.OnInfoListener,
        PLMediaPlayer.OnCompletionListener,
        PLMediaPlayer.OnVideoSizeChangedListener,
        PLMediaPlayer.OnErrorListener {

    private String chatRoomId;
    DollsMachine mDollsMachine;

    //连麦，用于纯音频连麦，默认采用软编推流
    private RTCConferenceManager mRTCConferenceManager;
    RTCVideoWindow windowA, windowB;
    private boolean videoFB;

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        //TODO 启动机器连接
//        ClientConnectManager.getInstance().init(mContext);
//        ClientConnectManager.getInstance().connect(mContext);
//    }

    @Override
    public void onResume() {
//        mRTCConferenceManager.startVideoCapture();
    }

    @Override
    public void onPause() {
//        mRTCConferenceManager.stopVideoCapture();
    }

    @Override
    public void onDestroy() {
        //释放连麦 资源
        mRTCConferenceManager.destroy();

        //退出聊天室
        if (mDollsMachine != null)
            quitChatRoom(mDollsMachine.getId());

        stopConference();

        //停止播放
        if (mView.findPLVideo() != null && mView.findPLVideo().isPlaying()) {
            mView.findPLVideo().stopPlayback();
//            mPLVideoView = null;
        }

        SessionManager.getInstance().closeSession();
        SessionManager.getInstance().removeSession();
    }

    @Override
    public void initRTCMedia() {

        //初始化直播
        AVOptions options = new AVOptions();
        options.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);
        options.setInteger(AVOptions.KEY_LIVE_STREAMING, 1);
        options.setInteger(AVOptions.KEY_MEDIACODEC, 0);
        mView.findPLVideo().setAVOptions(options);
        mView.findPLVideo().setOnPreparedListener(this);
        mView.findPLVideo().setOnInfoListener(this);
        mView.findPLVideo().setOnCompletionListener(this);
        mView.findPLVideo().setOnVideoSizeChangedListener(this);
        mView.findPLVideo().setOnErrorListener(this);
        mView.findPLVideo().setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_PAVED_PARENT);
//        mView.findPLVideo().setBufferingIndicator(mView.findLoadingView());
        mView.findPLVideo().setDisplayOrientation(270);

        //====================连麦，当前用户抢到机器之后 连麦====================
        /**
         * Step 4: create streaming manager and set listeners
         */
        mRTCConferenceManager = new RTCConferenceManager(mContext);
        mRTCConferenceManager.setConferenceStateListener(mRTCStreamingStateChangedListener);
        mRTCConferenceManager.setRemoteWindowEventListener(mRTCRemoteWindowEventListener);
        mRTCConferenceManager.setUserEventListener(mRTCUserEventListener);
        mRTCConferenceManager.setDebugLoggingEnabled(BuildConfig.DEBUG);
        mRTCConferenceManager.setMediaSubscribeCallback(mRTCMediaSubscribeCallback);
        mRTCConferenceManager.setRemoteAudioEventListener(mRTCRemoteAudioEventListener);

        /**
         * Step 5: set conference options
         */
        RTCConferenceOptions rtcOptions = new RTCConferenceOptions();
        rtcOptions.setVideoEncodingSizeRatio(RTCConferenceOptions.VIDEO_ENCODING_SIZE_RATIO.RATIO_16_9);
        rtcOptions.setVideoEncodingSizeLevel(RTCConferenceOptions.VIDEO_ENCODING_SIZE_HEIGHT_480);
        rtcOptions.setVideoBitrateRange(800 * 1024, 1024 * 1024);
        rtcOptions.setVideoEncodingFps(15);
        rtcOptions.setHWCodecEnabled(false);
        mRTCConferenceManager.setConferenceOptions(rtcOptions);

        /**
         * Step 6: create the remote windows, must add enough windows for remote users
         */
        windowA = mView.createRTCVideoWindow();
        windowA.setZOrderOnTop(true);
        windowA.setZOrderMediaOverlay(true);
        windowA.setVisibility(View.GONE);

        windowB = mView.createRTCVideoWindowB();
        windowB.setVisibility(View.GONE);

        /**
         * Step 7: add the remote windows
         */
        mRTCConferenceManager.addRemoteWindow(windowA);
        mRTCConferenceManager.addRemoteWindow(windowB);


        /**
         * Step 8: do prepare
         */
        mRTCConferenceManager.prepare(null);
    }

    @Override
    public void initMachineDetail(String id) {
        chatRoomId = id;
        mModel.requestMachineDetail(id).subscribe(new Observer<DollsMachine>() {
            @Override
            public void onSubscribe(Disposable d) {
                mRxManage.add(d);
            }

            @Override
            public void onNext(DollsMachine dollsMachine) {
                mDollsMachine = dollsMachine;

                //开始播放推流
                startPlayVideo();

                //初始化视图
                mView.initUI(dollsMachine);
                //加入聊天室
                joinChatRoom(dollsMachine.getId());


            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
            }
        });
    }


    @Override
    public void startSeize(String dollMaId) {
        mModel.startSeize(dollMaId).subscribe(new Observer<SeizeResult>() {
            @Override
            public void onSubscribe(Disposable d) {
                mRxManage.add(d);
            }

            @Override
            public void onNext(SeizeResult result) {
                mView.refreshSeize(result);
                if (result.isResult()) {
                    RongChatRoomUtil.sendChatRoomMessage(chatRoomId, "", "C2", null);
                    startConference(true);
                }

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                ToastUtils.showShort(e.getMessage());
            }

            @Override
            public void onComplete() {
                userBalance();//更新用户余额
            }
        });
    }

    @Override
    public void beginGame() {

        if (AppHolder.getInstance().user.getCoins() >= mDollsMachine.getPrice() || AppHolder.getInstance().user.getSuccess_num() == 0) {
            startSeize(mDollsMachine.getId());
        } else {
            mView.showRechargeDialog();
        }
    }

    @Override
    public void userBalance() {
        mModel.userBalance().subscribe(new Observer<Map<String, Integer>>() {
            @Override
            public void onSubscribe(Disposable d) {
                mRxManage.add(d);
            }

            @Override
            public void onNext(Map<String, Integer> map) {
                int coin = map.get("coins");
                if (coin >= 0) {
                    AppHolder.getInstance().user.setCoins(coin);
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
                mView.uiUserBalance();
            }
        });
    }

    @Override
    public void gameResult(String id) {
        mModel.gameResult(id).subscribe(new Observer<Map<String, String>>() {
            @Override
            public void onSubscribe(Disposable d) {
                mRxManage.add(d);
            }

            @Override
            public void onNext(Map<String, String> map) {
                String result = map.get("result");
                mView.uiGameResult("1".equals(result));
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {

                //当局结束 停止session
                SessionManager.getInstance().closeSession();
                SessionManager.getInstance().removeSession();
            }
        });
    }

    @Override
    public void shareGame(String gameId) {
        mModel.shareGame(gameId).subscribe(new Observer<Map<String, String>>() {
            @Override
            public void onSubscribe(Disposable d) {
                mRxManage.add(d);
            }

            @Override
            public void onNext(Map<String, String> map) {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {

                userBalance();//更新用户余额
            }
        });
    }

    @Override
    public void machineStart() {
        if (mDollsMachine != null) {
            SessionManager.getInstance().writeToServer(mDollsMachine.getMac() + ",start");
        }
//        SessionManager.getInstance().writeToServer("12345678901234567890");
    }

    @Override
    public void machineLeft() {
        if (mDollsMachine != null) {
            SessionManager.getInstance().writeToServer(mDollsMachine.getMac() + "," + 2);
        }

    }

    @Override
    public void machineRight() {
        if (mDollsMachine != null) {
            SessionManager.getInstance().writeToServer(mDollsMachine.getMac() + "," + 3);
        }

    }

    @Override
    public void machineUp() {
        if (mDollsMachine != null) {
            SessionManager.getInstance().writeToServer(mDollsMachine.getMac() + "," + 1);
        }

    }

    @Override
    public void machineDown() {

        if (mDollsMachine != null) {
            SessionManager.getInstance().writeToServer(mDollsMachine.getMac() + "," + 0);
        }

    }

    @Override
    public void machineGo() {
        if (mDollsMachine != null) {
            SessionManager.getInstance().writeToServer(mDollsMachine.getMac() + ",go");
        }
    }

    @Override
    public void machineSwitch(View view) {

        if (videoFB) {

            windowA.setVisibility(View.VISIBLE);
            windowB.setVisibility(View.GONE);
            videoFB = false;
        } else {
            windowA.setVisibility(View.GONE);
            windowB.setVisibility(View.VISIBLE);
            videoFB = true;
        }
    }

    @Override
    public void joinChatRoom(final String chatRoomId) {
        RongIMClient.getInstance().joinChatRoom(chatRoomId, -1, new RongIMClient.OperationCallback() {
            @Override
            public void onSuccess() {

                //获得聊天室详情
                getChatRoomInfo(chatRoomId);

                //发送 更新消息-更新人数
                RongChatRoomUtil.sendChatRoomMessage(chatRoomId, "", "C0", null);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                KLog.d(errorCode.getMessage());
            }
        });
    }

    @Override
    public void quitChatRoom(final String chatRoomId) {
        RongIMClient.getInstance().quitChatRoom(chatRoomId, new RongIMClient.OperationCallback() {
            @Override
            public void onSuccess() {
                RongChatRoomUtil.sendChatRoomMessage(chatRoomId, "", "C1", null);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }

    @Override
    public void getChatRoomInfo(String chatRoomId) {
        RongIMClient.getInstance().getChatRoomInfo(chatRoomId, 5, ChatRoomInfo.ChatRoomMemberOrder.RC_CHAT_ROOM_MEMBER_DESC, new RongIMClient.ResultCallback<ChatRoomInfo>() {
            @Override
            public void onSuccess(final ChatRoomInfo chatRoomInfo) {


                final List<Friend> friends = new ArrayList<>();
                Observable
                        .fromIterable(chatRoomInfo.getMemberInfo())
                        .flatMap(new Function<ChatRoomMemberInfo, ObservableSource<Friend>>() {
                            @Override
                            public ObservableSource<Friend> apply(ChatRoomMemberInfo chatRoomMemberInfo) throws Exception {
                                return RongCloudEvent.getInstance().getUserInfo(chatRoomMemberInfo.getUserId());
                            }
                        })
                        .onErrorReturnItem(new Friend())
                        .subscribe(new Observer<Friend>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                mRxManage.add(d);
                            }

                            @Override
                            public void onNext(Friend friend) {
                                friends.add(friend);
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                                List<OnlinePeople> onlinePeopleList = new ArrayList<>();
                                for (Friend friend : friends) {
                                    OnlinePeople onlinePeople = new OnlinePeople(friend.getId(), friend.getNickname(), friend.getIcon());
                                    onlinePeopleList.add(onlinePeople);
                                }
                                mView.refreshOnlinePeople(onlinePeopleList, chatRoomInfo.getTotalMemberCount());
                            }
                        });

            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }

    @Override
    public void selfSendMessage(String message) {
        //自己发送消息到聊天室，需要手动更新
        RongChatRoomUtil.sendChatRoomMessage(chatRoomId, message, null, new IRongCallback.ISendMessageCallback() {
            @Override
            public void onAttached(Message message) {

            }

            @Override
            public void onSuccess(Message message) {

                if (message.getConversationType() == Conversation.ConversationType.CHATROOM) {

                    if (message.getContent() instanceof TextMessage) {
                        TextMessage textMessage = (TextMessage) message.getContent();
                        Arad.bus.post(new EventModel.ChatRoomMessage(textMessage.getUserInfo().getName(), textMessage.getContent()));
                    }
                }

            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {

            }
        });
    }

    private void startPlayVideo() {


        //初始化直播(某些机器需要延迟一下)
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mView.findPLVideo().setVideoPath(mDollsMachine.getPush_flow_ahead());
                mView.findPLVideo().start();
            }
        }, 100);
    }


    public boolean isConferenceSuccess() {
        return mRTCConferenceManager.isConferenceStarted();
    }

    private boolean startConference(final boolean ahead) {

        if (mRTCConferenceManager.isConferenceStarted()) {
            return true;
        }

        Bitmap bitmap = mView.findPLVideo().getTextureView().getBitmap();
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        Bitmap captureBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);  // true或false没影响

        mView.findImgCapture().setImageBitmap(captureBitmap);
        mView.findImgCapture().setVisibility(View.VISIBLE);

        mView.conferenceStart();

        new Thread(new Runnable() {
            @Override
            public void run() {
                startConferenceInternal(ahead);
            }
        }).start();

        return true;
    }

    private boolean startConferenceInternal(boolean ahead) {

        String roomToken = ahead ? mDollsMachine.getRoom_token_ahead() : mDollsMachine.getRoom_token_side();
        String roomName = ahead ? mDollsMachine.getRoom_name_ahead() : mDollsMachine.getRoom_name_side();

        if (roomToken == null) {
            mView.conferenceLoading(true);
            ToastUtils.showShort("无法获取房间信息 !");
            return false;
        }

        if (ahead) {
            mRTCConferenceManager.startConference(AppHolder.getInstance().user.getId(), roomName, roomToken, new RTCStartConferenceCallback() {
                @Override
                public void onStartConferenceSuccess() {
                    mView.conferenceLoading(false);
                    mView.conferenceSuccess();
                    mRTCConferenceManager.setStreamStatsEnabled(false);
                }

                @Override
                public void onStartConferenceFailed(int errorCode) {
                    mView.conferenceLoading(true);
                    mView.conferenceFaild();
                    KLog.d(mContext.getResources().getString(R.string.failed_to_start_conference) + errorCode);
                }
            });
        } else {
//            KLog.d("侧面摄像头 开始连麦:" + roomName);
//            mRTCConferenceManager_Back.startConference(AppHolder.getInstance().user.getId(), roomName, roomToken, new RTCStartConferenceCallback() {
//                @Override
//                public void onStartConferenceSuccess() {
//                    KLog.d("侧面摄像头 连麦OK");
//                    mRTCConferenceManager_Back.setStreamStatsEnabled(false);
//                }
//
//                @Override
//                public void onStartConferenceFailed(int errorCode) {
//                    KLog.d("侧面摄像头 失败");
//                    KLog.d(getString(R.string.failed_to_start_conference) + errorCode);
//                }
//            });
        }


        return true;
    }

    private boolean stopConference() {
        if (!mRTCConferenceManager.isConferenceStarted()) {
            return true;
        }
        mRTCConferenceManager.stopConference();
        return true;
    }


    private RTCConferenceStateChangedListener mRTCStreamingStateChangedListener = new RTCConferenceStateChangedListener() {
        @Override
        public void onConferenceStateChanged(RTCConferenceState state, int extra) {
            switch (state) {
                case READY:
                    // You must `StartConference` after `Ready`
//                    ToastUtils.showShort(getString(R.string.ready));
                    KLog.d("READY");

                    break;
                case CONNECT_FAIL:
                    ToastUtils.showShort(mContext.getResources().getString(R.string.failed_to_connect_rtc_server));
                    mView.activityFinish();
                    break;
                case VIDEO_PUBLISH_FAILED:
                case AUDIO_PUBLISH_FAILED:
                    ToastUtils.showShort(mContext.getResources().getString(R.string.failed_to_publish_av_to_rtc) + extra);
                    mView.activityFinish();
                    break;
                case VIDEO_PUBLISH_SUCCESS:
                    ToastUtils.showShort(mContext.getResources().getString(R.string.success_publish_video_to_rtc));
                    break;
                case AUDIO_PUBLISH_SUCCESS:
                    ToastUtils.showShort(mContext.getResources().getString(R.string.success_publish_audio_to_rtc));
                    break;
                case USER_JOINED_AGAIN:
                    ToastUtils.showShort(mContext.getResources().getString(R.string.user_join_other_where));
                    mView.activityFinish();
                    break;
                case USER_KICKOUT_BY_HOST:
                    ToastUtils.showShort(mContext.getResources().getString(R.string.user_kickout_by_host));
                    mView.activityFinish();
                    break;
                case OPEN_CAMERA_FAIL:
                    ToastUtils.showShort(mContext.getResources().getString(R.string.failed_open_camera));
                    break;
                case AUDIO_RECORDING_FAIL:
                    ToastUtils.showShort(mContext.getResources().getString(R.string.failed_open_microphone));
                    break;
                default:
                    return;
            }
        }
    };


    private RTCMediaSubscribeCallback mRTCMediaSubscribeCallback = new RTCMediaSubscribeCallback() {
        @Override
        public boolean isSubscribeVideoStream(String fromUserId) {

            if (fromUserId.equals("kxssz_user02") || fromUserId.equals("kxssz_user01")) {
                return true;
            } else {
                return false;
            }
        }
    };

    private RTCRemoteAudioEventListener mRTCRemoteAudioEventListener = new RTCRemoteAudioEventListener() {
        @Override
        public void onRemoteAudioPublished(String userId) {
            KLog.d("onRemoteAudioPublished userId = " + userId);
        }

        @Override
        public void onRemoteAudioUnpublished(String userId) {
            KLog.d("onRemoteAudioUnpublished userId = " + userId);
        }
    };

    private RTCUserEventListener mRTCUserEventListener = new RTCUserEventListener() {
        @Override
        public void onUserJoinConference(String remoteUserId) {
            KLog.d("onUserJoinConference: " + remoteUserId);
        }

        @Override
        public void onUserLeaveConference(String remoteUserId) {
            KLog.d("onUserLeaveConference: " + remoteUserId);
        }
    };

    private RTCRemoteWindowEventListener mRTCRemoteWindowEventListener = new RTCRemoteWindowEventListener() {
        @Override
        public void onRemoteWindowAttached(RTCVideoWindow window, String remoteUserId) {
            KLog.d("onRemoteWindowAttached: " + remoteUserId);
            if (remoteUserId.equals("kxssz_user02") && window == windowA) {
                mRTCConferenceManager.switchRenderView(windowA.getGLSurfaceView(), windowB.getGLSurfaceView());
                mView.returnView();
            }
        }

        @Override
        public void onRemoteWindowDetached(RTCVideoWindow window, String remoteUserId) {
            KLog.d("onRemoteWindowDetached: " + remoteUserId);

        }

        @Override
        public void onFirstRemoteFrameArrived(String remoteUserId) {
            KLog.d("onFirstRemoteFrameArrived: " + remoteUserId);
        }
    };

    public RTCVideoWindow getRTCVideoWindowA() {
        return windowA;
    }

    public RTCVideoWindow getRTCVideoWindowB() {
        return windowB;
    }

    @Override
    public void onCompletion(PLMediaPlayer plMediaPlayer) {
        KLog.d("onCompletion");
    }

    @Override
    public boolean onError(PLMediaPlayer plMediaPlayer, int i) {
        KLog.d("onError" + i);

        return false;
    }

    @Override
    public boolean onInfo(PLMediaPlayer plMediaPlayer, int i, int i1) {
//        KLog.d("onInfo" + i);

        return false;
    }

    @Override
    public void onPrepared(PLMediaPlayer plMediaPlayer, int i) {
        KLog.d("onPrepared");
//        plMediaPlayer.start();
        mView.findLoadingView().setVisibility(View.INVISIBLE);

    }

    @Override
    public void onVideoSizeChanged(PLMediaPlayer plMediaPlayer, int i, int i1) {
        KLog.d("onVideoSizeChanged,width:" + i + " height:" + i1);

    }

    public DollsMachine getDollsMachine() {
        return mDollsMachine;
    }
}