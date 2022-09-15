package com.pexpress.pexpresscustomer.model.payment

import com.google.gson.annotations.SerializedName

data class ResponseStatusPembayaran(

    @field:SerializedName("data")
    val data: List<ResultStatusPembayaran?>? = null,

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class ResultStatusPembayaran(

    @field:SerializedName("account_number")
    val accountNumber: String? = null,

    @field:SerializedName("namabank")
    val namabank: String? = null,

    @field:SerializedName("namafile")
    val namafile: String? = null,

    @field:SerializedName("paytime")
    val paytime: String? = null,

    @field:SerializedName("ewallet")
    val ewallet: String? = null,

    @field:SerializedName("bank")
    val bank: String? = null,

    @field:SerializedName("tagihan")
    val tagihan: Int? = null,

    @field:SerializedName("expired")
    val expired: String? = null,

    @field:SerializedName("nama")
    val nama: String? = null,

    @field:SerializedName("usercreate")
    val usercreate: String? = null,

    @field:SerializedName("filebank")
    val filebank: String? = null,

    @field:SerializedName("nomorpemesanan")
    val nomorpemesanan: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("jenispembayaran")
    val jenispembayaran: String? = null,

    @field:SerializedName("status")
    val status: String? = null
)