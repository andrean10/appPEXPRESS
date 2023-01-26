package com.pexpress.pexpresscustomer.model.kilometer.ongkir

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class ResponseCheckOngkirKilometer(

    @field:SerializedName("data")
    val data: ResultCheckOngkirKilometer? = null,

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

@Parcelize
data class ResultCheckOngkirKilometer(

    @field:SerializedName("tarif")
    val tarif: String? = null,

    @field:SerializedName("jenisukuran")
    val jenisukuran: String? = null,

    @field:SerializedName("layanan")
    val layanan: String? = null,

    @field:SerializedName("minkm")
    val minkm: Int? = null,

    @field:SerializedName("waktumaksimalorder")
    val waktumaksimalorder: String? = null,

    @field:SerializedName("maksimalpanjang")
    val maksimalpanjang: String? = null,

    @field:SerializedName("maksimallebar")
    val maksimallebar: String? = null,

    @field:SerializedName("maksimaltinggi")
    val maksimaltinggi: String? = null,

    @field:SerializedName("maksimaljamorder")
    val maksimaljamorder: String? = null,

    @field:SerializedName("maxkm")
    val maxkm: Int? = null,

    @field:SerializedName("maksimalberat")
    val maksimalberat: String? = null,

    @field:SerializedName("minimumtarif")
    val minimumtarif: Int? = null,

    @field:SerializedName("waktupengiriman")
    val waktupengiriman: String? = null,

    @field:SerializedName("tarifkm")
    val tarifkm: Int? = null,

    @field:SerializedName("biaya")
    val biaya: Int? = null,

    @field:SerializedName("kodepaket")
    val kodepaket: String? = null,

    @field:SerializedName("informasipengiriman")
    val informasipengiriman: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("satuanpanjang")
    val satuanpanjang: String? = null
) : Parcelable
