package com.pexpress.pexpresscustomer.model.checkout.kilometer

import com.google.gson.annotations.SerializedName

data class ResponseCheckoutKilometer(

    @field:SerializedName("data")
    val data: List<ResultCheckoutKilometer>? = null,

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class ResultCheckoutKilometer(

    @field:SerializedName("teleponpengirim")
    val teleponpengirim: String? = null,

    @field:SerializedName("emailpengirim")
    val emailpengirim: String? = null,

    @field:SerializedName("alamatpengirim")
    val alamatpengirim: String? = null,

    @field:SerializedName("biaya")
    val biaya: String? = null,

    @field:SerializedName("nomorpemesanan")
    val nomorpemesanan: String? = null,

    @field:SerializedName("nomortracking")
    val nomortracking: String? = null,

    @field:SerializedName("namapengirim")
    val namapengirim: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("namapenerima")
    val namapenerima: String? = null
)
