package com.pexpress.pexpresscustomer.view.main.order.checkout

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.pexpress.pexpresscustomer.BuildConfig
import com.pexpress.pexpresscustomer.R
import com.pexpress.pexpresscustomer.databinding.FragmentCheckoutBinding
import com.pexpress.pexpresscustomer.db.payments.CashPayment
import com.pexpress.pexpresscustomer.db.payments.EWalletPayment
import com.pexpress.pexpresscustomer.db.payments.VAPayment
import com.pexpress.pexpresscustomer.model.checkout.asuransi.ResultAsuransi
import com.pexpress.pexpresscustomer.model.type_payments.ResultEWallet
import com.pexpress.pexpresscustomer.model.type_payments.ResultVA
import com.pexpress.pexpresscustomer.network.ApiConfig
import com.pexpress.pexpresscustomer.utils.UtilsCode.CASH_PAYMENT_CODE
import com.pexpress.pexpresscustomer.utils.UtilsCode.EWALLET_PAYMENT_CODE
import com.pexpress.pexpresscustomer.utils.UtilsCode.QRIS_PAYMENT_CODE
import com.pexpress.pexpresscustomer.utils.UtilsCode.TAG
import com.pexpress.pexpresscustomer.utils.UtilsCode.VA_PAYMENT_CODE
import com.pexpress.pexpresscustomer.utils.formatRegexRupiah
import com.pexpress.pexpresscustomer.utils.formatRupiah
import com.pexpress.pexpresscustomer.utils.getAuthToken
import com.pexpress.pexpresscustomer.utils.showMessage
import com.pexpress.pexpresscustomer.view.main.order.dialog.e_wallet.EWalletDialogFragment
import com.pexpress.pexpresscustomer.view.main.order.dialog.qrcode.QRCodeDialogFragment
import com.pexpress.pexpresscustomer.view.main.order.dialog.va.VADialogFragment
import com.pexpress.pexpresscustomer.view.main.order.viewmodel.CheckoutViewModel
import com.pexpress.pexpresscustomer.view.main.order.viewmodel.PaymentViewModel
import www.sanju.motiontoast.MotionToast


class CheckoutFragment : Fragment() {

    private var _binding: FragmentCheckoutBinding? = null
    private val binding get() = _binding!!
    private val viewModelCheckout by activityViewModels<CheckoutViewModel>()
    private val viewModelPayment by viewModels<PaymentViewModel>()

    private lateinit var args: CheckoutFragmentArgs
    private var defaultColorBackground = 0
    private var selectColorBackround = 0

    private lateinit var modalVirtualAccount: VADialogFragment
    private lateinit var modalEWallet: EWalletDialogFragment
    private lateinit var modalQRIS: QRCodeDialogFragment

    private var paymentCode = 0
    private var totalPayment = 0.0

    private var resultAsuransi: ResultAsuransi? = null
    private var resultVA: ResultVA? = null
    private var resultEWallet: ResultEWallet? = null
    private var params: HashMap<String, String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar()

