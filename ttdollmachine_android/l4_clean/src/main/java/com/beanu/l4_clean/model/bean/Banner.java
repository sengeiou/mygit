package com.beanu.l4_clean.model.bean;

import java.util.List;

/**
 * Created by Beanu on 2017/12/5.
 */
public class Banner {

    private List<BannerItem> mItemList;

    public List<BannerItem> getItemList() {
        return mItemList;
    }

    public void setItemList(List<BannerItem> itemList) {
        mItemList = itemList;
    }
}