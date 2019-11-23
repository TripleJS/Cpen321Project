package com.cpen321.ubconnect.model;


import retrofit2.Response;

public class NetworkUtil {

    public static String onServerResponseError(Response response) {
        return response.message();
    }
}
