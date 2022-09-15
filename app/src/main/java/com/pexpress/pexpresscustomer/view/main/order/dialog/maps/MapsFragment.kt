package com.pexpress.pexpresscustomer.view.main.order.dialog.maps

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.pexpress.pexpresscustomer.R
import com.pexpress.pexpresscustomer.databinding.FragmentMapsBinding
import com.pexpress.pexpresscustomer.utils.UtilsCode
import com.pexpress.pexpresscustomer.utils.showMessage
import com.pexpress.pexpresscustomer.view.main.order.viewmodel.OrderPaketViewModel
import pub.devrel.easypermissions.AppSettingsDialog
import www.sanju.motiontoast.MotionToast

class MapsFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<OrderPaketViewModel>()

    private var idForm = 0
    private var gMap: GoogleMap? = null
    private var myLocationMarker: Marker? = null
    private var isLocationPermissionGranted = false
    private var latLocation = ""
    private var longLocation = ""

    private lateinit var mPermissionLauncher: ActivityResultLauncher<String>

    private val DEFAULT_POINT_MAP = LatLng(-6.2693499, 106.9590946)
    private var pointMapAutoComplete: LatLng? = null
    private val DEFAULT_ZOOM = 5f
    private val ZOOM_ROAD = 16f

    companion object {
        val TAG = MapsFragment::class.simpleName
    }

    private val locationRequest = LocationRequest.create().apply {
        interval = 5000
        fastestInterval = 3000
        priority = Priority.PRIORITY_HIGH_ACCURACY
        maxWaitTime = 60
    }

    private val callback = OnMapReadyCallback { googleMap ->
        gMap = googleMap

        if (pointMapAutoComplete != null) {
            googleMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    pointMapAutoComplete!!,
                    ZOOM_ROAD
                )
            )
        } else {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(DEFAULT_POINT_MAP, DEFAULT_ZOOM))
        }

        googleMap.isTrafficEnabled = true
        googleMap.isBuildingsEnabled = true

        googleMap.setOnMapClickListener { latLng ->
            myLocationMarker?.remove()
            addMarker(latLng)
        }

//        googleMap.setOnCameraIdleListener {
//            val locationBias = RectangularBounds.newInstance(
//                googleMap.projection.visibleRegion.latLngBounds
//            )
//        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    isLocationPermissionGranted = true
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            requireActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                    ) { // disable permission tidak permanen
                        AlertDialog.Builder(requireContext())
                            .setTitle("Perizinan Lokasi Dibutuhkan")
                            .setMessage("Agar fungsi aplikasi ini bekerja maksimal, izinkan aplikasi untuk dapat mengakses lokasi")
                            .setPositiveButton(
                                "Ya"
                            ) { _, _ ->
                                mPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                            }
                            .setNegativeButton("Tidak") { dialogInterface, _ ->
                                dialogInterface.dismiss()
                            }
                            .create()
                            .show()
                    } else { // permanen
                        Log.d(
                            TAG,
                            "onRequestPermissionsResult: Dijalankan setting"
                        )
                        AppSettingsDialog.Builder(this).build().show()
                    }
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        checkLocationPermission()
        initView()

        with(binding) {
            btnClose.setOnClickListener { dialog?.dismiss() }
            fabMyLocation.setOnClickListener { checkMyLocation() }
            fabSave.setOnClickListener { saveLocation() }
        }
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
    }

    @SuppressLint("MissingPermission")
    private fun checkMyLocation() {
        // cek permission
        Log.d(TAG, "checkMyLocation: $isLocationPermissionGranted")
        if (isLocationPermissionGranted) {
            if (isGPSEnabled()) {
                LocationServices.getFusedLocationProviderClient(requireContext())
                    .requestLocationUpdates(locationRequest, object : LocationCallback() {
                        override fun onLocationResult(locationResult: LocationResult) {
                            super.onLocationResult(locationResult)
                            LocationServices.getFusedLocationProviderClient(requireContext())
                                .removeLocationUpdates(this)

                            if (locationResult.locations.size > 0) {
                                val index = locationResult.locations.size - 1
                                val latitude = locationResult.locations[index].latitude
                                val longitude = locationResult.locations[index].longitude

                                val myLocationLatLng = LatLng(latitude, longitude)
                                myLocationMarker?.remove()
                                addMarker(myLocationLatLng)
                            }
                        }
                    }, Looper.getMainLooper())
            } else {
                turnOnGPS()
            }
        } else {
            checkLocationPermission()
        }
    }

    private fun turnOnGPS() {
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .setAlwaysShow(true)

        val locationSettingResponse = LocationServices.getSettingsClient(requireActivity())
            .checkLocationSettings(builder.build())

        locationSettingResponse.addOnCompleteListener { task ->
            if (task.isComplete) {
                try {
                    val response = task.getResult(ApiException::class.java)
                    Log.d(TAG, "checkGPSSetting: GPS Aktif!")
                } catch (e: ApiException) {
                    if (e.statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                        val resolvableApiException = e as ResolvableApiException
                        try {
                            resolvableApiException.startResolutionForResult(
                                requireActivity(),
                                2
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
    }

    private fun isGPSEnabled(): Boolean {
        var locationManager: LocationManager? = null
        if (locationManager == null) {
            locationManager =
                requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        }
        return locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun checkLocationPermission() {
        when (PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                Log.d(
                    TAG,
                    "checkLocationPermission: Izin lokasi latar depan diberikan "
                )
                isLocationPermissionGranted = true
            }
            else -> {
                // aplikasi meminta perizinan pertama kali
                mPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    private fun addMarker(latLng: LatLng) {
        myLocationMarker = gMap?.addMarker(
            MarkerOptions().position(
                latLng
            )
        )

        latLocation = latLng.latitude.toString()
        longLocation = latLng.longitude.toString()

        moveCameraMaps(latLng, ZOOM_ROAD)
        binding.fabSave.isEnabled = true
    }

    private fun moveCameraMaps(latLng: LatLng, zoomMarker: Float) {
        gMap?.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                latLng,
                zoomMarker
            )
        )
    }

    private fun saveLocation() {
        when (idForm) {
            UtilsCode.FORM_PENGIRIM -> {
                val params = hashMapOf(
                    "latpengirim" to latLocation,
                    "longpengirim" to longLocation
                )
                Log.d(
                    TAG,
                    "saveLocation: LatLongPengirim = ${
                        LatLng(
                            latLocation.toDouble(),
                            longLocation.toDouble()
                        )
                    }"
                )
                viewModel.setFormLatLongPengirim(params)
            }
            UtilsCode.FORM_PENERIMA -> {
                val params = hashMapOf(
                    "latpenerima" to latLocation,
                    "longpenerima" to longLocation
                )
                viewModel.setFormLatLongPenerima(params)
            }
        }

        dialog?.dismiss()
    }

    fun setDataMaps(stateForm: Int, latLng: LatLng?) {
        idForm = stateForm
        pointMapAutoComplete = latLng
        Log.d(TAG, "setDataMaps: Form = $stateForm")
        Log.d(TAG, "setDataMaps: Latlng = $latLng")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}