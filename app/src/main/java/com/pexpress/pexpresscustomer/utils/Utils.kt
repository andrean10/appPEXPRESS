package com.pexpress.pexpresscustomer.utils

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.util.Base64
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentActivity
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.appbar.MaterialToolbar
import com.pexpress.pexpresscustomer.R
import www.sanju.motiontoast.MotionToast
import java.text.NumberFormat
import java.util.*
import kotlin.math.PI
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

fun showMessage(
    activity: Activity,
    title: String,
    message: String = "Cek Koneksi Internet Dan Coba Lagi!",
    style: String,
) {
    MotionToast.createColorToast(
        activity,
        title,
        message,
        style,
        MotionToast.GRAVITY_BOTTOM,
        MotionToast.LONG_DURATION,
        ResourcesCompat.getFont(activity, R.font.helvetica_regular)
    )
}

fun isLoading(
    state: Boolean,
    pb: ProgressBar? = null,
    pbLottie: LottieAnimationView? = null,
    isClickButton: Boolean = false,
    tv: TextView? = null,
) {
    if (isClickButton) {
        if (state) {
            pb?.visibility = VISIBLE
            pbLottie?.visibility = VISIBLE
            tv!!.visibility = GONE
        } else {
            pb?.visibility = GONE
            pbLottie?.visibility = GONE
            tv!!.visibility = VISIBLE
        }
    } else {
        if (state) {
            pb?.visibility = VISIBLE
            pbLottie?.visibility = VISIBLE
        } else {
            pb?.visibility = GONE
            pbLottie?.visibility = GONE
        }
    }
}

//fun isLoadingPercentageProgress(state: Boolean, pb: ProgressBar, tv: TextView) {
//    if (state) {
//        pb.visibility = VISIBLE
//        tv.visibility = GONE
//    } else {
//        pb.visibility = GONE
//        tv.visibility = VISIBLE
//    }
//}

//fun isLoadingImage(state: Boolean, pb: ProgressBar) {
//    if (state) {
//        pb.visibility = VISIBLE
//    } else {
//        pb.visibility = GONE
//    }
//}

//fun listenerImages(pb: ProgressBar? = null, pbLottie: LottieAnimationView? = null) =
//    object : RequestListener<Drawable> {
//        override fun onResourceReady(
//            resource: Drawable?,
//            model: Any?,
//            target: Target<Drawable>?,
//            dataSource: DataSource?,
//            isFirstResource: Boolean,
//        ): Boolean {
//            pb?.visibility = GONE
//            pbLottie?.visibility = GONE
//            return false
//        }
//
//        override fun onLoadFailed(
//            e: GlideException?,
//            model: Any?,
//            target: Target<Drawable>?,
//            isFirstResource: Boolean,
//        ): Boolean {
//            pb?.visibility = GONE
//            pbLottie?.visibility = GONE
//            return false
//        }
//    }

// loading in bottomsheet
//fun loadingInBottomSheet(btnSave: Button, progressBarSheet: ProgressBar, isLoading: Boolean) {
//    if (isLoading) {
//        btnSave.visibility = View.INVISIBLE
//        progressBarSheet.visibility = VISIBLE
//    } else {
//        btnSave.visibility = GONE
//        progressBarSheet.visibility = GONE
//    }
//}

//fun createPartFromString(descriptionString: String): RequestBody {
//    return descriptionString.toRequestBody(MultipartBody.FORM)
//}

//fun reqFileImage(path: String?, name: String): MultipartBody.Part {
//    val fileImage = File(path!!)
//    val reqFileImage =
//        fileImage.asRequestBody("image/jpeg/jpg/png".toMediaTypeOrNull())
//    return MultipartBody.Part.createFormData(
//        name, fileImage.name, reqFileImage
//    )
//}

//fun reqFileImageEmpty(name: String): MultipartBody.Part {
//    val reqFileImage = ""
//        .toRequestBody("image/jpeg/jpg/png".toMediaTypeOrNull())
//    return MultipartBody.Part.createFormData(name, "", reqFileImage)
//}

fun setVisibilityBottomHead(activity: Activity, state: Boolean) {
    activity.findViewById<MaterialToolbar>(R.id.toolbar_main).visibility =
        if (state) VISIBLE else GONE
}

fun checkSystemMode(context: Context): Int {
    return when (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
        Configuration.UI_MODE_NIGHT_YES -> UtilsCode.MODE_NIGHT
        Configuration.UI_MODE_NIGHT_NO -> UtilsCode.MODE_LIGHT
        Configuration.UI_MODE_NIGHT_UNDEFINED -> UtilsCode.UNDEFINIED_MODE
        else -> -2
    }
}

fun closeKeyboard(fragmentActivity: FragmentActivity) {
    val view = fragmentActivity.currentFocus
    if (view != null) {
        val inputMethodManager: InputMethodManager =
            fragmentActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun formatRupiah(number: Double): String {
    val localeID = Locale("in", "ID")
    val numberFormat = NumberFormat.getCurrencyInstance(localeID)
    val formatRupiah = numberFormat.format(number)
    val split = formatRupiah.split(",")
    val length = split[0].length
    return split[0].substring(0, 2) + ". " + split[0].substring(2, length)
}

fun formatRegexRupiah(value: String): String {
    return value.replace("Rp. ", "")
}

fun normalizedNumber(number: String): String {
    return when {
        number.contains("+62") -> number.replace("+62", "")
        number.contains("0") -> {
            number.replaceFirst("0", "")
        }
        else -> number
    }
}

fun normalizedNumber2(number: String): String {
    return if (number.contains("+62")) {
        number.replace("+62", "0")
    } else {
        number
    }
}

// algoritma heversine
fun distance(
    latOrigin: Double,
    longOrigin: Double,
    latDestination: Double,
    longDestination: Double,
): String {
    val R = 6371 // radian bumi bulat
    // hitung perbedaan atau selisih longitude asal dan tujuan
    val longDiff = longOrigin - longDestination
    // hitung jarak
    var distance =
        sin(deg2rad(latOrigin)) *
                sin(deg2rad(latDestination)) +
                cos(deg2rad(latOrigin)) *
                cos(deg2rad(latDestination)) *
                cos(deg2rad(longDiff))
    distance = acos(distance)

    // konversi jarak radian ke bujur
//        distance = rad2deg(distance)
    // jarak dalam km
    val distanceKm = distance * R
    // return distance km
    return String.format(Locale.US, "%.2f", distanceKm)
}

// konversi radian ke bujur
//private fun rad2deg(distance: Double) = (distance * 180.0 / PI)

// konversi bujur ke radian
private fun deg2rad(lat1: Double) = (lat1 * PI / 180.0)

fun getAuthToken(key: String): String {
    val authKey = "$key:"
    val byteData = authKey.toByteArray()
    val base64 = Base64.encodeToString(byteData, Base64.NO_WRAP)
    return "Basic $base64"
}

val String.capitalizeEachWords
    get() = this.split(" ").joinToString(" ") {
        it.replaceFirstChar { c ->
            if (c.isLowerCase()) {
                c.titlecase(Locale.getDefault())
            } else {
                c.toString()
            }
        }
    }