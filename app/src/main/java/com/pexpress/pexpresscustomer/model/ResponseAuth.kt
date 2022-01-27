package com.pexpress.pexpresscustomer.model

import com.google.gson.annotations.SerializedName

data class ResponseAuth(

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("detail")
    val detail: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)
