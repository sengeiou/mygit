package com.beanu.l4_clean.support.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
 * 免费卡  dialog
 * Created by Beanu on 2017/11/22.
 */

public class FreeCardDialogFragment extends DialogFragment {

    private static final String TAG = "PKRestDialog";

    public interface Listener {
        void onClickLeft(View view);

        void onClickRight(View view);

    }

    private Listener mListener;
    private int balance;

    private TextView txt1;
    private TextView txt2;

    private TextView mTxtTips;
    private TextView mTxtBalance;


    public static FreeCardDialogFragment newInstance() {
        FreeCardDialogFragment f = new FreeCardDialogFragment();
        return f;
    }


    public static void show(FragmentManager manager, int balance, Listener listener) {
        newInstance()
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

        View view = inflater.inflate(R.layout.dialog_free_card, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txt1 = view.findViewById(R.id.txt_1);
        txt2 = view.findViewById(R.id.txt_2);

        mTxtTips = view.findViewById(R.id.txt_tips);
        mTxtBalance = view.findViewById(R.id.textView18);

        mTxtBalance.setText(String.format("您还有%s张免费卡未使用，\\n每日登录赠送的10张免费卡\\n仅限当天使用", balance));

        txt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (mListener != null)
                    mListener.onClickLeft(view);
            }
        });
        txt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
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
        getDialog().getWindow().setLayout((int) (dm.widthPixels), ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    public FreeCardDialogFragment setListener(Listener listener) {
        mListener = listener;
        return this;
    }

    public FreeCardDialogFragment setBalance(int balance) {
        this.balance = balance;
        return this;
    }
}
