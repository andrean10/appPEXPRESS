package com.pexpress.pexpresscustomer.utils

object UtilsCode {
    const val TAG = "AppDebug"

    const val TIME_DELAY_SCREEN = 3000L
    const val MODE_NIGHT = 1
    const val MODE_LIGHT = 0
    const val UNDEFINIED_MODE = -1

    const val PATTERN_DATE_VIEW = "dd MMMM yyyy"
    const val PATTERN_DATE_POST = "yyyy-MM-dd"
    const val PATTERN_DATE_FROM_API = "yyyy-MM-dd HH:mm:ss"
    const val PATTERN_DATE_VIEW_STATUS_PEMBAYARAN = "dd MMMM yyyy HH:mm"
    const val PATTERN_DATE_VIEW_MILESTONE = "E, dd MMMM yyyy"
    const val PATTERN_TIME_VIEW_MILESTONE = "HH:mm"

    const val FORM_PENGIRIM = 1
    const val FORM_PENERIMA = 2

    const val FORM_ASAL = 1
    const val FORM_TUJUAN = 2

    const val CONSTANT_TYPE_PACKAGE = "type_package"
    const val TYPE_PACKAGE_FIXRATE = 1
    const val TYPE_PACKAGE_KILOMETER = 2

    const val TYPE_PACKAGE_FIXRATE_STRING = "fix_rate"
    const val TYPE_PACKAGE_KILOMETER_STRING = "kilometer"

    const val STATUS_PACKAGE_DELIVERED = 10
    const val STATUS_PACKAGE_PROCCESS_PAYMENT = 11
    const val STATUS_PACKAGE_WAITING_FOR_PICKUP = 2

    const val CASH_PAYMENT_CODE = 1
    const val VA_PAYMENT_CODE = 2
    const val EWALLET_PAYMENT_CODE = 3
    const val QRIS_PAYMENT_CODE = 4

    const val EWALLET_OVO = "ID_OVO"
    const val EWALLET_DANA = "ID_DANA"
    const val EWALLET_LINKAJA = "ID_LINKAJA"
    const val EWALLET_SHOPEEPAY = "ID_SHOPEEPAY"
    const val EWALLET_SAKUKU = "ID_SAKUKU"
    const val EWALLET_PAYMAYA = "PH_PAYMAYA"
    const val EWALLET_GCASH = "PH_GCASH"

    const val REFRESH_TIME = 1000
}