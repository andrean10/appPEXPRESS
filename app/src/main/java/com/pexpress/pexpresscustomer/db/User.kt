package com.pexpress.pexpresscustomer.db

data class User(
    var id: Int? = null,
    var name: String? = null,
    var numberPhone: String?,
    var email: String? = null,
    var otp: Int? = null
)