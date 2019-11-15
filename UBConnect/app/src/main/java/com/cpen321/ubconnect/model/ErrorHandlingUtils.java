package com.cpen321.ubconnect.model;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.cpen321.ubconnect.ui.home.HomeActivity;
import com.google.android.material.snackbar.Snackbar;

public class ErrorHandlingUtils {
    private Snackbar mSnackBar;

    public void hideError() {
        if (mSnackBar != null) {
            mSnackBar.dismiss();
        }
    }

    public void showError(final Activity activity, final String err, View.OnClickListener onClickListener, String action) {
        try {
            Thread.sleep(100);

            mSnackBar = Snackbar.make(activity.findViewById(android.R.id.content) , err, Snackbar.LENGTH_INDEFINITE)
                    .setAction(action, onClickListener);

            View snackbarView = mSnackBar.getView();
            TextView snackTextView = (TextView) snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);

            snackTextView.setMaxLines(2);

            mSnackBar.show();
        }catch (Exception e){};

    }
}
