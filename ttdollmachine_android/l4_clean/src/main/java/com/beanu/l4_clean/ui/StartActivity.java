package com.beanu.l4_clean.ui;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.beanu.arad.Arad;
import com.beanu.arad.http.RxHelper;
import com.beanu.l3_common.model.PageModel;
import com.beanu.l3_common.model.api.API;
import com.beanu.l3_common.model.bean.GlobalConfig;
import com.beanu.l3_common.model.bean.SiteClass;
import com.beanu.l3_common.model.bean.User;
import com.beanu.l3_common.util.AppHolder;
import com.beanu.l3_common.util.Constants;
import com.beanu.l3_guide.common.GuideActivity;
import com.beanu.l3_login.ui.LoginActivity;
import com.beanu.l4_clean.R;
import com.beanu.l4_clean.model.APIService;
import com.bumptech.glide.Glide;
import com.tendcloud.tenddata.TCAgent;
import com.tendcloud.tenddata.TDAccount;

import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;


/**
 * 启动页
 */
public class StartActivity extends AppCompatActivity {

    private View mContentView;
    private ImageView mImgAd;
    private Button mBtnJump;

    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //设置状态栏透明状态
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        //设置状态栏透明状态
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        setContentView(R.layout.activity_start);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();


        //查找View
        mContentView = findViewById(R.id.fullscreen_content);
        mImgAd = findViewById(R.id.img_splash_ad);
        mBtnJump = findViewById(R.id.btn_jump_over);

        //执行行为
        Observable.merge(tryLogin(), initConfig(), playLogoAnim(), initSite())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                        if (!TextUtils.isEmpty(AppHolder.getInstance().mConfig.getAdImg())) {
                            Glide.with(StartActivity.this).load(AppHolder.getInstance().mConfig.getAdImg()).into(mImgAd);
                            mBtnJump.setVisibility(View.VISIBLE);
                            startCountDown(3000);
                        } else {
                            mImgAd.setVisibility(View.GONE);
                            gotoNextPage();
                        }

                    }
                });


    }

    // logo动画
    private Observable<Object> playLogoAnim() {
        return Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(final ObservableEmitter<Object> e) throws Exception {

                ObjectAnimator anim = ObjectAnimator.ofFloat(mContentView, "alpha", 0, 0);
                anim.setDuration(2000);
                anim.start();
                anim.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        e.onComplete();
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });

                e.onNext(new Object());
            }
        });

    }

    //进入下一页
    private void gotoNextPage() {
        boolean isFirst = Arad.preferences.getBoolean(Constants.P_ISFIRSTLOAD, true);
        //TODO 删除这句话
        isFirst = false;
        if (!isFirst) {

            if (AppHolder.getInstance().user.isLogin()) {
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(intent);
            }


        } else {
            Intent intent = new Intent(StartActivity.this, GuideActivity.class);
            startActivity(intent);
        }

        finish();
        overridePendingTransition(R.anim.fade, R.anim.hold);
    }

    private void startCountDown(long millisInFuture) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(millisInFuture + 500, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mBtnJump.setText(String.format(Locale.CHINA, "%ds跳过", millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                mBtnJump.setText(R.string.jump_over_0s);
                gotoNextPage();
            }
        }.start();
    }

    //自动登录
    private Observable<Object> tryLogin() {

        String userId = Arad.preferences.getString(Constants.P_User_Id);
//        String password = Arad.preferences.getString(Constants.P_PWD);
//        String loginType = Arad.preferences.getString(Constants.P_LOGIN_TYPE, "0");
//        String loginOpenId = Arad.preferences.getString(Constants.P_LOGIN_OPENID);

//        if ("0".equals(loginType)) {//密码登录
//            password = Base64Util.getStringFromBase64(password);
//        }

        AppHolder.getInstance().user.setId(userId);

        return API.getInstance(APIService.class).loginWhitId()
                .compose(RxHelper.<User>handleResult())
                .map(new Function<User, Object>() {
                    @Override
                    public Object apply(User user) throws Exception {
                        AppHolder.getInstance().setUser(user);
                        TCAgent.onLogin(user.getId(), TDAccount.AccountType.REGISTERED, user.getNickname());

                        return AppHolder.getInstance().user;
                    }
                }).onErrorReturnItem(AppHolder.getInstance().user);
    }


    //获取自动配置
    private Observable<Object> initConfig() {
        return API.getInstance(APIService.class).getConfig()
                .compose(RxHelper.<GlobalConfig>handleResult())
                .map(new Function<GlobalConfig, Object>() {
                    @Override
                    public Object apply(GlobalConfig globalConfig) throws Exception {
                        AppHolder.getInstance().setConfig(globalConfig);
                        return AppHolder.getInstance().mConfig;
                    }
                }).onErrorReturnItem(AppHolder.getInstance().mConfig);
    }

    //获得初始化的分类
    private Observable<Object> initSite() {
        return API.getInstance(APIService.class).siteClassList()
                .compose(RxHelper.<PageModel<SiteClass>>handleResult())
                .map(new Function<PageModel<SiteClass>, Object>() {
                    @Override
                    public Object apply(PageModel<SiteClass> list) throws Exception {
                        AppHolder.getInstance().mSiteClassList.clear();
                        AppHolder.getInstance().addSiteClass(list.getDataList());
                        return AppHolder.getInstance().mSiteClassList;
                    }
                }).onErrorReturnItem(AppHolder.getInstance().mSiteClassList);
    }

}