package com.beanu.l3_login;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.beanu.arad.Arad;
import com.beanu.arad.base.ToolBarActivity;
import com.beanu.arad.utils.ToastUtils;
import com.beanu.arad.utils.statusbar.ImmersionBar;
import com.beanu.l2_shareutil.LoginUtil;
import com.beanu.l2_shareutil.login.LoginListener;
import com.beanu.l2_shareutil.login.LoginPlatform;
import com.beanu.l2_shareutil.login.LoginResult;
import com.beanu.l3_common.util.Constants;
import com.beanu.l3_login.mvp.contract.LoginContract;
import com.beanu.l3_login.mvp.model.LoginModelImpl;
import com.beanu.l3_login.mvp.presenter.LoginPresenterImpl;
import com.beanu.l3_login.ui.LoginActivity;

@Route(path = "/login/signIn2")
public class SignIn2Activity extends ToolBarActivity<LoginPresenterImpl, LoginModelImpl> implements LoginContract.View {


    //第三方登录监听
    final LoginListener mLoginListener = new LoginListener() {
        @Override
        public void loginSuccess(LoginResult result) {
            //登录成功， 如果你选择了获取用户信息，可以通过

            String loginType = "0";
            if (result.getPlatform() == LoginPlatform.QQ) {
                loginType = "1";
            } else if (result.getPlatform() == LoginPlatform.WX) {
                loginType = "2";
            }
            hideProgress();
            Arad.preferences.putString(Constants.P_LOGIN_OPENID, result.getToken().getOpenid());
            Arad.preferences.flush();

            mPresenter.login(null, null, loginType, result.getToken().getOpenid(), result.getUserInfo().getSex(), result.getUserInfo().getHeadImageUrl(), result.getUserInfo().getNickname());
            showProgress();
        }

        @Override
        public void loginFailure(Exception e) {
            Log.i("TAG", "登录失败");

            hideProgress();
        }

        @Override
        public void loginCancel() {

            hideProgress();
            Log.i("TAG", "登录取消");
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        setContentView(R.layout.activity_sign_in2);
    }

    @Override
    protected void setStatusBar() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.transparentStatusBar().init();
    }

    public void onClickWeChat(View view) {

        showProgress();
        LoginUtil.login(this, LoginPlatform.WX, mLoginListener, true);

    }

    public void onClickPhone(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void loginSuccess() {
        hideProgress();
        gotoMain();
        finish();
    }

    @Override
    public void loginFailed(String error) {
        hideProgress();
        ToastUtils.showShort(error);
    }

    @Override
    public void obtainSMS(String smsCode) {

    }

    private void gotoMain() {
        ARouter.getInstance().build("/app/main").navigation();
    }
}