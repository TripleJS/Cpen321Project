package com.cpen321.ubconnect.model;


import retrofit2.Response;

public class NetworkUtil {

    public static String onServerResponseError(Response response) {
        return response.message();
    }

    public static String onServerResponseError(Response response, String route) {
        if("signup".equals(route) & response.code() == 403){
            return "username already exists";
        }
        return response.message();
    }
}
