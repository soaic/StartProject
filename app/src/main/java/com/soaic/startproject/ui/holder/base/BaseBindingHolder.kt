package com.soaic.startproject.ui.holder.base

import android.content.Context
import android.view.LayoutInflater
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView


class BaseBindingHolder<T : ViewDataBinding>(var binding: T) : RecyclerView.ViewHolder(binding.root) {

    var context: Context = itemView.context

    val inflater: LayoutInflater
        get() = LayoutInflater.from(context)

    fun setVisibility(visibility: Int) {
        binding.root.visibility = visibility
    }

    companion object {
        fun <T : ViewDataBinding> createHolder(binding: T): BaseBindingHolder<T> {
            return BaseBindingHolder(binding)
        }
    }
}
