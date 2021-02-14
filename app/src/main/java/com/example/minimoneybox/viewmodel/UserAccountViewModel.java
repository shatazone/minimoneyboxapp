package com.example.minimoneybox.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.minimoneybox.MoneyBoxManager;
import com.example.minimoneybox.misc.SingleLiveEvent;
import com.example.minimoneybox.model.data.ProductResponse;
import com.example.minimoneybox.model.repository.Repository;
import com.example.minimoneybox.network.data.NetworkResponse;
import com.example.minimoneybox.view.ui.Request;

import org.apache.commons.lang3.Validate;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class UserAccountViewModel extends ViewModel {
    public final MutableLiveData<String> LoginFullName = new MutableLiveData<>();
    public final MutableLiveData<Double> TotalPlanValue = new MutableLiveData<>();
    public final MutableLiveData<List<ProductResponse>> ProductResponseList = new MutableLiveData<>();

    public final SingleLiveEvent<ProductResponseModel> SelectedProduct = new SingleLiveEvent<>();
    public final SingleLiveEvent<Request<NetworkResponse<?>>> RefreshProductListResponse = new SingleLiveEvent<>();
    public final SingleLiveEvent<Request<NetworkResponse<?>>> AddMoneyBoxRequest = new SingleLiveEvent<>();

    private final Repository repository;
    private final MoneyBoxManager moneyBoxManager;
    private Disposable refreshProductsDisposer;
    private Disposable addToMoneyBoxDisposer;

    @Inject
    public UserAccountViewModel(MoneyBoxManager moneyBoxManager, Repository repository) {
        this.moneyBoxManager = moneyBoxManager;
        this.repository = repository;

        loadFromRepository();
    }

    public void selectProduct(int id) {
        ProductResponse productResponse = repository.getProductResponse(id);
        ProductResponseModel productResponseModel = new ProductResponseModel(id);
        productResponseModel.AccountName.postValue(productResponse.getProduct().getFriendlyName());
        productResponseModel.PlanValue.postValue(productResponse.getPlanValue());
        productResponseModel.MoneyBox.postValue(productResponse.getMoneybox());
        SelectedProduct.setValue(productResponseModel);
    }

    public synchronized void refreshProductResponseList() {
        RefreshProductListResponse.postValue(Request.loading());

        refreshProductsDisposer = moneyBoxManager.getInvestorProducts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> RefreshProductListResponse.postValue(Request.success(response)), throwable -> RefreshProductListResponse.postValue(Request.failed(throwable)));
    }

    public synchronized void addToMoneybox(double amount) {
        Validate.notNull(SelectedProduct.getValue(), "Product is not selected yet");

        AddMoneyBoxRequest.postValue(Request.loading());
        addToMoneyBoxDisposer = moneyBoxManager.addOneOff(SelectedProduct.getValue().ID, amount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(response -> {
                    if (response.isSuccessful()) {
                        SelectedProduct.getValue().MoneyBox.postValue(response.getBody());
                        rebuildProductResultList();
                    }
                })
                .subscribe(response -> AddMoneyBoxRequest.postValue(Request.success(response)), throwable -> AddMoneyBoxRequest.postValue(Request.failed(throwable)));
    }

    public void logout() {
        moneyBoxManager.logout(true);
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        if(refreshProductsDisposer != null && !refreshProductsDisposer.isDisposed()) {
            refreshProductsDisposer.dispose();
        }

        if(addToMoneyBoxDisposer != null && !addToMoneyBoxDisposer.isDisposed()) {
            addToMoneyBoxDisposer.dispose();
        }
    }

    void loadFromRepository() {
        LoginFullName.postValue(repository.getLoginFullName());
        TotalPlanValue.postValue(repository.getInvestorProducts().getTotalPlanValue());
        ProductResponseList.postValue(repository.getInvestorProducts().getProductResponses());
    }

    void rebuildProductResultList() {
        ProductResponseList.postValue(repository.getInvestorProducts().getProductResponses());
    }

    public static class ProductResponseModel {
        public final int ID;
        public final MutableLiveData<String> AccountName = new MutableLiveData<>();
        public final MutableLiveData<Double> PlanValue = new MutableLiveData<>();
        public final MutableLiveData<Double> MoneyBox = new MutableLiveData<>();

        public ProductResponseModel(int ID) {
            this.ID = ID;
        }
    }
}