package com.beanu.l3_common.model.bean;

import java.util.HashMap;

/**
 * 所有的事件分发
 * Created by Beanu on 16/9/8.
 */
public class EventModel {


    //登陆事件
    public static class LoginEvent {

        public User mUser;

        public LoginEvent(User user) {
            mUser = user;
        }
    }


    //购物车购买成功
    public static class CartBuySuccess {

        public CartBuySuccess() {
        }

    }

    //跟服务器TCP连接成功
    public static class SocketConnect {
        public boolean success;

        public SocketConnect(boolean success) {
            this.success = success;
        }
    }

    //后台发过来的通知
    public static class ChatRoomFormServer {
        public HashMap<String, String> message;
        public String extra;

        public ChatRoomFormServer(HashMap<String, String> message, String extra) {
            this.message = message;
            this.extra = extra;
        }
    }

    // 加入or退出聊天室
    public static class ChatRoomInfoUpdate {
        public String extra;

        public String userName;
        public String userIcon;
        public String userId;

        public ChatRoomInfoUpdate(String extra) {
            this.extra = extra;
        }
    }

    //加入or退出PK聊天室
    public static class ChatRoomPKInfoUpdate {
        public String extra;
        public String userName;
        public String userIcon;
        public String userId;

        public ChatRoomPKInfoUpdate(String extra) {
            this.extra = extra;
        }
    }

    //聊天室消息
    public static class ChatRoomMessage {
        public String userName;
        public String content;

        public ChatRoomMessage(String userName, String content) {
            this.userName = userName;
            this.content = content;
        }
    }

    //pk 次数
    public static class ChatRoomPKTimes {
        public String extra;
        public String times;

        public ChatRoomPKTimes(String extra, String times) {
            this.extra = extra;
            this.times = times;
        }
    }

}