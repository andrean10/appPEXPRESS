package com.pexpress.pexpresscustomer.view.main.order.dialog.qrcode

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pexpress.pexpresscustomer.databinding.FragmentQRCodeDialogBinding

class QRCodeDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentQRCodeDialogBinding? = null
    private val binding get() = _binding!!

    companion object {
        val TAG = QRCodeDialogFragment::class.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentQRCodeDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {
        with(binding) {

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}