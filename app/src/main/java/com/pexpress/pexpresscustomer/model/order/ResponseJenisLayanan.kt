package com.pexpress.pexpresscustomer.model.order

import com.google.gson.annotations.SerializedName

data class ResponseJenisLayanan(

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("detail")
    val detail: List<ResultJenisLayanan>? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class ResultJenisLayanan(

    @field:SerializedName("layanan")
    val layanan: String? = null,

    @field:SerializedName("waktupengiriman")
    val waktupengiriman: String? = null,

    @field:SerializedName("waktumaksimalorder")
    val waktumaksimalorder: String? = null,

    @field:SerializedName("usercreate")
    val usercreate: String? = null,

    @field:SerializedName("idlayanan")
    val idlayanan: Int? = null,

    @field:SerializedName("kodelayanan")
    val kodelayanan: String? = null,

    @field:SerializedName("isactive")
    val isactive: Int? = null,

    @field:SerializedName("informasipengiriman")
    val informasipengiriman: String? = null
)
