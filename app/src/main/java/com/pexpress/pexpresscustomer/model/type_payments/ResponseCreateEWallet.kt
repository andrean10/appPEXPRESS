package com.pexpress.pexpresscustomer.model.type_payments

import com.google.gson.annotations.SerializedName

data class ResponseCreateEWallet(

    @field:SerializedName("data")
    val data: Data? = null,

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class Actions(

    @field:SerializedName("mobile_deeplink_checkout_url")
    val mobileDeeplinkCheckoutUrl: String? = null,

    @field:SerializedName("mobile_web_checkout_url")
    val mobileWebCheckoutUrl: String? = null,

    @field:SerializedName("qr_checkout_string")
    val qrCheckoutString: String? = null,

    @field:SerializedName("desktop_web_checkout_url")
    val desktopWebCheckoutUrl: String? = null
)

data class Data(

    @field:SerializedName("voided_at")
    val voidedAt: String? = null,

    @field:SerializedName("basket")
    val basket: String? = null,

    @field:SerializedName("failure_code")
    val failureCode: Int? = null,

    @field:SerializedName("metadata")
    val metadata: Any? = null,

    @field:SerializedName("reference_id")
    val referenceId: String? = null,

    @field:SerializedName("created")
    val created: String? = null,

    @field:SerializedName("capture_now")
    val captureNow: Boolean? = null,

    @field:SerializedName("is_redirect_required")
    val isRedirectRequired: Boolean? = null,

    @field:SerializedName("channel_code")
    val channelCode: String? = null,

    @field:SerializedName("refunded_amount")
    val refundedAmount: Any? = null,

    @field:SerializedName("callback_url")
    val callbackUrl: String? = null,

    @field:SerializedName("payment_method_id")
    val paymentMethodId: Int? = null,

    @field:SerializedName("checkout_method")
    val checkoutMethod: String? = null,

    @field:SerializedName("currency")
    val currency: String? = null,

    @field:SerializedName("channel_properties")
    val channelProperties: ChannelProperties? = null,

    @field:SerializedName("void_status")
    val voidStatus: Any? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("customer_id")
    val customerId: Int? = null,

    @field:SerializedName("business_id")
    val businessId: String? = null,

    @field:SerializedName("actions")
    val actions: Actions? = null,

    @field:SerializedName("updated")
    val updated: String? = null,

    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("charge_amount")
    val chargeAmount: Int? = null,

    @field:SerializedName("capture_amount")
    val captureAmount: Int? = null
)

data class ChannelProperties(

    @field:SerializedName("success_redirect_url")
    val successRedirectUrl: String? = null,

    @field:SerializedName("mobile_number")
    val mobileNumber: String? = null,

    @field:SerializedName("failure_redirect_url")
    val failureRedirectUrl: String? = null
)
