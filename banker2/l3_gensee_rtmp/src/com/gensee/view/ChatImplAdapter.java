package com.gensee.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gensee.adapter.AbsChatAdapter;
import com.gensee.chat.msg.AbsChatMessage;
import com.gensee.chat.msg.SysMessage;
import com.gensee.rtmpresourcelib.R;

public class ChatImplAdapter extends AbsChatAdapter {

    public ChatImplAdapter(Context context) {
        super(context);
    }

    @Override
    protected AbstractViewHolder createViewHolder(View view) {

        ChatViewHolder mChatViewHolder = new ChatViewHolder(view);
        return mChatViewHolder;
    }

    @Override
    protected View createView(LayoutInflater inflater) {
        View convertView = inflater.inflate(R.layout.chat_listitem_layout,
                null);
        return convertView;

    }

    private class ChatViewHolder extends AbsChatViewHolder {

        private LinearLayout mLinearLayout;
        private RelativeLayout mContainer;
        private ImageView mImgTag;
        private ImageView mDeleteTag;
        private TextView mTxtName;

        public ChatViewHolder(View view) {
            super(view);
            mLinearLayout = (LinearLayout) view.findViewById(R.id.ll_tag);
            mContainer = (RelativeLayout) view.findViewById(R.id.rl_container);
            mImgTag = (ImageView) view.findViewById(R.id.chattagimg);
            mDeleteTag = (ImageView) view.findViewById(getChatSysDelIvId());
            mTxtName = (TextView) view.findViewById(this.getChatNameEdtid());
        }

        @Override
        public void init(int positon) {
            super.init(positon);

            mDeleteTag.setVisibility(View.GONE);
            mContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    ViewGroup.LayoutParams layoutParams = mLinearLayout.getLayoutParams();
                    layoutParams.height = mContainer.getHeight();
                    mLinearLayout.setLayoutParams(layoutParams);

                    Log.d("TAG", mLinearLayout.getLayoutParams().height + "height");
                }
            });


            AbsChatMessage mAbsChatMessage = (AbsChatMessage) getItem(positon);

            if (mAbsChatMessage instanceof SysMessage) {
                mImgTag.setBackgroundResource(R.drawable.message_official);
            } else if (isHost(mAbsChatMessage.getSenderRole())) {
                mImgTag.setBackgroundResource(R.drawable.message_organizer);
                mTxtName.setTextColor(context.getResources().getColor(R.color.chat_host_name));
            } else {
                mImgTag.setBackgroundResource(R.drawable.message_person);
                mTxtName.setTextColor(context.getResources().getColor(R.color.chat_person_name));
            }

        }

        private boolean isHost(int role) {
            //组织者1  主讲2  助教4
            return (role & 1) == 1 || (role & 2) == 2 || (role & 4) == 4;
        }

        @Override
        protected int getChatNameEdtid() {
            return R.id.chatnametext;
        }

        @Override
        protected int getChatTimeTvid() {
            return R.id.chattimetext;
        }

        @Override
        protected int getChatContentTvId() {
            return R.id.chatcontexttextview;
        }

        @Override
        protected int getChatSysTvId() {
            return R.id.chat_listview_tex_context;
        }

        @Override
        protected int getChatSysDelIvId() {
            return R.id.chat_listview_bnt_delete_context;
        }

        @Override
        protected int getSysMsgTipId() {
            return R.string.chat_system_msg_colon;
        }

        @Override
        protected int getSysMsgColorId() {
            return R.color.chat_system_message;
        }

        @Override
        protected int getChatmeTipStrId() {
            return R.string.chat_me;
        }

        @Override
        protected int getChattoStrId() {
            return R.string.chat_to;
        }

        @Override
        protected int getChatsayStrId() {
            return R.string.chat_say;
        }

    }

}