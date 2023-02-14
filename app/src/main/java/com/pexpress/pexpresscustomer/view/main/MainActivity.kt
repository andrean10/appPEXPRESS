package com.pexpress.pexpresscustomer.view.main

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.pexpress.pexpresscustomer.R
import com.pexpress.pexpresscustomer.databinding.ActivityMainBinding
import com.pexpress.pexpresscustomer.session.UserPreference
import com.pexpress.pexpresscustomer.utils.UtilsCode.TAG
import com.pexpress.pexpresscustomer.utils.setVisibilityBottomHead
import com.pexpress.pexpresscustomer.utils.showMessage
import com.pexpress.pexpresscustomer.view.auth.AuthActivity
import com.pexpress.pexpresscustomer.view.main.home.viewmodel.HomeViewModel
import www.sanju.motiontoast.MotionToast

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val viewModel by viewModels<HomeViewModel>()

    private lateinit var appUpdateManager: AppUpdateManager
    private lateinit var userPreference: UserPreference
    private lateinit var mHandler: Handler

    private var isShowPopUp = false

    companion object {
        //                private const val DAYS_FOR_FLEXIBLE_UPDATE = 3
        private const val MY_REQUEST_CODE = 100
        const val EXTRA_IS_FROM_AUTH = "extra_is_from_auth"
        private const val DEFAULT_TIMER_REALTIME = 8000L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        setupView()
    }

    private fun init() {
        navController = findNavController(R.id.nav_host_fragment_activity_main)
        appUpdateManager = AppUpdateManagerFactory.create(this)
        appUpdateManager.registerListener(listener)
        userPreference = UserPreference(this)
        mHandler = Handler(Looper.getMainLooper())
    }

    private fun checkAuthentication() {
        viewModel.myProfile(userPreference.getUser().numberPhone.toString())
        observeProfile()
    }

    private fun setupView() {
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.navigation_home -> {
                    stateButton(binding.btnHome)
                    setVisibilityBottomHead(this, true)
                }
                R.id.navigation_status_pembayaran -> {
                    stateButton(binding.btnPayment)
                    setVisibilityBottomHead(this, true)
                }
                R.id.navigation_tracking -> {
                    stateButton(binding.btnPesanan)
                    setVisibilityBottomHead(this, false)
                }
                R.id.navigation_account -> {
                    stateButton(binding.btnAccount)
                    setVisibilityBottomHead(this, true)
                }
            }
        }

        with(binding) {
            btnHome.setOnClickListener { moveToHome() }
            btnPayment.setOnClickListener { moveToPayment() }
            btnPesanan.setOnClickListener { moveToHistory() }
            btnAccount.setOnClickListener { moveToAccount() }
        }

        checkUpdate()
        checkAuthentication()
    }

    private fun observeProfile() {
        viewModel.profile.observe(this) { response ->
            if (response != null) {
                if (response.success!!) {
                    val deviceId = response.detail?.get(0)?.deviceId
                    if (deviceId != null) {
                        if (deviceId != userPreference.getDeviceId()) {
                            if (!isShowPopUp) {
                                showPopUp(
                                    "Sesi Berakhir",
                                    "Maaf anda telah dikeluarkan karena login di device lain."
                                ) {
                                    // null
                                }
                                isShowPopUp = true
                            }
                        }
                    }
                } else {
                    showMessage(
                        this,
                        getString(R.string.failed_title),
                        getString(R.string.failed_description),
                        MotionToast.TOAST_ERROR
                    )
                }
            }
        }
    }

    private fun checkUpdate() {
        // Returns an intent object that you use to check for an update.
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        // Checks that the platform will allow the specified type of update.
        Log.d(TAG, "Checking for updates")
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
            ) {
                // Request the update.
                Log.d(TAG, "Update available")
                startForInAppUpdate(appUpdateInfo)
            } else {
                Log.d(TAG, "No Update available")
            }
        }
    }

    private fun startForInAppUpdate(appUpdateInfo: AppUpdateInfo) {
        appUpdateManager.startUpdateFlowForResult(
            // Pass the intent that is returned by 'getAppUpdateInfo()'.
            appUpdateInfo,
            // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
            AppUpdateType.FLEXIBLE,
            // The current activity making the update request.
            this,
            // Include a request code to later monitor this update request.
            MY_REQUEST_CODE
        )
    }

    private val listener = InstallStateUpdatedListener { installState ->
        if (installState.installStatus() == InstallStatus.DOWNLOADED) {
            // After the update is downloaded, show a notification
            // and request user confirmation to restart the app.
            Log.d(TAG, "An update has been downloaded")
            showSnackBarForCompleteUpdate()
        }
    }

    private fun showSnackBarForCompleteUpdate() {
        val snackbar = Snackbar.make(
            binding.container,
            "Pembaruan telah selesai di unduh",
            Snackbar.LENGTH_INDEFINITE
        ).apply {
            setActionTextColor(getColor(R.color.primaryColor))
            setAction("Mulai Ulang") {
                appUpdateManager.completeUpdate()
            }
        }.show()
    }

    private val mRunnable = object : Runnable {
        override fun run() {
            viewModel.myProfile(userPreference.getUser().numberPhone.toString())
            mHandler.postDelayed(this, DEFAULT_TIMER_REALTIME)
        }
    }

    private fun moveToAuth() {
        userPreference.removeLogin()
        userPreference.removeUser()
        userPreference.removeDeviceId()
        startActivity(Intent(this, AuthActivity::class.java))
        finish()
    }

    private fun moveToHome() {
        navController.navigate(R.id.navigation_home)
    }

    private fun moveToPayment() {
        navController.navigate(R.id.navigation_status_pembayaran)
    }

    private fun moveToHistory() {
        navController.navigate(R.id.fragment_history)
    }

    private fun moveToAccount() {
        navController.navigate(R.id.navigation_account)
    }

    private fun stateButton(btnView: MaterialButton) {
        with(binding) {
            when (btnView) {
                btnHome -> {
                    btnHome.setBackgroundColor(
                        ContextCompat.getColor(
                            this@MainActivity,
                            R.color.bg_button_header_item_menu
                        )
                    )
                    btnPayment.setBackgroundColor(
                        ContextCompat.getColor(
                            this@MainActivity,
                            R.color.transparentColor
                        )
                    )
                    btnPesanan.setBackgroundColor(
                        ContextCompat.getColor(
                            this@MainActivity,
                            R.color.transparentColor
                        )
                    )
                    btnAccount.setBackgroundColor(
                        ContextCompat.getColor(
                            this@MainActivity,
                            R.color.transparentColor
                        )
                    )
                }
                btnPayment -> {
                    btnPayment.setBackgroundColor(
                        ContextCompat.getColor(
                            this@MainActivity,
                            R.color.bg_button_header_item_menu
                        )
                    )
                    btnHome.setBackgroundColor(
                        ContextCompat.getColor(
                            this@MainActivity,
                            R.color.transparentColor
                        )
                    )
                    btnPesanan.setBackgroundColor(
                        ContextCompat.getColor(
                            this@MainActivity,
                            R.color.transparentColor
                        )
                    )
                    btnAccount.setBackgroundColor(
                        ContextCompat.getColor(
                            this@MainActivity,
                            R.color.transparentColor
                        )
                    )
                }
                btnPesanan -> {
                    btnPesanan.setBackgroundColor(
                        ContextCompat.getColor(
                            this@MainActivity,
                            R.color.bg_button_header_item_menu
                        )
                    )
                    btnHome.setBackgroundColor(
                        ContextCompat.getColor(
                            this@MainActivity,
                            R.color.transparentColor
                        )
                    )
                    btnPayment.setBackgroundColor(
                        ContextCompat.getColor(
                            this@MainActivity,
                            R.color.transparentColor
                        )
                    )
                    btnAccount.setBackgroundColor(
                        ContextCompat.getColor(
                            this@MainActivity,
                            R.color.transparentColor
                        )
                    )
                }
                btnAccount -> {
                    btnAccount.setBackgroundColor(
                        ContextCompat.getColor(
                            this@MainActivity,
                            R.color.bg_button_header_item_menu
                        )
                    )
                    btnHome.setBackgroundColor(
                        ContextCompat.getColor(
                            this@MainActivity,
                            R.color.transparentColor
                        )
                    )
                    btnPayment.setBackgroundColor(
                        ContextCompat.getColor(
                            this@MainActivity,
                            R.color.transparentColor
                        )
                    )
                    btnPesanan.setBackgroundColor(
                        ContextCompat.getColor(
                            this@MainActivity,
                            R.color.transparentColor
                        )
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mHandler.post(mRunnable)
    }

    override fun onPause() {
        super.onPause()
        mHandler.removeCallbacks(mRunnable)
    }

    private fun showPopUp(title: String, message: String, action: () -> Unit) {
        AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton("Oke") { dialogInterface, _ ->
                action()
                dialogInterface.dismiss()
            }
            setOnDismissListener {
                moveToAuth()
            }
            show()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        appUpdateManager.unregisterListener(listener)
    }

    override fun onDestroy() {
        super.onDestroy()
        appUpdateManager.unregisterListener(listener)
    }

    //    private fun checkUpdateApp() {
//        appUpdateManager = AppUpdateManagerFactory.create(this)
//        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
//        Log.d(TAG, "checkUpdateApp: AppUpdateInfoTask $appUpdateInfoTask")
//        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
//            Log.d(TAG, "checkUpdateApp: $appUpdateInfo")
//            // cek apakah ada update
//            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
//                // jika ada update yang sudah melewati 7 hari maka update full
//                if ((appUpdateInfo.clientVersionStalenessDays() ?: -1) >= DAYS_FOR_FLEXIBLE_UPDATE
//                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
//                ) {
//                    // Request the update.
//                    showPopUp("Versi Terbaru!", "Update aplikasi tersedia untuk versi terbaru.") {
//                        moveToPlaystore()
//                    }
//                } else { // jika belum 7 hari
//                    when {
//                        appUpdateInfo.updatePriority() >= 4
//                                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE) -> { // prioritas tinggi
//                            // Request the update.
//                            showPopUp(
//                                "Versi Terbaru!",
//                                "Update aplikasi tersedia untuk versi terbaru."
//                            ) {
//                                moveToPlaystore()
//                            }
//                        }
//                        appUpdateInfo.updatePriority() <= 4
//                                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE) -> { // prioritas normal
//                            // Request the update.
//                            showPopUp(
//                                "Versi Terbaru!",
//                                "Update aplikasi tersedia untuk versi terbaru."
//                            ) {
//                                moveToPlaystore()
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }

//    private fun moveToPlaystore() {
//        try {
//            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
//        } catch (e: ActivityNotFoundException) {
//            startActivity(
//                Intent(
//                    Intent.ACTION_VIEW,
//                    Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
//                )
//            )
//        }
//    }
}