package com.pexpress.pexpresscustomer.model.order

import com.google.gson.annotations.SerializedName

data class ResponseJenisBarang(

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("detail")
    val detail: List<ResultJenisBarang>? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class ResultJenisBarang(

    @field:SerializedName("panjangmaksimal")
    val panjangmaksimal: Int? = null,

    @field:SerializedName("satuanberat")
    val satuanberat: String? = null,

    @field:SerializedName("jeniskemasan")
    val jeniskemasan: String? = null,

    @field:SerializedName("isactive")
    val isactive: Int? = null,

    @field:SerializedName("beratmaksimal")
    val beratmaksimal: Int? = null,

    @field:SerializedName("lebarmaksimal")
    val lebarmaksimal: Int? = null,

    @field:SerializedName("namajenisbarang")
    val namajenisbarang: String? = null,

    @field:SerializedName("usermodify")
    val usermodify: Any? = null,

    @field:SerializedName("tglcreate")
    val tglcreate: String? = null,

    @field:SerializedName("tglmodify")
    val tglmodify: String? = null,

    @field:SerializedName("tinggimaksimal")
    val tinggimaksimal: Int? = null,

    @field:SerializedName("usercreate")
    val usercreate: String? = null,

    @field:SerializedName("kodejenisbarang")
    val kodejenisbarang: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("satuanpanjang")
    val satuanpanjang: String? = null
)
