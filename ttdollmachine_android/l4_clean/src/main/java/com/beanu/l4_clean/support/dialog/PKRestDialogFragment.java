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
import android.widget.ImageView;
import android.widget.TextView;

import com.beanu.l4_clean.R;

/**
 * PK 休息  dialog
 * Created by Beanu on 2017/11/22.
 */

public class PKRestDialogFragment extends DialogFragment {


    private static final String TAG = "PKRestDialog";

    public interface Listener {
        void onClickLeft(View view);

        void onClickRight(View view);

        void onFinish();
    }

    private Listener mListener;
    private int price, balance;

    private TextView txt1;
    private TextView txt2;
    private TextView txt3;

    private TextView mTxtPrice;
    private TextView mTxtBalance;


    private CountDownTimer mCountDownTimer = new CountDownTimer(5 * 1000, 1000) {
        @Override
        public void onTick(long l) {
            if (txt2 != null && txt3 != null) {
                txt2.setText("马上开始" + (l / 1000));
                txt3.setText((l / 1000) + "s");
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

    public static PKRestDialogFragment newInstance() {
        PKRestDialogFragment f = new PKRestDialogFragment();
        return f;
    }


    public static void show(FragmentManager manager, int price, int balance, Listener listener) {
        newInstance()
                .setPrice(price)
                .setBalance(balance)
                .setListener(listener)
                .show(manager, TAG);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCanceledOnTouchOutside(false);

        View view = inflater.inflate(R.layout.dialog_pk_rest, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txt1 = view.findViewById(R.id.txt_1);
        txt2 = view.findViewById(R.id.txt_2);
        txt2.setTextColor(getResources().getColor(R.color.red));
        txt3 = view.findViewById(R.id.txt_time);

        mTxtPrice = view.findViewById(R.id.txt_price);
        mTxtBalance = view.findViewById(R.id.textView20);

        mTxtPrice.setText(String.format("每轮消费：%s开心币", price));
        mTxtBalance.setText(String.format("账户余额：%s开心币", balance));

        ImageView img = view.findViewById(R.id.img_content);

        if (getArguments() != null) {
            int resId = getArguments().getInt("resId");
            img.setImageResource(resId);
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

    public PKRestDialogFragment setListener(Listener listener) {
        mListener = listener;
        return this;
    }

    public PKRestDialogFragment setPrice(int price) {
        this.price = price;

        return this;
    }

    public PKRestDialogFragment setBalance(int balance) {
        this.balance = balance;
        return this;
    }
}
