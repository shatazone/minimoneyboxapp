package com.example.minimoneybox.view.ui;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Request<T> {
    private Status status;
    private T response;
    private Throwable throwable;
    public enum Status{LOADING, SUCCESS, FAILED}

    public static <T> Request<T> loading() {
        return new Request(Status.LOADING, null, null);
    }

    public static <T> Request<T> success(T response) {
        return new Request(Status.SUCCESS, response, null);
    }

    public static <T> Request<T> failed(Throwable throwable) {
        return new Request(Status.FAILED, null, throwable);
    }
}
