package com.pexpress.pexpresscustomer.model.main

import com.google.gson.annotations.SerializedName

data class ResponseBanner(

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("banner")
    val banner: List<BannerItem>? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class BannerItem(

    @field:SerializedName("namafile")
    val namafile: String? = null
)
