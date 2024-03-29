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
        private const val KEY_USER_FULLNAME = "user_fullname"
        private const val KEY_USER_PROFILE_IMAGE = "user_profile_image"
        private const val KEY_LAUCH_APP = "user_launch_app"
        private const val KEY_LOGIN = "user_login"
    }

    fun setUser(values: User) {
        preferences.edit {
            putInt(KEY_USER_ID, values.idUser!!)
            putString(KEY_USER_FULLNAME, values.namaUser)
            putString(KEY_USER_PROFILE_IMAGE, values.fotoProfile)
        }
    }

    fun setLaunchApp() {
        preferences.edit { putBoolean(KEY_LAUCH_APP, false) }
    }

    fun setLogin(values: UtilsApplications) {
        preferences.edit {
            putBoolean(KEY_LOGIN, values.isLoginValid)
        }
    }

    fun getUser(): User {
        return User(
            idUser = preferences.getInt(KEY_USER_ID, 0),
            namaUser = preferences.getString(KEY_USER_FULLNAME, ""),
            fotoProfile = preferences.getString(KEY_USER_PROFILE_IMAGE, "")
        )
    }

    fun getLaunchApp() = UtilsApplications(
        isLaunchFirstApp = preferences.getBoolean(KEY_LAUCH_APP, true)
    )

    fun getLogin() = UtilsApplications(
        isLoginValid = preferences.getBoolean(KEY_LOGIN, false)
    )

    fun removeUser() {
        preferences.edit {
            remove(KEY_USER_ID)
            remove(KEY_USER_FULLNAME)
            remove(KEY_USER_PROFILE_IMAGE)
        }
    }

    fun removeLogin() {
        preferences.edit {
            remove(KEY_LOGIN)
        }
    }
}