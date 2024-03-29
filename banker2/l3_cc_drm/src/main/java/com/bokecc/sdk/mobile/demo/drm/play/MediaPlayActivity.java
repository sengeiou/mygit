package com.bokecc.sdk.mobile.demo.drm.play;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.beanu.arad.Arad;
import com.beanu.arad.base.BaseModel;
import com.beanu.arad.base.BasePresenter;
import com.beanu.arad.base.ToolBarActivity;
import com.beanu.arad.support.log.KLog;
import com.beanu.arad.utils.ToastUtils;
import com.beanu.l3_common.model.bean.EventModel;
import com.beanu.l3_common.util.Constants;
import com.bokecc.sdk.mobile.demo.drm.CCApplication;
import com.bokecc.sdk.mobile.demo.drm.R;
import com.bokecc.sdk.mobile.demo.drm.download.DownloadService;
import com.bokecc.sdk.mobile.demo.drm.download.DownloadService.DownloadBinder;
import com.bokecc.sdk.mobile.demo.drm.model.DownloadInfo;
import com.bokecc.sdk.mobile.demo.drm.util.ConfigUtil;
import com.bokecc.sdk.mobile.demo.drm.util.DataSet;
import com.bokecc.sdk.mobile.demo.drm.util.MediaUtil;
import com.bokecc.sdk.mobile.demo.drm.util.ParamsUtil;
import com.bokecc.sdk.mobile.demo.drm.view.PlayChangeVideoPopupWindow;
import com.bokecc.sdk.mobile.demo.drm.view.PlayTopPopupWindow;
import com.bokecc.sdk.mobile.demo.drm.view.PopMenu;
import com.bokecc.sdk.mobile.demo.drm.view.PopMenu.OnItemClickListener;
import com.bokecc.sdk.mobile.demo.drm.view.VerticalSeekBar;
import com.bokecc.sdk.mobile.download.Downloader;
import com.bokecc.sdk.mobile.play.DWIjkMediaPlayer;
import com.bokecc.sdk.mobile.play.DWMediaPlayer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import tv.danmaku.ijk.media.player.IMediaPlayer;

import static com.bokecc.sdk.mobile.demo.drm.util.DataSet.insertVideoPosition;
import static com.bokecc.sdk.mobile.demo.drm.util.DataSet.insertVideosIndex;


/**
 * 视频播放界面
 *
 * @author CC视频
 */
