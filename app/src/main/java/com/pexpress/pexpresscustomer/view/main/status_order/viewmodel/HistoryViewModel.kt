package com.pexpress.pexpresscustomer.view.main.status_order.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.pexpress.pexpresscustomer.model.ResponseStatusOrder
import com.pexpress.pexpresscustomer.model.resi.milestone.ResponseMilestone
import com.pexpress.pexpresscustomer.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryViewModel : ViewModel() {

    private val _statusOrder = MutableLiveData<ResponseStatusOrder?>()
    private val _milestone = MutableLiveData<ResponseMilestone?>()

    private val TAG = HistoryViewModel::class.simpleName

    fun statusOrder(params: HashMap<String, String>) {
        val client = ApiConfig.getApiService().statusOrder(params)
        client.enqueue(object : Callback<ResponseStatusOrder> {
            override fun onResponse(
                call: Call<ResponseStatusOrder>,
                response: Response<ResponseStatusOrder>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    result.also {
                        _statusOrder.postValue(it)
                    }
                } else {
                    val error =
                        Gson().fromJson(
                            response.errorBody()?.string(),
                            ResponseStatusOrder::class.java
                        )
                    _statusOrder.postValue(error)
                }
            }

            override fun onFailure(call: Call<ResponseStatusOrder>, t: Throwable) {
                _statusOrder.postValue(null)
                t.printStackTrace()
                Log.d(TAG, "onFailure: ${t.printStackTrace()}")
            }
        })
    }

    fun milestone(id: Int) {
        val client = ApiConfig.getApiService().milestone(id)
        client.enqueue(object : Callback<ResponseMilestone> {
            override fun onResponse(
                call: Call<ResponseMilestone>,
                response: Response<ResponseMilestone>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    result.also {
                        _milestone.postValue(it)
                    }
                } else {
                    val error =
                        Gson().fromJson(
                            response.errorBody()?.string(),
                            ResponseMilestone::class.java
                        )
                    _milestone.postValue(error)
                }
            }

            override fun onFailure(call: Call<ResponseMilestone>, t: Throwable) {
                _milestone.postValue(null)
            }
        })
    }

    val statusOrder: LiveData<ResponseStatusOrder?> = _statusOrder
    val milestone: LiveData<ResponseMilestone?> = _milestone
}