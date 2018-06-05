package com.lixd.moments.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lixd.moments.utils.ItemViewHelper;

import me.drakeet.multitype.ItemViewBinder;

public abstract class BaseItemViewBinder<T, VH extends BaseItemViewBinder.ViewHolder> extends ItemViewBinder<T, VH> {

    @NonNull
    @Override
    protected VH onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return createViewHolder(inflater, parent);
    }

    @Override
    protected void onBindViewHolder(@NonNull final VH holder, @NonNull T item) {
        bindViewHolder(holder, item);
        holder.itemView.post(new Runnable() {
            @Override
            public void run() {
                int adapterPosition = holder.getAdapterPosition();
                int height = holder.itemView.getHeight();
                Log.e("", "adapterPosition: " + adapterPosition + ", height: " + height);
                ItemViewHelper.getInstance().put(adapterPosition, height);
            }
        });
    }

    public abstract VH createViewHolder(LayoutInflater inflater, ViewGroup parent);

    public abstract void bindViewHolder(VH holder, T data);

    public static abstract class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
