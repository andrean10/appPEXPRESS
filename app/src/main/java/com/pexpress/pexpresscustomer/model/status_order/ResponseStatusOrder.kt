package com.pexpress.pexpresscustomer.model.status_order

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

data class ResponseStatusOrder(

    @field:SerializedName("data")
    val data: List<ResultStatusOrder?>? = null,

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

@Parcelize
data class ResultStatusOrder(

    @field:SerializedName("jenisbaranglainnya")
    val jenisbaranglainnya: String? = null,

    @field:SerializedName("asuransi")
    val asuransi: String? = null,

    @field:SerializedName("diserahkanolehdelivery")
    val diserahkanolehdelivery: String? = null,

    @field:SerializedName("kecamatanpengirim")
    val kecamatanpengirim: Int? = null,

    @field:SerializedName("provinsipenerima")
    val provinsipenerima: @RawValue Any? = null,

    @field:SerializedName("jenisbarang")
    val jenisbarang: String? = null,

    @field:SerializedName("trdiskon")
    val trdiskon: @RawValue Any? = null,

    @field:SerializedName("type")
    val type: String? = null,

    @field:SerializedName("bank")
    val bank: String? = null,

    @field:SerializedName("biaya")
    val biaya: String? = null,

    @field:SerializedName("berat")
    val berat: String? = null,

    @field:SerializedName("usercreate")
    val usercreate: String? = null,

    @field:SerializedName("namapengirim")
    val namapengirim: String? = null,

    @field:SerializedName("jenispembayaran")
    val jenispembayaran: String? = null,

    @field:SerializedName("catatanpengirim")
    val catatanpengirim: String? = null,

    @field:SerializedName("alamatpenerima")
    val alamatpenerima: String? = null,

    @field:SerializedName("statuspengiriman")
    val statuspengiriman: Int? = null,

    @field:SerializedName("catatanpenerima")
    val catatanpenerima: String? = null,

    @field:SerializedName("provinsipengirim")
    val provinsipengirim: Int? = null,

    @field:SerializedName("gkecamatanpenerima")
    val gkecamatanpenerima: String? = null,

    @field:SerializedName("panjang")
    val panjang: String? = null,

    @field:SerializedName("usermodify")
    val usermodify: String? = null,

    @field:SerializedName("tdmanual")
    val tdmanual: @RawValue Any? = null,

    @field:SerializedName("tglcreate")
    val tglcreate: String? = null,

    @field:SerializedName("teleponpenerima")
    val teleponpenerima: String? = null,

    @field:SerializedName("tanggalpickup")
    val tanggalpickup: String? = null,

    @field:SerializedName("kotapenerima")
    val kotapenerima: @RawValue Any? = null,

    @field:SerializedName("idbyrvirtual")
    val idbyrvirtual: @RawValue Any? = null,

    @field:SerializedName("informasistatuspengiriman")
    val informasistatuspengiriman: String? = null,

    @field:SerializedName("tinggi")
    val tinggi: String? = null,

    @field:SerializedName("jenisukuran")
    val jenisukuran: Int? = null,

    @field:SerializedName("account_number")
    val accountNumber: String? = null,

    @field:SerializedName("alamatpengirim")
    val alamatpengirim: String? = null,

    @field:SerializedName("kotapengirim")
    val kotapengirim: @RawValue Any? = null,

    @field:SerializedName("kodestatuspengiriman")
    val kodestatuspengiriman: String? = null,

    @field:SerializedName("gkecamatanpengirim")
    val gkecamatanpengirim: String? = null,

    @field:SerializedName("jenispengiriman")
    val jenispengiriman: Int? = null,

    @field:SerializedName("pelangganbaru")
    val pelangganbaru: @RawValue Any? = null,

    @field:SerializedName("expired")
    val expired: String? = null,

    @field:SerializedName("tglmodify")
    val tglmodify: String? = null,

    @field:SerializedName("catatanipembayaran")
    val catatanipembayaran: String? = null,

    @field:SerializedName("nomorpemesanan")
    val nomorpemesanan: String? = null,

    @field:SerializedName("nomortracking")
    val nomortracking: String? = null,

    @field:SerializedName("lebar")
    val lebar: String? = null,

    @field:SerializedName("teleponpengirim")
    val teleponpengirim: String? = null,

    @field:SerializedName("tipestatus")
    val tipestatus: String? = null,

    @field:SerializedName("namastatuspengiriman")
    val namastatuspengiriman: String? = null,

    @field:SerializedName("paytime")
    val paytime: String? = null,

    @field:SerializedName("emailpengirim")
    val emailpengirim: String? = null,

    @field:SerializedName("reffno")
    val reffno: Int? = null,

    @field:SerializedName("kecamatanpenerima")
    val kecamatanpenerima: Int? = null,

    @field:SerializedName("informasijenispembayaran")
    val informasijenispembayaran: String? = null,

    @field:SerializedName("iddiskon")
    val iddiskon: @RawValue Any? = null,

    @field:SerializedName("namapenerima")
    val namapenerima: String? = null,

    @field:SerializedName("statusangkut")
    val statusangkut: Int? = null
) : Parcelable
