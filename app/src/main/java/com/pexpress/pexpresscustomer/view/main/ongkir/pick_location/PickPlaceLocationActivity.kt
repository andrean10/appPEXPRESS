package com.pexpress.pexpresscustomer.view.main.ongkir.pick_location

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.pexpress.pexpresscustomer.BuildConfig.MAPS_API_KEY
import com.pexpress.pexpresscustomer.R
import com.pexpress.pexpresscustomer.databinding.ActivityPickPlaceLocationBinding
import com.pexpress.pexpresscustomer.utils.UtilsCode.FORM_ASAL
import com.pexpress.pexpresscustomer.utils.UtilsCode.FORM_TUJUAN
import com.pexpress.pexpresscustomer.utils.UtilsCode.TAG
import com.pexpress.pexpresscustomer.utils.UtilsCode.TYPE_PACKAGE_FIXRATE
import com.pexpress.pexpresscustomer.utils.UtilsCode.TYPE_PACKAGE_KILOMETER
import com.pexpress.pexpresscustomer.utils.UtilsMaps.placeFields
import com.pexpress.pexpresscustomer.utils.closeKeyboard
import com.pexpress.pexpresscustomer.utils.showMessage
import com.pexpress.pexpresscustomer.view.main.order.dialog.maps.adapter.PlacePredictionAdapter
import com.pexpress.pexpresscustomer.view.main.order.viewmodel.OrderPaketViewModel
import www.sanju.motiontoast.MotionToast

class PickPlaceLocationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPickPlaceLocationBinding
    private val viewModel by viewModels<OrderPaketViewModel>()
    private lateinit var placePredictionAdapter: PlacePredictionAdapter
    private lateinit var placesClient: PlacesClient
    private lateinit var token: AutocompleteSessionToken

//    private lateinit var modalMaps: MapsFragment // tidak digunakan

    private var idTypePackage = 0
    private var idForm = 0
    private var pointMapAutocomplete: LatLng? = null
