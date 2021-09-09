package com.soaic.startproject.ui.adapter.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.soaic.startproject.databinding.TestHomeItemBinding
import com.soaic.startproject.ui.holder.base.BaseBindingHolder
import com.soaic.libcommon.weight.recyclerview.adapter.BasicAdapter

class TestAdapter(private var context: Context?,
                  private var data: MutableList<MutableList<String>>):
    BasicAdapter<BaseBindingHolder<TestHomeItemBinding>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseBindingHolder<TestHomeItemBinding> {
        val view = TestHomeItemBinding.inflate(LayoutInflater.from(context),parent, false)
        return BaseBindingHolder.createHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBasicBindViewHolder(holder: BaseBindingHolder<TestHomeItemBinding>, position: Int) {
        holder.binding.testView.text = data[position][0]
    }
}