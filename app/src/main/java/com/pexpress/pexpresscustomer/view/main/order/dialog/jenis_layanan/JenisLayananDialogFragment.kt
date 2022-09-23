package com.pexpress.pexpresscustomer.view.main.order.dialog.jenis_layanan

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
import com.pexpress.pexpresscustomer.databinding.FragmentJenisLayananDialogBinding
import com.pexpress.pexpresscustomer.model.order.ResultJenisLayanan
import com.pexpress.pexpresscustomer.utils.UtilsCode.CONSTANT_TYPE_PACKAGE
import com.pexpress.pexpresscustomer.utils.UtilsCode.TARIF_TYPE_PACKAGE_FIXRATE
import com.pexpress.pexpresscustomer.utils.UtilsCode.TARIF_TYPE_PACKAGE_KILOMETER
import com.pexpress.pexpresscustomer.utils.UtilsCode.TYPE_PACKAGE_FIXRATE
import com.pexpress.pexpresscustomer.utils.UtilsCode.TYPE_PACKAGE_KILOMETER
import com.pexpress.pexpresscustomer.utils.showMessage
import com.pexpress.pexpresscustomer.view.main.ongkir.viewmodel.TarifFixRateViewModel
import com.pexpress.pexpresscustomer.view.main.ongkir.viewmodel.TarifKilometerViewModel
import com.pexpress.pexpresscustomer.view.main.order.dialog.jenis_layanan.adapter.JenisLayananAdapter
import com.pexpress.pexpresscustomer.view.main.order.viewmodel.OrderPaketViewModel
import com.pexpress.pexpresscustomer.view.main.order.viewmodel.PFixRateViewModel
import com.pexpress.pexpresscustomer.view.main.order.viewmodel.PKilometerViewModel
import www.sanju.motiontoast.MotionToast

class JenisLayananDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentJenisLayananDialogBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<OrderPaketViewModel>()
    private val viewModelFixRate by activityViewModels<PFixRateViewModel>()
    private val viewModelKilometer by activityViewModels<PKilometerViewModel>()
    private val viewModelTarifFixRate by activityViewModels<TarifFixRateViewModel>()
    private val viewModelTarifKilometer by activityViewModels<TarifKilometerViewModel>()

    private lateinit var jenisLayananAdapter: JenisLayananAdapter

    private var type = 0

    companion object {
        val TAG = JenisLayananDialogFragment::class.simpleName
    }

    fun newInstance(value: Int): JenisLayananDialogFragment {
        val args = Bundle()
        val f = JenisLayananDialogFragment()
        args.putInt(CONSTANT_TYPE_PACKAGE, value)
        f.arguments = args
        return f
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJenisLayananDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isLoading(true)
        prepareServiceType()

        if (arguments != null) {
            type = arguments!!.getInt(CONSTANT_TYPE_PACKAGE, 0)
        }

        binding.btnClose.setOnClickListener {
            dialog!!.dismiss()
        }
    }

    private fun prepareServiceType() {
        with(binding) {
            viewModel.layanan() // get data layanan to API
            jenisLayananAdapter = JenisLayananAdapter()

            with(rvServiceType) {
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
                addItemDecoration(
                    DividerItemDecoration(
                        requireContext(),
                        DividerItemDecoration.HORIZONTAL
                    )
                )
                adapter = jenisLayananAdapter

                viewModel.jenisLayanan.observe(viewLifecycleOwner) { response ->
                    if (response != null) {
                        if (response.success!!) {
                            jenisLayananAdapter.setJenisLayanan(response.detail)
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

                jenisLayananAdapter.setOnItemClickCallBack(object :
                    JenisLayananAdapter.OnItemClickCallBack {
                    override fun onItemClicked(result: ResultJenisLayanan) {
                        when (type) {
                            TYPE_PACKAGE_FIXRATE -> viewModelFixRate.setFormJenisLayanan(result)
                            TYPE_PACKAGE_KILOMETER -> viewModelKilometer.setFormJenisLayanan(result)
                            TARIF_TYPE_PACKAGE_FIXRATE -> viewModelTarifFixRate.setFormJenisLayanan(
                                result
                            )
                            TARIF_TYPE_PACKAGE_KILOMETER -> viewModelTarifKilometer.setFormJenisLayanan(
                                result
                            )
                        }
                        dialog?.dismiss()
                    }
                })
            }
        }
    }

    private fun isLoading(state: Boolean, result: List<ResultJenisLayanan>? = null) {
        with(binding) {
            if (state) {
                pbLoading.visibility = VISIBLE
                rvServiceType.visibility = GONE
                placeholderEmptyItems.visibility = GONE
            } else {
                pbLoading.visibility = GONE
                if (result.isNullOrEmpty()) {
                    placeholderEmptyItems.visibility = VISIBLE
                } else {
                    rvServiceType.visibility = VISIBLE
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}