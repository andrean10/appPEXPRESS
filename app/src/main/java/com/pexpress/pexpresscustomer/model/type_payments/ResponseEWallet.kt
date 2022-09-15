package com.pexpress.pexpresscustomer.model.type_payments

import com.google.gson.annotations.SerializedName

data class ResponseEWallet(

    @field:SerializedName("data")
    val data: List<ResultEWallet>? = null,

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class ResultEWallet(

    @field:SerializedName("usermodify")
    val usermodify: Any? = null,

    @field:SerializedName("nama")
    val nama: String? = null,

    @field:SerializedName("tglcreate")
    val tglcreate: Any? = null,

    @field:SerializedName("tglmodify")
    val tglmodify: String? = null,

    @field:SerializedName("usercreate")
    val usercreate: Any? = null,

    @field:SerializedName("namafile")
    val namafile: String? = null,

    @field:SerializedName("isactive")
    val isactive: Int? = null,

    @field:SerializedName("channel")
    val channel: String? = null,

    @field:SerializedName("id")
    val id: Int? = null
)
