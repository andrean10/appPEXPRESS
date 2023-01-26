package com.pexpress.pexpresscustomer.model.checkout.hari_libur

import com.google.gson.annotations.SerializedName

data class ResponseCheckHariLibur(

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: Boolean? = null
)
