package com.beanu.sf;

import android.content.Context;
import android.widget.ImageView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.beanu.arad.AradApplication;
import com.beanu.arad.AradApplicationConfig;
import com.beanu.l2_shareutil.ShareConfig;
import com.beanu.l2_shareutil.ShareManager;
import com.bumptech.glide.Glide;
import com.yuyh.library.imgsel.ISNav;
import com.yuyh.library.imgsel.common.ImageLoader;

/**
 *
 * Created by Administrator on 2017/1/17.
 */

public class MyApplication extends AradApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        String QQ_ID = "1105143764";
        String WX_ID = "wxfc9207d5bd5c0c53";
        String WEIBO_ID = "645439975";

        String WX_SECRET = "99b535f8e17282f247e8eae4ba57df1b";
        String REDIRECT_URL = "http://sns.whalecloud.com/sina2/callback";

        ShareConfig config = ShareConfig.instance()
                .qqId(QQ_ID)
                .wxId(WX_ID)
                .weiboId(WEIBO_ID)
                // 下面两个，如果不需要登录功能，可不填写
                .weiboRedirectUrl(REDIRECT_URL)
                .debug(true)
                .wxSecret(WX_SECRET);

        ShareManager.init(config);

        ARouter.init(this);

        ISNav.getInstance().init(new ImageLoader() {
            @Override
            public void displayImage(Context context, String path, ImageView imageView) {
                Glide.with(context).load(path).into(imageView);
            }
        });
    }

    @Override
    protected AradApplicationConfig appConfig() {
        return new AradApplicationConfig();
    }
}
