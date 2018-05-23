package com.beanu.l4_clean.support.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.beanu.l3_common.util.AppHolder;
import com.beanu.l4_clean.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

/**
 * 带图片的dialog
 * Created by Beanu on 2017/11/22.
 */

public class PKResultDialogFragment extends DialogFragment {


    private static final String TAG = "PKResultDialog";

    public interface Listener {
        void onClickLeft(View view);

        void onClickRight(View view);
    }

    private Listener mListener;
    private int meTimes, otherTimes;
    private String otherAvatar, otherName;

    public static PKResultDialogFragment newInstance(boolean success) {
        PKResultDialogFragment f = new PKResultDialogFragment();

        Bundle args = new Bundle();
        args.putBoolean("success", success);
        f.setArguments(args);

        return f;
    }


    public static void show(boolean success, int meTimes, int otherTimes, String otherAvatar, String otherName, Listener listener, FragmentManager manager) {
        newInstance(success)
                .setListener(listener)
                .setMeTimes(meTimes)
                .setOtherTimes(otherTimes)
                .setOtherAvatar(otherAvatar)
                .setOtherName(otherName)
                .show(manager, TAG);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCanceledOnTouchOutside(true);

        View view = inflater.inflate(R.layout.dialog_pk_result, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView txt1 = view.findViewById(R.id.txt_name_me);
        TextView txt2 = view.findViewById(R.id.txt_name_other);

        ImageView img1 = view.findViewById(R.id.img_avatar_me);
        ImageView img2 = view.findViewById(R.id.img_avatar_other);

        TextView times1 = view.findViewById(R.id.txt_times_me);
        TextView times2 = view.findViewById(R.id.txt_times_other);

        ImageView imgTop = view.findViewById(R.id.imageView8);
        TextView txtTips = view.findViewById(R.id.txt_tips);

        Button btn_ok = view.findViewById(R.id.btn_ok);
        Button btn_share = view.findViewById(R.id.btn_share);

        if (!TextUtils.isEmpty(AppHolder.getInstance().user.getIcon())) {
            Glide.with(getContext()).load(AppHolder.getInstance().user.getIcon()).apply(RequestOptions.circleCropTransform()).into(img1);
        }
        if (!TextUtils.isEmpty(otherAvatar)) {
            Glide.with(getContext()).load(otherAvatar).apply(RequestOptions.circleCropTransform()).into(img2);
        }

        times1.setText("抓了" + meTimes + "次");
        times2.setText("抓了" + otherTimes + "次");
        txt1.setText(AppHolder.getInstance().user.getNickname());
        txt2.setText(otherName);


        ConstraintLayout layout = view.findViewById(R.id.layout_top);
        if (getArguments() != null) {
            boolean success = getArguments().getBoolean("success");
            if (success) {
                layout.setBackgroundResource(R.drawable.success);
                btn_share.setVisibility(View.VISIBLE);
                Glide.with(getContext()).load(AppHolder.getInstance().user.getIcon()).apply(RequestOptions.circleCropTransform()).into(imgTop);
                txtTips.setText(AppHolder.getInstance().user.getNickname() + "获胜，获得100开心币奖励");
            } else {
                layout.setBackgroundResource(R.drawable.lost);
                btn_share.setVisibility(View.GONE);
                if (!TextUtils.isEmpty(otherAvatar)) {
                    Glide.with(getContext()).load(otherAvatar).apply(RequestOptions.circleCropTransform()).into(imgTop);
                }
                txtTips.setText(otherName + "获胜，获得100开心币奖励");

            }


        }

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (mListener != null)
                    mListener.onClickLeft(view);
            }
        });
        btn_share.setOnClickListener(new View.OnClickListener() {
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
        getDialog().getWindow().setLayout((int) (dm.widthPixels * 0.9), ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    public PKResultDialogFragment setListener(Listener listener) {
        mListener = listener;
        return this;
    }

    public PKResultDialogFragment setMeTimes(int meTimes) {
        this.meTimes = meTimes;
        return this;
    }

    public PKResultDialogFragment setOtherTimes(int otherTimes) {
        this.otherTimes = otherTimes;
        return this;
    }

    public PKResultDialogFragment setOtherAvatar(String otherAvatar) {
        this.otherAvatar = otherAvatar;
        return this;
    }

    public PKResultDialogFragment setOtherName(String otherName) {
        this.otherName = otherName;
        return this;
    }
}