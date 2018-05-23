package com.beanu.l4_bottom_tab.mvp.presenter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.beanu.l3_common.util.Constants;
import com.beanu.l4_bottom_tab.mvp.contract.UserInfoContract;
import com.beanu.l4_bottom_tab.util.ImageUtil;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONObject;

import java.util.UUID;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Beanu on 2017/05/22
 */

public class UserInfoPresenterImpl extends UserInfoContract.Presenter {

    private String mQiniuToken;
    private UploadManager uploadManager = new UploadManager();
    private String qn_path;           //从七牛获取的地址
    private boolean uploadComplete = true;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //获取七牛的token

        mModel.getQNToken().subscribe(new Observer<String>() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSubscribe(Disposable d) {
                mRxManage.add(d);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String jsonObject) {
                mQiniuToken = jsonObject;
            }
        });
    }



    @Override
    public void updateUserInfo(String icon, String nickName, String motto, String sex, String school) {
        if (uploadComplete) {
            mView.showProgress();

            mModel.updateUserInfo(icon, nickName, motto, sex, school)
                    .subscribe(new Observer<String>() {
                        @Override
                        public void onComplete() {
                            mView.hideProgress();
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            mView.hideProgress();
                        }

                        @Override
                        public void onSubscribe(Disposable d) {
                            mRxManage.add(d);
                        }

                        @Override
                        public void onNext(String s) {
                            mView.updateSuccess();
                        }
                    });
        }
    }

    @Override
    public void uploadPhoto(String imgPaths) {

        uploadComplete = false;

        //0.发送一张图片
        Observable.just(imgPaths)
                .flatMap(new Function<String, ObservableSource<byte[]>>() {
                    @Override
                    public ObservableSource<byte[]> apply(String s) throws Exception {
                        //1.压缩这张图片
                        return compressPhoto(s);
                    }
                })
                .flatMap(new Function<byte[], ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(byte[] bytes) throws Exception {
                        //2.上传到七牛得到url
                        return uploadToQN(bytes);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onComplete() {
                        uploadComplete = true;
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        mRxManage.add(d);
                    }

                    @Override
                    public void onNext(String path) {
                        qn_path = path;
                    }
                });
    }

    private Observable<byte[]> compressPhoto(final String path) {

        return Observable.create(new ObservableOnSubscribe<byte[]>() {
            @Override
            public void subscribe(ObservableEmitter<byte[]> subscriber) throws Exception {
                byte[] bytes = ImageUtil.compressImageToByte(path);
                subscriber.onNext(bytes);
                subscriber.onComplete();
            }
        });
    }

    private Observable<String> uploadToQN(final byte[] bytes) {

        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> subscriber) throws Exception {
                String expectKey = UUID.randomUUID().toString();
                uploadManager.put(bytes, expectKey, mQiniuToken, new UpCompletionHandler() {

                            public void complete(String k, ResponseInfo rinfo, JSONObject response) {

                                if (!TextUtils.isEmpty(Constants.QN_URL)) {
                                    String httpPath = Constants.QN_URL;
                                    String s = null;
                                    try {
                                        s = httpPath + response.getString("key");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    subscriber.onNext(s);
                                    subscriber.onComplete();
                                }
                            }
                        }
                        , new UploadOptions(null, "test-type", true, null, null)
                );

            }
        });
    }

    public String getQn_path() {
        return qn_path;
    }
}