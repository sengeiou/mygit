package com.beanu.l4_bottom_tab.ui.module5_my;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;

import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.base.BaseSDActivity;
import com.beanu.l4_bottom_tab.util.AndroidBug5497Workaround;
import com.qiyukf.unicorn.api.ConsultSource;
import com.qiyukf.unicorn.api.Unicorn;
import com.qiyukf.unicorn.ui.fragment.ServiceMessageFragment;

/**
 * 客服
 */
public class ServicesActivity extends BaseSDActivity {

    String title;
    private AndroidBug5497Workaround mAndroidBug5497Workaround;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        mAndroidBug5497Workaround = AndroidBug5497Workaround.assistActivity(this);

        title = "时代客服";
        ConsultSource source = new ConsultSource("APP", "银行人客户端", "我是客服小时");

        if (Unicorn.isServiceAvailable()) {
            ServiceMessageFragment fragment = Unicorn.newServiceFragment(title, source, (LinearLayout) findViewById(R.id.viewGroup_menu));
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.replace(R.id.frame_layout, fragment);
            try {
                transaction.commitAllowingStateLoss();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAndroidBug5497Workaround.release();
    }

    @Override
    public String setupToolBarTitle() {
        return title;
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
