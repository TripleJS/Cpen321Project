package com.cpen321.ubconnect.model;

import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class ErrorHandlingUtils {
    public static void errorHandling(String err, View view){
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
