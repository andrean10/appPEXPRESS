package com.pexpress.pexpresscustomer.view.main.order.checkout.ewallet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pexpress.pexpresscustomer.databinding.FragmentEWalletWebViewBinding

class EWalletWebViewFragment : Fragment() {

    private var _binding: FragmentEWalletWebViewBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEWalletWebViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {
        val url = EWalletWebViewFragmentArgs.fromBundle(arguments as Bundle).url
        binding.webView.apply {
            loadUrl(url)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}