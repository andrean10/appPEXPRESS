package com.pexpress.pexpresscustomer.view.main.status_order.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pexpress.pexpresscustomer.R
import com.pexpress.pexpresscustomer.databinding.ItemsMilestoneBinding
import com.pexpress.pexpresscustomer.model.resi.milestone.ResultMilestone
import com.pexpress.pexpresscustomer.utils.FormatDate
import com.pexpress.pexpresscustomer.utils.UtilsCode.PATTERN_DATE_FROM_API
import com.pexpress.pexpresscustomer.utils.UtilsCode.PATTERN_DATE_VIEW_MILESTONE
import com.pexpress.pexpresscustomer.utils.UtilsCode.PATTERN_TIME_VIEW_MILESTONE
import com.pexpress.pexpresscustomer.utils.UtilsCode.STATUS_PACKAGE_DELIVERED
import com.pexpress.pexpresscustomer.utils.UtilsCode.STATUS_PACKAGE_PROCCESS_PAYMENT
import com.pexpress.pexpresscustomer.utils.UtilsCode.STATUS_PACKAGE_WAITING_FOR_PICKUP

class MilestoneAdapter : RecyclerView.Adapter<MilestoneAdapter.MilestoneViewHolder>() {

    private var onItemClickCallBack: OnItemClickCallBack? = null
    private val listMilestone = arrayListOf<ResultMilestone>()

    fun setDataMilestone(itemMilestone: List<ResultMilestone>?) {
        if (itemMilestone == null) return
        listMilestone.clear()
        listMilestone.addAll(itemMilestone)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MilestoneViewHolder {
        val binding =
            ItemsMilestoneBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MilestoneViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: MilestoneViewHolder, position: Int) {
        viewHolder.bind(listMilestone[position])
    }

    override fun getItemCount() = listMilestone.size

    inner class MilestoneViewHolder(private val binding: ItemsMilestoneBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(milestone: ResultMilestone) {
            with(binding) {
                val formatDate = FormatDate()
                val tanggal = formatDate.formatedDate(
                    milestone.tglcreate,
                    PATTERN_DATE_FROM_API,
                    PATTERN_DATE_VIEW_MILESTONE
                )
                val waktu = formatDate.formatedDate(
                    milestone.tglcreate,
                    PATTERN_DATE_FROM_API,
                    PATTERN_TIME_VIEW_MILESTONE
                )

                tvTanggalStatus.text =
                    itemView.context.getString(R.string.datetime_format, tanggal, waktu)

                if (milestone.statuspengiriman == STATUS_PACKAGE_DELIVERED) {
                    tvStatus.text = itemView.context.getString(
                        R.string.milestone_status_pengiriman,
                        milestone.namastatuspengiriman,
                        milestone.diserahkanoleh
                    )

                    if (!milestone.fotomenyerahkan.isNullOrEmpty()) {
                        viewVisible(tvFotoPenerimaPengiriman)
                    }
                } else {
                    tvStatus.text = milestone.namastatuspengiriman
                }

                tvDetailStatusPengiriman.text = if (!milestone.namakurir.isNullOrEmpty()) {
                    if (milestone.statuspengiriman != STATUS_PACKAGE_PROCCESS_PAYMENT) {
                        itemView.context.getString(
                            R.string.milestone_detail_status_pengiriman,
                            milestone.informasistatuspengiriman,
                            milestone.namakurir
                        )
                    } else {
                        milestone.informasistatuspengiriman
                    }
                } else {
                    milestone.informasistatuspengiriman
                }

                tvFotoPenerimaPengiriman.setOnClickListener {
                    onItemClickCallBack?.onItemClicked(milestone.fotomenyerahkan.toString())
                }

                // lines milestone condition
                if (milestone.statuspengiriman == STATUS_PACKAGE_WAITING_FOR_PICKUP) {
                    linesMilestoneVisibility(linesMilestone, false)
                } else {
                    linesMilestoneVisibility(linesMilestone, true)
                }
            }
        }
    }

    private fun viewVisible(view: View) {
        view.visibility = View.VISIBLE
    }

    private fun linesMilestoneVisibility(view: View, state: Boolean) {
        view.visibility = if (state) View.VISIBLE else View.INVISIBLE
    }

    fun setOnItemClickCallBack(onItemClickCallBack: OnItemClickCallBack) {
        this.onItemClickCallBack = onItemClickCallBack
    }

    interface OnItemClickCallBack {
        fun onItemClicked(fotoPenerima: String)
    }
}
