package com.pexpress.pexpresscustomer.view.main.status_order.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pexpress.pexpresscustomer.R
import com.pexpress.pexpresscustomer.databinding.ItemsHistoryBinding
import com.pexpress.pexpresscustomer.model.status_order.ResultStatusOrder

class StatusOrderCancelAdapter :
    RecyclerView.Adapter<StatusOrderCancelAdapter.HistoryOrderViewHolder>() {

    private var onItemClickCallBack: OnItemClickCallBack? = null
    private val listStatusOrder = arrayListOf<ResultStatusOrder>()

    fun setDataOrder(itemStatusOrder: List<ResultStatusOrder>?) {
        if (itemStatusOrder == null) return
        listStatusOrder.clear()
        listStatusOrder.addAll(itemStatusOrder)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): HistoryOrderViewHolder {
        val binding =
            ItemsHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryOrderViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: HistoryOrderViewHolder, position: Int) {
        viewHolder.bind(listStatusOrder[position])
    }

    override fun getItemCount() = listStatusOrder.size

    inner class HistoryOrderViewHolder(private val binding: ItemsHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(statusOrder: ResultStatusOrder) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(R.drawable.ic_parcel_return)
                    .into(imgStatusOrder)

                tvResiOrder.text = statusOrder.nomorpemesanan
                tvStatusOrder.text = statusOrder.namastatuspengiriman
                tvStatusDate.text = statusOrder.tglcreate
            }

            itemView.setOnClickListener { onItemClickCallBack?.onItemClicked(statusOrder) }
        }
    }

    fun setOnItemClickCallBack(onItemClickCallBack: OnItemClickCallBack) {
        this.onItemClickCallBack = onItemClickCallBack
    }

    interface OnItemClickCallBack {
        fun onItemClicked(detailOrder: ResultStatusOrder)
    }
}
