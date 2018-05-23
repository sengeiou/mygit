package com.beanu.l4_clean.ui.user;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beanu.arad.base.ToolBarActivity;
import com.beanu.arad.utils.ToastUtils;
import com.beanu.l3_common.model.bean.User;
import com.beanu.l3_common.util.AppHolder;
import com.beanu.l3_shoppingcart.AddressChooseActivity;
import com.beanu.l4_clean.R;
import com.beanu.l4_clean.mvp.contract.UserInfoContract;
import com.beanu.l4_clean.mvp.model.UserInfoModelImpl;
import com.beanu.l4_clean.mvp.presenter.UserInfoPresenterImpl;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yuyh.library.imgsel.ISNav;
import com.yuyh.library.imgsel.config.ISListConfig;
import com.yuyh.library.imgsel.ui.ISListActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 个人信息
 */
public class UserInfoActivity extends ToolBarActivity<UserInfoPresenterImpl, UserInfoModelImpl> implements UserInfoContract.View {

    public static final int REQUEST_CODE = 1000;
    private static final int REQUEST_CODE_NICKNAME = 1001;
    private static final int REQUEST_CODE_SIGN = 1002;


    @BindView(R.id.img_avatar) ImageView mImgAvatar;
    @BindView(R.id.txt_nickName) TextView mTxtNickName;
    @BindView(R.id.txt_sex) TextView mTxtSign;
    @BindView(R.id.txt_school) TextView mTxtSchool;

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
        mTxtSign.setText(user.getMotto());
//        mTxtSchool.setText(user.getSchool());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 图片选择结果回调
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra(ISListActivity.INTENT_RESULT);
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
        if (requestCode == REQUEST_CODE_SIGN && resultCode == RESULT_OK && data != null) {
            String school = data.getStringExtra("name");
            mTxtSign.setText(school);
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

                Bundle bundle2 = new Bundle();
                bundle2.putString("name", mTxtSchool.getText().toString());
                bundle2.putString("type", "1");
                startActivityForResult(ModifyUserInfoActivity.class, bundle2, REQUEST_CODE_SIGN);

//                OptionPicker optionPicker = new OptionPicker(this, new String[]{"男", "女"});
//                optionPicker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
//                    @Override
//                    public void onOptionPicked(int index, String item) {
//                        switch (index) {
//                            case 0:
//                                mTxtSign.setText("男");
//                                break;
//                            case 1:
//                                mTxtSign.setText("女");
//                                break;
//                        }
//                    }
//                });
//                optionPicker.show();

                break;
            case R.id.rl_item_school:
                //收获地址
                startActivity(AddressChooseActivity.class);

                break;
        }
    }

    //打开图片选择器
    private void showImageSelector() {

        // 自由配置选项
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
                .cropSize(1, 1, 200, 200)
                .needCrop(true)
                // 第一个是否显示相机，默认true
                .needCamera(false)
                .build();

        // 跳转到图片选择器
        ISNav.getInstance().toListActivity(this, config, REQUEST_CODE);
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
    public boolean setupToolBarRightButton2(View rightButton) {
        if (rightButton instanceof TextView) {
            ((TextView) rightButton).setText("保存");
            rightButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String nickName = mTxtNickName.getText().toString();
                    String sign = mTxtSign.getText().toString();
//                    if ("男".equals(sex)) {
//                        sex = "0";
//                    } else {
//                        sex = "1";
//                    }
//                    String school = mTxtSchool.getText().toString();

                    mPresenter.updateUserInfo(mPresenter.getQn_path(), nickName, sign, null);
                }
            });
        }
        return true;
    }

    @Override
    public void updateSuccess() {
        ToastUtils.showShort("更新用户信息成功");

        Intent intent = getIntent();
        if (!TextUtils.isEmpty(mPresenter.getQn_path())) {
            AppHolder.getInstance().user.setIcon(mPresenter.getQn_path());
        }

        String nickName = mTxtNickName.getText().toString();
        AppHolder.getInstance().user.setNickname(nickName);

        String sign = mTxtSign.getText().toString();
        AppHolder.getInstance().user.setMotto(sign);

        setResult(RESULT_OK, intent);
        finish();
    }
}