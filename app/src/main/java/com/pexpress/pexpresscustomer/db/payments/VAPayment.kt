package com.pexpress.pexpresscustomer.db.payments

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VAPayment(
    val noInvoice: String,
    val va: String,
    val bank: String,
    val name: String,
    var totalPayment: String,
    var expiredTime: String? = null,
) : Parcelable