public abstract class MediaPlayActivity<T extends BasePresenter, E extends BaseModel> extends ToolBarActivity<T, E> implements
        IMediaPlayer.OnBufferingUpdateListener,
        DWIjkMediaPlayer.OnInfoListener,
        DWIjkMediaPlayer.OnPreparedListener, DWIjkMediaPlayer.OnErrorListener,
        DWIjkMediaPlayer.OnVideoSizeChangedListener, SurfaceHolder.Callback, SensorEventListener, IMediaPlayer.OnCompletionListener {

    private boolean networkConnected = true;
    private DWIjkMediaPlayer player;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private ProgressBar bufferProgressBar;
    private SeekBar skbProgress;
    private ImageView backPlayList;
    private TextView videoIdText, playDuration, videoDuration;//视频ID，播放时长，视频时长
    private TextView tvDefinition, tvSpeedPlay;//清晰度，
    private PopMenu definitionMenu, speedPlayMenu;
    private LinearLayout playerTopLayout, volumeLayout;
    private LinearLayout playerBottomLayout;
    private AudioManager audioManager;
    private VerticalSeekBar volumeSeekBar;
    private int currentVolume;
    private int maxVolume;
    private TextView subtitleText;

    private boolean isLocalPlay;
    private boolean isPrepared;
    private Map<String, Integer> definitionMap;

    private Handler playerHandler;
    private Timer timer = new Timer();
    private TimerTask timerTask, networkInfoTimerTask;

    private int currentScreenSizeFlag = 1;
    private int currentSpeedFlag = 0;
    private int currrentSubtitleSwitchFlag = 0;
    private int currentDefinitionIndex = 0;
    // 默认设置为普清
    private int defaultDefinition = DWMediaPlayer.NORMAL_DEFINITION;

    private boolean firstInitDefinition = true;
    private String path;

    private Boolean isPlaying = false;
    // 当player未准备好，并且当前activity经过onPause()生命周期时，此值为true
    private boolean isFreeze = false;
    private boolean isSurfaceDestroy = false;

    int currentPosition;
    private Dialog dialog;

    private String[] definitionArray;
    private final String[] screenSizeArray = new String[]{"满屏", "100%", "75%", "50%"};
    private final String[] subtitleSwitchArray = new String[]{"开启", "关闭"};
    private final String[] speedArray = new String[]{"1.0", "1.2", "1.5", "1.8", "2.0"};

//    private final String subtitleExampleURL = "http://dev.bokecc.com/static/font/example.utf8.srt";

    private CCApplication demoApplication;

    private GestureDetector detector;
    private float scrollTotalDistance;
    private int lastPlayPosition, currentPlayPosition;
    private int lastPlayIndex;
    private String videoId;
    private RelativeLayout rlPlay;
    private WindowManager wm;
    private ImageView ivFullscreen;
    // 隐藏界面的线程
    private Runnable hidePlayRunnable = new Runnable() {
        @Override
        public void run() {
            setLayoutVisibility(View.GONE, false);
        }
    };


    //就是这个list是课时的播放列表
    private List<String> videoIds = new ArrayList<>();


    //我添加
    private  SharedPreferences sharedPreferences ;
    private  SharedPreferences.Editor editor;

    public void onCreate() {

        sharedPreferences = getSharedPreferences("PlayIndex",MODE_PRIVATE);
        editor = sharedPreferences.edit();

        // 隐藏标题
        demoApplication = (CCApplication) getApplication();
        demoApplication.getDRMServer().reset();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        detector = new GestureDetector(this, new MyGesture());

        initView();

        initPlayHander();

        initPlayInfo();
        initSpeedPlayMenu();

        if (!isLocalPlay) {
            initNetworkTimerTask();
        }
    }

    @Override
    public void onResume() {
        if (isFreeze) {
            isFreeze = false;
            if (isPrepared) {
                player.start();
            }
        } else {
            if (isPlaying != null && isPlaying.booleanValue() && isPrepared) {
                player.start();
            }
        }
        super.onResume();
        if (!isLocalPlay) {
            sensorManager.registerListener(this,
                    sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    public void onPause() {
        if (isPrepared) {
            // 如果播放器prepare完成，则对播放器进行暂停操作，并记录状态
            if (player.isPlaying()) {
                isPlaying = true;
            } else {
                isPlaying = false;
            }
            player.pause();
        } else {
            // 如果播放器没有prepare完成，则设置isFreeze为true
            isFreeze = true;
        }
        super.onPause();
    }

    @Override
    protected void onStop() {
        if (!isLocalPlay) {
            sensorManager.unregisterListener(this);
            setLandScapeRequestOrientation();
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (timerTask != null) {
            timerTask.cancel();
        }


        playerHandler.removeCallbacksAndMessages(null);
        playerHandler = null;

        alertHandler.removeCallbacksAndMessages(null);
        alertHandler = null;

        updateDataPosition();

        saveLastIndex();//我添加的销毁时保存最后的index到数据库

        if (player != null) {
            player.reset();
            player.release();
            player = null;
        }

        demoApplication.getDRMServer().disconnectCurrentStream();

        if (dialog != null) {
            dialog.dismiss();
        }
        if (!isLocalPlay) {
            networkInfoTimerTask.cancel();
        }
        super.onDestroy();
    }

    private void initNetworkTimerTask() {
        networkInfoTimerTask = new TimerTask() {
            @Override
            public void run() {
                parseNetworkInfo();
            }
        };

        timer.schedule(networkInfoTimerTask, 0, 600);
    }

    enum NetworkStatus {
        WIFI,
        MOBILEWEB,
        NETLESS,
    }

    private NetworkStatus currentNetworkStatus;
    ConnectivityManager cm;

    private void parseNetworkInfo() {
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                if (currentNetworkStatus != null && currentNetworkStatus == NetworkStatus.WIFI) {
                    return;
                } else {
                    currentNetworkStatus = NetworkStatus.WIFI;
                    showWifiToast();
                }

            } else {
                if (currentNetworkStatus != null && currentNetworkStatus == NetworkStatus.MOBILEWEB) {
                    return;
                } else {
                    currentNetworkStatus = NetworkStatus.MOBILEWEB;
                    showMobileDialog();
                }
            }

            startPlayerTimerTask();
            networkConnected = true;
        } else {
            if (currentNetworkStatus != null && currentNetworkStatus == NetworkStatus.NETLESS) {
                return;
            } else {
                currentNetworkStatus = NetworkStatus.NETLESS;
                showNetlessToast();
            }

            if (timerTask != null) {
                timerTask.cancel();
            }

            networkConnected = false;
        }
    }

    private void showWifiToast() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                Toast.makeText(getApplicationContext(), "已切换至wifi", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void showMobileDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(MediaPlayActivity.this);
                AlertDialog dialog = builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                }).setPositiveButton("继续", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setMessage("当前为移动网络，是否继续播放？").create();

                dialog.show();
            }
        });

    }

    private void showNetlessToast() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "当前无网络信号，无法播放", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startPlayerTimerTask() {
        if (timerTask != null) {
            timerTask.cancel();
        }
        timerTask = new TimerTask() {
            @Override
            public void run() {

                if (!isPrepared) {
                    return;
                }

                playerHandler.sendEmptyMessage(0);
            }
        };
        timer.schedule(timerTask, 0, 1000);
    }

    ImageView lockView;
    ImageView ivCenterPlay;
    ImageView ivDownload;
    ImageView ivTopMenu;
    TextView tvChangeVideo;
    ImageView ivBackVideo, ivNextVideo, ivPlay;


    public abstract void showBottomLayout();

    public abstract void hideBottomLayout();

    private void initView() {

        rlPlay = (RelativeLayout) findViewById(R.id.rl_play);

        rlPlay.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!isPrepared) {
                    return true;
                }

                resetHideDelayed();

                // 事件监听交给手势类来处理
                detector.onTouchEvent(event);
                return true;
            }
        });

        rlPlay.setClickable(true);
        rlPlay.setLongClickable(true);
        rlPlay.setFocusable(true);

        ivTopMenu = (ImageView) findViewById(R.id.iv_top_menu);
        ivTopMenu.setOnClickListener(onClickListener);

        surfaceView = (SurfaceView) findViewById(R.id.playerSurfaceView);
        bufferProgressBar = (ProgressBar) findViewById(R.id.bufferProgressBar);

        ivCenterPlay = (ImageView) findViewById(R.id.iv_center_play);
        ivCenterPlay.setOnClickListener(onClickListener);

        backPlayList = (ImageView) findViewById(R.id.backPlayList);
        videoIdText = (TextView) findViewById(R.id.videoIdText);
        ivDownload = (ImageView) findViewById(R.id.iv_download_play);
        ivDownload.setOnClickListener(onClickListener);

        playDuration = (TextView) findViewById(R.id.playDuration);
        videoDuration = (TextView) findViewById(R.id.videoDuration);
        playDuration.setText(ParamsUtil.millsecondsToStr(0));
        videoDuration.setText(ParamsUtil.millsecondsToStr(0));

        ivBackVideo = (ImageView) findViewById(R.id.iv_video_back);
        ivNextVideo = (ImageView) findViewById(R.id.iv_video_next);
        ivPlay = (ImageView) findViewById(R.id.iv_play);

        ivBackVideo.setOnClickListener(onClickListener);
        ivNextVideo.setOnClickListener(onClickListener);
        ivPlay.setOnClickListener(onClickListener);

        tvChangeVideo = (TextView) findViewById(R.id.tv_change_video);
        tvChangeVideo.setOnClickListener(onClickListener);

        tvDefinition = (TextView) findViewById(R.id.tv_definition);

        tvSpeedPlay = (TextView) findViewById(R.id.tv_speed_play);
        tvSpeedPlay.setOnClickListener(onClickListener);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        volumeSeekBar = (VerticalSeekBar) findViewById(R.id.volumeSeekBar);
        volumeSeekBar.setThumbOffset(2);

        volumeSeekBar.setMax(maxVolume);
        volumeSeekBar.setProgress(currentVolume);
        volumeSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);

        skbProgress = (SeekBar) findViewById(R.id.skbProgress);
        skbProgress.setOnSeekBarChangeListener(onSeekBarChangeListener);

        playerTopLayout = (LinearLayout) findViewById(R.id.playerTopLayout);
        volumeLayout = (LinearLayout) findViewById(R.id.volumeLayout);
        playerBottomLayout = (LinearLayout) findViewById(R.id.playerBottomLayout);

        ivFullscreen = (ImageView) findViewById(R.id.iv_fullscreen);

        ivFullscreen.setOnClickListener(onClickListener);
        backPlayList.setOnClickListener(onClickListener);
        tvDefinition.setOnClickListener(onClickListener);

        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); //2.3及以下使用，不然出现只有声音没有图像的问题
        surfaceHolder.setFormat(PixelFormat.RGBA_8888);
        surfaceHolder.addCallback(this);

        subtitleText = (TextView) findViewById(R.id.subtitleText);

        lockView = (ImageView) findViewById(R.id.iv_lock);
        lockView.setSelected(false);
        lockView.setOnClickListener(onClickListener);



    }

    private void initPlayHander() {
        playerHandler = new Handler() {
            public void handleMessage(Message msg) {

                if (player == null) {
                    return;
                }

                // 刷新字幕
//                subtitleText.setText(subtitle.getSubtitleByTime(player.getCurrentPosition()));

                // 更新播放进度
                currentPlayPosition = (int) player.getCurrentPosition();
                int duration = (int) player.getDuration();

                if (duration > 0) {
                    long pos = skbProgress.getMax() * currentPlayPosition / duration;
                    playDuration.setText(ParamsUtil.millsecondsToStr((int) player.getCurrentPosition()));
                    skbProgress.setProgress((int) pos);
                }
            }

            ;
        };


    }

    private void initPlayInfo() {

        // 通过定时器和Handler来更新进度
        if (player == null) {
            isPrepared = false;
            player = new DWIjkMediaPlayer();
            player.reset();
            player.setOnErrorListener(this);
            player.setOnCompletionListener(this);
            player.setOnVideoSizeChangedListener(this);
            player.setOnInfoListener(this);
            player.setDRMServerPort(demoApplication.getDrmServerPort());
            videoId = "";
            isLocalPlay = getIntent().getBooleanExtra("isLocalPlay", false);
            try {

                if (!isLocalPlay) {// 播放线上视频
                    player.setVideoPlayInfo(videoId, ConfigUtil.USERID, ConfigUtil.API_KEY, this);
                    // 设置默认清晰度
                    player.setDefaultDefinition(defaultDefinition);

                } else {// 播放本地已下载视频

                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

                    if (android.os.Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                        path = Environment.getExternalStorageDirectory() + "/".concat(ConfigUtil.DOWNLOAD_DIR).concat("/").concat(videoId).concat(MediaUtil.PCM_FILE_SUFFIX);
                        if (!new File(path).exists()) {
                            return;
                        }
                    }
                }

            } catch (IllegalArgumentException e) {
                Log.e("player error", e.getMessage());
            } catch (SecurityException e) {
                Log.e("player error", e.getMessage());
            } catch (IllegalStateException e) {
                Log.e("player error", e + "");
            }

            // 设置视频字幕
//        subtitle = new Subtitle(new OnSubtitleInitedListener() {
//
//            @Override
//            public void onInited(Subtitle subtitle) {
//                // 初始化字幕控制菜单
//            }
//        });
//        subtitle.initSubtitleResource(subtitleExampleURL);
        }
    }


    //这个list 就是课时的播放列表

    public void refreshPlayList(List<String> videoIds) {
        this.videoIds.clear();
        this.videoIds.addAll(videoIds);



        if (videoIds.size() > 0) {
            videoId = videoIds.get(0);
        } else {
            videoId = "";
        }
        videoIdText.setText(videoId);
        isPrepared = false;
        currentPosition = 0;
        currentPlayPosition = 0;


        if (videoIds.size() > 0) {

            //int index = DataSet.getVideosIndex(videoId);//我添加的

            //int index = videoIds.indexOf(DataSet.getVideosIndex());

            int index = sharedPreferences.getInt(Constants.VIDEOS_INDEX,0);
            if(index == 0){
                videoId = videoIds.get(0);
            }
            videoId = videoIds.get(index);//这里应该是取数据库中的index，原来固定是0，修改了
            if (TextUtils.isEmpty(videoId)) {
                videoId = "";
            }

            player.setDefaultDefinition(defaultDefinition);
            player.setVideoPlayInfo(videoId, ConfigUtil.USERID, ConfigUtil.API_KEY, MediaPlayActivity.this);
            player.setDisplay(surfaceHolder);
            demoApplication.getDRMServer().reset();
            player.prepareAsync();
        }

    }

    private List<String> videoInfos = new ArrayList<>();

    public void refreshPlayListUI(List<String> infoList) {
        videoInfos.clear();
        videoInfos.addAll(infoList);


    }

    private LayoutParams getScreenSizeParams(int position) {
        currentScreenSizeFlag = position;
        int width = 600;
        int height = 400;
        if (isPortrait()) {
            width = wm.getDefaultDisplay().getWidth();
            height = wm.getDefaultDisplay().getHeight() * 2 / 5; //TODO 根据当前布局更改
        } else {
            width = wm.getDefaultDisplay().getWidth();
            height = wm.getDefaultDisplay().getHeight();
        }


        String screenSizeStr = screenSizeArray[position];
        if (screenSizeStr.indexOf("%") > 0) {// 按比例缩放
            int vWidth = player.getVideoWidth();
            if (vWidth == 0) {
                vWidth = 600;
            }

            int vHeight = player.getVideoHeight();
            if (vHeight == 0) {
                vHeight = 400;
            }

            if (vWidth > width || vHeight > height) {
                float wRatio = (float) vWidth / (float) width;
                float hRatio = (float) vHeight / (float) height;
                float ratio = Math.max(wRatio, hRatio);

                width = (int) Math.ceil((float) vWidth / ratio);
                height = (int) Math.ceil((float) vHeight / ratio);
            } else {
                float wRatio = (float) width / (float) vWidth;
                float hRatio = (float) height / (float) vHeight;
                float ratio = Math.min(wRatio, hRatio);

                width = (int) Math.ceil((float) vWidth * ratio);
                height = (int) Math.ceil((float) vHeight * ratio);
            }


            int screenSize = ParamsUtil.getInt(screenSizeStr.substring(0, screenSizeStr.indexOf("%")));
            width = (width * screenSize) / 100;
            height = (height * screenSize) / 100;
        }

        LayoutParams params = new LayoutParams(width, height);
        return params;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            if (player != null) {
                player.setDisplay(surfaceHolder);
                player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                player.setOnBufferingUpdateListener(this);
                player.setOnPreparedListener(this);
                player.setDisplay(holder);
                player.setScreenOnWhilePlaying(true);

                if (isLocalPlay) {
                    player.setDRMVideoPath(path, this);
                }

                demoApplication.getDRMServer().reset();
                player.prepareAsync();
            }
        } catch (Exception e) {
            Log.e("videoPlayer", "error", e);
        }
        Log.i("videoPlayer", "surface created");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        holder.setFixedSize(width, height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (player == null) {
            return;
        }
        if (isPrepared) {
            currentPosition = (int) player.getCurrentPosition();
        }

        isPrepared = false;
        isSurfaceDestroy = true;

        player.stop();
        player.reset();

    }

    private void initTimerTask() {
        if (timerTask != null) {
            timerTask.cancel();
        }

        timerTask = new TimerTask() {
            @Override
            public void run() {

                if (!isPrepared) {
                    return;
                }

                playerHandler.sendEmptyMessage(0);
            }
        };
        timer.schedule(timerTask, 0, 1000);

    }

    //播放视频
    @Override
    public void onPrepared(IMediaPlayer mp) {
        KLog.d("onPrepared");
        initTimerTask();
        isPrepared = true;
        if (!isFreeze) {
            if (isPlaying == null || isPlaying) {
                player.start();
                ivPlay.setImageResource(R.drawable.smallstop_ic);
                player.setSpeed(Float.parseFloat(speedArray[currentSpeedFlag]));

            }
        }

        if (isPlaying != null && !isPlaying) {
            player.pause();
        }

        if (!isLocalPlay) {
            if (currentPosition > 0) {
                player.seekTo(currentPosition);
            } else {
                // TODO: 2018/5/9 获取视频最后的播放位置，如果大于0，就把进度条移到该位置播放
                lastPlayPosition = DataSet.getVideoPosition(videoId);

                if (lastPlayPosition > 0) {
                    player.seekTo(lastPlayPosition);
                }
            }
        }

        videoIdText.setText(player.getVideoTitle());

        definitionMap = player.getDefinitions();
        if (!isLocalPlay) {
            initDefinitionPopMenu();
        }

        bufferProgressBar.setVisibility(View.GONE);
        surfaceHolder.setFixedSize(player.getVideoWidth(), player.getVideoHeight());
        setSurfaceViewLayout();
        videoDuration.setText(ParamsUtil.millsecondsToStr((int) player.getDuration()));
    }

    // 设置surfaceview的布局
    private void setSurfaceViewLayout() {
        LayoutParams params = getScreenSizeParams(currentScreenSizeFlag);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        surfaceView.setLayoutParams(params);
    }

    private void initSpeedPlayMenu() {

        speedPlayMenu = new PopMenu(this, R.drawable.popup, currentSpeedFlag, 400);
        speedPlayMenu.addItems(speedArray);
        speedPlayMenu.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(int position) {
                currentSpeedFlag = position;
                player.setSpeed(Float.parseFloat(speedArray[position]));
            }
        });

    }

    private void initDefinitionPopMenu() {
        if (definitionMap.size() > 1) {
            currentDefinitionIndex = 1;
            Integer[] definitions = new Integer[]{};
            definitions = definitionMap.values().toArray(definitions);
            // 设置默认为普清，所以此处需要判断一下
            for (int i = 0; i < definitions.length; i++) {
                if (definitions[i].intValue() == defaultDefinition) {
                    currentDefinitionIndex = i;
                }
            }

//			firstInitDefinition = false;
        }

        definitionMenu = new PopMenu(this, R.drawable.popdown, currentDefinitionIndex, getResources().getDimensionPixelSize(R.dimen.popmenu_height));
        // 设置清晰度列表
        definitionArray = new String[]{};
        definitionArray = definitionMap.keySet().toArray(definitionArray);

        definitionMenu.addItems(definitionArray);
        definitionMenu.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(int position) {
                try {

                    currentDefinitionIndex = position;
                    defaultDefinition = definitionMap.get(definitionArray[position]);

                    if (isPrepared) {
                        currentPosition = (int) player.getCurrentPosition();
                        if (player.isPlaying()) {
                            isPlaying = true;
                        } else {
                            isPlaying = false;
                        }
                    }

                    isPrepared = false;

                    setLayoutVisibility(View.GONE, false);
                    bufferProgressBar.setVisibility(View.VISIBLE);


                    demoApplication.getDRMServer().disconnectCurrentStream();
                    player.reset();
                    demoApplication.getDRMServer().reset();
                    player.setDefinition(getApplicationContext(), defaultDefinition);
                    player.setDisplay(surfaceHolder);
//                    surfaceView.setVisibility(View.INVISIBLE);
//                    surfaceView.setVisibility(View.VISIBLE);

                } catch (IOException e) {
                    Log.e("player error", e.getMessage());
                }

            }
        });
    }

    @Override
    public void onBufferingUpdate(IMediaPlayer mp, int percent) {
        skbProgress.setSecondaryProgress(percent);
    }

    OnClickListener onClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            resetHideDelayed();

            int i = v.getId();
            if (i == R.id.backPlayList) {
                if (isPortrait() || isLocalPlay) {
                    finish();
                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }

            } else if (i == R.id.iv_fullscreen) {
                if (isPortrait()) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }

            } else if (i == R.id.tv_definition) {
                definitionMenu.showAsDropDown(v);

            } else if (i == R.id.tv_speed_play) {
                speedPlayMenu.showAsDropDown(v);

            } else if (i == R.id.iv_lock) {
                if (lockView.isSelected()) {
                    lockView.setSelected(false);
                    setLayoutVisibility(View.VISIBLE, true);
                    toastInfo("已解开屏幕");
                } else {
                    lockView.setSelected(true);
                    setLandScapeRequestOrientation();
                    setLayoutVisibility(View.GONE, true);
                    lockView.setVisibility(View.VISIBLE);
                    toastInfo("已锁定屏幕");
                }

            } else if (i == R.id.iv_center_play || i == R.id.iv_play) {
                if (isUserCharged()) {
                    changePlayStatus();
                } else {
                    toBuyLesson();
                }


            } else if (i == R.id.iv_download_play) {
                //下载视频
                downloadCurrentVideo();

            } else if (i == R.id.iv_top_menu) {
                setLayoutVisibility(View.GONE, false);
                showTopPopupWindow();

            } else if (i == R.id.tv_change_video) {
                setLayoutVisibility(View.GONE, false);
                showChangeVideoWindow();

            } else if (i == R.id.iv_video_back) {
                changeToBackVideo();

            } else if (i == R.id.iv_video_next) {
                changeToNextVideo(false);

            }
        }
    };

    protected abstract void toBuyLesson();

    protected abstract boolean isUserCharged();

    // 设置横屏的固定方向，禁用掉重力感应方向
    private void setLandScapeRequestOrientation() {
        int rotation = wm.getDefaultDisplay().getRotation();
        // 旋转90°为横屏正向，旋转270°为横屏逆向
        if (rotation == Surface.ROTATION_90) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else if (rotation == Surface.ROTATION_270) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
        }
    }


    // 点击》播放下一个视频
    private void changeToNextVideo(boolean isCompleted) {
        int currentIndex = getCurrentVideoIndex();
        int length = videoIds.size();
        int position = 0;
        if (currentIndex == length - 1) {
            position = 0;
        } else {
            position = ++currentIndex;
        }
        changeVideo(position, isCompleted);
    }

    //点击《 播放上一个视频
    private void changeToBackVideo() {
        int currentPosition = getCurrentVideoIndex();
        int length = videoIds.size();
        int position = 0;
        if (currentPosition == 0) {
            position = length - 1;
        } else {
            position = --currentPosition;
        }
        changeVideo(position, false);
    }

    PlayChangeVideoPopupWindow playChangeVideoPopupWindow;

    private void showChangeVideoWindow() {
        if (playChangeVideoPopupWindow == null) {
            initPlayChangeVideoPopupWindow();
        }
        playChangeVideoPopupWindow.setSelectedPosition(getCurrentVideoIndex()).showAsDropDown(rlPlay);
    }


    //得到当前的课时 index
    private int getCurrentVideoIndex() {
        return videoIds.indexOf(videoId);
    }

    //我添加的 保存最后一个播放的视频id和index
    private  void saveLastIndex(){
       // insertVideosIndex(videoId,getCurrentVideoIndex());
        editor.putInt(Constants.VIDEOS_INDEX,getCurrentVideoIndex());
        editor.commit();

    }

    private void initPlayChangeVideoPopupWindow() {
        playChangeVideoPopupWindow = new PlayChangeVideoPopupWindow(this, surfaceView.getHeight(), videoInfos);

        playChangeVideoPopupWindow.setItem(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                changeVideo(position, false);
                playChangeVideoPopupWindow.setSelectedPosition(position);
                playChangeVideoPopupWindow.refreshView();
                playChangeVideoPopupWindow.dismiss();

                Arad.bus.post(new EventModel.OnlineLessonPeriodSelected(videoId));
            }
        });
    }

    //点击课时列表的item的时候切换视频
    //这个里面的参数position是课时列表的position
    public void changeVideo(int position) {
        isPlaying = true;
        changeVideo(position, false);
    }

    private void changeVideo(int position, boolean isCompleted) {
        if (isCompleted) {
            //播放完之后自动播放下一个
            updateCompleteDataPosition();
        } else {
            updateDataPosition();
        }

        isPrepared = false;

        setLayoutVisibility(View.GONE, false);
        bufferProgressBar.setVisibility(View.VISIBLE);
        ivCenterPlay.setVisibility(View.GONE);

        currentPosition = 0;
        currentPlayPosition = 0;

        timerTask.cancel();

        if (position < videoIds.size()) {
            videoId = videoIds.get(position);
            if (playChangeVideoPopupWindow != null) {
                playChangeVideoPopupWindow.setSelectedPosition(getCurrentVideoIndex()).refreshView();
            }

            player.pause();
            player.stop();
            player.reset();
            player.setDefaultDefinition(defaultDefinition);
            player.setVideoPlayInfo(videoId, ConfigUtil.USERID, ConfigUtil.API_KEY, MediaPlayActivity.this);
            player.setDisplay(surfaceHolder);

            demoApplication.getDRMServer().reset();
            player.prepareAsync();
        }

    }

    PlayTopPopupWindow playTopPopupWindow;

    private void showTopPopupWindow() {
        if (playTopPopupWindow == null) {
            initPlayTopPopupWindow();
        }
        playTopPopupWindow.showAsDropDown(rlPlay);
    }

    private void initPlayTopPopupWindow() {
        playTopPopupWindow = new PlayTopPopupWindow(this, surfaceView.getHeight());
        playTopPopupWindow.setSubtitleCheckLister(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_subtitle_open) {
                    currentScreenSizeFlag = 0;
                    subtitleText.setVisibility(View.VISIBLE);

                } else if (checkedId == R.id.rb_subtitle_close) {
                    currentScreenSizeFlag = 1;
                    subtitleText.setVisibility(View.GONE);

                }
            }
        });

        playTopPopupWindow.setScreenSizeCheckLister(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                int position = 0;
                if (checkedId == R.id.rb_screensize_full) {
                    position = 0;

                } else if (checkedId == R.id.rb_screensize_100) {
                    position = 1;

                } else if (checkedId == R.id.rb_screensize_75) {
                    position = 2;

                } else if (checkedId == R.id.rb_screensize_50) {
                    position = 3;

                }

                Toast.makeText(getApplicationContext(), screenSizeArray[position], Toast.LENGTH_SHORT).show();
                LayoutParams params = getScreenSizeParams(position);
                params.addRule(RelativeLayout.CENTER_IN_PARENT);
                surfaceView.setLayoutParams(params);
            }
        });

    }

    private DownloadService.DownloadBinder binder;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i("service disconnected", name + "");
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (DownloadBinder) service;
        }
    };


    //下载当前视频
    private void downloadCurrentVideo() {
        if (DataSet.hasDownloadInfo(videoId)) {
            Toast.makeText(this, "文件已存在", Toast.LENGTH_SHORT).show();
            return;
        }

        File file = MediaUtil.createFile(videoId, MediaUtil.PCM_FILE_SUFFIX);
        if (file == null) {
            Toast.makeText(this, "创建文件失败", Toast.LENGTH_SHORT).show();
            return;
        }

        DataSet.addDownloadInfo(new DownloadInfo(videoId, videoId, 0, null, Downloader.WAIT, new Date()));

        Intent service = new Intent(this, DownloadService.class);
        service.putExtra("title", videoId);
        startService(service);

        Toast.makeText(this, "文件已加入下载队列", Toast.LENGTH_SHORT).show();
    }

    private void toastInfo(String info) {
        Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
    }

    OnSeekBarChangeListener onSeekBarChangeListener = new OnSeekBarChangeListener() {
        int progress = 0;

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            if (networkConnected || isLocalPlay) {
                player.seekTo(progress);
                playerHandler.postDelayed(hidePlayRunnable, 5000);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            playerHandler.removeCallbacks(hidePlayRunnable);
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (networkConnected || isLocalPlay) {
                this.progress = progress * (int) player.getDuration() / seekBar.getMax();
            }
        }
    };

    VerticalSeekBar.OnSeekBarChangeListener seekBarChangeListener = new VerticalSeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            currentVolume = progress;
            volumeSeekBar.setProgress(progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            playerHandler.removeCallbacks(hidePlayRunnable);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            playerHandler.postDelayed(hidePlayRunnable, 5000);
        }

    };

    // 控制播放器面板显示
    private boolean isDisplay = false;

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // 监测音量变化
        if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN
                || event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {

            int volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (currentVolume != volume) {
                currentVolume = volume;
                volumeSeekBar.setProgress(currentVolume);
            }

            if (isPrepared) {
                setLayoutVisibility(View.VISIBLE, true);
            }
        }
        return super.dispatchKeyEvent(event);
    }

    /**
     * @param visibility 显示状态
     * @param isDisplay  是否延迟消失
     */
    private void setLayoutVisibility(int visibility, boolean isDisplay) {
        if (player == null || player.getDuration() <= 0) {
            return;
        }

        playerHandler.removeCallbacks(hidePlayRunnable);

        this.isDisplay = isDisplay;

        if (definitionMenu != null && visibility == View.GONE) {
            definitionMenu.dismiss();
        }

        if (isDisplay) {
            playerHandler.postDelayed(hidePlayRunnable, 5000);
        }

        if (isPortrait()) {
            ivFullscreen.setVisibility(visibility);

            lockView.setVisibility(View.GONE);

            volumeLayout.setVisibility(View.GONE);
            tvDefinition.setVisibility(View.GONE);
            tvChangeVideo.setVisibility(View.GONE);
            tvSpeedPlay.setVisibility(View.GONE);
            ivTopMenu.setVisibility(View.GONE);
            ivBackVideo.setVisibility(View.GONE);
            ivNextVideo.setVisibility(View.GONE);
        } else {
            ivFullscreen.setVisibility(View.GONE);

            lockView.setVisibility(visibility);
            if (lockView.isSelected()) {
                visibility = View.GONE;
            }

            volumeLayout.setVisibility(visibility);
            tvDefinition.setVisibility(visibility);
            tvChangeVideo.setVisibility(visibility);
            tvSpeedPlay.setVisibility(visibility);
            ivTopMenu.setVisibility(visibility);
            ivBackVideo.setVisibility(visibility);
            ivNextVideo.setVisibility(visibility);
        }

        if (isLocalPlay) {
            ivDownload.setVisibility(View.GONE);
            ivTopMenu.setVisibility(View.GONE);

            ivBackVideo.setVisibility(View.GONE);
            ivNextVideo.setVisibility(View.GONE);
            tvChangeVideo.setVisibility(View.GONE);
            tvSpeedPlay.setVisibility(View.GONE);
            tvDefinition.setVisibility(View.GONE);
            ivFullscreen.setVisibility(View.INVISIBLE);
        }

        playerTopLayout.setVisibility(visibility);
        playerBottomLayout.setVisibility(visibility);
    }

    private Handler alertHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
