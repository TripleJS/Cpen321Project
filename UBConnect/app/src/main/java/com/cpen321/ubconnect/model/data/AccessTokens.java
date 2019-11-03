package com.cpen321.ubconnect.model.data;

public class AccessTokens {
    String access_token;
    String fcmAccessToken;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getFcmAccessToken() {
        return fcmAccessToken;
    }

    public void setFcmAccessToken(String fcmAccessToken) {
        this.fcmAccessToken = fcmAccessToken;
    }
}
