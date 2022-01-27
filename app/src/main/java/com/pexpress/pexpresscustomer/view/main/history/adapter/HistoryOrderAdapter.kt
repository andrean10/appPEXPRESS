package com.pexpress.pexpresscustomer.view.main.history.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pexpress.pexpresscustomer.R
import com.pexpress.pexpresscustomer.databinding.ItemsHistoryOrderBinding
import com.pexpress.pexpresscustomer.db.HistoryOrders

class HistoryOrderAdapter(private val historyOrders: List<HistoryOrders?>?) :
    RecyclerView.Adapter<HistoryOrderAdapter.HistoryOrderViewHolder>() {

    private val limit = 5

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): HistoryOrderViewHolder {
        val binding =
            ItemsHistoryOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryOrderViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: HistoryOrderViewHolder, position: Int) {
        viewHolder.bind(historyOrders?.get(position))
    }

    override fun getItemCount() = if (historyOrders.isNullOrEmpty()) {
        0
    } else {
        if (historyOrders.size > limit) {
            limit
        } else {
            historyOrders.size
        }
    }

    inner class HistoryOrderViewHolder(private val binding: ItemsHistoryOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(historyOrder: HistoryOrders?) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(historyOrder?.image)
                    .error(R.drawable.img_not_found)
                    .into(imgStatusOrder)

                tvResiOrder.text = historyOrder?.resi
                tvStatusOrder.text = historyOrder?.status
            }
        }
    }
}
