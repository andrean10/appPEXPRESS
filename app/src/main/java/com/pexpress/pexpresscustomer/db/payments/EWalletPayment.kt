package com.pexpress.pexpresscustomer.db.payments

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EWalletPayment(
    val noInvoice: String,
    val channel: String,
    val nameEwallet: String,
    val name: String,
    val mobileNumber: String,
    val totalPayment: String,
    val icon: String
) : Parcelable
