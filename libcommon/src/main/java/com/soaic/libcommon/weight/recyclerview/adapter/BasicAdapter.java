package com.soaic.libcommon.weight.recyclerview.adapter;

import android.annotation.SuppressLint;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class BasicAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T>{
    private OnItemClickListener<T> onItemClickListener;
    private OnItemLongClickListener<T> onItemLongClickListener;

    @Override
    public void onBindViewHolder(@NonNull final T holder, @SuppressLint("RecyclerView") final int position) {
        if(onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(view, holder, position);
                }
            });
        }

        if(onItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    onItemLongClickListener.onItemLongClick(view, holder, position);
                    return false;
                }
            });
        }

        onBasicBindViewHolder(holder, position);
    }

    public abstract void onBasicBindViewHolder(T holder, int position);

    public interface OnItemClickListener<T> {
        void onItemClick(View view, T holder, int position);
    }

    public interface OnItemLongClickListener<T> {
        void onItemLongClick(View view, T holder, int position);
    }

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener<T> onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }


}
