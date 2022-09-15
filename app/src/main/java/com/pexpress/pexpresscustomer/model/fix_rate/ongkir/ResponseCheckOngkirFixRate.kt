package com.pexpress.pexpresscustomer.model.fix_rate.ongkir

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class ResponseCheckOngkirFixRate(

    @field:SerializedName("data")
    val data: List<ResultCheckOngkirFixRate?>? = null,

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

@Parcelize
data class ResultCheckOngkirFixRate(

    @field:SerializedName("jenisukuran")
    val jenisukuran: String? = null,

    @field:SerializedName("cabangasal")
    val cabangasal: Int? = null,

    @field:SerializedName("layanan")
    val layanan: String? = null,

    @field:SerializedName("waktumaksimalorder")
    val waktumaksimalorder: String? = null,

    @field:SerializedName("maksimalpanjang")
    val maksimalpanjang: String? = null,

    @field:SerializedName("maksimallebar")
    val maksimallebar: String? = null,

    @field:SerializedName("maksimaltinggi")
    val maksimaltinggi: String? = null,

    @field:SerializedName("maksimalberat")
    val maksimalberat: String? = null,

    @field:SerializedName("cabangtujuan")
    val cabangtujuan: Int? = null,

    @field:SerializedName("tarif")
    val tarif: String? = null,

    @field:SerializedName("waktupengiriman")
    val waktupengiriman: String? = null,

    @field:SerializedName("idjenispengiriman")
    val idjenispengiriman: Int? = null,

    @field:SerializedName("informasipengiriman")
    val informasipengiriman: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("idjenisukuran")
    val idjenisukuran: Int? = null,

    @field:SerializedName("satuanpanjang")
    val satuanpanjang: String? = null
) : Parcelable
