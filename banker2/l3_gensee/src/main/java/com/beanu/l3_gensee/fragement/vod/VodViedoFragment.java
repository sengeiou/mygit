package com.beanu.l3_gensee.fragement.vod;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.beanu.l3_gensee.R;
import com.beanu.l3_gensee.voddemo.ChapterInfo;
import com.gensee.entity.ChatMsg;
import com.gensee.entity.DocInfo;
import com.gensee.entity.PageInfo;
import com.gensee.entity.PingEntity;
import com.gensee.media.PlaySpeed;
import com.gensee.media.VODPlayer;
import com.gensee.utils.GenseeLog;
import com.gensee.view.GSVideoView;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

@SuppressLint("ValidFragment")
public class VodViedoFragment extends Fragment implements OnClickListener, VODPlayer.OnVodPlayListener, SeekBar.OnSeekBarChangeListener {

    private VODPlayer mVodPlayer;
    private View mView;
    private GSVideoView mGSViedoView;
    private ProgressBar mLoadingProgress;
    private ImageView mImgOnlyAudio;
    private ImageButton txtVideo, txtHand, txtFullScreen;
    private TextView btnPlaySpeed;
    private TextView txtHandText;
    private TextView txtAudio, txtMic, txtIdc;
    private LinearLayout mRightPane;
    private RelativeLayout mToolbar;
    private LinearLayout mLayoutPlayStatus;
    private ImageView mToolbarLeft, mToolbarRight;
    private TextView mToolbarTitle;
    private Spinner spinnerRate;
    private Runnable handRun = null;
    private List<PingEntity> idcs;
    private boolean isShowingPane = true, isAnimation = false;
    private PopupWindow mPopupWindow;

    private SeekBar mSeekBarPlayViedo;
    private Button stopVeidoPlay;
    private Button replyVedioPlay;
    private TextView mNowTimeTextview;
    private TextView mAllTimeTextView;

    private ImageButton mPauseScreenplay;
    private ChapterListAdapter chapterListAdapter;
    private List<ChapterInfo> chapterList;


