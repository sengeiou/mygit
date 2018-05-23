package com.beanu.l4_clean.util;


import android.content.Context;
import android.text.TextUtils;

import com.beanu.arad.Arad;
import com.beanu.arad.http.RxHelper;
import com.beanu.arad.support.log.KLog;
import com.beanu.l3_common.model.api.API;
import com.beanu.l3_common.model.bean.EventModel;
import com.beanu.l3_common.model.bean.User;
import com.beanu.l3_common.util.AppHolder;
import com.beanu.l4_clean.model.APIService;
import com.beanu.l4_clean.model.bean.Friend;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;

/**
 * 融云事件 监听
 * * 把事件统一处理，开发者可直接复制到自己的项目中去使用。
 * <p/>
 * 该类包含的监听事件有：
 * 1、消息接收器：OnReceiveMessageListener。
 * 2、发出消息接收器：OnSendMessageListener。
 * 3、用户信息提供者：GetUserInfoProvider。
 * 4、好友信息提供者：GetFriendsProvider。
 * 5、群组信息提供者：GetGroupInfoProvider。
 * 7、连接状态监听器，以获取连接相关状态：ConnectionStatusListener。
 * 8、地理位置提供者：LocationProvider。
 * 9、自定义 push 通知： OnReceivePushMessageListener。
 * 10、会话列表界面操作的监听器：ConversationListBehaviorListener。
 * Created by Beanu on 2017/11/14.
 */
public class RongCloudEvent implements RongIMClient.OnReceiveMessageListener {

    private static RongCloudEvent mRongCloudInstance;

    private Context mContext;

    /**
     * 初始化 RongCloud.
     *
     * @param context 上下文。
     */
    public static void init(Context context) {

        if (mRongCloudInstance == null) {

            synchronized (RongCloudEvent.class) {

                if (mRongCloudInstance == null) {
                    mRongCloudInstance = new RongCloudEvent(context);
                }
            }
        }
    }

    public static RongCloudEvent getInstance() {
        return mRongCloudInstance;
    }

    /**
     * 构造方法。
     *
     * @param context 上下文。
     */
    private RongCloudEvent(Context context) {
        mContext = context;
        initDefaultListener();
    }

    private void initDefaultListener() {
        RongIMClient.setOnReceiveMessageListener(this);
    }


    @Override
    public boolean onReceived(Message message, int i) {

        //如果是聊天室消息
        if (message.getConversationType() == Conversation.ConversationType.CHATROOM) {

            if (message.getContent() instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message.getContent();
                KLog.d("消息:" + textMessage.getContent() + ":extra:" + textMessage.getExtra());

                //如果是后台发送的消息
                if ("FFFFFF".equals(message.getSenderUserId())) {

                    String json = textMessage.getContent();
                    HashMap<String, String> map = new Gson().fromJson(json, new TypeToken<HashMap<String, String>>() {
                    }.getType());
                    if (map != null) {
                        Arad.bus.post(new EventModel.ChatRoomFormServer(map, textMessage.getExtra()));
                    }

                } else {

                    String extra = textMessage.getExtra();
                    if (!TextUtils.isEmpty(extra) && i == 0) {//如果是自定义消息

                        if ("C0".equals(extra) || "C1".equals(extra) || "CZB0".equals(extra) || "CZB2".equals(extra)) {
                            EventModel.ChatRoomInfoUpdate infoUpdate = new EventModel.ChatRoomInfoUpdate(extra);
                            infoUpdate.userId = textMessage.getUserInfo().getUserId();
                            infoUpdate.userName = textMessage.getUserInfo().getName();
                            infoUpdate.userIcon = textMessage.getUserInfo().getPortraitUri().toString();
                            Arad.bus.post(infoUpdate);
                        }

                        if ("CZB1".equals(extra)) {
                            String json = textMessage.getContent();
                            HashMap<String, String> map = new Gson().fromJson(json, new TypeToken<HashMap<String, String>>() {
                            }.getType());
                            if (map != null) {
                                EventModel.ChatRoomInfoUpdate infoUpdate = new EventModel.ChatRoomInfoUpdate(extra);
                                infoUpdate.userId = map.get("users_id");
                                infoUpdate.userName = map.get("users_nickName");
                                infoUpdate.userIcon = map.get("users_icon");

                                Arad.bus.post(infoUpdate);
                            }
                        }


                        if ("CPK0".equals(extra) || "CPK1".equals(extra)) {

                            EventModel.ChatRoomPKInfoUpdate infoUpdate = new EventModel.ChatRoomPKInfoUpdate(extra);
                            infoUpdate.userId = textMessage.getUserInfo().getUserId();
                            infoUpdate.userName = textMessage.getUserInfo().getName();
                            infoUpdate.userIcon = textMessage.getUserInfo().getPortraitUri().toString();
                            Arad.bus.post(infoUpdate);
                        }

                        if ("CPK2".equals(extra) || "CPK3".equals(extra)) {
                            Arad.bus.post(new EventModel.ChatRoomPKInfoUpdate(extra));
                        }

                        if ("CPK4".equals(extra)) {
                            Arad.bus.post(new EventModel.ChatRoomPKTimes(extra, textMessage.getContent()));
                        }

                    }

                    if (TextUtils.isEmpty(extra)) {//如果是普通消息
                        if (textMessage.getUserInfo() != null) {
                            Arad.bus.post(new EventModel.ChatRoomMessage(textMessage.getUserInfo().getName(), textMessage.getContent()));
                        }
                    }

                }


            }

        }

        return false;
    }


    public Observable<Friend> getUserInfo(final String userId) {

        if (userId == null)
            return null;

        final User user = AppHolder.getInstance().user;

        if (user.getId().equals(userId)) {//是自己
            return Observable.create(new ObservableOnSubscribe<Friend>() {
                @Override
                public void subscribe(ObservableEmitter<Friend> e) throws Exception {
                    Friend friend = new Friend(userId, user.getNickname(), user.getIcon());
                    e.onNext(friend);
                    e.onComplete();
                }
            });
        } else {
            return Observable.concat(diskDB(userId), network(userId))
                    .firstElement()
                    .toObservable();
        }

    }


    private Observable<Friend> diskDB(final String userId) {
        return Observable.create(new ObservableOnSubscribe<Friend>() {
            @Override
            public void subscribe(ObservableEmitter<Friend> e) throws Exception {
                Friend friend = Arad.db.findById(Friend.class, userId);
                if (friend != null) {
                    e.onNext(friend);
                } else {
                    e.onComplete();
                }

            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    private Observable<Friend> network(final String userId) {

        return API.getInstance(APIService.class).getFriendInfo(userId)
                .compose(RxHelper.<Friend>handleResult())
                .doOnNext(new Consumer<Friend>() {
                    @Override
                    public void accept(Friend friend) throws Exception {
                        Arad.db.save(friend);
                    }
                });
    }
}