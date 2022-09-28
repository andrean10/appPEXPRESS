package com.pexpress.pexpresscustomer.view.main.order.p_fix_rate

import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
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
import com.pexpress.pexpresscustomer.model.checkout.fix_rate.ResultCheckout
import com.pexpress.pexpresscustomer.session.UserPreference
import com.pexpress.pexpresscustomer.utils.*
import com.pexpress.pexpresscustomer.utils.UtilsCode.FORM_PENERIMA
import com.pexpress.pexpresscustomer.utils.UtilsCode.FORM_PENGIRIM
import com.pexpress.pexpresscustomer.utils.UtilsCode.PATTERN_DATE_POST
import com.pexpress.pexpresscustomer.utils.UtilsCode.PATTERN_DATE_VIEW
import com.pexpress.pexpresscustomer.utils.UtilsCode.TYPE_PACKAGE_FIXRATE
import com.pexpress.pexpresscustomer.utils.UtilsCode.TYPE_PACKAGE_FIXRATE_STRING
import com.pexpress.pexpresscustomer.view.main.order.dialog.jenis_barang.JenisBarangDialogFragment
import com.pexpress.pexpresscustomer.view.main.order.dialog.jenis_layanan.JenisLayananDialogFragment
import com.pexpress.pexpresscustomer.view.main.order.dialog.ukuran_barang.UkuranBarangDialogFragment
import com.pexpress.pexpresscustomer.view.main.order.viewmodel.PFixRateViewModel
import www.sanju.motiontoast.MotionToast

class PFixRateFragment : Fragment() {

    private var _binding: FragmentPFixRateBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<PFixRateViewModel>()

    private lateinit var userPreference: UserPreference

    private lateinit var modalJenisLayanan: JenisLayananDialogFragment
    private lateinit var modalUkuranBarang: UkuranBarangDialogFragment
    private lateinit var modalJenisBarang: JenisBarangDialogFragment

    private var cabangAsal = ""
    private var cabangTujuan = ""
    private var gKecPengirim = ""
    private var gKecPenerima = ""
    private var latPengirim = ""
    private var longPengirim = ""
    private var latPenerima = ""
    private var longPenerima = ""
    private var kecamatanPengirim = ""
    private var kecamatanPenerima = ""
    private var jenisLayanan = ""
    private var jenisUkuran = ""
    private var jenisBarang = ""

    private var isPengirim = false
    private var isClickLainnya = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPFixRateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbar()
        userPreference = UserPreference(requireContext())

