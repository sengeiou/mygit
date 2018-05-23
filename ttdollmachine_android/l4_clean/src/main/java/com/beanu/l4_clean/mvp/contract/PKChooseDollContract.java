package com.beanu.l4_clean.mvp.contract;

import com.beanu.arad.base.BaseModel;
import com.beanu.arad.base.BasePresenter;
import com.beanu.arad.base.BaseView;
import com.beanu.l4_clean.model.bean.Dolls;
import com.beanu.l4_clean.model.bean.Friend;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;


/**
 * PKChooseDoll
 * Created by Beanu on 2017/11/25.
 */

public interface PKChooseDollContract {


    public interface View extends BaseView {
        void uiRefreshAvatar(Friend friend, Friend friend2);

        void uiRefreshChallenger(boolean show);


        void uiRefreshRecycleView(List<Dolls> dollsList);

        void uiReady(boolean isOK);

        void uiGoToGame();

    }

    public abstract class Presenter extends BasePresenter<View, Model> {

        public abstract void dollsList();

        public abstract void pkConfirmReady(String dollMaId, String matchId);


        public abstract void joinChatRoom(String chatRoomId);

        public abstract void quitChatRoom(String chatRoomId);

        public abstract void getChatRoomInfo(String chatRoomId);
    }

    public interface Model extends BaseModel {
        Observable<List<Dolls>> httpDollsList();

        Observable<Map<String, String>> pkConfirmReady(String dollMaId, String matchId);
    }


}