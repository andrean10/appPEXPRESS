package com.pexpress.pexpresscustomer.view.main.status_pembayaran.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.pexpress.pexpresscustomer.model.payment.ResponseDetailStatusPembayaran
import com.pexpress.pexpresscustomer.model.payment.ResponseStatusPembayaran
import com.pexpress.pexpresscustomer.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StatusPembayaranViewModel : ViewModel() {

    private val TAG = StatusPembayaranViewModel::class.simpleName

    private val _statusPembayaran = MutableLiveData<ResponseStatusPembayaran?>()
    private var _detailStatusPembayaran: MutableLiveData<ResponseDetailStatusPembayaran?>? = null

    fun getDetailStatusPembayaran(noInvoice: String): LiveData<ResponseDetailStatusPembayaran?> {
        _detailStatusPembayaran = MutableLiveData()
        detailStatusPembayaran(noInvoice)
        return _detailStatusPembayaran as MutableLiveData<ResponseDetailStatusPembayaran?>
    }

    fun statusPembayaran(idUser: String) {
        val client = ApiConfig.getApiService().statusPembayaran(idUser, "all")
        client.enqueue(object : Callback<ResponseStatusPembayaran> {
            override fun onResponse(
                call: Call<ResponseStatusPembayaran>,
                response: Response<ResponseStatusPembayaran>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    result.also {
                        _statusPembayaran.postValue(it)
                    }
                } else {
                    val error =
                        Gson().fromJson(
                            response.errorBody()?.string(),
                            ResponseStatusPembayaran::class.java
                        )
                    _statusPembayaran.postValue(error)
                }
            }

            override fun onFailure(call: Call<ResponseStatusPembayaran>, t: Throwable) {
                _statusPembayaran.postValue(null)
                t.printStackTrace()
                Log.d(TAG, "onFailure: ${t.printStackTrace()}")
            }
        })
    }

    fun detailStatusPembayaran(noInvoice: String) {
        val client = ApiConfig.getApiService().detailStatusPembayaran(noInvoice)
        client.enqueue(object : Callback<ResponseDetailStatusPembayaran> {
            override fun onResponse(
                call: Call<ResponseDetailStatusPembayaran>,
                response: Response<ResponseDetailStatusPembayaran>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    result.also {
                        _detailStatusPembayaran!!.postValue(it)
                    }
                } else {
                    val error =
                        Gson().fromJson(
                            response.errorBody()?.string(),
                            ResponseDetailStatusPembayaran::class.java
                        )
                    _detailStatusPembayaran!!.postValue(error)
                }
            }

            override fun onFailure(call: Call<ResponseDetailStatusPembayaran>, t: Throwable) {
                _detailStatusPembayaran!!.postValue(null)
                t.printStackTrace()
                Log.d(TAG, "onFailure: ${t.printStackTrace()}")
            }
        })
    }

    val statusPembayaran: LiveData<ResponseStatusPembayaran?> = _statusPembayaran
}