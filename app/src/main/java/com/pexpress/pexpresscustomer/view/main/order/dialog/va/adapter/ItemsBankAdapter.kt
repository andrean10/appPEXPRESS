package com.pexpress.pexpresscustomer.view.main.order.dialog.va.adapter

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pexpress.pexpresscustomer.R
import com.pexpress.pexpresscustomer.databinding.ItemsListBankBinding
import com.pexpress.pexpresscustomer.model.type_payments.ResultVA
import com.pexpress.pexpresscustomer.network.ApiConfig
import com.pexpress.pexpresscustomer.utils.UtilsCode
import com.pexpress.pexpresscustomer.utils.checkSystemMode

class ItemsBankAdapter : RecyclerView.Adapter<ItemsBankAdapter.ItemBankViewHolder>() {

    private var onItemClickCallBack: OnItemClickCallBack? = null
    private val itemBank = arrayListOf<ResultVA>()

    fun setListBank(listVA: List<ResultVA>?) {
        if (listVA == null) return
        itemBank.clear()
        itemBank.addAll(listVA)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ItemBankViewHolder {
        val binding =
            ItemsListBankBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemBankViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ItemBankViewHolder, position: Int) {
        viewHolder.bind(itemBank[position])
    }

    override fun getItemCount() = itemBank.size

    inner class ItemBankViewHolder(private val binding: ItemsListBankBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(itemBank: ResultVA) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(ApiConfig.URL_LOGO_VA + itemBank.namafile)
                    .into(imgBank)
                tvBankName.text = itemBank.name
                if (itemBank.isActivated == 1) {
                    tvBankStatus.text = itemView.context.getString(R.string.bank_24jam)
                    val outValue = TypedValue()
                    itemView.context.theme.resolveAttribute(
                        android.R.attr.selectableItemBackground,
                        outValue,
                        true
                    )
                    layoutItemListBank.setBackgroundResource(outValue.resourceId)
                    itemView.setOnClickListener { onItemClickCallBack?.onItemClicked(itemBank) }
                } else {
                    tvBankStatus.text = itemView.context.getString(R.string.bank_service_problem)
                    layoutItemListBank.setBackgroundColor(
                        ResourcesCompat.getColor(
                            itemView.context.resources,
                            when (checkSystemMode(itemView.context)) {
                                UtilsCode.MODE_LIGHT -> R.color.grayLight
                                UtilsCode.MODE_NIGHT -> R.color.grayDark
                                UtilsCode.UNDEFINIED_MODE -> R.color.grayLight
                                else -> {
                                    R.color.grayLight
                                }
                            },
                            null
                        )
                    )
                }
            }
        }
    }

    fun setOnItemClickCallBack(onItemClickCallBack: OnItemClickCallBack) {
        this.onItemClickCallBack = onItemClickCallBack
    }

    interface OnItemClickCallBack {
        fun onItemClicked(itemBank: ResultVA)
    }
}
