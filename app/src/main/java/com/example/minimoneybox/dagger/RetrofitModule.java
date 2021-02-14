package com.example.minimoneybox.dagger;

import com.example.minimoneybox.BuildConfig;
import com.example.minimoneybox.dagger.annotations.Authentication;
import com.example.minimoneybox.dagger.annotations.NoAuthentication;
import com.example.minimoneybox.network.AuthTokenSupplier;
import com.example.minimoneybox.network.AuthTokenExpiredConsumer;
import com.example.minimoneybox.network.HttpClient;
import com.example.minimoneybox.network.impl.retrofit.ResponseAdapter;
import com.example.minimoneybox.network.impl.retrofit.RetrofitHttpClientImpl;
import com.example.minimoneybox.network.impl.retrofit.interceptor.AuthInterceptor;
import com.example.minimoneybox.network.impl.retrofit.interceptor.HeaderInterceptor;
import com.google.gson.Gson;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class RetrofitModule {
    @Provides
    public static Gson provideGson() {
        return new Gson();
    }

    @Provides
    public static ResponseAdapter provideResponseAdapter(Gson gson) {
        return new ResponseAdapter(gson);
    }

    @Provides
    public static HttpLoggingInterceptor providerLoggingHeaderInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }

    @Provides
    public static HeaderInterceptor provideHeaderInterceptor() {
        return new HeaderInterceptor();
    }

    @Provides
    public static AuthInterceptor provideAuthInterceptor(AuthTokenSupplier tokenSupplier, ResponseAdapter responseAdapter, AuthTokenExpiredConsumer authTokenExpiredConsumer) {
        return new AuthInterceptor(tokenSupplier, responseAdapter, authTokenExpiredConsumer);
    }

    @Authentication
    @Provides
    public static OkHttpClient provideAuthOkHttpClient(HeaderInterceptor headerInterceptor, HttpLoggingInterceptor loggingInterceptor, AuthInterceptor authInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(headerInterceptor)
                .addInterceptor(authInterceptor)
                .addInterceptor(loggingInterceptor)
                .build();
    }

    @NoAuthentication
    @Provides
    public static OkHttpClient provideOkHttpClient(HeaderInterceptor headerInterceptor, HttpLoggingInterceptor loggingInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(headerInterceptor)
                .addInterceptor(loggingInterceptor)
                .build();
    }

    @Authentication
    @Provides
    public static Retrofit provideAuthRetrofit(@Authentication OkHttpClient okHttpClient, Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @NoAuthentication
    @Provides
    public static Retrofit provideNoAuthRetrofit(@NoAuthentication OkHttpClient okHttpClient, Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    @Provides
    public static HttpClient provideNetworkManager(@NoAuthentication Retrofit noAuthRetrofit,
                                                   @Authentication Retrofit authRetrofit,
                                                   ResponseAdapter responseAdapter, AuthTokenSupplier authTokenSupplier) {
        return new RetrofitHttpClientImpl(noAuthRetrofit, authRetrofit, responseAdapter, authTokenSupplier);
    }
}