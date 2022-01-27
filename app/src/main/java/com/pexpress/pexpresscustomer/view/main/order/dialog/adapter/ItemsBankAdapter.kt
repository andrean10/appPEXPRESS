package com.pexpress.pexpresscustomer.view.main.order.dialog.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pexpress.pexpresscustomer.databinding.ItemsListBankBinding
import com.pexpress.pexpresscustomer.db.ItemBank

class ItemsBankAdapter(private val itemBank: List<ItemBank?>?) :
    RecyclerView.Adapter<ItemsBankAdapter.ItemSizeViewHolder>() {

    private val limit = 5
    private var onItemClickCallBack: OnItemClickCallBack? = null

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ItemSizeViewHolder {
        val binding =
            ItemsListBankBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemSizeViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ItemSizeViewHolder, position: Int) {
        viewHolder.bind(itemBank?.get(position))
    }

    override fun getItemCount() = if (itemBank.isNullOrEmpty()) { // data not found, emergency!!
        0
    } else {
        itemBank.size
    }

    inner class ItemSizeViewHolder(private val binding: ItemsListBankBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(itemBank: ItemBank?) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(itemBank)
                    .into(imgBank)

                tvBankName.text = itemBank!!.codeBank

                if (itemBank.statusBank) {
                    tvBankStatus.text = "24 Jam"
                } else {
                    tvBankStatus.text = "Bank Mengalamai Masalah"
                }

                itemView.setOnClickListener { onItemClickCallBack?.onItemClicked(itemBank) }
            }
        }
    }

    fun setOnItemClickCallBack(onItemClickCallBack: OnItemClickCallBack) {
        this.onItemClickCallBack = onItemClickCallBack
    }

    interface OnItemClickCallBack {
        fun onItemClicked(itemBank: ItemBank)
    }
}
