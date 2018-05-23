package com.beanu.l4_bottom_tab.ui.module5_my.about_us;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.beanu.arad.Arad;
import com.beanu.arad.support.updateversion.UpdateChecker;
import com.beanu.arad.utils.ToastUtils;
import com.beanu.arad.utils.UIUtils;
import com.beanu.l2_shareutil.ShareDialogUtil;
import com.beanu.l2_shareutil.share.ShareListener;
import com.beanu.l3_common.util.AppHolder;
import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.base.BaseSDActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 关于我们
 */
public class AboutUsActivity extends BaseSDActivity {


    private  String mVersionName;
    @butterknife.BindView(R.id.textView4)
    android.widget.TextView textView4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ButterKnife.bind(this);

        textView4.setText("版本V" + getmVersionName());
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

    @Override
    public String setupToolBarTitle() {
        return "关于我们";
    }

    @OnClick({R.id.rl_update, R.id.rl_wechat, R.id.rl_link_us, R.id.rl_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_update:


                if (!TextUtils.isEmpty(AppHolder.getInstance().mVersion.getApkversion())) {
                    try {
                        int version = Integer.valueOf(AppHolder.getInstance().mVersion.getApkversion());

                        if (version > Arad.app.deviceInfo.getVersionCode()) {
                            UpdateChecker.checkForDialog(AboutUsActivity.this, AppHolder.getInstance().mVersion.getDetail(), AppHolder.getInstance().mVersion.getApkurl(), version);

                        } else {
                            ToastUtils.showShort("当前是最新版");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    ToastUtils.showShort("当前是最新版");
                }

                break;
            case R.id.rl_wechat:

                break;
            case R.id.rl_link_us:
                UIUtils.dial(this, "4008756006");
                break;
            case R.id.rl_share:
                ShareDialogUtil.showShareDialog(this, "时代教育", "成就金融职业之路，打开黄金就业之门", "http://yinhangren.cn/download.html", R.mipmap.ic_launcher, new ShareListener() {
                    @Override
                    public void shareSuccess() {

                    }

                    @Override
                    public void shareFailure(Exception e) {
                        e.printStackTrace();
                        ToastUtils.showShort(e.getLocalizedMessage());

                    }

                    @Override
                    public void shareCancel() {

                    }
                });
                break;
        }
    }


    //获取版本号
    private String getmVersionName() {

        android.content.pm.PackageManager packageManager = getPackageManager();
        try {
            android.content.pm.PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            mVersionName = packageInfo.versionName;

        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return mVersionName;
    }



}
