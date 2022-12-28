package com.pexpress.pexpresscustomer.view.main.order.p_kilometer

import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
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
import com.pexpress.pexpresscustomer.BuildConfig.MAPS_API_KEY
import com.pexpress.pexpresscustomer.R
import com.pexpress.pexpresscustomer.databinding.FragmentPKilometerBinding
import com.pexpress.pexpresscustomer.session.UserPreference
import com.pexpress.pexpresscustomer.utils.*
import com.pexpress.pexpresscustomer.utils.UtilsCode.FORM_PENERIMA
import com.pexpress.pexpresscustomer.utils.UtilsCode.FORM_PENGIRIM
import com.pexpress.pexpresscustomer.utils.UtilsCode.PATTERN_DATE_POST
import com.pexpress.pexpresscustomer.utils.UtilsCode.PATTERN_DATE_VIEW
import com.pexpress.pexpresscustomer.utils.UtilsCode.TAG
import com.pexpress.pexpresscustomer.utils.UtilsCode.TYPE_PACKAGE_KILOMETER
import com.pexpress.pexpresscustomer.utils.UtilsCode.TYPE_PACKAGE_KILOMETER_STRING
import com.pexpress.pexpresscustomer.view.dialog.DialogLoadingFragment
import com.pexpress.pexpresscustomer.view.main.order.dialog.jenis_barang.JenisBarangDialogFragment
import com.pexpress.pexpresscustomer.view.main.order.dialog.jenis_layanan.JenisLayananDialogFragment
import com.pexpress.pexpresscustomer.view.main.order.dialog.ukuran_barang.UkuranBarangDialogFragment
import com.pexpress.pexpresscustomer.view.main.order.viewmodel.PKilometerViewModel
import www.sanju.motiontoast.MotionToast
import java.util.*

class PKilometerFragment : Fragment() {

    private var _binding: FragmentPKilometerBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<PKilometerViewModel>()
    private lateinit var loadingFragment: DialogLoadingFragment

    private lateinit var userPreference: UserPreference

    private lateinit var modalJenisLayanan: JenisLayananDialogFragment
    private lateinit var modalItemsSize: UkuranBarangDialogFragment
    private lateinit var modalJenisBarang: JenisBarangDialogFragment

    private var cabangAsal = ""
    private var cabangTujuan = ""
    private var gKecPengirim = ""
    private var gKecPenerima = ""
    private var latPengirim = ""
    private var longPengirim = ""
    private var latPenerima = ""
    private var longPenerima = ""
    private var idLocPenerima = ""
    private var idLocPengirim = ""
    private var jarak = ""
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
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPKilometerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar()

        userPreference = UserPreference(requireContext())

