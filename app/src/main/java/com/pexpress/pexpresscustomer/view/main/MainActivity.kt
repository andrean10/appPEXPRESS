package com.pexpress.pexpresscustomer.view.main

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.pexpress.pexpresscustomer.R
import com.pexpress.pexpresscustomer.databinding.ActivityMainBinding
import com.pexpress.pexpresscustomer.db.User
import com.pexpress.pexpresscustomer.session.UserPreference
import com.pexpress.pexpresscustomer.utils.UtilsCode.TAG
import com.pexpress.pexpresscustomer.utils.setVisibilityBottomHead
import com.pexpress.pexpresscustomer.view.auth.AuthActivity
import com.pexpress.pexpresscustomer.view.inAppUpdate.InAppUpdate
import com.pexpress.pexpresscustomer.view.main.home.viewmodel.HomeViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var inAppUpdate: InAppUpdate
    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var userPreference: UserPreference
    private var isFromAuth = false

    private lateinit var appUpdateManager: AppUpdateManager

    companion object {
        private const val DAYS_FOR_FLEXIBLE_UPDATE = 14
        private const val MY_REQUEST_CODE = 100
        const val EXTRA_IS_FROM_AUTH = "extra_is_from_auth"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkUpdateApp()
        inAppUpdate = InAppUpdate(this)

        isFromAuth = intent.getBooleanExtra(EXTRA_IS_FROM_AUTH, false)
        if (isFromAuth) setupView() else checkAuthentication()
    }

    private fun checkAuthentication() {
        userPreference = UserPreference(this)
        getDataProfile()
    }

    private fun setupView() {
        navController = findNavController(R.id.nav_host_fragment_activity_main)
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
                    stateButton(binding.btnTracking)
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
            btnTracking.setOnClickListener { moveToTracking() }
            btnAccount.setOnClickListener { moveToAccount() }
        }

        isFromAuth = false
    }

    private fun getDataProfile() {
        val user = userPreference.getUser()
        viewModel.getProfile(user.numberPhone.toString()).observe(this) { response ->
            if (response != null) {
                if (response.success!!) {
                    val deviceId = response.detail?.get(0)?.deviceId ?: ""
                    Log.d(TAG, "getDataProfile: ${user.deviceId}")
                    if (deviceId.isNotEmpty()) {
                        if (deviceId == user.deviceId) {
                            setupView()
                        } else {
                            showPopUp(
                                "Sesi Berakhir",
                                "Maaf anda telah dikeluarkan karena login di device lain."
                            ) {
                                moveToAuth()
                            }
                        }
                    } else {
                        setupView()
                    }
                } else {
                    moveToAuth()
                }
            }
        }
    }

    private fun workManager() {

    }

    private fun moveToAuth() {
        userPreference.removeLogin()
        userPreference.removeUser()
        startActivity(Intent(this, AuthActivity::class.java))
        finish()
    }

    private fun moveToHome() {
        navController.navigate(R.id.navigation_home)
    }

    private fun moveToPayment() {
        navController.navigate(R.id.navigation_status_pembayaran)
    }

    private fun moveToTracking() {
        navController.navigate(R.id.navigation_tracking)
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
                    btnTracking.setBackgroundColor(
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
                    btnTracking.setBackgroundColor(
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
                btnTracking -> {
                    btnTracking.setBackgroundColor(
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
                    btnTracking.setBackgroundColor(
                        ContextCompat.getColor(
                            this@MainActivity,
                            R.color.transparentColor
                        )
                    )
                }
            }
        }
    }

    fun checkUpdateApp() {
        appUpdateManager = AppUpdateManagerFactory.create(this)
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            // cek apakah ada update
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                // jika ada update yang sudah melewati 7 hari maka update full
                if ((appUpdateInfo.clientVersionStalenessDays() ?: -1) >= DAYS_FOR_FLEXIBLE_UPDATE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
                ) {
                    // Request the update.
                    showPopUp("Versi Terbaru!", "Update aplikasi tersedia untuk versi terbaru.") {
                        moveToPlaystore()
                    }
                } else { // jika belum 7 hari
                    when {
                        appUpdateInfo.updatePriority() >= 4
                                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE) -> { // prioritas tinggi
                            // Request the update.
                            showPopUp(
                                "Versi Terbaru!",
                                "Update aplikasi tersedia untuk versi terbaru."
                            ) {
                                moveToPlaystore()
                            }
                        }
                        appUpdateInfo.updatePriority() <= 4
                                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE) -> { // prioritas normal
                            // Request the update.
                            showPopUp(
                                "Versi Terbaru!",
                                "Update aplikasi tersedia untuk versi terbaru."
                            ) {
                                moveToPlaystore()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        inAppUpdate.onResume()

        if (isFromAuth) setupView() else checkAuthentication()
    }

    override fun onPause() {
        super.onPause()
        if (isFromAuth) setupView() else checkAuthentication()
    }

    private fun showPopUp(title: String, message: String, actions: () -> Unit) {
        AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton("Oke") { dialogInterface, _ ->
                actions()
                dialogInterface.dismiss()
                Log.d(TAG, "showPopUp: positive button = Dikeluarkan")
            }
            setOnDismissListener {
                Log.d(TAG, "showPopUp: dismiss area windows = Dikeluarkan")
                moveToAuth()
            }
            show()
        }
    }

    private fun moveToPlaystore() {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                )
            )
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        inAppUpdate.onActivityResult(requestCode, resultCode, data)
    }

    fun updateApp(appUpdateInfo: AppUpdateInfo) {
        appUpdateManager.startUpdateFlowForResult(
            // Pass the intent that is returned by 'getAppUpdateInfo()'.
            appUpdateInfo,
            // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
            AppUpdateType.IMMEDIATE,
            // The current activity making the update request.
            this,
            // Include a request code to later monitor this update request.
            MY_REQUEST_CODE
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        inAppUpdate.onDestroy()
    }
}