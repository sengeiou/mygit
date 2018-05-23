package com.beanu.l4_bottom_tab.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;

import com.beanu.arad.Arad;
import com.beanu.arad.widget.dialog.AlertDialogFragment;

public class dialogUtils {

    public static Context getContext() {
        return Arad.app;
    }

    public static View inflate(int resId) {
        return LayoutInflater.from(getContext()).inflate(resId, null);
    }


    public static int convertDpToPixel(Context context, float dp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return (int) px;
    }


    public static void showEdittextDialog(FragmentManager fm, String title,
                                       String message, String positiveButtonText,
                                       String negativeButtonText, DialogInterface.OnClickListener positiveListener,
                                       DialogInterface.OnClickListener negativeListener) {
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        AlertDialogFragment dialog = AlertDialogFragment.newInstance(title,
                message, positiveButtonText, negativeButtonText);
        dialog.setNegativeListener(negativeListener);
        dialog.setPositiveListener(positiveListener);
        dialog.show(fm, "dialog");

    }
}
