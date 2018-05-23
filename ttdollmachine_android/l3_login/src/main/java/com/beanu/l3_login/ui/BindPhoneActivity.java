package com.beanu.l3_login.ui;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.beanu.arad.Arad;
import com.beanu.arad.http.RxHelper;
import com.beanu.arad.utils.ToastUtils;
import com.beanu.l3_common.base.BaseTTActivity;
import com.beanu.l3_common.model.api.API;
import com.beanu.l3_common.model.bean.EventModel;
import com.beanu.l3_common.model.bean.User;
import com.beanu.l3_common.util.AppHolder;
import com.beanu.l3_common.util.Constants;
import com.beanu.l3_login.R;
import com.beanu.l3_login.model.APILoginService;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * 绑定手机号
 */
public class BindPhoneActivity extends BaseTTActivity implements View.OnClickListener {

    EditText mEditLoginPhone;
    EditText mEditLoginPassword;
    Button mBtnLoginLogin;
    TextView mBtnRegisterSend;


    private TimeCount timeCount;//60s倒计时

    private String loginType;
    private String token;
    private int sex;
    private String header;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_phone);

        loginType = getIntent().getStringExtra("type");
        token = getIntent().getStringExtra("token");
        sex = getIntent().getIntExtra("sex", 0);
        header = getIntent().getStringExtra("header");
        name = getIntent().getStringExtra("name");

        timeCount = new TimeCount(60000, 1000);

        mEditLoginPhone = findViewById(R.id.edit_login_phone);
        mEditLoginPassword = findViewById(R.id.edit_login_password);
        mBtnLoginLogin = findViewById(R.id.btn_login_login);
        mBtnRegisterSend = findViewById(R.id.btn_register_send);

        mBtnLoginLogin.setOnClickListener(this);
        mBtnRegisterSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        int i = view.getId();
        if (i == R.id.btn_login_login) {
            String phone = mEditLoginPhone.getText().toString();
            String password = mEditLoginPassword.getText().toString();

            if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(password)) {
                showProgress();
                login(phone, password, loginType, token, sex, header, name);
            }


        } else if (i == R.id.btn_register_send) {
            String mPhoneNum = mEditLoginPhone.getText().toString();
            if (mPhoneNum.length() >= 11) {
//                phone = mPhoneNum;
                bindPhone(mPhoneNum);
                timeCount.start();
            } else {
                ToastUtils.showShort("手机号不正确");
            }


        }
    }


    private void bindPhone(String phoneNum) {

        API.getInstance(APILoginService.class).smsVCode(phoneNum, "0")
                .compose(RxHelper.<String>handleResult())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mRxManage.add(d);
                    }

                    @Override
                    public void onNext(@NonNull String smsCode) {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }


    private void login(String phone, String password, final String type, final String token, int sex, String header, String name) {
        API.getInstance(APILoginService.class).user_login(phone, password, type, token, sex, header, name)
                .compose(RxHelper.<User>handleResult()).subscribe(new Observer<User>() {
            @Override
            public void onSubscribe(Disposable d) {
                mRxManage.add(d);
            }

            @Override
            public void onNext(User user) {
                hideProgress();

                AppHolder.getInstance().setUser(user);

                Arad.preferences.putString(Constants.P_LOGIN_TYPE, loginType);
                Arad.preferences.putString(Constants.P_User_Id, user.getId());
                Arad.preferences.putBoolean(Constants.P_ISFIRSTLOAD, false);
                Arad.preferences.putString(Constants.P_LOGIN_OPENID, token);
                Arad.preferences.flush();

                //通知登录成功
                Arad.bus.post(new EventModel.LoginEvent(user));
                ARouter.getInstance().build("/app/main").navigation();
                finish();
            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.showShort(e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public String setupToolBarTitle() {
        return "绑定手机号";
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

    //倒计时
    private class TimeCount extends CountDownTimer {
        TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        //开始倒计时
        @Override
        public void onTick(long millisUntilFinished) {
            mBtnRegisterSend.setText("重新发送(" + millisUntilFinished / 1000 + "s)");
            mBtnRegisterSend.setClickable(false);
        }

        //倒计时执行完毕
        @Override
        public void onFinish() {
            mBtnRegisterSend.setText("重新发送");
            mBtnRegisterSend.setClickable(true);
        }
    }
}
