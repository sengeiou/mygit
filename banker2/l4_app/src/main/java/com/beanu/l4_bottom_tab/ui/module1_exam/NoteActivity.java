package com.beanu.l4_bottom_tab.ui.module1_exam;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beanu.arad.support.log.KLog;
import com.beanu.arad.utils.ToastUtils;
import com.beanu.l2_imageselector.GlideLoader;
import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.adapter.PhotoGridAdapter;
import com.beanu.l4_bottom_tab.base.BaseSDActivity;
import com.beanu.l4_bottom_tab.model.bean.ExamNote;
import com.beanu.l4_bottom_tab.model.bean.ExamQuestion;
import com.beanu.l4_bottom_tab.mvp.contract.NoteContract;
import com.beanu.l4_bottom_tab.mvp.model.NoteModelImpl;
import com.beanu.l4_bottom_tab.mvp.presenter.NotePresenterImpl;
import com.beanu.l4_bottom_tab.util.AndroidBug5497Workaround;
import com.beanu.l4_bottom_tab.util.CapturePhotoHelper;
import com.beanu.l4_bottom_tab.util.FolderManager;
import com.yancy.imageselector.ImageConfig;
import com.yancy.imageselector.ImageSelector;
import com.yancy.imageselector.ImageSelectorActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 错题 添加笔记
 */
public class NoteActivity extends BaseSDActivity<NotePresenterImpl, NoteModelImpl> implements NoteContract.View {

    @BindView(R.id.edit_content) EditText mEditContent;
    @BindView(R.id.recycle_view) RecyclerView mRecycleView;
    @BindView(R.id.txt_word_num) TextView mTxtWordNum;
    @BindView(R.id.ll_layout) LinearLayout llContainer;

    private static final int REQUEST_CODE = 1000;
    private final int content_num = 500;

    private PhotoGridAdapter mPhotoGridAdapter;
    private ArrayList<String> mLocalPath = new ArrayList<>();      //本地图片地址
    private ExamQuestion mExamQuestion;

    //拍照
    private final static String EXTRA_RESTORE_PHOTO = "extra_restore_photo";
    private final static int RUNTIME_PERMISSION_REQUEST_CODE = 0x1;

    private CapturePhotoHelper mCapturePhotoHelper;
    private File mRestorePhotoFile;
    private AndroidBug5497Workaround mAndroidBug5497Workaround;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
        mAndroidBug5497Workaround = AndroidBug5497Workaround.assistActivity(this);

        mExamQuestion = getIntent().getParcelableExtra("question");

        mPhotoGridAdapter = new PhotoGridAdapter(this, mLocalPath);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        mRecycleView.setLayoutManager(gridLayoutManager);
        mRecycleView.setAdapter(mPhotoGridAdapter);

