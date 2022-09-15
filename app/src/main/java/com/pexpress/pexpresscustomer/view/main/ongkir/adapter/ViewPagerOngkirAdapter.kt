package com.pexpress.pexpresscustomer.view.main.ongkir.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pexpress.pexpresscustomer.view.main.ongkir.tabview.TarifFixRateOngkirFragment
import com.pexpress.pexpresscustomer.view.main.ongkir.tabview.TarifKilometerOngkirFragment

class ViewPagerOngkirAdapter(private val fa: Fragment) : FragmentStateAdapter(fa) {

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TarifFixRateOngkirFragment()
            1 -> TarifKilometerOngkirFragment()
            else -> TarifFixRateOngkirFragment()
        }
    }
}