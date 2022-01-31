package com.pexpress.pexpresscustomer.view.splash.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.pexpress.pexpresscustomer.R
import com.pexpress.pexpresscustomer.databinding.ActivityOnBoardingBinding
import com.pexpress.pexpresscustomer.db.ScreenItem
import com.pexpress.pexpresscustomer.session.UserPreference
import com.pexpress.pexpresscustomer.view.auth.AuthActivity
import com.pexpress.pexpresscustomer.view.splash.onboarding.adapter.OnBoardingAdapter

class OnBoardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnBoardingBinding
    private lateinit var onBoardingAdapter: OnBoardingAdapter

    private val TAG = OnBoardingActivity::class.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupOnBoardingItems()
        with(binding) {
            viewPager.adapter = onBoardingAdapter

            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    setCurrentOnBoardingIndicator(position)
                }
            })
        }

        setupOnBoardingIndicators()
        setCurrentOnBoardingIndicator(0)
    }

    private fun setupOnBoardingItems() {
        val mList = listOf(
            ScreenItem("Title 1", getString(R.string.lorem_ipsum), R.drawable.img_onboarding),
            ScreenItem("Title 2", getString(R.string.lorem_ipsum), R.drawable.img_onboarding2),
            ScreenItem("Title 3", getString(R.string.lorem_ipsum), R.drawable.img_onboarding3),
            ScreenItem("Title 4", getString(R.string.lorem_ipsum), R.drawable.img_onboarding4)
        )
        onBoardingAdapter = OnBoardingAdapter(mList)
    }

    private fun setupOnBoardingIndicators() {
        val indicators = arrayOfNulls<ImageView>(onBoardingAdapter.itemCount)
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(8, 0, 8, 0)
        indicators.forEachIndexed { i, _ ->
            indicators[i] = ImageView(applicationContext)
            indicators[i]?.setImageDrawable(
                ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.onboarding_indicator_inactive
                )
            )
            indicators[i]?.layoutParams = layoutParams
            binding.layoutOnboardingIndicators.addView(indicators[i])
        }
    }

    private fun setCurrentOnBoardingIndicator(position: Int) {
        with(binding) {
            val childCount = layoutOnboardingIndicators.childCount
            for (i in 0..childCount) {
                val imageView = layoutOnboardingIndicators.getChildAt(i) as ImageView?
                if (i == position) {
                    imageView?.setImageDrawable(
                        ContextCompat.getDrawable(
                            applicationContext,
                            R.drawable.onboarding_indicator_active
                        )
                    )

                    if (childCount - 1 == position) {
                        btnGetStarted.visibility = VISIBLE
                        btnGetStarted.setOnClickListener { moveToAuth() }
                    } else {
                        btnGetStarted.visibility = INVISIBLE
                    }
                } else {
                    imageView?.setImageDrawable(
                        ContextCompat.getDrawable(
                            applicationContext,
                            R.drawable.onboarding_indicator_inactive
                        )
                    )
                }
            }
        }
    }

    private fun moveToAuth() {
        // set user preferences
        val userPreference = UserPreference(this)
        userPreference.setLaunchApp()

        startActivity(Intent(this, AuthActivity::class.java))
        finish()
    }
}