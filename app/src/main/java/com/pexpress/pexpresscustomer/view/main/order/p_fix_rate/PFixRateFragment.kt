package com.pexpress.pexpresscustomer.view.main.order.p_fix_rate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.google.android.material.card.MaterialCardView
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.pexpress.pexpresscustomer.R
import com.pexpress.pexpresscustomer.databinding.FragmentPFixRateBinding
import com.pexpress.pexpresscustomer.utils.hideViewBottomNav
import com.pexpress.pexpresscustomer.view.main.order.dialog.ItemSizeDialogFragment
import com.pexpress.pexpresscustomer.view.main.order.dialog.PickLocationDialogFragment
import com.pexpress.pexpresscustomer.view.main.order.dialog.ServiceTypeDialogFragment
import com.pexpress.pexpresscustomer.view.main.order.viewmodel.OrderPaketViewModel

class PFixRateFragment : Fragment() {

    private var _binding: FragmentPFixRateBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<OrderPaketViewModel>()

    private lateinit var modalPickLocation: PickLocationDialogFragment
    private lateinit var modalServiceType: ServiceTypeDialogFragment
    private lateinit var modalItemsSize: ItemSizeDialogFragment

    private val TAG = PFixRateFragment::class.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        orderPaketViewModel =
//            ViewModelProvider(this).get(OrderPaketViewModel::class.java)
        _binding = FragmentPFixRateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar()
        hideViewBottomNav(requireActivity())

        with(binding) {
            cdInfoPengirim.setOnClickListener {
                with(viewModel) {
                    // cek visibilitas
                    isExpandInfo(
                        !stateInfoPengirim.value!!,
                        cdInfoPengirim,
                        expandInfoPengirim,
                        icDropdownPengirim
                    )

                    // ubah set visibilitas
                    setStateInfoPengirim(!stateInfoPengirim.value!!)
                }
            }

            cdInfoPenerima.setOnClickListener {
                with(viewModel) {
                    isExpandInfo(
                        !stateInfoPenerima.value!!,
                        cdInfoPenerima,
                        expandInfoPenerima,
                        icDropdownPenerima
                    )
                    setStateInfoPenerima(!stateInfoPenerima.value!!)
                }
            }

            cdInfoPickup.setOnClickListener {
                with(viewModel) {
                    isExpandInfo(
                        !stateInfoPickup.value!!,
                        cdInfoPickup,
                        expandInfoPickup,
                        icDropdownPickup
                    )
                    setStateInfoPickup(!stateInfoPickup.value!!)
                }
            }

            btnBookNow.setOnClickListener {
                findNavController().navigate(R.id.action_PFixRateFragment_to_checkoutFragment)
            }
        }

        prepareDataFixRate()
    }

    private fun prepareDataFixRate() {
        modalPickLocation = PickLocationDialogFragment()
        modalServiceType = ServiceTypeDialogFragment()
        modalItemsSize = ItemSizeDialogFragment()

        with(binding) {
            // data info pengirim
            edtAsalPengirim.setOnClickListener {
                modalPickLocation.setCodeForm(PickLocationDialogFragment.FORM_PENGIRIM)
                modalPickLocation.show(parentFragmentManager, PickLocationDialogFragment.TAG)
            }

            // cek form asal pengirim
            var asalPengirim = ""

            viewModel.formAsalPengirim.observe(viewLifecycleOwner, { value ->
                asalPengirim = value
                edtAsalPengirim.setText(asalPengirim)
            })

            val namaPengirim = edtAsalPengirim.text.toString().trim()
            val noHandphonePengirim = edtNumberPhonePengirim.text.toString().trim()
            val catatanLokasiPengirim = edtCatatanLokasiPengirim.text.toString().trim()

            // data info penerima
            edtAsalPenerima.setOnClickListener {
                modalPickLocation.setCodeForm(PickLocationDialogFragment.FORM_PENERIMA)
                modalPickLocation.show(parentFragmentManager, PickLocationDialogFragment.TAG)
            }

            var asalPenerima = ""

            viewModel.formAsalPenerima.observe(viewLifecycleOwner, { value ->
                asalPenerima = value
                edtAsalPenerima.setText(asalPenerima)
            })

            val namaPenerima = edtNamaPenerima.text.toString().trim()
            val noHandphonePenerima = edtNumberPhonePenerima.text.toString().trim()
            val catatanLokasiPenerima = edtCatatanLokasiPenerima.text.toString().trim()

            // data info pickup
            edtJenisLayananPickup.apply {
                setOnClickListener {
                    modalServiceType.show(parentFragmentManager, ServiceTypeDialogFragment.TAG)
                }
            }

            edtUkuranBarangPickup.setOnClickListener {
                modalItemsSize.show(parentFragmentManager, ItemSizeDialogFragment.TAG)
            }

            edtTanggalPickup.setOnClickListener {
                // Makes only dates from today forward selectable.
                val constraintsBuilder =
                    CalendarConstraints.Builder()
                        .setValidator(DateValidatorPointForward.now())

                val datePicker =
                    MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select date")
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .setCalendarConstraints(constraintsBuilder.build())
                        .build()

                datePicker.show(parentFragmentManager, "TAG")
            }

            val catatanPickup = edtCatatanPickup.text.toString().trim()
        }
    }

    private fun observeServiceType() {
        viewModel.formJenisLayanan.observe(viewLifecycleOwner, { value ->
            binding.edtJenisLayananPickup.setText(value)
        })
    }

    private fun observeItemSize() {
        viewModel.formUkuranBarang.observe(viewLifecycleOwner, { value ->
            binding.edtUkuranBarangPickup.setText(value)
        })
    }

    private fun isExpandInfo(
        state: Boolean,
        cdInfo: MaterialCardView,
        expandInfo: ConstraintLayout,
        icDropDown: ImageView
    ) {
        if (state) {
            TransitionManager.beginDelayedTransition(cdInfo, AutoTransition())
            expandInfo.visibility = View.VISIBLE
            icDropDown.setImageResource(R.drawable.ic_drop_down)
        } else {
            expandInfo.visibility = View.GONE
            icDropDown.setImageResource(R.drawable.ic_drop_right)
        }
    }

    override fun onResume() {
        super.onResume()
        // cek visibilitas info pengirim
        viewModel.stateInfoPengirim.observe(viewLifecycleOwner, { state ->
            with(binding) {
                isExpandInfo(state, cdInfoPengirim, expandInfoPengirim, icDropdownPengirim)
            }
        })

        // cek visibilitas info penerima
        viewModel.stateInfoPenerima.observe(viewLifecycleOwner, { state ->
            with(binding) {
                isExpandInfo(state, cdInfoPenerima, expandInfoPenerima, icDropdownPenerima)
            }
        })

        // cek visibilitas info pengirim
        viewModel.stateInfoPickup.observe(viewLifecycleOwner, { state ->
            with(binding) {
                isExpandInfo(state, cdInfoPickup, expandInfoPickup, icDropdownPickup)
            }
        })

        observeServiceType()
        observeItemSize()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) findNavController().navigateUp()
        return super.onOptionsItemSelected(item)
    }

    private fun setToolbar() {
        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.toolbar)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        activity?.viewModelStore?.clear()
    }
}