        prepareViewFixRate()
        prepareDataFixRate()
    }

    private fun prepareViewFixRate() {
        with(binding) {
            cdInfoPengirim.setOnClickListener {
                with(viewModel) {
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

            btnCheckout.setOnClickListener { checkout() }
        }
    }

    private fun prepareDataFixRate() {
        modalJenisLayanan = JenisLayananDialogFragment()
        modalUkuranBarang = UkuranBarangDialogFragment()
        modalJenisBarang = JenisBarangDialogFragment()

        with(binding) {
            /***---------------- DATA PENGIRIM -------------------****/
            edtAsalPengirim.setOnClickListener {
                moveToPickLocation(FORM_PENGIRIM)
            }

            observeFormAsalPengirim() // cek form asal pengirim didapatkan dari result maps
            observeLocationPengirim() // get latlong pengirim

            // akses kontak pengirim
            tiNumberPhonePengirim.setEndIconOnClickListener {
                isPengirim = true
                pickContact.launch(0)
            }

            /***---------------- DATA PENERIMA -------------------****/

            // data info penerima
            edtAsalPenerima.setOnClickListener {
                moveToPickLocation(FORM_PENERIMA)
            }

            observeFormAsalPenerima()
            observeLocationPenerima() // get lat long penerima

            // akses kontak penerima
            tiNumberPhonePenerima.setEndIconOnClickListener {
                isPengirim = false
                pickContact.launch(0)
            }

            /***---------------- DATA PICKUP -------------------****/
            // data info pickup
            edtJenisLayananPickup.setOnClickListener {
                modalJenisLayanan.newInstance(TYPE_PACKAGE_FIXRATE)
                    .show(parentFragmentManager, JenisLayananDialogFragment.TAG)
            }

            edtUkuranBarangPickup.setOnClickListener {
                modalUkuranBarang.newInstance(TYPE_PACKAGE_FIXRATE)
                    .show(parentFragmentManager, UkuranBarangDialogFragment.TAG)
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

                datePicker.addOnPositiveButtonClickListener { selection ->
                    edtTanggalPickup.setText(
                        FormatDate().outputDateFormat(PATTERN_DATE_VIEW).format(selection)
                    )
                }
            }

            edtJenisBarangPickup.setOnClickListener {
                modalJenisBarang.newInstance(TYPE_PACKAGE_FIXRATE)
                    .show(parentFragmentManager, JenisBarangDialogFragment.TAG)
            }

            boxLainnya.setOnCheckedChangeListener { _, state ->
                isClickLainnya = state
                checkClickLainnya(state)
            }
        }
    }

    private fun checkout() {
        with(binding) {
            val namaPengirim = edtNamaPengirim.text.toString().trim()
            val namaPenerima = edtNamaPenerima.text.toString().trim()
            val noHandphonePengirim =
                getString(R.string.format_number_id, edtNumberPhonePengirim.text.toString().trim())
            val noHandphonePenerima =
                getString(R.string.format_number_id, edtNumberPhonePenerima.text.toString().trim())
            val patokanAlamatPengirim = edtPatokanAlamatPengirim.text.toString().trim()
            val patokanAlamatPenerima = edtPatokanAlamatPenerima.text.toString().trim()
            val catatanLokasiPengirim = edtCatatanLokasiPengirim.text.toString().trim()
            val catatanLokasiPenerima = edtCatatanLokasiPenerima.text.toString().trim()
            val tanggalPickup = FormatDate().formatedDate(
                edtTanggalPickup.text.toString().trim(),
                PATTERN_DATE_VIEW,
                PATTERN_DATE_POST
            )
            val jenisBarangLainnya = edtJenisBarangLainnya.text.toString().trim()

            val user = userPreference.getUser()

            val params = hashMapOf(
                "idcustomer" to user.id.toString(),
                "namapengirim" to namaPengirim,
                "telppengirim" to noHandphonePengirim,
                "emailpengirim" to user.email.toString(),
                "kecamatanpengirim" to kecamatanPengirim,
                "alamatpengirim" to patokanAlamatPengirim,
                "catatanpengirim" to catatanLokasiPengirim,
                "namapenerima" to namaPenerima,
                "telppenerima" to noHandphonePenerima,
                "kecamatanpenerima" to kecamatanPenerima,
                "alamatpenerima" to patokanAlamatPenerima,
                "catatanpenerima" to catatanLokasiPenerima,
                "jenispengiriman" to jenisLayanan,
                "jenisukuran" to jenisUkuran,
                "cabangasal" to cabangAsal,
                "cabangtujuan" to cabangTujuan,
                "jenisbarang" to if (!isClickLainnya) jenisBarang else "",
                "catatan" to if (isClickLainnya) jenisBarangLainnya else "",
                "gkecpengirim" to gKecPengirim,
                "gkecpenerima" to gKecPenerima,
                "tglpickup" to tanggalPickup,
                "latpengirim" to latPengirim,
                "longpengirim" to longPengirim,
                "latpenerima" to latPenerima,
                "longpenerima" to longPenerima
            )

            // check all field value when user click button order
            when {
                patokanAlamatPengirim.isEmpty() -> {
                    showMessageFieldRequired()
                    return@with
                }
                namaPengirim.isEmpty() -> {
                    showMessageFieldRequired()
                    return@with
                }
                noHandphonePengirim.isEmpty() -> {
                    showMessageFieldRequired()
                    return@with
                }
                patokanAlamatPenerima.isEmpty() -> {
                    showMessageFieldRequired()
                    return@with
                }
                namaPenerima.isEmpty() -> {
                    showMessageFieldRequired()
                    return@with
                }
                noHandphonePenerima.isEmpty() -> {
                    showMessageFieldRequired()
                    return@with
                }
                jenisLayanan.isEmpty() -> {
                    showMessageFieldRequired()
                    return@with
                }
                jenisUkuran.isEmpty() -> {
                    showMessageFieldRequired()
                    return@with
                }
                tanggalPickup.isEmpty() -> {
                    showMessageFieldRequired()
                    return@with
                }
                else -> {
                    when {
                        !isClickLainnya -> { // tidak checklist lainnya
                            if (jenisBarang.isEmpty()) {
                                showMessageFieldRequired()
                            } else {
                                observeCheckout(params)
                            }
                        }
                        isClickLainnya -> {
                            if (jenisBarangLainnya.isEmpty()) {
                                showMessageFieldRequired()
                            } else {
                                observeCheckout(params)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun observeFormAsalPengirim() {
        viewModel.formAsalPengirim.observe(viewLifecycleOwner) { value ->
            cabangAsal = value["id_cabang_asal"].toString()
            val asalPengirim = value["alamatpengirim"].toString()
            gKecPengirim = value["gkecpengirim"].toString()
            kecamatanPengirim = value["kecamatanpengirim"].toString()
            binding.edtAsalPengirim.setText(asalPengirim)

            viewModel.checkStateSubTotal()
        }
    }

    private fun observeLocationPengirim() {
        viewModel.formLatLongPengirim.observe(viewLifecycleOwner) { value ->
            latPengirim = value["latpengirim"].toString()
            longPengirim = value["longpengirim"].toString()
        }
    }

    private fun observeFormAsalPenerima() {
        viewModel.formAsalPenerima.observe(viewLifecycleOwner) { value ->
            cabangTujuan = value["id_cabang_tujuan"].toString()
            val asalPenerima = value["alamatpenerima"].toString()
            gKecPenerima = value["gkecpenerima"].toString()
            kecamatanPenerima = value["kecamatanpenerima"].toString()
            binding.edtAsalPenerima.setText(asalPenerima)

            viewModel.checkStateSubTotal()
        }
    }

    private fun observeLocationPenerima() {
        viewModel.formLatLongPenerima.observe(viewLifecycleOwner) { value ->
            latPenerima = value["latpenerima"].toString()
            longPenerima = value["longpenerima"].toString()
        }
    }

    private fun observeJenisLayanan() {
        viewModel.formJenisLayanan.observe(viewLifecycleOwner) { value ->
            jenisLayanan = value.idlayanan.toString()
            binding.edtJenisLayananPickup.setText(value.layanan)
            viewModel.checkStateSubTotal()
        }
    }

    private fun observeUkuranBarang() {
        viewModel.formUkuranBarang.observe(viewLifecycleOwner) { value ->
            jenisUkuran = value.idjenisukuran.toString()
            binding.edtUkuranBarangPickup.setText(value.jenisukuran)
            viewModel.checkStateSubTotal()
        }
    }

    private fun observeJenisBarang() {
        viewModel.formJenisBarang.observe(viewLifecycleOwner) { value ->
            jenisBarang = value.id.toString()
            binding.edtJenisBarangPickup.setText(value.namajenisbarang)
        }
    }

    private fun observeCheckSubTotal() {
        viewModel.checkSubtotal.observe(viewLifecycleOwner) { state ->
            if (state) {
                val params = hashMapOf(
                    "cabangasal" to cabangAsal,
                    "cabangtujuan" to cabangTujuan,
                    "jenispengiriman" to jenisLayanan,
                    "jenisukuran" to jenisUkuran,
                    "type" to TYPE_PACKAGE_FIXRATE_STRING
                )

                observeCheckOngkir(params)
            }
        }
    }

    private fun observeCheckOngkir(params: HashMap<String, String>) {
        viewModel.checkOngkirFixRate(params).observe(viewLifecycleOwner) { response ->
            with(binding) {
                if (response != null) {
                    if (response.success!!) {
                        val result = response.data?.get(0)

                        tvTotalSubtotal.text = formatRupiah(result?.tarif?.toDouble() ?: 0.0)
                        btnCheckout.isEnabled = true

                    } else {
                        tvTotalSubtotal.text = getString(R.string.p_tarif_not_found)
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
    }

    private fun observeCheckout(params: HashMap<String, String>) {
        viewModel.checkout(params).observe(viewLifecycleOwner) { response ->
            if (response != null) {
                if (response.success!!) {
                    val result = response.data?.get(0)
                    result?.also {
                        moveToCheckout(it, params["namapengirim"].toString())
                    }

                    showMessage(
                        requireActivity(),
                        "Berhasil",
                        "Checkout berhasil",
                        MotionToast.TOAST_SUCCESS
                    )
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

    private fun isExpandInfo(
        state: Boolean,
        cdInfo: MaterialCardView,
        expandInfo: ConstraintLayout,
        icDropDown: ImageView
    ) {
        if (state) {
            TransitionManager.beginDelayedTransition(cdInfo, AutoTransition())
            expandInfo.visibility = VISIBLE
            icDropDown.setImageResource(R.drawable.ic_drop_down)
        } else {
            expandInfo.visibility = GONE
            icDropDown.setImageResource(R.drawable.ic_drop_right)
        }
    }

    override fun onResume() {
        super.onResume()
        setVisibilityBottomHead(requireActivity(), false)

        viewModel.stateInfoPengirim.observe(viewLifecycleOwner) { state ->
            with(binding) {
                isExpandInfo(state, cdInfoPengirim, expandInfoPengirim, icDropdownPengirim)
            }
        }

        viewModel.stateInfoPenerima.observe(viewLifecycleOwner) { state ->
            with(binding) {
                isExpandInfo(state, cdInfoPenerima, expandInfoPenerima, icDropdownPenerima)
            }
        }

        viewModel.stateInfoPickup.observe(viewLifecycleOwner) { state ->
            with(binding) {
                isExpandInfo(state, cdInfoPickup, expandInfoPickup, icDropdownPickup)
            }
        }

        observeJenisLayanan()
        observeUkuranBarang()
        observeJenisBarang()
        observeCheckSubTotal()
    }

    private val pickContact = registerForActivityResult(PickContact()) {
        it?.also { contactUri ->
            val projection = arrayOf(
                ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER
            )

            context?.contentResolver?.query(contactUri, projection, null, null, null)?.apply {
                moveToFirst()

                if (isPengirim) {
                    binding.edtNumberPhonePengirim.setText(normalizedNumber(getString(0)))
                } else {
                    binding.edtNumberPhonePenerima.setText(normalizedNumber(getString(0)))
                }
                close()
            }
        }
    }

    private fun checkClickLainnya(state: Boolean) {
        with(binding) {
            if (state) {
                tiJenisBarang.isEnabled = false
                tiJenisBarangLainnya.visibility = VISIBLE
            } else {
                tiJenisBarang.isEnabled = true
                tiJenisBarangLainnya.visibility = GONE
            }
        }
    }

    private fun moveToPickLocation(idForm: Int) {
        val toPickLocation =
            PFixRateFragmentDirections.actionPFixRateFragmentToPickPlaceLocationFragment().apply {
                codeForm = idForm
                typePackage = TYPE_PACKAGE_FIXRATE
            }
        findNavController().navigate(toPickLocation)
    }

    private fun moveToCheckout(result: ResultCheckout, namaPengirim: String) {
        val toCheckout =
            PFixRateFragmentDirections.actionPFixRateFragmentToCheckoutFragment("Fix Rate")
                .apply {
                    noInvoice = result.nomorpemesanan.toString()
                    name = namaPengirim
                    totalPayment = result.biaya.toString()
                }
        findNavController().navigate(toCheckout)
    }

    private fun showMessageFieldRequired() {
        showMessage(
            requireActivity(),
            "Gagal Checkout!",
            "Field wajib masih ada yang kosong, isi terlebih dahulu",
            MotionToast.TOAST_ERROR
        )
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
        viewModel.apply {
            removeFormAsalPengirim()
            removeFormAsalPenerima()
            removeFormLatLongPengirim()
            removeFormLatLongPenerima()
            removeFormJenisLayanan()
            removeFormUkuranBarang()
            removeFormJenisBarang()
            setStateInfoPengirim(false)
            setStateInfoPenerima(false)
            setStateInfoPickup(false)
        }
    }
}