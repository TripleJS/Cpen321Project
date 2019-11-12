package com.cpen321.ubconnect.model;

import android.app.Application;

public class GlobalVariables extends Application {

    private String userID;
    private String jwt;


    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
