package com.beanu.l4_clean.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 收货地址
 */

public class Address implements Parcelable {
    public String name;
    public String phone;
    public String city;
    public String address;

    public Address(String name, String phone, String city, String address) {
        this.name = name;
        this.phone = phone;
        this.city = city;
        this.address = address;
    }

    protected Address(Parcel in) {
        name = in.readString();
        phone = in.readString();
        city = in.readString();
        address = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(city);
        dest.writeString(address);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Address> CREATOR = new Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };
}
