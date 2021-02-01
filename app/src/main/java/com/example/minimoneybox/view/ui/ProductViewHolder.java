package com.example.minimoneybox.view.ui;

import androidx.recyclerview.widget.RecyclerView;

import com.example.minimoneybox.databinding.LayoutUserAccountItemBinding;
import com.example.minimoneybox.model.data.ProductResponse;

public class ProductViewHolder extends RecyclerView.ViewHolder {
    private LayoutUserAccountItemBinding mBinding;

    public ProductViewHolder(LayoutUserAccountItemBinding binding) {
        super(binding.getRoot());
        this.mBinding = binding;
    }

    public void bind(ProductResponse productResponse) {
        mBinding.setProductResponse(productResponse);
        mBinding.executePendingBindings();
    }
}
