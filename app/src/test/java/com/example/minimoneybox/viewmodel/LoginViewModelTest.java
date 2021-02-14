package com.example.minimoneybox.viewmodel;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.core.app.ApplicationProvider;

import com.example.minimoneybox.MoneyBoxManager;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.schedulers.Schedulers;

@RunWith(RobolectricTestRunner.class)
public class LoginViewModelTest {
    private static final String VALID_EMAIL = "ahmad@gmail.com";
    private static final String VALID_PASSWORD = "1234";
    private static final String INVALID_EMAIL = "wrong email format";
    private static final String DISPLAY_NAME = "ahmad";

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    @Mock
    MoneyBoxManager moneyBoxManager;

    private LoginViewModel viewModel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(schedulerCallable -> Schedulers.trampoline());

        viewModel = new LoginViewModel(ApplicationProvider.getApplicationContext(), moneyBoxManager);
    }

    @Test
    public void testFormIsValidWithProperInput() throws InterruptedException {
        testValidationStatus(VALID_EMAIL, VALID_PASSWORD, DISPLAY_NAME, true);
        testValidationStatus(VALID_EMAIL, VALID_PASSWORD, null, true);
    }

    @Test
    public void testFormIsInvalidWithInProperInput() throws InterruptedException {
        testValidationStatus(VALID_EMAIL, null, false);
        testValidationStatus(null, VALID_PASSWORD, false);
        testValidationStatus(INVALID_EMAIL, VALID_PASSWORD, false);
        testValidationStatus(INVALID_EMAIL, null, false);
        testValidationStatus(null, null, false);
    }

    @Test
    public void testExceptionIsThrownWhenLoggingInWithInvalidForm() throws InterruptedException {
        testValidationStatus(INVALID_EMAIL, VALID_PASSWORD, false);
    }

    private void testValidationStatus(String email, String password, boolean expected) throws InterruptedException {
        testValidationStatus(email, password, viewModel.DisplayName.getValue(), expected);
    }

    private void testValidationStatus(String email, String password, String displayName, boolean expected) throws InterruptedException {
        boolean actual = fillForm(email, password, displayName);
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    private boolean fillForm(String email, String password, String displayName) {
        viewModel.EmailAddress.setValue(email);
        viewModel.Password.setValue(password);
        viewModel.DisplayName.setValue(displayName);

        viewModel.validateEmail();
        viewModel.validatePassword();
        viewModel.validateDisplayName();

        return viewModel.validateForm();
    }
}