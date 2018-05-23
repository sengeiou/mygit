package com.beanu.l4_clean.ui.user;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.beanu.arad.base.ToolBarActivity;
import com.beanu.arad.utils.ToastUtils;
import com.beanu.l4_clean.R;
import com.beanu.l4_clean.mvp.contract.FeedbackContract;
import com.beanu.l4_clean.mvp.model.FeedbackModelImpl;
import com.beanu.l4_clean.mvp.presenter.FeedbackPresenterImpl;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yuyh.library.imgsel.ISNav;
import com.yuyh.library.imgsel.config.ISListConfig;
import com.yuyh.library.imgsel.ui.ISListActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FeedbackActivity extends ToolBarActivity<FeedbackPresenterImpl, FeedbackModelImpl>
        implements TextWatcher, FeedbackContract.View {

    @BindView(R.id.edit_content)
    EditText mEditContent;
    @BindView(R.id.add_image)
    ImageView mAddImage;
    @BindView(R.id.text_wechat)
    TextView mTextWechat;
    @BindView(R.id.text_size)
    TextView mTextSize;

    public static final int REQUEST_CODE = 1000;

    private String imgPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);

        mEditContent.addTextChangedListener(this);
    }

    @Override
    public String setupToolBarTitle() {
        return "意见反馈";
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


    private void commit() {

        String content = mEditContent.getText().toString();
        if (!TextUtils.isEmpty(content)) {
            mPresenter.pushFeedback(content);
        } else {
            ToastUtils.showShort("请填写你的意见");
        }
    }


    @Override
    public void requestSuccess() {
        ToastUtils.showShort("感谢您的宝贵意见");
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 图片选择结果回调
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra(ISListActivity.INTENT_RESULT);
            if (pathList != null && pathList.size() > 0) {
                imgPath = pathList.get(0);
                Glide.with(this).load(imgPath).apply(RequestOptions.circleCropTransform()).into(mAddImage);
                //上传到服务器
                mPresenter.uploadImage(imgPath);
            }
        }

    }


    @OnClick(R.id.add_image)
    public void onViewClicked() {
        showImageSelector();
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
                .statusBarColor(Color.parseColor("#3F51B5"))
                // 标题
                .title("图片")
                // 标题文字颜色
                .titleColor(Color.WHITE)
                // TitleBar背景色
                .titleBgColor(Color.parseColor("#3F51B5"))
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
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (TextUtils.isEmpty(s)) {
            mTextSize.setText("200字以内");
        } else {
            int currentLength = s.length();
            mTextSize.setText(currentLength + "/200");
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @OnClick(R.id.btn_submit)
    public void onButtonClicked() {
        commit();
    }
}
