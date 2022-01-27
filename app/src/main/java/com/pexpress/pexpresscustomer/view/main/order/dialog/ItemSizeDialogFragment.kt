package com.pexpress.pexpresscustomer.view.main.order.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pexpress.pexpresscustomer.databinding.FragmentItemsSizeDialogBinding
import com.pexpress.pexpresscustomer.db.ItemSize
import com.pexpress.pexpresscustomer.utils.ItemsDummy
import com.pexpress.pexpresscustomer.view.main.order.dialog.adapter.ItemsSizeAdapter
import com.pexpress.pexpresscustomer.view.main.order.viewmodel.OrderPaketViewModel

class ItemSizeDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentItemsSizeDialogBinding? = null
    private val binding get() = _binding!!
    private val vieModel by activityViewModels<OrderPaketViewModel>()
    private lateinit var adapterItemsSize: ItemsSizeAdapter

    companion object {
        val TAG = ItemSizeDialogFragment::class.simpleName
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentItemsSizeDialogBinding.inflate(inflater, container, false)
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
            adapterItemsSize = ItemsSizeAdapter(ItemsDummy.listItemSize)

            with(rvItemsSize) {
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
                addItemDecoration(
                    DividerItemDecoration(
                        requireContext(),
                        DividerItemDecoration.HORIZONTAL
                    )
                )
                adapter = adapterItemsSize
            }

            adapterItemsSize.setOnItemClickCallBack(object : ItemsSizeAdapter.OnItemClickCallBack {
                override fun onItemClicked(itemSize: ItemSize) {
                    vieModel.setFormUkuranBarang(itemSize.nameItemSize)
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