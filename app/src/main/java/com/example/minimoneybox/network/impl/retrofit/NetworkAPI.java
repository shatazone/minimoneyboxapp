package com.example.minimoneybox.network.impl.retrofit;

import com.example.minimoneybox.network.data.InvestorProductsResponse;
import com.example.minimoneybox.network.data.AddOneOffResponse;
import com.example.minimoneybox.network.data.UserLoginResponse;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface NetworkAPI {
    @FormUrlEncoded
    @POST("/users/login")
    Observable<Response<UserLoginResponse>> login(@Field("Email") String email, @Field("Password") String password);

    @GET("/investorproducts")
    Observable<Response<InvestorProductsResponse>> getInvestorProducts();

    @FormUrlEncoded
    @POST("/oneoffpayments")
    Observable<Response<AddOneOffResponse>> issueOneOffPayments(@Field("Amount") double amount, @Field("InvestorProductId") int investorProductId);
}
