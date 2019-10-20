package com.cpen321.ubconnect.model;


import java.util.Set;

import okhttp3.Headers;
import retrofit2.Response;

public class NetworkUtil {

    public static String onServerResponseError(Response response) {

        if (response.code() == 403) {
            Headers headers = response.headers();
            Set<String> headerNames = headers.names();

            for (String headerName : headerNames) {
                String headerValue = headers.get(headerName);
                if (headerValue == null)
                    continue;


            }
        }

        return response.message();
    }
}
