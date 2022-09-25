package com.pexpress.pexpresscustomer.view.main.ongkir.tabview

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.material.textfield.TextInputLayout
import com.pexpress.pexpresscustomer.R
import com.pexpress.pexpresscustomer.databinding.FragmentTarifFixRateOngkirBinding
import com.pexpress.pexpresscustomer.utils.UtilsCode.FORM_ASAL
import com.pexpress.pexpresscustomer.utils.UtilsCode.FORM_TUJUAN
import com.pexpress.pexpresscustomer.utils.UtilsCode.TARIF_TYPE_PACKAGE_FIXRATE
import com.pexpress.pexpresscustomer.utils.UtilsCode.TYPE_PACKAGE_FIXRATE
import com.pexpress.pexpresscustomer.utils.showMessage
import com.pexpress.pexpresscustomer.view.main.ongkir.detail.DetailOngkirActivity
import com.pexpress.pexpresscustomer.view.main.ongkir.pick_location.PickPlaceLocationActivity
import com.pexpress.pexpresscustomer.view.main.ongkir.viewmodel.OngkirViewModel
import com.pexpress.pexpresscustomer.view.main.ongkir.viewmodel.TarifFixRateViewModel
import com.pexpress.pexpresscustomer.view.main.order.dialog.jenis_layanan.JenisLayananDialogFragment
import com.pexpress.pexpresscustomer.view.main.order.dialog.ukuran_barang.UkuranBarangDialogFragment
import www.sanju.motiontoast.MotionToast

class TarifFixRateOngkirFragment : Fragment() {

    private var _binding: FragmentTarifFixRateOngkirBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<OngkirViewModel>()
    private val viewModelTarifFixRate by activityViewModels<TarifFixRateViewModel>()

    private lateinit var modalJenisLayanan: JenisLayananDialogFragment
    private lateinit var modalUkuranBarang: UkuranBarangDialogFragment

    private var cabangAsal = ""
    private var cabangTujuan = ""
    private var gKecAsal = ""
    private var gKecTujuan = ""
    private var jenisPengiriman = ""
    private var jenisUkuran = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTarifFixRateOngkirBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()

