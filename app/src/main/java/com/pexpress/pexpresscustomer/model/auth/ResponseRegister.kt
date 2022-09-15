package com.pexpress.pexpresscustomer.model.auth

import com.google.gson.annotations.SerializedName

data class ResponseRegister(

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("detail")
    val detail: List<Any?>? = null,

    @field:SerializedName("message")
    val message: String? = null
)
