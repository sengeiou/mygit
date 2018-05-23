package com.beanu.l4_bottom_tab.ui.module_news;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.adapter.ProvinceAdapter;
import com.beanu.l4_bottom_tab.base.BaseSDActivity;
import com.beanu.l4_bottom_tab.model.bean.ProvinceEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.yokeyword.indexablerv.IndexableAdapter;
import me.yokeyword.indexablerv.IndexableLayout;
import me.yokeyword.indexablerv.SimpleHeaderAdapter;


/**
 * 选取省份
 */
public class PickerProvinceActivity extends BaseSDActivity {

    @BindView(R.id.index_layout) IndexableLayout mIndexLayout;
    ProvinceAdapter mProvinceAdapter;
    private SimpleHeaderAdapter mHeaderAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker_province);
        ButterKnife.bind(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mIndexLayout.setLayoutManager(linearLayoutManager);

        mProvinceAdapter = new ProvinceAdapter();
        mIndexLayout.setAdapter(mProvinceAdapter);

        List<ProvinceEntity> list = new ArrayList<>();
        list.add(new ProvinceEntity("110000", "北京市"));
        list.add(new ProvinceEntity("120000", "天津市"));
        list.add(new ProvinceEntity("130000", "河北省"));
        list.add(new ProvinceEntity("140000", "山西省"));
        list.add(new ProvinceEntity("150000", "内蒙古自治区"));
        list.add(new ProvinceEntity("210000", "辽宁省"));
        list.add(new ProvinceEntity("220000", "吉林省"));
        list.add(new ProvinceEntity("230000", "黑龙江省"));
        list.add(new ProvinceEntity("310000", "上海市"));
        list.add(new ProvinceEntity("320000", "江苏省"));
        list.add(new ProvinceEntity("330000", "浙江省"));
        list.add(new ProvinceEntity("340000", "安徽省"));
        list.add(new ProvinceEntity("350000", "福建省"));
        list.add(new ProvinceEntity("360000", "江西省"));
        list.add(new ProvinceEntity("370000", "山东省"));
        list.add(new ProvinceEntity("410000", "河南省"));
        list.add(new ProvinceEntity("420000", "湖北省"));
        list.add(new ProvinceEntity("430000", "湖南省"));
        list.add(new ProvinceEntity("440000", "广东省"));
        list.add(new ProvinceEntity("450000", "广西壮族自治区"));
        list.add(new ProvinceEntity("460000", "海南省"));
        list.add(new ProvinceEntity("500000", "重庆市"));
        list.add(new ProvinceEntity("510000", "四川省"));
        list.add(new ProvinceEntity("520000", "贵州省"));
        list.add(new ProvinceEntity("530000", "云南省"));
        list.add(new ProvinceEntity("540000", "西藏自治区"));
        list.add(new ProvinceEntity("610000", "陕西省"));
        list.add(new ProvinceEntity("620000", "甘肃省"));
        list.add(new ProvinceEntity("630000", "青海省"));
        list.add(new ProvinceEntity("640000", "宁夏回族自治区"));
        list.add(new ProvinceEntity("650000", "新疆维吾尔自治区"));
        list.add(new ProvinceEntity("710000", "台湾省"));
        list.add(new ProvinceEntity("810000", "香港特别行政区"));
        list.add(new ProvinceEntity("820000", "澳门特别行政区"));

        mProvinceAdapter.setDatas(list);
        mProvinceAdapter.setOnItemContentClickListener(new IndexableAdapter.OnItemContentClickListener<ProvinceEntity>() {
            @Override
            public void onItemClick(View v, int originalPosition, int currentPosition, ProvinceEntity entity) {

                Intent intent = getIntent().putExtra("provinceId", entity.getId());
                intent.putExtra("provinceName", entity.getFieldIndexBy());
                setResult(RESULT_OK, intent);
                finish();

            }
        });

        //头部信息
        List<ProvinceEntity> headerList = new ArrayList<>();
        headerList.add(new ProvinceEntity("", "全国"));

        mHeaderAdapter = new SimpleHeaderAdapter<>(mProvinceAdapter, "↑", "#", headerList);
        mIndexLayout.addHeaderAdapter(mHeaderAdapter);

    }


    @Override
    public String setupToolBarTitle() {
        return "选择省份";
    }

    @Override
    public boolean setupToolBarLeftButton(View leftButton) {
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        return true;
    }
}
