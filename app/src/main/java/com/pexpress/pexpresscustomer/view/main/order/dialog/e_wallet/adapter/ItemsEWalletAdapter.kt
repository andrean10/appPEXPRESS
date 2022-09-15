package com.pexpress.pexpresscustomer.view.main.order.dialog.e_wallet.adapter

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pexpress.pexpresscustomer.R
import com.pexpress.pexpresscustomer.databinding.ItemsListEwalletBinding
import com.pexpress.pexpresscustomer.model.type_payments.ResultEWallet
import com.pexpress.pexpresscustomer.network.ApiConfig
import com.pexpress.pexpresscustomer.utils.UtilsCode
import com.pexpress.pexpresscustomer.utils.checkSystemMode


class ItemsEWalletAdapter : RecyclerView.Adapter<ItemsEWalletAdapter.ItemEWalletViewHolder>() {

    private var onItemClickCallBack: OnItemClickCallBack? = null
    private val itemEWallet = arrayListOf<ResultEWallet>()

    private val TAG = ItemsEWalletAdapter::class.simpleName

    fun setListEWallet(listVA: List<ResultEWallet>?) {
        if (listVA == null) return
        itemEWallet.clear()
        itemEWallet.addAll(listVA)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ItemEWalletViewHolder {
        val binding =
            ItemsListEwalletBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemEWalletViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ItemEWalletViewHolder, position: Int) {
        viewHolder.bind(itemEWallet[position])
    }

    override fun getItemCount() = itemEWallet.size

    inner class ItemEWalletViewHolder(private val binding: ItemsListEwalletBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(itemEWallet: ResultEWallet) {
            with(binding) {
                Glide.with(itemView.context)
                    .load("${ApiConfig.URL_LOGO_EWALLET}${itemEWallet.namafile}")
                    .into(imgEwallet)

                tvEwalletName.text = itemEWallet.nama

                if (itemEWallet.isactive == 1) {
                    tvEwalletStatus.text = itemView.context.getString(R.string.ewallet_24jam)
                    val outValue = TypedValue()
                    itemView.context.theme.resolveAttribute(
                        android.R.attr.selectableItemBackground,
                        outValue,
                        true
                    )
                    layoutItemListBank.setBackgroundResource(outValue.resourceId)
                    itemView.setOnClickListener { onItemClickCallBack?.onItemClicked(itemEWallet) }
                } else {
                    tvEwalletStatus.text =
                        itemView.context.getString(R.string.ewallet_service_problem)
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
        fun onItemClicked(itemEWallet: ResultEWallet)
    }
}
