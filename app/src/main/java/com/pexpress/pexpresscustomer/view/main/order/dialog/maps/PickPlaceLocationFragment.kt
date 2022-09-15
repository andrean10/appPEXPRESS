package com.pexpress.pexpresscustomer.view.main.order.dialog.maps

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
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
import com.pexpress.pexpresscustomer.BuildConfig
import com.pexpress.pexpresscustomer.R
import com.pexpress.pexpresscustomer.databinding.FragmentPickPlaceLocationBinding
import com.pexpress.pexpresscustomer.utils.UtilsCode.FORM_PENERIMA
import com.pexpress.pexpresscustomer.utils.UtilsCode.FORM_PENGIRIM
import com.pexpress.pexpresscustomer.utils.UtilsCode.TAG
import com.pexpress.pexpresscustomer.utils.UtilsCode.TYPE_PACKAGE_FIXRATE
import com.pexpress.pexpresscustomer.utils.UtilsCode.TYPE_PACKAGE_KILOMETER
import com.pexpress.pexpresscustomer.utils.UtilsMaps.placeFields
import com.pexpress.pexpresscustomer.utils.closeKeyboard
import com.pexpress.pexpresscustomer.utils.showMessage
import com.pexpress.pexpresscustomer.view.main.order.dialog.maps.adapter.PlacePredictionAdapter
import com.pexpress.pexpresscustomer.view.main.order.viewmodel.OrderPaketViewModel
import com.pexpress.pexpresscustomer.view.main.order.viewmodel.PFixRateViewModel
import com.pexpress.pexpresscustomer.view.main.order.viewmodel.PKilometerViewModel
import www.sanju.motiontoast.MotionToast


class PickPlaceLocationFragment : Fragment() {

    private var _binding: FragmentPickPlaceLocationBinding? = null
    private val binding get() = _binding!!

    private val viewModel by activityViewModels<OrderPaketViewModel>()
    private val viewModelFixRate by activityViewModels<PFixRateViewModel>()
    private val viewModelKilometer by activityViewModels<PKilometerViewModel>()

    private lateinit var placePredictionAdapter: PlacePredictionAdapter
    private lateinit var placesClient: PlacesClient
    private lateinit var token: AutocompleteSessionToken

    private var idTypePackage = 0
    private var idForm = 0
    private var pointMapAutocomplete: LatLng? = null

    //    private var isPlacesClicked = false
    private var isUserSetLocation = false

//    private lateinit var modalMaps: MapsFragment // tidak digunakan

    private val BOUNDS = RectangularBounds.newInstance(
        LatLngBounds(
            LatLng(-5.4902851, 136.9151474),
            LatLng(0.1617255, 99.5820647)
        )
    )
    private val DEFAULT_ORIGIN = LatLng(-1.6175488, 118.5606852)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPickPlaceLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val args = PickPlaceLocationFragmentArgs.fromBundle(arguments as Bundle)
        idForm = args.codeForm
        idTypePackage = args.typePackage

        setToolbar()
        setupView()

