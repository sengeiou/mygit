package com.beanu.l4_clean.support.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.beanu.l4_clean.R;

/**
 * 提交发货
 */

public class ConfirmOrderDialog extends DialogFragment {


    private View.OnClickListener mListener;

    public static ConfirmOrderDialog newInstance() {
        ConfirmOrderDialog dialog = new ConfirmOrderDialog();
        return dialog;
    }

    public static void show(FragmentManager manager, View.OnClickListener listener) {
        newInstance().setListener(listener).show(manager, "ConfirmOrderDialog");
    }

    public ConfirmOrderDialog setListener(View.OnClickListener listener) {
        mListener = listener;
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Dialog dialog = getDialog();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return inflater.inflate(R.layout.dialog_confirm_order, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        TextView tvConfirm = view.findViewById(R.id.btn_commit);
//        tvConfirm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//                if (mListener != null) {
//                    mListener.onClick(v);
//                }
//            }
//        });
    }
}
