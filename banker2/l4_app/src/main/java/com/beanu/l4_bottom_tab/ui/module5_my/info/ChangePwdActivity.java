package com.beanu.l4_bottom_tab.ui.module5_my.info;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.beanu.arad.Arad;
import com.beanu.arad.http.RxHelper;
import com.beanu.arad.utils.ToastUtils;
import com.beanu.l3_common.model.api.API;
import com.beanu.l3_common.util.AppHolder;
import com.beanu.l3_common.util.Base64Util;
import com.beanu.l3_common.util.Constants;
import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.base.BaseSDActivity;
import com.beanu.l4_bottom_tab.model.ApiService;
import com.beanu.l4_bottom_tab.util.Subscriber;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 修改密码
 */
public class ChangePwdActivity extends BaseSDActivity implements TextWatcher {

    @BindView(R.id.edit_pwd) EditText mEditPwd;
    @BindView(R.id.edit_pwd_new) EditText mEditPwdNew;
    @BindView(R.id.edit_pwd_again) EditText mEditPwdAgain;
    @BindView(R.id.btn_register_next) Button mBtnRegisterNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);
        ButterKnife.bind(this);

        mEditPwd.addTextChangedListener(this);
        mEditPwdNew.addTextChangedListener(this);
        mEditPwdAgain.addTextChangedListener(this);
    }

    @OnClick(R.id.btn_register_next)
    public void onViewClicked() {

        String pwd_old = mEditPwd.getText().toString();
        final String pwd_new = mEditPwdNew.getText().toString();
        final String pwd_again = mEditPwdAgain.getText().toString();

        if (pwd_new.equals(pwd_again)) {
            API.getInstance(ApiService.class).change_pwd(API.createHeader(), AppHolder.getInstance().user.getId(), pwd_old, pwd_new)
                    .compose(RxHelper.<String>handleResult())
                    .subscribe(new Subscriber<String>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(String s) {

                            Arad.preferences.putString(Constants.P_PWD, Base64Util.getStringFromBase64(pwd_new));
                            Arad.preferences.flush();

                            ToastUtils.showShort("密码修改成功");

                            finish();
                        }
                    });
        }

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String pwd_old = mEditPwd.getText().toString();
        String pwd_new = mEditPwdNew.getText().toString();

        if (!TextUtils.isEmpty(pwd_old) && !TextUtils.isEmpty(pwd_new)) {
            mBtnRegisterNext.setEnabled(true);
        } else {
            mBtnRegisterNext.setEnabled(false);
        }

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }


    @Override
    public String setupToolBarTitle() {
        return "修改密码";
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
