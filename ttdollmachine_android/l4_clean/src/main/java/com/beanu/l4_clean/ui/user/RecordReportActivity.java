package com.beanu.l4_clean.ui.user;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.beanu.arad.utils.ToastUtils;
import com.beanu.l3_common.base.BaseTTActivity;
import com.beanu.l4_clean.R;
import com.beanu.l4_clean.model.bean.CrawlRecord;
import com.beanu.l4_clean.mvp.contract.RecordReportContract;
import com.beanu.l4_clean.mvp.model.RecordReportModelImpl;
import com.beanu.l4_clean.mvp.presenter.RecordReportPresenterImpl;
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

/**
 * 抓娃娃记录 问题上报
 */
public class RecordReportActivity extends BaseTTActivity<RecordReportPresenterImpl, RecordReportModelImpl> implements RecordReportContract.View {

    public static final int REQUEST_CODE = 1000;

    @BindView(R.id.img_doll) ImageView mImgDoll;
    @BindView(R.id.txt_title) TextView mTxtTitle;
    @BindView(R.id.txt_time) TextView mTxtTime;
    @BindView(R.id.txt_number) TextView mTxtNumber;
    @BindView(R.id.txt_status) TextView mTxtStatus;
    @BindView(R.id.txt_reason) TextView mTxtReason;
    @BindView(R.id.edit_intro) EditText mEditIntro;
    @BindView(R.id.img_pz) ImageView mImgPz;
    @BindView(R.id.button) Button mButton;

    CrawlRecord mCrawlRecord;
    private String imgPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_report);
        ButterKnife.bind(this);

        mCrawlRecord = getIntent().getParcelableExtra("bean");

        if (mCrawlRecord != null) {
            if (!TextUtils.isEmpty(mCrawlRecord.getImage_cover())) {
                Glide.with(this).load(mCrawlRecord.getImage_cover()).into(mImgDoll);
            }
            mTxtTitle.setText(mCrawlRecord.getName());
            mTxtTime.setText(mCrawlRecord.getCreatetime());

            if (mCrawlRecord.getIsSucceed() == 0) {
                mTxtStatus.setText("抓取状态:失败");
            } else {
                mTxtStatus.setText("抓取状态:成功");
            }
            mTxtNumber.setText("游戏编号：" + mCrawlRecord.getCode());
        }

    }

    @OnClick({R.id.img_pz, R.id.button, R.id.txt_reason})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_pz:
                showImageSelector();

                break;
            case R.id.txt_reason:
                showChooseDialog();
                break;
            case R.id.button:

                String content = mEditIntro.getText().toString();
                String intro = mTxtReason.getText().toString();

                if (!TextUtils.isEmpty(content)) {
                    Map<String, String> params = new HashMap<>();
                    params.put("logId", mCrawlRecord.getId());
                    params.put("code", mCrawlRecord.getCode());
                    params.put("content", intro);
                    params.put("explainContent", content);
                    params.put("videoUrl", mPresenter.getQn_path());
                    mPresenter.pushReport(params);

                } else {
                    ToastUtils.showShort("请选择你的问题");
                }

                break;
        }
    }

    @Override
    public void reportSuccess() {
        ToastUtils.showShort("申述成功，等待客服处理");
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
                Glide.with(this).load(imgPath).into(mImgPz);
                //上传到服务器
                mPresenter.uploadImage(imgPath);
            }
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


    String[] sexArry = new String[]{"游戏中工作人员补货", "摄像头故障", "娃娃机抓手故障", "重复扣币", "娃娃掉进洞口却显示未抓中", "抓取娃娃成功但背包中没有记录", "游戏中掉线", "其他（务必填写申述说明）"};//性别选择

    private void showChooseDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);// 自定义对话框
        builder.setSingleChoiceItems(sexArry, 0, new DialogInterface.OnClickListener() {// 2默认的选中

            @Override
            public void onClick(DialogInterface dialog, int which) {// which是被选中的位置
                //showToast(which+"");
                mTxtReason.setText(sexArry[which]);
                dialog.dismiss();//随便点击一个item消失对话框，不用点击确认取消
            }
        });
        builder.show();// 让弹出框显示
    }


    @Override
    public String setupToolBarTitle() {
        return "问题反馈";
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