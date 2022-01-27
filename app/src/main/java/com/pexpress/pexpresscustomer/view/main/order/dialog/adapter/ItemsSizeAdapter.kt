package com.pexpress.pexpresscustomer.view.main.order.dialog.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pexpress.pexpresscustomer.databinding.ItemsSizeBinding
import com.pexpress.pexpresscustomer.db.ItemSize

class ItemsSizeAdapter(private val itemSize: List<ItemSize?>?) :
    RecyclerView.Adapter<ItemsSizeAdapter.ItemSizeViewHolder>() {

    private val limit = 5
    private var onItemClickCallBack: OnItemClickCallBack? = null

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ItemSizeViewHolder {
        val binding =
            ItemsSizeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemSizeViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ItemSizeViewHolder, position: Int) {
        viewHolder.bind(itemSize?.get(position))
    }

    override fun getItemCount() = if (itemSize.isNullOrEmpty()) {
        0
    } else {
        if (itemSize.size > limit) {
            limit
        } else {
            itemSize.size
        }
    }

    inner class ItemSizeViewHolder(private val binding: ItemsSizeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(itemSize: ItemSize?) {
            with(binding) {
                tvNameItemSize.text = itemSize?.nameItemSize
                tvDimensionSize.text =
                    "Panjang: ${itemSize?.dimenLength}\nLebar: ${itemSize?.dimenWidth}\nTinggi: ${itemSize?.dimenHeight}"
                tvWeightMax.text = "Berat Max: ${itemSize?.dimenWeight}"

                itemView.setOnClickListener { onItemClickCallBack?.onItemClicked(itemSize!!) }
            }
        }
    }

    fun setOnItemClickCallBack(onItemClickCallBack: OnItemClickCallBack) {
        this.onItemClickCallBack = onItemClickCallBack
    }

    interface OnItemClickCallBack {
        fun onItemClicked(itemSize: ItemSize)
    }
}
