package com.beanu.l4_clean.ui.user;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.beanu.arad.http.RxHelper;
import com.beanu.arad.utils.ToastUtils;
import com.beanu.l2_shareutil.ShareDialogUtil;
import com.beanu.l2_shareutil.share.ShareListener;
import com.beanu.l3_common.model.api.API;
import com.beanu.l3_common.util.AppHolder;
import com.beanu.l4_clean.R;
import com.beanu.l3_common.base.BaseTTActivity;
import com.beanu.l4_clean.model.APIService;

import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 分享页面
 */
public class ShareActivity extends BaseTTActivity {


    String title, intro, url;
    TextView mTxtCode1, mTxtCode2, mTxtCode3, mTxtCode4, mTxtCode5, mTxtCode6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        mTxtCode1 = findViewById(R.id.txt_code1);
        mTxtCode2 = findViewById(R.id.txt_code2);
        mTxtCode3 = findViewById(R.id.txt_code3);
        mTxtCode4 = findViewById(R.id.txt_code4);
        mTxtCode5 = findViewById(R.id.txt_code5);
        mTxtCode6 = findViewById(R.id.txt_code6);


        String code = AppHolder.getInstance().user.getInvitation_code();
        if (!TextUtils.isEmpty(code) && code.length() == 6) {
            mTxtCode1.setText(String.valueOf(code.charAt(0)));
            mTxtCode2.setText(String.valueOf(code.charAt(1)));
            mTxtCode3.setText(String.valueOf(code.charAt(2)));
            mTxtCode4.setText(String.valueOf(code.charAt(3)));
            mTxtCode5.setText(String.valueOf(code.charAt(4)));
            mTxtCode6.setText(String.valueOf(code.charAt(5)));
        }

        initShareInfo();
    }


    @Override
    public String setupToolBarTitle() {
        return "邀请奖励";
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

    @Override
    public boolean setupToolBarRightButton2(View rightButton2) {
        if (rightButton2 instanceof TextView) {
            ((TextView) rightButton2).setText("输入邀请码");
            ((TextView) rightButton2).setTextColor(getResources().getColor(R.color.white));
            rightButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    startActivity(BindInviteActivity.class);

//                    RecommedBindFragment.show(getSupportFragmentManager(), new RecommedBindFragment.Listener() {
//                        @Override
//                        public void onClickBind(android.support.v4.app.DialogFragment dialogFragment, String code) {
//                            if (!TextUtils.isEmpty(code)) {
//                                userBind(dialogFragment, code);
//                            } else {
//                                ToastUtils.showShort("邀请码不能为空");
//                            }
//                        }
//                    });
                }
            });
        }

        return true;
    }



    private void initShareInfo() {
        API.getInstance(APIService.class).shareUrl().compose(RxHelper.<Map<String, String>>handleResult())
                .subscribe(new Observer<Map<String, String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Map<String, String> map) {
                        title = map.get("title");
                        intro = map.get("intro");
                        url = map.get("APPShare");

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void onClickShare(View view) {
        ShareDialogUtil.showShareDialog(this, title, intro, url + "?id=" + AppHolder.getInstance().user.getId(), R.mipmap.ic_launcher, new ShareListener() {
            @Override
            public void shareSuccess() {

            }

            @Override
            public void shareFailure(Exception e) {
                e.printStackTrace();
                ToastUtils.showShort(e.getLocalizedMessage());
            }

            @Override
            public void shareCancel() {

            }
        });
    }
}