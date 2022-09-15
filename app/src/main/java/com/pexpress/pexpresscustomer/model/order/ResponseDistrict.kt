package com.pexpress.pexpresscustomer.model.order

import com.google.gson.annotations.SerializedName

data class ResponseDistrict(

    @field:SerializedName("data")
    val data: List<ResultDistrict?>? = null,

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class ResultDistrict(

    @field:SerializedName("district")
    val district: String? = null,

    @field:SerializedName("id_district")
    val idDistrict: Int? = null,

    @field:SerializedName("id")
    val id: Int? = null
)
