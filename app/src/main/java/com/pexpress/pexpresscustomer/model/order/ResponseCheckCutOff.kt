package com.pexpress.pexpresscustomer.model.order

import com.google.gson.annotations.SerializedName

data class ResponseCheckCutOff(

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)
