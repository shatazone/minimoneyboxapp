package com.example.minimoneybox.dagger;

import com.example.minimoneybox.model.repository.KeyValueRepository;
import com.example.minimoneybox.network.data.UserLoginResponse;
import com.example.minimoneybox.network.AuthTokenSupplier;
import com.example.minimoneybox.model.storage.impl.HawkStorage;
import com.example.minimoneybox.model.storage.KeyValueStorage;
import com.example.minimoneybox.model.repository.Repository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public abstract class RepositoryModule {

    @Singleton
    @Provides
    public static KeyValueStorage providesKeyValueBasedStorage() {
        return new HawkStorage();
    }

    @Singleton
    @Provides
    public static Repository provideRepository(KeyValueStorage storage) {
        return new KeyValueRepository(storage);
    }

    @Singleton
    @Provides
    public static AuthTokenSupplier provideAuthTokenSupplier(Repository repository) {
        UserLoginResponse userLoginResponse = repository.getUserLogin();
        String token = userLoginResponse == null ? null : userLoginResponse.getSession().getBearerToken();

        return new AuthTokenSupplier(token);
    }
}
