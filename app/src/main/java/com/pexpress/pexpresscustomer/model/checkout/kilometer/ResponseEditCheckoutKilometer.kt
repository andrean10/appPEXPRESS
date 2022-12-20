package com.pexpress.pexpresscustomer.model.checkout.kilometer

import com.google.gson.annotations.SerializedName
import com.pexpress.pexpresscustomer.model.checkout.fix_rate.Data

data class ResponseEditCheckoutKilometer(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class Data(

	@field:SerializedName("kecamatanpengirim")
	val kecamatanpengirim: String? = null,

	@field:SerializedName("provinsipenerima")
	val provinsipenerima: Any? = null,

	@field:SerializedName("kodeasuransi")
	val kodeasuransi: Any? = null,

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

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("jenispembayaran")
	val jenispembayaran: String? = null,

	@field:SerializedName("latpengirim")
	val latpengirim: String? = null,

	@field:SerializedName("catatanpengirim")
	val catatanpengirim: String? = null,

	@field:SerializedName("alamatpenerima")
	val alamatpenerima: String? = null,

	@field:SerializedName("statuspengiriman")
	val statuspengiriman: Any? = null,

	@field:SerializedName("catatanpenerima")
	val catatanpenerima: String? = null,

	@field:SerializedName("provinsipengirim")
	val provinsipengirim: Any? = null,

	@field:SerializedName("gkecamatanpenerima")
	val gkecamatanpenerima: String? = null,

	@field:SerializedName("latpenerima")
	val latpenerima: String? = null,

	@field:SerializedName("panjang")
	val panjang: String? = null,

	@field:SerializedName("statusdelivery")
	val statusdelivery: String? = null,

	@field:SerializedName("usermodify")
	val usermodify: Any? = null,

	@field:SerializedName("tdmanual")
	val tdmanual: Any? = null,

	@field:SerializedName("tglcreate")
	val tglcreate: String? = null,

	@field:SerializedName("teleponpenerima")
	val teleponpenerima: String? = null,

	@field:SerializedName("tanggalpickup")
	val tanggalpickup: String? = null,

	@field:SerializedName("kotapenerima")
	val kotapenerima: Any? = null,

	@field:SerializedName("idbyrvirtual")
	val idbyrvirtual: Any? = null,

	@field:SerializedName("tinggi")
	val tinggi: String? = null,

	@field:SerializedName("biayaasuransi")
	val biayaasuransi: String? = null,

	@field:SerializedName("jenisukuran")
	val jenisukuran: String? = null,

	@field:SerializedName("statuspickup")
	val statuspickup: String? = null,

	@field:SerializedName("alamatpengirim")
	val alamatpengirim: String? = null,

	@field:SerializedName("kotapengirim")
	val kotapengirim: Any? = null,

	@field:SerializedName("tipepengiriman")
	val tipepengiriman: String? = null,

	@field:SerializedName("gkecamatanpengirim")
	val gkecamatanpengirim: String? = null,

	@field:SerializedName("jenispengiriman")
	val jenispengiriman: String? = null,

	@field:SerializedName("pelangganbaru")
	val pelangganbaru: Any? = null,

	@field:SerializedName("tglmodify")
	val tglmodify: Any? = null,

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

	@field:SerializedName("longpengirim")
	val longpengirim: String? = null,

	@field:SerializedName("jaraktempuh")
	val jaraktempuh: String? = null,

	@field:SerializedName("emailpengirim")
	val emailpengirim: String? = null,

	@field:SerializedName("kecamatanpenerima")
	val kecamatanpenerima: String? = null,

	@field:SerializedName("longpenerima")
	val longpenerima: String? = null,

	@field:SerializedName("iddiskon")
	val iddiskon: Any? = null,

	@field:SerializedName("namapenerima")
	val namapenerima: String? = null,

	@field:SerializedName("statusangkut")
	val statusangkut: Any? = null,

	@field:SerializedName("shipment_type")
	val shipmentType: String? = null
)
