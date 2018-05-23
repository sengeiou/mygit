package com.beanu.l4_clean.mvp.contract;

import android.widget.ImageView;

import com.beanu.arad.base.BaseModel;
import com.beanu.arad.base.BasePresenter;
import com.beanu.arad.base.BaseView;
import com.beanu.l4_clean.model.bean.PKMatchDetail;
import com.pili.pldroid.player.widget.PLVideoTextureView;
import com.qiniu.pili.droid.rtcstreaming.RTCVideoWindow;

import java.util.Map;

import io.reactivex.Observable;

/**
 * 游戏比赛
 * Created by Beanu on 2017/11/28.
 */

public interface PKGameContract {

    public interface View extends BaseView {

        void uiInit(PKMatchDetail detail);

        RTCVideoWindow createRTCVideoWindowA();

        RTCVideoWindow createRTCVideoWindowB();

        PLVideoTextureView findPLVideoOther();

        ImageView findLoadingView();

        void conferenceLoading(boolean loading);

        void activityFinish();


        void uiUserBalance();
    }

    public abstract class Presenter extends BasePresenter<View, Model> {

        public abstract void initPlayVideo();

        public abstract void pkMatchDetail(String matchId);

        public abstract void pkMatchGoOn(String matchId, String id);

        public abstract void pkMatchOver(String matchId);

        //更新当前用户余额
        public abstract void userBalance();


        //机器控制
        public abstract void machineStart();

        public abstract void machineLeft();

        public abstract void machineRight();

        public abstract void machineUp();

        public abstract void machineDown();

        public abstract void machineGo();

        public abstract void machineSwitch(android.view.View view);

        //聊天室相关
        public abstract void joinChatRoom(String chatRoomId);

        public abstract void quitChatRoom(String chatRoomId);

        public abstract void getChatRoomInfo(String chatRoomId);
    }

    public interface Model extends BaseModel {

        Observable<PKMatchDetail> pkMatchDetail(String matchId);

        Observable<String> pkMatchGoOn(String matchId, String id);

        Observable<String> pkMatchOver(String matchId);


        Observable<Map<String, Integer>> userBalance();

    }


}