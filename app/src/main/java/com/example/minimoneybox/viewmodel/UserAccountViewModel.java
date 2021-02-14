package com.example.minimoneybox.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.minimoneybox.MoneyBoxManager;
import com.example.minimoneybox.view.ui.Request;
import com.example.minimoneybox.model.data.ProductResponse;
import com.example.minimoneybox.model.repository.Repository;
import com.example.minimoneybox.network.data.NetworkResponse;

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

    public final MutableLiveData<Request<NetworkResponse<?>>> RefreshProductListResponse = new MutableLiveData<>();

    private final Repository repository;
    private final MoneyBoxManager moneyBoxManager;
    private Disposable apiCallDisposer;

    @Inject
    public UserAccountViewModel(MoneyBoxManager moneyBoxManager, Repository repository) {
        this.moneyBoxManager = moneyBoxManager;
        this.repository = repository;

        loadFromRepository();
    }

    public synchronized void refreshProductResponseList() {
        RefreshProductListResponse.postValue(Request.loading());

        apiCallDisposer = moneyBoxManager.getInvestorProducts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> RefreshProductListResponse.postValue(Request.success(response)), throwable -> RefreshProductListResponse.postValue(Request.failed(throwable)));
    }

    public void logout() {
        moneyBoxManager.logout(true);
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        if(apiCallDisposer != null && !apiCallDisposer.isDisposed()) {
            apiCallDisposer.dispose();
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
}