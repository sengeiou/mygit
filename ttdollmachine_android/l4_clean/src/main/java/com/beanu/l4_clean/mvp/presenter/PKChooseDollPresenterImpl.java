package com.beanu.l4_clean.mvp.presenter;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.beanu.arad.support.log.KLog;
import com.beanu.l4_clean.model.bean.Dolls;
import com.beanu.l4_clean.model.bean.Friend;
import com.beanu.l4_clean.mvp.contract.PKChooseDollContract;
import com.beanu.l4_clean.util.RongChatRoomUtil;
import com.beanu.l4_clean.util.RongCloudEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.ChatRoomInfo;
import io.rong.imlib.model.ChatRoomMemberInfo;

/**
 * Created by Beanu on 2017/11/25
 */

public class PKChooseDollPresenterImpl extends PKChooseDollContract.Presenter {


    private boolean oppositeReady;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        dollsList();
    }


    @Override
    public void dollsList() {
        mModel.httpDollsList().subscribe(new Observer<List<Dolls>>() {
            @Override
            public void onSubscribe(Disposable d) {
                mRxManage.add(d);
            }

            @Override
            public void onNext(List<Dolls> dollsPageModel) {
                mView.uiRefreshRecycleView(dollsPageModel);
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
    public void pkConfirmReady(String dollMaId, final String matchId) {
        mModel.pkConfirmReady(dollMaId, matchId).subscribe(new Observer<Map<String, String>>() {
            @Override
            public void onSubscribe(Disposable d) {
                mRxManage.add(d);
            }

            @Override
            public void onNext(Map<String, String> map) {
//                String gameId = map.get("gameId");
                String machineStatus = map.get("machineStatus");

                if ("1".equals(machineStatus)) {
                    //机器可用 已准备
                    if (oppositeReady) {
                        RongChatRoomUtil.sendChatRoomMessage(matchId, "", "CPK3", null);
                        mView.uiGoToGame();
                    } else {
                        RongChatRoomUtil.sendChatRoomMessage(matchId, "", "CPK2", null);
                        mView.uiReady(true);
                    }
                } else {
                    //当年机器不可用
                    mView.uiReady(false);
                }

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                //TODO 余额不足的情况
            }

            @Override
            public void onComplete() {

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
                RongChatRoomUtil.sendChatRoomMessage(chatRoomId, "", "CPK0", null);
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
                RongChatRoomUtil.sendChatRoomMessage(chatRoomId, "", "CPK1", null);
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

                int memberCount = chatRoomInfo.getTotalMemberCount();
                if (memberCount == 2) {//当前满两人
                    mView.uiRefreshChallenger(true);
                } else {
                    mView.uiRefreshChallenger(false);

                }


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
                                if (friends.size() == 2) {
                                    mView.uiRefreshAvatar(friends.get(0), friends.get(1));
                                }
                            }
                        });

            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }

    public void setOppositeReady(boolean oppositeReady) {
        this.oppositeReady = oppositeReady;
    }
}