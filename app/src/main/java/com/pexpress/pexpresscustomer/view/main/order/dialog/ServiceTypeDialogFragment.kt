package com.pexpress.pexpresscustomer.view.main.order.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pexpress.pexpresscustomer.R
import com.pexpress.pexpresscustomer.databinding.FragmentServiceTypeBinding
import com.pexpress.pexpresscustomer.db.ServiceType
import com.pexpress.pexpresscustomer.utils.ItemsDummy
import com.pexpress.pexpresscustomer.view.main.order.dialog.adapter.ServiceTypeAdapter
import com.pexpress.pexpresscustomer.view.main.order.viewmodel.OrderPaketViewModel

class ServiceTypeDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentServiceTypeBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<OrderPaketViewModel>()
    private lateinit var serviceAdapter: ServiceTypeAdapter

    companion object {
        val TAG = ServiceTypeDialogFragment::class.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentServiceTypeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog!!.setOnShowListener {
            val dialog = it as BottomSheetDialog
            val bottomSheet = dialog.findViewById<View>(R.id.design_bottom_sheet)
            bottomSheet?.let { sheet ->
                dialog.behavior.apply {
                    isDraggable = false
                }
                sheet.parent.parent.requestLayout()
            }
        }

        prepareServiceType()

        binding.btnClose.setOnClickListener {
            dialog!!.dismiss()
        }
    }

    private fun prepareServiceType() {
        with(binding) {
            serviceAdapter = ServiceTypeAdapter(ItemsDummy.listServiceType)

            with(rvServiceType) {
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
                addItemDecoration(
                    DividerItemDecoration(
                        requireContext(),
                        DividerItemDecoration.HORIZONTAL
                    )
                )
                adapter = serviceAdapter

                serviceAdapter.setOnItemClickCallBack(object :
                    ServiceTypeAdapter.OnItemClickCallBack {
                    override fun onItemClicked(service: ServiceType) {
                        viewModel.setFormJenisLayanan(service.serviceType)
                        dialog?.dismiss()
                    }
                })
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}