        prepareViewKilometer()
        prepareDataKilometer()
    }

    private fun prepareViewKilometer() {
        loadingFragment = DialogLoadingFragment()

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

            btnCheckout.setOnClickListener { checkout() }
        }
    }

    private fun prepareDataKilometer() {
        modalJenisLayanan = JenisLayananDialogFragment()
        modalItemsSize = UkuranBarangDialogFragment()
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
            edtJenisLayananPickup.setOnClickListener {
                modalJenisLayanan.newInstance(TYPE_PACKAGE_KILOMETER)
                    .show(parentFragmentManager, JenisLayananDialogFragment.TAG)
            }

            edtUkuranBarangPickup.setOnClickListener {
                modalItemsSize.newInstance(TYPE_PACKAGE_KILOMETER)
                    .show(parentFragmentManager, UkuranBarangDialogFragment.TAG)
            }

            edtTanggalPickup.setOnClickListener {
                loadingFragment.loader(parentFragmentManager, true)
                if (jenisLayanan.isEmpty()) {
                    loadingFragment.loader(parentFragmentManager, false)
                    showMessage(
                        requireActivity(),
                        getString(R.string.text_warning),
                        "Pilih jenis layanan terlebih dahulu sebelum mengisi tanggal pickup!",
                        MotionToast.TOAST_WARNING,
                    )
                    return@setOnClickListener
                }

                observeCheckCutOff(jenisLayanan.toInt())
            }

            edtJenisBarangPickup.setOnClickListener {
                modalJenisBarang.newInstance(TYPE_PACKAGE_KILOMETER)
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
                "longpenerima" to longPenerima,
                "jaraktempuh" to roundingDistance(jarak.toDouble())
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
                jarak.isEmpty() -> {
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
                                viewModel.changeOrderPaket.observe(viewLifecycleOwner) { value ->
                                    val id = value["id"] as Int
                                    val state = value["state"] as Boolean

                                    if (state) {
                                        observeEditCheckout(id, params)
                                    } else {
                                        observeCheckout(params)
                                    }
                                }
                            }
                        }
                        isClickLainnya -> {
                            if (jenisBarangLainnya.isEmpty()) {
                                showMessageFieldRequired()
                            } else {
                                viewModel.changeOrderPaket.observe(viewLifecycleOwner) { value ->
                                    val id = value["id"] as Int
                                    val state = value["state"] as Boolean

                                    if (state) {
                                        observeEditCheckout(id, params)
                                    } else {
                                        observeCheckout(params)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun observeFormAsalPengirim() {
        viewModel.formAsalPengirim.observe(viewLifecycleOwner) { value ->
            if (value != null) {
                cabangAsal = value["id_cabang_asal"].toString()
                val asalPengirim = value["alamatpengirim"].toString()
                gKecPengirim = value["gkecpengirim"].toString()
                kecamatanPengirim = value["kecamatanpengirim"].toString()
                binding.edtAsalPengirim.setText(asalPengirim)
                viewModel.checkStateSubTotal()
            }
        }
    }

    private fun observeLocationPengirim() {
        viewModel.formLatLongPengirim.observe(viewLifecycleOwner) { value ->
            idLocPengirim = value["placeId"].toString()
            latPengirim = value["latpengirim"].toString()
            longPengirim = value["longpengirim"].toString()
            checkDistance() // cek jarak
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
            idLocPenerima = value["placeId"].toString()
            latPenerima = value["latpenerima"].toString()
            longPenerima = value["longpenerima"].toString()
            checkDistance() // cek jarak
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

    private fun openPickDate(isCutOff: Boolean) {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Jakarta"))
        lateinit var validator: CalendarConstraints.DateValidator
        val today = MaterialDatePicker.todayInUtcMilliseconds()

        if (isCutOff) {
            validator = DateValidatorPointForward.now()
            calendar.timeInMillis = today
        } else {
            calendar.apply {
                timeInMillis = today
                add(Calendar.DAY_OF_YEAR, 1)
            }
            validator = DateValidatorPointForward.from(calendar.timeInMillis)
        }

        val constraintsBuilder =
            CalendarConstraints.Builder()
                .setValidator(validator)
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Pilih Tanggal Pickup")
                .setSelection(calendar.timeInMillis)
                .setCalendarConstraints(constraintsBuilder.build())
                .build()

        datePicker.show(parentFragmentManager, "TAG")

        datePicker.addOnPositiveButtonClickListener { selection ->
            binding.edtTanggalPickup.setText(
                FormatDate().outputDateFormat(PATTERN_DATE_VIEW).format(selection)
            )
        }
    }

    private fun observeCheckCutOff(layanan: Int) {
        viewModel.checkCutOff(layanan).observe(viewLifecycleOwner) { response ->
            loadingFragment.loader(parentFragmentManager, false)
            if (response != null) {
                if (response.success!!) {
                    val status = response.status ?: true
                    openPickDate(status)
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

    private fun observeCheckSubTotal() {
        viewModel.checkSubtotal.observe(viewLifecycleOwner) { state ->
            if (state) {
                val params = hashMapOf(
                    "cabangasal" to cabangAsal,
                    "cabangtujuan" to cabangTujuan,
                    "jenispengiriman" to jenisLayanan,
                    "jenisukuran" to jenisUkuran,
                    "jarak" to roundingDistance(jarak.toDouble()),
                    "type" to TYPE_PACKAGE_KILOMETER_STRING
                )

                observeCheckOngkir(params)
            }
        }
    }

    private fun observeCheckOngkir(params: HashMap<String, String>) {
        viewModel.checkOngkirKilometer(params).observe(viewLifecycleOwner) { response ->
            with(binding) {
                if (response != null) {
                    if (response.success!!) {
                        val result = response.data

                        tvTotalSubtotal.text = formatRupiah(result?.biaya?.toDouble() ?: 0.0)
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
                        moveToCheckout(
                            it.id ?: 0,
                            it.namapengirim.toString(),
                            it.nomorpemesanan.toString(),
                            it.biaya.toString()
                        )
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
                        MotionToast.TOAST_WARNING
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

    private fun observeEditCheckout(id: Int, params: HashMap<String, String>) {
        viewModel.editCheckout(id, params).observe(viewLifecycleOwner) { response ->
            Log.d(TAG, "observeEditCheckout: response = $response")
            if (response != null) {
                if (response.success!!) {
                    val result = response.data
                    result?.also {
                        moveToCheckout(
                            it.id ?: 0,
                            params["namapengirim"].toString(),
                            it.nomorpemesanan.toString(),
                            it.biaya.toString()
                        )
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

    private fun checkDistance() {
        if (latPengirim.isNotEmpty() && longPengirim.isNotEmpty() && latPenerima.isNotEmpty() && longPenerima.isNotEmpty()) {
            val params = hashMapOf(
                "origin" to "place_id:$idLocPengirim",
                "destination" to "place_id:$idLocPenerima",
                "region" to "id",
                "key" to MAPS_API_KEY
            )
            // send to viewmodel and get response
            observeDistance(params)
        }
    }

    private fun observeDistance(params: HashMap<String, String>) {
        viewModel.checkDistance(params).observe(viewLifecycleOwner) { response ->
            with(binding) {
                if (response != null) {
                    if (response.status == "OK") {
                        val distance = response.routes?.get(0)?.legs?.get(0)?.distance
                        jarak = distance!!.parseKm()
                        edtJarak.setText("$jarak Km")
                    } else {
                        edtJarak.setText("Jarak tidakk ditemukan")
                    }
                } else {
                    edtJarak.setText("Jarak tidak ditemukan")
                }
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
            PKilometerFragmentDirections.actionPKilometerFragmentToPickLocationFragment().apply {
                codeForm = idForm
                typePackage = TYPE_PACKAGE_KILOMETER
            }
        findNavController().navigate(toPickLocation)
    }

    private fun moveToCheckout(
        id: Int,
        namaPengirim: String,
        nomorPemesanan: String,
        totalPembayaran: String
    ) {
        val toCheckout =
            PKilometerFragmentDirections.actionPKilometerFragmentToCheckoutFragment(
                TYPE_PACKAGE_KILOMETER_STRING
            )
                .apply {
                    this.id = id
                    titlePackage = "Kilometer"
                    noInvoice = nomorPemesanan
                    name = namaPengirim
                    totalPayment = totalPembayaran
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
            changeOrderPaket(state = false)
        }
    }
}