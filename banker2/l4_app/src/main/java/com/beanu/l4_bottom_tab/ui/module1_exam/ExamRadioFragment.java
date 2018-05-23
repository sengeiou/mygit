package com.beanu.l4_bottom_tab.ui.module1_exam;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beanu.arad.Arad;
import com.beanu.arad.base.ToolBarFragment;
import com.beanu.l4_bottom_tab.R;
import com.beanu.l4_bottom_tab.adapter.ExamRadioAdapter;
import com.beanu.l4_bottom_tab.model.bean.ExamNote;
import com.beanu.l4_bottom_tab.model.bean.ExamQuestion;
import com.beanu.l4_bottom_tab.model.bean.QuestionEntry;
import com.beanu.l4_bottom_tab.support.widget.RadioGroupListView;
import com.zzhoujay.richtext.ImageHolder;
import com.zzhoujay.richtext.RichText;
import com.zzhoujay.richtext.callback.ImageFixCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * 单选题
 */
public class ExamRadioFragment extends ToolBarFragment {

    private static final String ARG_QUESTION = "arg_question";
    private static final String ARG_MATERIAL = "arg_material";
    private static final int REQUESTCODE = 100;

    @BindView(R.id.txt_exam_title) TextView mTxtTitle;
    @BindView(R.id.txt_exam_progress) TextView mTxtProgress;
    @BindView(R.id.txt_exam_content) TextView mTxtContent;
    @BindView(R.id.radio_group_exam) RadioGroupListView mRadioGroup;
    @BindView(R.id.rl_exam_title) RelativeLayout mRelativeLayoutTitle;
    @BindView(R.id.view_place) View mViewPlace;
    @BindView(R.id.txt_analysis) TextView mTxtAnalysis;
    @BindView(R.id.ll_analysis) LinearLayout mLlAnalysis;
    @BindView(R.id.ll_note) LinearLayout mLlNote;
    @BindView(R.id.txt_note) TextView mTxtNote;
    @BindView(R.id.txt_note_content) TextView mTxtNoteContent;
    Unbinder unbinder;

    private IExamResponseListener mResponseListener;

    ExamRadioAdapter mRadioAdapter;
    ExamQuestion mExamQuestion;
    QuestionEntry mQuestionEntry;
    boolean isMaterialMode;//是否是材料题
    int position_whichOne;//第几个题

    int screen_width;

    public ExamRadioFragment() {
        // Required empty public constructor
    }

    public static ExamRadioFragment newInstance(QuestionEntry entry, boolean isMaterial) {

        Bundle args = new Bundle();
        args.putParcelable(ARG_QUESTION, entry);
        args.putBoolean(ARG_MATERIAL, isMaterial);

        ExamRadioFragment fragment = new ExamRadioFragment();
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
            isMaterialMode = getArguments().getBoolean(ARG_MATERIAL, false);

            mExamQuestion = mQuestionEntry.getExamQuestion();
            position_whichOne = mQuestionEntry.getPosition();
        }
        screen_width = Arad.app.deviceInfo.getScreenWidth() - getResources().getDimensionPixelSize(R.dimen.fab_margin) * 2;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_exam_radio, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refreshView();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUESTCODE && resultCode == Activity.RESULT_OK) {
            ExamNote examNote = data.getParcelableExtra("note");
            mExamQuestion.setNote(examNote);
            refreshView();
        }
    }

    public void refreshView() {
        mTxtTitle.setText(mQuestionEntry.getTitle());
        mTxtProgress.setText((position_whichOne + 1) + "/" + mQuestionEntry.getCount());

        try {
            if(!TextUtils.isEmpty((isMaterialMode ? "【选项】" : "【单选题】") + mExamQuestion.getStem())){
            RichText.fromHtml((isMaterialMode ? "【选项】" : "【单选题】") + mExamQuestion.getStem())
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        mRadioAdapter = new ExamRadioAdapter(getActivity(), mExamQuestion, mQuestionEntry.getExam_or_analysis() != 1);
        mRadioGroup.setAdapter(mRadioAdapter);

        //答题模式
        if (mQuestionEntry.getExam_or_analysis() != 1) {

            for (int i = 0; i < mRadioGroup.getChildCount(); i++) {
                final int _position = i;
                View radiobutton = mRadioGroup.getChildAt(i);
                radiobutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mResponseListener.onRadio(mExamQuestion.getDataList().get(_position).getId(), position_whichOne);
                    }
                });
            }

//            mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
//                    mResponseListener.onRadio(mExamQuestion.getDataList().get(i).getId(), position_whichOne);
//                }
//            });
        }

        //错误分析模式
        if (mQuestionEntry.getExam_or_analysis() == 1) {

            mLlAnalysis.setVisibility(View.VISIBLE);
            if(!TextUtils.isEmpty(mExamQuestion.getAnalysis())){


            RichText.fromHtml(mExamQuestion.getAnalysis())
                    .autoFix(false)
                    .scaleType(ImageHolder.ScaleType.fit_xy)
                    .size(ImageHolder.WRAP_CONTENT, ImageHolder.WRAP_CONTENT)
                    .resetSize(true)
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
                    startActivityForResult(intent, REQUESTCODE);
                }
            });
            mTxtNote.setText("编辑笔记");

            if (mExamQuestion.getNote() != null) {
                ExamNote note = mExamQuestion.getNote();

//                if (!TextUtils.isEmpty(note.getContent())) {
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

        //如果是材料题
        if (isMaterialMode) {
            mRelativeLayoutTitle.setBackgroundColor(getResources().getColor(R.color.color_line));
            mViewPlace.setVisibility(View.GONE);
        }
    }


    public QuestionEntry getQuestionEntry() {
        return mQuestionEntry;
    }
}
