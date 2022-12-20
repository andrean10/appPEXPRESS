package com.pexpress.pexpresscustomer.view.main.status_order.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pexpress.pexpresscustomer.R
import com.pexpress.pexpresscustomer.databinding.ItemsMilestoneBinding
import com.pexpress.pexpresscustomer.model.resi.milestone.ResultMilestone
import com.pexpress.pexpresscustomer.utils.FormatDate
import com.pexpress.pexpresscustomer.utils.UtilsCode.MILESTONE_DELIVERY
import com.pexpress.pexpresscustomer.utils.UtilsCode.MILESTONE_IN_TRANSIT
import com.pexpress.pexpresscustomer.utils.UtilsCode.MILESTONE_NOT_DELIVERY
import com.pexpress.pexpresscustomer.utils.UtilsCode.MILESTONE_PACKAGE_ALREADY_PICKUP
import com.pexpress.pexpresscustomer.utils.UtilsCode.MILESTONE_PROCESS_DELIVERY
import com.pexpress.pexpresscustomer.utils.UtilsCode.MILESTONE_PROCESS_PAYMENT
import com.pexpress.pexpresscustomer.utils.UtilsCode.MILESTONE_PROCESS_SHUTTLE
import com.pexpress.pexpresscustomer.utils.UtilsCode.MILESTONE_PROCESS_TRANSIT
import com.pexpress.pexpresscustomer.utils.UtilsCode.MILESTONE_RELEASE
import com.pexpress.pexpresscustomer.utils.UtilsCode.MILESTONE_RETURNED_TO_SENDER
import com.pexpress.pexpresscustomer.utils.UtilsCode.MILESTONE_SHUTTLE
import com.pexpress.pexpresscustomer.utils.UtilsCode.MILESTONE_TRANSIT
import com.pexpress.pexpresscustomer.utils.UtilsCode.MILESTONE_WAITING_FOR_PICKUP
import com.pexpress.pexpresscustomer.utils.UtilsCode.PATTERN_DATE_FROM_API
import com.pexpress.pexpresscustomer.utils.UtilsCode.PATTERN_DATE_FROM_API2
import com.pexpress.pexpresscustomer.utils.UtilsCode.PATTERN_DATE_VIEW_MILESTONE

class MilestoneAdapter : RecyclerView.Adapter<MilestoneAdapter.MilestoneViewHolder>() {

    private var onItemClickCallBack: OnItemClickCallBack? = null
    private val listMilestone = arrayListOf<ResultMilestone>()

