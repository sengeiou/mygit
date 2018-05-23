package com.beanu.l4_clean.support.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;

/**
 * 提示  dialog
 * Created by Beanu on 2017/12/5.
 */

public class NeutralDialogFragment extends DialogFragment {

    private DialogInterface.OnClickListener neutralCallback;
    private String title;
    private String message;
    private String hint;

    public void show(String title, String message, String hint, DialogInterface.OnClickListener neutralCallback,
                     FragmentManager fragmentManager) {
        this.title = title;
        this.message = message;
        this.hint = hint;
        this.neutralCallback = neutralCallback;
        show(fragmentManager, "NeutralDialogFragment");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNeutralButton(hint, neutralCallback);
        return builder.create();
    }

}