//    private var isPlacesClicked = false
//    private var isUserSetLocation = false

    private val BOUNDS = RectangularBounds.newInstance(
        LatLngBounds(
            LatLng(-5.4902851, 136.9151474),
            LatLng(0.1617255, 99.5820647)
        )
    )
    private val DEFAULT_ORIGIN = LatLng(-1.6175488, 118.5606852)

    companion object {
        const val EXTRA_BUNDLE = "extra_bundle"
        const val EXTRA_FORM = "extra_form"
        const val EXTRA_TYPE_PACKAGE = "extra_type_package"
        const val EXTRA_ALAMAT = "extra_alamat"
        const val EXTRA_GKEC = "extra_gkec"
        const val EXTRA_CABANG = "extra_cabang"
        const val EXTRA_LATITUDE = "extra_latitude"
        const val EXTRA_LONGITUDE = "extra_longitude"
        const val EXTRA_PLACE_ID = "extra_place_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPickPlaceLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent.getBundleExtra(EXTRA_BUNDLE)
        intent?.also {
            idForm = it.getInt(EXTRA_FORM)
            idTypePackage = it.getInt(EXTRA_TYPE_PACKAGE)
        }

        setToolbar()
        setupView()

        // tidak digunakan
//        with(binding) {
//            btnChooseMap.setOnClickListener {
//                modalMaps.setDataMaps(idForm, pointMapAutocomplete)
//                modalMaps.show(supportFragmentManager, MapsFragment.TAG)
//            }
//        }
    }

    private fun setupView() {
        with(binding) {
            // tidak digunakan
//            modalMaps = MapsFragment()
//            if (idTypePackage == TYPE_PACKAGE_FIXRATE) {
//                btnChooseMap.visibility = View.GONE
//            }

            token = AutocompleteSessionToken.newInstance()
            if (!Places.isInitialized()) {
                Places.initialize(this@PickPlaceLocationActivity, MAPS_API_KEY)
            }

            placesClient = Places.createClient(this@PickPlaceLocationActivity)

            placePredictionAdapter = PlacePredictionAdapter()
            with(rvPlacePrediction) {
                layoutManager = LinearLayoutManager(this@PickPlaceLocationActivity)
                setHasFixedSize(true)
                addItemDecoration(
                    DividerItemDecoration(
                        this@PickPlaceLocationActivity,
                        DividerItemDecoration.HORIZONTAL
                    )
                )
                adapter = placePredictionAdapter
            }

            placePredictionAdapter.setOnPlaceItemClickCallBack(object :
                PlacePredictionAdapter.OnPlaceItemClickCallBack {
                override fun onPlaceClicked(place: AutocompletePrediction) {
                    isLoadingClickDetail(true)
                    getDetailPlaces(place.placeId)
                }
            })
        }

        observeQuerySearch()
    }

    private fun observeQuerySearch() {
        binding.edtPlacePrediction.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s?.length!! > 0) {
                    searchClick(s.toString())
                }
            }
        })
    }

    private fun searchClick(query: String) {
        if (query.isNotEmpty()) {
            isLoadingGetData(true)
            getPredictionPlaces(query)
        }
    }

    private fun getPredictionPlaces(query: String) {
        val request = FindAutocompletePredictionsRequest.builder()
            .setLocationBias(BOUNDS)
            .setOrigin(DEFAULT_ORIGIN)
            .setCountries("ID")
            .setSessionToken(token)
            .setQuery(query)
            .build()

        placesClient.findAutocompletePredictions(request).addOnSuccessListener { response ->
            placePredictionAdapter.setPredictions(response.autocompletePredictions)
            isLoadingGetData(false)
        }.addOnFailureListener { exception ->
            if (exception is ApiException) {
                Log.e(TAG, "Place not found: " + exception.statusCode)
            }
            isLoadingGetData(false)
        }
    }

    private fun getDetailPlaces(placeId: String) {
        val request = FetchPlaceRequest.newInstance(placeId, placeFields)

        placesClient.fetchPlace(request).addOnSuccessListener { response ->
            val place = response.place
            pointMapAutocomplete = place.latLng
            val addressNameComponent = place.addressComponents?.asList()
            val address = place.name ?: ""
            val gKec = place.address ?: ""

            var kecamatan = ""

            addressNameComponent?.forEach { component ->
                component.types.forEach { type ->
                    when (type) {
                        "administrative_area_level_3" -> {
                            kecamatan = if (component.name.contains("Kecamatan")) {
                                component.name.replace("Kecamatan ", "")
                            } else {
                                component.name
                            }
                        }
                    }
                }
            }
            checkDistrict(kecamatan, address, gKec, place.id)
        }
    }

    private fun checkDistrict(kecamatan: String, address: String, gKec: String, placeId: String?) {
        val layanan = when (idTypePackage) {
            TYPE_PACKAGE_FIXRATE -> "fix_rate"
            TYPE_PACKAGE_KILOMETER -> "kilometer"
            else -> "type_not_found"
        }

        viewModel.checkDistrict(kecamatan, layanan).observe(this) { response ->
            if (response != null) {
                if (response.success!!) {
                    val result = response.data?.get(0)
                    val idDistrict = result?.idDistrict ?: 0
                    checkCabang(idDistrict, address, gKec, placeId)
                } else {
                    showMessage(
                        this,
                        getString(R.string.failed_title),
                        response.message.toString(),
                        MotionToast.TOAST_ERROR
                    )
                    isLoadingClickDetail(false)
                }
            } else {
                showMessage(
                    this,
                    getString(R.string.failed_title),
                    getString(R.string.picklocation_placeholder_failed_get_cabang2),
                    MotionToast.TOAST_ERROR
                )
                isLoadingClickDetail(false)
            }
        }
    }

    private fun checkCabang(idDistrict: Int, address: String, gKec: String, placeId: String?) {
        viewModel.checkCabang(idDistrict).observe(this) { response ->
            isLoadingClickDetail(false)
            if (response != null) {
                if (response.success!!) {
                    val result = response.data?.get(0)
                    result?.also {
                        if (it.isactive == 1) {
                            setFormDataLocation(result.idCabang.toString(), address, gKec, placeId)
                            showMessage(
                                this,
                                getString(R.string.text_success),
                                "Alamat Tersimpan",
                                MotionToast.TOAST_SUCCESS
                            )
                            closeKeyboard(this)
                            finish()

                            // tidak digunakan
//                            binding.btnChooseMap.isEnabled = true
                        } else {
                            showMessage(
                                this,
                                getString(R.string.failed_title),
                                getString(R.string.picklocation_placeholder_failed_get_cabang),
                                MotionToast.TOAST_ERROR
                            )
                        }
                    }
                } else { // server bermasalah
                    showMessage(
                        this,
                        getString(R.string.failed_title),
                        getString(R.string.picklocation_placeholder_failed_get_cabang2),
                        MotionToast.TOAST_ERROR
                    )
                }
            }
        }
    }

    private fun setFormDataLocation(
        idCabang: String,
        address: String,
        gKec: String,
        placeId: String?
    ) {
        when (idForm) {
            FORM_ASAL -> {
                if (idTypePackage == TYPE_PACKAGE_KILOMETER) {
                    val valueLatLng = hashMapOf(
                        "latpengirim" to pointMapAutocomplete?.latitude.toString(),
                        "longpengirim" to pointMapAutocomplete?.longitude.toString(),
                        "placeId" to placeId.toString()
                    )
                    viewModel.setFormLatLongPengirim(valueLatLng)
                    observeResultbackAsal(idCabang, address, gKec)
                } else {
                    val intent = Intent().apply {
                        putExtra(EXTRA_FORM, idForm)
                        putExtra(EXTRA_CABANG, idCabang)
                        putExtra(EXTRA_ALAMAT, address)
                        putExtra(EXTRA_GKEC, gKec)
                    }
                    setResult(RESULT_OK, intent)
                    finish()
                }
            }
            FORM_TUJUAN -> {
                if (idTypePackage == TYPE_PACKAGE_KILOMETER) {
                    val valueLatLng = hashMapOf(
                        "latpenerima" to pointMapAutocomplete?.latitude.toString(),
                        "longpenerima" to pointMapAutocomplete?.longitude.toString(),
                        "placeId" to placeId.toString()
                    )
                    viewModel.setFormLatLongPenerima(valueLatLng)
                    observeResultbackTujuan(idCabang, address, gKec)
                } else {
                    val intent = Intent().apply {
                        putExtra(EXTRA_FORM, idForm)
                        putExtra(EXTRA_CABANG, idCabang)
                        putExtra(EXTRA_ALAMAT, address)
                        putExtra(EXTRA_GKEC, gKec)
                    }
                    setResult(RESULT_OK, intent)
                    finish()
                }
            }
        }
    }

    private fun observeResultbackAsal(idCabang: String, address: String, gKec: String) {
        viewModel.formLatLongPengirim.observe(this) { value ->
            if (!value["latpengirim"].isNullOrEmpty()) {
                val intent = Intent().apply {
                    putExtra(EXTRA_FORM, idForm)
                    putExtra(EXTRA_CABANG, idCabang)
                    putExtra(EXTRA_ALAMAT, address)
                    putExtra(EXTRA_GKEC, gKec)
                    putExtra(EXTRA_LATITUDE, value["latpengirim"])
                    putExtra(EXTRA_LONGITUDE, value["longpengirim"])
                    putExtra(EXTRA_PLACE_ID, value["placeId"])
                }
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }

    private fun observeResultbackTujuan(idCabang: String, address: String, gKec: String) {
        viewModel.formLatLongPenerima.observe(this) { value ->
            if (!value["latpenerima"].isNullOrEmpty()) {
                val intent = Intent().apply {
                    putExtra(EXTRA_FORM, idForm)
                    putExtra(EXTRA_CABANG, idCabang)
                    putExtra(EXTRA_ALAMAT, address)
                    putExtra(EXTRA_GKEC, gKec)
                    putExtra(EXTRA_LATITUDE, value["latpenerima"])
                    putExtra(EXTRA_LONGITUDE, value["longpenerima"])
                    putExtra(EXTRA_PLACE_ID, value["placeId"])
                }
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }

    private fun isLoadingGetData(state: Boolean) {
        with(binding) {
            if (state) {
                pbPlacePrediction.visibility = View.VISIBLE
                rvPlacePrediction.visibility = View.GONE
            } else {
                pbPlacePrediction.visibility = View.GONE
                rvPlacePrediction.visibility = View.VISIBLE
            }
        }
    }

    private fun isLoadingClickDetail(state: Boolean) {
        with(binding) {
            if (state) {
                layoutLoading.visibility = View.VISIBLE
                window.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                )
            } else {
                layoutLoading.visibility = View.GONE
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            // tidak digunakan
//            if (idTypePackage == TYPE_PACKAGE_KILOMETER) {
//                if (isPlacesClicked && isUserSetLocation) {
//                    finish()
//                } else {
//                    showPopUp()
//                }
//            } else {
//                finish()
//            }
        }
        return super.onOptionsItemSelected(item)
    }

//    private fun showPopUp() {
//        AlertDialog.Builder(this)
//            .setTitle("Pemilihan Lokasi Belum Diselesaikan!")
//            .setMessage("Apakah Anda Yakin Ingin Keluar dari Halaman Ini?")
//            .setPositiveButton(
//                "Lanjutkan"
//            ) { dialogInterface, _ ->
//                dialogInterface.dismiss()
//            }
//            .setNegativeButton("Keluar") { _, _ ->
//                setFormDataLocation("", "", "")
//                finish()
//            }
//            .create()
//            .show()
//    }

    private fun setToolbar() {
        setSupportActionBar(binding.toolbar)
    }

    // tidak digunakan lagi
//    override fun onBackPressed() {
//        super.onBackPressed()
//        if (idTypePackage == TYPE_PACKAGE_KILOMETER) {
//            if (isPlacesClicked && isUserSetLocation) {
//                finish()
//            } else {
//                if (isPlacesClicked) {
//                    showPopUp()
//                } else {
//                    finish()
//                }
//            }
//        } else {
//            finish()
//        }
//    }
}