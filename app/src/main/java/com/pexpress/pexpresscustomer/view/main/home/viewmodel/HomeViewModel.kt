package com.pexpress.pexpresscustomer.view.main.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.pexpress.pexpresscustomer.model.main.ResponseBanner
import com.pexpress.pexpresscustomer.model.profile.ResponseProfile
import com.pexpress.pexpresscustomer.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private var _banner: MutableLiveData<ResponseBanner?> = MutableLiveData()
    private var _profile: MutableLiveData<ResponseProfile?>? = null

    fun getBanner(): LiveData<ResponseBanner?> {
        banner()
        return _banner
    }

    fun getProfile(contact: String): LiveData<ResponseProfile?> {
        _profile = MutableLiveData()
        myProfile(contact)
        return _profile as MutableLiveData<ResponseProfile?>
    }

    private fun banner() {
        val client = ApiConfig.getApiService().banner()
        client.enqueue(object : Callback<ResponseBanner> {
            override fun onResponse(
                call: Call<ResponseBanner>,
                response: Response<ResponseBanner>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    result.also {
                        _banner.postValue(it)
                    }
                } else {
                    val error =
                        Gson().fromJson(response.errorBody()?.string(), ResponseBanner::class.java)
                    _banner.postValue(error)
                }
            }

            override fun onFailure(call: Call<ResponseBanner>, t: Throwable) {
                _banner.postValue(null)
            }
        })
    }

    private fun myProfile(contact: String) {
        val client = ApiConfig.getApiService().profile(contact)
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
                        Gson().fromJson(response.errorBody()?.string(), ResponseProfile::class.java)
                    _profile!!.postValue(error)
                }
            }

            override fun onFailure(call: Call<ResponseProfile>, t: Throwable) {
                _profile!!.postValue(null)
            }
        })
    }
}