    private val TAG = MilestoneAdapter::class.simpleName

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
        viewHolder.bind(listMilestone[position], position)
    }

    override fun getItemCount() = listMilestone.size

    inner class MilestoneViewHolder(private val binding: ItemsMilestoneBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val formatDate = FormatDate()

        fun bind(milestone: ResultMilestone, position: Int) {
            var namaKurir = ""
            var tanggalStatus = ""
            when (milestone.statuspengiriman) {
                MILESTONE_PROCESS_PAYMENT -> {
                    namaKurir = ""
                    tanggalStatus = itemView.context.getString(
                        R.string.datetime_format, formatDate.formatedDate(
                            milestone.paytime,
                            PATTERN_DATE_FROM_API,
                            PATTERN_DATE_VIEW_MILESTONE
                        )
                    )
                }
                MILESTONE_WAITING_FOR_PICKUP -> {
                    namaKurir = milestone.namakurir.toString()
                    tanggalStatus = itemView.context.getString(
                        R.string.datetime_format,
                        formatDate.formatedDate(
                            "${milestone.tanggalpenugasanpickup} ${milestone.jampenugasanpickup}",
                            PATTERN_DATE_FROM_API2,
                            PATTERN_DATE_VIEW_MILESTONE,
                        )
                    )
                }
                MILESTONE_PACKAGE_ALREADY_PICKUP -> {
                    namaKurir = milestone.namakurir.toString()
                    tanggalStatus = itemView.context.getString(
                        R.string.datetime_format, formatDate.formatedDate(
                            "${milestone.tanggalpickup} ${milestone.jampickup}",
                            PATTERN_DATE_FROM_API2,
                            PATTERN_DATE_VIEW_MILESTONE
                        )
                    )
                }
                MILESTONE_TRANSIT -> {
                    namaKurir = milestone.namakurirDelivery.toString()
                    tanggalStatus = itemView.context.getString(
                        R.string.datetime_format, formatDate.formatedDate(
                            milestone.tanggalpenugasantransit,
                            PATTERN_DATE_FROM_API,
                            PATTERN_DATE_VIEW_MILESTONE
                        )
                    )
                }
                MILESTONE_PROCESS_TRANSIT
                -> {
                    namaKurir = milestone.namakurirDelivery.toString()
                    tanggalStatus = itemView.context.getString(
                        R.string.datetime_format, formatDate.formatedDate(
                            "${milestone.tglpenugasandelivery} ${milestone.jampenugasandelivery}",
                            PATTERN_DATE_FROM_API2,
                            PATTERN_DATE_VIEW_MILESTONE
                        )
                    )
                }
                MILESTONE_IN_TRANSIT
                -> {
                    namaKurir = milestone.namakurirDelivery.toString()
                    tanggalStatus = itemView.context.getString(
                        R.string.datetime_format, formatDate.formatedDate(
                            "${milestone.tglprosesintransit} ${milestone.jamprosesintransit}",
                            PATTERN_DATE_FROM_API2,
                            PATTERN_DATE_VIEW_MILESTONE
                        )
                    )
                }
                MILESTONE_SHUTTLE
                -> {
                    namaKurir = milestone.namakurirShuttle.toString()
                    tanggalStatus = itemView.context.getString(
                        R.string.datetime_format, formatDate.formatedDate(
                            "${milestone.tanggalpenugasanshuttle} ${milestone.jampenugasanshuttle}",
                            PATTERN_DATE_FROM_API2,
                            PATTERN_DATE_VIEW_MILESTONE
                        )
                    )
                }
                MILESTONE_PROCESS_SHUTTLE -> {
                    namaKurir = milestone.namakurirShuttle.toString()
                    tanggalStatus = itemView.context.getString(
                        R.string.datetime_format, formatDate.formatedDate(
                            "${milestone.tglprosesshuttle} ${milestone.jamprosesshuttle}",
                            PATTERN_DATE_FROM_API2,
                            PATTERN_DATE_VIEW_MILESTONE
                        )
                    )
                }
                MILESTONE_RELEASE -> {
                    namaKurir = milestone.namakurirShuttle.toString()
                    tanggalStatus = itemView.context.getString(
                        R.string.datetime_format, formatDate.formatedDate(
                            "${milestone.tglrelase} ${milestone.jamrelase}",
                            PATTERN_DATE_FROM_API2,
                            PATTERN_DATE_VIEW_MILESTONE
                        )
                    )
                }
                MILESTONE_PROCESS_DELIVERY -> {
                    namaKurir = milestone.namakurirDelivery.toString()
                    tanggalStatus = itemView.context.getString(
                        R.string.datetime_format, formatDate.formatedDate(
                            "${milestone.tglpenugasandelivery} ${milestone.jampenugasandelivery}",
                            PATTERN_DATE_FROM_API2,
                            PATTERN_DATE_VIEW_MILESTONE
                        )
                    )
                }
                MILESTONE_NOT_DELIVERY
                -> {
                    namaKurir = milestone.namakurirDelivery.toString()
                    tanggalStatus = itemView.context.getString(
                        R.string.datetime_format, formatDate.formatedDate(
                            "${milestone.tglpending} ${milestone.jampending}",
                            PATTERN_DATE_FROM_API2,
                            PATTERN_DATE_VIEW_MILESTONE
                        )
                    )
                }
                MILESTONE_RETURNED_TO_SENDER -> {
                    namaKurir = milestone.namakurirDelivery.toString()
                    tanggalStatus = itemView.context.getString(
                        R.string.datetime_format, formatDate.formatedDate(
                            "${milestone.tglpenugasankembali} ${milestone.jampenugasankembali}",
                            PATTERN_DATE_FROM_API2,
                            PATTERN_DATE_VIEW_MILESTONE
                        )
                    )
                }
                MILESTONE_DELIVERY -> {
                    namaKurir = milestone.namakurirDelivery.toString()
                    tanggalStatus = itemView.context.getString(
                        R.string.datetime_format, formatDate.formatedDate(
                            "${milestone.tanggaldelivery} ${milestone.jamdelivery}",
                            PATTERN_DATE_FROM_API2,
                            PATTERN_DATE_VIEW_MILESTONE
                        )
                    )
                }
            }

            with(binding) {
                tvTanggalStatus.text = tanggalStatus
                tvStatus.text = if (milestone.statuspengiriman == MILESTONE_DELIVERY) {
                    itemView.context.getString(
                        R.string.milestone_status_pengiriman,
                        milestone.namastatuspengiriman,
                        milestone.diserahkanolehdelivery,
                    )
                } else {
                    milestone.namastatuspengiriman
                }

                tvDetailStatusPengiriman.text =
                    if (milestone.statuspengiriman == MILESTONE_PROCESS_PAYMENT) {
                        milestone.informasistatuspengiriman
                    } else {
                        itemView.context.getString(
                            R.string.milestone_detail_status_pengiriman,
                            milestone.informasistatuspengiriman,
                            namaKurir
                        )

                    }

                if (milestone.statuspengiriman == MILESTONE_DELIVERY && !milestone.fotomenyerahkandelivery.isNullOrEmpty()) {
                    viewVisible(tvFotoPenerimaPengiriman)
                    tvFotoPenerimaPengiriman.setOnClickListener {
                        onItemClickCallBack?.onItemClicked(milestone.fotomenyerahkandelivery.toString())
                    }
                }

                // lines milestone condition
                if (position == (itemCount - 1)) {
                    linesMilestoneVisibility(linesMilestone, false)
                } else {
                    linesMilestoneVisibility(linesMilestone, true)
                }
            }
        }
    }

    private fun viewVisible(view: View) {
        view.visibility = VISIBLE
    }

    private fun linesMilestoneVisibility(view: View, state: Boolean) {
        view.visibility = if (state) VISIBLE else INVISIBLE
    }

    fun setOnItemClickCallBack(onItemClickCallBack: OnItemClickCallBack) {
        this.onItemClickCallBack = onItemClickCallBack
    }

    interface OnItemClickCallBack {
        fun onItemClicked(fotoPenerima: String)
    }
}
