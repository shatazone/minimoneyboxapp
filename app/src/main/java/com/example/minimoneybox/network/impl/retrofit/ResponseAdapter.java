package com.example.minimoneybox.network.impl.retrofit;

import com.example.minimoneybox.network.data.ErrorBody;
import com.example.minimoneybox.network.data.NetworkResponse;
import com.google.gson.Gson;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class ResponseAdapter {
    private final Gson gson;

    @Inject
    public ResponseAdapter(Gson gson) {
        this.gson = gson;
    }

    public <T> NetworkResponse<T> adapt(Response<T> response) throws IOException {
        NetworkResponse<T> networkResponse = new NetworkResponse<>();
        networkResponse.setBody(response.body());
        networkResponse.setCode(response.code());
        networkResponse.setErrorBody(adapt(response.errorBody()));
        return networkResponse;
    }

    public ErrorBody adapt(ResponseBody responseBody) throws IOException {
        if(responseBody == null) {
            return null;
        }

        return gson.fromJson(responseBody.string(), ErrorBody.class);
    }


}
