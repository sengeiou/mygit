package com.beanu.l4_bottom_tab.ui.module4_book;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.beanu.arad.Arad;
import com.beanu.arad.http.RxHelper;
import com.beanu.arad.utils.AppUtils;
import com.beanu.l3_common.model.api.API;
import com.beanu.l3_common.model.bean.EventModel;
import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.adapter.BookFragmentAdapter;
import com.beanu.l4_bottom_tab.base.BaseSDActivity;
import com.beanu.l4_bottom_tab.model.ApiService;
import com.beanu.l4_bottom_tab.model.bean.BookItem;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import com.beanu.l4_bottom_tab.util.Subscriber;
import com.jakewharton.rxbinding2.view.RxView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

@Route(path = "/app/book/detail")
public class BookDetailActivity extends BaseSDActivity {

    private static final int REQUEST_UPDATE_CART_NUM = 0;
    @BindView(R.id.tab_layout) TabLayout mTabLayout;
    @BindView(R.id.viewPager) ViewPager mViewPager;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.txt_cart) TextView mTxtCart;
    @BindView(R.id.frame_layout) FrameLayout mFrameLayout;
    @BindView(R.id.btn_to_car) Button mBtnToCar;
    @BindView(R.id.txt_cart_num) TextView mTxtCartNum;

    BookFragmentAdapter mBookFragmentAdapter;
    private Handler mHanlder;
    private int cartNum;

    private BookItem mBookDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        ButterKnife.bind(this);

        mHanlder = new Handler(getMainLooper());

        final String bookId = getIntent().getStringExtra("bookId");

        mBookFragmentAdapter = new BookFragmentAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mBookFragmentAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mTabLayout.setupWithViewPager(mViewPager);

        cartNum = 0;
        refreshNumOfCart();

        //获取图书的详情
        httpBookDetail(bookId);

        //从网络获取购物车中商品数量
