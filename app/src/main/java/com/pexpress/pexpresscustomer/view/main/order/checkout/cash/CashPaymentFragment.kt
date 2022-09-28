package com.pexpress.pexpresscustomer.view.main.order.checkout.cash

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.pexpress.pexpresscustomer.BuildConfig
import com.pexpress.pexpresscustomer.R
import com.pexpress.pexpresscustomer.databinding.FragmentCashPaymentBinding
import com.pexpress.pexpresscustomer.db.payments.CashPayment
import com.pexpress.pexpresscustomer.utils.UtilsCode.TAG
import com.pexpress.pexpresscustomer.utils.formatRegexRupiah
import com.pexpress.pexpresscustomer.utils.getAuthToken
import com.pexpress.pexpresscustomer.utils.setVisibilityBottomHead
import com.pexpress.pexpresscustomer.utils.showMessage
import com.pexpress.pexpresscustomer.view.main.order.viewmodel.PaymentViewModel
import www.sanju.motiontoast.MotionToast

class CashPaymentFragment : Fragment() {

    private var _binding: FragmentCashPaymentBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<PaymentViewModel>()

    private lateinit var authorization: String
    private lateinit var dataCash: CashPayment
    private var isOrder = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCashPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar()

        val args = CashPaymentFragmentArgs.fromBundle(arguments as Bundle)
        dataCash = args.dataCash
        isOrder = args.isFromOrder

        Log.d(TAG, "onViewCreated: $isOrder")

        setupView()

        with(binding.btnConfirm) {
            text =
                if (isOrder) getString(R.string.pembayaran_cash_button) else getString(R.string.btn_confirm_ewallet)
            setOnClickListener {
                if (isOrder) {
                    moveToCreateOrder()
                } else {
                    moveToCheckOrder()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (!isOrder) setVisibilityBottomHead(requireActivity(), false)
    }

    private fun setupView() {
        with(binding) {
            tvDescPayout.text = getString(R.string.pembayaran_cash_desc, dataCash.noInvoice)
            tvNominal.text = dataCash.totalPayment
        }
    }

    private fun moveToCreateOrder() {
        authorization = getAuthToken(BuildConfig.XENDIT_KEY)
        val params = hashMapOf(
            "no_invoice" to dataCash.noInvoice,
            "name" to dataCash.name,
            "amount" to formatRegexRupiah(dataCash.totalPayment).replace(".", "")
        )

        viewModel.createCash(authorization, params).observe(viewLifecycleOwner) { response ->
            if (response != null) {
                if (response.success!!) {
                    moveToCheckOrder()
                } else {
                    showMessage(
                        requireActivity(),
                        getString(R.string.failed_title),
                        "Transaksi Gagal Diproses silahkan coba lagi",
                        MotionToast.TOAST_ERROR
                    )
                }
            } else {
                showMessage(
                    requireActivity(),
                    getString(R.string.failed_title),
                    getString(R.string.failed_description),
                    MotionToast.TOAST_ERROR
                )
            }
        }
    }

    private fun moveToCheckOrder() {
        val toCheckOrder =
            CashPaymentFragmentDirections.actionCashPaymentFragmentToCheckOrderFragment()
                .apply {
                    noInvoice = dataCash.noInvoice
                    isCashPayment = true
                }
        findNavController().navigate(toCheckOrder)
    }

    private fun setToolbar() {
        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.toolbar)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            findNavController().navigateUp()
//            if (isOrder) {
//                findNavController().navigate(R.id.action_cashPaymentFragment_to_navigation_home)
//            } else {
//                findNavController().navigateUp()
//            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}