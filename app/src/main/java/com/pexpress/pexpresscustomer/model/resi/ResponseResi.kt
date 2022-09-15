package com.pexpress.pexpresscustomer.model.resi

import com.google.gson.annotations.SerializedName

data class ResponseResi(

    @field:SerializedName("data")
    val data: List<ResultResi>? = null,

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class ResultResi(

    @field:SerializedName("kecamatanpengirim")
    val kecamatanpengirim: Int? = null,

    @field:SerializedName("provinsipenerima")
    val provinsipenerima: Any? = null,

    @field:SerializedName("jenisbarang")
    val jenisbarang: String? = null,

    @field:SerializedName("trdiskon")
    val trdiskon: Any? = null,

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
    val provinsipengirim: Any? = null,

    @field:SerializedName("gkecamatanpenerima")
    val gkecamatanpenerima: String? = null,

    @field:SerializedName("namajenisbarang")
    val namajenisbarang: String? = null,

    @field:SerializedName("panjang")
    val panjang: String? = null,

    @field:SerializedName("usermodify")
    val usermodify: Any? = null,

    @field:SerializedName("tdmanual")
    val tdmanual: Any? = null,

    @field:SerializedName("tglcreate")
    val tglcreate: String? = null,

    @field:SerializedName("teleponpenerima")
    val teleponpenerima: String? = null,

    @field:SerializedName("district")
    val district: String? = null,

    @field:SerializedName("tanggalpickup")
    val tanggalpickup: String? = null,

    @field:SerializedName("kotapenerima")
    val kotapenerima: Any? = null,

    @field:SerializedName("idbyrvirtual")
    val idbyrvirtual: Any? = null,

    @field:SerializedName("informasistatuspengiriman")
    val informasistatuspengiriman: String? = null,

    @field:SerializedName("tinggi")
    val tinggi: String? = null,

    @field:SerializedName("jenisukuran")
    val jenisukuran: Int? = null,

    @field:SerializedName("alamatpengirim")
    val alamatpengirim: String? = null,

    @field:SerializedName("kotapengirim")
    val kotapengirim: Any? = null,

    @field:SerializedName("kodestatuspengiriman")
    val kodestatuspengiriman: String? = null,

    @field:SerializedName("gkecamatanpengirim")
    val gkecamatanpengirim: String? = null,

    @field:SerializedName("jenispengiriman")
    val jenispengiriman: Int? = null,

    @field:SerializedName("pelangganbaru")
    val pelangganbaru: Any? = null,

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

    @field:SerializedName("namajenisukuran")
    val namajenisukuran: String? = null,

    @field:SerializedName("emailpengirim")
    val emailpengirim: String? = null,

    @field:SerializedName("reffno")
    val reffno: Int? = null,

    @field:SerializedName("kecamatanpenerima")
    val kecamatanpenerima: Int? = null,

    @field:SerializedName("iddiskon")
    val iddiskon: Any? = null,

    @field:SerializedName("namapenerima")
    val namapenerima: String? = null,

    @field:SerializedName("statusangkut")
    val statusangkut: Any? = null
)
