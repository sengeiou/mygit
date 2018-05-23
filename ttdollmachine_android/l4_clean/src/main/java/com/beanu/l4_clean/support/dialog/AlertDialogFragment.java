package com.beanu.l4_clean.support.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
 * 带图片文字的dialog
 * Created by Beanu on 2017/11/22.
 */

public class AlertDialogFragment extends DialogFragment {


    private static final String TAG = "alertDialog";

    public interface Listener {
        void onClickLeft(View view);

        void onClickRight(View view);
    }

    private Listener mListener;

    public static AlertDialogFragment newInstance(@DrawableRes int resId, String title, String left, String right) {
        AlertDialogFragment f = new AlertDialogFragment();

        Bundle args = new Bundle();
        args.putInt("resId", resId);
        args.putString("title", title);
        args.putString("left", left);
        args.putString("right", right);
        f.setArguments(args);

        return f;
    }


    public static void show(FragmentManager manager, String title, int resId, String leftTitle, String rightTitle, Listener listener) {
        newInstance(resId, title, leftTitle, rightTitle)
                .setListener(listener)
                .show(manager, TAG);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.arad_AnimBottomDialog;

        View view = inflater.inflate(R.layout.dialog_alert_with_drawable, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView txt1 = view.findViewById(R.id.txt_1);
        TextView txt2 = view.findViewById(R.id.txt_2);
        ImageView img = view.findViewById(R.id.img_content);
        TextView title = view.findViewById(R.id.txt_title);

        if (getArguments() != null) {
            int resId = getArguments().getInt("resId");
            img.setImageResource(resId);

            String tt = getArguments().getString("title");
            title.setText(tt);

            String left = getArguments().getString("left");
            txt1.setText(left);

            String right = getArguments().getString("right");
            txt2.setText(right);
        }
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
        getDialog().getWindow().setLayout((int) (dm.widthPixels * 0.75), ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    public AlertDialogFragment setListener(Listener listener) {
        mListener = listener;
        return this;
    }
}
