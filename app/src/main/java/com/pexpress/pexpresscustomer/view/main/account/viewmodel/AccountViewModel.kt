package com.pexpress.pexpresscustomer.view.main.account.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.pexpress.pexpresscustomer.model.auth.session.ResponseSession
import com.pexpress.pexpresscustomer.model.profile.ResponseManageProfile
import com.pexpress.pexpresscustomer.model.profile.ResponseProfile
import com.pexpress.pexpresscustomer.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccountViewModel : ViewModel() {

    private var _profile: MutableLiveData<ResponseProfile?>? = null
    private var _updateProfile: MutableLiveData<ResponseManageProfile?>? = null
    private var _logoutSession: MutableLiveData<ResponseSession?>? = null

    fun profile(numberPhone: String): LiveData<ResponseProfile?> {
        _profile = MutableLiveData()
        detailProfile(numberPhone)
        return _profile as MutableLiveData<ResponseProfile?>
    }

    fun updateProfile(params: HashMap<String, String>): LiveData<ResponseManageProfile?> {
        _updateProfile = MutableLiveData()
        editProfile(params)
        return _updateProfile as MutableLiveData<ResponseManageProfile?>
    }

    fun logout(idUser: String): LiveData<ResponseSession?> {
        _logoutSession = MutableLiveData()
        logoutSession(idUser)
        return _logoutSession as MutableLiveData<ResponseSession?>
    }

    private fun detailProfile(numberPhone: String) {
        val client = ApiConfig.getApiService().profile(numberPhone)
        client.enqueue(object : Callback<ResponseProfile> {
            override fun onResponse(
                call: Call<ResponseProfile>,
                response: Response<ResponseProfile>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    result.also {
                        _profile!!.postValue(it)
                    }
                } else {
                    val error =
                        Gson().fromJson(
                            response.errorBody()?.string(),
                            ResponseProfile::class.java
                        )
                    _profile!!.postValue(error)
                }
            }

            override fun onFailure(call: Call<ResponseProfile>, t: Throwable) {
                _profile!!.postValue(null)
            }
        })
    }

    private fun editProfile(params: HashMap<String, String>) {
        val client = ApiConfig.getApiService().updateProfile(params)
        client.enqueue(object : Callback<ResponseManageProfile> {
            override fun onResponse(
                call: Call<ResponseManageProfile>,
                response: Response<ResponseManageProfile>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    result.also {
                        _updateProfile!!.postValue(it)
                    }
                } else {
                    val error =
                        Gson().fromJson(
                            response.errorBody()?.string(),
                            ResponseManageProfile::class.java
                        )
                    _updateProfile!!.postValue(error)
                }
            }

            override fun onFailure(call: Call<ResponseManageProfile>, t: Throwable) {
                _updateProfile!!.postValue(null)
            }
        })
    }

    private fun logoutSession(idUser: String) {
        val client = ApiConfig.getApiService().logoutSession(idUser)
        client.enqueue(object : Callback<ResponseSession> {
            override fun onResponse(
                call: Call<ResponseSession>,
                response: Response<ResponseSession>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    result.also {
                        _logoutSession!!.postValue(it)
                    }
                } else {
                    val error =
                        Gson().fromJson(
                            response.errorBody()?.string(),
                            ResponseSession::class.java
                        )
                    _logoutSession!!.postValue(error)
                }
            }

            override fun onFailure(call: Call<ResponseSession>, t: Throwable) {
                _logoutSession!!.postValue(null)
            }
        })
    }
}