package com.beanu.l4_clean.mvp.presenter;

import android.hardware.Camera;
import android.view.View;

import com.beanu.arad.Arad;
import com.beanu.arad.support.log.KLog;
import com.beanu.arad.utils.ToastUtils;
import com.beanu.l3_common.util.AppHolder;
import com.beanu.l4_clean.R;
import com.beanu.l4_clean.model.bean.Friend;
import com.beanu.l4_clean.model.bean.HelpMeCraw;
import com.beanu.l4_clean.model.bean.HelpUser;
import com.beanu.l4_clean.model.bean.LiveRoom;
import com.beanu.l4_clean.model.bean.OnlinePeople;
import com.beanu.l4_clean.model.bean.SeizeResult;
import com.beanu.l4_clean.mvp.contract.AnchorLivingContract;
import com.beanu.l4_clean.support.droid.StreamUtils;
import com.beanu.l4_clean.support.mina.manager.SessionManager;
import com.beanu.l4_clean.support.qiniu.ExtAudioCapture;
import com.beanu.l4_clean.support.qiniu.ExtVideoCapture;
import com.beanu.l4_clean.util.RongChatRoomUtil;
import com.beanu.l4_clean.util.RongCloudEvent;
import com.google.gson.Gson;
import com.qiniu.pili.droid.rtcstreaming.RTCConferenceOptions;
import com.qiniu.pili.droid.rtcstreaming.RTCConferenceState;
import com.qiniu.pili.droid.rtcstreaming.RTCConferenceStateChangedListener;
import com.qiniu.pili.droid.rtcstreaming.RTCFrameMixedCallback;
import com.qiniu.pili.droid.rtcstreaming.RTCRemoteWindowEventListener;
import com.qiniu.pili.droid.rtcstreaming.RTCStartConferenceCallback;
import com.qiniu.pili.droid.rtcstreaming.RTCStreamingManager;
import com.qiniu.pili.droid.rtcstreaming.RTCUserEventListener;
import com.qiniu.pili.droid.rtcstreaming.RTCVideoWindow;
import com.qiniu.pili.droid.streaming.AVCodecType;
import com.qiniu.pili.droid.streaming.CameraStreamingSetting;
import com.qiniu.pili.droid.streaming.StreamStatusCallback;
import com.qiniu.pili.droid.streaming.StreamingProfile;
import com.qiniu.pili.droid.streaming.StreamingSessionListener;
import com.qiniu.pili.droid.streaming.StreamingState;
import com.qiniu.pili.droid.streaming.StreamingStateChangedListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.ChatRoomInfo;
import io.rong.imlib.model.ChatRoomMemberInfo;

/**
 * Created by Beanu on 2017/12/01
 */

public class AnchorLivingPresenterImpl extends AnchorLivingContract.Presenter {


    //用户相关
    private int mRole;
    private LiveRoom mLiveRoom;

    //直播相关
    private RTCStreamingManager mRTCStreamingManager;
    private ExtVideoCapture mExtVideoCapture;
    private ExtAudioCapture mExtAudioCapture;

    //    private RTCMediaStreamingManager mRTCStreamingManager;
//    private GLSurfaceView mCameraPreviewFrameView;
    private RTCVideoWindow mRTCVideoWindowA;
    private int mCurrentCamFacingIndex;

    //连麦相关
    private boolean mIsActivityPaused = true;
    private boolean mIsConferenceStarted = false;
    private boolean mIsPublishStreamStarted = false;
    private boolean mIsInReadyState = false;


    private StreamingProfile mStreamingProfile;


    @Override
    public void onResume() {
        mIsActivityPaused = false;

        mRTCStreamingManager.resume();
        mExtVideoCapture.setOnPreviewFrameCallback(mOnPreviewFrameCallback);
        mExtAudioCapture.setOnAudioFrameCapturedListener(mOnAudioFrameCapturedListener);
        mExtAudioCapture.startCapture();

        //Step 10
//        mRTCStreamingManager.startCapture();
    }

    @Override
    public void onPause() {
        mIsActivityPaused = true;

        mRTCStreamingManager.pause();
        stopConference();
        mExtVideoCapture.setOnPreviewFrameCallback(null);
        mExtAudioCapture.setOnAudioFrameCapturedListener(null);
        mExtAudioCapture.stopCapture();


        //Step 11
//        mRTCStreamingManager.stopCapture();
//        stopConference();
    }

