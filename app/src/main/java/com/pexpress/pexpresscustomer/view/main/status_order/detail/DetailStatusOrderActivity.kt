package com.pexpress.pexpresscustomer.view.main.status_order.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pexpress.pexpresscustomer.R

class DetailStatusOrderActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_DATA = "extra_data"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_status_order)
    }
}