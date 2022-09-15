package com.pexpress.pexpresscustomer.view.main.order.dialog.jenis_barang.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pexpress.pexpresscustomer.R
import com.pexpress.pexpresscustomer.databinding.ItemsJenisBarangBinding
import com.pexpress.pexpresscustomer.model.order.ResultJenisBarang

class JenisBarangAdapter : RecyclerView.Adapter<JenisBarangAdapter.ItemSizeViewHolder>() {

    private var onItemClickCallBack: OnItemClickCallBack? = null
    private val itemJenisBarang = arrayListOf<ResultJenisBarang>()

    fun setJenisBarang(jenisBarang: List<ResultJenisBarang>?) {
        if (jenisBarang == null) return
        itemJenisBarang.clear()
        itemJenisBarang.addAll(jenisBarang)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ItemSizeViewHolder {
        val binding =
            ItemsJenisBarangBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemSizeViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ItemSizeViewHolder, position: Int) {
        viewHolder.bind(itemJenisBarang[position])
    }

    override fun getItemCount() = itemJenisBarang.size


    inner class ItemSizeViewHolder(private val binding: ItemsJenisBarangBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(result: ResultJenisBarang) {
            with(binding) {
                tvNamaJenisBarang.text = result.namajenisbarang
                tvDimensionSize.text = itemView.context.getString(
                    R.string.dimension_all_size,
                    result.panjangmaksimal.toString(),
                    result.satuanpanjang,
                    result.lebarmaksimal.toString(),
                    result.satuanpanjang,
                    result.tinggimaksimal.toString(),
                    result.satuanpanjang,
                )
                tvWeightMax.text = itemView.context.getString(
                    R.string.dimension_weight_size,
                    result.beratmaksimal.toString(),
                    result.satuanberat
                )
            }

            itemView.setOnClickListener { onItemClickCallBack?.onItemClicked(result) }
        }
    }

    fun setOnItemClickCallBack(onItemClickCallBack: OnItemClickCallBack) {
        this.onItemClickCallBack = onItemClickCallBack
    }

    interface OnItemClickCallBack {
        fun onItemClicked(result: ResultJenisBarang)
    }
}
