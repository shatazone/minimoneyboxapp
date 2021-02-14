package com.example.minimoneybox.network.impl.retrofit.interceptor;

import com.example.minimoneybox.BuildConfig;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HeaderInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request()
                .newBuilder()
                .addHeader("AppId", BuildConfig.APP_ID)
                .addHeader("Content-Type", "application/json")
                .addHeader("appVersion", BuildConfig.VERSION_NAME)
                .addHeader("apiVersion", BuildConfig.API_VERSION)
                .build();
        return chain.proceed(request);
    }
}