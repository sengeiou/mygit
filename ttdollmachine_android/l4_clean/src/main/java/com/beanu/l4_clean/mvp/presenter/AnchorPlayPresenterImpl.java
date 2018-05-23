package com.beanu.l4_clean.mvp.presenter;

import com.beanu.arad.Arad;
import com.beanu.arad.support.log.KLog;
import com.beanu.l3_common.model.bean.EventModel;
import com.beanu.l4_clean.model.bean.Friend;
import com.beanu.l4_clean.model.bean.LiveRoom2Audience;
import com.beanu.l4_clean.model.bean.OnlinePeople;
import com.beanu.l4_clean.mvp.contract.AnchorPlayContract;
import com.beanu.l4_clean.util.RongChatRoomUtil;
import com.beanu.l4_clean.util.RongCloudEvent;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.ChatRoomInfo;
import io.rong.imlib.model.ChatRoomMemberInfo;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;

/**
 * Created by Beanu on 2017/12/04
 */

public class AnchorPlayPresenterImpl extends AnchorPlayContract.Presenter {

    private String chatRoomId;

    @Override
    public void audienceLiveRoom(String roomId, String logId) {
        chatRoomId = roomId;
        mModel.audienceLiveRoom(roomId, logId).subscribe(new Observer<LiveRoom2Audience>() {
            @Override
            public void onSubscribe(Disposable d) {
                mRxManage.add(d);
            }

            @Override
            public void onNext(LiveRoom2Audience liveRoom2Audience) {
                mView.initUI(liveRoom2Audience);

                //加入聊天室
                joinChatRoom(chatRoomId);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void anchorHelpMe(String logId) {
        mModel.anchorHelpMe(logId).subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {
                mRxManage.add(d);
            }

            @Override
            public void onNext(Object s) {
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                mView.uiHelpMe(false);

            }

            @Override
            public void onComplete() {
                mView.uiHelpMe(true);

                //通知聊天室的人，我申请了帮我抓
                RongChatRoomUtil.sendChatRoomMessage(chatRoomId, "", "CZB0", null);
            }
        });
    }

    @Override
    public void joinChatRoom(final String chatRoomId) {
        RongIMClient.getInstance().joinChatRoom(chatRoomId, -1, new RongIMClient.OperationCallback() {
            @Override
            public void onSuccess() {

                //获得聊天室详情
                getChatRoomInfo(chatRoomId);

                //发送 更新消息-更新人数
                RongChatRoomUtil.sendChatRoomMessage(chatRoomId, "", "C0", null);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                KLog.d(errorCode.getMessage());
            }
        });
    }

    @Override
    public void quitChatRoom(final String chatRoomId) {
        RongIMClient.getInstance().quitChatRoom(chatRoomId, new RongIMClient.OperationCallback() {
            @Override
            public void onSuccess() {
                RongChatRoomUtil.sendChatRoomMessage(chatRoomId, "", "C1", null);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }

    @Override
    public void getChatRoomInfo(String chatRoomId) {
        RongIMClient.getInstance().getChatRoomInfo(chatRoomId, 5, ChatRoomInfo.ChatRoomMemberOrder.RC_CHAT_ROOM_MEMBER_DESC, new RongIMClient.ResultCallback<ChatRoomInfo>() {
            @Override
            public void onSuccess(final ChatRoomInfo chatRoomInfo) {


                final List<Friend> friends = new ArrayList<>();
                Observable
                        .fromIterable(chatRoomInfo.getMemberInfo())
                        .flatMap(new Function<ChatRoomMemberInfo, ObservableSource<Friend>>() {
                            @Override
                            public ObservableSource<Friend> apply(ChatRoomMemberInfo chatRoomMemberInfo) throws Exception {
                                return RongCloudEvent.getInstance().getUserInfo(chatRoomMemberInfo.getUserId());
                            }
                        })
                        .onErrorReturnItem(new Friend())
                        .subscribe(new Observer<Friend>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(Friend friend) {
                                friends.add(friend);
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                                List<OnlinePeople> onlinePeopleList = new ArrayList<>();
                                for (Friend friend : friends) {
                                    OnlinePeople onlinePeople = new OnlinePeople(friend.getId(), friend.getNickname(), friend.getIcon());
                                    onlinePeopleList.add(onlinePeople);
                                }
                                mView.refreshOnlinePeople(onlinePeopleList, chatRoomInfo.getTotalMemberCount());
                            }
                        });

            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }

    @Override
    public void selfSendMessage(String message) {
        RongChatRoomUtil.sendChatRoomMessage(chatRoomId, message, null, new IRongCallback.ISendMessageCallback() {
            @Override
            public void onAttached(Message message) {

            }

            @Override
            public void onSuccess(Message message) {

                if (message.getConversationType() == Conversation.ConversationType.CHATROOM) {

                    if (message.getContent() instanceof TextMessage) {
                        TextMessage textMessage = (TextMessage) message.getContent();
                        Arad.bus.post(new EventModel.ChatRoomMessage(textMessage.getUserInfo().getName(), textMessage.getContent()));
                    }
                }

            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {

            }
        });
    }
}