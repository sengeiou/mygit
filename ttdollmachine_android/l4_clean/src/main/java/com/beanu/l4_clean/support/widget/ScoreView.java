package com.beanu.l4_clean.support.widget;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.beanu.l4_clean.R;

/**
 * pk比分
 */

public class ScoreView extends LinearLayout {

    private LinearLayout mScore1, mScore2;
    private SparseIntArray numbers = new SparseIntArray();

    public ScoreView(Context context) {
        this(context, null);
    }

    public ScoreView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScoreView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.layout_score_view, this);

        numbers.put(0, R.drawable.number0);
        numbers.put(1, R.drawable.number1);
        numbers.put(2, R.drawable.number2);
        numbers.put(3, R.drawable.number3);
        numbers.put(4, R.drawable.number4);
        numbers.put(5, R.drawable.number5);
        numbers.put(6, R.drawable.number6);
        numbers.put(7, R.drawable.number7);
        numbers.put(8, R.drawable.number8);
        numbers.put(9, R.drawable.number9);

        mScore1 = findViewById(R.id.score_1);
        mScore2 = findViewById(R.id.score_2);

    }

    private ImageView createNumberView(@IntRange(from = 0, to = 9) int number) {

        ImageView imageView = new ImageView(getContext());
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        imageView.setImageResource(numbers.get(number));
        return imageView;
    }

    public void setScore(int score1, int score2) {

        if (score1 > 0) {
            mScore1.removeAllViews();
        }

        if (score2 > 0) {
            mScore2.removeAllViews();
        }

        int temp;
        while ((temp = (score1 % 10)) > 0) {
            mScore1.addView(createNumberView(temp));
            score1 /= 10;
        }

        while ((temp = (score2 % 10)) > 0) {
            mScore2.addView(createNumberView(temp));
            score2 /= 10;
        }

    }
}
