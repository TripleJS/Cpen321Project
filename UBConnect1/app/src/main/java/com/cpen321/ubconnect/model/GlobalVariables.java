package com.cpen321.ubconnect.model;

import android.app.Application;

public class GlobalVariables extends Application {

    private String userID;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