    @Override
    public void onDestroy() {

        mRTCStreamingManager.destroy();
        RTCStreamingManager.deinit();

        // Step 12: You must call destroy to release some resources when activity destroyed
//        mRTCStreamingManager.destroy();

        //退出聊天室
        quitChatRoom();

        SessionManager.getInstance().closeSession();
        SessionManager.getInstance().removeSession();
    }

    @Override
    public void initRTCMedia() {

        //Step 1: init sdk, you can also move this to Application.onCreate
        RTCStreamingManager.init(Arad.app);


        //Step 2: find & init views （连麦人的视频显示）
//        AspectFrameLayout afl = mView.findAspectLayout();
//        afl.setShowMode(AspectFrameLayout.SHOW_MODE.REAL);
//        mCameraPreviewFrameView = mView.findSurfaceView();
        mView.findLocalPreview().setZOrderOnTop(true);

        CameraStreamingSetting.CAMERA_FACING_ID facingId = chooseCameraFacingId();
        mCurrentCamFacingIndex = facingId.ordinal();

        boolean isSwCodec = true;
        boolean isBeautyEnabled = true;
        boolean isWaterMarkEnabled = false;
        boolean isDebugModeEnabled = false;
        boolean isCustomSettingEnabled = false;
        boolean audioLevelCallback = false;

        //Step 3: config camera settings
        CameraStreamingSetting cameraStreamingSetting = new CameraStreamingSetting();
        cameraStreamingSetting.setCameraFacingId(facingId)
                .setContinuousFocusModeEnabled(true)
                .setRecordingHint(false)
                .setResetTouchFocusDelayInMs(3000)
                .setFocusMode(CameraStreamingSetting.FOCUS_MODE_CONTINUOUS_PICTURE)
                .setCameraPrvSizeLevel(CameraStreamingSetting.PREVIEW_SIZE_LEVEL.MEDIUM)
                .setCameraPrvSizeRatio(CameraStreamingSetting.PREVIEW_SIZE_RATIO.RATIO_16_9);

        if (isBeautyEnabled) {//美颜功能
            cameraStreamingSetting.setBuiltInFaceBeautyEnabled(true); // Using sdk built in face beauty algorithm
            cameraStreamingSetting.setFaceBeautySetting(new CameraStreamingSetting.FaceBeautySetting(0.8f, 0.8f, 0.6f)); // sdk built in face beauty settings
            cameraStreamingSetting.setVideoFilter(CameraStreamingSetting.VIDEO_FILTER_TYPE.VIDEO_FILTER_BEAUTY); // set the beauty on/off
        }

        // Step 4: create streaming manager and set listeners
        AVCodecType codecType = isSwCodec ? AVCodecType.SW_VIDEO_WITH_SW_AUDIO_CODEC : AVCodecType.HW_VIDEO_YUV_AS_INPUT_WITH_HW_AUDIO_CODEC;
//        mRTCStreamingManager = new RTCMediaStreamingManager(mContext, afl, mCameraPreviewFrameView, codecType);
        mRTCStreamingManager = new RTCStreamingManager(mContext, codecType);
        mRTCStreamingManager.setConferenceStateListener(mRTCStreamingStateChangedListener);
        mRTCStreamingManager.setRemoteWindowEventListener(mRTCRemoteWindowEventListener);
        mRTCStreamingManager.setUserEventListener(mRTCUserEventListener);
        mRTCStreamingManager.setDebugLoggingEnabled(isDebugModeEnabled);
//        mRTCStreamingManager.setEncodingMirror(true);


        // Step 5: set conference options
        RTCConferenceOptions options = new RTCConferenceOptions();
        if (mRole == StreamUtils.RTC_ROLE_ANCHOR) {
            options.setVideoEncodingSizeRatio(RTCConferenceOptions.VIDEO_ENCODING_SIZE_RATIO.RATIO_16_9);
            options.setVideoEncodingSizeLevel(RTCConferenceOptions.VIDEO_ENCODING_SIZE_HEIGHT_480);
            options.setVideoBitrateRange(800 * 1024, 1024 * 1024);
            options.setVideoEncodingFps(15);
        } else {
            options.setVideoEncodingSizeRatio(RTCConferenceOptions.VIDEO_ENCODING_SIZE_RATIO.RATIO_16_9);
            options.setVideoEncodingSizeLevel(RTCConferenceOptions.VIDEO_ENCODING_SIZE_HEIGHT_480);
            options.setVideoBitrateRange(800 * 1024, 1024 * 1024);
            options.setVideoEncodingFps(15);
        }
        options.setHWCodecEnabled(!isSwCodec);
        mRTCStreamingManager.setConferenceOptions(options);

        // Step 6: create the remote windows
        RTCVideoWindow windowA = mView.createRTCVideoWindow();
//        windowA.setAbsolutetMixOverlayRect(0, 0, 130, 130);

        windowA.setRelativeMixOverlayRect(0f, 0.5f, 1f, 1f);

        // Step 7: configure the mix stream position and size (only anchor)

        // Step 8: add the remote windows
        mRTCStreamingManager.addRemoteWindow(windowA);
        mRTCVideoWindowA = windowA;
        mRTCVideoWindowA.setVisibility(View.GONE);

        // Step 9: do prepare, anchor should config streaming profile first
        mRTCStreamingManager.setMixedFrameCallback(new RTCFrameMixedCallback() {
            @Override
            public void onVideoFrameMixed(byte[] data, int width, int height, int fmt, long timestamp) {
                KLog.d("Mixed video: " + data.toString() + "  Format: " + fmt);
            }

            @Override
            public void onAudioFrameMixed(byte[] pcm, long timestamp) {
                KLog.d("Mixed audio: " + pcm.toString());
            }
        });

        if (mRole == StreamUtils.RTC_ROLE_ANCHOR) {
            mRTCStreamingManager.setStreamStatusCallback(mStreamStatusCallback);
            mRTCStreamingManager.setStreamingStateListener(mStreamingStateChangedListener);
            mRTCStreamingManager.setStreamingSessionListener(mStreamingSessionListener);
            mStreamingProfile = new StreamingProfile();
            mStreamingProfile.setVideoQuality(StreamingProfile.VIDEO_QUALITY_MEDIUM2)
                    .setAudioQuality(StreamingProfile.AUDIO_QUALITY_MEDIUM1)
                    .setEncoderRCMode(StreamingProfile.EncoderRCModes.QUALITY_PRIORITY)
                    .setPreferredVideoEncodingSize(options.getVideoEncodingWidth(), options.getVideoEncodingHeight());

            mStreamingProfile.setEncodingOrientation(StreamingProfile.ENCODING_ORIENTATION.PORT);
            mRTCStreamingManager.prepare(mStreamingProfile);

        } else {
            KLog.d("开始连麦");
//            mRTCStreamingManager.prepare(cameraStreamingSetting, null);
        }

        mExtVideoCapture = new ExtVideoCapture(mView.findLocalPreview());
        mExtAudioCapture = new ExtAudioCapture();

        startConference();
    }


