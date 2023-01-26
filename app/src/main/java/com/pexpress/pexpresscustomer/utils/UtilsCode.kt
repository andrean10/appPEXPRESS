package com.pexpress.pexpresscustomer.utils

object UtilsCode {
    const val TAG = "AppDebug"

    const val TIME_DELAY_SCREEN = 3000L
    const val TIME_DELAY_OPEN_PICK_DATE = 4000L
    const val MODE_NIGHT = 1
    const val MODE_LIGHT = 0
    const val UNDEFINIED_MODE = -1

    const val PATTERN_DATE_VIEW = "dd MMMM yyyy"
    const val PATTERN_DATE_POST = "yyyy-MM-dd"
    const val PATTERN_DATE_FROM_API = "yyyy-MM-dd HH:mm:ss"
    const val PATTERN_DATE_FROM_API2 = "yyyy-MM-dd HH:mm"
    const val PATTERN_DATE_VIEW_STATUS_PEMBAYARAN = "dd MMMM yyyy HH:mm"
    const val PATTERN_DATE_VIEW_MILESTONE = "EE, dd MMMM yyyy HH:mm"
    const val PATTERN_TIME_VIEW_MILESTONE = "HH:mm"
    const val PATTERN_TIME = "HH:mm"

    const val FORM_PENGIRIM = 1
    const val FORM_PENERIMA = 2

    const val FORM_ASAL = 1
    const val FORM_TUJUAN = 2

    const val CONSTANT_TYPE_PACKAGE = "type_package"
    const val TYPE_PACKAGE_FIXRATE = 1
    const val TYPE_PACKAGE_KILOMETER = 2

    const val CONSTANT_TARIF_TYPE_PACKAGE = "tarif_type_package"
    const val TARIF_TYPE_PACKAGE_FIXRATE = 3
    const val TARIF_TYPE_PACKAGE_KILOMETER = 4

    const val TYPE_PACKAGE_FIXRATE_STRING = "fix_rate"
    const val TYPE_PACKAGE_KILOMETER_STRING = "kilometer"

    const val STATUS_PACKAGE_DELIVERED = 10
//    const val STATUS_PACKAGE_PROCCESS_PAYMENT = 11

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

    const val MILESTONE_PROCESS_PAYMENT = "11"
    const val MILESTONE_WAITING_FOR_PICKUP = "2"
    const val MILESTONE_PACKAGE_ALREADY_PICKUP = "3"
    const val MILESTONE_TRANSIT = "6"
    const val MILESTONE_PROCESS_TRANSIT = "26"
    const val MILESTONE_IN_TRANSIT = "27"
    const val MILESTONE_SHUTTLE = "7"
    const val MILESTONE_PROCESS_SHUTTLE = "1"
    const val MILESTONE_RELEASE = "8"
    const val MILESTONE_PROCESS_DELIVERY = "5"
    const val MILESTONE_NOT_DELIVERY = "30"
    const val MILESTONE_RETURNED_TO_SENDER = "31"
    const val MILESTONE_DELIVERY = "10"

    const val DEFAULT_TIMER = "00 : 00"
}