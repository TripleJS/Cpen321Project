package com.cpen321.ubconnect.model.data;

public class AccessTokens {
    private String access_token;
    private String fcmAccessToken;

    public String getAccessToken() {
        return access_token;
    }

    public void setAccessToken(String access_token) {
        this.access_token = access_token;
    }

    public String getFcmAccessToken() {
        return fcmAccessToken;
    }

    public void setFcmAccessToken(String fcmAccessToken) {
        this.fcmAccessToken = fcmAccessToken;
    }
}
