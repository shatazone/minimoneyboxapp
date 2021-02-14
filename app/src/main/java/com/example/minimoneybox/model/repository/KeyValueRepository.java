package com.example.minimoneybox.model.repository;

import com.example.minimoneybox.network.data.InvestorProductsResponse;
import com.example.minimoneybox.model.data.ProductResponse;
import com.example.minimoneybox.network.data.UserLoginResponse;
import com.example.minimoneybox.model.storage.KeyValueStorage;

import org.apache.commons.lang3.Validate;

import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.Observable;

public class KeyValueRepository implements Repository {

    private static final String KEY_USER_LOGIN = "userLogin";
    private static final String KEY_LOGIN_FULL_NAME = "loginFullName";
    private static final String KEY_INVESTOR_PRODUCTS = "investorProducts";

    private final KeyValueStorage storage;

    @Inject
    public KeyValueRepository(KeyValueStorage keyValueStorage) {
        this.storage = keyValueStorage;
    }

    @Override
    public UserLoginResponse getUserLogin() {
        return storage.get(KEY_USER_LOGIN);
    }

    @Override
    public String getLoginFullName() {
        return storage.get(KEY_LOGIN_FULL_NAME);
    }

    @Override
    public InvestorProductsResponse getInvestorProducts() {
        return storage.get(KEY_INVESTOR_PRODUCTS);
    }

    @Override
    public void setUserLogin(UserLoginResponse userLoginResponse) {
        storage.put(KEY_USER_LOGIN, userLoginResponse);
    }

    @Override
    public void setLoginFullName(String loginFullName) {
        storage.put(KEY_LOGIN_FULL_NAME, loginFullName);
    }

    @Override
    public void setInvestorProducts(InvestorProductsResponse investorProductsResponse) {
        storage.put(KEY_INVESTOR_PRODUCTS, investorProductsResponse);
    }

    @Override
    public void updateProductResponse(int id, ProductResponse productResponse) {
        ProductResponse match = getProductResponse(id);
        Validate.notNull(match, "No ProductResponse with matching id was found");

        if(productResponse != null) {
            productResponse.setId(id);
        }

        InvestorProductsResponse investorProductsResponse = getInvestorProducts();
        int index = investorProductsResponse.getProductResponses().indexOf(match);
        investorProductsResponse.getProductResponses().set(index, productResponse);
        setInvestorProducts(investorProductsResponse);
    }

    @Override
    public ProductResponse getProductResponse(int id) {
        return Observable.just(getInvestorProducts())
                .flatMap(investorProducts -> Observable.fromIterable(investorProducts.getProductResponses()))
                .filter(productResponse -> Objects.equals(productResponse.getId(), id))
                .blockingFirst(null);
    }

    @Override
    public boolean wipe() {
        return storage.deleteAll();
    }
}
