package com.pexpress.pexpresscustomer.view.main.status_order.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pexpress.pexpresscustomer.view.main.status_order.tabview.StatusOrderCancelFragment
import com.pexpress.pexpresscustomer.view.main.status_order.tabview.StatusOrderFinishFragment
import com.pexpress.pexpresscustomer.view.main.status_order.tabview.StatusOrderProcessFragment

class ViewPagerHistoryAdapter(fa: Fragment) : FragmentStateAdapter(fa) {

    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> StatusOrderProcessFragment()
            1 -> StatusOrderFinishFragment()
            2 -> StatusOrderCancelFragment()
            else -> StatusOrderProcessFragment()
        }
    }
}