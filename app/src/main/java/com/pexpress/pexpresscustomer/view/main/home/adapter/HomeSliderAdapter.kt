package com.pexpress.pexpresscustomer.view.main.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.pexpress.pexpresscustomer.R
import com.pexpress.pexpresscustomer.databinding.ItemsSliderBinding
import com.pexpress.pexpresscustomer.model.main.BannerItem
import com.pexpress.pexpresscustomer.network.ApiConfig
import com.smarteist.autoimageslider.SliderViewAdapter

class HomeSliderAdapter : SliderViewAdapter<HomeSliderAdapter.HomeSliderViewHolder>() {
    private val sliderItems = arrayListOf<BannerItem>()

    fun setData(banner: List<BannerItem>?) {
        if (banner == null) return
        sliderItems.clear()
        sliderItems.addAll(banner)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup?): HomeSliderViewHolder {
        val binding =
            ItemsSliderBinding.inflate(LayoutInflater.from(parent?.context), parent, false)
        return HomeSliderViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: HomeSliderViewHolder, position: Int) {
        viewHolder.bind(sliderItems[position])
    }

    override fun getCount() = sliderItems.size

    inner class HomeSliderViewHolder(private val binding: ItemsSliderBinding) :
        SliderViewAdapter.ViewHolder(binding.root) {
        fun bind(item: BannerItem) {
            Glide.with(itemView.context)
                .asBitmap()
                .load("${ApiConfig.URL_IMG_BANNER}${item.namafile}")
                .placeholder(R.drawable.img_placeholder)
                .error(R.drawable.image_error)
                .fallback(R.drawable.image_error)
                .into(binding.imgSlider)
        }
    }
}
