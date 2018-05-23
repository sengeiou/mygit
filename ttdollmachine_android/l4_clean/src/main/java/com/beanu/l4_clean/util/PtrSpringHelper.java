package com.beanu.l4_clean.util;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.beanu.arad.utils.ViewUtils;
import com.beanu.l4_clean.R;

import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

public class PtrSpringHelper implements PtrUIHandler {

    @Nullable
    private View progress;
    @Nullable
    private View dynamicView;
    private int minHeight;
    private View[] needToHide = {};

    private final int spinnerStart = R.drawable.sel_spinner;
    private final int spinnerAnim = R.drawable.anim_sel_spinner;

    public PtrSpringHelper() {
    }

    public PtrSpringHelper(@Nullable View progress, @Nullable View dynamicView, int minHeight, View... needToHide) {
        this.progress = progress;
        this.needToHide = needToHide;
        this.dynamicView = dynamicView;
        this.minHeight = minHeight;
    }

    public PtrSpringHelper setProgressView(@Nullable View progress) {
        this.progress = progress;
        return this;
    }

    public PtrSpringHelper setDynamicView(@Nullable View dynamicView, int minHeight) {
        this.dynamicView = dynamicView;
        this.minHeight = minHeight;
        return this;
    }

    public PtrSpringHelper setNeedToHide(View[] needToHide) {
        this.needToHide = needToHide;
        return this;
    }

    public PtrSpringHelper blackMode(boolean black) {
        if (progress != null) {
            progress.setEnabled(!black);
        }
        return this;
    }

    public void attachTo(@NonNull PtrFrameLayout ptrFrameLayout, boolean hasRefresh) {
        PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(ptrFrameLayout.getContext());
        header.setVisibility(View.INVISIBLE);
        ptrFrameLayout.setHeaderView(header);
        ptrFrameLayout.addPtrUIHandler(header);
        ptrFrameLayout.setPinContent(true);
        ptrFrameLayout.addPtrUIHandler(this);
        if (!hasRefresh) {
            ptrFrameLayout.setPtrHandler(new PtrDefaultHandler() {
                @Override
                public void onRefreshBegin(PtrFrameLayout frame) {
                    frame.refreshComplete();
                }
            });
        }
    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {
        setImage(progress, spinnerStart);
        if (progress != null) {
            progress.setRotation(0);
            ViewUtils.setVisibility(View.GONE, progress);
        }
        ViewUtils.setVisibility(View.VISIBLE, needToHide);
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
        ViewUtils.setVisibility(View.INVISIBLE, needToHide);
        ViewUtils.setVisibility(View.VISIBLE, progress);
    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        setImage(progress, spinnerAnim);
        if (progress != null) {
            progress.post(new Runnable() {
                @Override
                public void run() {
                    Drawable drawable = getDrawable(progress);
                    if (drawable != null && drawable instanceof AnimationDrawable) {
                        ((AnimationDrawable) drawable).start();
                    }
                }
            });
        }
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
        Drawable drawable = getDrawable(progress);
        if (drawable != null && drawable instanceof AnimationDrawable) {
            ((AnimationDrawable) drawable).stop();
            setImage(progress, spinnerStart);
        }
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
        if (dynamicView != null) {
            dynamicView.getLayoutParams().height = minHeight + ptrIndicator.getCurrentPosY();
            dynamicView.requestLayout();
        }

        Drawable drawable = getDrawable(progress);
        if (progress != null && drawable != null && !(drawable instanceof AnimationDrawable)) {
            progress.setRotation(ptrIndicator.getCurrentPosY());
        }
    }

    private void setImage(View view, int res) {
        if (view == null) {
            return;
        }
        if (view instanceof ImageView) {
            ((ImageView) view).setImageResource(res);
        } else {
            view.setBackgroundResource(res);
        }
    }

    @Nullable
    private Drawable getDrawable(View view) {
        if (view == null) {
            return null;
        }
        if (view instanceof ImageView) {
            return ((ImageView) view).getDrawable();
        } else {
            return view.getBackground();
        }
    }

}
