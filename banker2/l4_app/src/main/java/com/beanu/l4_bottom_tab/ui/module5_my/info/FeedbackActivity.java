package com.beanu.l4_bottom_tab.ui.module5_my.info;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.beanu.arad.http.RxHelper;
import com.beanu.arad.utils.ToastUtils;
import com.beanu.l3_common.model.api.API;
import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.base.BaseSDActivity;
import com.beanu.l4_bottom_tab.model.ApiService;
import com.beanu.l4_bottom_tab.util.Subscriber;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 意见反馈
 */
public class FeedbackActivity extends BaseSDActivity implements TextWatcher {

    @BindView(R.id.txt_title) TextView mTxtTitle;
    @BindView(R.id.edit_title) EditText mEditTitle;
    @BindView(R.id.txt_content) TextView mTxtContent;
    @BindView(R.id.edit_content) EditText mEditContent;
    @BindView(R.id.btn_send) Button mBtnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);

        mEditTitle.addTextChangedListener(this);
        mEditContent.addTextChangedListener(this);

    }

    @OnClick(R.id.btn_send)
    public void onViewClicked() {
        String title = mEditTitle.getText().toString();
        String content = mEditContent.getText().toString();

        showProgress();
        API.getInstance(ApiService.class).feed_back(API.createHeader(), content, title)
                .compose(RxHelper.<String>handleResult())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        hideProgress();
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        hideProgress();
                    }

                    @Override
                    public void onNext(String s) {
                        ToastUtils.showShort( "提交成功，请等待回复");
                    }
                });
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        String title = mEditTitle.getText().toString();
        String content = mEditContent.getText().toString();

        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(content)) {
            mBtnSend.setEnabled(true);
        } else {
            mBtnSend.setEnabled(false);
        }

        mTxtTitle.setText(title.length() + "/15");
        mTxtContent.setText(content.length() + "/150");

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }


    @Override
    public String setupToolBarTitle() {
        return "意见反馈";
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