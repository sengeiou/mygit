package com.beanu.l4_bottom_tab.model.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 顶部banner  适合复用
 * Created by Beanu on 2017/2/27.
 */
public class Banner {

    private List<BannerItem> mItemList;

    public List<BannerItem> getItemList() {
        return mItemList == null ? new ArrayList<BannerItem>() : mItemList;
    }

    public void setItemList(List<BannerItem> itemList) {
        mItemList = itemList;
    }
}