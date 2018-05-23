package com.beanu.l4_clean.ui.anchor;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.beanu.arad.Arad;
import com.beanu.arad.support.recyclerview.divider.HorizontalDividerItemDecoration;
import com.beanu.l3_common.model.bean.EventModel;
import com.beanu.l4_clean.R;
import com.beanu.l4_clean.adapter.IMMessageAdapter;
import com.beanu.l4_clean.model.bean.IMMessage;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 主播直播 底部输入框
 */
public class AnchorLivingInputFragment extends Fragment {

    private static final String ARG_PARAM1 = "chatRoomId";
    private static final String ARG_PARAM2 = "logId";

    @BindView(R.id.recycle_view_message) RecyclerView mRecycleViewMessage;
    @BindView(R.id.img_message_control) ImageView mImgMessageControl;
    @BindView(R.id.btn_help_me) ImageView mBtnHelpMe;
    @BindView(R.id.btn_share) ImageView mBtnShare;
    @BindView(R.id.edit_message) EditText mEditMessage;
    @BindView(R.id.img_send) ImageView mImgSend;
    @BindView(R.id.layout_bottom) ConstraintLayout mLayoutBottom;
    Unbinder unbinder;

    private String mChatRoomId;
    private String mLogId;

    private OnFragmentInteractionListener mListener;

    //聊天消息
    IMMessageAdapter mIMMessageAdapter;
    private List<IMMessage> mIMMessageList;

    public AnchorLivingInputFragment() {
        // Required empty public constructor
    }

    public static AnchorLivingInputFragment newInstance(String param1, String param2) {
        AnchorLivingInputFragment fragment = new AnchorLivingInputFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mChatRoomId = getArguments().getString(ARG_PARAM1);
            mLogId = getArguments().getString(ARG_PARAM2);
        }


        Arad.bus.register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_anchor_living_input, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //聊天记录
        mIMMessageList = new ArrayList<>();
        mIMMessageAdapter = new IMMessageAdapter(getActivity(), mIMMessageList);
        mRecycleViewMessage.setAdapter(mIMMessageAdapter);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecycleViewMessage.setLayoutManager(linearLayoutManager1);
        mRecycleViewMessage.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).colorResId(android.R.color.transparent).sizeResId(R.dimen.grid_space).build());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        Arad.bus.unregister(this);
    }

    @OnClick({R.id.btn_help_me, R.id.btn_share, R.id.img_send, R.id.img_message_control})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_help_me:
                mListener.onHelpMe();
                break;
            case R.id.btn_share:
                mListener.onShare();
                break;
            case R.id.img_send:

                String message = mEditMessage.getText().toString();
                if (!TextUtils.isEmpty(message)) {
                    mListener.onSendMessage(message);

                    mEditMessage.setText("");
                }


                break;
            case R.id.img_message_control:
                if (mRecycleViewMessage.getVisibility() == View.GONE) {
                    mRecycleViewMessage.setVisibility(View.VISIBLE);
                    mImgMessageControl.setImageResource(R.drawable.shouqi);
                } else {
                    mRecycleViewMessage.setVisibility(View.GONE);
                    mImgMessageControl.setImageResource(R.drawable.zhankai);
                }
                break;
        }
    }


    public interface OnFragmentInteractionListener {
        void onSendMessage(String message);

        void onHelpMe();

        void onShare();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventListener(EventModel.ChatRoomMessage message) {
        mIMMessageList.add(new IMMessage(message.userName, message.content));
        if (mIMMessageList.size() > 30) {
            mIMMessageList.subList(mIMMessageList.size() - 30, 30);
        }

        mIMMessageAdapter.notifyDataSetChanged();
        mRecycleViewMessage.smoothScrollToPosition(mIMMessageList.size() - 1);
    }
}