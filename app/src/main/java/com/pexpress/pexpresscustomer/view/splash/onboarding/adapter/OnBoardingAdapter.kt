package com.pexpress.pexpresscustomer.view.splash.onboarding.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pexpress.pexpresscustomer.databinding.ItemsOnboardingScreenBinding
import com.pexpress.pexpresscustomer.db.ScreenItem

class OnBoardingAdapter(private val screenItem: List<ScreenItem>) :
    RecyclerView.Adapter<OnBoardingAdapter.OnBoardingViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardingViewHolder {
        val binding =
            ItemsOnboardingScreenBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OnBoardingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OnBoardingViewHolder, position: Int) {
        holder.bind(screenItem[position])
    }

    override fun getItemCount() = screenItem.size

    inner class OnBoardingViewHolder(private val binding: ItemsOnboardingScreenBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(screenItem: ScreenItem) {
            with(binding) {
                imgOnboarding.setImageResource(screenItem.onBoardingImg)
                tvTitleOnboarding.text = screenItem.title
                tvDescOnboarding.text = screenItem.description
            }
        }
    }
}
