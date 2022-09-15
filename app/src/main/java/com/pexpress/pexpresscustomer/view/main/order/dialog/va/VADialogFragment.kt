package com.pexpress.pexpresscustomer.view.main.order.dialog.va

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pexpress.pexpresscustomer.R
import com.pexpress.pexpresscustomer.databinding.FragmentVirtualAccountDialogBinding
import com.pexpress.pexpresscustomer.model.type_payments.ResultVA
import com.pexpress.pexpresscustomer.utils.showMessage
import com.pexpress.pexpresscustomer.view.main.order.dialog.va.adapter.ItemsBankAdapter
import com.pexpress.pexpresscustomer.view.main.order.viewmodel.CheckoutViewModel
import www.sanju.motiontoast.MotionToast

class VADialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentVirtualAccountDialogBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<CheckoutViewModel>()
    private lateinit var adapterItemsBank: ItemsBankAdapter

    companion object {
        val TAG = VADialogFragment::class.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVirtualAccountDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isLoading(true)
        prepareItemSize()

        dialog?.also { binding.btnClose.setOnClickListener { dialog!!.dismiss() } }
    }

    private fun prepareItemSize() {
        with(binding) {
            viewModel.listVA()
            adapterItemsBank = ItemsBankAdapter()

            with(rvItemsBank) {
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
                addItemDecoration(
                    DividerItemDecoration(
                        requireContext(),
                        DividerItemDecoration.HORIZONTAL
                    )
                )
                adapter = adapterItemsBank
            }

            viewModel.listVA.observe(viewLifecycleOwner) { response ->
                if (response != null) {
                    if (response.success!!) {
                        rvItemsBank.visibility = VISIBLE
                        adapterItemsBank.setListBank(response.data)
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
                }
            }

            adapterItemsBank.setOnItemClickCallBack(object : ItemsBankAdapter.OnItemClickCallBack {
                override fun onItemClicked(itemBank: ResultVA) {
                    viewModel.setVAPayment(itemBank)
                    dialog?.dismiss()
                }
            })
        }
    }

    private fun isLoading(state: Boolean, result: List<ResultVA>? = null) {
        with(binding) {
            if (state) {
                pbLoading.visibility = VISIBLE
                rvItemsBank.visibility = GONE
                placeholderEmptyItems.visibility = GONE
            } else {
                pbLoading.visibility = GONE
                if (result.isNullOrEmpty()) {
                    placeholderEmptyItems.visibility = VISIBLE
                } else {
                    rvItemsBank.visibility = VISIBLE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}