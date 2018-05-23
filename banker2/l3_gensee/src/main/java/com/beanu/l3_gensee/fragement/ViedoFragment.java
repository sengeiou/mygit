package com.beanu.l3_gensee.fragement;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.beanu.l3_gensee.R;
import com.gensee.entity.PingEntity;
import com.gensee.player.Player;
import com.gensee.player.VideoRate;
import com.gensee.view.GSVideoView;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class ViedoFragment extends Fragment implements OnClickListener {

    private Player mPlayer;
    private View mView;
    private GSVideoView mGSViedoView;
    private ProgressBar mLoadingProgress;
    private ImageView mImgOnlyAudio;
    private ImageButton txtVideo, txtHand, txtFullScreen;
    private TextView txtHandText;
    private TextView txtAudio, txtMic, txtIdc;
    private LinearLayout mRightPane;
    private RelativeLayout mToolbar;
    private ImageView mToolbarLeft, mToolbarRight;
    private TextView mToolbarTitle;
    private Spinner spinnerRate;
    private Runnable handRun = null;
    private List<PingEntity> idcs;
    private boolean isShowingPane = true, isAnimation = false;
    private PopupWindow mPopupWindow;

    public ViedoFragment(Player player) {
        this.mPlayer = player;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.imviedo, null);
        txtVideo = (ImageButton) mView.findViewById(R.id.txtVideo);
        txtAudio = (TextView) mView.findViewById(R.id.txtAudio);
        txtMic = (TextView) mView.findViewById(R.id.txtMic);
        txtHand = (ImageButton) mView.findViewById(R.id.txtHand);
        txtHandText = (TextView) mView.findViewById(R.id.txtHand_text);
        txtFullScreen = (ImageButton) mView.findViewById(R.id.txtFullscreen);
        mLoadingProgress = (ProgressBar) mView.findViewById(R.id.progress_video_loading);
        mImgOnlyAudio = (ImageView) mView.findViewById(R.id.img_only_audio);
        mToolbar = (RelativeLayout) mView.findViewById(R.id.rl_toolbar);
        mRightPane = (LinearLayout) mView.findViewById(R.id.ll_right_pane);
        mToolbarLeft = (ImageView) mView.findViewById(R.id.toolbar_left);
        mToolbarRight = (ImageView) mView.findViewById(R.id.toolbar_right);
        mToolbarTitle = (TextView) mView.findViewById(R.id.toolbar_title);
        spinnerRate = (Spinner) mView.findViewById(R.id.spinnerRate);
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

        mPlayer.setGSVideoView(mGSViedoView);

        return mView;
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

    public void onJoin(boolean isJoined) {
        if (txtAudio != null) {
            txtAudio.setEnabled(isJoined);
            txtVideo.setEnabled(isJoined);
            txtHand.setEnabled(isJoined);
            txtIdc.setEnabled(isJoined);
            mLoadingProgress.setVisibility(View.GONE);
        }
        hideTools(2000);
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
            if (mPlayer != null) {
                // isSelect 代表关闭状态，默认非关闭状态 即false
                if (v.isSelected()) {
                    mPlayer.audioSet(false);
                    v.setSelected(false);
                    txtAudio.setText(R.string.audio_close);

                } else {
                    mPlayer.audioSet(true);
                    v.setSelected(true);
                    txtAudio.setText(R.string.audio_open);
                }
            }

        } else if (i1 == R.id.txtVideo) {
            if (mPlayer != null) {
                // isSelect 代表关闭状态，默认非关闭状态 即false
                if (!v.isSelected()) {
                    showOnlyAudio(true);
                    txtVideo.setImageResource(R.drawable.ic_video_video_no);

                    mPlayer.videoSet(true);
                    v.setSelected(true);
                } else {
                    showOnlyAudio(false);
                    txtVideo.setImageResource(R.drawable.ic_video_video);

                    mPlayer.videoSet(false);
                    v.setSelected(false);
                }
            }

        } else if (i1 == R.id.txtMic) {
            if (mPlayer != null) {
                mPlayer.openMic(getActivity(), false, null);
                mPlayer.inviteAck((Integer) v.getTag(), false, null);
            }

        } else if (i1 == R.id.txtHand) {
            if (handRun != null) {
                txtHand.removeCallbacks(handRun);
            }
            if (!v.isSelected()) {
                mPlayer.handUp(true, null);
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
            int size = idcs == null ? 0 : idcs.size();
            if (size > 0) {
                CharSequence[] sequences = new CharSequence[size];
                String curIdc = mPlayer.getCurIdc();
                int itemindex = -1;
                for (int i = 0; i < size; i++) {
                    sequences[i] = idcs.get(i).getName() + "(" + idcs.get(i).getDescription() + ")";
                    if (idcs.get(i).getIdcId().equals(curIdc)) {
                        itemindex = i;
                    }
                }

                alert(sequences, itemindex, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPlayer.setIdcId(idcs.get(which).getCode(), null);
                        dialog.dismiss();
                    }
                });
            }

        } else if (i1 == R.id.toolbar_left) {
            getActivity().onBackPressed();
        } else if (i1 == R.id.toolbar_right) {
            initPopupWindow();
            mPopupWindow.showAsDropDown(mToolbarRight);
        }
    }

    private void handDown() {
//        txtHand.setText("举手");
        txtHandText.setText("");
        mPlayer.handUp(false, null);
        txtHand.setSelected(false);
    }

    public void onMicColesed() {
        txtMic.setVisibility(View.GONE);
    }

    public void onMicOpened(int inviteMediaType) {
        txtMic.setTag(inviteMediaType);
        txtMic.setVisibility(View.VISIBLE);
    }

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
                if (mPlayer != null) {
                    switch (arg2) {
                        case 0:
                            mPlayer.switchRate(VideoRate.RATE_NORMAL, null);
                            break;
                        case 1:
                            mPlayer.switchRate(VideoRate.RATE_LOW, null);
                            break;
                    }
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void alert(CharSequence[] items, int checkedItem, DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(getActivity()).setSingleChoiceItems(items, checkedItem, listener).create().show();
    }

    public void onIdcList(List<PingEntity> arg0) {
        this.idcs = arg0;
    }

    private void showTools() {
        Log.d("tag", "onclick show");
        if (!isShowingPane && !isAnimation) {
            Log.d("tag", "on show");

            int height = mToolbar.getHeight();
            int width = mRightPane.getWidth() + 40;
            isAnimation = true;
            mToolbar.animate().translationYBy(height).setDuration(400).setStartDelay(0).start();
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

    public void setToolbarTitle(String title) {
        mToolbarTitle.setText(Html.fromHtml(title));
    }


}