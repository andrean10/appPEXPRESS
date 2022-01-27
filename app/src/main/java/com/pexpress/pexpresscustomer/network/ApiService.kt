package com.pexpress.pexpresscustomer.network

import com.pexpress.pexpresscustomer.model.ResponseAuth
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    /** AUTH */
    @FormUrlEncoded
    @POST("login-customer")
    fun login(@Field("contact") contact: String): Call<ResponseAuth>

    @FormUrlEncoded
    @POST("register-customer")
    fun register(@FieldMap params: HashMap<String, String>): Call<ResponseAuth>

    @FormUrlEncoded
    @POST("verify-otp")
    fun verifyOTP(@FieldMap params: HashMap<String, String>): Call<ResponseAuth>
}
