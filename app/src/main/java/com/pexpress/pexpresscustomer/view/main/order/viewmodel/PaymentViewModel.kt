package com.pexpress.pexpresscustomer.view.main.order.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.pexpress.pexpresscustomer.model.ResponseCreateVA
import com.pexpress.pexpresscustomer.model.type_payments.ResponseCreateCash
import com.pexpress.pexpresscustomer.model.type_payments.ResponseCreateEWallet
import com.pexpress.pexpresscustomer.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaymentViewModel : ViewModel() {

    private var _createCash: MutableLiveData<ResponseCreateCash?>? = null
    private var _createVa: MutableLiveData<ResponseCreateVA?>? = null
    private var _createEWallet: MutableLiveData<ResponseCreateEWallet?>? = null

    fun createCash(
        authorization: String,
        params: HashMap<String, String>
    ): LiveData<ResponseCreateCash?> {
        _createCash = MutableLiveData()
        cash(authorization, params)
        return _createCash as MutableLiveData<ResponseCreateCash?>
    }

    fun createVA(
        authorization: String,
        params: HashMap<String, String>
    ): LiveData<ResponseCreateVA?> {
        _createVa = MutableLiveData()
        va(authorization, params)
        return _createVa as MutableLiveData<ResponseCreateVA?>
    }

    fun createEWallet(
        authorization: String,
        params: HashMap<String, String>
    ): LiveData<ResponseCreateEWallet?> {
        _createEWallet = MutableLiveData()
        eWallet(authorization, params)
        return _createEWallet as MutableLiveData<ResponseCreateEWallet?>
    }

    private fun cash(authorization: String, params: HashMap<String, String>) {
        val client = ApiConfig.getApiService().createCash(authorization, params)
        client.enqueue(object : Callback<ResponseCreateCash> {
            override fun onResponse(
                call: Call<ResponseCreateCash>,
                response: Response<ResponseCreateCash>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    result.also {
                        _createCash?.postValue(it)
                    }
                } else {
                    val error =
                        Gson().fromJson(
                            response.errorBody()?.string(),
                            ResponseCreateCash::class.java
                        )
                    _createCash?.postValue(error)
                }
            }

            override fun onFailure(call: Call<ResponseCreateCash>, t: Throwable) {
                _createCash?.postValue(null)
            }
        })
    }

    private fun va(authorization: String, params: HashMap<String, String>) {
        val client = ApiConfig.getApiService().createVA(authorization, params)
        client.enqueue(object : Callback<ResponseCreateVA> {
            override fun onResponse(
                call: Call<ResponseCreateVA>,
                response: Response<ResponseCreateVA>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    result.also {
                        _createVa?.postValue(it)
                    }
                } else {
                    val error =
                        Gson().fromJson(
                            response.errorBody()?.string(),
                            ResponseCreateVA::class.java
                        )
                    _createVa?.postValue(error)
                }
            }

            override fun onFailure(call: Call<ResponseCreateVA>, t: Throwable) {
                _createVa?.postValue(null)
            }
        })
    }

    private fun eWallet(authorization: String, params: HashMap<String, String>) {
        val client = ApiConfig.getApiService().createEWallet(authorization, params)
        client.enqueue(object : Callback<ResponseCreateEWallet> {
            override fun onResponse(
                call: Call<ResponseCreateEWallet>,
                response: Response<ResponseCreateEWallet>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    result.also {
                        _createEWallet?.postValue(it)
                    }
                } else {
                    val error =
                        Gson().fromJson(
                            response.errorBody()?.string(),
                            ResponseCreateEWallet::class.java
                        )
                    _createEWallet?.postValue(error)
                }
            }

            override fun onFailure(call: Call<ResponseCreateEWallet>, t: Throwable) {
                _createEWallet?.postValue(null)
            }
        })
    }

}