//        requestNumOfCart();

        Disposable subscribe = RxView.clicks(mBtnToCar)
                .throttleFirst(300, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        try {
                            Uri uri = Uri.parse(mBookDetail.getPay_url());
                            Intent intent = new Intent("android.intent.action.VIEW", uri);
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
        //加入购物车 事件
        /*Observable<Object> observable = RxView.clicks(mBtnToCar).share();
        observable
                .observeOn(AndroidSchedulers.mainThread())
//                .doOnNext(new Action1<Void>() {
//                    @Override
//                    public void call(Void aVoid) {
//                        addToCartAnim();
//                    }
//                })
                .buffer(observable.debounce(300, TimeUnit.MILLISECONDS))
                .map(new Function<List<Object>, Integer>() {
                    @Override
                    public Integer apply(List<Object> voids) {
                        return voids.size();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        cartNum += integer;
//                        httpAddShopToCart(bookId, integer);//请求网络
                        //跳转到淘宝或浏览器

                        Uri uri = Uri.parse(mBookDetail.getPay_url());
                        Intent intent = new Intent("android.intent.action.VIEW", uri);


                       startActivity(intent);

//
//                        boolean isInstall = AppUtils.isInstallApp("com.taobao.taobao");
//                        if (mBookDetail != null && !TextUtils.isEmpty(mBookDetail.getPay_url())) {
//
//                            if (isInstall) {
//                                Intent intent = new Intent();
//                                intent.setAction("android.intent.action.VIEW");
//                                Uri uri = Uri.parse(mBookDetail.getPay_url());
//                                intent.setData(uri);
//                                intent.setClassName("com.taobao.taobao", "com.taobao.tao.detail.activity.DetailActivity");
//                                startActivity(intent);
//
//                            } else {
//                                Uri uri = Uri.parse(mBookDetail.getPay_url());
//                                Intent intent = new Intent("android.intent.action.VIEW", uri);
//                                startActivity(intent);
//                            }
//                        }
                    }
                });*/

        Arad.bus.register(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_UPDATE_CART_NUM) {
//            requestNumOfCart();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Arad.bus.unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventModel.CartBuySuccess item) {
        //如果购物车中商品购买成功  则关闭这个页面
        finish();
    }


    @OnClick({R.id.txt_cart})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txt_cart:
                startActivityForResult(ShoppingCartActivity.class, REQUEST_UPDATE_CART_NUM);

                break;
        }
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
    public String setupToolBarTitle() {
        return "";
    }

    //获取购物车中的数量
    private void requestNumOfCart() {
        API.getInstance(ApiService.class)
                .numOfShopCart(API.createHeader())
                .compose(RxHelper.<Integer>handleResult())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        refreshNumOfCart();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integerHttpModel) {
                        cartNum = integerHttpModel;
                    }
                });
    }

    //添加商品到购物车
    private void httpAddShopToCart(String productId, int num) {
        API.getInstance(ApiService.class)
                .addShopToCart(API.createHeader(), productId, num)
                .compose(RxHelper.<Void>handleResult())
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Void aVoid) {

                    }
                });
    }

    //获取图书的详情
    private void httpBookDetail(String bookId) {
        API.getInstance(ApiService.class).book_detail(API.createHeader(), bookId)
                .compose(RxHelper.<BookItem>handleResult())
                .subscribe(new Subscriber<BookItem>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(BookItem bookItem) {

                        mBookDetail = bookItem;
                        mBookFragmentAdapter.setData(bookItem);
                        mBookFragmentAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void refreshNumOfCart() {
        mTxtCartNum.setTag(cartNum);
        if (cartNum == 0) {
            mTxtCartNum.setVisibility(View.GONE);
        } else {
            mTxtCartNum.setText(cartNum + "");
            mTxtCartNum.setVisibility(View.GONE);
        }
    }

    //购物车添加动画
    private void addToCartAnim() {
        final ImageView dot = new ImageView(this);
        dot.setImageResource(R.drawable.cart_dot);
        dot.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));

        int[] loc = new int[2];
        mBtnToCar.getLocationInWindow(loc);
        loc[0] = loc[0] + mBtnToCar.getWidth() / 2;
        loc[1] = loc[1] + mBtnToCar.getHeight() / 2;

        addViewToAnimLayout(mFrameLayout, dot, loc);
        AnimatorSet set = createAnim(loc[0], loc[1], dot);
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

                //设置购物车数量
                int num = (int) mTxtCartNum.getTag();
                num++;

                if (num > 0 && mTxtCartNum.getVisibility() == View.GONE) {
                    mTxtCartNum.setVisibility(View.GONE);
                }
                mTxtCartNum.setTag(num);
                mTxtCartNum.setText(num + "");

                //执行动画
                cartButtonAnim(mTxtCart);
                mHanlder.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mFrameLayout.removeView(dot);
                    }
                }, 100);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    private void addViewToAnimLayout(final ViewGroup vg, final View view, int[] location) {

        int x = location[0];
        int y = location[1];
        int[] loc = new int[2];
        vg.getLocationInWindow(loc);
        view.setX(x);
        view.setY(y - loc[1]);
        vg.addView(view);
    }

    private AnimatorSet createAnim(int startX, int startY, View view) {
        int[] des = new int[2];
        mTxtCartNum.getLocationInWindow(des);
        int upDistance = 400;
        int durationTime = 300;

        float distance = Math.abs(des[1] - startY + upDistance);
        long time = (long) (Math.sqrt(distance * 2 / 10000) * 1000);

        AnimatorSet set = new AnimatorSet();

        ObjectAnimator translationX = ObjectAnimator.ofFloat(view, "x", startX, des[0]);
        translationX.setInterpolator(new LinearInterpolator());
        translationX.setDuration(durationTime + time);

        AnimatorSet setT = new AnimatorSet();
        ObjectAnimator translationYUP = ObjectAnimator.ofFloat(view, "y", startY, startY - upDistance);
        translationYUP.setInterpolator(new DecelerateInterpolator());
        translationYUP.setDuration(durationTime);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(view, "y", startY - upDistance, des[1]);
        translationY.setInterpolator(new AccelerateInterpolator());
        translationY.setDuration(time);
        setT.playSequentially(translationYUP, translationY);

        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", 1f, 0.2f);
        alpha.setDuration(durationTime + time);

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.5f);
        scaleX.setInterpolator(new AccelerateInterpolator());
        scaleX.setDuration(durationTime + time);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.5f);
        scaleY.setInterpolator(new AccelerateInterpolator());
        scaleY.setDuration(durationTime + time);

        set.playTogether(translationX, setT, alpha, scaleX, scaleY);
        set.start();

        return set;
    }

    private void cartButtonAnim(final View view) {
        view.animate().scaleX(1.2f).scaleY(1.2f).setDuration(200).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                view.animate().scaleX(1f).scaleY(1f).setDuration(200).start();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        }).start();

    }


}
