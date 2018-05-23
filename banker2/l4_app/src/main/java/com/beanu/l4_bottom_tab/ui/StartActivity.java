package com.beanu.l4_bottom_tab.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.beanu.arad.Arad;
import com.beanu.arad.http.RxHelper;
import com.beanu.arad.utils.AnimUtil;
import com.beanu.l3_common.model.api.API;
import com.beanu.l3_common.model.bean.User;
import com.beanu.l3_common.util.AppHolder;
import com.beanu.l3_common.util.Base64Util;
import com.beanu.l3_common.util.Constants;
import com.beanu.l3_guide.common.GuideActivity;
import com.beanu.l3_login.SignIn2Activity;
import com.beanu.l3_login.model.ApiLoginService;
import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.util.Subscriber;


/**
 * 启动页
 */
public class StartActivity extends AppCompatActivity {

    private View mContentView;

    private boolean animitorEnd, loginEnd;
    private boolean enadbleAutoLogin = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //设置状态栏透明状态
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.setFlags(
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_start);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        //查找View
        mContentView = findViewById(R.id.fullscreen_content);

        //执行动画行为
        playLogoAnim();


        String loginType = Arad.preferences.getString(Constants.P_LOGIN_TYPE);

        if (TextUtils.isEmpty(loginType)) {
            loginEnd = true;
            enadbleAutoLogin = true;
            gotoNextPage();
        } else {
            enadbleAutoLogin = false;
            //自动登录
            tryLogin();
        }


    }


    // logo动画
    private void playLogoAnim() {
        ObjectAnimator anim = ObjectAnimator.ofFloat(mContentView, "alpha", 0, 1);
        anim.setDuration(2000);
        anim.start();
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                animitorEnd = true;
                gotoNextPage();
            }
        });
    }

    //进入下一页
    private void gotoNextPage() {
        if (animitorEnd && loginEnd) {
            boolean isFirst = Arad.preferences.getBoolean(Constants.P_ISFIRSTLOAD, true);
            if (!isFirst) {

                if (enadbleAutoLogin) {
                    Intent intent = new Intent(StartActivity.this, SignIn2Activity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(StartActivity.this, MainActivity.class);
                    if (getIntent().getBundleExtra(Constants.EXTRA_BUNDLE) != null) {
                        intent.putExtra(Constants.EXTRA_BUNDLE,
                                getIntent().getBundleExtra(Constants.EXTRA_BUNDLE));
                    }
                    startActivity(intent);
                }
            } else {
                Intent intent = new Intent(StartActivity.this, GuideActivity.class);
                startActivity(intent);
            }

            AnimUtil.intentSlidIn(StartActivity.this);
            finish();
        }
    }

    //    业务
    private void tryLogin() {

        String phone = Arad.preferences.getString(Constants.P_ACCOUNT);
        String password = Arad.preferences.getString(Constants.P_PWD);
        String loginType = Arad.preferences.getString(Constants.P_LOGIN_TYPE);
        String loginOpenId = Arad.preferences.getString(Constants.P_LOGIN_OPENID);

        if ("0".equals(loginType)) {
            password = Base64Util.getStringFromBase64(password);
        }

        API.getInstance(ApiLoginService.class).user_login(phone, password, loginType, loginOpenId, 0, null, null)
                .compose(RxHelper.<User>handleResult())
                .subscribe(new Subscriber<User>() {
                    @Override
                    public void onCompleted() {
                        loginEnd = true;
                        gotoNextPage();
                    }

                    @Override
                    public void onError(Throwable e) {
                        loginEnd = true;
                        gotoNextPage();
                    }

                    @Override
                    public void onNext(User user) {
                        AppHolder.getInstance().setUser(user);
                    }
                });

    }
}
