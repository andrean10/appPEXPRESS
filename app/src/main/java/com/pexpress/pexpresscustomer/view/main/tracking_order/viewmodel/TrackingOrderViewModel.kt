package com.pexpress.pexpresscustomer.view.main.tracking_order.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.pexpress.pexpresscustomer.model.tracking.ResponseTracking
import com.pexpress.pexpresscustomer.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TrackingOrderViewModel : ViewModel() {

    private val TAG = TrackingOrderViewModel::class.simpleName
    private var _tracking: MutableLiveData<ResponseTracking?>? = null

    fun trackingSearch(noTracking: String): LiveData<ResponseTracking?> {
        _tracking = MutableLiveData()
        getTrackingSearch(noTracking)
        return _tracking as MutableLiveData<ResponseTracking?>
    }

    private fun getTrackingSearch(noTracking: String) {
        val client = ApiConfig.getApiService().checkTracking(noTracking)
        client.enqueue(object : Callback<ResponseTracking> {
            override fun onResponse(
                call: Call<ResponseTracking>,
                response: Response<ResponseTracking>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    result.also {
                        _tracking!!.postValue(it)
                    }
                } else {
                    val error =
                        Gson().fromJson(
                            response.errorBody()?.string(),
                            ResponseTracking::class.java
                        )
                    _tracking!!.postValue(error)
                }
            }

            override fun onFailure(call: Call<ResponseTracking>, t: Throwable) {
                _tracking!!.postValue(null)
                t.printStackTrace()
                Log.d(TAG, "onFailure: ${t.printStackTrace()}")
            }
        })
    }
}