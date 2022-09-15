package com.pexpress.pexpresscustomer.view.main.status_order.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pexpress.pexpresscustomer.view.main.status_order.tabview.StatusOrderFragment

class ViewPagerHistoryAdapter(fa: Fragment) : FragmentStateAdapter(fa) {

    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> StatusOrderFragment(StatusOrderFragment.STATUS_PROCESS)
            1 -> StatusOrderFragment(StatusOrderFragment.STATUS_FINISH)
            2 -> StatusOrderFragment(StatusOrderFragment.STATUS_CANCEL)
            else -> StatusOrderFragment(StatusOrderFragment.STATUS_PROCESS)
        }
    }
}