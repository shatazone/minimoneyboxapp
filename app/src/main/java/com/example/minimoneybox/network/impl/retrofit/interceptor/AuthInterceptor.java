package com.example.minimoneybox.network.impl.retrofit.interceptor;

import com.example.minimoneybox.network.AuthTokenExpiredConsumer;
import com.example.minimoneybox.network.AuthTokenSupplier;
import com.example.minimoneybox.network.data.ErrorBody;
import com.example.minimoneybox.network.impl.retrofit.ResponseAdapter;

import java.net.HttpURLConnection;

import javax.inject.Inject;

import lombok.SneakyThrows;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    private final AuthTokenSupplier tokenSupplier;
    private final ResponseAdapter responseAdapter;
    private final AuthTokenExpiredConsumer authTokenExpiredConsumer;

    @Inject
    public AuthInterceptor(AuthTokenSupplier tokenSupplier, ResponseAdapter responseAdapter, AuthTokenExpiredConsumer authTokenExpiredConsumer) {
        this.tokenSupplier = tokenSupplier;
        this.responseAdapter = responseAdapter;
        this.authTokenExpiredConsumer = authTokenExpiredConsumer;
    }

    @SneakyThrows
    @Override
    public Response intercept(Chain chain) {
        Request request = chain.request()
                .newBuilder()
                .addHeader("Authorization", "Bearer " + tokenSupplier.get())
                .build();

        Response response = chain.proceed(request);

        if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
            ErrorBody errorBody = responseAdapter.adapt(response.peekBody(2048));
            authTokenExpiredConsumer.accept(errorBody);
        }

        return response;
    }
}