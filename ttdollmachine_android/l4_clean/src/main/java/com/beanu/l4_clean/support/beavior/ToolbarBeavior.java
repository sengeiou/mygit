//package com.beanu.l4_clean.support.beavior;
//
//import android.content.Context;
//import android.os.Message;
//import android.support.annotation.NonNull;
//import android.support.design.widget.AppBarLayout;
//import android.support.design.widget.CoordinatorLayout;
//import android.support.v4.view.ViewCompat;
//import android.support.v7.widget.Toolbar;
//import android.util.AttributeSet;
//import android.util.DisplayMetrics;
//import android.view.View;
//
//import com.beanu.arad.support.log.KLog;
//import com.beanu.arad.utils.SizeUtils;
//import com.beanu.l4_clean.ui.MainActivity;
//
///**
// * toolbar的渐变
// * Created by Beanu on 2018/2/10.
// */
//
//public class ToolbarBeavior extends CoordinatorLayout.Behavior<Toolbar> {
//
//    Context mContext;
//
//    public ToolbarBeavior(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        DisplayMetrics display = context.getResources().getDisplayMetrics();
//        mContext = context;
//    }
//
//    @Override
//    public boolean layoutDependsOn(CoordinatorLayout parent, Toolbar child, View dependency) {
//
////        float maxL = DensityUtil.dip2px(mContext, 150) + DensityUtil.getZhuangtai(mContext);
//
//        KLog.d(dependency.getY() + "Y");
//
//        Message message = new Message();
//
//        if (dependency.getY() > 0) {
//
//            float a = ((dependency.getY()) / (SizeUtils.dp2px(356)));
//
//            KLog.d("" + a);
//            message.what = (int) (a * 100f);
//
//
//            if (MainActivity.mHandler != null)
//                MainActivity.mHandler.sendMessage(message);
//        }
//
//        return dependency instanceof CoordinatorLayout;
//    }
//
//    @Override
//    public boolean onDependentViewChanged(CoordinatorLayout parent, Toolbar child, View dependency) {
//        //得到依赖View的滑动距离
//        int top = ((AppBarLayout.Behavior) ((CoordinatorLayout.LayoutParams) dependency.getLayoutParams()).getBehavior()).getTopAndBottomOffset();
//        //因为BottomNavigation的滑动与ToolBar是反向的，所以取-top值
//        ViewCompat.setTranslationY(child, -top);
//        return false;
//    }
//
//
//    @Override
//    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull Toolbar child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
//
//        KLog.d("滑动" + dy);
//    }
//
////    @Override
////    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
////        //super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
////
////        if (dy>0 &&!isAnimate && child.getTranslationY()<child.getHeight()){
////            child.setTranslationY(child.getTranslationY() + dy);
////        }else if (dy<0 &&!isAnimate && child.getTranslationY()>0){
////            child.setVisibility(View.VISIBLE);
////            if (child.getTranslationY()+dy<0){
////                child.setTranslationY(0);
////            }else {
////                child.setTranslationY(child.getTranslationY()+dy);
////            }
////        }
////    }
//
//
//}
