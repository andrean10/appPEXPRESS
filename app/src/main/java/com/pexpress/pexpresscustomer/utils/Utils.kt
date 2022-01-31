package com.pexpress.pexpresscustomer.utils

import android.app.Activity
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.pexpress.pexpresscustomer.R
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import www.sanju.motiontoast.MotionToast
import java.io.File
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun View.snackbar(message: String, backgroundColor: Int, textColor: Int) {
    Snackbar.make(
        this,
        message,
        Snackbar.LENGTH_LONG
    ).also { snackbar ->
//        snackbar.setTextColor(textColor)
//        snackbar.setAction("OKE") {
//            snackbar.dismiss()
//        }
        snackbar.view.apply {
            setBackgroundColor(backgroundColor)
        }
    }.show()
}

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

fun checkValue(value: String, ti: TextInputEditText, errorMessage: String): Boolean {
    if (value.isEmpty()) {
        ti.error = errorMessage
        return false
    } else {
        ti.error = null
    }
    return true
}

//fun checkSpinnerValue(value: Int, sp: SmartMaterialSpinner<*>, errorMessage: String): Boolean {
//    if (value == 0) {
//        sp.errorText = errorMessage
//        return false
//    } else {
//        sp.errorText = null
//    }
//    return true
//}

fun checkValueBottomSheet(
    value: String?,
    edtInput: TextInputLayout,
    messageError: String,
): Boolean {
    if (value.isNullOrEmpty()) {
        edtInput.error = messageError
        return false
    }
    return true
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
            pb?.visibility = View.VISIBLE
            pbLottie?.visibility = View.VISIBLE
            tv!!.visibility = View.GONE
        } else {
            pb?.visibility = View.GONE
            pbLottie?.visibility = View.GONE
            tv!!.visibility = View.VISIBLE
        }
    } else {
        if (state) {
            pb?.visibility = View.VISIBLE
            pbLottie?.visibility = View.VISIBLE
        } else {
            pb?.visibility = View.GONE
            pbLottie?.visibility = View.GONE
        }
    }
}

fun isLoadingPercentageProgress(state: Boolean, pb: ProgressBar, tv: TextView) {
    if (state) {
        pb.visibility = View.VISIBLE
        tv.visibility = View.GONE
    } else {
        pb.visibility = View.GONE
        tv.visibility = View.VISIBLE
    }
}

fun isLoadingImage(state: Boolean, pb: ProgressBar) {
    if (state) {
        pb.visibility = View.VISIBLE
    } else {
        pb.visibility = View.GONE
    }
}

fun listenerImages(pb: ProgressBar? = null, pbLottie: LottieAnimationView? = null) =
    object : RequestListener<Drawable> {
        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean,
        ): Boolean {
            pb?.visibility = View.GONE
            pbLottie?.visibility = View.GONE
            return false
        }

        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean,
        ): Boolean {
            pb?.visibility = View.GONE
            pbLottie?.visibility = View.GONE
            return false
        }
    }

// loading in bottomsheet
fun loadingInBottomSheet(btnSave: Button, progressBarSheet: ProgressBar, isLoading: Boolean) {
    if (isLoading) {
        btnSave.visibility = View.INVISIBLE
        progressBarSheet.visibility = View.VISIBLE
    } else {
        btnSave.visibility = View.GONE
        progressBarSheet.visibility = View.GONE
    }
}

fun createPartFromString(descriptionString: String): RequestBody {
    return descriptionString.toRequestBody(MultipartBody.FORM)
}

fun reqFileImage(path: String?, name: String): MultipartBody.Part {
    val fileImage = File(path!!)
    val reqFileImage =
        fileImage.asRequestBody("image/jpeg/jpg/png".toMediaTypeOrNull())
    return MultipartBody.Part.createFormData(
        name, fileImage.name, reqFileImage
    )
}

fun reqFileImageEmpty(name: String): MultipartBody.Part {
    val reqFileImage = ""
        .toRequestBody("image/jpeg/jpg/png".toMediaTypeOrNull())
    return MultipartBody.Part.createFormData(name, "", reqFileImage)
}

fun outputDateFormat(pattern: String): SimpleDateFormat {
    return SimpleDateFormat(pattern, Locale.getDefault())
}

fun formateDate(oldPattern: String, date: String, newPattern: String): String {
    var formattedTime = ""
    val sdf = SimpleDateFormat(oldPattern, Locale.getDefault())
//    sdf.timeZone = TimeZone.getTimeZone("UTC")

    try {
        val parseDate = sdf.parse(date)
        formattedTime = SimpleDateFormat(newPattern, Locale.getDefault()).format(parseDate)
    } catch (e: ParseException) {
        Log.e("TAG", "formateDate: ${e.printStackTrace()}")
    }
    return formattedTime
}

fun miliSecondToTimer(milliSeconds: Long): String {
    var timerString = ""
    var secondString = ""

    val hours: Int = ((milliSeconds / (1000 * 60 * 60)).toInt())
    val minutes: Int = (milliSeconds % (1000 * 60 * 60)).toInt() / (1000 * 60)
    val seconds: Int = ((milliSeconds % (1000 * 60 * 60) % (1000 * 60) / 1000).toInt())

    timerString = if (hours > 0) {
        "$hours:"
    } else {
        "0"
    }

    secondString = if (seconds < 10) {
        "0$seconds"
    } else {
        "" + seconds
    }

    timerString = "$timerString$minutes:$secondString"
    return timerString
}

fun hideViewBottomNav(activity: Activity) {
    activity.findViewById<BottomNavigationView>(R.id.nav_view).visibility = View.GONE
}

val outputDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).apply {
    timeZone = TimeZone.getTimeZone("UTC")
}