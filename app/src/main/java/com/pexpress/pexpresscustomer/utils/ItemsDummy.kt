package com.pexpress.pexpresscustomer.utils

import com.pexpress.pexpresscustomer.R
import com.pexpress.pexpresscustomer.db.HistoryOrders
import com.pexpress.pexpresscustomer.db.ItemBank
import com.pexpress.pexpresscustomer.db.ItemSize
import com.pexpress.pexpresscustomer.db.ServiceType

object ItemsDummy {
    val listSlider = listOf(
        R.drawable.dummy_img_slider,
        R.drawable.dummy_img_slider2,
        R.drawable.dummy_img_slider3
    )

    val listHistoryOrder = listOf(
        HistoryOrders(
            resi = "JKT1291499",
            status = "Sedang Diproses",
            image = R.drawable.ic_order_process
        ),
        HistoryOrders(
            resi = "JKT1231411",
            status = "Sedang Diproses",
            image = R.drawable.ic_order_process
        ),
        HistoryOrders(
            resi = "BDG1231041",
            status = "Sedang Dalam Perjalanan",
            image = R.drawable.ic_order_delivery
        ),
        HistoryOrders(
            resi = "PKU0012311",
            status = "Sedang Dalam Perjalanan",
            image = R.drawable.ic_order_delivery
        ),
        HistoryOrders(
            resi = "BDG1231041",
            status = "Sedang Dalam Perjalanan",
            image = R.drawable.ic_order_delivery
        ),
        HistoryOrders(
            resi = "PKU0012311",
            status = "Sedang Dalam Perjalanan",
            image = R.drawable.ic_order_delivery
        )
    )

    val listServiceType = listOf(
//        ServiceType(
//            serviceType = "REGULAR",
//            estimation = 2
//        ),
        ServiceType(
            serviceType = "NEXTDAY",
            estimation = 3
        ),
    )

    val listItemSize = listOf(
        ItemSize(
            nameItemSize = "Small",
            dimenLength = 20,
            dimenWidth = 20,
            dimenHeight = 20,
            dimenWeight = 5
        ),
//        ItemSize(
//            nameItemSize = "Medium",
//            dimenLength = 30,
//            dimenWidth = 30,
//            dimenHeight = 30,
//            dimenWeight = 6
//        ),
    )

    val listItemBank = listOf(
        ItemBank(
            nameBank = "Bank Rakyat Indonesia",
            codeBank = "BRI",
            statusBank = true,
            imageBank = ""
        ),
        ItemBank(
            nameBank = "Bank Negara Indonesia",
            codeBank = "BNI",
            statusBank = true,
            imageBank = ""
        ),
        ItemBank(
            nameBank = "Bank Central Asia",
            codeBank = "BCA",
            statusBank = false,
            imageBank = ""
        ),
        ItemBank(
            nameBank = "Bank Mandiri",
            codeBank = "MANDIRI",
            statusBank = true,
            imageBank = ""
        )
    )

//    val listHistoryOrder: List<HistoryOrders?>? = null
}