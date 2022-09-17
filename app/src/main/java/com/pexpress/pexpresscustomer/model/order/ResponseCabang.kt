package com.pexpress.pexpresscustomer.model.order

import com.google.gson.annotations.SerializedName

data class ResponseCabang(

    @field:SerializedName("data")
    val data: List<ResultCabang?>? = null,

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class ResultCabang(

    @field:SerializedName("jeniskantor")
    val jeniskantor: String? = null,

    @field:SerializedName("id_cabang")
    val idCabang: Int? = null,

    @field:SerializedName("nama")
    val nama: String? = null,

    @field:SerializedName("koderegiondistrict")
    val koderegiondistrict: String? = null,

    @field:SerializedName("kode")
    val kode: String? = null,

    @field:SerializedName("isactive")
    val isactive: Int? = null,

    @field:SerializedName("telepon")
    val telepon: String? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("alamat")
    val alamat: String? = null
)