        args = CheckoutFragmentArgs.fromBundle(arguments as Bundle)
        setupView()
    }

    private fun setupView() {
        viewModelCheckout.asuransi()
        with(binding) {
            modalVirtualAccount = VADialogFragment()
            modalEWallet = EWalletDialogFragment()
            modalQRIS = QRCodeDialogFragment()

            observeAsuransi()

            totalPayment = args.totalPayment.toDouble()
            tvJumlahPembayaran.text = formatRupiah(totalPayment)
            tvTotalPay.text = formatRupiah(totalPayment)

            defaultColorBackground = cdMethodCash.cardBackgroundColor.defaultColor
            selectColorBackround = cdMethodCash.rippleColor.defaultColor

            cdMethodCash.setOnClickListener { menuPaymentClickListener(cdMethodCash) }
            cdMethodTransfer.setOnClickListener { menuPaymentClickListener(cdMethodTransfer) }
            cdMethodEwallet.setOnClickListener { menuPaymentClickListener(cdMethodEwallet) }
//            cdMethodQris.setOnClickListener { menuPaymentClickListener(cdMethodQris) }
            checkboxAsuransiPengiriman.setOnCheckedChangeListener { _, state ->
                tvTotalPay.text = formatRupiah(
                    if (state) {
                        var priceAsuransi = 0.0
                        if (viewModelCheckout.isReadyDataAsuransi.value == true) {
                            priceAsuransi = resultAsuransi?.value?.toDouble() ?: 0.0
                        }
                        totalPayment + priceAsuransi
                    } else {
                        totalPayment
                    }
                )
            }

            btnPayClick()
            observeVAPayment()
            observeEWalletPayment()
        }
    }

    private fun btnPayClick() {
        with(binding) {
            btnPayNow.setOnClickListener {
                val isCheckedAsuransi = checkboxAsuransiPengiriman.isChecked
                val totalPayFormat =
                    formatRegexRupiah(tvTotalPay.text.toString().trim()).replace(".", "")

                val paramsAsuransi = hashMapOf(
                    "no_invoice" to args.noInvoice,
                    "biaya" to totalPayFormat
                )

                when (paymentCode) {
                    CASH_PAYMENT_CODE -> {
                        if (isCheckedAsuransi) {
                            observeAddAsuransi(paramsAsuransi)
                        } else {
                            moveToCashPayment(totalPayFormat)
                        }
                    }
                    VA_PAYMENT_CODE -> {
                        if (isCheckedAsuransi) {
                            observeAddAsuransi(paramsAsuransi)
                        } else {
                            val totalPay = formatRegexRupiah(
                                binding.tvTotalPay.text.toString().trim()
                            ).replace(".", "")
                            params?.let {
                                it["amount"] = totalPay
                                observeCreateVA(it)
                            }
                        }
                    }
                    EWALLET_PAYMENT_CODE -> {
                        val dataEWallet = EWalletPayment(
                            args.noInvoice,
                            resultEWallet?.channel.toString(),
                            resultEWallet?.nama.toString(),
                            args.name,
                            "",
                            binding.tvTotalPay.text.toString().trim(),
                            resultEWallet?.namafile.toString()
                        )
                        if (isCheckedAsuransi) {
                            observeAddAsuransi(
                                paramsAsuransi, dataEWallet = dataEWallet
                            )
                        } else {
                            moveToEWalletPayment(dataEWallet)
                        }
                    }
                    QRIS_PAYMENT_CODE -> moveToQRPaymentDialog()
                }
            }
        }
    }

    private fun observeAsuransi() {
        viewModelCheckout.asuransi.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                if (response.success!!) {
                    resultAsuransi = response.data?.get(0)
                    binding.tvPriceAsuransi.text =
                        formatRupiah(response.data?.get(0)?.value?.toDouble() ?: 0.0)
                }
            }
        }
    }

    private fun observeVAPayment() {
        with(binding) {
            viewModelCheckout.vaPayment.observe(viewLifecycleOwner) { result ->
                if (result != null) {
                    resultVA = result
                    Glide.with(requireContext())
                        .load(ApiConfig.URL_LOGO_VA + result.namafile)
                        .into(imgLogoBank)
                    tvNameBank.text = result.name

                    // hide ewallet choose
                    layoutChooseTransfer.visibility = VISIBLE
                    layoutChooseEwallet.visibility = GONE

                    // set pembayaran yang dipilih user
                    paymentCode = VA_PAYMENT_CODE
                    // set choose item background
                    cdMethodTransfer.setBackgroundColor(selectColorBackround)
                    cdMethodCash.setBackgroundColor(defaultColorBackground)
                    cdMethodEwallet.setBackgroundColor(defaultColorBackground)
//                    cdMethodQris.setBackgroundColor(defaultColorBackground)
                    btnPayNow.isEnabled = true

                    params = hashMapOf(
                        "no_invoice" to args.noInvoice,
                        "bank" to result.code.toString(),
                        "name" to args.name,
                    )
                }
            }
        }
    }

    private fun observeCreateVA(params: HashMap<String, String>) {
        val authorization = getAuthToken(BuildConfig.XENDIT_KEY)
        viewModelPayment.createVA(authorization, params).observe(viewLifecycleOwner) { response ->
            if (response != null) {
                if (response.success!!) {
                    response.data?.let {
                        val dataVA = VAPayment(
                            args.noInvoice,
                            it.accountNumber.toString(),
                            params["bank"].toString(),
                            args.name,
                            binding.tvTotalPay.text.toString().trim()
                        )

                        moveToVAPayment(dataVA)
                    }
                } else {
                    showMessage(
                        requireActivity(),
                        getString(R.string.failed_title),
                        response.message.toString(),
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

    private fun observeEWalletPayment() {
        with(binding) {
            viewModelCheckout.ewalletPayment.observe(viewLifecycleOwner) { result ->
                if (result != null) {
                    resultEWallet = result

                    Glide.with(requireContext())
                        .load(ApiConfig.URL_LOGO_EWALLET + result.namafile)
                        .error(R.drawable.image_error)
                        .into(imgLogoEwallet)
                    tvNameEwallet.text = result.nama

                    // hide transfer va choose
                    layoutChooseEwallet.visibility = VISIBLE
                    layoutChooseTransfer.visibility = GONE

                    paymentCode = EWALLET_PAYMENT_CODE
                    cdMethodEwallet.setBackgroundColor(selectColorBackround)
                    cdMethodCash.setBackgroundColor(defaultColorBackground)
                    cdMethodTransfer.setBackgroundColor(defaultColorBackground)
//                    cdMethodQris.setBackgroundColor(defaultColorBackground)
                    btnPayNow.isEnabled = true
                }
            }
        }
    }

    private fun observeAddAsuransi(
        paramsAsuransi: HashMap<String, String>,
        dataEWallet: EWalletPayment? = null
    ) {
        viewModelCheckout.checkoutAddAsuransi(paramsAsuransi)
            .observe(viewLifecycleOwner) { response ->
                if (response != null) {
                    if (response.success!!) {
                        when (paymentCode) {
                            CASH_PAYMENT_CODE -> moveToCashPayment(paramsAsuransi["biaya"].toString())
                            VA_PAYMENT_CODE -> {
                                val totalPay = formatRegexRupiah(
                                    binding.tvTotalPay.text.toString().trim()
                                ).replace(".", "")
                                params?.let {
                                    it["amount"] = totalPay
                                    observeCreateVA(it)
                                }
                            }
                            EWALLET_PAYMENT_CODE -> moveToEWalletPayment(dataEWallet!!)
                        }
                    } else {
                        showMessage(
                            requireActivity(),
                            getString(R.string.failed_title),
                            response.message.toString(),
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

    private fun moveToCashPayment(totalPay: String) {
        val formatTotalPay = formatRupiah(totalPay.toDouble())
        val toCashPayment =
            CheckoutFragmentDirections.actionCheckoutFragmentToCashPaymentFragment(
                CashPayment(
                    args.noInvoice,
                    args.name,
                    formatTotalPay
                )
            ).apply {
                isFromOrder = true
            }
        findNavController().navigate(toCashPayment)
    }

    private fun moveToVAPayment(dataVA: VAPayment) {
        val toVAPayment =
            CheckoutFragmentDirections.actionCheckoutFragmentToVaPaymentFragment(dataVA)
        findNavController().navigate(toVAPayment)
    }

    private fun moveToEWalletPayment(dataEWallet: EWalletPayment) {
        Log.d(TAG, "moveToEWalletPayment: ${dataEWallet.noInvoice}")
        val toEWalletPayment =
            CheckoutFragmentDirections.actionCheckoutFragmentToEWalletPaymentFragment(dataEWallet)
                .apply {
                    isFromOrder = true
                }
        findNavController().navigate(toEWalletPayment)
    }

    private fun moveToVAPaymentDialog() {
        modalVirtualAccount.show(parentFragmentManager, VADialogFragment.TAG)
    }

    private fun moveToEWalletPaymentDialog() {
        modalEWallet.show(parentFragmentManager, EWalletDialogFragment.TAG)
    }

    private fun moveToQRPaymentDialog() {
        modalQRIS.show(parentFragmentManager, QRCodeDialogFragment.TAG)
    }

    private fun menuPaymentClickListener(menu: MaterialCardView) {
        with(binding) {
            when (menu.id) {
                R.id.cd_method_cash -> {
                    paymentCode = CASH_PAYMENT_CODE
                    menu.setBackgroundColor(selectColorBackround)
                    cdMethodTransfer.setBackgroundColor(defaultColorBackground)
                    cdMethodEwallet.setBackgroundColor(defaultColorBackground)
//                    cdMethodQris.setBackgroundColor(defaultColorBackground)

                    // hide choose transfer va and ewallet
                    layoutChooseTransfer.visibility = GONE
                    layoutChooseEwallet.visibility = GONE
                    btnPayNow.isEnabled = true
                }
                R.id.cd_method_transfer -> moveToVAPaymentDialog()
                R.id.cd_method_ewallet -> moveToEWalletPaymentDialog()
//                R.id.cd_method_qris -> {
//                    paymentCode = QRIS_PAYMENT_CODE
//                    menu.setBackgroundColor(selectColorBackround)
//                    cdMethodCash.setBackgroundColor(defaultColorBackground)
//                    cdMethodTransfer.setBackgroundColor(defaultColorBackground)
//                    cdMethodEwallet.setBackgroundColor(defaultColorBackground)
//
//                    // hide choose transfer va and ewallet
//                    layoutChooseTransfer.visibility = GONE
//                    layoutChooseEwallet.visibility = GONE
//                    btnPayNow.isEnabled = true
//                }
                else -> {
                    Log.d(TAG, "menuPaymentClickListener: Id tidak ditemukan")
                }
            }
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
        viewModelCheckout.apply {
            removeVAPayment()
            removeEWalletPayment()
        }
    }
}