        // tidak digunakan
//        with(binding) {
//            btnChooseMap.setOnClickListener {
//                modalMaps.setDataMaps(idForm, pointMapAutocomplete)
//                modalMaps.show(parentFragmentManager, MapsFragment.TAG)
//            }
//        }
    }

    private fun setupView() {
        with(binding) {
//            modalMaps = MapsFragment() // tidak digunakan

            token = AutocompleteSessionToken.newInstance()
            if (!Places.isInitialized()) {
                Places.initialize(requireContext(), BuildConfig.MAPS_API_KEY)
            }

            placesClient = Places.createClient(requireContext())

            placePredictionAdapter = PlacePredictionAdapter()
            with(rvPlacePrediction) {
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
                addItemDecoration(
                    DividerItemDecoration(requireContext(), DividerItemDecoration.HORIZONTAL)
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
        observeDataLocation()
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
            val gkec = place.address ?: ""
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
            checkDistrict(kecamatan, address, gkec)
        }
    }

    private fun checkDistrict(kecamatan: String, address: String, gKec: String) {
        val layanan = when (idTypePackage) {
            TYPE_PACKAGE_FIXRATE -> "fix_rate"
            TYPE_PACKAGE_KILOMETER -> "kilometer"
            else -> "type_not_found"
        }

        viewModel.checkDistrict(kecamatan, layanan).observe(viewLifecycleOwner) { response ->
            if (response != null) {
                if (response.success!!) {
                    val result = response.data?.get(0)
                    val idDistrict = result?.idDistrict ?: 0
                    checkCabang(idDistrict, address, gKec)
                } else {
                    showMessage(
                        requireActivity(),
                        getString(R.string.failed_title),
                        response.message.toString(),
                        MotionToast.TOAST_ERROR
                    )
                    isLoadingClickDetail(false)
                }
            } else {
                showMessage(
                    requireActivity(),
                    getString(R.string.failed_title),
                    getString(R.string.picklocation_placeholder_failed_get_cabang2),
                    MotionToast.TOAST_ERROR
                )
                isLoadingClickDetail(false)
            }
        }
    }

    private fun checkCabang(idDistrict: Int, address: String, gKec: String) {
        viewModel.checkCabang(idDistrict).observe(viewLifecycleOwner) { response ->
            isLoadingClickDetail(false)
            Log.d(TAG, "checkCabang: $response")
            if (response != null) {
                if (response.success!!) {
                    val result = response.data?.get(0)
                    result?.also {
                        if (it.isactive == 1) {
                            setFormDataLocation(result.idCabang ?: 0, address, gKec, idDistrict)
                            showMessage(
                                requireActivity(),
                                getString(R.string.text_success),
                                "Alamat Tersimpan",
                                MotionToast.TOAST_SUCCESS
                            )
                            closeKeyboard(requireActivity())
                            findNavController().navigateUp()

                            // tidak digunakan
//                            binding.btnChooseMap.isEnabled = true // tidak digunakan
//                            isPlacesClicked = true
                        } else {
                            showMessage(
                                requireActivity(),
                                getString(R.string.failed_title),
                                getString(R.string.picklocation_placeholder_failed_get_cabang),
                                MotionToast.TOAST_ERROR
                            )
                        }
                    }
                } else { // server bermasalah
                    showMessage(
                        requireActivity(),
                        getString(R.string.failed_title),
                        getString(R.string.picklocation_placeholder_failed_get_cabang2),
                        MotionToast.TOAST_ERROR
                    )
                }
            }
        }
    }

    private fun setFormDataLocation(
        idCabang: Int = 0,
        address: String,
        gKec: String,
        idDistrict: Int
    ) {
        when (idForm) {
            FORM_PENGIRIM -> {
                val params = hashMapOf<String, Any>(
                    "id_cabang_asal" to idCabang,
                    "alamatpengirim" to address,
                    "gkecpengirim" to gKec,
                    "kecamatanpengirim" to idDistrict,
                )
                val valueLatLng = hashMapOf(
                    "latpengirim" to pointMapAutocomplete?.latitude.toString(),
                    "longpengirim" to pointMapAutocomplete?.longitude.toString()
                )

                if (idTypePackage == TYPE_PACKAGE_FIXRATE) {
                    viewModelFixRate.apply {
                        setFormAsalPengirim(params)
                        setFormLatLongPengirim(valueLatLng)
                    }
                } else {
                    viewModelKilometer.apply {
                        setFormAsalPengirim(params)
                        setFormLatLongPengirim(valueLatLng)
                    }
                }
            }
            FORM_PENERIMA -> {
                val params = hashMapOf<String, Any>(
                    "id_cabang_tujuan" to idCabang,
                    "alamatpenerima" to address,
                    "gkecpenerima" to gKec,
                    "kecamatanpenerima" to idDistrict,
                )
                val valueLatLng = hashMapOf(
                    "latpenerima" to pointMapAutocomplete?.latitude.toString(),
                    "longpenerima" to pointMapAutocomplete?.longitude.toString()
                )

                if (idTypePackage == TYPE_PACKAGE_FIXRATE) {
                    viewModelFixRate.apply {
                        setFormAsalPenerima(params)
                        setFormLatLongPenerima(valueLatLng)
                    }
                } else {
                    viewModelKilometer.apply {
                        setFormAsalPenerima(params)
                        setFormLatLongPenerima(valueLatLng)
                    }
                }
            }
        }
    }

    private fun observeDataLocation() {
        when (idForm) {
            FORM_PENGIRIM -> {
                if (idTypePackage == TYPE_PACKAGE_FIXRATE) {
                    viewModelFixRate.formLatLongPengirim.observe(viewLifecycleOwner) { value ->
                        if (!value["latpengirim"].isNullOrEmpty()) {
                            isUserSetLocation = true
                        }
                    }
                } else {
                    viewModelKilometer.formLatLongPengirim.observe(viewLifecycleOwner) { value ->
                        if (!value["latpengirim"].isNullOrEmpty()) {
                            isUserSetLocation = true
                        }
                    }
                }
            }
            FORM_PENERIMA -> {
                if (idTypePackage == TYPE_PACKAGE_FIXRATE) {
                    viewModelFixRate.formLatLongPenerima.observe(viewLifecycleOwner) { value ->
                        if (!value["latpenerima"].isNullOrEmpty()) {
                            isUserSetLocation = true
                        }
                    }
                } else {
                    viewModelKilometer.formLatLongPenerima.observe(viewLifecycleOwner) { value ->
                        if (!value["latpenerima"].isNullOrEmpty()) {
                            isUserSetLocation = true
                        }
                    }
                }
            }
        }
    }

    private fun isLoadingGetData(state: Boolean) {
        with(binding) {
            if (state) {
                pbPlacePrediction.visibility = VISIBLE
                rvPlacePrediction.visibility = GONE
            } else {
                pbPlacePrediction.visibility = GONE
                rvPlacePrediction.visibility = VISIBLE
            }
        }
    }

    private fun isLoadingClickDetail(state: Boolean) {
        with(binding) {
            if (state) {
                layoutLoading.visibility = VISIBLE
                requireActivity().window.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                )
            } else {
                layoutLoading.visibility = GONE
                requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) findNavController().navigateUp()
        return super.onOptionsItemSelected(item)
    }

    private fun setToolbar() {
        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.toolbar)
    }

//    private fun showPopUp() {
//        AlertDialog.Builder(requireContext())
//            .setTitle("Pemilihan Lokasi Belum Diselesaikan!")
//            .setMessage("Apakah Anda Yakin Ingin Keluar dari Halaman Ini?")
//            .setPositiveButton(
//                "Lanjutkan"
//            ) { dialogInterface, _ ->
//                dialogInterface.dismiss()
//            }
//            .setNegativeButton("Keluar") { _, _ ->
//                setFormDataLocation(0, "", "", 0)
//                findNavController().navigateUp()
//            }
//            .create()
//            .show()
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}