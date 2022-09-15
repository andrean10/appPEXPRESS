package com.pexpress.pexpresscustomer.model.profile

import com.google.gson.annotations.SerializedName

data class ResponseManageProfile(

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("detail")
    val detail: List<ResultManageProfile?>? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class ResultManageProfile(

    @field:SerializedName("birthday")
    val birthday: String? = null,

    @field:SerializedName("full_name")
    val fullName: String? = null,

    @field:SerializedName("address")
    val address: String? = null,

    @field:SerializedName("gender")
    val gender: String? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("contact")
    val contact: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("otp")
    val otp: Int? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("is_verified")
    val isVerified: Int? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("is_login")
    val isLogin: Boolean? = null
)
