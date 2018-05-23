package com.beanu.l4_clean.ui.user;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.beanu.arad.http.RxHelper;
import com.beanu.arad.utils.ToastUtils;
import com.beanu.l3_common.model.api.API;
import com.beanu.l3_common.util.AppHolder;
import com.beanu.l4_clean.R;
import com.beanu.l3_common.base.BaseTTActivity;
import com.beanu.l4_clean.model.APIService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 绑定邀请码
 */
public class BindInviteActivity extends BaseTTActivity {

    @BindView(R.id.edit_input) EditText mEditInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_invite);
        ButterKnife.bind(this);
    }


    private void userBind(final String code) {
        showProgress();
        API.getInstance(APIService.class).userBinding(code).compose(RxHelper.handleResult())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mRxManage.add(d);
                    }

                    @Override
                    public void onNext(Object o) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        AppHolder.getInstance().user.setRecommend_code(code);
                        hideProgress();

                        ToastUtils.showShort("绑定成功");
                        finish();
                    }
                });
    }

    @OnClick(R.id.btn_submit)
    public void onViewClicked() {

        String code = mEditInput.getText().toString();
        if (!TextUtils.isEmpty(code)) {
            userBind(code);
        } else {
            ToastUtils.showShort("邀请码不能为空");
        }

    }


    @Override
    public String setupToolBarTitle() {
        return "邀请码";
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