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
import android.widget.ImageView;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.l3_common.util.Constants;
import com.beanu.l4_clean.R;

/**
 * 免费卡 赠送 dialog
 * Created by Beanu on 2017/11/22.
 */

public class FreeCardTipsDialogFragment extends DialogFragment {


    private static final String TAG = "Free_Card_Dialog";


    public static FreeCardTipsDialogFragment newInstance() {
        FreeCardTipsDialogFragment f = new FreeCardTipsDialogFragment();
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

        View view = inflater.inflate(R.layout.dialog_free_card_tips, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView txtNotioce = view.findViewById(R.id.txt_no_notice);
        ImageView imgClose = view.findViewById(R.id.img_close);


        txtNotioce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                //不再提示
                Arad.preferences.putBoolean(Constants.P_NOTICE, false);
                Arad.preferences.flush();

            }
        });
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
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

}