        //总数统计
        mEditContent.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private int selectionStart;
            private int selectionEnd;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                temp = charSequence;
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mTxtWordNum.setText(editable.length() + "/" + content_num + "字");
                selectionStart = mEditContent.getSelectionStart();
                selectionEnd = mEditContent.getSelectionEnd();
                if (temp.length() > content_num) {
                    editable.delete(selectionStart - 1, selectionEnd);
                    mEditContent.setText(editable);
                    mEditContent.setSelection(selectionEnd);
                }
            }
        });

        //请求是否有笔记数据
        mPresenter.request_note(mExamQuestion.getId(), mExamQuestion.getSource(), mExamQuestion.getType(), mExamQuestion.getCourseId());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mCapturePhotoHelper != null) {
            mRestorePhotoFile = mCapturePhotoHelper.getPhoto();
            if (mRestorePhotoFile != null) {
                outState.putSerializable(EXTRA_RESTORE_PHOTO, mRestorePhotoFile);
            }
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (mCapturePhotoHelper != null) {
            mRestorePhotoFile = (File) savedInstanceState.getSerializable(EXTRA_RESTORE_PHOTO);
            mCapturePhotoHelper.setPhoto(mRestorePhotoFile);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            List<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);

            for (String path : pathList) {
                Log.i("ImagePathList", path);
            }
            mLocalPath.clear();
            mLocalPath.addAll(pathList);

            List<String> _list = new ArrayList<>();
            _list.addAll(mPresenter.getServicePath());
            _list.addAll(mLocalPath);

            mPhotoGridAdapter.setList(_list);
            mPhotoGridAdapter.notifyDataSetChanged();

            //上传到服务器
            mPresenter.uploadPhotos(mLocalPath);
        }

        if (requestCode == CapturePhotoHelper.CAPTURE_PHOTO_REQUEST_CODE && mCapturePhotoHelper != null) {
            File photoFile = mCapturePhotoHelper.getPhoto();
            if (photoFile != null) {
                if (resultCode == RESULT_OK) {

                    mLocalPath.add(photoFile.getPath());

                    List<String> _list = new ArrayList<>();
                    _list.addAll(mPresenter.getServicePath());
                    _list.addAll(mLocalPath);

                    mPhotoGridAdapter.setList(_list);
                    mPhotoGridAdapter.notifyDataSetChanged();

                    //上传到服务器
                    mPresenter.uploadPhotos(mLocalPath);
                } else {
                    if (photoFile.exists()) {
                        photoFile.delete();
                    }
                }
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAndroidBug5497Workaround.release();
    }

    @OnClick({R.id.toolbar_close, R.id.toolbar_save, R.id.btn_take_photo, R.id.btn_picker_photo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar_close:
                finish();
                break;
            case R.id.toolbar_save:
                mPresenter.updateNote(mExamQuestion.getId(), mExamQuestion.getSource(), mExamQuestion.getType(), mExamQuestion.getCourseId(), mEditContent.getText().toString());
                break;
            case R.id.btn_take_photo:

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //Android M 处理Runtime Permission
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {//检查是否有写入SD卡的授权
                        turnOnCamera();
                    } else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            KLog.i("should show request permission rationale!");
                        }
                        requestPermission();
                    }
                } else {
                    turnOnCamera();
                }

                break;
            case R.id.btn_picker_photo:
                showPickerPhoto();
                break;
        }
    }


    /**
     * 开启相机
     */
    private void turnOnCamera() {
        if (mCapturePhotoHelper == null) {
            mCapturePhotoHelper = new CapturePhotoHelper(this, FolderManager.getPhotoFolder());
        }
        mCapturePhotoHelper.capture();
    }

    /**
     * 申请写入sd卡的权限
     */
    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, RUNTIME_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RUNTIME_PERMISSION_REQUEST_CODE) {
            for (int index = 0; index < permissions.length; index++) {
                String permission = permissions[index];
                if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(permission)) {
                    if (grantResults[index] == PackageManager.PERMISSION_GRANTED) {
                        turnOnCamera();
                    } else {
                        showMissingPermissionDialog();

                    }
                }
            }
        }
    }

    /**
     * 显示打开权限提示的对话框
     */
    private void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.help);
        builder.setMessage(R.string.help_content);

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(NoteActivity.this, R.string.camera_open_error, Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        builder.setPositiveButton(R.string.settings, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                turnOnSettings();
            }
        });

        builder.show();
    }

    /**
     * 启动系统权限设置界面
     */
    private void turnOnSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    private void showPickerPhoto() {
        if (mPresenter.getServicePath().size() < 4) {
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
                    // 多选时的最大数量   （默认 9 张）
                    .mutiSelectMaxSize(4 - mPresenter.getServicePath().size())
                    // 已选择的图片路径
                    .pathList(mLocalPath)
                    // 拍照后存放的图片路径（默认 /temp/picture）
                    .filePath("/ImageSelector/Pictures")
                    // 开启拍照功能 （默认开启）
                    .showCamera()
                    .requestCode(REQUEST_CODE)
                    .build();


            ImageSelector.open(NoteActivity.this, imageConfig);   // 开启图片选择器
        }
    }

    @Override
    public void uploadNoteStatus(boolean success, ExamNote examNote) {
        if (success) {
            ToastUtils.showShort("保存成功");

            Intent resultIntent = new Intent();
            resultIntent.putExtra("note", examNote);
            setResult(RESULT_OK, resultIntent);

            finish();
        } else {
            ToastUtils.showShort("保存失败，请重试");
        }
    }

    @Override
    public void cannotPostNote() {
        ToastUtils.showShort("等待图片上传完，再重试");
    }

    @Override
    public void showNoteContent(ExamNote noteEntry) {
        mEditContent.setText(noteEntry.getContent());
        mPhotoGridAdapter.setList(mPresenter.getServicePath());
        mPhotoGridAdapter.notifyDataSetChanged();
    }
}
