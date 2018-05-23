package com.beanu.l4_bottom_tab.support.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.beanu.l4_bottom_tab.R;


/**
 * 自定义dialog
 * Created by Beanu on 16/9/21.
 */
public class AlertDialogCustom extends DialogFragment {

    private View.OnClickListener mClickListener;
    private View.OnClickListener mCancelClickListener;


    public static AlertDialogCustom newInstance(String message) {
        AlertDialogCustom f = new AlertDialogCustom();

        Bundle args = new Bundle();
        args.putString("message", message);
        f.setArguments(args);

        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCanceledOnTouchOutside(true);

        View view = inflater.inflate(R.layout.dialog_alert_custom, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView txtCancel = (TextView) view.findViewById(R.id.txt_cancel);
        TextView txtSure = (TextView) view.findViewById(R.id.txt_sure);
        TextView txtTitle = (TextView) view.findViewById(R.id.txt_title);

        txtTitle.setText(getArguments().getString("message"));
        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (mCancelClickListener != null) {
                    mCancelClickListener.onClick(view);
                }
            }
        });
        txtSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (mClickListener != null) {
                    mClickListener.onClick(view);
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

    public void setClickListener(View.OnClickListener clickListener) {
        mClickListener = clickListener;
    }

    public void setCancelClickListener(View.OnClickListener cancelClickListener) {
        mCancelClickListener = cancelClickListener;
    }
}
