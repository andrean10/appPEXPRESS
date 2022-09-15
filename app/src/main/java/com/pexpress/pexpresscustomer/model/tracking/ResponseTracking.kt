package com.pexpress.pexpresscustomer.model.tracking

import com.google.gson.annotations.SerializedName
import com.pexpress.pexpresscustomer.model.resi.milestone.ResultMilestone

data class ResponseTracking(

    @field:SerializedName("data")
    val data: ResultTracking? = null,

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

//data class MillestoneItem(
//
//	@field:SerializedName("fotomenyerahkandelivery")
//	val fotomenyerahkandelivery: String? = null,
//
//	@field:SerializedName("cabang")
//	val cabang: Int? = null,
//
//	@field:SerializedName("namastatuspengiriman")
//	val namastatuspengiriman: String? = null,
//
//	@field:SerializedName("jambatal")
//	val jambatal: String? = null,
//
//	@field:SerializedName("tanggaldelivery")
//	val tanggaldelivery: String? = null,
//
//	@field:SerializedName("catatan")
//	val catatan: String? = null,
//
//	@field:SerializedName("statuspengiriman")
//	val statuspengiriman: Int? = null,
//
//	@field:SerializedName("namakurir")
//	val namakurir: String? = null,
//
//	@field:SerializedName("fotomenyerahkan")
//	val fotomenyerahkan: String? = null,
//
//	@field:SerializedName("namakurirshuttle")
//	val namakurirshuttle: String? = null,
//
//	@field:SerializedName("diserahkanolehdelivery")
//	val diserahkanolehdelivery: String? = null,
//
//	@field:SerializedName("namakurirdelivery")
//	val namakurirdelivery: Int? = null,
//
//	@field:SerializedName("tglbatal")
//	val tglbatal: String? = null,
//
//	@field:SerializedName("diserahkanoleh")
//	val diserahkanoleh: String? = null,
//
//	@field:SerializedName("reffno")
//	val reffno: Int? = null,
//
//	@field:SerializedName("tglcreate")
//	val tglcreate: String? = null,
//
//	@field:SerializedName("jampenugasanshuttle")
//	val jampenugasanshuttle: String? = null,
//
//	@field:SerializedName("jamdelivery")
//	val jamdelivery: String? = null,
//
//	@field:SerializedName("jampenugasanpickup")
//	val jampenugasanpickup: String? = null,
//
//	@field:SerializedName("jampickup")
//	val jampickup: String? = null,
//
//	@field:SerializedName("tanggalpickup")
//	val tanggalpickup: String? = null,
//
//	@field:SerializedName("informasistatuspengiriman")
//	val informasistatuspengiriman: String? = null,
//
//	@field:SerializedName("tanggalpenugasanpickup")
//	val tanggalpenugasanpickup: String? = null,
//
//	@field:SerializedName("tanggalpenugasanshuttle")
//	val tanggalpenugasanshuttle: String? = null
//)

data class ResultTracking(

    @field:SerializedName("kecamatanpengirim")
    val kecamatanpengirim: Int? = null,

    @field:SerializedName("provinsipenerima")
    val provinsipenerima: String? = null,

    @field:SerializedName("jenisbarang")
    val jenisbarang: String? = null,

    @field:SerializedName("trdiskon")
    val trdiskon: Int? = null,

    @field:SerializedName("biaya")
    val biaya: String? = null,

    @field:SerializedName("berat")
    val berat: String? = null,

    @field:SerializedName("usercreate")
    val usercreate: String? = null,

    @field:SerializedName("namapengirim")
    val namapengirim: String? = null,

    @field:SerializedName("jenispembayaran")
    val jenispembayaran: Int? = null,

    @field:SerializedName("catatanpengirim")
    val catatanpengirim: String? = null,

    @field:SerializedName("alamatpenerima")
    val alamatpenerima: String? = null,

    @field:SerializedName("statuspengiriman")
    val statuspengiriman: Int? = null,

    @field:SerializedName("catatanpenerima")
    val catatanpenerima: String? = null,

    @field:SerializedName("provinsipengirim")
    val provinsipengirim: String? = null,

    @field:SerializedName("gkecamatanpenerima")
    val gkecamatanpenerima: String? = null,

    @field:SerializedName("namajenisbarang")
    val namajenisbarang: String? = null,

    @field:SerializedName("panjang")
    val panjang: String? = null,

    @field:SerializedName("usermodify")
    val usermodify: String? = null,

    @field:SerializedName("tdmanual")
    val tdmanual: String? = null,

    @field:SerializedName("tglcreate")
    val tglcreate: String? = null,

    @field:SerializedName("teleponpenerima")
    val teleponpenerima: String? = null,

    @field:SerializedName("district")
    val district: String? = null,

    @field:SerializedName("tanggalpickup")
    val tanggalpickup: String? = null,

    @field:SerializedName("kotapenerima")
    val kotapenerima: String? = null,

    @field:SerializedName("idbyrvirtual")
    val idbyrvirtual: Int? = null,

    @field:SerializedName("informasistatuspengiriman")
    val informasistatuspengiriman: String? = null,

    @field:SerializedName("tinggi")
    val tinggi: String? = null,

    @field:SerializedName("jenisukuran")
    val jenisukuran: Int? = null,

    @field:SerializedName("layanan")
    val layanan: String? = null,

    @field:SerializedName("alamatpengirim")
    val alamatpengirim: String? = null,

    @field:SerializedName("kotapengirim")
    val kotapengirim: String? = null,

    @field:SerializedName("kodelayanan")
    val kodelayanan: String? = null,

    @field:SerializedName("kodestatuspengiriman")
    val kodestatuspengiriman: String? = null,

    @field:SerializedName("gkecamatanpengirim")
    val gkecamatanpengirim: String? = null,

    @field:SerializedName("jenispengiriman")
    val jenispengiriman: Int? = null,

    @field:SerializedName("pelangganbaru")
    val pelangganbaru: String? = null,

    @field:SerializedName("tglmodify")
    val tglmodify: String? = null,

    @field:SerializedName("catatanipembayaran")
    val catatanipembayaran: String? = null,

    @field:SerializedName("nomorpemesanan")
    val nomorpemesanan: String? = null,

    @field:SerializedName("nomortracking")
    val nomortracking: String? = null,

    @field:SerializedName("regencies")
    val regencies: String? = null,

    @field:SerializedName("lebar")
    val lebar: String? = null,

    @field:SerializedName("teleponpengirim")
    val teleponpengirim: String? = null,

    @field:SerializedName("tipestatus")
    val tipestatus: String? = null,

    @field:SerializedName("namastatuspengiriman")
    val namastatuspengiriman: String? = null,

    @field:SerializedName("millestone")
    val millestone: List<ResultMilestone>? = null,

    @field:SerializedName("namajenisukuran")
    val namajenisukuran: String? = null,

    @field:SerializedName("emailpengirim")
    val emailpengirim: String? = null,

    @field:SerializedName("reffno")
    val reffno: Int? = null,

    @field:SerializedName("kecamatanpenerima")
    val kecamatanpenerima: Int? = null,

    @field:SerializedName("iddiskon")
    val iddiskon: String? = null,

    @field:SerializedName("namapenerima")
    val namapenerima: String? = null,

    @field:SerializedName("statusangkut")
    val statusangkut: String? = null
)
