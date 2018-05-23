package com.beanu.l4_bottom_tab.mvp.contract;

import com.beanu.arad.base.BaseModel;
import com.beanu.arad.base.BasePresenter;
import com.beanu.arad.base.BaseView;
import com.beanu.l4_bottom_tab.model.bean.ExamNote;

import java.util.List;

import io.reactivex.Observable;

/**
 * 做笔记
 * Created by Beanu on 2017/4/26.
 */

public interface NoteContract {

    public interface View extends BaseView {
        void uploadNoteStatus(boolean success, ExamNote examNote);

        void cannotPostNote();

        void showNoteContent(ExamNote noteEntry);

    }

    public abstract class Presenter extends BasePresenter<View, Model> {

        //提交笔记
        public abstract void updateNote(String recordId, int type, int questionType, String courseId, String content);

        //批量上传图片
        public abstract void uploadPhotos(List<String> imgPaths);

        //获取笔记内容
        public abstract void request_note(String questionId, int type, int questionType, String courseId);

    }

    public interface Model extends BaseModel {
        Observable<String> getQNToken();

        Observable<String> uploadNote(String recordId, int type, int questionType, String courseId, String content, String imgOne, String imgTwo, String imgThree, String imgFour);

        Observable<ExamNote> request_question_note(String id, int type, int questionType, String courseId);
    }


}