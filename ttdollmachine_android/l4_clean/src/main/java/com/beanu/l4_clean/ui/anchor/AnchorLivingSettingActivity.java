package com.beanu.l4_clean.ui.anchor;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.beanu.arad.base.ToolBarActivity;
import com.beanu.arad.support.log.KLog;
import com.beanu.arad.support.recyclerview.divider.VerticalDividerItemDecoration;
import com.beanu.arad.utils.ToastUtils;
import com.beanu.l3_common.model.bean.User;
import com.beanu.l3_common.util.AppHolder;
import com.beanu.l4_clean.R;
import com.beanu.l4_clean.adapter.DollsMachineForAnchorAdapter;
import com.beanu.l4_clean.model.bean.Dolls;
import com.beanu.l4_clean.model.bean.LiveRoom;
import com.beanu.l4_clean.mvp.contract.AnchorLivingSettingContract;
import com.beanu.l4_clean.mvp.model.AnchorLivingSettingModelImpl;
import com.beanu.l4_clean.mvp.presenter.AnchorLivingSettingPresenterImpl;
import com.beanu.l4_clean.support.droid.StreamUtils;
import com.bumptech.glide.Glide;
import com.yuyh.library.imgsel.ISNav;
import com.yuyh.library.imgsel.config.ISListConfig;
import com.yuyh.library.imgsel.ui.ISListActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 主播开播 开始设置页面
 */
public class AnchorLivingSettingActivity extends ToolBarActivity<AnchorLivingSettingPresenterImpl, AnchorLivingSettingModelImpl> implements AnchorLivingSettingContract.View {

    @BindView(R.id.camera_preview) SurfaceView mCameraPreview;
    @BindView(R.id.img_cover) ImageView mImgCover;
    @BindView(R.id.txt_desc) EditText mTxtDesc;
    @BindView(R.id.btn_start) Button mBtnStart;
    @BindView(R.id.recycle_view) RecyclerView mRecyclerView;

    private static final int REQUEST_CODE = 1000;
    private String headPath;
    private boolean updateImg;//是否需要上传图片

    //空闲娃娃机
    DollsMachineForAnchorAdapter mDollsMachineForPKAdapter;
    List<Dolls> mDollsList;

    private Camera camera;
    private boolean isPreview;
    private SurfaceHolder.Callback mSurfaceCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            try {

                Camera.CameraInfo facing_front_info = null;
                int numberOfCameras = Camera.getNumberOfCameras();
                for (int i = 0; i < numberOfCameras; i++) {
                    Camera.CameraInfo info = new Camera.CameraInfo();
                    Camera.getCameraInfo(i, info);
                    if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                        camera = Camera.open(i);
                        facing_front_info = info;
                        break;
                    }
                }

                //设置角度
                setCameraDisplayOrientation(AnchorLivingSettingActivity.this, facing_front_info, camera);
                camera.setPreviewDisplay(surfaceHolder);//通过SurfaceView显示取景画面
                camera.startPreview();//开始预览
                isPreview = true;//设置是否预览参数为真
            } catch (IOException e) {
                KLog.e(e.toString());
            }

        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            if (camera != null) {
                if (isPreview) {//正在预览
                    camera.stopPreview();
                    camera.release();
                }
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anchor_living_setting);
        ButterKnife.bind(this);

        SurfaceHolder mSurfaceHolder = mCameraPreview.getHolder();
        mSurfaceHolder.setFormat(PixelFormat.TRANSPARENT);
        mSurfaceHolder.addCallback(mSurfaceCallback);


        //先显示之前的信息
        User user = AppHolder.getInstance().user;
//        if (!TextUtils.isEmpty(user.getCover())) {
//            Glide.with(this).load(user.getCover()).into(mImgCover);
//            headPath = user.getCover();
//            updateImg = false;
//        }
//        if (!TextUtils.isEmpty(user.getTitle())) {
//            mTxtDesc.setText(user.getTitle());
//        }


        //初始化空闲娃娃
        mDollsList = new ArrayList<>();
        mDollsMachineForPKAdapter = new DollsMachineForAnchorAdapter(this, mDollsList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mDollsMachineForPKAdapter);
        mRecyclerView.addItemDecoration(new VerticalDividerItemDecoration.Builder(this).colorResId(android.R.color.transparent).sizeResId(R.dimen.grid_space).build());
    }

    @OnClick(R.id.btn_start)
    public void onViewClicked() {
        String desc = mTxtDesc.getText().toString();
        String machineId = null;
        if (mDollsMachineForPKAdapter.getSelectedPos() >= 0) {
            int position = mDollsMachineForPKAdapter.getSelectedPos();
            machineId = mDollsList.get(position).getId();
        }

        if (!TextUtils.isEmpty(headPath) && !TextUtils.isEmpty(desc) && !TextUtils.isEmpty(machineId)) {
            mPresenter.startLiving(headPath, desc, machineId, updateImg);
        } else {
            ToastUtils.showShort("信息填写不完整");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra(ISListActivity.INTENT_RESULT);

            if (pathList != null && pathList.size() > 0) {
                headPath = pathList.get(0);
                Glide.with(this).load(headPath).into(mImgCover);
                updateImg = true;

            }


        }
    }

    /**
     * 设置 摄像头的角度
     *
     * @param activity 上下文
     * @param camera   摄像头对象
     */
    public static void setCameraDisplayOrientation(Activity activity,
                                                   Camera.CameraInfo cameraInfo, android.hardware.Camera camera) {

        if (cameraInfo != null) {
            int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
            //获取摄像头当前的角度
            int degrees = 0;
            switch (rotation) {
                case Surface.ROTATION_0:
                    degrees = 0;
                    break;
                case Surface.ROTATION_90:
                    degrees = 90;
                    break;
                case Surface.ROTATION_180:
                    degrees = 180;
                    break;
                case Surface.ROTATION_270:
                    degrees = 270;
                    break;
            }

            int result;
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                //前置摄像头
                result = (cameraInfo.orientation + degrees) % 360;
                result = (360 - result) % 360; // compensate the mirror
            } else {
                // back-facing  后置摄像头
                result = (cameraInfo.orientation - degrees + 360) % 360;
            }
            camera.setDisplayOrientation(result);
        }
    }


    @Override
    public void uiRefresh(LiveRoom liveRoom) {
        Intent intent = new Intent(this, AnchorLivingActivity.class);
        intent.putExtra("role", StreamUtils.RTC_ROLE_ANCHOR);
        intent.putExtra("room", liveRoom);
        startActivity(intent);
        finish();
    }

    @Override
    public void uiFailure() {
        ToastUtils.showShort("娃娃选取失败，请重新选择");
    }

    @Override
    public void uiDollsList(List<Dolls> dollsList) {

        List<Dolls> list = new ArrayList<>();
        for (Dolls dolls : dollsList) {
            if (dolls.getGame_status() == 0) {
                list.add(dolls);
            }
        }

        mDollsList.addAll(list);
        mDollsMachineForPKAdapter.notifyDataSetChanged();
    }

    public void onClickUpload(View view) {
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
}
