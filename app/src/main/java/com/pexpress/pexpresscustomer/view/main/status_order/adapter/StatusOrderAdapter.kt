package com.pexpress.pexpresscustomer.view.main.status_order.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pexpress.pexpresscustomer.R
import com.pexpress.pexpresscustomer.databinding.ItemsHistoryBinding
import com.pexpress.pexpresscustomer.model.ResultStatusOrder
import com.pexpress.pexpresscustomer.view.main.status_order.tabview.StatusOrderFragment

class StatusOrderAdapter(private val status: Int) :
    RecyclerView.Adapter<StatusOrderAdapter.HistoryOrderViewHolder>() {

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
                    .load(
                        when (status) {
                            StatusOrderFragment.STATUS_PROCESS -> R.drawable.ic_parcel_ontheway
                            StatusOrderFragment.STATUS_FINISH -> R.drawable.ic_parcel_accept
                            StatusOrderFragment.STATUS_CANCEL -> R.drawable.ic_parcel_return
                            else -> R.color.black
                        }
                    )
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
