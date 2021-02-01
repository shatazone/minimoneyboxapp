package com.example.minimoneybox.viewmodel;

import android.content.Context;
import android.text.TextUtils;
import android.util.Patterns;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.minimoneybox.BuildConfig;
import com.example.minimoneybox.MoneyBoxManager;
import com.example.minimoneybox.R;
import com.example.minimoneybox.network.Callback;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


@HiltViewModel
public class LoginViewModel extends ViewModel {
    public final MutableLiveData<String> EmailAddress = new MutableLiveData<>();
    public final MutableLiveData<String> Password = new MutableLiveData<>();
    public final MutableLiveData<String> DisplayName = new MutableLiveData<>();

    public final ObservableField<String> EmailAddressError = new ObservableField();
    public final ObservableField<String> PasswordError = new ObservableField();
    public final ObservableField<String> DisplayNameError = new ObservableField();

    private final Context mContext;
    private final MoneyBoxManager mMoneyBoxManager;
    private Disposable mApiDisposer;

    @Inject
    public LoginViewModel(@ApplicationContext Context context, MoneyBoxManager moneyBoxManager) {
        this.mContext = context;
        this.mMoneyBoxManager = moneyBoxManager;

        // Pre-fill credentials for convenience
        if (BuildConfig.DEBUG) {
            EmailAddress.postValue(BuildConfig.TEST_USERNAME);
            Password.postValue(BuildConfig.TEST_PASSWORD);
        }
    }

    public synchronized boolean login(Callback callback) {
        if (!validateForm()) {
            return false;
        }

        callback.onStarted();;

        mApiDisposer = mMoneyBoxManager.login(EmailAddress.getValue(), Password.getValue(), DisplayName.getValue())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> callback.onResponse(response), throwable -> callback.onFailure(throwable));

        return true;
    }

    private void checkMandatoryField(LiveData<String> source, ObservableField<String> error) {
        if (TextUtils.isEmpty(source.getValue())) {
            error.set(mContext.getString(R.string.field_is_mandatory));
        }
    }

    public void validateEmail() {
        String email = EmailAddress.getValue();
        if (TextUtils.isEmpty(email) || Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            EmailAddressError.set(null);
        } else {
            EmailAddressError.set(mContext.getString(R.string.invalid_email));
        }
    }

    public void validatePassword() {
        PasswordError.set(null);
    }

    public void validateDisplayName() {
        DisplayNameError.set(null);
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        if (mApiDisposer != null && !mApiDisposer.isDisposed()) {
            mApiDisposer.dispose();
        }
    }

    public boolean validateForm() {
        validateEmail();
        validatePassword();
        validateDisplayName();

        checkMandatoryField(EmailAddress, EmailAddressError);
        checkMandatoryField(Password, PasswordError);

        return TextUtils.isEmpty(EmailAddressError.get())
                && TextUtils.isEmpty(PasswordError.get())
                && TextUtils.isEmpty(DisplayNameError.get());
    }
}
