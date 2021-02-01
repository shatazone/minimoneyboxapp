package com.example.minimoneybox.view.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.minimoneybox.network.data.ErrorBody;

public abstract class MoneyBoxEventBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = MoneyBoxEventBroadcastReceiver.class.getSimpleName();

    public static final String ACTION_HANDLE_EVENT = "HANDLE_EVENT";
    public static final String EXTRA_ERROR_BODY = "errorBody";
    public static final String EXTRA_EVENT = "event";
    public static final int EVENT_SESSION_EXPIRED = 1;
    public static final int EVENT_LOGOUT = 2;

    @Override
    public void onReceive(Context context, Intent intent) {
        int event = intent.getIntExtra(EXTRA_EVENT, -1);

        switch (event) {
            case EVENT_SESSION_EXPIRED:
                ErrorBody errorBody = (ErrorBody) intent.getSerializableExtra(EXTRA_ERROR_BODY);
                onAuthTokenExpired(errorBody);
                setResultCode(Activity.RESULT_OK);
                break;

            case EVENT_LOGOUT:
                onLogout();
                setResultCode(Activity.RESULT_OK);
                break;

            default:
                Log.w(TAG, String.format("Event '%d' is not identified", event));
        }
    }

    protected abstract void onAuthTokenExpired(ErrorBody errorBody);

    protected abstract void onLogout();

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
}
