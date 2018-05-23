package com.beanu.l4_clean.support.dialog;

import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
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
import android.widget.ImageView;

import com.beanu.l4_clean.R;

/**
 * 等待dialog
 * Created by Beanu on 2017/11/22.
 */

public class WaitDialogFragment extends DialogFragment {


    private static final String TAG = "waitDialog";

    public static WaitDialogFragment newInstance() {
        WaitDialogFragment f = new WaitDialogFragment();

        return f;
    }


    public static void show(FragmentManager manager) {
        newInstance()
                .show(manager, TAG);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCanceledOnTouchOutside(false);
//        getDialog().getWindow().getAttributes().windowAnimations = R.style.arad_AnimBottomDialog;

        View view = inflater.inflate(R.layout.dialog_wait, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView img = view.findViewById(R.id.img);

        AnimationDrawable animationDrawable = (AnimationDrawable) img.getDrawable();
        animationDrawable.start();
    }

    @Override
    public void onStart() {
        super.onStart();

        //设置宽度
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout((int) (dm.widthPixels), ViewGroup.LayoutParams.WRAP_CONTENT);

    }

}
