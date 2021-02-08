package com.example.minimoneybox.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.minimoneybox.R;
import com.example.minimoneybox.databinding.FragmentLoginBinding;
import com.example.minimoneybox.misc.Utils;
import com.example.minimoneybox.network.data.NetworkResponse;
import com.example.minimoneybox.view.ui.RequestObserver;
import com.example.minimoneybox.viewmodel.LoginViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginFragment extends Fragment {

    private FragmentLoginBinding mBinding;
    private NavController mNavController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentLoginBinding.inflate(inflater, container, false);
        mBinding.setLifecycleOwner(this);
        mBinding.setViewModel(new ViewModelProvider(this).get(LoginViewModel.class));
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mNavController = Navigation.findNavController(getView());
        mBinding.getViewModel().LoginResponse.observe(getViewLifecycleOwner(), new LoginRequestObserver());
    }

    private void displayError(String title, String message) {
        LoginFragmentDirections.DisplayErrorFragment displayErrorFragment = LoginFragmentDirections.displayErrorFragment(title, message);
        mNavController.navigate(displayErrorFragment);
    }

    private void displayUserAccounts() {
        mNavController.navigate(R.id.action_loginFragment_to_userAccountsFragment);
    }

    private void startLoading() {
        mBinding.animation.playAnimation();
        mNavController.navigate(R.id.displayLoadingDialog);
    }

    private void stopLoading() {
        mNavController.popBackStack(R.id.loadingFragment, true);
    }

    private class LoginRequestObserver extends RequestObserver {

        @Override
        protected void updateLoader(boolean loading) {
            if(loading) {
                startLoading();
            } else {
                stopLoading();
            }
        }

        @Override
        protected void onResponseReceived(NetworkResponse response) {
            if (response.isSuccessful()) {
                displayUserAccounts();
            } else {
                displayError(response.getErrorBody().getName(), response.getErrorBody().getMessage());
            }
        }

        @Override
        protected void onError(Throwable throwable) {
            displayError(Utils.getTitleFor(getContext(), throwable), Utils.getMessageFor(getContext(), throwable));
        }
    }
}