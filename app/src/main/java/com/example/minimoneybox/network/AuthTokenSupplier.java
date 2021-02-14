package com.example.minimoneybox.network;

import androidx.core.util.Supplier;

public class AuthTokenSupplier implements Supplier<String> {
    private String token;

    public AuthTokenSupplier(String token) {
        this.token = token;
    }

    @Override
    public String get() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
