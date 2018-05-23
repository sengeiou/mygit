package com.beanu.l4_bottom_tab.ui.module1_exam;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.base.ToolBarFragment;
import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.model.bean.ExamNote;
import com.beanu.l4_bottom_tab.model.bean.ExamOption;
import com.beanu.l4_bottom_tab.model.bean.ExamQuestion;
import com.beanu.l4_bottom_tab.model.bean.QuestionEntry;
import com.zzhoujay.richtext.ImageHolder;
import com.zzhoujay.richtext.RichText;
import com.zzhoujay.richtext.callback.ImageFixCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * 判断题
 */
public class ExamJudgeFragment extends ToolBarFragment {

    private static final String ARG_QUESTION = "arg_question";
    private static final String ARG_WHICHONE = "arg_whichOne";

    @BindView(R.id.txt_exam_title) TextView mTxtTitle;
    @BindView(R.id.txt_exam_progress) TextView mTxtProgress;
    @BindView(R.id.txt_exam_content) TextView mTxtContent;
    @BindView(R.id.radio_group_exam) RadioGroup mRadioGroupExam;
    @BindView(R.id.radio_button_right) RadioButton mRadioButtonRight;
    @BindView(R.id.radio_button_wrong) RadioButton mRadioButtonWrong;
    @BindView(R.id.txt_analysis) TextView mTxtAnalysis;
    @BindView(R.id.ll_analysis) LinearLayout mLlAnalysis;
    @BindView(R.id.ll_note) LinearLayout mLlNote;
    @BindView(R.id.txt_note) TextView mTxtNote;
    @BindView(R.id.txt_note_content) TextView mTxtNoteContent;
    Unbinder unbinder;

    private IExamResponseListener mResponseListener;
    int position_whichOne;//第几个题

    ExamQuestion mExamQuestion;
    QuestionEntry mQuestionEntry;

    int screen_width;


    public ExamJudgeFragment() {
        // Required empty public constructor
    }

    public static ExamJudgeFragment newInstance(QuestionEntry question) {

        Bundle args = new Bundle();
        args.putParcelable(ARG_QUESTION, question);

        ExamJudgeFragment fragment = new ExamJudgeFragment();
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exam_judge, container, false);
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

            RichText.from("【判断题】" + mExamQuestion.getStem())
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
                    })
                    .into(mTxtContent);

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mQuestionEntry.getExam_or_analysis() != 1) {

            mRadioButtonRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mExamQuestion.getDataList() != null && mExamQuestion.getDataList().size() == 2) {
                        String selectedId = mExamQuestion.getDataList().get(0).getId();
                        mResponseListener.onJudge(selectedId, position_whichOne);
                    }
                }
            });
            mRadioButtonWrong.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mExamQuestion.getDataList() != null && mExamQuestion.getDataList().size() == 2) {
                        String selectedId = mExamQuestion.getDataList().get(1).getId();
                        mResponseListener.onJudge(selectedId, position_whichOne);
                    }
                }
            });

//            mRadioGroupExam.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
//
//                    if (mExamQuestion.getDataList() != null && mExamQuestion.getDataList().size() == 2) {
//
//                        if (i == R.id.radio_button_right) {
//                            KLog.d("我点击了 正确");
//                            String selectedId = mExamQuestion.getDataList().get(0).getId();
//                            mResponseListener.onJudge(selectedId, position_whichOne);
//                        } else if (i == R.id.radio_button_wrong) {
//                            KLog.d("我点击了 错误");
//                            String selectedId = mExamQuestion.getDataList().get(1).getId();
//                            mResponseListener.onJudge(selectedId, position_whichOne);
//                        }
//                    } else {
//                        KLog.d("判断题数据有问题");
//                    }
//
//                }
//            });
        } else {

            mRadioButtonRight.setCompoundDrawablesWithIntrinsicBounds(R.drawable.right_s, 0, 0, 0);
            mRadioButtonWrong.setCompoundDrawablesWithIntrinsicBounds(R.drawable.wrong, 0, 0, 0);

            for (int i = 0; i < mExamQuestion.getDataList().size(); i++) {
                ExamOption option = mExamQuestion.getDataList().get(i);
                if (option.getIs_correct() == 1) {
                    switch (i) {
                        case 0:
                            mRadioButtonRight.setCompoundDrawablesWithIntrinsicBounds(R.drawable.right_right, 0, 0, 0);
                            break;
                        case 1:
                            mRadioButtonWrong.setCompoundDrawablesWithIntrinsicBounds(R.drawable.wrong_right, 0, 0, 0);
                            break;
                    }
                }
            }
            if (mExamQuestion.getIsRealy() == 0 && mExamQuestion.getAnswer() != null) {
                if (mExamQuestion.getDataList().get(0).getId().equals(mExamQuestion.getAnswer())) {
                    mRadioButtonRight.setCompoundDrawablesWithIntrinsicBounds(R.drawable.right_wrong, 0, 0, 0);
                } else {
                    mRadioButtonWrong.setCompoundDrawablesWithIntrinsicBounds(R.drawable.wrong_wrong, 0, 0, 0);
                }
            }


        }

        //错误分析模式
        if (mQuestionEntry.getExam_or_analysis() == 1) {
            mLlAnalysis.setVisibility(View.VISIBLE);
            if(!TextUtils.isEmpty(mExamQuestion.getAnalysis())) {


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
                        })
                        .into(mTxtAnalysis);
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


}
