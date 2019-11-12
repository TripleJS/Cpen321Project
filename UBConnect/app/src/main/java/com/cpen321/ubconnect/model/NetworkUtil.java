package com.cpen321.ubconnect.model;


import java.util.Set;

import okhttp3.Headers;
import retrofit2.Response;

public class NetworkUtil {

    public static String onServerResponseError(Response response) {
        return response.message();
    }
}
