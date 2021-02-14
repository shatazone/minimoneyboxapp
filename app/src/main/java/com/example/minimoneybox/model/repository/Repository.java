package com.example.minimoneybox.model.repository;

import com.example.minimoneybox.model.data.ProductResponse;
import com.example.minimoneybox.network.data.InvestorProductsResponse;
import com.example.minimoneybox.network.data.UserLoginResponse;

public interface Repository {
    UserLoginResponse getUserLogin();
    String getLoginFullName();
    InvestorProductsResponse getInvestorProducts();
    ProductResponse getProductResponse(int id);

    void setUserLogin(UserLoginResponse userLogin);
    void setLoginFullName(String loginFullName);
    void setInvestorProducts(InvestorProductsResponse investorProducts);
    void updateProductResponse(int id, ProductResponse productResponse);

    boolean wipe();
}
