package com.example.minimoneybox.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.minimoneybox.R;

public class AlertDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        ErrorFragmentArgs fragmentArgs = ErrorFragmentArgs.fromBundle(getArguments());
        return new AlertDialog.Builder(requireContext())
                .setTitle(fragmentArgs.getTitle())
                .setMessage(fragmentArgs.getMessage())
                .setPositiveButton(getString(R.string.ok), (dialog, which) -> dismiss())
                .create();
    }
}
