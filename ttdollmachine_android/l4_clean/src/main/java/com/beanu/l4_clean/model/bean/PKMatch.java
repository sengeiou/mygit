package com.beanu.l4_clean.model.bean;

/**
 * pk比赛的信息
 * Created by Beanu on 2017/11/25.
 */

public class PKMatch {

    private String code;//比赛邀请码
    private String id;//比赛ID
    private int isHave;//是否有已经发起的比赛 0否 1是
    private String opp_users_icon;//挑战者头像
    private String opp_users_nickName;//挑战者昵称
    private String opponent_users_id;//挑战者ID
    private int match_status;//比赛状态 0未开始（未有用户加入） 1已有用户加入，未开始 2 发起者已准备 3挑战者已准备 4进行中 5结束 6发起者取消
    private boolean isInChatRoom;//挑战者是否在聊天室中

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIsHave() {
        return isHave;
    }

    public void setIsHave(int isHave) {
        this.isHave = isHave;
    }

    public String getOpp_users_icon() {
        return opp_users_icon;
    }

    public void setOpp_users_icon(String opp_users_icon) {
        this.opp_users_icon = opp_users_icon;
    }

    public String getOpp_users_nickName() {
        return opp_users_nickName;
    }

    public void setOpp_users_nickName(String opp_users_nickName) {
        this.opp_users_nickName = opp_users_nickName;
    }

    public String getOpponent_users_id() {
        return opponent_users_id;
    }

    public void setOpponent_users_id(String opponent_users_id) {
        this.opponent_users_id = opponent_users_id;
    }

    public int getMatch_status() {
        return match_status;
    }

    public void setMatch_status(int match_status) {
        this.match_status = match_status;
    }

    public boolean isInChatRoom() {
        return isInChatRoom;
    }

    public void setInChatRoom(boolean inChatRoom) {
        isInChatRoom = inChatRoom;
    }
}