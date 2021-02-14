package com.example.minimoneybox.network;

import com.example.minimoneybox.network.data.InvestorProductsResponse;
import com.example.minimoneybox.network.data.AddOneOffResponse;
import com.example.minimoneybox.network.data.UserLoginResponse;
import com.example.minimoneybox.network.data.NetworkResponse;

import io.reactivex.Observable;

public interface HttpClient {
    Observable<NetworkResponse<UserLoginResponse> >login(String username, String password, String loginFullName);

    Observable<NetworkResponse<InvestorProductsResponse>> getInvestorProducts();

    Observable<NetworkResponse<AddOneOffResponse>> addOneOff(int productId, double amount);

    void setAuthToken(String token);
}
