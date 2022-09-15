package com.pexpress.pexpresscustomer.model

import com.google.gson.annotations.SerializedName

data class ResponseCreateVA(

    @field:SerializedName("data")
    val data: ResultCreateVA? = null,

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class ResultCreateVA(

    @field:SerializedName("bank_code")
    val bankCode: String? = null,

    @field:SerializedName("account_number")
    val accountNumber: String? = null,

    @field:SerializedName("merchant_code")
    val merchantCode: String? = null,

    @field:SerializedName("owner_id")
    val ownerId: String? = null,

    @field:SerializedName("is_single_use")
    val isSingleUse: Boolean? = null,

    @field:SerializedName("external_id")
    val externalId: String? = null,

    @field:SerializedName("expiration_date")
    val expirationDate: String? = null,

    @field:SerializedName("expected_amount")
    val expectedAmount: Int? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("currency")
    val currency: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("is_closed")
    val isClosed: Boolean? = null,

    @field:SerializedName("status")
    val status: String? = null
)
