package com.example.minimoneybox.viewmodel;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.minimoneybox.MoneyBoxManager;
import com.example.minimoneybox.StubData;
import com.example.minimoneybox.model.data.ProductResponse;
import com.example.minimoneybox.model.repository.Repository;
import com.example.minimoneybox.network.data.ErrorBody;
import com.example.minimoneybox.network.data.NetworkResponse;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.net.HttpURLConnection;

import io.reactivex.Observable;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.schedulers.Schedulers;

@RunWith(RobolectricTestRunner.class)
public class ProductDetailsViewModelTest {
    private static final int VALID_PRODUCT_ID = 6136;
    private static final int INVALID_PRODUCT_ID = 200;
    private static final ProductResponse PRODUCT_RESPONSE = StubData.newInvestorProducts().getProductResponses()
            .stream()
            .filter(productResponse -> Integer.valueOf(VALID_PRODUCT_ID).equals(productResponse.getId()))
            .findFirst()
            .get();
    private static final double MONEY_BOX_RESPONSE = 100d;
    private static final double VALID_MONEY_ADD = 10d;
    private static final double INVALID_MONEY_ADD = -10d;

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    @Mock
    MoneyBoxManager moneyBoxManager;

    @Mock
    Repository repository;

    private ProductDetailsViewModel viewModel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(schedulerCallable -> Schedulers.trampoline());

        Mockito.when(repository.getProductResponse(VALID_PRODUCT_ID)).thenReturn(PRODUCT_RESPONSE);
        Mockito.when(repository.getProductResponse(INVALID_PRODUCT_ID)).thenReturn(null);

        Mockito.when(moneyBoxManager.addOneOff(VALID_PRODUCT_ID, VALID_MONEY_ADD)).thenReturn(Observable.just(new NetworkResponse<Double>(HttpURLConnection.HTTP_OK, MONEY_BOX_RESPONSE, null)));
        Mockito.when(moneyBoxManager.addOneOff(VALID_PRODUCT_ID, INVALID_MONEY_ADD)).thenReturn(Observable.just(new NetworkResponse<Double>(HttpURLConnection.HTTP_BAD_REQUEST, null, new ErrorBody())));

        viewModel = new ProductDetailsViewModel(moneyBoxManager, repository);
    }

    @Test
    public void testThatViewModelLoadedProductProperly() {
        viewModel.loadProduct(VALID_PRODUCT_ID);

        Assertions.assertThat(viewModel.AccountName.getValue()).isEqualTo(PRODUCT_RESPONSE.getProduct().getFriendlyName());
        Assertions.assertThat(viewModel.PlanValue.getValue()).isEqualTo(PRODUCT_RESPONSE.getPlanValue());
        Assertions.assertThat(viewModel.MoneyBox.getValue()).isEqualTo(PRODUCT_RESPONSE.getMoneybox());
    }

    @Test(expected = NullPointerException.class)
    public void testThatViewModelThrowExceptionWhenLoadedWithWrongProductId() {
        viewModel.loadProduct(INVALID_PRODUCT_ID);
    }
}