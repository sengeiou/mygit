package com.beanu.l4_clean.mvp.contract;

import com.beanu.arad.base.BaseModel;
import com.beanu.arad.base.BasePresenter;
import com.beanu.arad.base.BaseView;
import com.beanu.l4_clean.model.bean.PKMatch;

import java.util.Map;

import io.reactivex.Observable;

/**
 * 发起挑战
 * Created by Beanu on 2017/11/25.
 */

public interface ChallengeContract {

    public interface View extends BaseView {
        void uiInit(PKMatch pkMatch);

        void uiAcceptInvitation(String chatRoomId);

        void uiInvite(String code, String shareUrl);
    }

    public abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void initMatch();

        public abstract void acceptInvitation(String code);

        public abstract void joinChatRoom(String chatRoomId);

//        public abstract void quitChatRoom(String chatRoomId);
//
//        public abstract void getChatRoomInfo(String chatRoomId);

    }

    public interface Model extends BaseModel {

        public Observable<PKMatch> initMatch();

        public Observable<Map<String, String>> acceptInvitation(String code);

        public Observable<Map<String, String>> invite2PK();
    }


}