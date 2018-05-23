package com.beanu.l3_login.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.beanu.arad.base.ToolBarActivity;
import com.beanu.l3_login.R;
import com.beanu.l3_login.SignIn2Activity;

public class RegisterActivity extends ToolBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    @Override
    public String setupToolBarTitle() {
        return "注册";
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
        Intent intent = new Intent(RegisterActivity.this, SignIn2Activity.class);
        startActivity(intent);
        super.onBackPressed();
    }
}
