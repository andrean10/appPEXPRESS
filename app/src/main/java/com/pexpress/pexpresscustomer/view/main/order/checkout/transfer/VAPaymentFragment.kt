package com.pexpress.pexpresscustomer.view.main.order.checkout.transfer

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.pexpress.pexpresscustomer.R
import com.pexpress.pexpresscustomer.databinding.FragmentVaPaymentBinding
import com.pexpress.pexpresscustomer.db.payments.VAPayment
import com.pexpress.pexpresscustomer.utils.FormatDate
import com.pexpress.pexpresscustomer.utils.UtilsCode.PATTERN_DATE_FROM_API
import com.pexpress.pexpresscustomer.utils.UtilsCode.PATTERN_TIME_VIEW_MILESTONE
import com.pexpress.pexpresscustomer.utils.formatRegexRupiah
import java.text.SimpleDateFormat
import java.util.*

class VAPaymentFragment : Fragment() {

    private var _binding: FragmentVaPaymentBinding? = null
    private val binding get() = _binding!!
    private lateinit var dataVA: VAPayment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigate(R.id.action_vaPaymentFragment_to_navigation_home)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVaPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar()

        dataVA = VAPaymentFragmentArgs.fromBundle(arguments as Bundle).dataVa
        prepareDataPayout()

        with(binding) {
            btnCopyVa.setOnClickListener {
                copyText(
                    tvVa.text.toString().trim(),
                    getString(R.string.pembayaran_placeholder_copy_text2)
                )
            }
            btnCopyNominal.setOnClickListener {
                copyText(
                    formatRegexRupiah(tvNominal.text.toString().trim()).replace(".", ""),
                    getString(R.string.pembayaran_placeholder_copy_text)
                )
            }
            btnBookNow.setOnClickListener { checkPayment() }
        }
    }

    private fun prepareDataPayout() {
        with(binding) {
            tvDescPayout.text = getString(R.string.pembayaran_message, dataVA.noInvoice)
            tvVa.text = dataVA.va
            tvNominal.text = dataVA.totalPayment
            tvTimePayout.text = getString(
                R.string.wib_format, if (dataVA.expiredTime != null) {
                    formatTimeByStatusPembayaran(dataVA.expiredTime!!)
                } else {
                    formatTimeNow()
                }
            )
        }
    }

    private fun copyText(text: String, message: String) {
        val myClipboard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val myClip = ClipData.newPlainText("Label", text)
        myClipboard.setPrimaryClip(myClip)

        Toast.makeText(
            requireContext(),
            message,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun formatTimeNow(): String {
//        var deadlineTime = ""
//        viewModel.deadlineTransaction.observe(viewLifecycleOwner, { deadline ->
//            if (deadline == null) {
//                val now = Calendar.getInstance(Locale.getDefault())
//                now.add(Calendar.MINUTE, 15)
////                val formatIncoming = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH)
//                val formatOutgoing = SimpleDateFormat("HH:mm", Locale.getDefault())
//                val tz = TimeZone.getTimeZone("Asia/Jakarta")
//                formatOutgoing.timeZone = tz
////                deadlineTime = formatOutgoing.format(formatIncoming.parse(now.time.toString()))
//                deadlineTime = formatOutgoing.format(now.time)
//
//                viewModel.setDeadlineTransaction(deadlineTime)
//            } else {
//                deadlineTime = deadline
//            }
//        })

        val now = Calendar.getInstance(Locale.getDefault())
        now.add(Calendar.MINUTE, 15)
        val formatOutgoing = SimpleDateFormat("HH:mm", Locale.getDefault())
        val tz = TimeZone.getTimeZone("Asia/Jakarta")
        formatOutgoing.timeZone = tz
        return formatOutgoing.format(now.time)
    }

    private fun formatTimeByStatusPembayaran(date: String): String {
        return FormatDate().formatedDate(
            date,
            PATTERN_DATE_FROM_API,
            PATTERN_TIME_VIEW_MILESTONE
        )
    }

    private fun checkPayment() {
        val toCheckPayment =
            VAPaymentFragmentDirections.actionPayoutFragmentToCheckOrderFragment().apply {
                noInvoice = dataVA.noInvoice
            }
        findNavController().navigate(toCheckPayment)
    }

    private fun setToolbar() {
        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.toolbar)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}