    private static final int DURITME = Toast.LENGTH_SHORT;
    private static final String DURATION = "DURATION";
    private int lastPostion = 0;
    private int VIEDOPAUSEPALY = 0;
    private boolean isTouch = false;
    private int speedItem = 0;

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        isTouch = true;

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (null != mVodPlayer) {
            int pos = seekBar.getProgress();
            GenseeLog.d(TAG, "onStopTrackingTouch pos = " + pos);
            mVodPlayer.seekTo(pos);

        }
    }


    interface MSG {
        int MSG_ON_INIT = 1;
        int MSG_ON_STOP = 2;
        int MSG_ON_POSITION = 3;
        int MSG_ON_VIDEOSIZE = 4;
        int MSG_ON_PAGE = 5;
        int MSG_ON_SEEK = 6;
        int MSG_ON_AUDIOLEVEL = 7;
        int MSG_ON_ERROR = 8;
        int MSG_ON_PAUSE = 9;
        int MSG_ON_RESUME = 10;
    }

    protected Handler myHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG.MSG_ON_INIT:

                    mLoadingProgress.setVisibility(View.GONE);

                    int max = msg.getData().getInt(DURATION);
                    mSeekBarPlayViedo.setMax(max);
                    max = max / 1000;
                    GenseeLog.i(TAG, "MSG_ON_INIT duration = " + max);
                    mAllTimeTextView.setText(getTime(max));
                    mVodPlayer.seekTo(lastPostion);
                    mPauseScreenplay.setImageResource(R.drawable.icon_pause);

                    if (null != chapterListAdapter) {
                        chapterList.clear();
                        if (null != msg.obj) {
                            List<DocInfo> docInfoList = (List<DocInfo>) msg.obj;
                            for (DocInfo docInfo : docInfoList) {
                                List<PageInfo> pageInfoList = docInfo.getPages();
                                if (null != pageInfoList && pageInfoList.size() > 0) {
                                    for (PageInfo pageInfo : pageInfoList) {

                                        ChapterInfo chapterInfo = new ChapterInfo();
                                        chapterInfo.setDocId(docInfo.getDocId());
                                        chapterInfo
                                                .setDocName(docInfo.getDocName());
                                        chapterInfo.setDocPageNum(docInfo
                                                .getPageNum());
                                        chapterInfo.setDocType(docInfo.getType());

                                        chapterInfo.setPageTimeStamp(pageInfo
                                                .getTimeStamp());
                                        chapterInfo.setPageTitle(pageInfo
                                                .getTitle());
                                        chapterList.add(chapterInfo);
                                    }
                                }
                            }

                        }
                        chapterListAdapter.notifyData(chapterList);
                    }
                    break;
                case MSG.MSG_ON_STOP:

                    break;
                case MSG.MSG_ON_VIDEOSIZE:

                    break;
                case MSG.MSG_ON_PAGE:
                    int position = (Integer) msg.obj;
                    int nSize = chapterList.size();
                    for (int i = 0; i < nSize; i++) {
                        ChapterInfo chapterInfo = chapterList.get(i);
                        if (chapterInfo.getPageTimeStamp() == position) {
                            if (null != chapterListAdapter) {
                                chapterListAdapter.setSelectedPosition(i);
                            }
                            break;
                        }
                    }
                    break;
                case MSG.MSG_ON_PAUSE:
                    VIEDOPAUSEPALY = 1;
                    mPauseScreenplay.setImageResource(R.drawable.icon_play);
                    break;
                case MSG.MSG_ON_RESUME:
                    VIEDOPAUSEPALY = 0;
                    mPauseScreenplay.setImageResource(R.drawable.icon_pause);
                    break;
                case MSG.MSG_ON_POSITION:
                    if (isTouch) {
                        return;
                    }
                case MSG.MSG_ON_SEEK:
                    isTouch = false;
                    int anyPosition = (Integer) msg.obj;
                    mSeekBarPlayViedo.setProgress(anyPosition);
                    anyPosition = anyPosition / 1000;
                    mNowTimeTextview.setText(getTime(anyPosition));
                    break;
                case MSG.MSG_ON_AUDIOLEVEL:

                    break;
                case MSG.MSG_ON_ERROR:
                    int errorCode = (Integer) msg.obj;
                    switch (errorCode) {
                        case ERR_PAUSE:
                            Toast.makeText(getActivity().getApplicationContext(), "暂停失败", DURITME)
                                    .show();
                            break;
                        case ERR_PLAY:
                            Toast.makeText(getActivity().getApplicationContext(), "播放失败", DURITME)
                                    .show();
                            break;
                        case ERR_RESUME:
                            Toast.makeText(getActivity().getApplicationContext(), "恢复失败", DURITME)
                                    .show();
                            break;
                        case ERR_SEEK:
                            Toast.makeText(getActivity().getApplicationContext(), "进度变化失败", DURITME)
                                    .show();
                            break;
                        case ERR_STOP:
                            Toast.makeText(getActivity().getApplicationContext(), "停止失败", DURITME)
                                    .show();
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }

    };

    public VodViedoFragment() {
    }

    public VodViedoFragment(VODPlayer player) {
        this.mVodPlayer = player;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chapterList = new ArrayList<ChapterInfo>();
        chapterListAdapter = new ChapterListAdapter();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.vod_imviedo, null);
        txtVideo = (ImageButton) mView.findViewById(R.id.txtVideo);
        txtAudio = (TextView) mView.findViewById(R.id.txtAudio);
        txtMic = (TextView) mView.findViewById(R.id.txtMic);
        btnPlaySpeed = (TextView) mView.findViewById(R.id.play_speed);
        txtHand = (ImageButton) mView.findViewById(R.id.txtHand);
        txtHandText = (TextView) mView.findViewById(R.id.txtHand_text);
        txtFullScreen = (ImageButton) mView.findViewById(R.id.txtFullscreen);
        mLoadingProgress = (ProgressBar) mView.findViewById(R.id.progress_video_loading);
        mImgOnlyAudio = (ImageView) mView.findViewById(R.id.img_only_audio);
        mToolbar = (RelativeLayout) mView.findViewById(R.id.rl_toolbar);
        mLayoutPlayStatus = (LinearLayout) mView.findViewById(R.id.ll_play_status);
        mRightPane = (LinearLayout) mView.findViewById(R.id.ll_right_pane);
        mToolbarLeft = (ImageView) mView.findViewById(R.id.toolbar_left);
        mToolbarRight = (ImageView) mView.findViewById(R.id.toolbar_right);
        mToolbarTitle = (TextView) mView.findViewById(R.id.toolbar_title);
        spinnerRate = (Spinner) mView.findViewById(R.id.spinnerRate);
        stopVeidoPlay = (Button) mView.findViewById(R.id.stopveidoplay);
        mPauseScreenplay = (ImageButton) mView.findViewById(R.id.pauseresumeplay);
        replyVedioPlay = (Button) mView.findViewById(R.id.replayvedioplay);
        mNowTimeTextview = (TextView) mView.findViewById(R.id.palynowtime);
        mAllTimeTextView = (TextView) mView.findViewById(R.id.palyalltime);
        mSeekBarPlayViedo = (SeekBar) mView.findViewById(R.id.seekbarpalyviedo);

        initRateSelectView();

        txtIdc = (TextView) mView.findViewById(R.id.txtIdc);

        mGSViedoView = (GSVideoView) mView.findViewById(R.id.imvideoview);
        mGSViedoView.setDefColor(getResources().getColor(R.color.video_view_bg));
        mGSViedoView.setOnClickListener(this);
        txtVideo.setOnClickListener(this);
        txtAudio.setOnClickListener(this);
        txtMic.setOnClickListener(this);
        txtHand.setOnClickListener(this);
        txtIdc.setOnClickListener(this);
        txtFullScreen.setOnClickListener(this);
        mToolbarLeft.setOnClickListener(this);
        mToolbarRight.setOnClickListener(this);
        stopVeidoPlay.setOnClickListener(this);
        replyVedioPlay.setOnClickListener(this);
        mPauseScreenplay.setOnClickListener(this);
        btnPlaySpeed.setOnClickListener(this);
        mView.findViewById(R.id.speed).setOnClickListener(this);

        mSeekBarPlayViedo.setOnSeekBarChangeListener(this);

        mVodPlayer.setGSVideoView(mGSViedoView);

        return mView;
    }

    public void play(String vodId) {
        mVodPlayer.play(vodId, this, "", false);
    }

    private void initPopupWindow() {
        if (mPopupWindow != null) {
            return;
        }
        mPopupWindow = new PopupWindow();
        View view = View.inflate(getActivity(), R.layout.pop_more, null);
        mPopupWindow.setContentView(view);
        mPopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setOutsideTouchable(true);

        view.findViewById(R.id.pop_more_change).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                txtIdc.performClick();
            }
        });
    }

    public void setVideoViewVisible(boolean bVisible) {
        if (isAdded()) {
            if (bVisible) {
                mGSViedoView.setVisibility(View.VISIBLE);
            } else {
                mGSViedoView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        int i1 = v.getId();
        if (i1 == R.id.imvideoview) {
            if (isShowingPane) {
                hideTools(0);
            } else {
                showTools();
            }

        } else if (i1 == R.id.txtFullscreen) {
            int orientation = getActivity().getRequestedOrientation();
            if (orientation == ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
                    || orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                orientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;
            } else {
                orientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT;
            }
            getActivity().setRequestedOrientation(orientation);
        } else if (i1 == R.id.txtAudio) {
            if (mVodPlayer != null) {
                // isSelect 代表关闭状态，默认非关闭状态 即false
                if (v.isSelected()) {
//                    mVodPlayer.audioSet(false);
                    v.setSelected(false);
                    txtAudio.setText(R.string.audio_close);

                } else {
//                    mVodPlayer.audioSet(true);
                    v.setSelected(true);
                    txtAudio.setText(R.string.audio_open);
                }
            }

        } else if (i1 == R.id.txtVideo) {
            if (mVodPlayer != null) {
                // isSelect 代表关闭状态，默认非关闭状态 即false
                if (!v.isSelected()) {
                    showOnlyAudio(true);
                    txtVideo.setImageResource(R.drawable.ic_video_video_no);

//                    mVodPlayer.videoSet(true);
                    v.setSelected(true);
                } else {
                    showOnlyAudio(false);
                    txtVideo.setImageResource(R.drawable.ic_video_video);

//                    mVodPlayer.videoSet(false);
                    v.setSelected(false);
                }
            }

        } else if (i1 == R.id.txtMic) {
            if (mVodPlayer != null) {
//                mVodPlayer.openMic(getActivity(), false, null);
//                mVodPlayer.inviteAck((Integer) v.getTag(), false, null);
            }

        } else if (i1 == R.id.txtHand) {
            if (handRun != null) {
                txtHand.removeCallbacks(handRun);
            }
            if (!v.isSelected()) {
//                mVodPlayer.handUp(true, null);
                txtHand.setSelected(true);
                handRun = new Runnable() {
                    private int time = 60;

                    @Override
                    public void run() {
                        txtHandText.setText(time + "s");
                        time--;
                        if (time < 0) {
                            handDown();
                        } else {
                            txtHand.postDelayed(this, 1000);
                        }
                    }
                };
                txtHand.post(handRun);
            } else {
                handDown();
            }

        } else if (i1 == R.id.txtIdc) {
//            int size = idcs == null ? 0 : idcs.size();
//            if (size > 0) {
//                CharSequence[] sequences = new CharSequence[size];
//                String curIdc = mVodPlayer.getCurIdc();
//                int itemindex = -1;
//                for (int i = 0; i < size; i++) {
//                    sequences[i] = idcs.get(i).getName() + "(" + idcs.get(i).getDescription() + ")";
//                    if (idcs.get(i).getIdcId().equals(curIdc)) {
//                        itemindex = i;
//                    }
//                }
//
//                alert(sequences, itemindex, new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
////                        mVodPlayer.setIdcId(idcs.get(which).getCode(), null);
//                        dialog.dismiss();
//                    }
//                });
//            }

        } else if (i1 == R.id.toolbar_left) {
            getActivity().onBackPressed();
        } else if (i1 == R.id.toolbar_right) {
            initPopupWindow();
            mPopupWindow.showAsDropDown(mToolbarRight);
        } else if (i1 == R.id.stopveidoplay) {
            boolean ret = mVodPlayer.stop();
            mSeekBarPlayViedo.setMax(0);
            Toast.makeText(getActivity(), ret ? "操作成功" : "操作失败", DURITME).show();
        } else if (i1 == R.id.replayvedioplay) {
//            isTouch = false;
//            String vodIdOrLocalPath = getVodIdOrLocalPath();
//            if (vodIdOrLocalPath == null) {
//                Toast.makeText(this, "路径不对", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            //重新播放时  将速度恢复为正常速度，如果要保持上一次速度播放，设置为上一次速度
//            speedItem = 0;
//            mVodPlayer.setSpeed(PlaySpeed.SPEED_NORMAL, null);
//            mVodPlayer.setGSVideoView(mGSVideoView);
//            mVodPlayer.setGSDocViewGx(mGlDocView);
//            mVodPlayer.play(vodIdOrLocalPath, this, "",false);
        } else if (i1 == R.id.pauseresumeplay) {
            if (VIEDOPAUSEPALY == 0) {
                mVodPlayer.pause();
            } else if (VIEDOPAUSEPALY == 1) {
                mVodPlayer.resume();
            }
        } else if (i1 == R.id.doc_list_btn) {
//            if (lvChapterList.getVisibility() == View.VISIBLE) {
//                lvChapterList.setVisibility(View.GONE);
//            } else {
//                lvChapterList.setVisibility(View.VISIBLE);
//            }
        } else if (i1 == R.id.play_speed) {
            switchSpeed();
        }
    }

    private void handDown() {
//        txtHand.setText("举手");
        txtHandText.setText("");
//        mVodPlayer.handUp(false, null);
        txtHand.setSelected(false);
    }
//
//    public void onMicColesed() {
//        txtMic.setVisibility(View.GONE);
//    }
//
//    public void onMicOpened(int inviteMediaType) {
//        txtMic.setTag(inviteMediaType);
//        txtMic.setVisibility(View.VISIBLE);
//    }

    private void initRateSelectView() {
        List<String> list = new ArrayList<String>();
        list.add(getString(R.string.video_rate_nor));
        list.add(getString(R.string.video_rate_low));

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_layout, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRate.setAdapter(adapter);
        spinnerRate.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
//                if (mVodPlayer != null) {
//                    switch (arg2) {
//                        case 0:
//                            mVodPlayer.switchRate(VideoRate.RATE_NORMAL, null);
//                            break;
//                        case 1:
//                            mVodPlayer.switchRate(VideoRate.RATE_LOW, null);
//                            break;
//                    }
//                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }
//
//    private void alert(CharSequence[] items, int checkedItem, DialogInterface.OnClickListener listener) {
//        new AlertDialog.Builder(getActivity()).setSingleChoiceItems(items, checkedItem, listener).create().show();
//    }
//
//    public void onIdcList(List<PingEntity> arg0) {
//        this.idcs = arg0;
//    }

    private void showTools() {
        Log.d("tag", "onclick show");
        if (!isShowingPane && !isAnimation) {
            Log.d("tag", "on show");

            int height = mToolbar.getHeight();
            int width = mRightPane.getWidth() + 40;
            isAnimation = true;
            mToolbar.animate().translationYBy(height).setDuration(400).setStartDelay(0).start();
            mLayoutPlayStatus.animate().alpha(1).setStartDelay(400).setStartDelay(0).start();
            mRightPane.animate().translationXBy(-width).setDuration(400).setStartDelay(0).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    isShowingPane = true;
                    isAnimation = false;
                    hideTools(2000);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            }).start();
        }
    }

    private void hideTools(long delay) {

        Log.d("tag", "onclick hide");
        if (isShowingPane && !isAnimation) {


            Log.d("tag", "on hide");
            int height = mToolbar.getHeight();
            int width = mRightPane.getWidth() + 40;
            isAnimation = true;
            mToolbar.animate().translationYBy(-height).setDuration(400).setStartDelay(delay).start();
            mLayoutPlayStatus.animate().alpha(0).setStartDelay(400).setStartDelay(delay).start();
            mRightPane.animate().translationXBy(width).setDuration(400).setStartDelay(delay).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    isShowingPane = false;
                    isAnimation = false;
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            }).start();
        }
    }


    public void showOnlyAudio(boolean show) {
        if (show) {
            mImgOnlyAudio.setVisibility(View.VISIBLE);
        } else {
            mImgOnlyAudio.setVisibility(View.GONE);
        }
    }

    public void setToolbarTitle(final String title) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mToolbarTitle != null) {
                        mToolbarTitle.setText(Html.fromHtml(title));
                    }
                }
            });
        }
    }

    private String getTime(int time) {
        return String.format("%02d", time / 3600) + ":"
                + String.format("%02d", time % 3600 / 60) + ":"
                + String.format("%02d", time % 3600 % 60);
    }

    /**
     * 变速播放
     */
    private void switchSpeed() {
        new AlertDialog.Builder(getActivity())
                .setSingleChoiceItems(R.array.speeds, speedItem,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                speedItem = which;
                                PlaySpeed ps = PlaySpeed.SPEED_NORMAL;
                                switch (which) {
                                    case 0:
                                        ps = PlaySpeed.SPEED_NORMAL;
                                        break;
                                    case 1:
                                        ps = PlaySpeed.SPEED_125;
                                        break;
                                    case 2:
                                        ps = PlaySpeed.SPEED_150;
                                        break;
                                    case 3:
                                        ps = PlaySpeed.SPEED_175;
                                        break;
                                    case 4:
                                        ps = PlaySpeed.SPEED_200;
                                        break;
                                    case 6:
                                        ps = PlaySpeed.SPEED_250;
                                        break;
                                    case 7:
                                        ps = PlaySpeed.SPEED_300;
                                        break;
                                    case 8:
                                        ps = PlaySpeed.SPEED_350;
                                        break;
                                    case 9:
                                        ps = PlaySpeed.SPEED_400;
                                        break;

                                    default:
                                        break;
                                }
                                mVodPlayer.setSpeed(ps, null);
                                dialog.dismiss();
                            }
                        }).create().show();
    }

    @Override
    public void onInit(int result, boolean haveVideo, int duration, List<DocInfo> docInfos) {
        if (lastPostion >= duration - 1000) {
            lastPostion = 0;
        }
        Message message = new Message();
        message.what = MSG.MSG_ON_INIT;
        message.obj = docInfos;
        Bundle bundle = new Bundle();
        bundle.putInt(DURATION, duration);
        message.setData(bundle);
        myHandler.sendMessage(message);
    }

    @Override
    public void onPlayStop() {
        myHandler.sendMessage(myHandler.obtainMessage(MSG.MSG_ON_STOP, 0));
    }

    @Override
    public void onPlayPause() {
        myHandler.sendMessage(myHandler.obtainMessage(MSG.MSG_ON_PAUSE, 0));

    }

    @Override
    public void onPlayResume() {
        myHandler.sendMessage(myHandler.obtainMessage(MSG.MSG_ON_RESUME, 0));

    }

    @Override
    public void onPosition(int position) {
        GenseeLog.d(TAG, "onPosition pos = " + position);
        lastPostion = position;
        myHandler.sendMessage(myHandler.obtainMessage(MSG.MSG_ON_POSITION,
                position));
    }

    @Override
    public void onVideoSize(int i, int i1, int i2) {
        myHandler.sendMessage(myHandler.obtainMessage(MSG.MSG_ON_VIDEOSIZE, 0));
    }

    @Override
    public void onPageSize(int position, int i1, int i2) {
        //文档翻页切换，开始显示
        myHandler.sendMessage(myHandler
                .obtainMessage(MSG.MSG_ON_PAGE, position));
    }

    @Override
    public void onSeek(int position) {
        myHandler.sendMessage(myHandler
                .obtainMessage(MSG.MSG_ON_SEEK, position));
    }

    @Override
    public void onAudioLevel(int level) {
        myHandler.sendMessage(myHandler.obtainMessage(MSG.MSG_ON_AUDIOLEVEL,
                level));
    }

    @Override
    public void onCaching(boolean b) {

    }

    @Override
    public void onVideoStart() {

    }

    @Override
    public void onChat(List<ChatMsg> list) {

    }

    @Override
    public void onDocInfo(List<DocInfo> list) {

    }

    @Override
    public void onError(int errCode) {

        myHandler.sendMessage(myHandler
                .obtainMessage(MSG.MSG_ON_ERROR, errCode));
    }


    private class ChapterListAdapter extends BaseAdapter {
        private List<ChapterInfo> pageList;
        private int selectedPosition = 0;

        public void setSelectedPosition(int position) {
            selectedPosition = position;
            notifyDataSetChanged();
//            lvChapterList.setSelection(position);
        }

        public ChapterListAdapter() {
            pageList = new ArrayList<ChapterInfo>();
        }

        public void notifyData(List<ChapterInfo> pageList) {
            this.pageList.clear();
            this.pageList.addAll(pageList);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return pageList.size();
        }

        @Override
        public Object getItem(int position) {
            return pageList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (null == convertView) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.doc_list_item_ly, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.init((ChapterInfo) getItem(position), position);
            return convertView;
        }

        private class ViewHolder {
            private TextView tvChapter;
            private TextView tvTitle;
            private TextView tvTime;
            private LinearLayout lyChapter;

            private String getChapterTime(long time) {
                return String.format("%02d", time / (3600 * 1000))
                        + ":"
                        + String.format("%02d", time % (3600 * 1000)
                        / (60 * 1000))
                        + ":"
                        + String.format("%02d", time % (3600 * 1000)
                        % (60 * 1000) / 1000);
            }

            public ViewHolder(View view) {
                tvChapter = (TextView) view.findViewById(R.id.chapter_title);
                tvTitle = (TextView) view.findViewById(R.id.doc_title);
                tvTime = (TextView) view.findViewById(R.id.chapter_time);
                lyChapter = (LinearLayout) view.findViewById(R.id.chapter_ly);
            }

            public void init(ChapterInfo chapterInfo, int position) {
                tvChapter.setText(chapterInfo.getPageTitle());
                tvTime.setText(getChapterTime(chapterInfo.getPageTimeStamp()));
                tvTitle.setText(chapterInfo.getDocName());

                if (selectedPosition == position) {
                    lyChapter.setBackgroundResource(R.color.red);
                } else {
                    lyChapter.setBackgroundResource(R.color.transparent);
                }
            }
        }

    }
}