package com.pexpress.pexpresscustomer.view.main.tracking_order

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.pexpress.pexpresscustomer.R
import com.pexpress.pexpresscustomer.databinding.FragmentTrackingOrderBinding
import com.pexpress.pexpresscustomer.model.tracking.ResultTracking
import com.pexpress.pexpresscustomer.utils.UtilsCode.MILESTONE_DELIVERY
import com.pexpress.pexpresscustomer.utils.capitalizeEachWords
import com.pexpress.pexpresscustomer.utils.closeKeyboard
import com.pexpress.pexpresscustomer.utils.formatRupiah
import com.pexpress.pexpresscustomer.utils.showMessage
import com.pexpress.pexpresscustomer.view.main.status_order.adapter.MilestoneAdapter
import com.pexpress.pexpresscustomer.view.main.tracking_order.scanner_qr_code.ScannerQRFragment
import com.pexpress.pexpresscustomer.view.main.tracking_order.viewmodel.TrackingOrderViewModel
import www.sanju.motiontoast.MotionToast

class TrackingOrderFragment : Fragment() {

    private var _binding: FragmentTrackingOrderBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<TrackingOrderViewModel>()
    private lateinit var milestoneAdapter: MilestoneAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrackingOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar()
        setupSearch()
    }

    private fun setupSearch() {
        with(binding) {
            setFragmentResultListener(ScannerQRFragment.EXTRA_DATA) { key, bundle ->
                if (key == ScannerQRFragment.EXTRA_DATA) {
                    val result = bundle.getString(ScannerQRFragment.EXTRA_ID_TRACKING)
                    result?.also {
                        edtSearch.setText(it)
                        observeTracking(it)
                    }
                }
            }

            btnCheckResi.setOnClickListener {
                val query = edtSearch.text.toString().trim()
                if (query.isNotEmpty()) {
                    isLoading(true)
                    observeTracking(query)
                }
            }
        }
    }

    private fun setupView(resultTracking: ResultTracking) {
        with(binding) {
            milestoneAdapter = MilestoneAdapter()
            with(rvMilestone) {
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
                addItemDecoration(
                    DividerItemDecoration(requireContext(), DividerItemDecoration.HORIZONTAL)
                )
                adapter = milestoneAdapter
            }

            milestoneAdapter.setOnItemClickCallBack(object : MilestoneAdapter.OnItemClickCallBack {
                override fun onItemClicked(fotoPenerima: String) {
                    moveToBuktiPengiriman(fotoPenerima)
                }
            })

            tvTanggalOrder.text = resultTracking.tanggalpickup
            tvNoResi.text = resultTracking.nomortracking
            tvStatus.text =
                if (resultTracking.statuspengiriman == MILESTONE_DELIVERY.toInt()) {
                    "${resultTracking.namastatuspengiriman} ${resultTracking.millestone?.get(0)?.diserahkanoleh}"
                } else {
                    resultTracking.namastatuspengiriman
                }

            tvNamaPengirim.text = resultTracking.namapengirim

            val kota = resultTracking.district?.lowercase()?.capitalizeEachWords
            val kabupaten = resultTracking.regencies?.lowercase()?.capitalizeEachWords
            tvAsalPengirim.text = getString(R.string.tracking_asal_pengirim, kota, kabupaten)

            tvNamaPenerima.text = resultTracking.namapenerima
            tvAsalPenerima.text = resultTracking.gkecamatanpenerima
            tvJenisLayanan.text = resultTracking.layanan
            tvJenisBerat.text = getString(R.string.dimension_weight_size_2, resultTracking.berat)
            tvTarif.text = formatRupiah(resultTracking.biaya?.toDouble() ?: 0.0)
        }
    }

    private fun launchScanQr() {
        if (checkPermissionCamera()) {
            moveToScanQr()
        } else {
            permissionCamera.launch(Manifest.permission.CAMERA)
        }
    }

    private fun moveToScanQr() {
        findNavController().navigate(R.id.action_navigation_tracking_to_scannerQRFragment)
    }

    private fun checkPermissionCamera(): Boolean {
        return requireContext().checkCallingOrSelfPermission(Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED
    }

    private val permissionCamera =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                moveToScanQr()
            } else {
                showRationalePermission()
            }
        }

    private fun observeTracking(noTracking: String) {
        viewModel.trackingSearch(noTracking).observe(viewLifecycleOwner) { response ->
            if (response != null) {
                if (response.success!!) {
                    response.data?.also {
                        setupView(it)
                        milestoneAdapter.setDataMilestone(it.millestone)
                        closeKeyboard(requireActivity())
                    }
                    isLoading(state = false, isData = true)
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
    }

    private fun moveToBuktiPengiriman(fotoPenerima: String) {
        val toProofDelivery =
            TrackingOrderFragmentDirections.actionNavigationTrackingToProofDeliveryFragment(
                fotoPenerima
            )
        findNavController().navigate(toProofDelivery)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.tracking_menu, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> findNavController().navigateUp()
            R.id.tracking_scan_qr -> launchScanQr()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun isLoading(state: Boolean, isData: Boolean = false) {
        with(binding) {
            if (state) {
                pbLoading.visibility = View.VISIBLE
                layoutSearch.visibility = View.GONE
            } else {
                pbLoading.visibility = View.GONE
                if (isData) {
                    layoutSearch.visibility = View.VISIBLE
                } else {
                    layoutEmptyTrackingOrder.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun showRationalePermission() {
        AlertDialog.Builder(requireContext())
            .setTitle("Perizinan Diperlukan!")
            .setMessage("Perizinan diperlukan agar fitur dapat berjalan dengan semestinya")
            .setPositiveButton("Oke") { dialogInterface, _ ->
                permissionCamera.launch(Manifest.permission.CAMERA)
                dialogInterface.dismiss()
            }
            .show()
    }

    private fun setToolbar() {
        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.toolbar)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}