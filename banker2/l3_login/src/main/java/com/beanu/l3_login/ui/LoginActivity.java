package com.beanu.l3_login.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.beanu.arad.base.ToolBarActivity;
import com.beanu.l3_login.R;
import com.beanu.l3_login.SignIn2Activity;

public class LoginActivity extends ToolBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

//    @Override
//    protected void setStatusBar() {
//        StatusBarUtil.setTransparentForImageView(this, null);
//
//        //设置toolbar的低版本的高度
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
//            ViewGroup.LayoutParams layoutParams = getToolbar().getLayoutParams();
//            layoutParams.height = getResources().getDimensionPixelSize(R.dimen.toolbar);
//            getToolbar().setLayoutParams(layoutParams);
//        }
//    }

    @Override
    public String setupToolBarTitle() {
        return "登录";
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
    public void onBackPressed() {
        Intent intent = new Intent(LoginActivity.this, SignIn2Activity.class);
        startActivity(intent);
        super.onBackPressed();
    }
}
