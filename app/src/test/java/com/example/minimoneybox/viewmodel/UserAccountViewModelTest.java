package com.example.minimoneybox.viewmodel;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.minimoneybox.MoneyBoxManager;
import com.example.minimoneybox.StubData;
import com.example.minimoneybox.model.repository.Repository;
import com.example.minimoneybox.network.data.InvestorProductsResponse;
import com.jraska.livedata.TestObserver;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.schedulers.Schedulers;

@RunWith(RobolectricTestRunner.class)
public class UserAccountViewModelTest {
    private static final String DISPLAY_NAME = "my display name";
    private static final InvestorProductsResponse INVESTOR_PRODUCTS = StubData.newInvestorProducts();
    private static final InvestorProductsResponse INVESTOR_PRODUCTS_2 = StubData.newInvestorProducts2();

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    @Mock
    MoneyBoxManager moneyBoxManager;

    @Mock
    Repository repository;

    private UserAccountViewModel viewModel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(schedulerCallable -> Schedulers.trampoline());

        Mockito.when(repository.getLoginFullName()).thenReturn(DISPLAY_NAME);
        Mockito.when(repository.getInvestorProducts()).thenReturn(INVESTOR_PRODUCTS);

        viewModel = new UserAccountViewModel(moneyBoxManager, repository);
    }

    @Test
    public void testDataFilledCorrectly() throws InterruptedException {
        viewModel.fillValues(DISPLAY_NAME, INVESTOR_PRODUCTS.getTotalPlanValue(), INVESTOR_PRODUCTS.getProductResponses());
        TestObserver.test(viewModel.LoginFullName).awaitValue().assertValue(DISPLAY_NAME);
        TestObserver.test(viewModel.TotalPlanValue).awaitValue().assertValue(INVESTOR_PRODUCTS.getTotalPlanValue());
        TestObserver.test(viewModel.ProductResponseList).awaitValue().assertValue(INVESTOR_PRODUCTS.getProductResponses());
    }

    @Test
    public void testProductListRebuiltFromRepository() throws InterruptedException {
        Mockito.when(repository.getInvestorProducts()).thenReturn(INVESTOR_PRODUCTS_2);
        viewModel.rebuildProductResultList();
        TestObserver.test(viewModel.ProductResponseList).awaitValue().assertValue(INVESTOR_PRODUCTS_2.getProductResponses());
    }
}