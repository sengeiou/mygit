package com.beanu.l4_bottom_tab.ui.module5_my.info;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.beanu.arad.utils.ToastUtils;
import com.beanu.l2_imageselector.GlideLoader;
import com.beanu.l3_common.model.bean.User;
import com.beanu.l3_common.util.AppHolder;
import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.base.BaseSDActivity;
import com.beanu.l4_bottom_tab.mvp.contract.UserInfoContract;
import com.beanu.l4_bottom_tab.mvp.model.UserInfoModelImpl;
import com.beanu.l4_bottom_tab.mvp.presenter.UserInfoPresenterImpl;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yancy.imageselector.ImageConfig;
import com.yancy.imageselector.ImageSelector;
import com.yancy.imageselector.ImageSelectorActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.OptionPicker;

/**
 * 个人信息
 */
public class UserInfoActivity extends BaseSDActivity<UserInfoPresenterImpl, UserInfoModelImpl> implements UserInfoContract.View {

    public static final int REQUEST_CODE = 1000;
    private static final int REQUEST_CODE_NICKNAME = 1001;
    private static final int REQUEST_CODE_SCHOOL = 1002;


    @BindView(R.id.img_avatar) ImageView mImgAvatar;
    @BindView(R.id.txt_nickName) TextView mTxtNickName;
    @BindView(R.id.txt_sex) TextView mTxtSex;
    @BindView(R.id.txt_school) TextView mTxtSchool;

    @BindView(R.id.edit_sign) EditText mEditSign;
    private String imgPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);

        User user = AppHolder.getInstance().user;
        if (!TextUtils.isEmpty(user.getIcon())) {
            Glide.with(this).load(user.getIcon()).apply(RequestOptions.circleCropTransform()).into(mImgAvatar);
        }
        mTxtNickName.setText(user.getNickname());
        mEditSign.setText(user.getMotto());
        mTxtSex.setText(user.getSex() == 0 ? "男" : "女");
        mTxtSchool.setText(user.getSchool());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 图片选择结果回调
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
            if (pathList != null && pathList.size() > 0) {
                imgPath = pathList.get(0);
                Glide.with(this).load(imgPath).apply(RequestOptions.circleCropTransform()).into(mImgAvatar);
                //上传到服务器
                mPresenter.uploadPhoto(imgPath);
            }
        }

        //昵称
        if (requestCode == REQUEST_CODE_NICKNAME && resultCode == RESULT_OK && data != null) {
            String name = data.getStringExtra("name");
            mTxtNickName.setText(name);
        }

        //学校
        if (requestCode == REQUEST_CODE_SCHOOL && resultCode == RESULT_OK && data != null) {
            String school = data.getStringExtra("name");
            mTxtSchool.setText(school);
        }

    }

    @OnClick({R.id.rl_item_avatar, R.id.rl_item_nickname, R.id.rl_item_sex, R.id.rl_item_school})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_item_avatar:
                showImageSelector();
                break;
            case R.id.rl_item_nickname:

                Bundle bundle = new Bundle();
                bundle.putString("name", mTxtNickName.getText().toString());
                bundle.putString("type", "0");
                startActivityForResult(ModifyUserInfoActivity.class, bundle, REQUEST_CODE_NICKNAME);
                break;
            case R.id.rl_item_sex:

                OptionPicker optionPicker = new OptionPicker(this, new String[]{"男", "女"});
                optionPicker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
                    @Override
                    public void onOptionPicked(int index, String item) {
                        switch (index) {
                            case 0:
                                mTxtSex.setText("男");
                                break;
                            case 1:
                                mTxtSex.setText("女");
                                break;
                        }
                    }
                });
                optionPicker.show();

                break;
            case R.id.rl_item_school:

                Bundle bundle2 = new Bundle();
                bundle2.putString("name", mTxtSchool.getText().toString());
                bundle2.putString("type", "1");
                startActivityForResult(ModifyUserInfoActivity.class, bundle2, REQUEST_CODE_SCHOOL);

                break;
        }
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
                .singleSelect()
                .crop(1, 1, 200, 200)
                .filePath("/ImageSelector/Pictures")
                .showCamera()
                .requestCode(REQUEST_CODE)
                .build();

        ImageSelector.open(this, imageConfig);   // 开启图片选择器
    }

    @Override
    public String setupToolBarTitle() {
        return "个人信息";
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
    public boolean setupToolBarRightButton1(View rightButton1) {
        return setupToolBarRightButton(rightButton1);
    }

    public boolean setupToolBarRightButton(View rightButton) {
        if (rightButton instanceof TextView) {
            ((TextView) rightButton).setText("保存");
            rightButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String nickName = mTxtNickName.getText().toString();
                    String sign = mEditSign.getText().toString();
                    String sex = mTxtSex.getText().toString();
                    if ("男".equals(sex)) {
                        sex = "0";
                    } else {
                        sex = "1";
                    }
                    String school = mTxtSchool.getText().toString();

                    mPresenter.updateUserInfo(mPresenter.getQn_path(), nickName, sign, sex, school);
                }
            });
        }
        return true;
    }

    @Override
    public void updateSuccess() {
        ToastUtils.showShort("更新用户信息成功");

        Intent intent = getIntent();
        intent.putExtra("icon", mPresenter.getQn_path());
        intent.putExtra("nickName", mTxtNickName.getText().toString());
        intent.putExtra("sign", mEditSign.getText().toString());
        String sex = mTxtSex.getText().toString();
        if ("男".equals(sex)) {
            sex = "0";
        } else {
            sex = "1";
        }
        intent.putExtra("sex", sex);
        intent.putExtra("school", mTxtSchool.getText().toString());

        setResult(RESULT_OK, intent);
        finish();
    }
}