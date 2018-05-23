package com.beanu.l4_clean.support.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.beanu.l3_common.util.AppHolder;
import com.beanu.l4_clean.R;

/**
 * 推荐 绑定
 */
public class RecommedBindFragment extends DialogFragment {

    private Listener mListener;


    public RecommedBindFragment setListener(Listener listener) {
        mListener = listener;
        return this;
    }

    private static RecommedBindFragment newInstance() {
        RecommedBindFragment f = new RecommedBindFragment();
        return f;
    }

    public static void show(FragmentManager manager, Listener listener) {
        newInstance().setListener(listener).show(manager, "RecommendDialog");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCanceledOnTouchOutside(true);

        View view = inflater.inflate(R.layout.fragment_recommend_bind, container, false);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final EditText editText = view.findViewById(R.id.textView27);
        Button btn_ok = view.findViewById(R.id.btn_ok);
        final TextView title = view.findViewById(R.id.img_tips);

        String recommend = AppHolder.getInstance().user.getRecommend_code();
        if (!TextUtils.isEmpty(recommend)) {
            editText.setText(recommend);
            editText.setEnabled(false);
            btn_ok.setVisibility(View.INVISIBLE);
            title.setText("已绑定邀请码");
        }

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = editText.getText().toString();
                if (mListener != null) {
                    mListener.onClickBind(RecommedBindFragment.this, code);
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
        void onClickBind(DialogFragment dialogFragment, String code);
    }

}