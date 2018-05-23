package com.beanu.l4_clean.ui.pk;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.beanu.arad.base.ToolBarActivity;
import com.beanu.arad.http.RxHelper;
import com.beanu.l3_common.model.api.API;
import com.beanu.l4_clean.R;
import com.beanu.l4_clean.model.APIService;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * PK邀请
 */
public class InvitationActivity extends ToolBarActivity {

    @BindView(R.id.txt_code) TextView mTxtCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation);
        ButterKnife.bind(this);

        invite();
    }


    @Override
    public String setupToolBarTitle() {
        return "发起邀请";
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

    private void invite() {
        API.getInstance(APIService.class).invite2PK().compose(RxHelper.<Map<String, String>>handleResult())
                .subscribe(new Observer<Map<String, String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mRxManage.add(d);
                    }

                    @Override
                    public void onNext(Map<String, String> map) {
                        String code = map.get("code");
                        String shareUrl = map.get("share_url");
                        mTxtCode.setText(code);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
