package com.beanu.l4_bottom_tab.model.bean;

import me.yokeyword.indexablerv.IndexableEntity;

/**
 * 省份
 * Created by Beanu on 2017/5/3.
 */

public class ProvinceEntity implements IndexableEntity {

    private String id;
    private String name;
    private String pinyin;

    public ProvinceEntity(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String getFieldIndexBy() {
        return name;
    }

    @Override
    public void setFieldIndexBy(String indexField) {
        this.name = indexField;
    }

    @Override
    public void setFieldPinyinIndexBy(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
