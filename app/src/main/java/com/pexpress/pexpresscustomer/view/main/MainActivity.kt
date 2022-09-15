package com.pexpress.pexpresscustomer.view.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.android.material.button.MaterialButton
import com.pexpress.pexpresscustomer.R
import com.pexpress.pexpresscustomer.databinding.ActivityMainBinding
import com.pexpress.pexpresscustomer.utils.setVisibilityBottomHead
import com.pexpress.pexpresscustomer.view.inAppUpdate.InAppUpdate

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var inAppUpdate: InAppUpdate

    companion object {
        private const val DAYS_FOR_FLEXIBLE_UPDATE = 14
        private const val MY_REQUEST_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        inAppUpdate = InAppUpdate(this)

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

//    fun checkUpdateApp() {
//        appUpdateManager = AppUpdateManagerFactory.create(this)
//        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
//        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
//            // cek apakah ada update
//            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
//                // jika ada update yang sudah melewati 7 hari maka update full
//                if ((appUpdateInfo.clientVersionStalenessDays() ?: -1) >= DAYS_FOR_FLEXIBLE_UPDATE
//                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
//                ) {
//                    // Request the update.
//                } else { // jika belum 7 hari
//                    when {
//                        appUpdateInfo.updatePriority() >= 4
//                                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE) -> { // prioritas tinggi
//                            // Request the update.
//                        }
//                        appUpdateInfo.updatePriority() <= 4
//                                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE) -> { // prioritas normal
//                            // Request the update.
//                        }
//                    }
//                }
//            }
//        }
//    }

    override fun onResume() {
        super.onResume()
        inAppUpdate.onResume()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        inAppUpdate.onActivityResult(requestCode, resultCode, data)
    }

//    fun updateApp(appUpdateInfo: AppUpdateInfo) {
//        appUpdateManager.startUpdateFlowForResult(
//            // Pass the intent that is returned by 'getAppUpdateInfo()'.
//            appUpdateInfo,
//            // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
//            AppUpdateType.IMMEDIATE,
//            // The current activity making the update request.
//            this,
//            // Include a request code to later monitor this update request.
//            MY_REQUEST_CODE)
//
//    }

    override fun onDestroy() {
        super.onDestroy()
        inAppUpdate.onDestroy()
    }
}