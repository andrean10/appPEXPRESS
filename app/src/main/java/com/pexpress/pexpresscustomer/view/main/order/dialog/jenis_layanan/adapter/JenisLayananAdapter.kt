package com.pexpress.pexpresscustomer.view.main.order.dialog.jenis_layanan.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pexpress.pexpresscustomer.databinding.ItemsJenisLayananBinding
import com.pexpress.pexpresscustomer.model.order.ResultJenisLayanan

class JenisLayananAdapter : RecyclerView.Adapter<JenisLayananAdapter.ServiceTypeViewHolder>() {
    private var onItemClickCallBack: OnItemClickCallBack? = null

    private val serviceType = arrayListOf<ResultJenisLayanan>()

    fun setJenisLayanan(jenisLayanan: List<ResultJenisLayanan>?) {
        if (jenisLayanan == null) return
        serviceType.clear()
        serviceType.addAll(jenisLayanan)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ServiceTypeViewHolder {
        val binding =
            ItemsJenisLayananBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ServiceTypeViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ServiceTypeViewHolder, position: Int) {
        viewHolder.bind(serviceType[position])
    }

    override fun getItemCount() = serviceType.size

    inner class ServiceTypeViewHolder(private val binding: ItemsJenisLayananBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(jenisLayanan: ResultJenisLayanan) {
            with(binding) {
                tvServiceType.text = jenisLayanan.layanan
                tvInformationService.text = jenisLayanan.informasipengiriman
            }

            itemView.setOnClickListener { onItemClickCallBack?.onItemClicked(jenisLayanan) }
        }
    }

    fun setOnItemClickCallBack(onItemClickCallBack: OnItemClickCallBack) {
        this.onItemClickCallBack = onItemClickCallBack
    }

    interface OnItemClickCallBack {
        fun onItemClicked(result: ResultJenisLayanan)
    }
}
