package com.example.minimoneybox.network;

import com.example.minimoneybox.network.data.ErrorBody;

import io.reactivex.functions.Consumer;

public interface AuthTokenExpiredConsumer extends Consumer<ErrorBody> {

}
