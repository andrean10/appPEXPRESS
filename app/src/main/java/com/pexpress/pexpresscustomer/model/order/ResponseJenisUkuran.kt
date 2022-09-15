package com.pexpress.pexpresscustomer.model.order

import com.google.gson.annotations.SerializedName

data class ResponseJenisUkuran(

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("detail")
    val detail: List<ResultJenisUkuran>? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class ResultJenisUkuran(

    @field:SerializedName("jenisukuran")
    val jenisukuran: String? = null,

    @field:SerializedName("maksimalpanjang")
    val maksimalpanjang: String? = null,

    @field:SerializedName("maksimallebar")
    val maksimallebar: String? = null,

    @field:SerializedName("isactive")
    val isactive: Int? = null,

    @field:SerializedName("maksimaltinggi")
    val maksimaltinggi: String? = null,

    @field:SerializedName("maksimalberat")
    val maksimalberat: String? = null,

    @field:SerializedName("usermodify")
    val usermodify: Any? = null,

    @field:SerializedName("tglcreate")
    val tglcreate: String? = null,

    @field:SerializedName("tglmodify")
    val tglmodify: String? = null,

    @field:SerializedName("usercreate")
    val usercreate: String? = null,

    @field:SerializedName("kodejenisukuran")
    val kodejenisukuran: String? = null,

    @field:SerializedName("idjenisukuran")
    val idjenisukuran: Int? = null,

    @field:SerializedName("satuanpanjang")
    val satuanpanjang: String? = null
)
