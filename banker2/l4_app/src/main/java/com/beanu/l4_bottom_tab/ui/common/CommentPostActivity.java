package com.beanu.l4_bottom_tab.ui.common;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.beanu.arad.http.RxHelper;
import com.beanu.arad.utils.ToastUtils;
import com.beanu.l3_common.model.api.API;
import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.base.BaseSDActivity;
import com.beanu.l4_bottom_tab.model.ApiService;
import com.beanu.l4_bottom_tab.model.bean.OrderShop;
import com.beanu.l4_bottom_tab.util.Subscriber;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 发布评论
 */
public class CommentPostActivity extends BaseSDActivity {

    @BindView(R.id.img_comment) ImageView mImgComment;
    @BindView(R.id.txt_name) TextView mTxtName;
    @BindView(R.id.txt_title) TextView mTxtTitle;
    @BindView(R.id.txt_date) TextView mTxtDate;
    @BindView(R.id.ratingBar) RatingBar mRatingBar;
    @BindView(R.id.edit_content) EditText mEditContent;

    private OrderShop mOrderShop;
    private int type;//0 直播课 1网课  2图书 3 老师

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_post);
        ButterKnife.bind(this);

        mOrderShop = getIntent().getParcelableExtra("shop");
        type = getIntent().getIntExtra("type", 0);

        if (mOrderShop != null) {
            switch (type) {
                case 0:
                    mTxtDate.setText(mOrderShop.getTimeSlot());
                    break;
                case 1:
                    mTxtDate.setText(mOrderShop.getBrief());
                    break;
                case 2:
                    mTxtDate.setText(mOrderShop.getPress());
                    break;
                case 3:
                    mTxtName.setVisibility(View.VISIBLE);
                    mTxtName.setText(mOrderShop.getTeacher());
                    break;
            }

            if (!TextUtils.isEmpty(mOrderShop.getCoverAPP())) {
                Glide.with(this).load(mOrderShop.getCoverAPP()).into(mImgComment);
            }
            mTxtTitle.setText(mOrderShop.getName());

        }
    }


    @OnClick(R.id.btn_comment)
    public void onViewClicked() {
        int rate = (int) mRatingBar.getRating();
        String content = mEditContent.getText().toString();

        showProgress();
        switch (type) {
            case 0:
            case 1:
                API.getInstance(ApiService.class).postComment(API.createHeader(), mOrderShop.getOrderProId(), content, rate)
                        .compose(RxHelper.<String>handleResult())
                        .subscribe(new Subscriber<String>() {
                            @Override
                            public void onCompleted() {
                                hideProgress();
                                finish();
                            }

                            @Override
                            public void onError(Throwable e) {
                                hideProgress();
                            }

                            @Override
                            public void onNext(String aVoid) {
                                ToastUtils.showShort("发布评论成功");
                            }
                        });
                break;
            case 2:
                API.getInstance(ApiService.class).postBookComment(API.createHeader(), mOrderShop.getOrderProId(), content, rate)
                        .compose(RxHelper.<String>handleResult())
                        .subscribe(new Subscriber<String>() {
                            @Override
                            public void onCompleted() {
                                hideProgress();
                                finish();
                            }

                            @Override
                            public void onError(Throwable e) {
                                hideProgress();
                            }

                            @Override
                            public void onNext(String aVoid) {
                                ToastUtils.showShort("发布评论成功");
                            }
                        });
                break;

            case 3:
                API.getInstance(ApiService.class).postTeacherComment(API.createHeader(), mOrderShop.getOrderProId(), content, rate)
                        .compose(RxHelper.<String>handleResult())
                        .subscribe(new Subscriber<String>() {
                            @Override
                            public void onCompleted() {
                                hideProgress();
                                finish();
                            }

                            @Override
                            public void onError(Throwable e) {
                                hideProgress();
                            }

                            @Override
                            public void onNext(String aVoid) {
                                ToastUtils.showShort("发布评论成功");
                            }
                        });
                break;
        }

    }


    @Override
    public String setupToolBarTitle() {
        return "发表评论";
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
