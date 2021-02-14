package com.example.minimoneybox.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.minimoneybox.R;
import com.example.minimoneybox.databinding.FragmentProductDetailsBinding;
import com.example.minimoneybox.misc.Utils;
import com.example.minimoneybox.network.data.NetworkResponse;
import com.example.minimoneybox.view.ui.RequestObserver;
import com.example.minimoneybox.viewmodel.UserAccountViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ProductDetailsFragment extends Fragment {
    private FragmentProductDetailsBinding mBinding;
    private NavController mNavController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentProductDetailsBinding.inflate(inflater, container, false);
        mBinding.setLifecycleOwner(this);
        mBinding.setViewModel(new ViewModelProvider(requireActivity()).get(UserAccountViewModel.class));
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mNavController = Navigation.findNavController(getView());
        mBinding.getViewModel().AddMoneyBoxRequest.observe(getViewLifecycleOwner(), new AddMoneyBoxRequestObserver());
    }

    private void displayError(String title, String message) {
        ProductDetailsFragmentDirections.ActionAccountDetailsFragmentToAlertDialogFragment alertDialogFragment = ProductDetailsFragmentDirections.actionAccountDetailsFragmentToAlertDialogFragment(title, message);
        mNavController.navigate(alertDialogFragment);
    }

    private void startLoading() {
        mNavController.navigate(R.id.action_productDetailsFragment_to_loadingFragment);
    }

    private void stopLoading() {
        mNavController.popBackStack(R.id.loadingFragment, true);
    }

    private class AddMoneyBoxRequestObserver extends RequestObserver {

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
                Toast.makeText(getActivity(), String.format(getString(R.string.pounds_were_added)), Toast.LENGTH_LONG).show();
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