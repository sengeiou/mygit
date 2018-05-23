package com.beanu.l4_bottom_tab.mvp.presenter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.beanu.arad.support.log.KLog;
import com.beanu.l3_common.util.Constants;
import com.beanu.l4_bottom_tab.model.bean.ExamNote;
import com.beanu.l4_bottom_tab.mvp.contract.NoteContract;
import com.beanu.l4_bottom_tab.util.ImageUtil;
import com.beanu.l4_bottom_tab.util.Subscriber;
import com.qiniu.android.common.Zone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.reactivex.Observable;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Beanu on 2017/04/26
 */

public class NotePresenterImpl extends NoteContract.Presenter {

    private String mQiniuToken;

    Configuration config = new Configuration.Builder().build();
    private UploadManager uploadManager = new UploadManager(config);
    private List<String> qn_path = new ArrayList<>();           //从七牛获取的地址
    private ArrayList<String> mServicePath = new ArrayList<>(); //服务器上的地址
    private boolean uploadComplete = true;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //获取七牛的token
        mModel.getQNToken().subscribe(new Subscriber<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                mRxManage.add(d);
            }

            @Override
            public void onCompleted() {

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
    public void updateNote(String recordId, int type, int questionType, String courseId, String content) {
        if (uploadComplete) {
            mView.showProgress();

            List<String> list = new ArrayList<>();
            list.addAll(mServicePath);
            list.addAll(qn_path);

            String imgOne = null, imgTwo = null, imgThree = null, imgFour = null;
            if (list.size() > 0) imgOne = list.get(0);
            if (list.size() > 1) imgTwo = list.get(1);
            if (list.size() > 2) imgThree = list.get(2);
            if (list.size() > 3) imgFour = list.get(3);

            //ExamNote 传回去
            final ExamNote examNote = new ExamNote();
            examNote.setContent(content);
            examNote.setImg_one(imgOne);
            examNote.setImg_two(imgTwo);
            examNote.setImg_three(imgThree);
            examNote.setImg_four(imgFour);

            mModel.uploadNote(recordId, type, questionType, courseId, content, imgOne, imgTwo, imgThree, imgFour)
                    .subscribe(new Subscriber<String>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            mRxManage.add(d);
                        }

                        @Override
                        public void onCompleted() {
                            mView.hideProgress();
                            mView.uploadNoteStatus(true, examNote);
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            mView.hideProgress();
                            mView.uploadNoteStatus(false, null);
                        }

                        @Override
                        public void onNext(String s) {

                        }
                    });
        } else {
            mView.cannotPostNote();
        }
    }

    @Override
    public void uploadPhotos(List<String> imgPaths) {
        qn_path.clear();
        uploadComplete = false;

        //0.发送一张图片
        Observable.fromIterable(imgPaths)
                .flatMap(new Function<String, ObservableSource<byte[]>>() {
                    @Override
                    public ObservableSource<byte[]> apply(String s) throws Exception {
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
                .subscribe(new Subscriber<String>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        mRxManage.add(d);
                    }

                    @Override
                    public void onCompleted() {
                        uploadComplete = true;
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String path) {
                        KLog.d(path);
                        qn_path.add(path);
                    }
                });
    }

    @Override
    public void request_note(String questionId, int type, int questionType, String courseId) {
        mModel.request_question_note(questionId, type, questionType, courseId)
                .subscribe(new Subscriber<ExamNote>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        mRxManage.add(d);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ExamNote noteEntry) {

                        if (!TextUtils.isEmpty(noteEntry.getImg_one()))
                            mServicePath.add(noteEntry.getImg_one());
                        if (!TextUtils.isEmpty(noteEntry.getImg_two()))
                            mServicePath.add(noteEntry.getImg_two());
                        if (!TextUtils.isEmpty(noteEntry.getImg_three()))
                            mServicePath.add(noteEntry.getImg_three());
                        if (!TextUtils.isEmpty(noteEntry.getImg_four()))
                            mServicePath.add(noteEntry.getImg_four());

                        mView.showNoteContent(noteEntry);
                    }
                });
    }

    public ArrayList<String> getServicePath() {
        return mServicePath;
    }

    private Observable<byte[]> compressPhoto(final String path) {
        return Observable.create(new ObservableOnSubscribe<byte[]>() {
            @Override
            public void subscribe(ObservableEmitter<byte[]> emitter) throws Exception {
                byte[] bytes = ImageUtil.compressImageToByte(path);
                emitter.onNext(bytes);
                emitter.onComplete();
            }
        });
    }

    private Observable<String> uploadToQN(final byte[] bytes) {

        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> emitter) throws Exception {
                String expectKey = UUID.randomUUID().toString();
                uploadManager.put(bytes, expectKey, mQiniuToken, new UpCompletionHandler() {

                            public void complete(String k, ResponseInfo rinfo, JSONObject response) {


                                String s = null;
                                try {
                                    s = Constants.QN_URL + response.getString("key");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                emitter.onNext(s);
                                emitter.onComplete();

                            }
                        }
                        , new UploadOptions(null, "test-type", true, null, null)
                );
            }
        });
    }
}