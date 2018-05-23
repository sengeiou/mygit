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
import android.widget.Button;
import android.widget.TextView;

import com.beanu.l3_common.util.AppHolder;
import com.beanu.l4_clean.R;

/**
 * 帮我抓
 */
public class HelpGrabFragment extends DialogFragment {

    private Listener mListener;

    private static HelpGrabFragment newInstance(String price) {
        HelpGrabFragment f = new HelpGrabFragment();

        Bundle args = new Bundle();
        args.putString("price", price);
        f.setArguments(args);
        return f;
    }

    public static void show(FragmentManager manager, String price, Listener listener) {
        newInstance(price).setListener(listener).show(manager, "HelpGrabDialog");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCanceledOnTouchOutside(true);

        View view = inflater.inflate(R.layout.fragment_help_grab, container, false);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView txtBalance = view.findViewById(R.id.txt_balance);
        TextView txtCharge = view.findViewById(R.id.txt_charge);
        TextView txtPrice = view.findViewById(R.id.txt_price);
        Button btn_ok = view.findViewById(R.id.btn_ok);

        txtBalance.setText("账户余额：" + AppHolder.getInstance().user.getCoins());
        txtPrice.setText("x" + getArguments().getString("price"));


        txtCharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (mListener != null) {
                    mListener.onClickCharge(view);
                }
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (mListener != null) {
                    mListener.onClickOK(view);
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        //设置宽度
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout((int) (dm.widthPixels * 0.75), ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    public interface Listener {
        void onClickOK(View view);

        void onClickCharge(View view);

    }


    public HelpGrabFragment setListener(HelpGrabFragment.Listener listener) {
        mListener = listener;
        return this;
    }

}