    private ExtVideoCapture.OnPreviewFrameCallback mOnPreviewFrameCallback = new ExtVideoCapture.OnPreviewFrameCallback() {
        @Override
        public void onPreviewFrameCaptured(byte[] data, int width, int height, int orientation, boolean mirror, int fmt, long tsInNanoTime) {
            // Log.d(TAG, "onPreviewFrameCaptured: " + width + "," + height + ", " + data.length + ", timestamp: " + tsInNanoTime);
            if (mRTCStreamingManager.isStreamingStarted() || mRTCStreamingManager.isConferenceStarted()) {
                mRTCStreamingManager.inputVideoFrame(data, width, height, orientation, mirror, fmt, tsInNanoTime);
            }
        }
    };

    private ExtAudioCapture.OnAudioFrameCapturedListener mOnAudioFrameCapturedListener = new ExtAudioCapture.OnAudioFrameCapturedListener() {
        @Override
        public void onAudioFrameCaptured(byte[] audioData, long tsInNanoTime) {
            // Log.d(TAG, "onAudioFrameCaptured: " + tsInNanoTime);
            if (mRTCStreamingManager.isStreamingStarted() || mRTCStreamingManager.isConferenceStarted()) {
                mRTCStreamingManager.inputAudioFrame(audioData, tsInNanoTime);
            }
        }
    };


    private StreamStatusCallback mStreamStatusCallback = new StreamStatusCallback() {
        @Override
        public void notifyStreamStatusChanged(final StreamingProfile.StreamStatus streamStatus) {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    String stat = "bitrate: " + streamStatus.totalAVBitrate / 1024 + " kbps"
//                            + "\naudio: " + streamStatus.audioFps + " fps"
//                            + "\nvideo: " + streamStatus.videoFps + " fps";
//                    mStatTextView.setText(stat);
//                }
//            });
        }
    };


