package com.pexpress.pexpresscustomer.view.main.order.dialog.ukuran_barang

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
import com.pexpress.pexpresscustomer.databinding.FragmentJenisUkuranDialogBinding
import com.pexpress.pexpresscustomer.model.order.ResultJenisUkuran
import com.pexpress.pexpresscustomer.utils.UtilsCode.CONSTANT_TYPE_PACKAGE
import com.pexpress.pexpresscustomer.utils.UtilsCode.TARIF_TYPE_PACKAGE_FIXRATE
import com.pexpress.pexpresscustomer.utils.UtilsCode.TARIF_TYPE_PACKAGE_KILOMETER
import com.pexpress.pexpresscustomer.utils.UtilsCode.TYPE_PACKAGE_FIXRATE
import com.pexpress.pexpresscustomer.utils.UtilsCode.TYPE_PACKAGE_KILOMETER
import com.pexpress.pexpresscustomer.utils.showMessage
import com.pexpress.pexpresscustomer.view.main.ongkir.viewmodel.TarifFixRateViewModel
import com.pexpress.pexpresscustomer.view.main.ongkir.viewmodel.TarifKilometerViewModel
import com.pexpress.pexpresscustomer.view.main.order.dialog.ukuran_barang.adapter.JenisUkuranAdapter
import com.pexpress.pexpresscustomer.view.main.order.viewmodel.OrderPaketViewModel
import com.pexpress.pexpresscustomer.view.main.order.viewmodel.PFixRateViewModel
import com.pexpress.pexpresscustomer.view.main.order.viewmodel.PKilometerViewModel
import www.sanju.motiontoast.MotionToast

class UkuranBarangDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentJenisUkuranDialogBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<OrderPaketViewModel>()
    private val viewModelFixRate by activityViewModels<PFixRateViewModel>()
    private val viewModelKilometer by activityViewModels<PKilometerViewModel>()
    private val viewModelTarifFixRate by activityViewModels<TarifFixRateViewModel>()
    private val viewModelTarifKilometer by activityViewModels<TarifKilometerViewModel>()

    private lateinit var adapterJenisUkuran: JenisUkuranAdapter

    private var type = 0

    companion object {
        val TAG = UkuranBarangDialogFragment::class.simpleName
    }

    fun newInstance(value: Int): UkuranBarangDialogFragment {
        val args = Bundle()
        val f = UkuranBarangDialogFragment()
        args.putInt(CONSTANT_TYPE_PACKAGE, value)
        f.arguments = args
        return f
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJenisUkuranDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isLoading(true)
        prepareItemSize()

        if (arguments != null) {
            type = arguments!!.getInt(CONSTANT_TYPE_PACKAGE, 0)
        }

        binding.btnClose.setOnClickListener {
            dialog!!.dismiss()
        }
    }

    private fun prepareItemSize() {
        with(binding) {
            viewModel.ukuran()
            adapterJenisUkuran = JenisUkuranAdapter()

            with(rvItemsSize) {
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
                addItemDecoration(
                    DividerItemDecoration(
                        requireContext(),
                        DividerItemDecoration.HORIZONTAL
                    )
                )
                adapter = adapterJenisUkuran
            }

            viewModel.jenisUkuran.observe(viewLifecycleOwner) { response ->
                if (response != null) {
                    if (response.success!!) {
                        adapterJenisUkuran.setJenisUkuran(response.detail)
                        isLoading(false, response.detail)
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

            adapterJenisUkuran.setOnItemClickCallBack(object :
                JenisUkuranAdapter.OnItemClickCallBack {
                override fun onItemClicked(result: ResultJenisUkuran) {
                    when (type) {
                        TYPE_PACKAGE_FIXRATE -> viewModelFixRate.setFormUkuranBarang(result)
                        TYPE_PACKAGE_KILOMETER -> viewModelKilometer.setFormUkuranBarang(
                            result
                        )
                        TARIF_TYPE_PACKAGE_FIXRATE -> viewModelTarifFixRate.setFormUkuranBarang(
                            result
                        )
                        TARIF_TYPE_PACKAGE_KILOMETER -> viewModelTarifKilometer.setFormUkuranBarang(
                            result
                        )
                    }
                    dialog?.dismiss()
                }
            })
        }
    }

    private fun isLoading(state: Boolean, result: List<ResultJenisUkuran>? = null) {
        with(binding) {
            if (state) {
                pbLoading.visibility = VISIBLE
                rvItemsSize.visibility = GONE
                placeholderEmptyItems.visibility = GONE
            } else {
                pbLoading.visibility = GONE
                if (result.isNullOrEmpty()) {
                    placeholderEmptyItems.visibility = VISIBLE
                } else {
                    rvItemsSize.visibility = VISIBLE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}