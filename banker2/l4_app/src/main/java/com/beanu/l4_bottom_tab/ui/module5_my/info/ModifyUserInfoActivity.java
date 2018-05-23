package com.beanu.l4_bottom_tab.ui.module5_my.info;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.beanu.arad.utils.ToastUtils;
import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.base.BaseSDActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 修改个人信息
 */
public class ModifyUserInfoActivity extends BaseSDActivity {

    @BindView(R.id.edit_content) EditText mEditContent;
    String type = "0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_user_info);
        ButterKnife.bind(this);

        String name = getIntent().getExtras().getString("name");
        type = getIntent().getExtras().getString("type");

        mEditContent.setText(name);
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
        return "1".equals(type) ? "修改学校" : "修改昵称";
    }

    @OnClick({R.id.img_close, R.id.btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_close:
                mEditContent.setText("");
                break;
            case R.id.btn_save:
                String name = mEditContent.getText().toString();
                if (!TextUtils.isEmpty(name)) {
                    Intent intent = new Intent();
                    intent.putExtra("name", name);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    ToastUtils.showShort("信息不能为空");
                }
                break;
        }
    }
}
