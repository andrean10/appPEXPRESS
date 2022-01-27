package com.pexpress.pexpresscustomer.view.splash.first_screen

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.pexpress.pexpresscustomer.R
import com.pexpress.pexpresscustomer.session.UserPreference
import com.pexpress.pexpresscustomer.utils.UtilsCode.TIME_DELAY_SCREEN
import com.pexpress.pexpresscustomer.view.auth.AuthActivity
import com.pexpress.pexpresscustomer.view.main.MainActivity
import com.pexpress.pexpresscustomer.view.splash.onboarding.OnBoardingActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    private val activityScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //hiding title bar of this activity
        window.requestFeature(Window.FEATURE_NO_TITLE)
        //making this activity full screen
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_splash)

        activityScope.launch {
            delay(TIME_DELAY_SCREEN)

            // check session auth
            val userPreference = UserPreference(this@SplashActivity)
            if (userPreference.getLaunchApp().isLaunchFirstApp) {
                startActivity(Intent(this@SplashActivity, OnBoardingActivity::class.java))
                finish()
            } else {
                if (userPreference.getLogin().isLoginValid) {
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    finish()
                } else {
                    startActivity(Intent(this@SplashActivity, AuthActivity::class.java))
                    finish()
                }
            }
        }
    }
}