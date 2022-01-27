package com.pexpress.pexpresscustomer.view.main.order.dialog.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pexpress.pexpresscustomer.databinding.ItemsServiceTypeBinding
import com.pexpress.pexpresscustomer.db.ServiceType

class ServiceTypeAdapter(private val serviceType: List<ServiceType?>) :
    RecyclerView.Adapter<ServiceTypeAdapter.ServiceTypeViewHolder>() {

    private val limit = 5
    private var onItemClickCallBack: OnItemClickCallBack? = null

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ServiceTypeViewHolder {
        val binding =
            ItemsServiceTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ServiceTypeViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ServiceTypeViewHolder, position: Int) {
        viewHolder.bind(serviceType[position])
    }

    override fun getItemCount() = if (serviceType.isNullOrEmpty()) {
        0
    } else {
        if (serviceType.size > limit) {
            limit
        } else {
            serviceType.size
        }
    }

    inner class ServiceTypeViewHolder(private val binding: ItemsServiceTypeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(service: ServiceType?) {
            with(binding) {
                tvServiceType.text = service?.serviceType
                tvEstimationService.text = "Waktu pengiriman ${service?.estimation} hari"

                itemView.setOnClickListener { onItemClickCallBack?.onItemClicked(service!!) }
            }
        }
    }

    fun setOnItemClickCallBack(onItemClickCallBack: OnItemClickCallBack) {
        this.onItemClickCallBack = onItemClickCallBack
    }

    interface OnItemClickCallBack {
        fun onItemClicked(service: ServiceType)
    }
}
