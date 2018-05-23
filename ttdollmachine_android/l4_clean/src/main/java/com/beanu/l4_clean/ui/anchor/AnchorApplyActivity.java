package com.beanu.l4_clean.ui.anchor;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.beanu.arad.base.ToolBarActivity;
import com.beanu.arad.utils.ToastUtils;
import com.beanu.l4_clean.R;
import com.beanu.l4_clean.mvp.contract.AnchorApplyContract;
import com.beanu.l4_clean.mvp.model.AnchorApplyModelImpl;
import com.beanu.l4_clean.mvp.presenter.AnchorApplyPresenterImpl;
import com.bumptech.glide.Glide;
import com.yuyh.library.imgsel.ISNav;
import com.yuyh.library.imgsel.config.ISListConfig;
import com.yuyh.library.imgsel.ui.ISListActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/***
 * 申请成为主播
 */
public class AnchorApplyActivity extends ToolBarActivity<AnchorApplyPresenterImpl, AnchorApplyModelImpl> implements AnchorApplyContract.View {

    @BindView(R.id.edit_name) EditText mEditName;
    @BindView(R.id.radio_group_sex) RadioGroup mRadioGroupSex;
    @BindView(R.id.edit_phone) EditText mEditPhone;
    @BindView(R.id.edit_id) EditText mEditId;
    @BindView(R.id.edit_profession) EditText mEditProfession;
    @BindView(R.id.img_cover) ImageView mImgCover;
    @BindView(R.id.img_card_front) ImageView mImgCardFront;
    @BindView(R.id.img_card_back) ImageView mImgCardBack;

    private static final int REQUEST_CODE_C = 1000;
    private static final int REQUEST_CODE_F = 1001;
    private static final int REQUEST_CODE_B = 1002;

    private String coverPath, cardFrontPath, cardBackPath;
    private boolean coverUpdateImg, cardFImg, cardBImg;//是否需要上传图片
    private String sexTag = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anchor_apply);
        ButterKnife.bind(this);

        mRadioGroupSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.radio_sex_boy) {
                    sexTag = "0";
                } else if (i == R.id.radio_sex_girl) {
                    sexTag = "1";
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra(ISListActivity.INTENT_RESULT);

            if (pathList != null && pathList.size() > 0) {

                if (requestCode == REQUEST_CODE_C) {
                    coverPath = pathList.get(0);
                    coverUpdateImg = true;
                    Glide.with(this).load(coverPath).into(mImgCover);

                } else if (requestCode == REQUEST_CODE_F) {
                    cardFrontPath = pathList.get(0);
                    cardFImg = true;

                    Glide.with(this).load(cardFrontPath).into(mImgCardFront);
                } else if (requestCode == REQUEST_CODE_B) {
                    cardBackPath = pathList.get(0);
                    cardBImg = true;
                    Glide.with(this).load(cardBackPath).into(mImgCardBack);
                }

            }


        }
    }

    public void onPickPicture(int requestCode) {

        int aspectX = 1;
        int aspectY = 1;
        int outputX = 600;
        int outputY = 600;

        if (requestCode != REQUEST_CODE_C) {
            aspectX = 3;
            aspectY = 2;
            outputX = 900;
            outputY = 600;
        }

        ISListConfig config = new ISListConfig.Builder()
                // 是否多选, 默认true
                .multiSelect(false)
                // 是否记住上次选中记录, 仅当multiSelect为true的时候配置，默认为true
                .rememberSelected(false)
                // “确定”按钮背景色
                .btnBgColor(Color.GRAY)
                // “确定”按钮文字颜色
                .btnTextColor(Color.BLUE)
                // 使用沉浸式状态栏
                .statusBarColor(getResources().getColor(R.color.colorPrimary))
                // 标题
                .title("图片")
                // 标题文字颜色
                .titleColor(Color.WHITE)
                // TitleBar背景色
                .titleBgColor(getResources().getColor(R.color.colorPrimary))
                // 裁剪大小。needCrop为true的时候配置
                .cropSize(aspectX, aspectY, outputX, outputY)
                .needCrop(true)
                // 第一个是否显示相机，默认true
                .needCamera(true)
                .build();

        // 跳转到图片选择器
        ISNav.getInstance().toListActivity(this, config, requestCode);
    }

    @Override
    public String setupToolBarTitle() {
        return "填写资料";
    }


    @Override
    public boolean setupToolBarRightButton2(View rightButton1) {
        if (rightButton1 instanceof TextView) {
            ((TextView) rightButton1).setText("提交");
            rightButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String name = mEditName.getText().toString();
                    String sex = sexTag;
                    String phone = mEditPhone.getText().toString();
                    String card = mEditId.getText().toString();
                    String profession = mEditProfession.getText().toString();

                    if (TextUtils.isEmpty(name) || TextUtils.isEmpty(sex) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(card) || TextUtils.isEmpty(profession) || TextUtils.isEmpty(coverPath) || TextUtils.isEmpty(cardFrontPath) || TextUtils.isEmpty(cardBackPath)) {
                        ToastUtils.showShort("信息填写不全");
                        return;
                    }
                    Map<String, String> params = new HashMap<>();
                    params.put("name", name);
                    params.put("sex", sex);
                    params.put("phone", phone);
                    params.put("cardNum", card);

                    params.put("birthday", "");

                    params.put("occupation", profession);

                    mPresenter.applyAnchor(params, coverPath, cardFrontPath, cardBackPath);
                }
            });
        }
        return true;
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

    @OnClick({R.id.img_cover, R.id.img_card_front, R.id.img_card_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_cover:
                onPickPicture(REQUEST_CODE_C);
                break;
            case R.id.img_card_front:
                onPickPicture(REQUEST_CODE_F);

                break;
            case R.id.img_card_back:
                onPickPicture(REQUEST_CODE_B);
                break;
        }
    }

    @Override
    public void uiApplyAnchor(boolean success) {
        if (success) {
            ToastUtils.showShort("申请成功，请等待审核结果");
            finish();
        } else {
            ToastUtils.showShort("上传失败，请再次提交审核");

        }
    }
}
