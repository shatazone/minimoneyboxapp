package com.example.minimoneybox.view.ui;

import android.util.Log;

import androidx.lifecycle.Observer;

import com.example.minimoneybox.network.data.NetworkResponse;

import java.net.HttpURLConnection;

public abstract class RequestObserver<T> implements Observer<Request<NetworkResponse<T>>> {
    @Override
    public final void onChanged(Request<NetworkResponse<T>> request) {
        switch (request.getStatus()) {
            case LOADING:
                updateLoader(true);
                break;

            case SUCCESS:
                if(request.getResponse().getCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    onUnAuthorizedRequest();
                } else {
                    updateLoader(false);
                    onResponseReceived(request.getResponse());
                }
                break;

            case FAILED:
                Log.e(getClass().getSimpleName(), "Error", request.getThrowable());
                updateLoader(false);
                onError(request.getThrowable());
                break;
        }
    }

    protected abstract void updateLoader(boolean loading);
    protected abstract void onResponseReceived(NetworkResponse<T> response);
    protected abstract void onError(Throwable throwable);

    protected void onUnAuthorizedRequest() {
        // ignored because the main activity should be handling it
    }
}
