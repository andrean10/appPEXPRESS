package com.pexpress.pexpresscustomer.view.main.order.dialog

import android.Manifest
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pexpress.pexpresscustomer.R
import com.pexpress.pexpresscustomer.databinding.FragmentPickLocationDialogBinding
import com.pexpress.pexpresscustomer.utils.PermissionCode
import com.pexpress.pexpresscustomer.utils.PermissionsApp
import com.pexpress.pexpresscustomer.utils.TrackingUtility
import com.pexpress.pexpresscustomer.utils.showMessage
import com.pexpress.pexpresscustomer.view.main.order.viewmodel.OrderPaketViewModel
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import www.sanju.motiontoast.MotionToast

class PickLocationDialogFragment : BottomSheetDialogFragment(), OnMapReadyCallback,
    EasyPermissions.PermissionCallbacks {

    private var _binding: FragmentPickLocationDialogBinding? = null
    private val binding get() = _binding!!

    private val viewModel by activityViewModels<OrderPaketViewModel>()

    private var codeForm = 0

    private var gMap: GoogleMap? = null
    private var myLocationMarker: Marker? = null
    private lateinit var autocompleteSupportFragment: AutocompleteSupportFragment
//    private var myLocationLatLong: LatLng? = null

    private val DEFAULT_POINT_MAP = LatLng(-6.2693499, 106.9590946)
    private val DEFAULT_ZOOM = 5f
    private val ZOOM_ROAD = 16f
    private val ZOOM_MARKER = 20f

    private var fusedLocationProvider: FusedLocationProviderClient? = null
    private val locationRequest: LocationRequest = LocationRequest.create().apply {
        interval = 30
        fastestInterval = 10
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        maxWaitTime = 60
    }

    private var locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val locationList = locationResult.locations
            if (locationList.isNotEmpty()) {
                // lokasi terakhir didalam list yang terbaru
                val location = locationList.last()
                Log.d(TAG, "onLocationResult: Got Location: $location")
            }
        }
    }

    companion object {
        val TAG = PickLocationDialogFragment::class.simpleName
        const val REQUEST_CODE_LOCATION_PERMISSION = 1
        const val FORM_PENGIRIM = 1
        const val FORM_PENERIMA = 2
    }

