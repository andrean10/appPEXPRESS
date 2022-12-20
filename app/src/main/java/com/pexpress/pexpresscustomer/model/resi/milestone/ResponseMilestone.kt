package com.pexpress.pexpresscustomer.model.resi.milestone

import com.google.gson.annotations.SerializedName

data class ResponseMilestone(

    @field:SerializedName("data")
    val data: List<ResultMilestone>? = null,

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class ResultMilestone(

    @field:SerializedName("tglpenugasandelivery")
    val tglpenugasandelivery: String? = null,

    @field:SerializedName("tglpending")
    val tglpending: String? = null,

    @field:SerializedName("jampending")
    val jampending: String? = null,

    @field:SerializedName("tanggalpenugasantransit")
    val tanggalpenugasantransit: String? = null,

    @field:SerializedName("tglprosesintransit")
    val tglprosesintransit: String? = null,

    @field:SerializedName("diserahkanolehdelivery")
    val diserahkanolehdelivery: String? = null,

    @field:SerializedName("tglbatal")
    val tglbatal: String? = null,

    @field:SerializedName("jamprosesshuttle")
    val jamprosesshuttle: String? = null,

    @field:SerializedName("namakurirDelivery")
    val namakurirDelivery: String? = null,

    @field:SerializedName("jampenugasanshuttle")
    val jampenugasanshuttle: String? = null,

    @field:SerializedName("jamprosesintransit")
    val jamprosesintransit: String? = null,

    @field:SerializedName("tglprosesshuttle")
    val tglprosesshuttle: String? = null,

    @field:SerializedName("tglrelase")
    val tglrelase: String? = null,

    @field:SerializedName("jampickup")
    val jampickup: String? = null,

    @field:SerializedName("jampenugasankembali")
    val jampenugasankembali: String? = null,

    @field:SerializedName("fotomenyerahkandelivery")
    val fotomenyerahkandelivery: String? = null,

    @field:SerializedName("cabang")
    val cabang: String? = null,

    @field:SerializedName("namastatuspengiriman")
    val namastatuspengiriman: String? = null,

    @field:SerializedName("jambatal")
    val jambatal: String? = null,

    @field:SerializedName("tanggaldelivery")
    val tanggaldelivery: String? = null,

    @field:SerializedName("catatan")
    val catatan: String? = null,

    @field:SerializedName("jamrelase")
    val jamrelase: String? = null,

    @field:SerializedName("statuspengiriman")
    val statuspengiriman: String? = null,

    @field:SerializedName("namakurir")
    val namakurir: String? = null,

    @field:SerializedName("fotomenyerahkan")
    val fotomenyerahkan: String? = null,

    @field:SerializedName("jampenugasandelivery")
    val jampenugasandelivery: String? = null,

    @field:SerializedName("tglpenugasankembali")
    val tglpenugasankembali: String? = null,

    @field:SerializedName("diserahkanoleh")
    val diserahkanoleh: String? = null,

    @field:SerializedName("reffno")
    val reffno: String? = null,

    @field:SerializedName("tglcreate")
    val tglcreate: String? = null,

    @field:SerializedName("jamdelivery")
    val jamdelivery: String? = null,

    @field:SerializedName("namakurirShuttle")
    val namakurirShuttle: String? = null,

    @field:SerializedName("jampenugasanpickup")
    val jampenugasanpickup: String? = null,

    @field:SerializedName("tanggalpickup")
    val tanggalpickup: String? = null,

    @field:SerializedName("informasistatuspengiriman")
    val informasistatuspengiriman: String? = null,

    @field:SerializedName("tanggalpenugasanpickup")
    val tanggalpenugasanpickup: String? = null,

    @field:SerializedName("tanggalpenugasanshuttle")
    val tanggalpenugasanshuttle: String? = null,

    @field:SerializedName("paytime")
    val paytime: String? = null,
)
