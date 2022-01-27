package com.pexpress.pexpresscustomer.view.main.order.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.pexpress.pexpresscustomer.R
import com.pexpress.pexpresscustomer.databinding.FragmentPayoutBinding

class PayoutFragment : Fragment() {

    private var _binding: FragmentPayoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentPayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar()

        with(binding) {
            btnCopyVa.setOnClickListener {
                Toast.makeText(
                    requireContext(),
                    "Nomor Va telah disalin",
                    Toast.LENGTH_SHORT
                ).show()
            }
            btnCopyNominal.setOnClickListener {
                Toast.makeText(
                    requireContext(),
                    "Nominal pembayaran telah disalin",
                    Toast.LENGTH_SHORT
                ).show()
            }
            btnBookNow.setOnClickListener { findNavController().navigate(R.id.action_payoutFragment_to_completeOrderFragment) }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) findNavController().navigateUp()
        return super.onOptionsItemSelected(item)
    }

    private fun setToolbar() {
        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.toolbar)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}