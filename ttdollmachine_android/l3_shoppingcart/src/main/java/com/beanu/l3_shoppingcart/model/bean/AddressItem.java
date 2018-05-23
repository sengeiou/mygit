package com.beanu.l3_shoppingcart.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 地址 信息
 * Created by Beanu on 2017/5/11.
 */

public class AddressItem implements Parcelable {

    private String addressId;//地址ID
    private String linkName;//联系人姓名
    private String linkPhone;//联系人电话
    private String address;//详细地址
    private String provinceId;//省份ID
    private String provinceName;//省份名称
    private String cityId;//城市ID
    private String cityName;//城市名称
    private String countyId;//区县ID
    private String countyName;//区县名称
//    private int is_default;//是否是默认选项  0  不是   1 是


    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public String getLinkPhone() {
        return linkPhone;
    }

    public void setLinkPhone(String linkPhone) {
        this.linkPhone = linkPhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountyId() {
        return countyId;
    }

    public void setCountyId(String countyId) {
        this.countyId = countyId;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.addressId);
        dest.writeString(this.linkName);
        dest.writeString(this.linkPhone);
        dest.writeString(this.address);
        dest.writeString(this.provinceId);
        dest.writeString(this.provinceName);
        dest.writeString(this.cityId);
        dest.writeString(this.cityName);
        dest.writeString(this.countyId);
        dest.writeString(this.countyName);
    }

    public AddressItem() {
    }

    protected AddressItem(Parcel in) {
        this.addressId = in.readString();
        this.linkName = in.readString();
        this.linkPhone = in.readString();
        this.address = in.readString();
        this.provinceId = in.readString();
        this.provinceName = in.readString();
        this.cityId = in.readString();
        this.cityName = in.readString();
        this.countyId = in.readString();
        this.countyName = in.readString();
    }

    public static final Parcelable.Creator<AddressItem> CREATOR = new Parcelable.Creator<AddressItem>() {
        @Override
        public AddressItem createFromParcel(Parcel source) {
            return new AddressItem(source);
        }

        @Override
        public AddressItem[] newArray(int size) {
            return new AddressItem[size];
        }
    };
}
