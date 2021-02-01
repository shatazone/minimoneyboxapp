package com.example.minimoneybox.view.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.minimoneybox.databinding.LayoutUserAccountItemBinding;
import com.example.minimoneybox.model.data.ProductResponse;

import java.util.Objects;

import io.reactivex.Observable;


public class ProductListAdapter extends ListAdapter<ProductResponse, ProductViewHolder> {
    private View.OnClickListener mOnClickListener;

    public ProductListAdapter() {
        super(new ProductResponseItemCallback());
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        LayoutUserAccountItemBinding binding = LayoutUserAccountItemBinding.inflate(layoutInflater, parent, false);

        return new ProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductResponse productResponse = getItem(position);
        holder.bind(productResponse);
        holder.itemView.setTag(holder);
        holder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    public int findPositionOf(ProductResponse productResponse) {
        return getCurrentList().indexOf(productResponse);
    }

    public ProductResponse getItemById(int id) {
        return Observable.fromIterable(getCurrentList())
                .filter(productResponse -> Objects.equals(id, productResponse.getId()))
                .blockingFirst();
    }

    private static class ProductResponseItemCallback extends DiffUtil.ItemCallback<ProductResponse> {
        @Override
        public boolean areItemsTheSame(@NonNull ProductResponse oldItem, @NonNull ProductResponse newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull ProductResponse oldItem, @NonNull ProductResponse newItem) {
            return oldItem.equals(newItem);
        }
    }
}
