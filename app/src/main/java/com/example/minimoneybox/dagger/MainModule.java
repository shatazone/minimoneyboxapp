package com.example.minimoneybox.dagger;

import android.content.Context;

import com.example.minimoneybox.MoneyBoxManager;
import com.example.minimoneybox.misc.JsonSerializer;
import com.example.minimoneybox.model.repository.Repository;
import com.example.minimoneybox.network.AuthTokenExpiredConsumer;
import com.example.minimoneybox.network.HttpClient;
import com.example.minimoneybox.view.ui.BroadcastAuthTokenExpiryConsumer;
import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class MainModule {

    @Singleton
    @Provides
    public static MoneyBoxManager provideMoneyManager(@ApplicationContext Context context, HttpClient httpClient, Repository repository) {
        return new MoneyBoxManager(context, httpClient, repository);
    }

    @Provides
    public static JsonSerializer provideJsonSerializer(Gson gson) {
        return new JsonSerializer(gson);
    }

    @Provides
    public static AuthTokenExpiredConsumer provideAuthTokenExpiredConsumer(@ApplicationContext Context context) {
        return new BroadcastAuthTokenExpiryConsumer(context);
    }
}
