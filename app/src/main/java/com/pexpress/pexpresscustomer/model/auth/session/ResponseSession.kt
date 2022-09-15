package com.pexpress.pexpresscustomer.model.auth.session

import com.google.gson.annotations.SerializedName

data class ResponseSession(

    @field:SerializedName("success")
    val success: Boolean,

    @field:SerializedName("detail")
    val detail: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)
