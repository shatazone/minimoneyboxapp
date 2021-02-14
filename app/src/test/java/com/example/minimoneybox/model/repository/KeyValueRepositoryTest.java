package com.example.minimoneybox.model.repository;

import com.example.minimoneybox.StubData;
import com.example.minimoneybox.misc.JsonSerializer;
import com.example.minimoneybox.network.data.InvestorProductsResponse;
import com.example.minimoneybox.model.data.ProductResponse;
import com.example.minimoneybox.network.data.UserLoginResponse;
import com.example.minimoneybox.model.storage.impl.InMemoryMapStorage;
import com.google.gson.Gson;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

public class KeyValueRepositoryTest {
    private static final InvestorProductsResponse INVESTOR_PRODUCTS_RESPONSE = StubData.newInvestorProducts();
    private static final UserLoginResponse USER_LOGIN_RESPONSE = StubData.newUserLogin();
    private static final String fullName = "moneybox developer";

    private KeyValueRepository repository;

    @Before
    public void setUp() throws Exception {
        Gson gson = new Gson();
        // HashMapStorage has its own unit test, and thus it can be used as dependency for our test
        this.repository = new KeyValueRepository(new InMemoryMapStorage(new JsonSerializer(gson)));

        repository.setInvestorProducts(INVESTOR_PRODUCTS_RESPONSE);
        repository.setUserLogin(USER_LOGIN_RESPONSE);
    }

    @Test
    public void testUserLoginSetterAndGetterWithNonNullValues() {
        repository.setUserLogin(USER_LOGIN_RESPONSE);

        Assertions.assertThat(repository.getUserLogin())
                .isEqualTo(USER_LOGIN_RESPONSE);
    }

    @Test
    public void testUserLoginSetterAndGetterWithNullValue() {
        repository.setUserLogin(null);

        Assertions.assertThat(repository.getUserLogin())
                .isEqualTo(null);
    }

    @Test
    public void testInvestorProductsSetterAndGetter() {
        repository.setInvestorProducts(INVESTOR_PRODUCTS_RESPONSE);

        Assertions.assertThat(repository.getInvestorProducts())
                .isEqualTo(INVESTOR_PRODUCTS_RESPONSE);
    }

    @Test
    public void testInvestorProductsSetterAndGetterWithNullValue() {
        repository.setInvestorProducts(null);

        Assertions.assertThat(repository.getInvestorProducts())
                .isEqualTo(null);
    }

    @Test
    public void testFullNameSetterAndGetter() {
        repository.setLoginFullName(fullName);

        Assertions.assertThat(repository.getLoginFullName())
                .isEqualTo(fullName);
    }

    @Test
    public void testFullNameSetterAndGetterWithNullValue() {
        repository.setLoginFullName(null);

        Assertions.assertThat(repository.getLoginFullName())
                .isEqualTo(null);
    }

    @Test
    public void testUpdateProductsResponse() {
        repository.setInvestorProducts(INVESTOR_PRODUCTS_RESPONSE);

        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(6136);
        productResponse.setIsFavourite(false);
        productResponse.setCollectionDayMessage("Hello collection day!");

        repository.updateProductResponse(productResponse.getId(), productResponse);

        Assertions.assertThat(repository.getProductResponse(productResponse.getId())).isEqualTo(productResponse);
    }
}