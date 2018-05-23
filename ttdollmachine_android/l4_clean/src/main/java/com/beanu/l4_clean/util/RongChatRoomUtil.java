package com.beanu.l4_clean.util;

import android.net.Uri;

import com.beanu.arad.support.log.KLog;
import com.beanu.l3_common.model.bean.User;
import com.beanu.l3_common.util.AppHolder;

import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.TextMessage;

/**
 * 融云聊天室 常用操作
 * Created by Beanu on 2017/11/25.
 */

public class RongChatRoomUtil {

    public static void sendChatRoomMessage(String chatRoomId, String content, String extra, IRongCallback.ISendMessageCallback callback) {

        User user = AppHolder.getInstance().user;

        if (user.isLogin()) {
            TextMessage textMessage = TextMessage.obtain(content == null ? "" : content);
            textMessage.setExtra(extra);
            textMessage.setUserInfo(new UserInfo(user.getId(), user.getNickname(), user.getIcon() == null ? null : Uri.parse(user.getIcon())));

            Message myMessage = Message.obtain(chatRoomId, Conversation.ConversationType.CHATROOM, textMessage);
            IRongCallback.ISendMessageCallback messageCallback;

            if (callback == null) {
                messageCallback = new IRongCallback.ISendMessageCallback() {
                    @Override
                    public void onAttached(Message message) {
                        //消息本地数据库存储成功的回调

                    }

                    @Override
                    public void onSuccess(Message message) {
                        //消息通过网络发送成功的回调
                        KLog.d("发送成功");
                    }

                    @Override
                    public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                        //消息发送失败的回调
                        KLog.d(errorCode.getMessage() + "：" + errorCode.getValue());

                    }
                };
            } else {
                messageCallback = callback;
            }

            RongIMClient.getInstance().sendMessage(myMessage, null, null, messageCallback);
        }
    }
}
