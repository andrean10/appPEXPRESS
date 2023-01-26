package com.pexpress.pexpresscustomer.model.status_order

import com.google.gson.annotations.SerializedName

data class ResponseStatusOrderNew(

    @field:SerializedName("data")
    val data: List<DataItem?>? = null,

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class DataItem(

    @field:SerializedName("kecamatanpengirim")
    val kecamatanpengirim: String? = null,

    @field:SerializedName("provinsipenerima")
    val provinsipenerima: Any? = null,

    @field:SerializedName("jenisbarang")
    val jenisbarang: String? = null,

    @field:SerializedName("namakecamatanpengirim")
    val namakecamatanpengirim: String? = null,

    @field:SerializedName("cabang_awal")
    val cabangAwal: String? = null,

    @field:SerializedName("trdiskon")
    val trdiskon: String? = null,

    @field:SerializedName("type")
    val type: String? = null,

    @field:SerializedName("cabangtujuan")
    val cabangtujuan: String? = null,

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

    @field:SerializedName("cabangasal")
    val cabangasal: String? = null,

    @field:SerializedName("latpengirim")
    val latpengirim: Any? = null,

    @field:SerializedName("catatanpengirim")
    val catatanpengirim: Any? = null,

    @field:SerializedName("alamatpenerima")
    val alamatpenerima: String? = null,

    @field:SerializedName("statuspengiriman")
    val statuspengiriman: String? = null,

    @field:SerializedName("catatanpenerima")
    val catatanpenerima: Any? = null,

    @field:SerializedName("provinsipengirim")
    val provinsipengirim: Any? = null,

    @field:SerializedName("gkecamatanpenerima")
    val gkecamatanpenerima: String? = null,

    @field:SerializedName("namajenisbarang")
    val namajenisbarang: String? = null,

    @field:SerializedName("panjang")
    val panjang: String? = null,

    @field:SerializedName("latpenerima")
    val latpenerima: Any? = null,

    @field:SerializedName("usermodify")
    val usermodify: String? = null,

    @field:SerializedName("namakecamatanpenerima")
    val namakecamatanpenerima: String? = null,

    @field:SerializedName("tdmanual")
    val tdmanual: String? = null,

    @field:SerializedName("tglcreate")
    val tglcreate: String? = null,

    @field:SerializedName("teleponpenerima")
    val teleponpenerima: String? = null,

    @field:SerializedName("jenisbaranglainnya")
    val jenisbaranglainnya: Any? = null,

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
    val jenisukuran: String? = null,

    @field:SerializedName("namajenispengiriman")
    val namajenispengiriman: String? = null,

    @field:SerializedName("account_number")
    val accountNumber: String? = null,

    @field:SerializedName("alamatpengirim")
    val alamatpengirim: String? = null,

    @field:SerializedName("kotapengirim")
    val kotapengirim: Any? = null,

    @field:SerializedName("kodestatuspengiriman")
    val kodestatuspengiriman: String? = null,

    @field:SerializedName("tipepengiriman")
    val tipepengiriman: String? = null,

    @field:SerializedName("gkecamatanpengirim")
    val gkecamatanpengirim: String? = null,

    @field:SerializedName("asuransi")
    val asuransi: String? = null,

    @field:SerializedName("jenispengiriman")
    val jenispengiriman: String? = null,

    @field:SerializedName("pelangganbaru")
    val pelangganbaru: Any? = null,

    @field:SerializedName("diserahkanolehdelivery")
    val diserahkanolehdelivery: String? = null,

    @field:SerializedName("expired")
    val expired: String? = null,

    @field:SerializedName("tglmodify")
    val tglmodify: String? = null,

    @field:SerializedName("catatanipembayaran")
    val catatanipembayaran: Any? = null,

    @field:SerializedName("nomorpemesanan")
    val nomorpemesanan: String? = null,

    @field:SerializedName("nomortracking")
    val nomortracking: String? = null,

    @field:SerializedName("lebar")
    val lebar: String? = null,

    @field:SerializedName("teleponpengirim")
    val teleponpengirim: String? = null,

    @field:SerializedName("cabang")
    val cabang: String? = null,

    @field:SerializedName("tipestatus")
    val tipestatus: String? = null,

    @field:SerializedName("namastatuspengiriman")
    val namastatuspengiriman: String? = null,

    @field:SerializedName("longpengirim")
    val longpengirim: Any? = null,

    @field:SerializedName("paytime")
    val paytime: String? = null,

    @field:SerializedName("jaraktempuh")
    val jaraktempuh: Any? = null,

    @field:SerializedName("namajenisukuran")
    val namajenisukuran: String? = null,

    @field:SerializedName("emailpengirim")
    val emailpengirim: String? = null,

    @field:SerializedName("reffno")
    val reffno: String? = null,

    @field:SerializedName("kecamatanpenerima")
    val kecamatanpenerima: String? = null,

    @field:SerializedName("longpenerima")
    val longpenerima: Any? = null,

    @field:SerializedName("informasijenispembayaran")
    val informasijenispembayaran: String? = null,

    @field:SerializedName("iddiskon")
    val iddiskon: String? = null,

    @field:SerializedName("namapenerima")
    val namapenerima: String? = null,

    @field:SerializedName("statusangkut")
    val statusangkut: Any? = null
)