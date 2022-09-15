package com.pexpress.pexpresscustomer.view.main.order.checkout.ewallet

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
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
import com.bumptech.glide.Glide
import com.pexpress.pexpresscustomer.BuildConfig
import com.pexpress.pexpresscustomer.R
import com.pexpress.pexpresscustomer.databinding.FragmentEWalletPaymentBinding
import com.pexpress.pexpresscustomer.db.payments.EWalletPayment
import com.pexpress.pexpresscustomer.network.ApiConfig
import com.pexpress.pexpresscustomer.utils.*
import com.pexpress.pexpresscustomer.view.main.order.viewmodel.PaymentViewModel
import www.sanju.motiontoast.MotionToast

class EWalletPaymentFragment : Fragment() {

    private var _binding: FragmentEWalletPaymentBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<PaymentViewModel>()

    private var dataEWallet: EWalletPayment? = null
    private var isCreateEWallet = false

    private val TAG = EWalletPaymentFragment::class.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            if (isCreateEWallet) { // kalau order sudah dibuat tombol kembali ke halaman home
                findNavController().navigate(R.id.action_EWalletPaymentFragment_to_navigation_home)
            } else {
                findNavController().navigateUp()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEWalletPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar()

        val args = EWalletPaymentFragmentArgs.fromBundle(arguments as Bundle)
        dataEWallet = args.dataEwallet
        val isOrder = args.isFromOrder

        setupView()

        with(binding) {
            tiNoHandphone.setEndIconOnClickListener {
                pickContact.launch(0)
            }

            btnConfirm.apply {
                text =
                    if (isOrder) getString(R.string.pembayaran_cash_button) else getString(R.string.btn_confirm_ewallet)
                setOnClickListener {
                    if (isOrder) {
                        validation(dataEWallet)
                    } else {
                        moveToCheckOrder()
                    }
                }
            }
        }
    }

    private fun setupView() {
        with(binding) {
            dataEWallet?.let {
                tvDescPayout.text = getString(R.string.pembayaran_cash_desc, it.noInvoice)
                tvNameEwallet.text = it.nameEwallet
                Glide.with(requireContext())
                    .load(ApiConfig.URL_LOGO_EWALLET + it.icon)
                    .placeholder(R.color.grayLight)
                    .error(R.drawable.image_error)
                    .into(imgLogoEwallet)
                tvNominal.text = it.totalPayment
                edtNoHandphone.setText(normalizedNumber(dataEWallet?.mobileNumber.toString()))
            }
        }
    }

    private fun validation(dataEWallet: EWalletPayment?) {
        dataEWallet?.also {
            val noHandphone =
                getString(R.string.format_number_id, binding.edtNoHandphone.text.toString().trim())
            val amount = formatRegexRupiah(it.totalPayment).replace(".", "")
            val params = hashMapOf(
                "no_invoice" to it.noInvoice,
                "channel" to it.channel.uppercase(),
                "name" to it.name,
                "amount" to amount,
                "mobile_number" to noHandphone
            )

            if (noHandphone == "+62") {
                showMessage(
                    requireActivity(),
                    getString(R.string.failed_title),
                    "Nomor handphone harus di isi!",
                    MotionToast.TOAST_ERROR
                )
            } else {
                if (isCreateEWallet) {
                    moveToCheckOrder()
                } else {
                    observeCreateEWallet(params)
                }
            }
        }
    }

    private fun observeCreateEWallet(params: HashMap<String, String>) {
        val authorization = getAuthToken(BuildConfig.XENDIT_KEY)
        viewModel.createEWallet(authorization, params).observe(viewLifecycleOwner) { response ->
            if (response != null) {
                if (response.success!!) {
                    showMessage(
                        requireActivity(),
                        getString(R.string.text_success),
                        "Berhasil membuat order transaksi",
                        MotionToast.TOAST_SUCCESS
                    )
                    isCreateEWallet = true
                    binding.btnConfirm.text = getString(R.string.btn_confirm_ewallet)

                    // cek pembayaran yang berjalan di link url
                    val actions = response.data?.actions
                    if (actions != null) {
                        if (!actions.mobileDeeplinkCheckoutUrl.isNullOrEmpty()) { // deep link url
                            val uri = Uri.parse(actions.mobileDeeplinkCheckoutUrl)
                            startActivity(Intent(Intent.ACTION_VIEW, uri))
                            Log.d(TAG, "observeCreateEWallet: Berjalan di mobile deep link")
                        } else if (!actions.mobileWebCheckoutUrl.isNullOrEmpty()) { // web url
//                            val toEWalletWebView =
//                                EWalletPaymentFragmentDirections.actionEWalletPaymentFragmentToEWalletWebViewFragment(
//                                    actions.mobileWebCheckoutUrl
//                                )
//                            findNavController().navigate(toEWalletWebView)
                            val uri = Uri.parse(actions.mobileWebCheckoutUrl)
                            startActivity(Intent(Intent.ACTION_VIEW, uri))
                        }
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

    private fun moveToCheckOrder() {
        val toCheckOrder =
            EWalletPaymentFragmentDirections.actionEWalletPaymentFragmentToCheckOrderFragment()
                .apply {
                    noInvoice = dataEWallet?.noInvoice ?: ""
                }
        findNavController().navigate(toCheckOrder)
    }

    private val pickContact = registerForActivityResult(PickContact()) {
        it?.also { contactUri ->
            val projection = arrayOf(
                ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER
            )

            context?.contentResolver?.query(contactUri, projection, null, null, null)?.apply {
                moveToFirst()
                binding.edtNoHandphone.setText(normalizedNumber(getString(0)))
                close()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            if (isCreateEWallet) { // kalau order sudah dibuat tombol kembali ke halaman home
                findNavController().navigate(R.id.action_EWalletPaymentFragment_to_navigation_home)
            } else {
                findNavController().navigateUp()
            }
        }
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