//    private val callback = OnMapReadyCallback { googleMap ->
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPickLocationDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        initView()

        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(requireContext())

        with(binding) {
            fabMyLocation.setOnClickListener { checkMyLocation() }
            btnClose.setOnClickListener { dialog?.dismiss() }
        }
    }

    fun setCodeForm(stateForm: Int) {
        codeForm = stateForm
    }

    private fun initView() {
        dialog!!.setOnShowListener {
            val dialog = it as BottomSheetDialog
            val bottomSheet = dialog.findViewById<View>(R.id.design_bottom_sheet)
            bottomSheet?.let { sheet ->
                dialog.behavior.apply {
                    peekHeight = sheet.height
                    isDraggable = false
                }
                sheet.parent.parent.requestLayout()
            }
        }

        requestPermissions()
    }

    private fun checkGPSSetting() {
        // cek apakah permission diizinkan atau tidak
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // meminta request setting location di device
            val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
                .setAlwaysShow(true)

            val locationSettingResponse = LocationServices.getSettingsClient(requireActivity())
                .checkLocationSettings(builder.build())

            locationSettingResponse.addOnCompleteListener { task ->

                Log.d(TAG, "checkGPSSetting: ${task.isComplete}")
                if (task.isComplete) {
                    try {

                        val response = task.getResult(ApiException::class.java)
                        Log.d(TAG, "checkGPSSetting: $response")
                    } catch (e: ApiException) {
                        if (e.statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                            val resolvableApiException = e as ResolvableApiException
                            try {
                                resolvableApiException.startResolutionForResult(
                                    requireActivity(),
                                    101
                                )
                            } catch (sendIntentException: IntentSender.SendIntentException) {
                                sendIntentException.printStackTrace()
                            }
                        } else if (e.statusCode == LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE) {
                            showMessage(
                                requireActivity(), "Gagal",
                                "Pengaturan Tidak Tersedia", MotionToast.TOAST_ERROR
                            )
                        }
                    }
                }
            }
        } else {
            showMessage(
                requireActivity(), "Perizinan Lokasi",
                "Perizinan lokasi dibutuhkan untuk menggunakan fitur ini", MotionToast.TOAST_WARNING
            )
        }
    }

    private fun checkMyLocation() {
        fusedLocationProvider?.lastLocation?.addOnSuccessListener { location ->
            val myLocationLatLng = LatLng(location.latitude, location.longitude)

            myLocationMarker?.remove()
            addMarker(myLocationLatLng)
            moveCameraMaps(myLocationLatLng, ZOOM_MARKER)
        }
            ?.addOnFailureListener {
                Log.d(TAG, "checkMyLocation: ${it.message}")
                showMessage(
                    requireActivity(),
                    "Gagal",
                    "Kesalahan Sistem!",
                    MotionToast.TOAST_ERROR
                )
            }
    }

    private fun requestPermissions() {
        if (TrackingUtility.hasLocationPermissions(requireContext())) {
            return
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.requestPermissions(
                this,
                "Perizinan lokasi dibutuhkan agar aplikasi dapat bekerja dengan normal",
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } else {
            EasyPermissions.requestPermissions(
                this,
                "Perizinan lokasi dibutuhkan agar aplikasi dapat bekerja dengan normal",
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
//            Log.d(TAG, "onPermissionsDenied: Permanent di tolak")
        } else {
            requestPermissions()
//            Log.d(TAG, "onPermissionsDenied: Tidak Permanen")
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {}

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onResume() {
        super.onResume()
        // jika pengguna aktif kembali maka update lokasi terakhir
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProvider?.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

    private val placeFields =
        listOf(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.ADDRESS,
            Place.Field.ADDRESS_COMPONENTS,
            Place.Field.LAT_LNG
        )

    private fun setupAutocompleteSupportFragment() {
        autocompleteSupportFragment =
            (childFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment?)!!
        autocompleteSupportFragment.apply {
            setPlaceFields(placeFields)
            setOnPlaceSelectedListener(placeSelectionListener)
            setHint("Masukkan Lokasi")
            setCountries("ID")
            setCountry("id")
//            setLocationBias(locationBias)
//            setLocationRestriction(locationRestriction)
            setTypeFilter(TypeFilter.ADDRESS)
            setActivityMode(AutocompleteActivityMode.OVERLAY)
        }
    }

    // handle when user click predictions
    private val placeSelectionListener = object : PlaceSelectionListener {
        override fun onError(status: Status) {
            Log.e(TAG, "onError: $status")
        }

        override fun onPlaceSelected(place: Place) {
            val addressNameComponent = place.addressComponents.asList()
            val address = place.name
            val latLng = place.latLng
            val kecamatan = addressNameComponent[2].name

            latLng?.let {
                moveCameraMaps(it, ZOOM_ROAD)
            }

            // set ke viewmodel alamat
            when (codeForm) {
                FORM_PENGIRIM -> {
                    viewModel.setFormAsalPengirim(address.toString())
                }
                FORM_PENERIMA -> {
                    viewModel.setFormAsalPenerima(address.toString())
                }
            }

            Log.d(
                TAG,
                "onPlaceSelected:\nNama: $address\nAlamat: $address\nKoordinat: $latLng"
            )
        }
    }

    override fun onPause() {
        super.onPause()
        // jika pengguna berpindah ke aplikasi lain hapus update lokasi sebelumnya
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            fusedLocationProvider?.removeLocationUpdates(locationCallback)
        }
    }

    private fun checkLocationPermission() {
        when {
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED -> {
                Log.d(TAG, "checkLocationPermission: Izin lokasi latar depan diberikan ")
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder(requireContext())
                    .setTitle("Perizinan Lokasi Dibutuhkan")
                    .setMessage("Agar fungsi aplikasi ini bekerja maksimal, izinkan aplikasi untuk dapat mengakses lokasi")
                    .setPositiveButton(
                        "Ya"
                    ) { _, _ ->
                        //Prompt the user once explanation has been shown
                        PermissionsApp.requestLocationPermission(
                            requireActivity(),
                            PermissionCode.REQUEST_LOCATION
                        )
                    }
                    .setNegativeButton("Tidak") { dialogInterface, _ ->
                        dialogInterface.dismiss()
                    }
                    .create()
                    .show()
            }
            else -> {
                // No explanation needed, we can request the permission.
                // aplikasi meminta perizinan pertama kali
                PermissionsApp.requestLocationPermission(
                    requireActivity(),
                    PermissionCode.REQUEST_LOCATION
                )
            }

        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        gMap = googleMap

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(DEFAULT_POINT_MAP, DEFAULT_ZOOM))

        googleMap.isTrafficEnabled = true
        googleMap.isBuildingsEnabled = true
        googleMap.uiSettings.apply {
            isCompassEnabled = true
            isMapToolbarEnabled = false
        }

        googleMap.setOnMapClickListener { latLng ->
            myLocationMarker?.remove()
            addMarker(latLng)
            moveCameraMaps(latLng, ZOOM_ROAD)
        }

        googleMap.setOnCameraIdleListener {
            val locationBias = RectangularBounds.newInstance(
                googleMap.projection.visibleRegion.latLngBounds
            )

            autocompleteSupportFragment.setLocationBias(locationBias)

            Log.d(TAG, "onMapReady: $locationBias")
        }

        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), "AIzaSyAFq1pVSgNiQOdRnoK7OmZG62EqVrr4lHI")
        }

        Places.createClient(requireContext())

        setupAutocompleteSupportFragment()
    }

    private fun addMarker(latLng: LatLng) {
        myLocationMarker = gMap?.addMarker(
            MarkerOptions().position(
                latLng
            )
        )
    }

    private fun moveCameraMaps(latLng: LatLng, zoomMarker: Float) {
        gMap?.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                latLng,
                zoomMarker
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}