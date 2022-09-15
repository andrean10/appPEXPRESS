package com.pexpress.pexpresscustomer.model.type_payments

import com.google.gson.annotations.SerializedName

data class ResponseCreateCash(

    @field:SerializedName("data")
    val data: Boolean? = null,

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)
