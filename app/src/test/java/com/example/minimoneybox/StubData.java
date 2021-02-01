package com.example.minimoneybox;

import com.example.minimoneybox.network.data.InvestorProductsResponse;
import com.example.minimoneybox.network.data.UserLoginResponse;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StubData {
    private static final Gson gson = new Gson();

    public InvestorProductsResponse newInvestorProducts() {
        return loadFromResources(InvestorProductsResponse.class, "/investor_products.json");
    }

    public InvestorProductsResponse newInvestorProducts2() {
        return loadFromResources(InvestorProductsResponse.class, "/investor_products2.json");
    }

    public static UserLoginResponse newUserLogin() {
        return loadFromResources(UserLoginResponse.class, "/user_login.json");
    }

    private static <T> T loadFromResources(Class<T> clazz, String path) {
        try {
            return gson.fromJson(new FileReader(StubData.class.getResource(path).getFile()), clazz);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
