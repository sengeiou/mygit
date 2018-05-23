package com.beanu.l4_clean.mvp.presenter;

import android.text.TextUtils;

import com.beanu.arad.support.log.KLog;
import com.beanu.l4_clean.model.bean.PKMatch;
import com.beanu.l4_clean.mvp.contract.ChallengeContract;
import com.beanu.l4_clean.util.RongChatRoomUtil;

import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.rong.imlib.RongIMClient;

/**
 * Created by Beanu on 2017/11/25
 */

public class ChallengePresenterImpl extends ChallengeContract.Presenter {

    private PKMatch mPKMatch;


    @Override
    public void onStart() {


        mModel.invite2PK().subscribe(new Observer<Map<String, String>>() {
            @Override
            public void onSubscribe(Disposable d) {
                mRxManage.add(d);
            }

            @Override
            public void onNext(Map<String, String> map) {
                String code = map.get("code");
                String shareUrl = map.get("share_url");

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                initMatch();

            }
        });
    }

    @Override
    public void initMatch() {
        mModel.initMatch().subscribe(new Observer<PKMatch>() {
            @Override
            public void onSubscribe(Disposable d) {
                mRxManage.add(d);
            }

            @Override
            public void onNext(PKMatch pkMatch) {
                mPKMatch = pkMatch;
                if (!TextUtils.isEmpty(pkMatch.getId())) {
                    joinChatRoom(pkMatch.getId());
                    mView.uiInit(pkMatch);
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void acceptInvitation(String code) {
        mView.showProgress();
        mModel.acceptInvitation(code).subscribe(new Observer<Map<String, String>>() {
            @Override
            public void onSubscribe(Disposable d) {
                mRxManage.add(d);
            }

            @Override
            public void onNext(Map<String, String> map) {
                mView.hideProgress();

                String chatRoomId = map.get("id");
                if (!TextUtils.isEmpty(chatRoomId)) {
                    joinChatRoom(chatRoomId);
                    mView.uiAcceptInvitation(chatRoomId);
                }
            }

            @Override
            public void onError(Throwable e) {
                mView.hideProgress();
                e.printStackTrace();
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

                //发送消息
                RongChatRoomUtil.sendChatRoomMessage(chatRoomId, "", "CPK0", null);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                KLog.d(errorCode.getMessage());
            }
        });
    }


    public PKMatch getPKMatch() {
        return mPKMatch;
    }
}