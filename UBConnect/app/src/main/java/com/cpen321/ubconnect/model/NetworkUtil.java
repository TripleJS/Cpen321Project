package com.cpen321.ubconnect.model;


import retrofit2.Response;

public class NetworkUtil {

    public static String onServerResponseError(Response response) {
        return response.message();
    }

    public static String onServerResponseError(Response response, String route) {
        if(route.equals("signup") && response.code() == 403){
            return "username already exists";
        }
        if(route.equals("login") && response.code() == 403){
            return "Incorrect Password";
        }
        return response.message();
    }
}
