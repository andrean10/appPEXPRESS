package com.pexpress.pexpresscustomer.network

import com.pexpress.pexpresscustomer.model.ResponseCreateVA
import com.pexpress.pexpresscustomer.model.auth.ResponseLogin
import com.pexpress.pexpresscustomer.model.auth.ResponseOTP
import com.pexpress.pexpresscustomer.model.auth.ResponseRegister
import com.pexpress.pexpresscustomer.model.auth.session.ResponseSession
import com.pexpress.pexpresscustomer.model.checkout.ResponseCheckPembayaran
import com.pexpress.pexpresscustomer.model.checkout.ResponseCheckoutUpdate
import com.pexpress.pexpresscustomer.model.checkout.asuransi.ResponseAsuransi
import com.pexpress.pexpresscustomer.model.checkout.fix_rate.ResponseCheckout
import com.pexpress.pexpresscustomer.model.checkout.fix_rate.ResponseEditCheckoutFixRate
import com.pexpress.pexpresscustomer.model.checkout.kilometer.ResponseCheckoutKilometer
import com.pexpress.pexpresscustomer.model.checkout.kilometer.ResponseEditCheckoutKilometer
import com.pexpress.pexpresscustomer.model.distance.ResponseDistance
import com.pexpress.pexpresscustomer.model.fix_rate.ongkir.ResponseCheckOngkirFixRate
import com.pexpress.pexpresscustomer.model.kilometer.ongkir.ResponseCheckOngkirKilometer
import com.pexpress.pexpresscustomer.model.main.ResponseBanner
import com.pexpress.pexpresscustomer.model.order.*
import com.pexpress.pexpresscustomer.model.payment.ResponseDetailStatusPembayaran
import com.pexpress.pexpresscustomer.model.payment.ResponseStatusPembayaran
import com.pexpress.pexpresscustomer.model.profile.ResponseManageProfile
import com.pexpress.pexpresscustomer.model.profile.ResponseProfile
import com.pexpress.pexpresscustomer.model.resi.ResponseResi
import com.pexpress.pexpresscustomer.model.resi.milestone.ResponseMilestone
import com.pexpress.pexpresscustomer.model.status_order.ResponseStatusOrder
import com.pexpress.pexpresscustomer.model.tracking.ResponseTracking
import com.pexpress.pexpresscustomer.model.type_payments.ResponseCreateCash
import com.pexpress.pexpresscustomer.model.type_payments.ResponseCreateEWallet
import com.pexpress.pexpresscustomer.model.type_payments.ResponseEWallet
import com.pexpress.pexpresscustomer.model.type_payments.ResponseVA
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    /** AUTH */
    @FormUrlEncoded
    @POST("login-customer")
    fun login(@Field("contact") contact: String): Call<ResponseLogin>

    @FormUrlEncoded
    @POST("register-customer")
    fun register(@Field("contact") contact: String): Call<ResponseRegister>

    @FormUrlEncoded
    @POST("verify-otp")
    fun verifyOTP(@FieldMap params: HashMap<String, String>): Call<ResponseOTP>

    @FormUrlEncoded
    @POST("send-otp")
    fun retryOTP(@Field("contact") contact: String): Call<ResponseOTP>

    // Profile
    @GET("profile-customer")
    fun profile(@Query("contact") contact: String): Call<ResponseProfile>

    // Update Profile
    @FormUrlEncoded
    @POST("update-customer")
    fun updateProfile(@FieldMap params: HashMap<String, String>): Call<ResponseManageProfile>

    // logout session
    @FormUrlEncoded
    @POST("logout-customer")
    fun logoutSession(@Field("id") idUser: String): Call<ResponseSession>

    // Banner
    @GET("banner")
    fun banner(): Call<ResponseBanner>

    // Jenis Layanan
    @GET("jenis-layanan")
    fun jenisLayanan(): Call<ResponseJenisLayanan>

    // Ukuran Barang
    @GET("jenis-ukuran")
    fun jenisUkuran(): Call<ResponseJenisUkuran>

    // Jenis Barang
    @GET("jenis-barang")
    fun jenisBarang(): Call<ResponseJenisBarang>

    // Check Batas Wilayah
    @GET("check-district")
    fun district(
        @Query("district") district: String,
        @Query("layanan") layanan: String
    ): Call<ResponseDistrict>

    // Cabang
    @GET("cabang")
    fun cabang(@Query("id_district") district: Int): Call<ResponseCabang>

    // Checkout
    @FormUrlEncoded
    @POST("checkout")
    fun checkout(@FieldMap params: HashMap<String, String>): Call<ResponseCheckout>

    @FormUrlEncoded
    @POST("update-pengiriman/{id}")
    fun editCheckoutFixRate(
        @Path("id") id: Int,
        @FieldMap params: HashMap<String, String>
    ): Call<ResponseEditCheckoutFixRate>

    @FormUrlEncoded
    @POST("update-pengiriman-kilometer/{id}")
    fun editCheckoutKilometer(
        @Path("id") id: Int,
        @FieldMap params: HashMap<String, String>
    ): Call<ResponseEditCheckoutKilometer>

    // Update Asuransi
    @FormUrlEncoded
    @POST("checkout-update")
    fun checkoutUpdate(@FieldMap params: HashMap<String, String>): Call<ResponseCheckoutUpdate>

    // Checkout Kilometer
    @FormUrlEncoded
    @POST("checkout-kilometer")
    fun checkoutKilometer(@FieldMap params: HashMap<String, String>): Call<ResponseCheckoutKilometer>

    // Get Price Asuransi
    @GET("asuransi")
    fun asuransi(): Call<ResponseAsuransi>

    // List VA
    @GET("payments/va/list")
    fun listVA(): Call<ResponseVA>

    // List E-Wallet
    @GET("payments/ewallet/list")
    fun listEWallet(): Call<ResponseEWallet>

    // Membuat transaksi COD
    @FormUrlEncoded
    @POST("payments/cash")
    fun createCash(
        @Header("Authorization") authorization: String,
        @FieldMap params: HashMap<String, String>
    ): Call<ResponseCreateCash>

    // Membuat transaksi Transfer Bank
    @FormUrlEncoded
    @POST("payments/va/create")
    fun createVA(
        @Header("Authorization") authorization: String,
        @FieldMap params: HashMap<String, String>
    ): Call<ResponseCreateVA>

    // Membuat transaksi ewallet
    @FormUrlEncoded
    @POST("payments/ewallet")
    fun createEWallet(
        @Header("Authorization") authorization: String,
        @FieldMap params: HashMap<String, String>
    ): Call<ResponseCreateEWallet>

    // Membuat transaksi qr code


    // Check Pembayaran
    @GET("payments/check")
    fun checkPembayaran(@Query("no_invoice") noInvoice: String): Call<ResponseCheckPembayaran>

    // Pengiriman
    @GET("pengiriman/detail")
    fun statusOrder(@QueryMap queryParams: HashMap<String, String>): Call<ResponseStatusOrder>

    // Milestone
    @GET("pengiriman/milestone")
    fun milestone(@Query("id") id: Int): Call<ResponseMilestone>

    // Resi
    @GET("check-resi")
    fun checkResi(@Query("id") noInvoice: String): Call<ResponseResi>

    @GET("directions/json")
    fun checkDirections(
        @QueryMap params: HashMap<String, String>
    ): Call<ResponseDistance>

    // cek ongkir fix rate
    @GET("pengiriman/check-ongkir")
    fun checkOngkirFixRate(@QueryMap params: HashMap<String, String>): Call<ResponseCheckOngkirFixRate>

    // cek ongkir kilometer
    @GET("pengiriman/check-ongkir")
    fun checkOngkirKilometer(@QueryMap params: HashMap<String, String>): Call<ResponseCheckOngkirKilometer>

    // status pembayaran
    @GET("payments/history")
    fun statusPembayaran(
        @Query("id") idUser: String,
        @Query("status") status: String
    ): Call<ResponseStatusPembayaran>

    // history detail
    @GET("payments/history/detail")
    fun detailStatusPembayaran(@Query("no_invoice") noInvoice: String): Call<ResponseDetailStatusPembayaran>

    // cek tracking pesanan
    @GET("check-tracking")
    fun checkTracking(@Query("nomortracking") noTracking: String): Call<ResponseTracking>
}
