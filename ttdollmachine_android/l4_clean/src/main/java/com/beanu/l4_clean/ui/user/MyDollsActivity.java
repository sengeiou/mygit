package com.beanu.l4_clean.ui.user;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.beanu.l4_clean.R;
import com.beanu.l3_common.base.BaseTTActivity;
import com.beanu.l4_clean.util.FragmentSwitcher;

/**
 * 我的娃娃
 */
public class MyDollsActivity extends BaseTTActivity {

    private FragmentSwitcher mSwitcher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_dolls);

        mSwitcher = new FragmentSwitcher(R.id.container, getSupportFragmentManager());
        mSwitcher.addFragment(DollsWaitFragment.class.getName(), DollsWaitFragment.class)
                .addFragment(DeliverFragment.class.getName(), DeliverFragment.class);
        mSwitcher.switchFragment(DollsWaitFragment.class.getName());
    }

    public FragmentSwitcher getSwitcher() {
        return mSwitcher;
    }


    @Override
    public String setupToolBarTitle() {
        return "我的娃娃";
    }

    @Override
    public boolean setupToolBarRightButton1(View rightButton1) {
        ((ImageView) rightButton1).setImageResource(R.drawable.kefu_white);
        rightButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = "mqqwpa://im/chat?chat_type=wpa&uin=2484740137";
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

            }
        });
        return false;
    }

    @Override
    public boolean setupToolBarLeftButton(View leftButton) {
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        return true;
    }


}