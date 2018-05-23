package com.beanu.l4_clean.ui;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.View;

import com.beanu.arad.base.ToolBarActivity;
import com.beanu.l4_clean.R;
import com.qiniu.pili.droid.rtcstreaming.RTCConferenceOptions;
import com.qiniu.pili.droid.rtcstreaming.RTCMediaStreamingManager;
import com.qiniu.pili.droid.rtcstreaming.RTCVideoWindow;
import com.qiniu.pili.droid.streaming.AVCodecType;
import com.qiniu.pili.droid.streaming.CameraStreamingSetting;
import com.qiniu.pili.droid.streaming.widget.AspectFrameLayout;

/**
 * DEMO
 */
public class VideoActivity extends ToolBarActivity {

    RTCMediaStreamingManager mMediaStreamingManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        //初始化七牛云直播
        RTCMediaStreamingManager.init(getApplicationContext());

        //创建推流&连麦核心对象
        AspectFrameLayout afl = findViewById(R.id.AspectLayout);
        afl.setShowMode(AspectFrameLayout.SHOW_MODE.FULL);
        GLSurfaceView localCameraView = findViewById(R.id.LocalPreivew);
        mMediaStreamingManager = new RTCMediaStreamingManager(getApplicationContext(), afl, localCameraView, AVCodecType.SW_VIDEO_WITH_SW_AUDIO_CODEC);

        //创建本地 Camera 的配置
        CameraStreamingSetting cameraStreamingSetting = new CameraStreamingSetting();
        cameraStreamingSetting.setCameraFacingId(CameraStreamingSetting.CAMERA_FACING_ID.CAMERA_FACING_FRONT)
                .setContinuousFocusModeEnabled(true)
                .setRecordingHint(false)
                .setResetTouchFocusDelayInMs(3000)
                .setFocusMode(CameraStreamingSetting.FOCUS_MODE_CONTINUOUS_PICTURE)
                .setCameraPrvSizeLevel(CameraStreamingSetting.PREVIEW_SIZE_LEVEL.MEDIUM)
                .setCameraPrvSizeRatio(CameraStreamingSetting.PREVIEW_SIZE_RATIO.RATIO_16_9)
                .setBuiltInFaceBeautyEnabled(true) // Using sdk built in face beauty algorithm
                .setFaceBeautySetting(new CameraStreamingSetting.FaceBeautySetting(0.8f, 0.8f, 0.6f)) // sdk built in face beauty settings
                .setVideoFilter(CameraStreamingSetting.VIDEO_FILTER_TYPE.VIDEO_FILTER_BEAUTY); // set the beauty on/off


        //配置连麦参数
        RTCConferenceOptions options = new RTCConferenceOptions();
        options.setVideoEncodingSizeRatio(RTCConferenceOptions.VIDEO_ENCODING_SIZE_RATIO.RATIO_16_9);
        options.setVideoEncodingSizeLevel(RTCConferenceOptions.VIDEO_ENCODING_SIZE_HEIGHT_480);
        // 主播／副主播可以配置不同的连麦码率
        options.setVideoBitrateRange(300 * 1000, 800 * 1000);
        mMediaStreamingManager.setConferenceOptions(options);


        //添加远端画面的窗口
        RTCVideoWindow windowA = new RTCVideoWindow(findViewById(R.id.RemoteWindowA), (GLSurfaceView) findViewById(R.id.RemoteGLSurfaceViewA));
        mMediaStreamingManager.addRemoteWindow(windowA);


        mMediaStreamingManager.prepare(cameraStreamingSetting, null);


        //按钮开始
        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMediaStreamingManager.startCapture();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMediaStreamingManager.stopCapture();
    }

}