    private StreamingStateChangedListener mStreamingStateChangedListener = new StreamingStateChangedListener() {
        @Override
        public void onStateChanged(final StreamingState state, Object o) {
            switch (state) {
                case PREPARING:
//                    setStatusText(getString(R.string.preparing));
                    KLog.d("onStateChanged state:" + "preparing");
                    break;
                case READY:
                    mIsInReadyState = true;
//                    setStatusText(getString(R.string.ready));
                    KLog.d("onStateChanged state:" + "ready");

                    startPublishStreaming();

                    break;
                case CONNECTING:
                    KLog.d("onStateChanged state:" + "connecting");
                    break;
                case STREAMING:
//                    setStatusText(getString(R.string.streaming));
                    KLog.d("onStateChanged state:" + "streaming");
                    break;
                case SHUTDOWN:
                    mIsInReadyState = true;
//                    setStatusText(getString(R.string.ready));
                    KLog.d("onStateChanged state:" + "shutdown");
                    break;
                case UNKNOWN:
                    KLog.d("onStateChanged state:" + "unknown");
                    break;
                case SENDING_BUFFER_EMPTY:
                    KLog.d("onStateChanged state:" + "sending buffer empty");
                    break;
                case SENDING_BUFFER_FULL:
                    KLog.d("onStateChanged state:" + "sending buffer full");
                    break;
                case AUDIO_RECORDING_FAIL:
                    KLog.d("onStateChanged state:" + "audio recording failed");
//                    showToast(getString(R.string.failed_open_microphone), Toast.LENGTH_SHORT);
//                    stopPublishStreaming();
                    break;
                case OPEN_CAMERA_FAIL:
                    KLog.d("onStateChanged state:" + "open camera failed");
//                    showToast(getString(R.string.failed_open_camera), Toast.LENGTH_SHORT);
//                    stopPublishStreaming();
                    break;
                case IOERROR:
                    /**
                     * Network-connection is unavailable when `startStreaming`.
                     * You can do reconnecting or just finish the streaming
                     */
                    KLog.d("onStateChanged state:" + "io error");
//                    showToast(getString(R.string.io_error), Toast.LENGTH_SHORT);
//                    sendReconnectMessage();
                    // stopPublishStreaming();
                    break;
                case DISCONNECTED:
                    /**
                     * Network-connection is broken after `startStreaming`.
                     * You can do reconnecting in `onRestartStreamingHandled`
                     */
                    KLog.d("onStateChanged state:" + "disconnected");
//                    setStatusText(getString(R.string.disconnected));
                    // we will process this state in `onRestartStreamingHandled`
                    break;
            }
        }
    };

    private StreamingSessionListener mStreamingSessionListener = new StreamingSessionListener() {
        @Override
        public boolean onRecordAudioFailedHandled(int code) {
            return false;
        }

        /**
         * When the network-connection is broken, StreamingState#DISCONNECTED will notified first,
         * and then invoked this method if the environment of restart streaming is ready.
         *
         * @return true means you handled the event; otherwise, given up and then StreamingState#SHUTDOWN
         * will be notified.
         */
        @Override
        public boolean onRestartStreamingHandled(int code) {
            KLog.d("onRestartStreamingHandled, reconnect ...");
            return mRTCStreamingManager.startStreaming();
        }

        @Override
        public Camera.Size onPreviewSizeSelected(List<Camera.Size> list) {
            return null;
        }

        @Override
        public int onPreviewFpsSelected(List<int[]> list) {
            return -1;
        }
    };


