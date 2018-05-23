package com.beanu.l4_clean.model.bean;

/**
 * 优惠券
 * Created by Beanu on 2018/2/23.
 */

public class Coupon {

    private String id;// 免费卡ID
    private String expire_time;//到期日期(yyyy-MM-dd) 如果为永久有效则到期日期为 2100-12-12
    private int is_us;//是否使用 0否 1是
    private String us_time;//使用时间

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExpire_time() {
        return expire_time;
    }

    public void setExpire_time(String expire_time) {
        this.expire_time = expire_time;
    }

    public int getIs_us() {
        return is_us;
    }

    public void setIs_us(int is_us) {
        this.is_us = is_us;
    }

    public String getUs_time() {
        return us_time;
    }

    public void setUs_time(String us_time) {
        this.us_time = us_time;
    }
}