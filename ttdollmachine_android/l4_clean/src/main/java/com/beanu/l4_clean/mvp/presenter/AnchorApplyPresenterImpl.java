package com.beanu.l4_clean.mvp.presenter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.beanu.l3_common.util.Constants;
import com.beanu.l4_clean.mvp.contract.AnchorApplyContract;
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
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * Created by Beanu on 2018/01/25
 */

public class AnchorApplyPresenterImpl extends AnchorApplyContract.Presenter {

    private String mQiniuToken;
    private UploadManager uploadManager = new UploadManager();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        mModel.refreshQNToken().subscribe(new Observer<String>() {
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
    public void applyAnchor(final Map<String, String> parmas, String pathC, String pathF, String pathB) {
        mView.showProgress();

        final String[] pathUrl = new String[3];
        String[] paths = new String[]{pathC, pathF, pathB};
        Observable.fromArray(paths)
                .concatMap(new Function<String, ObservableSource<byte[]>>() {
                    @Override
                    public ObservableSource<byte[]> apply(String s) throws Exception {
                        //1.压缩这张图片
                        return compressPhoto(s);
                    }
                })
                .concatMap(new Function<byte[], ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(byte[] bytes) throws Exception {
                        //2.上传到七牛得到url
                        return uploadToQN(bytes);
                    }
                })
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mRxManage.add(d);
                    }

                    @Override
                    public void onNext(String s) {

                        if (TextUtils.isEmpty(pathUrl[0])) {
                            pathUrl[0] = s;
                        } else if (TextUtils.isEmpty(pathUrl[1])) {
                            pathUrl[1] = s;
                        } else if (TextUtils.isEmpty(pathUrl[2])) {
                            pathUrl[2] = s;
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {


                        if (!TextUtils.isEmpty(pathUrl[0]) && !TextUtils.isEmpty(pathUrl[1]) && !TextUtils.isEmpty(pathUrl[2])) {

                            parmas.put("backImg", pathUrl[0]);
                            parmas.put("cardAhead", pathUrl[1]);
                            parmas.put("cardBack", pathUrl[2]);

                            mModel.applyAnchor(parmas).subscribe(new Observer<Object>() {
                                @Override
                                public void onSubscribe(Disposable d) {
                                    mRxManage.add(d);
                                }

                                @Override
                                public void onNext(Object o) {

                                }

                                @Override
                                public void onError(Throwable e) {
                                    e.printStackTrace();
                                    mView.hideProgress();
                                    mView.uiApplyAnchor(false);
                                }

                                @Override
                                public void onComplete() {
                                    mView.hideProgress();
                                    mView.uiApplyAnchor(true);
                                }
                            });
                        } else {
                            mView.uiApplyAnchor(false);
                        }


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
}