package com.pexpress.pexpresscustomer.view.main.status_pembayaran.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pexpress.pexpresscustomer.R
import com.pexpress.pexpresscustomer.databinding.ItemsStatusPembayaranBinding
import com.pexpress.pexpresscustomer.model.payment.ResultStatusPembayaran
import com.pexpress.pexpresscustomer.utils.FormatDate
import com.pexpress.pexpresscustomer.utils.UtilsCode
import com.pexpress.pexpresscustomer.utils.formatRupiah

class StatusPembayaranAdapter :
    RecyclerView.Adapter<StatusPembayaranAdapter.StatusPembayaranViewHolder>() {

    private var onItemClickCallBack: OnItemClickCallBack? = null
    private val listStatusPembayaran = arrayListOf<ResultStatusPembayaran>()
    private val TAG = StatusPembayaranAdapter::class.simpleName

    fun setDataStatusPembayaran(itemStatusPembayaran: List<ResultStatusPembayaran>?) {
        if (itemStatusPembayaran == null) return
        listStatusPembayaran.clear()
        listStatusPembayaran.addAll(itemStatusPembayaran)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): StatusPembayaranViewHolder {
        val binding =
            ItemsStatusPembayaranBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StatusPembayaranViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: StatusPembayaranViewHolder, position: Int) {
        viewHolder.bind(listStatusPembayaran[position])
    }

    override fun getItemCount() = listStatusPembayaran.size

    inner class StatusPembayaranViewHolder(private val binding: ItemsStatusPembayaranBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(statusPembayaran: ResultStatusPembayaran) {
            val formatDate = FormatDate()

            with(binding) {
                tvNoPemesanan.text = statusPembayaran.nomorpemesanan
                val formatPembayaran = statusPembayaran.jenispembayaran?.replace("Pembayaran ", "")
                tvJenisPembayaran.text = when (statusPembayaran.jenispembayaran) {
                    "Pembayaran Tunai" -> {
                        statusPembayaran.jenispembayaran.replace("Pembayaran ", "")
                    }
                    "Pembayaran Transfer" -> {
                        itemView.context.getString(
                            R.string.status_jenis_pembayaran,
                            formatPembayaran.toString(),
                            statusPembayaran.namabank?.let {
                                if (it.contains("Bank Rakyat Indonesia") ||
                                    it.contains("Bank Negara Indonesia")
                                ) {
                                    it
                                } else {
                                    it.replace("Bank ", "")
                                }
                            }
                        )
                    }
                    "Pembayaran Ewallet" -> {
                        itemView.context.getString(
                            R.string.status_jenis_pembayaran,
                            formatPembayaran.toString(),
                            statusPembayaran.ewallet?.replace("Pembayaran ", "")
                        )
                    }
                    else -> ""
                }

                tvTagihanPembayaran.text = formatRupiah(statusPembayaran.tagihan?.toDouble() ?: 0.0)
                tvStatusPembayaran.setTextColor(
                    when (statusPembayaran.status) {
                        "PAID" -> Color.parseColor("#FF00C853")
                        "PENDING" -> Color.parseColor("#deb422")
                        "FAILED" -> Color.parseColor("#c61e3a")
                        else -> tvStatusPembayaran.textColors.defaultColor
                    }
                )
                tvStatusPembayaran.text = statusPembayaran.status
                tvExpiredDatePembayaran.text = formatDate.formatedDate(
                    statusPembayaran.expired,
                    UtilsCode.PATTERN_DATE_FROM_API,
                    UtilsCode.PATTERN_DATE_VIEW_STATUS_PEMBAYARAN
                )
            }

            itemView.setOnClickListener { onItemClickCallBack?.onItemClicked(statusPembayaran) }
        }
    }

    fun setOnItemClickCallBack(onItemClickCallBack: OnItemClickCallBack) {
        this.onItemClickCallBack = onItemClickCallBack
    }

    interface OnItemClickCallBack {
        fun onItemClicked(resultStatusPembayaran: ResultStatusPembayaran)
    }
}
