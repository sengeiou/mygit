package com.beanu.l3_login.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.beanu.arad.base.ToolBarActivity;
import com.beanu.arad.support.log.KLog;
import com.beanu.l2_imageselector.GlideLoader;
import com.beanu.l3_login.R;
import com.beanu.l3_login.mvp.contract.RegisterContract;
import com.beanu.l3_login.mvp.model.RegisterModelImpl;
import com.beanu.l3_login.mvp.presenter.RegisterPresenterImpl;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yancy.imageselector.ImageConfig;
import com.yancy.imageselector.ImageSelector;
import com.yancy.imageselector.ImageSelectorActivity;

import java.util.List;


/**
 * 注册第三步,设置头像和昵称
 */
public class Register3Activity extends ToolBarActivity<RegisterPresenterImpl, RegisterModelImpl> implements View.OnClickListener, RegisterContract.View {

    ImageView mImgRegisterAvatar;
    EditText mEditRegisterNickname;
    Button mBtnRegisterComplete;

    private String phone, password, yzm, nickname;
    private String imgPath;

    public static final int REQUEST_CODE = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register3);

        mImgRegisterAvatar = (ImageView) findViewById(R.id.img_register_avatar);
        mEditRegisterNickname = (EditText) findViewById(R.id.edit_register_nickname);
        mBtnRegisterComplete = (Button) findViewById(R.id.btn_register_complete);

        mImgRegisterAvatar.setOnClickListener(this);
        mBtnRegisterComplete.setOnClickListener(this);

        phone = getIntent().getStringExtra("phone");
        password = getIntent().getStringExtra("password");
        yzm = getIntent().getStringExtra("yzm");

        mEditRegisterNickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    mBtnRegisterComplete.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.img_register_avatar) {
            showImageSelector();

        } else if (i == R.id.btn_register_complete) {
            nickname = mEditRegisterNickname.getText().toString();

            mPresenter.register(phone, password, yzm, nickname);


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 图片选择结果回调
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
            if (pathList != null && pathList.size() > 0) {
                imgPath = pathList.get(0);
                Glide.with(this).load(imgPath).apply(new RequestOptions().circleCrop()).into(mImgRegisterAvatar);
                //上传到服务器
                mPresenter.uploadAvatar(imgPath);
            }
        }

    }


    @Override
    public String setupToolBarTitle() {
        return "设置信息";
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


    //打开图片选择器
    private void showImageSelector() {

        ImageConfig imageConfig
                = new ImageConfig.Builder(
                // GlideLoader 可用自己用的缓存库
                new GlideLoader())
                // 如果在 4.4 以上，则修改状态栏颜色 （默认黑色）
                .steepToolBarColor(getResources().getColor(R.color.blue))
                // 标题的背景颜色 （默认黑色）
                .titleBgColor(getResources().getColor(R.color.blue))
                // 提交按钮字体的颜色  （默认白色）
                .titleSubmitTextColor(getResources().getColor(R.color.white))
                // 标题颜色 （默认白色）
                .titleTextColor(getResources().getColor(R.color.white))
                // 开启多选   （默认为多选）  (单选 为 singleSelect)
                .singleSelect()
                .crop(1, 1, 200, 200)
                // (截图默认配置：关闭    比例 1：1    输出分辨率  500*500)
//                        .crop(1, 2, 500, 1000)
                // 拍照后存放的图片路径（默认 /temp/picture）
                .filePath("/ImageSelector/Pictures")
                // 开启拍照功能 （默认开启）
                .showCamera()
                .requestCode(REQUEST_CODE)
                .build();

        ImageSelector.open(Register3Activity.this, imageConfig);   // 开启图片选择器
    }


    @Override
    public void registerSuccess() {
        //注册成功
        KLog.d("注册成功3");
    }

    @Override
    public void registerFail(String msg) {
        //注册失败
    }

    @Override
    public void obtainSMS(String smsCode) {
        // do nothing
    }

}
