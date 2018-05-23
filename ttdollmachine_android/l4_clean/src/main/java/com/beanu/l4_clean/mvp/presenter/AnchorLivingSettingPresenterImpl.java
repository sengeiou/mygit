package com.beanu.l4_clean.mvp.presenter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.beanu.l3_common.model.PageModel;
import com.beanu.l3_common.util.Constants;
import com.beanu.l4_clean.model.bean.Dolls;
import com.beanu.l4_clean.model.bean.LiveRoom;
import com.beanu.l4_clean.mvp.contract.AnchorLivingSettingContract;
import com.beanu.l4_clean.util.ImageUtil;
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
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * Created by Beanu on 2017/12/02
 */

public class AnchorLivingSettingPresenterImpl extends AnchorLivingSettingContract.Presenter {

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


        mModel.machineList().subscribe(new Observer<PageModel<Dolls>>() {
            @Override
            public void onSubscribe(Disposable d) {
                mRxManage.add(d);
            }

            @Override
            public void onNext(PageModel<Dolls> dollsPageModel) {
                mView.uiDollsList(dollsPageModel.getDataList());
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
    public void startLiving(String path, final String name, final String machineId, boolean updateInfo) {

        mView.showProgress();

        Observer<LiveRoom> observer = new Observer<LiveRoom>() {
            @Override
            public void onSubscribe(Disposable d) {
                mRxManage.add(d);
            }

            @Override
            public void onNext(LiveRoom liveRoom) {
                mView.hideProgress();
                mView.uiRefresh(liveRoom);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                mView.hideProgress();
                mView.uiFailure();
            }

            @Override
            public void onComplete() {

            }
        };

        if (updateInfo) {
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
                    .flatMap(new Function<String, ObservableSource<LiveRoom>>() {
                        @Override
                        public ObservableSource<LiveRoom> apply(String s) throws Exception {
                            return mModel.startLiving(s, name, machineId);
                        }
                    })
                    .subscribe(observer);
        } else {
            mModel.startLiving(path, name, machineId).subscribe(observer);
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
}