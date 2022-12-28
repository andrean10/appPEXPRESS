package com.pexpress.pexpresscustomer.utils

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.util.Base64
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.appbar.MaterialToolbar
import com.pexpress.pexpresscustomer.R
import com.pexpress.pexpresscustomer.model.distance.Distance
import com.pexpress.pexpresscustomer.view.dialog.DialogLoadingFragment
import www.sanju.motiontoast.MotionToast
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

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

fun Distance.parseKm(): String {
    var newValue = ""
    val value = this.value ?: 0
    val text = this.text ?: ""

    with(text) {
        when {
            contains("km") -> {
                newValue = replace("km", "").trim()
            }
            contains("m") -> {
                val km = value.toDouble() / 1000
                val formatKm = String.format(Locale.US, "%.2f", km)
                newValue = formatKm
            }
        }
    }
    return newValue
}

fun roundingDistance(jarak: Double): String {
    val df = DecimalFormat("#")
    df.roundingMode = RoundingMode.UP
    return df.format(jarak)
}

fun Context.copyText(value: CharSequence) {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
    val clip = ClipData.newPlainText("label", value)
    clipboard?.setPrimaryClip(clip)
    Toast.makeText(this, "Teks disalin", Toast.LENGTH_SHORT).show()
}

fun Activity.moveToSAndK() {
    val uri = Uri.parse("https://sites.google.com/view/snk-insurance-pex/beranda")
    val intent = Intent(Intent.ACTION_VIEW, uri)
    startActivity(intent)
}

fun DialogLoadingFragment.loader(manager: FragmentManager, state: Boolean) {
    if (state) {
        show(manager, DialogLoadingFragment.TAG)
    } else {
        dismiss()
    }
}