package com.pexpress.pexpresscustomer.db.payments

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class QRPayment(
    val noInvoice: String,
    val name: String,
    val totalPayment: String
) : Parcelable
