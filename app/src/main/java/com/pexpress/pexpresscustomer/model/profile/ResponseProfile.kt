package com.pexpress.pexpresscustomer.model.profile

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class ResponseProfile(

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("detail")
    val detail: List<ResultProfile?>? = null,

    @field:SerializedName("message")
    val message: String? = null
)

@Parcelize
data class ResultProfile(

    @field:SerializedName("birthday")
    val birthday: String? = null,

    @field:SerializedName("address")
    val address: String? = null,

    @field:SerializedName("gender")
    val gender: String? = null,

    @field:SerializedName("contact")
    val contact: String? = null,

    @field:SerializedName("otp")
    val otp: Int? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("fullname")
    val fullname: String? = null,

    @field:SerializedName("email")
    val email: String? = null
) : Parcelable
