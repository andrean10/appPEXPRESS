package com.pexpress.pexpresscustomer.view.main.ongkir

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.pexpress.pexpresscustomer.R
import com.pexpress.pexpresscustomer.databinding.FragmentOngkirBinding
import com.pexpress.pexpresscustomer.utils.setVisibilityBottomHead
import com.pexpress.pexpresscustomer.view.main.ongkir.adapter.ViewPagerOngkirAdapter

class OngkirFragment : Fragment() {

    private var _binding: FragmentOngkirBinding? = null
    private val binding get() = _binding!!
    private lateinit var pagerOngkirAdapter: ViewPagerOngkirAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOngkirBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar()
        setupView()
    }

    override fun onResume() {
        super.onResume()
        setVisibilityBottomHead(requireActivity(), false)
    }

    private fun setupView() {
        pagerOngkirAdapter = ViewPagerOngkirAdapter(this)

        with(binding) {
            viewPager.adapter = pagerOngkirAdapter

            TabLayoutMediator(tabs, viewPager) { tab, position ->
                when (position) {
                    0 -> tab.text = getString(R.string.tab_ongkir_1)
                    1 -> tab.text = getString(R.string.tab_ongkir_2)
                }
            }.attach()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) findNavController().navigateUp()
        return super.onOptionsItemSelected(item)
    }

    private fun setToolbar() {
        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.toolbar)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}