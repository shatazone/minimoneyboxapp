package com.example.minimoneybox.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.minimoneybox.MoneyBoxManager;
import com.example.minimoneybox.model.data.ProductResponse;
import com.example.minimoneybox.model.repository.Repository;
import com.example.minimoneybox.network.Callback;

import org.apache.commons.lang3.Validate;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class ProductDetailsViewModel extends ViewModel {
    public final MutableLiveData<String> AccountName = new MutableLiveData<>();
    public final MutableLiveData<Double> PlanValue = new MutableLiveData<>();
    public final MutableLiveData<Double> MoneyBox = new MutableLiveData<>();

    private final Repository repository;
    private final MoneyBoxManager moneyBoxManager;

    private Disposable apiCallDisposer;
    private ProductResponse productResponse;

    @Inject
    public ProductDetailsViewModel(MoneyBoxManager moneyBoxManager, Repository repository) {
        this.moneyBoxManager = moneyBoxManager;
        this.repository = repository;
    }

    public void loadProduct(int productId) {
        productResponse = repository.getProductResponse(productId);

        Validate.notNull(productResponse, "Unidentified product with id '" + productId + "'");

        AccountName.postValue(productResponse.getProduct().getFriendlyName());
        PlanValue.postValue(productResponse.getPlanValue());
        MoneyBox.postValue(productResponse.getMoneybox());
    }

    public synchronized void addToMoneybox(double amount, Callback<Double> callback) {
        Validate.notNull(productResponse, "Product is not loaded yet");

        callback.onStarted();
        apiCallDisposer = moneyBoxManager.addOneOff(productResponse.getId(), amount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response.isSuccessful()) {
                        MoneyBox.postValue(response.getBody());
                    }
                    callback.onResponse(response);
                }, throwable -> callback.onFailure(throwable));
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        if(apiCallDisposer != null && !apiCallDisposer.isDisposed()) {
            apiCallDisposer.dispose();
        }
    }
}