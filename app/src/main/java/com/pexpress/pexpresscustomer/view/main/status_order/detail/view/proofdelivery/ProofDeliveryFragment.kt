package com.pexpress.pexpresscustomer.view.main.status_order.detail.view.proofdelivery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.pexpress.pexpresscustomer.R
import com.pexpress.pexpresscustomer.databinding.FragmentProofDeliveryBinding
import com.pexpress.pexpresscustomer.network.ApiConfig

class ProofDeliveryFragment : Fragment() {

    private var _binding: FragmentProofDeliveryBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProofDeliveryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val img = ProofDeliveryFragmentArgs.fromBundle(arguments as Bundle).img

        setToolbar()
        setupView(img)
    }

    private fun setupView(img: String) {
        Glide.with(requireActivity())
            .asBitmap()
            .load(ApiConfig.URL + img)
            .placeholder(R.color.black)
            .error(R.drawable.image_error)
            .into(binding.imgProofOfDelivery)
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