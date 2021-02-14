package com.example.minimoneybox.view.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.minimoneybox.network.data.ErrorBody;

public class MoneyBoxEventBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = MoneyBoxEventBroadcastReceiver.class.getSimpleName();

    public static final String ACTION_HANDLE_EVENT = "HANDLE_EVENT";
    public static final String EXTRA_ERROR_BODY = "errorBody";
    public static final String EXTRA_EVENT = "event";
    public static final int EVENT_SESSION_EXPIRED = 1;
    public static final int EVENT_LOGOUT = 2;

    private final Listener mListener;

    public MoneyBoxEventBroadcastReceiver(@NonNull Listener listener) {
        this.mListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int event = intent.getIntExtra(EXTRA_EVENT, -1);

        switch (event) {
            case EVENT_SESSION_EXPIRED:
                ErrorBody errorBody = (ErrorBody) intent.getSerializableExtra(EXTRA_ERROR_BODY);
                mListener.onAuthTokenExpired(errorBody);
                break;

            case EVENT_LOGOUT:
                mListener.onLogout();
                break;

            default:
                Log.w(TAG, String.format("Event '%d' is not identified", event));
        }
    }

    public static void broadcastAuthExpired(Context context, ErrorBody errorBody) {
        Intent intent = new Intent(ACTION_HANDLE_EVENT);
        intent.putExtra(EXTRA_EVENT, EVENT_SESSION_EXPIRED);
        intent.putExtra(EXTRA_ERROR_BODY, errorBody);
        context.sendBroadcast(intent);
    }

    public static void broadcastLogout(Context context) {
        Intent intent = new Intent(ACTION_HANDLE_EVENT);
        intent.putExtra(EXTRA_EVENT, EVENT_LOGOUT);
        context.sendBroadcast(intent);
    }

    public interface Listener {
        void onAuthTokenExpired(ErrorBody errorBody);
        void onLogout();
    }
}
