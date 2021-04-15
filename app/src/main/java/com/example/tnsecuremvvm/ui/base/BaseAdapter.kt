package com.example.tnsecuremvvm.ui.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T, VH : BaseViewHolder<T>>(
    private var datas: List<T>,
    private var viewHolderResourceID: Int
) :
    RecyclerView.Adapter<VH>() {

    abstract fun initViewHolder(rootView: View): VH

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): VH {
        val rootView = LayoutInflater.from(parent.context)
            .inflate(viewHolderResourceID, parent, false)
        return initViewHolder(
            rootView
        )
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(datas[position])
    }

    fun update(data: List<T>) {
        datas = data
        notifyDataSetChanged()
    }
}