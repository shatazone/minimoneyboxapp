package com.example.minimoneybox.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.minimoneybox.BuildConfig;
import com.example.minimoneybox.MoneyBoxManager;
import com.example.minimoneybox.misc.SingleLiveEvent;
import com.example.minimoneybox.network.data.NetworkResponse;
import com.example.minimoneybox.view.ui.Request;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


@HiltViewModel
public class LoginViewModel extends ViewModel {
    public final SingleLiveEvent<Request<NetworkResponse>> LoginResponse = new SingleLiveEvent<>();

    private final MoneyBoxManager mMoneyBoxManager;
    private final LoginForm mLoginForm;
    private Disposable mApiDisposer;

    @Inject
    public LoginViewModel(MoneyBoxManager moneyBoxManager) {
        this.mMoneyBoxManager = moneyBoxManager;
        this.mLoginForm = new LoginForm();

        // Pre-fill credentials for convenience
        if (BuildConfig.DEBUG) {
            mLoginForm.EmailAddress.postValue(BuildConfig.TEST_USERNAME);
            mLoginForm.Password.postValue(BuildConfig.TEST_PASSWORD);
        }
    }

    public LoginForm getLoginForm() {
        return mLoginForm;
    }

    public synchronized boolean login() {
        if (!mLoginForm.validate()) {
            return false;
        }

        LoginResponse.postValue(Request.loading());

        mApiDisposer = mMoneyBoxManager.login(mLoginForm.EmailAddress.getValue(), mLoginForm.Password.getValue(), mLoginForm.DisplayName.getValue())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> LoginResponse.postValue(Request.success(response)), throwable -> LoginResponse.postValue(Request.failed(throwable)));

        return true;
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        if (mApiDisposer != null && !mApiDisposer.isDisposed()) {
            mApiDisposer.dispose();
        }
    }
}
