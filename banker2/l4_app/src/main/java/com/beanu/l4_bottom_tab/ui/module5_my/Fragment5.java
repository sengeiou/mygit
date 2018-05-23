package com.beanu.l4_bottom_tab.ui.module5_my;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beanu.arad.base.ToolBarFragment;
import com.beanu.arad.http.RxHelper;
import com.beanu.arad.utils.ToastUtils;
import com.beanu.arad.utils.UIUtils;
import com.beanu.l3_common.model.api.API;
import com.beanu.l3_common.model.bean.User;
import com.beanu.l3_common.util.AppHolder;
import com.beanu.l3_search.SearchActivity;
import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.adapter.provider.ExchangeNetOrder;
import com.beanu.l4_bottom_tab.model.ApiService;
import com.beanu.l4_bottom_tab.support.dialog.ExchangeOrderDialog;
import com.beanu.l4_bottom_tab.ui.MainActivity;
import com.beanu.l4_bottom_tab.ui.module5_my.about_us.AboutUsActivity;
import com.beanu.l4_bottom_tab.ui.module5_my.info.AccountInfoActivity;
import com.beanu.l4_bottom_tab.ui.module5_my.info.UserInfoActivity;
import com.beanu.l4_bottom_tab.ui.module5_my.live_lesson.MyLiveLessonActivity;
import com.beanu.l4_bottom_tab.ui.module5_my.offline_download.MyOfflineLessonActivity;
import com.beanu.l4_bottom_tab.ui.module5_my.online_lesson.MyOnlineLessonActivity;
import com.beanu.l4_bottom_tab.ui.module5_my.order.MyOrderActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 我的
 */
public class Fragment5 extends ToolBarFragment {

    private static final int REQUEST_CODE_UPDATE_USER_INFO = 100;

    Unbinder unbinder;
    @BindView(R.id.contact_info_head)
    ImageView mContactInfoHead;
    @BindView(R.id.contact_info_name)
    TextView mContactInfoName;
    @BindView(R.id.contact_info_tel)
    TextView mContactInfoTel;

    private ExchangeOrderDialog  myDialog;

    public Fragment5() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_5, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        updateUserInfo();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_UPDATE_USER_INFO && resultCode == Activity.RESULT_OK && data != null) {
            String icon = data.getStringExtra("icon");
            String nickName = data.getStringExtra("nickName");
            String sign = data.getStringExtra("sign");
            String sex = data.getStringExtra("sex");
            String school = data.getStringExtra("school");

            if (!TextUtils.isEmpty(icon)) {
                AppHolder.getInstance().user.setIcon(icon);
            }
            AppHolder.getInstance().user.setNickname(nickName);
            AppHolder.getInstance().user.setMotto(sign);
            AppHolder.getInstance().user.setSex(Integer.parseInt(sex));
            AppHolder.getInstance().user.setSchool(school);

            updateUserInfo();
        }
    }

    //展示兑换码弹窗
    private void showDialog(){
        // final EditText editText =new EditText(getActivity());
        final ExchangeOrderDialog.Builder builder = new ExchangeOrderDialog.Builder(getActivity());
        builder.setTitle("兑换课程");
        builder.setPositiveButton("兑换", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {

                String redeemCode = builder.getExchangeCode();
                if (redeemCode.equals("")) {
                    Toast.makeText(getActivity(), "请输入兑换码", Toast.LENGTH_SHORT).show();
                }

                API.getInstance(ApiService.class).exchange_netorder(redeemCode)
                        .compose(RxHelper.<Object>handleResult())
                        .subscribe(new Observer<Object>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                //  mRxManage.add();
                            }

                            @Override
                            public void onNext(Object o) {

                            }

                            @Override
                            public void onError(Throwable e) {

                                ToastUtils.showShort(e.getMessage());
                            }

                            @Override
                            public void onComplete() {

                                ToastUtils.showShort("兑换成功，请到我的直播课和或我的网课中进行观看");
                            }
                        });
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();

    }

    private void updateUserInfo() {
        User user = AppHolder.getInstance().user;

        if (!TextUtils.isEmpty(user.getId())) {
            if (!TextUtils.isEmpty(user.getIcon())) {
                Glide.with(getActivity()).load(user.getIcon()).apply(RequestOptions.circleCropTransform()).into(mContactInfoHead);
            }
            mContactInfoName.setText(user.getNickname());
            mContactInfoTel.setText(user.getMotto());
        }
    }

    @Override
    public String setupToolBarTitle() {
        return "我的";
    }

    @OnClick({R.id.rl_user_info, R.id.txt_my_order, R.id.txt_my_live, R.id.txt_my_online,R.id.rl_item_download, R.id.rl_item_redeemCode, R.id.rl_item_search, R.id.rl_item_recommend, R.id.rl_item_service, R.id.rl_item_account, R.id.rl_item_about_us})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_user_info:
                startActivityForResult(UserInfoActivity.class, REQUEST_CODE_UPDATE_USER_INFO);
                break;
            case R.id.txt_my_order:
                startActivity(MyOrderActivity.class);
                break;
            case R.id.txt_my_live:
                startActivity(MyLiveLessonActivity.class);
                break;
            case R.id.txt_my_online:
                startActivity(MyOnlineLessonActivity.class);
                break;
            case R.id.rl_item_download:
                startActivity(MyOfflineLessonActivity.class);
                break;
            case R.id.rl_item_redeemCode:
                showDialog();
                break;
            case R.id.rl_item_search:
                startActivity(SearchActivity.class);
                break;
            case R.id.rl_item_recommend:
                startActivity(RecommendActivity.class);
                break;
            case R.id.rl_item_service:
                startActivity(ServicesActivity.class);
                break;
            case R.id.rl_item_account:
                Intent intent = new Intent(getActivity(), AccountInfoActivity.class);
                getActivity().startActivityForResult(intent, MainActivity.EXIT_CODE);
                break;
            case R.id.rl_item_about_us:
                startActivity(AboutUsActivity.class);
                break;
        }
    }
}
