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
 * 休息dialog
 * Created by Beanu on 16/9/21.
 */
public class AlertDialogRest extends DialogFragment {

    private View.OnClickListener mClickListener;

    public static AlertDialogRest newInstance(String message) {
        AlertDialogRest f = new AlertDialogRest();

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
        getDialog().setCanceledOnTouchOutside(false);

        View view = inflater.inflate(R.layout.dialog_alert_rest, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView txtSure = (TextView) view.findViewById(R.id.txt_sure);
        TextView txtTitle = (TextView) view.findViewById(R.id.txt_title2);

        txtSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                mClickListener.onClick(view);
            }
        });

        if (getArguments() != null) {
            String message = getArguments().getString("message");
            txtTitle.setText(message);
        }
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
}
