package com.beanu.l4_bottom_tab.support.draw;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.beanu.arad.Arad;
import com.beanu.arad.utils.StreamUtil;
import com.beanu.l4_bottom_tab.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * 画图程序
 */
public class DrawFragment extends Fragment {

    @BindView(R.id.btn_undo) ImageButton mBtnUndo;
    @BindView(R.id.btn_redo) ImageButton mBtnRedo;
    @BindView(R.id.btn_remove) ImageButton mBtnRemove;
    @BindView(R.id.btn_save) ImageButton mBtnSave;
    @BindView(R.id.fl_board) FrameLayout mFlBoard;
    Unbinder unbinder;

    private static final String ARG_PARAM1 = "param1";

    private String mQuestionId;
    private TuyaView tuyaView;//自定义涂鸦板
    private PathInfo mPathInfo;


    public DrawFragment() {
        // Required empty public constructor
    }

    public static DrawFragment newInstance(String param1) {
        DrawFragment fragment = new DrawFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mQuestionId = getArguments().getString(ARG_PARAM1);
        }

        Object obj = StreamUtil.restoreObject(Arad.app.getCacheDir().getAbsolutePath() + "\\" + mQuestionId);
        if (obj != null) {
            mPathInfo = (PathInfo) obj;
        } else {
            mPathInfo = new PathInfo();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_draw, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

        if (mPathInfo != null) {
            StreamUtil.saveObject(Arad.app.getCacheDir().getAbsolutePath() + "\\" + mQuestionId, mPathInfo);
            mPathInfo = null;
        }
    }

    private void initData() {
        //虽然此时获取的是屏幕宽高，但是我们可以通过控制framlayout来实现控制涂鸦板大小
        Display defaultDisplay = getActivity().getWindowManager().getDefaultDisplay();
        int screenWidth = defaultDisplay.getWidth();
        int screenHeight = defaultDisplay.getHeight();
        tuyaView = new TuyaView(getActivity(), screenWidth, screenHeight);
        mFlBoard.addView(tuyaView);
        tuyaView.requestFocus();
        tuyaView.selectPaintSize(10);
        tuyaView.selectPaintColor(4);
        if (mPathInfo != null) {
            tuyaView.setPathInfo(mPathInfo);
        }
    }

    @OnClick({R.id.btn_close, R.id.btn_undo, R.id.btn_redo, R.id.btn_remove, R.id.btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_close:

                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment prev = getActivity().getSupportFragmentManager().findFragmentByTag("draw");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.commit();

                break;
            case R.id.btn_undo:
                tuyaView.undo();
                break;
            case R.id.btn_redo:
                tuyaView.recover();
                break;
            case R.id.btn_remove:
                tuyaView.redo();
                break;
            case R.id.btn_save:
                tuyaView.saveToSDCard();
                break;
        }
    }


}
