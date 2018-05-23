package com.beanu.l4_clean.mvp.contract;

import android.widget.ImageView;

import com.beanu.arad.base.BaseModel;
import com.beanu.arad.base.BasePresenter;
import com.beanu.arad.base.BaseView;
import com.beanu.l4_clean.model.bean.DollsMachine;
import com.beanu.l4_clean.model.bean.OnlinePeople;
import com.beanu.l4_clean.model.bean.SeizeResult;
import com.pili.pldroid.player.widget.PLVideoTextureView;
import com.qiniu.pili.droid.rtcstreaming.RTCVideoWindow;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

/**
 * 抓娃娃 mvp
 * Created by Beanu on 2017/11/18.
 */

public interface GameContract {

    public interface View extends BaseView {
        void initUI(DollsMachine dollsMachine);

        void refreshOnlinePeople(List<OnlinePeople> onlinePeopleList, int amount);

        void refreshSeize(SeizeResult seize);

        void uiUserBalance();

        void uiGameResult(boolean win);

        void activityFinish();

        RTCVideoWindow createRTCVideoWindow();

        RTCVideoWindow createRTCVideoWindowB();

        PLVideoTextureView findPLVideo();

        ImageView findLoadingView();

        ImageView findImgCapture();

        void conferenceLoading(boolean loading);

        void showRechargeDialog();

        void conferenceStart();

        void conferenceSuccess();

        void conferenceFaild();

        void returnView();

    }

    public abstract class Presenter extends BasePresenter<View, Model> {

        //初始化主播摄像头等
        public abstract void initRTCMedia();

        public abstract void initMachineDetail(String id);

        //抢占机器
        public abstract void startSeize(String dollMaId);

        public abstract void beginGame();

        //更新当前用户余额
        public abstract void userBalance();

        //主动请求，询问结果
        public abstract void gameResult(String id);

        //分享游戏加分
        public abstract void shareGame(String gameId);


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

        public abstract void selfSendMessage(String message);
    }

    public interface Model extends BaseModel {
        Observable<DollsMachine> requestMachineDetail(String id);

        Observable<SeizeResult> startSeize(String dollMaId);

        Observable<Map<String, Integer>> userBalance();

        Observable<Map<String, String>> gameResult(String id);

        Observable<Map<String, String>> shareGame(String id);
    }


}