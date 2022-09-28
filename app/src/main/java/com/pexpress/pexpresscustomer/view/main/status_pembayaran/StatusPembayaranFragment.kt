package com.pexpress.pexpresscustomer.view.main.status_pembayaran

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.pexpress.pexpresscustomer.R
import com.pexpress.pexpresscustomer.databinding.FragmentStatusPembayaranBinding
import com.pexpress.pexpresscustomer.db.payments.CashPayment
import com.pexpress.pexpresscustomer.db.payments.EWalletPayment
import com.pexpress.pexpresscustomer.db.payments.VAPayment
import com.pexpress.pexpresscustomer.model.payment.ResultStatusPembayaran
import com.pexpress.pexpresscustomer.session.UserPreference
import com.pexpress.pexpresscustomer.utils.formatRupiah
import com.pexpress.pexpresscustomer.utils.showMessage
import com.pexpress.pexpresscustomer.view.main.status_pembayaran.adapter.StatusPembayaranAdapter
import com.pexpress.pexpresscustomer.view.main.status_pembayaran.viewmodel.StatusPembayaranViewModel
import www.sanju.motiontoast.MotionToast


class StatusPembayaranFragment : Fragment() {

    private var _binding: FragmentStatusPembayaranBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<StatusPembayaranViewModel>()
    private lateinit var statusPembayaranAdapter: StatusPembayaranAdapter

    companion object {
        private const val DEFAULT_TIMER_REALTIME = 10000L
    }

    private lateinit var mHandler: Handler
    private var idUser = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatusPembayaranBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private val mRunnable = object : Runnable {
        override fun run() {
            observeStatusPembayaran()
            mHandler.postDelayed(this, DEFAULT_TIMER_REALTIME)
        }
    }

    private fun setupView() {
        mHandler = Handler(Looper.getMainLooper())
        idUser = UserPreference(requireContext()).getUser().id ?: 0

        with(binding) {
            statusPembayaranAdapter = StatusPembayaranAdapter()
            with(rvStatusPembayaran) {
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
                addItemDecoration(
                    DividerItemDecoration(
                        requireContext(),
                        DividerItemDecoration.HORIZONTAL
                    )
                )
                adapter = statusPembayaranAdapter
            }

            statusPembayaranAdapter.setOnItemClickCallBack(object :
                StatusPembayaranAdapter.OnItemClickCallBack {
                override fun onItemClicked(resultStatusPembayaran: ResultStatusPembayaran) {
                    if (resultStatusPembayaran.status == "PENDING") {
                        moveToPembayaran(resultStatusPembayaran)
                    } else {
                        moveToDetailPembayaran(resultStatusPembayaran.nomorpemesanan.toString())
                    }
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        mHandler.post(mRunnable)
    }

    private fun observeStatusPembayaran() {
        viewModel.statusPembayaran(idUser.toString())
        with(binding) {
            viewModel.statusPembayaran.observe(viewLifecycleOwner) { response ->
                pbStatusPembayaran.visibility = View.GONE
                if (response != null) {
                    if (response.success!!) {
                        val result = response.data

                        if (result!!.isNotEmpty()) {
                            statusPembayaranAdapter.setDataStatusPembayaran(result as List<ResultStatusPembayaran>?)
                            isNotEmptyStatusOrder(true)
                        } else {
                            isNotEmptyStatusOrder(false)
                        }
                    } else {
                        isNotEmptyStatusOrder(false)
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
    }

    private fun moveToDetailPembayaran(noInvoice: String) {
        val toDetailPembayaran =
            StatusPembayaranFragmentDirections.actionNavigationStatusPembayaranToDetailStatusPembayaranFragment(
                noInvoice
            )
        findNavController().navigate(toDetailPembayaran)
    }

    private fun moveToPembayaran(resultStatusPembayaran: ResultStatusPembayaran) {
        lateinit var toPembayaran: Any
        when (resultStatusPembayaran.jenispembayaran) {
            "Pembayaran Tunai" -> {
                val dataCash = CashPayment(
                    resultStatusPembayaran.nomorpemesanan.toString(),
                    resultStatusPembayaran.nama.toString(),
                    formatRupiah(resultStatusPembayaran.tagihan?.toDouble() ?: 0.0)
                )
                toPembayaran =
                    StatusPembayaranFragmentDirections.actionNavigationStatusPembayaranToCashPaymentFragment(
                        dataCash
                    )
            }
            "Pembayaran Transfer" -> {
                val dataVA = VAPayment(
                    resultStatusPembayaran.nomorpemesanan.toString(),
                    resultStatusPembayaran.accountNumber.toString(),
                    resultStatusPembayaran.bank.toString(),
                    resultStatusPembayaran.nama.toString(),
                    formatRupiah(resultStatusPembayaran.tagihan?.toDouble() ?: 0.0),
                    resultStatusPembayaran.expired
                )
                toPembayaran =
                    StatusPembayaranFragmentDirections.actionNavigationStatusPembayaranToVaPaymentFragment()
                        .apply {
                            dataVa = dataVA
                        }
            }
            "Pembayaran Ewallet" -> {
                val dataEWallet = EWalletPayment(
                    resultStatusPembayaran.nomorpemesanan.toString(),
                    resultStatusPembayaran.bank.toString(),
                    resultStatusPembayaran.ewallet.toString(),
                    resultStatusPembayaran.nama.toString(),
                    resultStatusPembayaran.accountNumber.toString(),
                    formatRupiah(resultStatusPembayaran.tagihan?.toDouble() ?: 0.0),
                    resultStatusPembayaran.namafile.toString()
                )
                toPembayaran =
                    StatusPembayaranFragmentDirections.actionNavigationStatusPembayaranToEWalletPaymentFragment(
                        dataEWallet
                    )
            }
        }
        findNavController().navigate(toPembayaran as NavDirections)
    }

    private fun isNotEmptyStatusOrder(state: Boolean) {
        with(binding) {
            if (state) {
                rvStatusPembayaran.visibility = View.VISIBLE
                layoutEmptyStatusPembayaran.visibility = View.GONE
            } else {
                rvStatusPembayaran.visibility = View.GONE
                layoutEmptyStatusPembayaran.visibility = View.VISIBLE
            }
        }
    }

    override fun onPause() {
        super.onPause()
        mHandler.removeCallbacks(mRunnable)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}