package com.example.minimoneybox.network.impl.retrofit;

import com.example.minimoneybox.network.data.InvestorProductsResponse;
import com.example.minimoneybox.network.data.AddOneOffResponse;
import com.example.minimoneybox.network.data.UserLoginResponse;
import com.example.minimoneybox.network.AuthTokenSupplier;
import com.example.minimoneybox.network.HttpClient;
import com.example.minimoneybox.network.data.NetworkResponse;

import javax.inject.Inject;

import io.reactivex.Observable;
import retrofit2.Retrofit;

public class RetrofitHttpClientImpl implements HttpClient {
    private final Retrofit noAuthRetrofit;
    private final Retrofit authRetrofit;
    private final ResponseAdapter responseAdapter;
    private final AuthTokenSupplier authTokenSupplier;

    @Inject
    public RetrofitHttpClientImpl(Retrofit noAuthRetrofit, Retrofit authRetrofit, ResponseAdapter responseAdapter, AuthTokenSupplier authTokenSupplier) {
        this.noAuthRetrofit = noAuthRetrofit;
        this.authRetrofit = authRetrofit;
        this.responseAdapter = responseAdapter;
        this.authTokenSupplier = authTokenSupplier;
    }


    @Override
    public Observable<NetworkResponse<UserLoginResponse>> login(String username, String password, String loginFullName) {
        return noAuthRetrofit.create(NetworkAPI.class)
                .login(username, password)
                .map(responseAdapter::adapt);
    }

    @Override
    public Observable<NetworkResponse<InvestorProductsResponse>> getInvestorProducts() {
        return authRetrofit.create(NetworkAPI.class)
                .getInvestorProducts()
                .map(responseAdapter::adapt);
    }

    @Override
    public Observable<NetworkResponse<AddOneOffResponse>> addOneOff(int productId, double amount) {
        return authRetrofit.create(NetworkAPI.class)
                .issueOneOffPayments(amount, productId)
                .map(responseAdapter::adapt);
    }

    @Override
    public void setAuthToken(String token) {
        authTokenSupplier.setToken(token);
    }
}
