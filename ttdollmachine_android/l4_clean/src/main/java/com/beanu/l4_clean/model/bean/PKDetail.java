package com.beanu.l4_clean.model.bean;

/**
 * PK详情
 * Created by jrl on 2017/11/21.
 */
public class PKDetail {

    private String battleRecordId;//对战记录id
    private String matchId;//对战ID
    private String startTime;//开始时间
    private int result;//1发起方获胜 2挑战者获胜
    private int selfNum;//发起者抓的次数
    private int oppNum;//挑战者抓的次数
    private String nickName;//发起者用户名称
    private String icon;//发起者用户头像
    private String challengeName;//挑战者用户名称
    private String challengeIcon;//挑战者用户头像
    private String dollName;//娃娃名称
    private String imageCover;//娃娃图片
    private String currency;//获得开心币数量

    public String getBattleRecordId() {
        return battleRecordId;
    }

    public void setBattleRecordId(String battleRecordId) {
        this.battleRecordId = battleRecordId;
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getSelfNum() {
        return selfNum;
    }

    public void setSelfNum(int selfNum) {
        this.selfNum = selfNum;
    }

    public int getOppNum() {
        return oppNum;
    }

    public void setOppNum(int oppNum) {
        this.oppNum = oppNum;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getChallengeName() {
        return challengeName;
    }

    public void setChallengeName(String challengeName) {
        this.challengeName = challengeName;
    }

    public String getChallengeIcon() {
        return challengeIcon;
    }

    public void setChallengeIcon(String challengeIcon) {
        this.challengeIcon = challengeIcon;
    }

    public String getDollName() {
        return dollName;
    }

    public void setDollName(String dollName) {
        this.dollName = dollName;
    }

    public String getImageCover() {
        return imageCover;
    }

    public void setImageCover(String imageCover) {
        this.imageCover = imageCover;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}