package com.pexpress.pexpresscustomer.view.auth.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.pexpress.pexpresscustomer.model.ResponseAuth
import com.pexpress.pexpresscustomer.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthViewModel : ViewModel() {
    private val TAG = AuthViewModel::class.simpleName

    private val _auth = MutableLiveData<ResponseAuth?>()

    val auth: LiveData<ResponseAuth?> = _auth

    fun checkLogin(contact: String) {
        val client = ApiConfig.getApiService().login(contact)
        client.enqueue(object : Callback<ResponseAuth> {
            override fun onResponse(call: Call<ResponseAuth>, response: Response<ResponseAuth>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null) {
                        _auth.postValue(result)
                    }
                } else {
                    val error =
                        Gson().fromJson(response.errorBody()?.string(), ResponseAuth::class.java)
                    _auth.postValue(error)
                }
            }

            override fun onFailure(call: Call<ResponseAuth>, t: Throwable) {
                _auth.postValue(null)
            }
        })
    }

    fun register(params: HashMap<String, String>) {
        val client = ApiConfig.getApiService().register(params)
        client.enqueue(object : Callback<ResponseAuth> {
            override fun onResponse(call: Call<ResponseAuth>, response: Response<ResponseAuth>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null) {
                        _auth.postValue(result)
                    }
                } else {
                    val error =
                        Gson().fromJson(response.errorBody()?.string(), ResponseAuth::class.java)
                    _auth.postValue(error)
                }
            }

            override fun onFailure(call: Call<ResponseAuth>, t: Throwable) {
                _auth.postValue(null)
            }
        })
    }

    fun verifyOTP(params: HashMap<String, String>) {
        val client = ApiConfig.getApiService().verifyOTP(params)
        client.enqueue(object : Callback<ResponseAuth> {
            override fun onResponse(call: Call<ResponseAuth>, response: Response<ResponseAuth>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null) {
                        _auth.postValue(result)
                    }
                } else {
                    val error =
                        Gson().fromJson(response.errorBody()?.string(), ResponseAuth::class.java)
                    _auth.postValue(error)
                }
            }

            override fun onFailure(call: Call<ResponseAuth>, t: Throwable) {
                _auth.postValue(null)
            }
        })
    }

}