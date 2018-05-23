package com.beanu.l4_clean.mvp.presenter;

import android.text.TextUtils;

import com.beanu.l3_common.util.Constants;
import com.beanu.l4_clean.mvp.contract.RecordReportContract;
import com.beanu.l4_clean.util.ImageUtil;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONObject;

import java.util.Map;
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
 * Created by Beanu on 2018/03/27
 */

public class RecordReportPresenterImpl extends RecordReportContract.Presenter {

    private String mQiniuToken;
    private String qn_path;
    private UploadManager uploadManager = new UploadManager();

    private boolean uploadComplete = true;

    @Override
    public void onStart() {
        //获取七牛的token
        mModel.getQNToken().subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                mRxManage.add(d);
            }

            @Override
            public void onNext(String s) {
                mQiniuToken = s;
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

    @Override
    public void uploadImage(String path) {

        uploadComplete = false;

        Observable.just(path)
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
                    public void onSubscribe(Disposable d) {
                        mRxManage.add(d);
                    }

                    @Override
                    public void onNext(String s) {
                        qn_path = s;

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        uploadComplete = true;
                    }
                });
    }

    @Override
    public void pushReport(Map<String, String> params) {
        if (uploadComplete) {
            mView.showProgress();

            mModel.report(params).subscribe(new Observer<Object>() {
                @Override
                public void onSubscribe(Disposable d) {
                    mRxManage.add(d);
                }

                @Override
                public void onNext(Object s) {

                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                    mView.hideProgress();
                }

                @Override
                public void onComplete() {
                    mView.reportSuccess();
                    mView.hideProgress();
                }
            });
        }
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

                                if (!TextUtils.isEmpty(Constants.QNUrl)) {
                                    String httpPath = Constants.QNUrl;
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