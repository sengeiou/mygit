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

import com.beanu.arad.http.RxHelper;
import com.beanu.arad.utils.ToastUtils;
import com.beanu.l3_common.model.api.API;
import com.beanu.l4_clean.R;
import com.beanu.l4_clean.model.APIService;
import com.beanu.l4_clean.model.bean.Rewards;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 每日签到 dialog
 * Created by Beanu on 2017/11/22.
 */

public class RewardsDialogFragment extends DialogFragment {

    private static final String TAG = "RewardsDialog";
    private Rewards mRewards;

    private Listener mListener;

    public interface Listener {
        public void onClose();
    }


    public static RewardsDialogFragment newInstance() {
        RewardsDialogFragment f = new RewardsDialogFragment();
        return f;
    }

    public static void show(FragmentManager manager, Rewards rewards, Listener listener) {
        newInstance()
                .setRewards(rewards)
                .setListener(listener)
                .show(manager, TAG);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCanceledOnTouchOutside(false);

        View view = inflater.inflate(R.layout.dialog_rewards, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView mImg = view.findViewById(R.id.img_check_in);
        TextView mTxtCoin = view.findViewById(R.id.txt_coins);
        TextView mTxtDays = view.findViewById(R.id.txt_check_days);

        if (mRewards != null) {
            mTxtCoin.setText("+" + mRewards.getZsCoins());
        }

        mImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRewards();
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


    public RewardsDialogFragment setRewards(Rewards rewards) {
        mRewards = rewards;
        return this;
    }

    public RewardsDialogFragment setListener(Listener listener) {
        mListener = listener;
        return this;
    }

    private void getRewards() {
        API.getInstance(APIService.class).userRewards().compose(RxHelper.handleResult()).subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Object o) {
                if (mListener != null) {
                    mListener.onClose();
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
                dismiss();
                ToastUtils.showShort("领取成功");
            }
        });
    }

}