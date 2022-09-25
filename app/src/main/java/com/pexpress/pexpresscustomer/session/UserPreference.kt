package com.pexpress.pexpresscustomer.session

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.pexpress.pexpresscustomer.db.User
import com.pexpress.pexpresscustomer.db.UtilsApplications

internal class UserPreference(context: Context) {
    private var preferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "user_pref"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_DEVICE_ID = "device_id"
        private const val KEY_USER_FULLNAME = "user_fullname"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_OTP = "user_otp"
        private const val KEY_USER_CONTACT = "user_contact"
        private const val KEY_LAUCH_APP = "user_launch_app"
        private const val KEY_LOGIN = "user_login"
    }

    fun setLaunchApp() {
        preferences.edit { putBoolean(KEY_LAUCH_APP, false) }
    }

    fun setUser(values: User) {
        preferences.edit {
            putInt(KEY_USER_ID, values.id ?: 0)
            putString(KEY_USER_FULLNAME, values.name)
            putString(KEY_USER_CONTACT, values.numberPhone)
            putString(KEY_USER_EMAIL, values.email)
            putInt(KEY_USER_OTP, values.otp ?: 0)
        }
    }

    fun setLogin(values: UtilsApplications) {
        preferences.edit {
            putBoolean(KEY_LOGIN, values.isLoginValid)
        }
    }

    fun setDeviceId(value: String) {
        preferences.edit {
            putString(KEY_USER_DEVICE_ID, value)
        }
    }

    fun getLaunchApp() = UtilsApplications(
        isLaunchFirstApp = preferences.getBoolean(KEY_LAUCH_APP, true)
    )

    fun getUser(): User {
        return User(
            id = preferences.getInt(KEY_USER_ID, 0),
            name = preferences.getString(KEY_USER_FULLNAME, ""),
            numberPhone = preferences.getString(KEY_USER_CONTACT, ""),
            email = preferences.getString(KEY_USER_EMAIL, ""),
            otp = preferences.getInt(KEY_USER_OTP, 0)
        )
    }

    fun getDeviceId() = preferences.getString(KEY_USER_DEVICE_ID, "")

    fun getLogin() = UtilsApplications(
        isLoginValid = preferences.getBoolean(KEY_LOGIN, false)
    )

    fun removeUser() {
        preferences.edit {
            remove(KEY_USER_ID)
            remove(KEY_USER_FULLNAME)
            remove(KEY_USER_CONTACT)
            remove(KEY_USER_EMAIL)
            remove(KEY_USER_OTP)
        }
    }

    fun removeDeviceId() {
        preferences.edit {
            remove(KEY_USER_DEVICE_ID)
        }
    }

    fun removeLogin() {
        preferences.edit {
            remove(KEY_LOGIN)
        }
    }
}