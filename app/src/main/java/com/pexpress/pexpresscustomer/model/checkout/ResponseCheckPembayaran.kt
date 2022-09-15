package com.pexpress.pexpresscustomer.model.checkout

import com.google.gson.annotations.SerializedName

data class ResponseCheckPembayaran(

    @field:SerializedName("data")
    val data: List<ResultCheckPembayaran?>? = null,

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class ResultCheckPembayaran(

    @field:SerializedName("account_number")
    val accountNumber: String? = null,

    @field:SerializedName("bank")
    val bank: String? = null,

    @field:SerializedName("tagihan")
    val tagihan: Int? = null,

    @field:SerializedName("expired")
    val expired: String? = null,

    @field:SerializedName("nama")
    val nama: String? = null,

    @field:SerializedName("nomorpemesanan")
    val nomorpemesanan: String? = null,

    @field:SerializedName("idjenispembayaran")
    val idjenispembayaran: Int? = null,

    @field:SerializedName("paytime")
    val paytime: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("status")
    val status: String? = null
)
