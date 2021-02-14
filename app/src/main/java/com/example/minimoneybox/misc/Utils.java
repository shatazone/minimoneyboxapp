package com.example.minimoneybox.misc;

import android.content.Context;
import android.text.TextUtils;

import com.example.minimoneybox.R;
import com.example.minimoneybox.network.data.ErrorBody;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import io.reactivex.Observable;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Utils {
    private static Set<Class<?>> CONNECTION_ERROR_SET = new HashSet<>(Arrays.asList(UnknownHostException.class, SocketTimeoutException.class, UnknownHostException.class));

    public static String getTitleFor(Context context, Throwable throwable) {
        return context.getString(R.string.error_generic);
    }

    public static String getMessageFor(Context context, Throwable throwable) {
        if (CONNECTION_ERROR_SET.contains(throwable) || throwable instanceof IOException) {
            return context.getString(R.string.error_check_connection);
        }

        return throwable.getMessage();
    }

    public static String getTitleFor(ErrorBody errorBody) {
        return errorBody.getName();
    }

    public static String getMessageFor(ErrorBody errorBody) {
        if (errorBody.getValidationErrorList() != null && !errorBody.getValidationErrorList().isEmpty()) {
            return errorBody.getMessage() + "\n" + TextUtils.join("\n", Observable.fromIterable(errorBody.getValidationErrorList())
                    .map(validationError -> validationError.getMessage())
                    .toList()
                    .blockingGet());
        } else {
            return errorBody.getMessage();
        }
    }
}
