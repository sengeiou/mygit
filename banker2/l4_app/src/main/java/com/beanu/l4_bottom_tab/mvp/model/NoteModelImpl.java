package com.beanu.l4_bottom_tab.mvp.model;

import com.beanu.arad.http.RxHelper;
import com.beanu.l3_common.model.api.API;
import com.beanu.l4_bottom_tab.model.ApiService;
import com.beanu.l4_bottom_tab.model.bean.ExamNote;
import com.beanu.l4_bottom_tab.mvp.contract.NoteContract;

import io.reactivex.Observable;

/**
 * Created by Beanu on 2017/04/26
 */

public class NoteModelImpl implements NoteContract.Model {

    @Override
    public Observable<String> getQNToken() {
        return API.getInstance(ApiService.class).qn_token(API.createHeader()).compose(RxHelper.<String>handleResult());
    }

    @Override
    public Observable<String> uploadNote(String recordId, int type, int questionType, String courseId, String content, String imgOne, String imgTwo, String imgThree, String imgFour) {
        return API.getInstance(ApiService.class).post_note(API.createHeader(), recordId, type, questionType, courseId, content, imgOne, imgTwo, imgThree, imgFour)
                .compose(RxHelper.<String>handleResult());
    }

    @Override
    public Observable<ExamNote> request_question_note(String id, int type, int questionType, String courseId) {
        return API.getInstance(ApiService.class).request_question_note(API.createHeader(), id, type, questionType, courseId)
                .compose(RxHelper.<ExamNote>handleResult());
    }

}