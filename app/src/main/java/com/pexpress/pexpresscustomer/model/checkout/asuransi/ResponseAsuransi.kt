package com.pexpress.pexpresscustomer.model.checkout.asuransi

import com.google.gson.annotations.SerializedName

data class ResponseAsuransi(

    @field:SerializedName("data")
    val data: List<ResultAsuransi?>? = null,

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class ResultAsuransi(

    @field:SerializedName("namaasuransi")
    val namaasuransi: String? = null,

    @field:SerializedName("usermodify")
    val usermodify: Any? = null,

    @field:SerializedName("kodeasuransi")
    val kodeasuransi: String? = null,

    @field:SerializedName("tglcreate")
    val tglcreate: String? = null,

    @field:SerializedName("tglmodify")
    val tglmodify: String? = null,

    @field:SerializedName("usercreate")
    val usercreate: String? = null,

    @field:SerializedName("isactive")
    val isactive: Int? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("value")
    val value: Int? = null
)
