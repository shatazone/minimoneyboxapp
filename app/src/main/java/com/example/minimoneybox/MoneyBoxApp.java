package com.example.minimoneybox;

import android.app.Application;

import com.orhanobut.hawk.Hawk;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class MoneyBoxApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Hawk.init(this).build();
    }
}
