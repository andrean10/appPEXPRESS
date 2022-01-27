package com.pexpress.pexpresscustomer.view.main.order.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pexpress.pexpresscustomer.R
import com.pexpress.pexpresscustomer.databinding.FragmentItemsBankDialogBinding
import com.pexpress.pexpresscustomer.db.ItemBank
import com.pexpress.pexpresscustomer.utils.ItemsDummy
import com.pexpress.pexpresscustomer.view.main.order.dialog.adapter.ItemsBankAdapter

class ItemsBankDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentItemsBankDialogBinding? = null
    private val binding get() = _binding!!

    //    private val vieModel by activityViewModels<OrderPaketViewModel>()
    private lateinit var adapterItemsBank: ItemsBankAdapter

    companion object {
        val TAG = ItemsBankDialogFragment::class.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentItemsBankDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareItemSize()

        binding.btnClose.setOnClickListener {
            dialog!!.dismiss()
        }
    }

    private fun prepareItemSize() {
        with(binding) {
            adapterItemsBank = ItemsBankAdapter(ItemsDummy.listItemBank)

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

            adapterItemsBank.setOnItemClickCallBack(object : ItemsBankAdapter.OnItemClickCallBack {
                override fun onItemClicked(itemBank: ItemBank) {
                    findNavController().navigate(R.id.action_checkoutFragment_to_payoutFragment)
                    dialog?.dismiss()
                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}