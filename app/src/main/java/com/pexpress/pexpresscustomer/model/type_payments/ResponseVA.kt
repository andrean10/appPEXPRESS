package com.pexpress.pexpresscustomer.model.type_payments

import com.google.gson.annotations.SerializedName

data class ResponseVA(

    @field:SerializedName("data")
    val data: List<ResultVA>? = null,

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class ResultVA(

    @field:SerializedName("code")
    val code: String? = null,

    @field:SerializedName("is_activated")
    val isActivated: Int? = null,

    @field:SerializedName("namafile")
    val namafile: String? = null,

    @field:SerializedName("name")
    val name: String? = null
)
