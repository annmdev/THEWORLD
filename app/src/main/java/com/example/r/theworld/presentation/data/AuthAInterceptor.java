package com.example.r.theworld.presentation.data;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Response;

public class AuthAInterceptor implements Interceptor {

    private static final String KEY = "key";

    private static final String PRIVATE_API_KEY = "09381ec02cc6434cb4845616190304";


    @Override
    public Response intercept(Chain chain) throws IOException {
        HttpUrl httpUrl = chain.request().url().newBuilder()
                .addQueryParameter(KEY, PRIVATE_API_KEY)
                .build();

        return chain.proceed(
                chain.request().newBuilder()
                .url(httpUrl)
                .build()
        );
    }
}