//            toastInfo("视频异常，无法播放。");
            super.handleMessage(msg);
        }

    };

    @Override
    public boolean onError(IMediaPlayer mp, final int what, int extra) {

        if (DWMediaPlayer.MEDIA_ERROR_DRM_LOCAL_SERVER == what) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    toastInfo("server_error");
                }
            });
        }

        Message msg = new Message();
        msg.what = what;
        if (alertHandler != null) {
            alertHandler.sendMessage(msg);
        }
        return false;
    }

    @Override
    public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i, int i1, int i2, int i3) {
        setSurfaceViewLayout();
    }

    // 重置隐藏界面组件的延迟时间
    private void resetHideDelayed() {
        playerHandler.removeCallbacks(hidePlayRunnable);
        playerHandler.postDelayed(hidePlayRunnable, 5000);
    }

    // 手势监听器类
    private class MyGesture extends SimpleOnGestureListener {

        private Boolean isVideo;
        private float scrollCurrentPosition;
        private float scrollCurrentVolume;

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return super.onSingleTapUp(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (lockView.isSelected()) {
                return true;
            }
            if (isVideo == null) {
                if (Math.abs(distanceX) > Math.abs(distanceY)) {
                    isVideo = true;
                } else {
                    isVideo = false;
                }
            }

            if (isVideo.booleanValue()) {
                parseVideoScroll(distanceX);
            } else {
                parseAudioScroll(distanceY);
            }

            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        private void parseVideoScroll(float distanceX) {
            if (!isDisplay) {
                setLayoutVisibility(View.VISIBLE, true);
            }

            scrollTotalDistance += distanceX;

            float duration = (float) player.getDuration();

            float width = wm.getDefaultDisplay().getWidth() * 0.75f; // 设定总长度是多少，此处根据实际调整
            //右滑distanceX为负
            float currentPosition = scrollCurrentPosition - (float) duration * scrollTotalDistance / width;

            if (currentPosition < 0) {
                currentPosition = 0;
            } else if (currentPosition > duration) {
                currentPosition = duration;
            }

            player.seekTo((int) currentPosition);

            playDuration.setText(ParamsUtil.millsecondsToStr((int) currentPosition));
            int pos = (int) (skbProgress.getMax() * currentPosition / duration);
            skbProgress.setProgress(pos);
        }

        private void parseAudioScroll(float distanceY) {
            if (!isDisplay) {
                setLayoutVisibility(View.VISIBLE, true);
            }
            scrollTotalDistance += distanceY;

            float height = wm.getDefaultDisplay().getHeight() * 0.75f;
            // 上滑distanceY为正
            currentVolume = (int) (scrollCurrentVolume + maxVolume * scrollTotalDistance / height);

            if (currentVolume < 0) {
                currentVolume = 0;
            } else if (currentVolume > maxVolume) {
                currentVolume = maxVolume;
            }

            volumeSeekBar.setProgress(currentVolume);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public void onShowPress(MotionEvent e) {
            super.onShowPress(e);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            scrollTotalDistance = 0f;
            isVideo = null;

            scrollCurrentPosition = (float) player.getCurrentPosition();
            scrollCurrentVolume = currentVolume;

            return super.onDown(e);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (lockView.isSelected()) {
                return true;
            }
            if (!isDisplay) {
                setLayoutVisibility(View.VISIBLE, true);
            }
            changePlayStatus();
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return super.onDoubleTapEvent(e);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (isDisplay) {
                setLayoutVisibility(View.GONE, false);
            } else {
                setLayoutVisibility(View.VISIBLE, true);
            }
            return super.onSingleTapConfirmed(e);
        }
    }

    public void changePlayStatus() {
        if (player.isPlaying()) {
            player.pause();
            ivCenterPlay.setVisibility(View.VISIBLE);
            ivPlay.setImageResource(R.drawable.smallbegin_ic);

        } else {
            if (isPrepared) {
                player.start();
                ivCenterPlay.setVisibility(View.GONE);
                ivPlay.setImageResource(R.drawable.smallstop_ic);
            } else {
                ToastUtils.showShort("请稍等...初始化中");
            }
        }
    }

    @Override
    public boolean onInfo(IMediaPlayer mp, int what, int extra) {
        switch (what) {
            case DWMediaPlayer.MEDIA_INFO_BUFFERING_START:
                if (player.isPlaying()) {
                    bufferProgressBar.setVisibility(View.VISIBLE);
                }
                break;
            case DWMediaPlayer.MEDIA_INFO_BUFFERING_END:
                bufferProgressBar.setVisibility(View.GONE);
                break;
        }
        return false;
    }

    // 获得当前屏幕的方向
    private boolean isPortrait() {
        int mOrientation = getApplicationContext().getResources().getConfiguration().orientation;
        if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            return false;
        } else {
            return true;
        }
    }

    private int mX, mY, mZ;
    private long lastTimeStamp = 0;
    private Calendar mCalendar;
    private SensorManager sensorManager;

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == null) {
            return;
        }

        if (!lockView.isSelected() && (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)) {
            int x = (int) event.values[0];
            int y = (int) event.values[1];
            int z = (int) event.values[2];
            mCalendar = Calendar.getInstance();
            long stamp = mCalendar.getTimeInMillis() / 1000l;

            int second = mCalendar.get(Calendar.SECOND);// 53

            int px = Math.abs(mX - x);
            int py = Math.abs(mY - y);
            int pz = Math.abs(mZ - z);

            int maxvalue = getMaxValue(px, py, pz);
            if (maxvalue > 2 && (stamp - lastTimeStamp) > 1) {
                lastTimeStamp = stamp;
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            }
            mX = x;
            mY = y;
            mZ = z;
        }
    }

    /**
     * 获取一个最大值
     *
     * @param px
     * @param py
     * @param pz
     * @return
     */
    private int getMaxValue(int px, int py, int pz) {
        int max = 0;
        if (px > py && px > pz) {
            max = px;
        } else if (py > px && py > pz) {
            max = py;
        } else if (pz > px && pz > py) {
            max = pz;
        }
        return max;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onBackPressed() {

        if (isPortrait() || isLocalPlay) {
            super.onBackPressed();
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }



    private void updateDataPosition() {
        if (isLocalPlay) {
            return;
        }

        if (currentPlayPosition > 0) {
            if (DataSet.getVideoPosition(videoId) > 0) {
                DataSet.updateVideoPosition(videoId, currentPlayPosition);
            } else {
                insertVideoPosition(videoId, currentPlayPosition);
            }
        }
    }

    private void updateCompleteDataPosition() {
        if (DataSet.getVideoPosition(videoId) > 0) {
            DataSet.updateVideoPosition(videoId, currentPlayPosition);
        } else {
            insertVideoPosition(videoId, currentPlayPosition);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);

        if (isPrepared) {
            // 刷新界面
            setLayoutVisibility(View.GONE, false);
            setLayoutVisibility(View.VISIBLE, true);
        }

        lockView.setSelected(false);

        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            showBottomLayout();
            ivFullscreen.setImageResource(R.drawable.fullscreen_close);

            if (playChangeVideoPopupWindow != null) {
                playChangeVideoPopupWindow.dismiss();
            }

            if (playTopPopupWindow != null) {
                playTopPopupWindow.dismiss();
            }

            orientationProtrait();

        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            hideBottomLayout();
            ivFullscreen.setImageResource(R.drawable.fullscreen_open);

            orientationHorizon();
        }

        setSurfaceViewLayout();
    }

    protected abstract void orientationHorizon();

    protected abstract void orientationProtrait();

    @Override
    public void onCompletion(IMediaPlayer mp) {

        if (isLocalPlay) {
            toastInfo("播放完成！");
            finish();
            return;
        }

        if (isPrepared) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
//                    toastInfo("切换中，请稍候……");
                    currentPlayPosition = 0;
                    currentPosition = 0;
                    changeToNextVideo(true);
                }
            });
        }
    }
}
