package com.pexpress.pexpresscustomer.view.main.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.pexpress.pexpresscustomer.databinding.ItemsSliderBinding
import com.smarteist.autoimageslider.SliderViewAdapter

class HomeSliderAdapter(private val sliderItems: List<Int>) :
    SliderViewAdapter<HomeSliderAdapter.HomeSliderViewHolder>() {
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
        fun bind(item: Int) {
            Glide.with(itemView.context)
                .load(item)
                .into(binding.imgSlider)
        }
    }
}
