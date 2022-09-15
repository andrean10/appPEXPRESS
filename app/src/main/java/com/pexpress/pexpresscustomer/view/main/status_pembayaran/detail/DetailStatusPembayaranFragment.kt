package com.pexpress.pexpresscustomer.view.main.status_pembayaran.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.pexpress.pexpresscustomer.R
import com.pexpress.pexpresscustomer.databinding.FragmentDetailStatusPembayaranBinding
import com.pexpress.pexpresscustomer.model.payment.ResultDetailStatusPembayaran
import com.pexpress.pexpresscustomer.utils.FormatDate
import com.pexpress.pexpresscustomer.utils.UtilsCode
import com.pexpress.pexpresscustomer.utils.formatRupiah
import com.pexpress.pexpresscustomer.utils.showMessage
import com.pexpress.pexpresscustomer.view.main.status_pembayaran.viewmodel.StatusPembayaranViewModel
import www.sanju.motiontoast.MotionToast

class DetailStatusPembayaranFragment : Fragment() {

    private var _binding: FragmentDetailStatusPembayaranBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<StatusPembayaranViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailStatusPembayaranBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isLoading(true)
        val noInvoice = DetailStatusPembayaranFragmentArgs.fromBundle(arguments as Bundle).noInvoice
        observeDetailPembayaran(noInvoice)
    }

    private fun setupView(result: ResultDetailStatusPembayaran?) {
        with(binding) {
            result?.let {
                when (result.status) {
                    "PAID" -> {
                        lottieAnimPayment.setAnimation(R.raw.success_payment)
                        lottieAnimPayment.playAnimation()
                        tvStatusPayment.text = getString(R.string.detail_title_status_succes)
                        tvStatusPaymentDetail.text =
                            getString(R.string.detail_body_status_succes, result.nomorpemesanan)
                    }
                    "FAILED" -> {
                        lottieAnimPayment.setAnimation(R.raw.failed_payment)
                        lottieAnimPayment.playAnimation()
                        tvStatusPayment.text = getString(R.string.detail_title_status_failed)
                        tvStatusPaymentDetail.text =
                            getString(R.string.detail_body_status_failed, result.nomorpemesanan)
                    }
                    "EXPIRED" -> {
                        lottieAnimPayment.setAnimation(R.raw.failed_payment)
                        lottieAnimPayment.playAnimation()
                        tvStatusPayment.text = getString(R.string.detail_title_status_expired)
                        tvStatusPaymentDetail.text =
                            getString(R.string.detail_body_status_expired, result.nomorpemesanan)
                    }
                }

                tvJenisPembayaran.text = result.jenispembayaran
                result.paytime?.let {
                    tvPlaceholderWaktuBayar.visibility = View.VISIBLE
                    tvWaktuBayar.visibility = View.VISIBLE
                    tvWaktuBayar.text = FormatDate().formatedDate(
                        it,
                        UtilsCode.PATTERN_DATE_FROM_API,
                        UtilsCode.PATTERN_DATE_VIEW_STATUS_PEMBAYARAN
                    )
                }
                tvOngkir.text = formatRupiah(result.tagihan?.toDouble() ?: 0.0)
                when (result.jenispembayaran) {
                    "Pembayaran Tunai" -> {
                        tvPlaceholderNamaBank.visibility = View.GONE
                        tvPlaceholderEwallet.visibility = View.GONE
                        tvBank.visibility = View.GONE
                        tvEwallet.visibility = View.GONE
                    }
                    "Pembayaran Transfer" -> {
                        tvPlaceholderNamaBank.visibility = View.VISIBLE
                        tvBank.visibility = View.VISIBLE
                        tvBank.text = result.bank
                        tvPlaceholderEwallet.visibility = View.GONE
                        tvEwallet.visibility = View.GONE
                    }
                    "Pembayaran Ewallet" -> {
                        tvPlaceholderEwallet.visibility = View.VISIBLE
                        tvEwallet.visibility = View.VISIBLE
                        tvEwallet.text = result.ewallet
                        tvPlaceholderNamaBank.visibility = View.GONE
                        tvBank.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun observeDetailPembayaran(noInvoice: String) {
        viewModel.getDetailStatusPembayaran(noInvoice).observe(viewLifecycleOwner) { response ->
            if (response != null) {
                if (response.success!!) {
                    response.data?.get(0).let {
                        setupView(it)
                    }
                    isLoading(false)
                } else {
                    showMessage(
                        requireActivity(),
                        getString(R.string.failed_title),
                        response.message.toString(),
                        MotionToast.TOAST_ERROR
                    )
                    isLoading(true)
                }
            } else {
                showMessage(
                    requireActivity(),
                    getString(R.string.failed_title),
                    getString(R.string.failed_description),
                    MotionToast.TOAST_ERROR
                )
                isLoading(true)
            }
        }
    }

    private fun isLoading(state: Boolean) {
        with(binding) {
            if (state) {
                pbDetailPembayaran.visibility = View.VISIBLE
                layoutDetailStatusPembayaran.visibility = View.GONE
            } else {
                pbDetailPembayaran.visibility = View.GONE
                layoutDetailStatusPembayaran.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}