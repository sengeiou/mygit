package com.beanu.l4_clean.mvp.presenter;

import android.os.Handler;
import android.text.TextUtils;
import android.view.View;

import com.beanu.arad.support.log.KLog;
import com.beanu.arad.utils.ToastUtils;
import com.beanu.l3_common.util.AppHolder;
import com.beanu.l4_clean.BuildConfig;
import com.beanu.l4_clean.R;
import com.beanu.l4_clean.model.bean.Friend;
import com.beanu.l4_clean.model.bean.PKMatchDetail;
import com.beanu.l4_clean.mvp.contract.PKGameContract;
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
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.ChatRoomInfo;
import io.rong.imlib.model.ChatRoomMemberInfo;

/**
 * Created by Beanu on 2017/11/28
 */

public class PKGamePresenterImpl extends PKGameContract.Presenter implements PLMediaPlayer.OnPreparedListener,
        PLMediaPlayer.OnInfoListener,
        PLMediaPlayer.OnCompletionListener,
        PLMediaPlayer.OnVideoSizeChangedListener,
        PLMediaPlayer.OnErrorListener {

    private List<Friend> mFriendList;
    private PKMatchDetail mPKMatchDetail;
    private boolean isGrap = false;//是否正在抓

    private RTCConferenceManager mRTCConferenceManager;
    RTCVideoWindow windowA, windowB;
    private boolean videoFB;
    private boolean isInitDetail = false;
    private String macAddress;


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

        stopConference();

        SessionManager.getInstance().closeSession();
        SessionManager.getInstance().removeSession();
    }

    @Override
    public void initPlayVideo() {
        //初始化连麦
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
        windowA = mView.createRTCVideoWindowA();
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


        //初始化第二个直播
        AVOptions options1 = new AVOptions();
        options1.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);
        options1.setInteger(AVOptions.KEY_LIVE_STREAMING, 1);
        options1.setInteger(AVOptions.KEY_MEDIACODEC, 0);
        mView.findPLVideoOther().setAVOptions(options1);
        mView.findPLVideoOther().setOnPreparedListener(this);
        mView.findPLVideoOther().setOnInfoListener(this);
        mView.findPLVideoOther().setOnCompletionListener(this);
        mView.findPLVideoOther().setOnVideoSizeChangedListener(this);
        mView.findPLVideoOther().setOnErrorListener(this);
        mView.findPLVideoOther().setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_FIT_PARENT);
        mView.findPLVideoOther().setDisplayOrientation(270);

    }

    @Override
    public void pkMatchDetail(String matchId) {
        mModel.pkMatchDetail(matchId).subscribe(new Observer<PKMatchDetail>() {
            @Override
            public void onSubscribe(Disposable d) {
                mRxManage.add(d);
            }

            @Override
            public void onNext(PKMatchDetail pkMatchDetail) {
                mPKMatchDetail = pkMatchDetail;
                mView.uiInit(pkMatchDetail);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {

                mView.uiUserBalance();

                isInitDetail = true;
                startPlayVideo();

                if (isInitDetail && isConferenceReady) {
                    startConference();
                }

            }
        });
    }

    @Override
    public void pkMatchGoOn(String matchId, String id) {
        mModel.pkMatchGoOn(matchId, id).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                mRxManage.add(d);
            }

            @Override
            public void onNext(String s) {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                ToastUtils.showShort(e.getMessage());
            }

            @Override
            public void onComplete() {
                userBalance();
            }
        });
    }

    @Override
    public void pkMatchOver(String matchId) {
        mModel.pkMatchOver(matchId).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                mRxManage.add(d);
            }

            @Override
            public void onNext(String s) {

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
    public void machineStart() {
        if (mPKMatchDetail != null) {
            SessionManager.getInstance().writeToServer(getMacAddress() + ",start");
            isGrap = false;
        }
    }

    @Override
    public void machineLeft() {
        if (mPKMatchDetail != null) {
            SessionManager.getInstance().writeToServer(getMacAddress() + ",2");
        }
    }

    @Override
    public void machineRight() {
        if (mPKMatchDetail != null) {
            SessionManager.getInstance().writeToServer(getMacAddress() + ",3");
        }
    }

    @Override
    public void machineUp() {
        if (mPKMatchDetail != null) {
            SessionManager.getInstance().writeToServer(getMacAddress() + ",1");
        }
    }

    @Override
    public void machineDown() {
        if (mPKMatchDetail != null) {
            SessionManager.getInstance().writeToServer(getMacAddress() + ",0");
        }
    }

    @Override
    public void machineGo() {
        if (mPKMatchDetail != null) {
            SessionManager.getInstance().writeToServer(getMacAddress() + ",go");
            isGrap = true;
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
                RongChatRoomUtil.sendChatRoomMessage(chatRoomId, "", "CPK1", null);
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
                                if (friends.size() == 2) {
                                    mFriendList = friends;
                                }
                            }
                        });

            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }


    public List<Friend> getFriendList() {
        return mFriendList;
    }

    private boolean isConferenceReady = false;
    private RTCConferenceStateChangedListener mRTCStreamingStateChangedListener = new RTCConferenceStateChangedListener() {
        @Override
        public void onConferenceStateChanged(RTCConferenceState state, int extra) {
            switch (state) {
                case READY:
                    // You must `StartConference` after `Ready`
                    isConferenceReady = true;
                    KLog.d("READY");
                    if (isInitDetail && isConferenceReady) {
                        startConference();
                    }

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
            KLog.d("remote video published: " + fromUserId);
            /**
             * decided whether subscribe the video stream
             * return true -- do subscribe, return false will ignore the video stream
             */
            return true;
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

    @Override
    public void onCompletion(PLMediaPlayer plMediaPlayer) {

    }

    @Override
    public boolean onError(PLMediaPlayer plMediaPlayer, int i) {
        return false;
    }

    @Override
    public boolean onInfo(PLMediaPlayer plMediaPlayer, int i, int i1) {
        return false;
    }

    @Override
    public void onPrepared(PLMediaPlayer plMediaPlayer, int i) {

    }

    @Override
    public void onVideoSizeChanged(PLMediaPlayer plMediaPlayer, int i, int i1) {

    }

    public PKMatchDetail getPKMatchDetail() {
        return mPKMatchDetail;
    }

    public boolean isGrap() {
        return isGrap;
    }

//    private String getMachineIP(PKMatchDetail pkMatchDetail) {
//        if (pkMatchDetail != null) {
//
//            String ip;
//
//            if (pkMatchDetail.getUserId().equals(AppHolder.getInstance().user.getId())) {
//                ip = mPKMatchDetail.getIp() + ":" + mPKMatchDetail.getPort();
//            } else {
//                ip = mPKMatchDetail.getOppIp() + ":" + mPKMatchDetail.getOppPort();
//            }
//            return ip;
//        }
//        return "";
//    }

    private boolean startConference() {

        if (mRTCConferenceManager.isConferenceStarted()) {
            return true;
        }

        mView.conferenceLoading(true);
//        mView.conferenceStart();

        new Thread(new Runnable() {
            @Override
            public void run() {
                startConferenceInternal();
            }
        }).start();

        return true;
    }

    private boolean startConferenceInternal() {

        String roomToken;
        String roomName;

        if (mPKMatchDetail.getUserId().equals(AppHolder.getInstance().user.getId())) {
            roomToken = mPKMatchDetail.getRoom_token_ahead();
            roomName = mPKMatchDetail.getRoom_name_ahead();
        } else {
            roomToken = mPKMatchDetail.getOpp_room_token_ahead();
            roomName = mPKMatchDetail.getOpp_room_name_ahead();
        }


        if (roomToken == null) {
            mView.conferenceLoading(false);
            ToastUtils.showShort("无法获取房间信息 !");
            return false;
        }

        mRTCConferenceManager.startConference(AppHolder.getInstance().user.getId(), roomName, roomToken, new RTCStartConferenceCallback() {
            @Override
            public void onStartConferenceSuccess() {
                mView.conferenceLoading(false);
//                    mView.conferenceSuccess();
                mRTCConferenceManager.setStreamStatsEnabled(false);
            }

            @Override
            public void onStartConferenceFailed(int errorCode) {
                mView.conferenceLoading(false);
                KLog.d(mContext.getResources().getString(R.string.failed_to_start_conference) + errorCode);
            }
        });

        return true;
    }

    private boolean stopConference() {
        if (!mRTCConferenceManager.isConferenceStarted()) {
            return true;
        }
        mRTCConferenceManager.stopConference();
        return true;
    }


    public RTCVideoWindow getRTCVideoWindowA() {
        return windowA;
    }

    public RTCVideoWindow getRTCVideoWindowB() {
        return windowB;
    }


    private void startPlayVideo() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (mPKMatchDetail.getUserId().equals(AppHolder.getInstance().user.getId())) {

                    mView.findPLVideoOther().setVideoPath(mPKMatchDetail.getOppPushFlowAhead());
                    mView.findPLVideoOther().setTag(R.id.video1, mPKMatchDetail.getOppPushFlowAhead());
                    mView.findPLVideoOther().setTag(R.id.video2, mPKMatchDetail.getOppPushFlowSide());

                    mView.findPLVideoOther().start();

                } else {

                    mView.findPLVideoOther().setVideoPath(mPKMatchDetail.getPushFlowAhead());
                    mView.findPLVideoOther().setTag(R.id.video1, mPKMatchDetail.getPushFlowAhead());
                    mView.findPLVideoOther().setTag(R.id.video2, mPKMatchDetail.getPushFlowSide());

                    mView.findPLVideoOther().start();
                }

            }
        }, 100);
    }

    private String getMacAddress() {
        if (!TextUtils.isEmpty(macAddress)) {
            return macAddress;
        }

        if (mPKMatchDetail.getUserId().equals(AppHolder.getInstance().user.getId())) {
            macAddress = mPKMatchDetail.getMac();
        } else {
            macAddress = mPKMatchDetail.getOppMac();
        }

        return macAddress;
    }
}