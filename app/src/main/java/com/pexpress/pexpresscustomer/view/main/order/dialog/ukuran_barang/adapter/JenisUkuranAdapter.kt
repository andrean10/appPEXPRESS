package com.pexpress.pexpresscustomer.view.main.order.dialog.ukuran_barang.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pexpress.pexpresscustomer.R.string.dimension_weight_size_2
import com.pexpress.pexpresscustomer.databinding.ItemsJenisUkuranBinding
import com.pexpress.pexpresscustomer.model.order.ResultJenisUkuran

class JenisUkuranAdapter : RecyclerView.Adapter<JenisUkuranAdapter.ItemSizeViewHolder>() {

    private var onItemClickCallBack: OnItemClickCallBack? = null
    private val itemSize = arrayListOf<ResultJenisUkuran>()

    fun setJenisUkuran(jenisUkuran: List<ResultJenisUkuran>?) {
        if (jenisUkuran == null) return
        itemSize.clear()
        itemSize.addAll(jenisUkuran)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ItemSizeViewHolder {
        val binding =
            ItemsJenisUkuranBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemSizeViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ItemSizeViewHolder, position: Int) {
        viewHolder.bind(itemSize[position])
    }

    override fun getItemCount() = itemSize.size

    inner class ItemSizeViewHolder(private val binding: ItemsJenisUkuranBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(result: ResultJenisUkuran) {
            with(binding) {
                tvNameItemSize.text = result.jenisukuran
                tvWeightMax.text = itemView.context.getString(
                    dimension_weight_size_2,
                    result.maksimalberat
                )
            }

            itemView.setOnClickListener { onItemClickCallBack?.onItemClicked(result) }
        }
    }

    fun setOnItemClickCallBack(onItemClickCallBack: OnItemClickCallBack) {
        this.onItemClickCallBack = onItemClickCallBack
    }

    interface OnItemClickCallBack {
        fun onItemClicked(result: ResultJenisUkuran)
    }
}
