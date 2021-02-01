package com.example.minimoneybox.network;

import com.example.minimoneybox.network.data.NetworkResponse;

public interface Callback<T> {
    void onStarted();
    void onResponse(NetworkResponse<T> response);
    void onFailure(Throwable throwable);
}
