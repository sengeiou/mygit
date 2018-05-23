package com.beanu.l4_clean.support.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.beanu.l4_clean.R;

/**
 * 倒计时  结果dialog
 * Created by Beanu on 2017/11/22.
 */

public class CountDownDialogFragment extends DialogFragment {

    private static final String TAG = "alertDialog";
    private TextView txt1;
    private Listener mListener;


    private CountDownTimer mCountDownTimer = new CountDownTimer(3 * 1000, 1000) {
        @Override
        public void onTick(long l) {
            if (txt1 != null) {
                txt1.setText((l / 1000) + "s");
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


    public static CountDownDialogFragment newInstance() {
        CountDownDialogFragment f = new CountDownDialogFragment();
        return f;
    }


    public static void show(FragmentManager manager, Listener listener) {
        newInstance().setListener(listener)
                .show(manager, TAG);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCanceledOnTouchOutside(false);

        View view = inflater.inflate(R.layout.dialog_count_down, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txt1 = view.findViewById(R.id.txt_time);
        mCountDownTimer.start();
    }

    @Override
    public void onStart() {
        super.onStart();

        //设置宽度
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout((int) (dm.widthPixels * 0.75), ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public CountDownDialogFragment setListener(Listener listener) {
        mListener = listener;
        return this;
    }

    public interface Listener {
        void onFinish();
    }

}