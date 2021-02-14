package com.example.minimoneybox.viewmodel;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

import androidx.core.util.Pair;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.minimoneybox.BuildConfig;
import com.example.minimoneybox.MoneyBoxManager;
import com.example.minimoneybox.R;
import com.example.minimoneybox.misc.SingleLiveEvent;
import com.example.minimoneybox.network.data.InvestorProductsResponse;
import com.example.minimoneybox.network.data.NetworkResponse;
import com.example.minimoneybox.network.data.UserLoginResponse;
import com.example.minimoneybox.network.data.ValidationError;
import com.example.minimoneybox.view.ui.Request;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


@HiltViewModel
public class LoginViewModel extends AndroidViewModel {
    private static final String TAG = LoginViewModel.class.getSimpleName();

    public final MutableLiveData<String> EmailAddress = new MutableLiveData<>();
    public final MutableLiveData<String> Password = new MutableLiveData<>();
    public final MutableLiveData<String> DisplayName = new MutableLiveData<>();

    public final MutableLiveData<String> EmailAddressError = new MutableLiveData<>();
    public final MutableLiveData<String> PasswordError = new MutableLiveData<>();
    public final MutableLiveData<String> DisplayNameError = new MutableLiveData<>();
    
    public final SingleLiveEvent<Request<NetworkResponse<Pair<UserLoginResponse, InvestorProductsResponse>>>> LoginResponse = new SingleLiveEvent<>();

    private final MoneyBoxManager mMoneyBoxManager;
    private Disposable mApiDisposer;

    @Inject
    public LoginViewModel(Application application, MoneyBoxManager moneyBoxManager) {
        super(application);
        this.mMoneyBoxManager = moneyBoxManager;

        // Pre-fill credentials for convenience
        if (BuildConfig.DEBUG) {
            EmailAddress.postValue(BuildConfig.TEST_USERNAME);
            Password.postValue(BuildConfig.TEST_PASSWORD);
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        if (mApiDisposer != null && !mApiDisposer.isDisposed()) {
            mApiDisposer.dispose();
        }
    }

    public synchronized boolean login() {
        if (!validate()) {
            return false;
        }

        LoginResponse.postValue(Request.loading());

        mApiDisposer = mMoneyBoxManager.login(EmailAddress.getValue(), Password.getValue(), DisplayName.getValue())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if(response.getErrorBody() != null && response.getErrorBody().getValidationErrorList() != null) {
                        for (ValidationError validationError : response.getErrorBody().getValidationErrorList()) {
                            applyValidationError(validationError);
                        }
                    }

                    LoginResponse.postValue(Request.success(response));
                }, throwable -> LoginResponse.postValue(Request.failed(throwable)));

        return true;
    }

    private void applyValidationError(ValidationError validationError) {
        String name = validationError.getName();

        if(name != null) {
            switch (name) {
                case "Email":
                    EmailAddressError.setValue(validationError.getMessage());
                    break;

                case "Password":
                    PasswordError.setValue(validationError.getMessage());
                    break;

                default:
                    Log.e(TAG, "Unrecognized validation error name '" + name + "'");
            }
        }
    }

    private void checkMandatoryField(LiveData<String> source, MutableLiveData<String> error) {
        if (TextUtils.isEmpty(source.getValue())) {
            error.setValue(getApplication().getString(R.string.field_is_mandatory));
        }
    }

    public void validateEmail(boolean hasFocus) {
        if(hasFocus || TextUtils.isEmpty(EmailAddress.getValue())) {
            EmailAddressError.setValue(null);
        } else if(!Patterns.EMAIL_ADDRESS.matcher(EmailAddress.getValue()).matches()) {
            EmailAddressError.setValue(getApplication().getString(R.string.invalid_email));
        }
    }

    public void validatePassword(boolean hasFocus) {
        if(hasFocus || TextUtils.isEmpty(Password.getValue())) {
            PasswordError.setValue(null);
        }
    }

    public void validateDisplayName(boolean hasFocus) {
        DisplayNameError.setValue(null);
    }

    boolean validate() {
        validateEmail(false);
        validatePassword(false);
        validateDisplayName(false);

        checkMandatoryField(EmailAddress, EmailAddressError);
        checkMandatoryField(Password, PasswordError);

        return TextUtils.isEmpty(EmailAddressError.getValue())
                && TextUtils.isEmpty(PasswordError.getValue())
                && TextUtils.isEmpty(DisplayNameError.getValue());
    }
}
