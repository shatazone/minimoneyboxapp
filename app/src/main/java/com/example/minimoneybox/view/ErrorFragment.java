package com.example.minimoneybox.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.minimoneybox.databinding.FragmentErrorBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ErrorFragment extends Fragment {
    private FragmentErrorBinding mBinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        mBinder = FragmentErrorBinding.inflate(inflater, container, false);
        return mBinder.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mBinder.btnProceed.setOnClickListener(v -> getActivity().onBackPressed());
        ErrorFragmentArgs errorFragmentArgs = ErrorFragmentArgs.fromBundle(getArguments());

        mBinder.txtErrorTitle.setText(errorFragmentArgs.getTitle());
        mBinder.txtErrorMessage.setText(errorFragmentArgs.getMessage());
    }
}