    @Override
    public void helpMeList() {
        String logId = mLiveRoom.getLogId();
        mModel.helpMeList(logId).subscribe(new Observer<List<HelpMeCraw>>() {
            @Override
            public void onSubscribe(Disposable d) {
                mRxManage.add(d);
            }

            @Override
            public void onNext(List<HelpMeCraw> helpMeCraws) {
                mView.uiHelpMeList(helpMeCraws);
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
    public void anchorHelpCraw(String id) {
        mModel.anchorAcceptCraw(id).subscribe(new Observer<HelpUser>() {
            @Override
            public void onSubscribe(Disposable d) {
                mRxManage.add(d);
            }

            @Override
            public void onNext(HelpUser helpUser) {

                Gson gson = new Gson();
                String json = gson.toJson(helpUser, HelpUser.class);
                RongChatRoomUtil.sendChatRoomMessage(mLiveRoom.getRoomId(), json, "CZB1", null);

                mView.uiAcceptCraw(helpUser);
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
    public void startSeize() {
        String dollMaId = mLiveRoom.getMachineId();
        mModel.startSeize(dollMaId).subscribe(new Observer<SeizeResult>() {
            @Override
            public void onSubscribe(Disposable d) {
                mRxManage.add(d);
            }

            @Override
            public void onNext(SeizeResult result) {
                mView.refreshSeize(result.isResult());
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
    public void machineStart() {
        if (mLiveRoom != null) {
            SessionManager.getInstance().writeToServer(mLiveRoom.getIp() + ":" + mLiveRoom.getPort() + ",start");
        }
    }

    @Override
    public void machineLeft() {
        if (mLiveRoom != null) {
            SessionManager.getInstance().writeToServer(mLiveRoom.getIp() + ":" + mLiveRoom.getPort() + ",2");
        }
    }

    @Override
    public void machineRight() {
        if (mLiveRoom != null) {
            SessionManager.getInstance().writeToServer(mLiveRoom.getIp() + ":" + mLiveRoom.getPort() + ",3");
        }
    }

    @Override
    public void machineUp() {
        if (mLiveRoom != null) {
            SessionManager.getInstance().writeToServer(mLiveRoom.getIp() + ":" + mLiveRoom.getPort() + ",1");
        }
    }

    @Override
    public void machineDown() {
        if (mLiveRoom != null) {
            SessionManager.getInstance().writeToServer(mLiveRoom.getIp() + ":" + mLiveRoom.getPort() + ",0");
        }
    }

    @Override
    public void machineGo() {
        if (mLiveRoom != null) {
            SessionManager.getInstance().writeToServer(mLiveRoom.getIp() + ":" + mLiveRoom.getPort() + ",go");
            mView.actionMachineGO();
        }
    }

    @Override
    public void machineSwitch(View view) {
//        FrameLayout window = (FrameLayout) view;
//        if (window.getChildAt(0).getId() == mCameraPreviewFrameView.getId()) {
//            mRTCStreamingManager.switchRenderView(mCameraPreviewFrameView, mRTCVideoWindowA.getGLSurfaceView());
//        } else {
//            mRTCStreamingManager.switchRenderView(mRTCVideoWindowA.getGLSurfaceView(), mCameraPreviewFrameView);
//        }
    }

    @Override
    public void joinChatRoom() {
        final String chatRoomId = mLiveRoom.getRoomId();
        RongIMClient.getInstance().joinChatRoom(chatRoomId, -1, new RongIMClient.OperationCallback() {
            @Override
            public void onSuccess() {

                //获得聊天室详情
                getChatRoomInfo();

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
    public void quitChatRoom() {
        final String chatRoomId = mLiveRoom.getRoomId();
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
    public void getChatRoomInfo() {
        String chatRoomId = mLiveRoom.getRoomId();
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
    public void exitLive() {
        mModel.exitLive(mLiveRoom.getRoomId(), mLiveRoom.getLogId()).subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {
                mRxManage.add(d);
            }

            @Override
            public void onNext(Object s) {
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
                mView.activityFinish();
                RongChatRoomUtil.sendChatRoomMessage(mLiveRoom.getRoomId(), "", "CZB2", null);

            }
        });
    }


    private boolean startPublishStreaming() {
        if (mIsPublishStreamStarted) {
            return true;
        }
        if (!mIsInReadyState) {
//            showToast(getString(R.string.stream_state_not_ready), Toast.LENGTH_SHORT);
            return false;
        }
//        mProgressDialog.setMessage("正在准备推流... ");
//        mProgressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                startPublishStreamingInternal();
            }
        }).start();
        return true;
    }

    private boolean startPublishStreamingInternal() {
        String publishAddr = "rtmp://pili-publish.sdxiaohia.com/kxssz-online/kxssz_room_xiaohai_kxssz_room_xiaohai_kxssz_room_xiaohai_A9";
        if (publishAddr == null) {
//            dismissProgressDialog();
//            showToast("无法获取房间信息/推流地址 !", Toast.LENGTH_SHORT);
            return false;
        }

        try {
            if (StreamUtils.IS_USING_STREAMING_JSON) {
                mStreamingProfile.setStream(new StreamingProfile.Stream(new JSONObject(publishAddr)));
            } else {
                mStreamingProfile.setPublishUrl(publishAddr);
            }
        } catch (JSONException e) {
            e.printStackTrace();
//            dismissProgressDialog();
            ToastUtils.showShort("无效的推流地址 !");
            return false;
        } catch (URISyntaxException e) {
            e.printStackTrace();
//            dismissProgressDialog();
            ToastUtils.showShort("无效的推流地址 !");
            return false;
        }

        mRTCStreamingManager.setStreamingProfile(mStreamingProfile);
        if (!mRTCStreamingManager.startStreaming()) {
//            dismissProgressDialog();
//            showToast(getString(R.string.failed_to_start_streaming), Toast.LENGTH_SHORT);
            return false;
        }
//        dismissProgressDialog();
//        showToast(getString(R.string.start_streaming), Toast.LENGTH_SHORT);
//        updateControlButtonText();
        mIsPublishStreamStarted = true;
        /**
         * Because `startPublishStreaming` need a long time in some weak network
         * So we should check if the activity paused.
         */
        if (mIsActivityPaused) {
            stopPublishStreaming();
        }
        return true;
    }

    private boolean stopPublishStreaming() {
        if (!mIsPublishStreamStarted) {
            return true;
        }
        mRTCStreamingManager.stopStreaming();
        mIsPublishStreamStarted = false;
//        showToast(getString(R.string.stop_streaming), Toast.LENGTH_SHORT);
//        updateControlButtonText();
        return false;
    }

    private RTCConferenceStateChangedListener mRTCStreamingStateChangedListener = new RTCConferenceStateChangedListener() {
        @Override
        public void onConferenceStateChanged(RTCConferenceState state, int extra) {
            switch (state) {
                case READY:
                    // You must `StartConference` after `Ready`
                    //开始连麦
                    if (!mIsConferenceStarted) {
                        startConference();
                    } else {
                        stopConference();
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
//                    ToastUtils.showShort(mContext.getResources().getString(R.string.success_publish_video_to_rtc));
                    break;
                case AUDIO_PUBLISH_SUCCESS:
//                    ToastUtils.showShort(mContext.getResources().getString(R.string.success_publish_audio_to_rtc));
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

    private boolean startConference() {
        if (mIsConferenceStarted) {
            return true;
        }
        mView.mediaLoading(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                startConferenceInternal();
            }
        }).start();
        return true;
    }

    private boolean stopConference() {
        if (!mIsConferenceStarted) {
            return true;
        }
        mRTCStreamingManager.stopConference();
        mIsConferenceStarted = false;
        return true;
    }


    private boolean startConferenceInternal() {

        String roomToken = mLiveRoom.getLm_token();
        if (roomToken == null) {
            mView.mediaLoading(false);
            ToastUtils.showShort("无法获取房间信息 !");
            return false;
        }
        mRTCStreamingManager.startConference(AppHolder.getInstance().user.getId(), mLiveRoom.getRoom_name(), roomToken, new RTCStartConferenceCallback() {
            @Override
            public void onStartConferenceSuccess() {
                mView.mediaLoading(false);

                mIsConferenceStarted = true;
                mRTCStreamingManager.setStreamStatsEnabled(false);
                mRTCStreamingManager.setAudioLevelMonitorEnabled(false);
                if (mIsActivityPaused) {
                    stopConference();
                }
            }

            @Override
            public void onStartConferenceFailed(int errorCode) {
                mView.mediaLoading(false);
                ToastUtils.showShort(mContext.getResources().getString(R.string.failed_to_start_conference) + errorCode);
            }
        });
        return true;
    }


    private CameraStreamingSetting.CAMERA_FACING_ID chooseCameraFacingId() {
        if (CameraStreamingSetting.hasCameraFacing(CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_3RD)) {
            return CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_3RD;
        } else if (CameraStreamingSetting.hasCameraFacing(CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_FRONT)) {
            return CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_FRONT;
        } else {
            return CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_BACK;
        }
    }

    public void setRole(int role) {
        mRole = role;
    }

    public void setLiveRoom(LiveRoom liveRoom) {
        mLiveRoom = liveRoom;
    }

    public RTCVideoWindow getRTCVideoWindowA() {
        return mRTCVideoWindowA;
    }
}