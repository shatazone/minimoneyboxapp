package com.example.minimoneybox;

import android.content.Context;

import androidx.core.util.Pair;

import com.example.minimoneybox.model.data.ProductResponse;
import com.example.minimoneybox.model.repository.Repository;
import com.example.minimoneybox.network.HttpClient;
import com.example.minimoneybox.network.data.ErrorBody;
import com.example.minimoneybox.network.data.InvestorProductsResponse;
import com.example.minimoneybox.network.data.NetworkResponse;
import com.example.minimoneybox.network.data.UserLoginResponse;
import com.example.minimoneybox.view.ui.MoneyBoxEventBroadcastReceiver;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

public class MoneyBoxManager {
    private final Context mContext;
    private final Repository repository;
    private final HttpClient httpClient;

    @Inject
    public MoneyBoxManager(Context context, HttpClient httpClient, Repository repository) {
        this.mContext = context;
        this.httpClient = httpClient;
        this.repository = repository;
    }

    public Observable<NetworkResponse<Pair<UserLoginResponse, InvestorProductsResponse>>> login(String username, String password, String loginFullName) {
        return httpClient.login(username, password, loginFullName)
                .flatMap((Function<NetworkResponse<UserLoginResponse>, ObservableSource<NetworkResponse<InvestorProductsResponse>>>) loginNetworkResponse -> {
                    if(loginNetworkResponse.isSuccessful()) {
                        httpClient.setAuthToken(loginNetworkResponse.getBody().getSession().getBearerToken());  // We found a new login token => update the current token that we have
                        return httpClient.getInvestorProducts();
                    } else {
                        return Observable.just(new NetworkResponse<>());
                    }
                }, (loginNetworkResponse, investorProductsResponse) -> {
                    Pair<UserLoginResponse, InvestorProductsResponse> pairResults = Pair.create(loginNetworkResponse.getBody(), investorProductsResponse.getBody());
                    int code = investorProductsResponse.isSuccessful() ? investorProductsResponse.getCode() : loginNetworkResponse.getCode();
                    ErrorBody errorBody = investorProductsResponse.isSuccessful() ? investorProductsResponse.getErrorBody() : loginNetworkResponse.getErrorBody();

                    if(loginNetworkResponse.isSuccessful() && investorProductsResponse.isSuccessful()) {
                        // Everything looks good, time to save the login data
                        repository.setLoginFullName(loginFullName);
                        repository.setUserLogin(loginNetworkResponse.getBody());
                        repository.setInvestorProducts(investorProductsResponse.getBody());
                    }

                    return new NetworkResponse(code, pairResults, errorBody);
                });
    }

    public Observable<NetworkResponse<InvestorProductsResponse>> getInvestorProducts() {
        return httpClient.getInvestorProducts()
                .doOnNext(response -> {
                    if (response.isSuccessful()) {
                        repository.setInvestorProducts(response.getBody());
                    }
                });
    }

    public Observable<NetworkResponse<Double>> addOneOff(int productId, double amount) {
        return httpClient.addOneOff(productId, amount)
                .doOnNext(response -> {
                    if (response.isSuccessful()) {
                        ProductResponse productResponse = repository.getProductResponse(productId);
                        productResponse.setMoneybox(response.getBody().getMoneyBox());
                        repository.updateProductResponse(productId, productResponse);
                    }
                })
                .map(response -> {
                    Double moneyBoxValue = response.getBody() == null ? null : response.getBody().getMoneyBox();
                    return new NetworkResponse<>(response.getCode(), moneyBoxValue, response.getErrorBody());
                });
    }

    public boolean isLoggedIn() {
        // TODO Consider checking against a boolean, this is a heavy operation
        return repository.getUserLogin() != null;
    }

    public void logout(boolean broadcast) {
        if(isLoggedIn()) {
            repository.wipe();

            if(broadcast) {
                MoneyBoxEventBroadcastReceiver.broadcastLogout(mContext);
            }
        }
    }
}
