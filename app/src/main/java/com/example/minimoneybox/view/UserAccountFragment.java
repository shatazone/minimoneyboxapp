package com.example.minimoneybox.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;

import com.example.minimoneybox.databinding.FragmentUserAccountBinding;
import com.example.minimoneybox.misc.Utils;
import com.example.minimoneybox.model.data.ProductResponse;
import com.example.minimoneybox.network.data.NetworkResponse;
import com.example.minimoneybox.view.ui.ProductListAdapter;
import com.example.minimoneybox.view.ui.ProductViewHolder;
import com.example.minimoneybox.view.ui.RequestObserver;
import com.example.minimoneybox.viewmodel.UserAccountViewModel;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class UserAccountFragment extends Fragment {

    private FragmentUserAccountBinding mBinding;
    private ProductListAdapter mAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentUserAccountBinding.inflate(inflater, container, false);
        mBinding.setLifecycleOwner(this);
        mBinding.setViewModel(new ViewModelProvider(this).get(UserAccountViewModel.class));
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAdapter = new ProductListAdapter();
        mAdapter.setOnClickListener(this::openProductDetails);
        mAdapter.setHasStableIds(true);

        mBinding.recyclerView.setHasFixedSize(true);
        mBinding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        mBinding.recyclerView.setAdapter(mAdapter);

        mBinding.getViewModel().ProductResponseList.observe(getActivity(), this::updateProductList);

        // Observe moneyvalue returned theoretically from AccountDetailsFragment, and update the whole products list.
        NavController navController = Navigation.findNavController(getView());
        SavedStateHandle savedStateHandle = navController.getCurrentBackStackEntry().getSavedStateHandle();
        MutableLiveData<Bundle> liveData = savedStateHandle.getLiveData(ProductDetailsFragment.KEY_MONEYBOX_UPDATED);
        liveData.observe(getViewLifecycleOwner(), bundle -> {
            int productId = bundle.getInt(ProductDetailsFragment.ARG_PRODUCT_RESPONSE_ID);
            double moneybox = bundle.getDouble(ProductDetailsFragment.ARG_MONEYBOX);

            onMoneyboxValueUpdated(productId, moneybox);
        });

        mBinding.getViewModel().RefreshProductListResponse.observe(getViewLifecycleOwner(), new RefreshProductListRequestObserver());
    }

    // It is safe to update the UI only, since any change by API will automatically update the repository
    // TODO find a way to update the view model as well... it is now inconsistent
    private void onMoneyboxValueUpdated(int productId, double moneybox) {
        ProductResponse productResponse = mAdapter.getItemById(productId);
        productResponse.setMoneybox(moneybox);
        int position = mAdapter.findPositionOf(productResponse);
        mAdapter.notifyItemChanged(position);
    }

    private void openProductDetails(View v) {
        ProductViewHolder holder = (ProductViewHolder) v.getTag();
        int adapterPosition = holder.getAdapterPosition();
        ProductResponse productResponse = mAdapter.getCurrentList().get(adapterPosition);

        UserAccountFragmentDirections.StartAccountDetails startAccountDetails = UserAccountFragmentDirections.startAccountDetails().setProductResponseId(productResponse.getId());
        Navigation.findNavController(getView()).navigate(startAccountDetails);
    }

    private void updateProductList(List<ProductResponse> productResponses) {
        int oldVisibility = mBinding.recyclerView.getVisibility();

        mBinding.recyclerView.setVisibility(productResponses.isEmpty() ? View.GONE : View.VISIBLE);
        mBinding.txtEmptyAccountList.setVisibility(productResponses.isEmpty() ? View.VISIBLE : View.GONE);
        mAdapter.submitList(productResponses);

        if (oldVisibility == View.GONE && mBinding.recyclerView.getVisibility() == View.VISIBLE) {
            mAdapter.notifyDataSetChanged();
        }
    }

    private class RefreshProductListRequestObserver extends RequestObserver {
        @Override
        protected void updateLoader(boolean loading) {
            mBinding.swipeRefreshLayout.setRefreshing(loading);
        }

        @Override
        protected void onResponseReceived(NetworkResponse response) {
            if (!response.isSuccessful()) {
                Toast.makeText(getContext(), response.getErrorBody().getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onError(Throwable throwable) {
            Toast.makeText(getContext(), Utils.getMessageFor(getContext(), throwable), Toast.LENGTH_LONG).show();
        }
    }
}