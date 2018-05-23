package com.beanu.l4_clean.mvp.contract;

import android.opengl.GLSurfaceView;
import android.view.SurfaceView;

import com.beanu.arad.base.BaseModel;
import com.beanu.arad.base.BasePresenter;
import com.beanu.arad.base.BaseView;
import com.beanu.l4_clean.model.bean.HelpMeCraw;
import com.beanu.l4_clean.model.bean.HelpUser;
import com.beanu.l4_clean.model.bean.OnlinePeople;
import com.beanu.l4_clean.model.bean.SeizeResult;
import com.qiniu.pili.droid.rtcstreaming.RTCVideoWindow;
import com.qiniu.pili.droid.streaming.widget.AspectFrameLayout;

import java.util.List;

import io.reactivex.Observable;

/**
 * 主播直播
 * Created by Beanu on 2017/12/1.
 */

public interface AnchorLivingContract {

    public interface View extends BaseView {
        public void refreshOnlinePeople(List<OnlinePeople> onlinePeopleList, int amount);

        public void uiHelpMeList(List<HelpMeCraw> crawList);

        public void uiAcceptCraw(HelpUser helpUser);

        public void refreshSeize(boolean seize);

        public void actionMachineGO();

        public void activityFinish();

        public void mediaLoading(boolean loading);

        public RTCVideoWindow createRTCVideoWindow();

        public GLSurfaceView findSurfaceView();

        public AspectFrameLayout findAspectLayout();

        public SurfaceView findLocalPreview();


    }

    public abstract class Presenter extends BasePresenter<View, Model> {

        //初始化主播摄像头等
        public abstract void initRTCMedia();

        //获取帮我抓列表
        public abstract void helpMeList();

        public abstract void anchorHelpCraw(String id);

        //抢占机器
        public abstract void startSeize();

        //机器控制
        public abstract void machineStart();

        public abstract void machineLeft();

        public abstract void machineRight();

        public abstract void machineUp();

        public abstract void machineDown();

        public abstract void machineGo();

        public abstract void machineSwitch(android.view.View view);


        //聊天室相关
        public abstract void joinChatRoom();

        public abstract void quitChatRoom();

        public abstract void getChatRoomInfo();

        public abstract void exitLive();
    }

    public interface Model extends BaseModel {

        public Observable<SeizeResult> startSeize(String dollMaId);

        public Observable<Object> exitLive(String roomId, String logId);

        public Observable<List<HelpMeCraw>> helpMeList(String logId);

        public Observable<HelpUser> anchorAcceptCraw(String id);
    }

}