package com.beanu.l4_bottom_tab.ui.module1_exam;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.base.ToolBarFragment;
import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.adapter.ExamCheckboxAdapter;
import com.beanu.l4_bottom_tab.model.bean.ExamNote;
import com.beanu.l4_bottom_tab.model.bean.ExamQuestion;
import com.beanu.l4_bottom_tab.model.bean.QuestionEntry;
import com.beanu.l4_bottom_tab.support.widget.CheckBoxListView;
import com.zzhoujay.richtext.ImageHolder;
import com.zzhoujay.richtext.RichText;
import com.zzhoujay.richtext.callback.ImageFixCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 多选题
 */
public class ExamMultiSelectFragment extends ToolBarFragment {

    private static final String ARG_QUESTION = "arg_question";

    @BindView(R.id.txt_exam_title) TextView mTxtTitle;
    @BindView(R.id.txt_exam_progress) TextView mTxtProgress;
    @BindView(R.id.txt_exam_content) TextView mTxtContent;
    @BindView(R.id.checkbox_exam_list) CheckBoxListView mCheckboxExamList;
    @BindView(R.id.txt_analysis) TextView mTxtAnalysis;
    @BindView(R.id.ll_analysis) LinearLayout mLlAnalysis;
    @BindView(R.id.ll_note) LinearLayout mLlNote;
    @BindView(R.id.txt_note) TextView mTxtNote;
    @BindView(R.id.txt_note_content) TextView mTxtNoteContent;
    Unbinder unbinder;

    private IExamResponseListener mResponseListener;
    int position_whichOne;//第几个题
    List<String> selectedIds;
    Map<String, Boolean> selectedMap;

    ExamCheckboxAdapter mCheckboxAdapter;
    ExamQuestion mExamQuestion;
    QuestionEntry mQuestionEntry;

    int screen_width;


    public ExamMultiSelectFragment() {
        // Required empty public constructor
    }

    public static ExamMultiSelectFragment newInstance(QuestionEntry question) {

        Bundle args = new Bundle();
        args.putParcelable(ARG_QUESTION, question);

        ExamMultiSelectFragment fragment = new ExamMultiSelectFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IExamResponseListener) {
            mResponseListener = (IExamResponseListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mQuestionEntry = getArguments().getParcelable(ARG_QUESTION);

            mExamQuestion = mQuestionEntry.getExamQuestion();
            position_whichOne = mQuestionEntry.getPosition();
        }

        screen_width = Arad.app.deviceInfo.getScreenWidth() - getResources().getDimensionPixelSize(R.dimen.fab_margin) * 2;

        selectedIds = new ArrayList<>();
        selectedMap = new HashMap<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exam_multi_select, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refreshView();
    }

    public void refreshView() {
        mTxtTitle.setText(mQuestionEntry.getTitle());
        mTxtProgress.setText((position_whichOne + 1) + "/" + mQuestionEntry.getCount());

        try {
            RichText.from("【多选题】" + mExamQuestion.getStem())
                    .autoFix(false)
                    .scaleType(ImageHolder.ScaleType.fit_xy)
                    .resetSize(true)
                    .size(ImageHolder.WRAP_CONTENT, ImageHolder.WRAP_CONTENT)
                    .fix(new ImageFixCallback() {
                        @Override
                        public void onInit(ImageHolder holder) {

                        }

                        @Override
                        public void onLoading(ImageHolder holder) {

                        }

                        @Override
                        public void onSizeReady(ImageHolder holder, int imageWidth, int imageHeight, ImageHolder.SizeHolder sizeHolder) {

                        }

                        @Override
                        public void onImageReady(ImageHolder holder, int width, int height) {

                            if (width * 2 > screen_width) {
                                holder.setWidth(screen_width);
                                holder.setHeight(height * 2 * screen_width / (width * 2));
                            } else {
                                holder.setWidth(width * 2);
                                holder.setHeight(height * 2);
                            }

                        }

                        @Override
                        public void onFailure(ImageHolder holder, Exception e) {

                        }
                    }).into(mTxtContent);
        } catch (Exception e) {
            e.printStackTrace();
        }


        //答题模式
        if (mQuestionEntry.getExam_or_analysis() != 1) {
            mCheckboxExamList.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    int id = compoundButton.getId();
                    String optionId = mExamQuestion.getDataList().get(id).getId();
                    selectedMap.put(optionId, b);
                    notifyActivity();
                }
            });
        }
        mCheckboxAdapter = new ExamCheckboxAdapter(getActivity(), mExamQuestion, mQuestionEntry.getExam_or_analysis() != 1);
        mCheckboxExamList.setAdapter(mCheckboxAdapter);

        //错误分析模式
        if (mQuestionEntry.getExam_or_analysis() == 1) {
            mLlAnalysis.setVisibility(View.VISIBLE);
            if(!TextUtils.isEmpty(mExamQuestion.getAnalysis())){



            RichText.fromHtml(mExamQuestion.getAnalysis())
                    .autoFix(false)
                    .scaleType(ImageHolder.ScaleType.fit_xy)
                    .resetSize(true)
                    .size(ImageHolder.WRAP_CONTENT, ImageHolder.WRAP_CONTENT)
                    .fix(new ImageFixCallback() {
                        @Override
                        public void onInit(ImageHolder holder) {

                        }

                        @Override
                        public void onLoading(ImageHolder holder) {

                        }

                        @Override
                        public void onSizeReady(ImageHolder holder, int imageWidth, int imageHeight, ImageHolder.SizeHolder sizeHolder) {

                        }

                        @Override
                        public void onImageReady(ImageHolder holder, int width, int height) {

                            if (width * 2 > screen_width) {
                                holder.setWidth(screen_width);
                                holder.setHeight(height * 2 * screen_width / (width * 2));
                            } else {
                                holder.setWidth(width * 2);
                                holder.setHeight(height * 2);
                            }

                        }

                        @Override
                        public void onFailure(ImageHolder holder, Exception e) {

                        }
                    }).into(mTxtAnalysis);
            }
            mLlNote.setVisibility(View.VISIBLE);
            mLlNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), NoteActivity.class);
                    intent.putExtra("question", mExamQuestion);
                    startActivity(intent);
                }
            });
            mTxtNote.setText("编辑笔记");

            if (mExamQuestion.getNote() != null) {
                ExamNote note = mExamQuestion.getNote();

                if (!TextUtils.isEmpty(note.getContent())) {
                    String content = "<p>" + note.getContent() + "</p>";
                    if (!TextUtils.isEmpty(note.getImg_one()))
                        content += "<img src='" + note.getImg_one() + "'/>";

                    if (!TextUtils.isEmpty(note.getImg_two()))
                        content += "<img src='" + note.getImg_two() + "'/>";
                    if (!TextUtils.isEmpty(note.getImg_three()))
                        content += "<img src='" + note.getImg_three() + "'/>";
                    if (!TextUtils.isEmpty(note.getImg_four()))
                        content += "<img src='" + note.getImg_four() + "'/>";

                    if(!TextUtils.isEmpty(content)){

                        RichText.fromHtml(content).into(mTxtNoteContent);
                    }
                    mTxtNoteContent.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mResponseListener = null;
    }

    public QuestionEntry getQuestionEntry() {
        return mQuestionEntry;
    }

    private void notifyActivity() {
        selectedIds.clear();
        if (selectedMap != null && selectedMap.size() > 0) {
            for (String key : selectedMap.keySet()) {
                if (selectedMap.get(key)) {
                    selectedIds.add(key);
                }
            }

            mResponseListener.onMutil(selectedIds, position_whichOne);
        }
    }

}
