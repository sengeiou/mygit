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
import android.widget.TextView;

import com.beanu.l4_clean.R;

/**
 * 兑换积分
 */

public class ConfirmRewardsDialog extends DialogFragment {

    private View.OnClickListener mListener;
    private int coins;

    public static ConfirmRewardsDialog newInstance() {
        ConfirmRewardsDialog dialog = new ConfirmRewardsDialog();
        return dialog;
    }

    public static void show(FragmentManager manager, int coins, View.OnClickListener listener) {
        newInstance().setCoins(coins).setListener(listener).show(manager, "ConfirmRewardsDialog");
    }

    public ConfirmRewardsDialog setListener(View.OnClickListener listener) {
        mListener = listener;
        return this;
    }

    public ConfirmRewardsDialog setCoins(int coins) {
        this.coins = coins;
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Dialog dialog = getDialog();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return inflater.inflate(R.layout.dialog_confirm_rewards, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView txtResult = view.findViewById(R.id.txt_result);
        txtResult.setText(String.format("您已成功兑换%s游戏币,请查收", coins));

        TextView tvConfirm = view.findViewById(R.id.btn_commit);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mListener != null) {
                    mListener.onClick(v);
                }
            }
        });
    }
}
