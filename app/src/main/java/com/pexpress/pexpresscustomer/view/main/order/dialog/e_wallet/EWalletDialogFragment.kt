package com.pexpress.pexpresscustomer.view.main.order.dialog.e_wallet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pexpress.pexpresscustomer.R
import com.pexpress.pexpresscustomer.databinding.FragmentEWalletDialogBinding
import com.pexpress.pexpresscustomer.db.payments.EWalletPayment
import com.pexpress.pexpresscustomer.model.type_payments.ResultEWallet
import com.pexpress.pexpresscustomer.utils.showMessage
import com.pexpress.pexpresscustomer.view.main.order.dialog.e_wallet.adapter.ItemsEWalletAdapter
import com.pexpress.pexpresscustomer.view.main.order.viewmodel.CheckoutViewModel
import www.sanju.motiontoast.MotionToast

class EWalletDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentEWalletDialogBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<CheckoutViewModel>()
    private lateinit var adapterItemEWallet: ItemsEWalletAdapter

    companion object {
        val TAG = EWalletPayment::class.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEWalletDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isLoading(true)
        prepareItemEWallet()
        dialog?.also { binding.btnClose.setOnClickListener { dialog!!.dismiss() } }
    }

    private fun prepareItemEWallet() {
        with(binding) {
            viewModel.listEWallet()
            adapterItemEWallet = ItemsEWalletAdapter()

            with(rvItemsEwallet) {
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
                addItemDecoration(
                    DividerItemDecoration(
                        requireContext(),
                        DividerItemDecoration.HORIZONTAL
                    )
                )
                adapter = adapterItemEWallet
            }

            viewModel.listEWallet.observe(viewLifecycleOwner) { response ->
                if (response != null) {
                    if (response.success!!) {
                        adapterItemEWallet.setListEWallet(response.data)
                        isLoading(false, response.data)
                    } else {
                        isLoading(false)
                    }
                } else {
                    showMessage(
                        requireActivity(),
                        getString(R.string.failed_title),
                        getString(R.string.failed_description),
                        MotionToast.TOAST_ERROR
                    )
                    isLoading(false)
                }
            }

            adapterItemEWallet.setOnItemClickCallBack(object :
                ItemsEWalletAdapter.OnItemClickCallBack {
                override fun onItemClicked(itemEWallet: ResultEWallet) {
                    viewModel.setEWalletPayment(itemEWallet)
                    dialog?.dismiss()
                }
            })
        }
    }

    private fun isLoading(state: Boolean, result: List<ResultEWallet>? = null) {
        with(binding) {
            if (state) {
                pbLoading.visibility = View.VISIBLE
                rvItemsEwallet.visibility = View.GONE
                placeholderEmptyItems.visibility = View.GONE
            } else {
                pbLoading.visibility = View.GONE
                if (result.isNullOrEmpty()) {
                    placeholderEmptyItems.visibility = View.VISIBLE
                } else {
                    rvItemsEwallet.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}