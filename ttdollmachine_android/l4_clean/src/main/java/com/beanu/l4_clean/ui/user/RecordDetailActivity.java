package com.beanu.l4_clean.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beanu.l3_common.base.BaseTTActivity;
import com.beanu.l4_clean.R;
import com.beanu.l4_clean.model.bean.CrawlRecord;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 抓取详情
 */
public class RecordDetailActivity extends BaseTTActivity {

    @BindView(R.id.img_doll) ImageView mImgDoll;
    @BindView(R.id.txt_title) TextView mTxtTitle;
    @BindView(R.id.txt_time) TextView mTxtTime;
    @BindView(R.id.img_status) ImageView mImgStatus;
    @BindView(R.id.txt_feedback) TextView mTxtFeedback;
    @BindView(R.id.txt_number) TextView mTxtNumber;
    CrawlRecord crawlRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_detail);
        ButterKnife.bind(this);

        crawlRecord = getIntent().getParcelableExtra("bean");

        if (crawlRecord != null) {
            if (!TextUtils.isEmpty(crawlRecord.getImage_cover())) {
                Glide.with(this).load(crawlRecord.getImage_cover()).into(mImgDoll);
            }
            mTxtTitle.setText(crawlRecord.getName());
            mTxtTime.setText(crawlRecord.getCreatetime());

            if (crawlRecord.getIsSucceed() == 0) {
                mImgStatus.setImageResource(R.drawable.zhua_lose_);
            } else {
                mImgStatus.setImageResource(R.drawable.zhua_success_);
            }
            mTxtNumber.setText(crawlRecord.getCode());
        }

        mTxtFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecordDetailActivity.this, RecordReportActivity.class);
                intent.putExtra("bean", crawlRecord);
                startActivity(intent);
            }
        });
    }


    @Override
    public String setupToolBarTitle() {
        return "抓取详情";
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