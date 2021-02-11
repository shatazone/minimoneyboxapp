package com.example.minimoneybox.viewmodel;

import android.content.Context;
import android.text.TextUtils;
import android.util.Patterns;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.minimoneybox.R;

public class LoginForm {
    public final MutableLiveData<String> EmailAddress = new MutableLiveData<>();
    public final MutableLiveData<String> Password = new MutableLiveData<>();
    public final MutableLiveData<String> DisplayName = new MutableLiveData<>();

    public final ObservableField<String> EmailAddressError = new ObservableField();
    public final ObservableField<String> PasswordError = new ObservableField();
    public final ObservableField<String> DisplayNameError = new ObservableField();

    private Context mContext;

    public void setContext(Context context) {
        this.mContext = context;
    }

    private void checkMandatoryField(LiveData<String> source, ObservableField<String> error) {
        if (TextUtils.isEmpty(source.getValue())) {
            error.set(mContext.getString(R.string.field_is_mandatory));
        }
    }

    public void validateEmail(boolean hasFocus) {
        if(hasFocus || EmailAddress.getValue().isEmpty()) {
            EmailAddressError.set(null);
        } else if(!Patterns.EMAIL_ADDRESS.matcher(EmailAddress.getValue()).matches()) {
            EmailAddressError.set(mContext.getString(R.string.invalid_email));
        }
    }

    public void validatePassword(boolean hasFocus) {
        if(hasFocus || Password.getValue().isEmpty()) {
            PasswordError.set(null);
        }
    }

    public void validateDisplayName(boolean hasFocus) {
        DisplayNameError.set(null);
    }

    public boolean validate() {
        validateEmail(false);
        validatePassword(false);
        validateDisplayName(false);

        checkMandatoryField(EmailAddress, EmailAddressError);
        checkMandatoryField(Password, PasswordError);

        return TextUtils.isEmpty(EmailAddressError.get())
                && TextUtils.isEmpty(PasswordError.get())
                && TextUtils.isEmpty(DisplayNameError.get());
    }
}
