package com.beanu.l4_clean.model.bean;

/**
 * 账单列表
 * Created by Beanu on 2017/11/10.
 */

public class BillItem {

    private String logId;//记录id
    private String createtime;//记录时间
    private String type;//类型 0 充值 1抓娃娃消耗 2主播帮我抓消耗 3主播帮我抓退还 4娃娃兑换金币收入 5PK比赛获胜获得 6 主播帮助用户抓娃娃消耗7比赛抓娃娃消费
    private String typeName;//类型名称
    private int coins;//金币数量（扣除为负数 收入为正数）

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }
}