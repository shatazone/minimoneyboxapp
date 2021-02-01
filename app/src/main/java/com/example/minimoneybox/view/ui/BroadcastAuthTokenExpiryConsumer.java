package com.example.minimoneybox.view.ui;

import android.content.Context;

import com.example.minimoneybox.network.AuthTokenExpiredConsumer;
import com.example.minimoneybox.network.data.ErrorBody;

import javax.inject.Inject;

public class BroadcastAuthTokenExpiryConsumer implements AuthTokenExpiredConsumer {
    private final Context mContext;

    @Inject
    public BroadcastAuthTokenExpiryConsumer(Context context) {
        this.mContext = context;
    }

    @Override
    public void accept(ErrorBody errorBody) {
        MoneyBoxEventBroadcastReceiver.broadcastAuthExpired(mContext, errorBody);
    }
}
