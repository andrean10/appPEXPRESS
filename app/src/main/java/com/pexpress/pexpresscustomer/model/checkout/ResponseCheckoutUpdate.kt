package com.pexpress.pexpresscustomer.model.checkout

import com.google.gson.annotations.SerializedName

data class ResponseCheckoutUpdate(

    @field:SerializedName("data")
    val data: ResultCheckoutUpdate? = null,

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class ResultCheckoutUpdate(

    @field:SerializedName("nomorpengiriman")
    val nomorpengiriman: String? = null,

    @field:SerializedName("biaya")
    val biaya: String? = null
)