        with(binding) {
            edtAsal.setOnClickListener {
                moveToPickPlaceLocation(FORM_ASAL)
            }

            edtTujuan.setOnClickListener {
                moveToPickPlaceLocation(FORM_TUJUAN)
            }

            edtJenisLayanan.setOnClickListener {
                modalJenisLayanan.newInstance(TARIF_TYPE_PACKAGE_FIXRATE)
                    .show(parentFragmentManager, JenisLayananDialogFragment.TAG)
            }

            edtUkuranBarang.setOnClickListener {
                modalUkuranBarang.newInstance(TARIF_TYPE_PACKAGE_FIXRATE)
                    .show(parentFragmentManager, UkuranBarangDialogFragment.TAG)
            }

            btnCekOngkir.setOnClickListener { checkOngkir() }
        }
    }

    private fun setupView() {
        modalJenisLayanan = JenisLayananDialogFragment()
        modalUkuranBarang = UkuranBarangDialogFragment()

        observeJenisLayanan()
        observeUkuranBarang()
    }

    private fun checkOngkir() {
        with(binding) {
            val asal = edtAsal.text.toString().trim()
            val tujuan = edtTujuan.text.toString().trim()
            val jenisLayanan = edtJenisLayanan.text.toString().trim()
            val ukuranBarang = edtUkuranBarang.text.toString().trim()

            when {
                asal.isEmpty() -> {
                    showMessageFieldRequired(tiAsal)
                    return@with
                }
                tujuan.isEmpty() -> {
                    showMessageFieldRequired(tiTujuan)
                    return@with
                }
                jenisLayanan.isEmpty() -> {
                    showMessageFieldRequired(tiJenisLayanan)
                    return@with
                }
                ukuranBarang.isEmpty() -> {
                    showMessageFieldRequired(tiUkuranBarang)
                    return@with
                }
                else -> {
                    val params = hashMapOf(
                        "jenispengiriman" to jenisPengiriman,
                        "jenisukuran" to jenisUkuran,
                        "cabangasal" to cabangAsal,
                        "cabangtujuan" to cabangTujuan,
                        "type" to "fix_rate"
                    )

                    observeCekOngkir(params)
                }
            }
        }
    }

    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val idForm = result.data?.getIntExtra(PickPlaceLocationActivity.EXTRA_FORM, 0)
                with(binding) {
                    when (idForm) {
                        FORM_ASAL -> {
                            cabangAsal =
                                result.data?.getStringExtra(PickPlaceLocationActivity.EXTRA_CABANG)
                                    .toString()
                            gKecAsal =
                                result.data?.getStringExtra(PickPlaceLocationActivity.EXTRA_GKEC)
                                    .toString()
                            edtAsal.setText(
                                result.data?.getStringExtra(PickPlaceLocationActivity.EXTRA_ALAMAT)
                                    .toString()
                            )
                        }
                        FORM_TUJUAN -> {
                            cabangTujuan =
                                result.data?.getStringExtra(PickPlaceLocationActivity.EXTRA_CABANG)
                                    .toString()
                            gKecTujuan =
                                result.data?.getStringExtra(PickPlaceLocationActivity.EXTRA_GKEC)
                                    .toString()
                            edtTujuan.setText(
                                result.data?.getStringExtra(PickPlaceLocationActivity.EXTRA_ALAMAT)
                                    .toString()
                            )
                        }
                    }
                }
            }
        }

    private fun observeCekOngkir(params: HashMap<String, String>) {
        viewModel.checkOngkirFixRate(params).observe(viewLifecycleOwner) { response ->
            if (response != null) {
                if (response.success!!) {
                    val intent = Intent(requireContext(), DetailOngkirActivity::class.java).apply {
                        putExtra(DetailOngkirActivity.EXTRA_TYPE_PACKAGE, TYPE_PACKAGE_FIXRATE)
                        putExtra(DetailOngkirActivity.EXTRA_DATA_ASAL, gKecAsal)
                        putExtra(DetailOngkirActivity.EXTRA_DATA_TUJUAN, gKecTujuan)
                        putExtra(DetailOngkirActivity.EXTRA_DATA, response.data?.get(0))
                    }
                    startActivity(intent)
                } else {
                    showMessage(
                        requireActivity(),
                        getString(R.string.failed_title),
                        response.message.toString(),
                        MotionToast.TOAST_ERROR
                    )
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

    private fun observeJenisLayanan() {
        viewModelTarifFixRate.formJenisLayanan.observe(viewLifecycleOwner) { value ->
            jenisPengiriman = value.idlayanan.toString()
            binding.edtJenisLayanan.setText(value.layanan)
        }
    }

    private fun observeUkuranBarang() {
        viewModelTarifFixRate.formUkuranBarang.observe(viewLifecycleOwner) { value ->
            jenisUkuran = value.idjenisukuran.toString()
            binding.edtUkuranBarang.setText(value.jenisukuran)
        }
    }

    private fun moveToPickPlaceLocation(idForm: Int) {
        val bundle = Bundle().apply {
            putInt(PickPlaceLocationActivity.EXTRA_FORM, idForm)
            putInt(PickPlaceLocationActivity.EXTRA_TYPE_PACKAGE, TYPE_PACKAGE_FIXRATE)
        }

        val intent = Intent(requireContext(), PickPlaceLocationActivity::class.java).apply {
            putExtra(PickPlaceLocationActivity.EXTRA_BUNDLE, bundle)
        }
        activityResultLauncher.launch(intent)
    }

    private fun showMessageFieldRequired(ti: TextInputLayout) {
        ti.error = "Field tidak boleh kosong!"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModelTarifFixRate.apply {
            removeFormJenisLayanan()
            removeFormUkuranBarang()
        }
    }
}