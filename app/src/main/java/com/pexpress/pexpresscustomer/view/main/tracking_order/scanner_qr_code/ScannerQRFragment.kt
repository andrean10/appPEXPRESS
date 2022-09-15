package com.pexpress.pexpresscustomer.view.main.tracking_order.scanner_qr_code

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.pexpress.pexpresscustomer.databinding.FragmentScannerQrBinding

class ScannerQRFragment : Fragment() {

    private var _binding: FragmentScannerQrBinding? = null
    private val binding get() = _binding!!
    private lateinit var codeScanner: CodeScanner

    private val TAG = ScannerQRFragment::class.simpleName

    companion object {
        const val EXTRA_DATA = "extra_data"
        const val EXTRA_ID_TRACKING = "extra_id_tracking"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScannerQrBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {
        with(binding) {
            codeScanner = CodeScanner(requireContext(), scannerView)

            codeScanner.decodeCallback = DecodeCallback {
                requireActivity().runOnUiThread {
                    val bundle = Bundle().apply {
                        putString(EXTRA_ID_TRACKING, it.text)
                    }
                    setFragmentResult(EXTRA_DATA, bundle)
                    findNavController().navigateUp()
                }
            }

            codeScanner.errorCallback = ErrorCallback {
                requireActivity().runOnUiThread {
                    Log.d(TAG, "setupView: Error = ${it.message}")
                    Toast.makeText(requireContext(), "Gagal Scan!", Toast.LENGTH_SHORT).show()
                }
            }

            scannerView.setOnClickListener {
                codeScanner.startPreview()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}