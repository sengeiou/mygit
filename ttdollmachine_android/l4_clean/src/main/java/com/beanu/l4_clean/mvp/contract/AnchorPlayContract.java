package com.beanu.l4_clean.mvp.contract;

import com.beanu.arad.base.BaseModel;
import com.beanu.arad.base.BasePresenter;
import com.beanu.arad.base.BaseView;
import com.beanu.l4_clean.model.bean.LiveRoom2Audience;
import com.beanu.l4_clean.model.bean.OnlinePeople;

import java.util.List;

import io.reactivex.Observable;

/**
 * 主播 播放
 * Created by Beanu on 2017/12/4.
 */

public interface AnchorPlayContract {

    public interface View extends BaseView {
        public void initUI(LiveRoom2Audience liveRoom);

        public void refreshOnlinePeople(List<OnlinePeople> onlinePeopleList, int amount);

        public void uiHelpMe(boolean success);

    }

    public abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void audienceLiveRoom(String roomId, String logId);

        public abstract void anchorHelpMe(String logId);

        //聊天室相关
        public abstract void joinChatRoom(String chatRoomId);

        public abstract void quitChatRoom(String chatRoomId);

        public abstract void getChatRoomInfo(String chatRoomId);

        public abstract void selfSendMessage(String message);

    }

    public interface Model extends BaseModel {

        Observable<LiveRoom2Audience> audienceLiveRoom(String roomId, String logId);

        Observable<Object> anchorHelpMe(String logId);

    }


}