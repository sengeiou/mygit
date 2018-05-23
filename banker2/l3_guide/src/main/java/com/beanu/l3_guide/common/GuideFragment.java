package com.beanu.l3_guide.common;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.beanu.arad.Arad;
import com.beanu.l3_common.util.Constants;
import com.beanu.l3_guide.R;

import java.io.IOException;
import java.io.InputStream;

public class GuideFragment extends Fragment {

    String fileName = "";
    ImageView img;

    public static GuideFragment newInstance(String fileName, boolean lastOne) {
        GuideFragment fragment = new GuideFragment();
        Bundle args = new Bundle();
        args.putString("fileName", fileName);
        args.putBoolean("lastOne", lastOne);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            fileName = getArguments().getString("fileName");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.guide_item, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments().getBoolean("lastOne")) {
            img = (ImageView) view.findViewById(R.id.btn_enter);
            img.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    Arad.preferences.putBoolean(Constants.P_ISFIRSTLOAD, false);
                    Arad.preferences.flush();

                    ARouter.getInstance().build("/login/signIn2").withTransition(R.anim.anim_slide_in, R.anim.anim_none).navigation(getActivity());
                    getActivity().finish();
                }
            });
        }

        if (!fileName.equals("")) {
            ImageView img = (ImageView) view.findViewById(R.id.guide_item_img);
            img.setImageBitmap(getBitmap(fileName));
        }

    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && getArguments().getBoolean("lastOne")) {
            if (img != null) {
                img.setAlpha(0f);
                img.setVisibility(View.VISIBLE);
                img.animate().alpha(1f).setDuration(600).setListener(null);
            }
        } else {
            if (img != null) {
                img.setVisibility(View.GONE);
            }
        }
    }

    private Bitmap getBitmap(String fileName) {
        Bitmap bitmap = null;
        try {
            InputStream is = getResources().getAssets().open(fileName);
            bitmap = BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
