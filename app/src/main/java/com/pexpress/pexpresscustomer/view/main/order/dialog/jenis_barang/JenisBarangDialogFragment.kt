package com.pexpress.pexpresscustomer.view.main.order.dialog.jenis_barang

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pexpress.pexpresscustomer.R
import com.pexpress.pexpresscustomer.databinding.FragmentJenisBarangDialogBinding
import com.pexpress.pexpresscustomer.model.order.ResultJenisBarang
import com.pexpress.pexpresscustomer.utils.UtilsCode.CONSTANT_TYPE_PACKAGE
import com.pexpress.pexpresscustomer.utils.UtilsCode.TYPE_PACKAGE_FIXRATE
import com.pexpress.pexpresscustomer.utils.showMessage
import com.pexpress.pexpresscustomer.view.main.order.dialog.jenis_barang.adapter.JenisBarangAdapter
import com.pexpress.pexpresscustomer.view.main.order.viewmodel.OrderPaketViewModel
import com.pexpress.pexpresscustomer.view.main.order.viewmodel.PFixRateViewModel
import com.pexpress.pexpresscustomer.view.main.order.viewmodel.PKilometerViewModel
import www.sanju.motiontoast.MotionToast

class JenisBarangDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentJenisBarangDialogBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<OrderPaketViewModel>()
    private val viewModelFixRate by activityViewModels<PFixRateViewModel>()
    private val viewModelKilometer by activityViewModels<PKilometerViewModel>()

    private lateinit var jenisBarangAdapter: JenisBarangAdapter

    private var typePackage = 0

    companion object {
        val TAG = JenisBarangDialogFragment::class.simpleName
    }

    fun newInstance(value: Int): JenisBarangDialogFragment {
        val args = Bundle()
        val f = JenisBarangDialogFragment()
        args.putInt(CONSTANT_TYPE_PACKAGE, value)
        f.arguments = args
        return f
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJenisBarangDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isLoading(true)
        prepareJenisBarang()

        if (arguments != null) {
            typePackage = arguments!!.getInt(CONSTANT_TYPE_PACKAGE, 0)
        }

        binding.btnClose.setOnClickListener {
            dialog!!.dismiss()
        }
    }

    private fun prepareJenisBarang() {
        with(binding) {
            viewModel.barang()
            jenisBarangAdapter = JenisBarangAdapter()

            with(rvItemsJenisBarang) {
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
                addItemDecoration(
                    DividerItemDecoration(
                        requireContext(),
                        DividerItemDecoration.HORIZONTAL
                    )
                )
                adapter = jenisBarangAdapter
            }

            viewModel.jenisBarang.observe(viewLifecycleOwner) { response ->
                if (response != null) {
                    if (response.success!!) {
                        isLoading(false, response.detail)
                        jenisBarangAdapter.setJenisBarang(response.detail)
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

            jenisBarangAdapter.setOnItemClickCallBack(object :
                JenisBarangAdapter.OnItemClickCallBack {
                override fun onItemClicked(result: ResultJenisBarang) {
                    if (typePackage == TYPE_PACKAGE_FIXRATE) {
                        viewModelFixRate.setFormJenisBarang(result)
                    } else {
                        viewModelKilometer.setFormJenisBarang(result)
                    }
                    dialog?.dismiss()
                }
            })
        }
    }

    private fun isLoading(state: Boolean, result: List<ResultJenisBarang>? = null) {
        with(binding) {
            if (state) {
                pbLoading.visibility = VISIBLE
                rvItemsJenisBarang.visibility = GONE
                placeholderEmptyItems.visibility = GONE
            } else {
                pbLoading.visibility = GONE
                if (result.isNullOrEmpty()) {
                    placeholderEmptyItems.visibility = VISIBLE
                } else {
                    rvItemsJenisBarang.visibility = VISIBLE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}