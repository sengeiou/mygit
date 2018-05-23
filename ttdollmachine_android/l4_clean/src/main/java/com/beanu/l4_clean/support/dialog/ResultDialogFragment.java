package com.beanu.l4_clean.support.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.beanu.l4_clean.R;

/**
 * 抓娃娃 结果dialog
 * Created by Beanu on 2017/11/22.
 */

public class ResultDialogFragment extends DialogFragment {


    private static final String TAG = "alertDialog";

    public interface Listener {
        void onClickLeft(View view);

        void onClickRight(View view);

        void onFinish();
    }

    private Listener mListener;

    private ImageView txt1;
    private TextView txt2;
    private TextView txtInfo;


    private CountDownTimer mCountDownTimer = new CountDownTimer(5 * 1000, 1000) {
        @Override
        public void onTick(long l) {
            if (txt2 != null) {
                txt2.setText("再玩一局(" + (l / 1000) + ")");
            }
        }

        @Override
        public void onFinish() {
            if (mListener != null) {
                dismiss();
                mListener.onFinish();
            }
        }
    };

    public static ResultDialogFragment newInstance(@DrawableRes int resId) {
        ResultDialogFragment f = new ResultDialogFragment();

        Bundle args = new Bundle();
        args.putInt("resId", resId);
        f.setArguments(args);

        return f;
    }


    public static void show(FragmentManager manager, int resId, Listener listener) {
        newInstance(resId)
                .setListener(listener)
                .show(manager, TAG);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCanceledOnTouchOutside(false);

        View view = inflater.inflate(R.layout.dialog_result, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txt1 = view.findViewById(R.id.txt_1);
        txt2 = view.findViewById(R.id.txt_2);
        txtInfo = view.findViewById(R.id.txt_info);
        ImageView img = view.findViewById(R.id.img_content);

        mCountDownTimer.start();

        if (getArguments() != null) {
            int resId = getArguments().getInt("resId");
            img.setImageResource(resId);

            if (resId == R.drawable.zhua_success) {
                txtInfo.setText("可在我的>我的娃娃中查看");
            } else {
                txtInfo.setText("本局+10积分\n您还有49个T币，是否再来一局？");
            }
        }
        txt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                mCountDownTimer.cancel();
                if (mListener != null)
                    mListener.onClickLeft(view);
            }
        });
        txt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                mCountDownTimer.cancel();
                if (mListener != null)
                    mListener.onClickRight(view);
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();

        //设置宽度
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout(dm.widthPixels, ViewGroup.LayoutParams.MATCH_PARENT);

    }

    public ResultDialogFragment setListener(Listener listener) {
        mListener = listener;
        return this;
    }
}
