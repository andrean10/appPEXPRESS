package com.pexpress.pexpresscustomer.model.auth

import com.google.gson.annotations.SerializedName

data class ResponseOTP(

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("detail")
    val detail: Int? = null,

    @field:SerializedName("message")
    val message: String? = null
)
