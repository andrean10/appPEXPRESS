package com.pexpress.pexpresscustomer.model.diskon

import com.google.gson.annotations.SerializedName

data class ResponseCheckDiskon(

    @field:SerializedName("data")
    val data: ResultCheckDiskon? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: Boolean? = null
)

data class ResultCheckDiskon(

//    @field:SerializedName("frnmdiskon")
//    val frnmdiskon: String? = null,

    @field:SerializedName("frtarif")
    val frtarif: String? = null,

    @field:SerializedName("frnpdiskon")
    val frnpdiskon: Int? = null,

    @field:SerializedName("diskonharga")
    val diskonharga: Int? = null,

    @field:SerializedName("frprddiskon")
    val frprddiskon: String? = null,

    @field:SerializedName("frkddiskon")
    val frkddiskon: String? = null
)
