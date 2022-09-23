package com.pexpress.pexpresscustomer.view.main.ongkir.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.pexpress.pexpresscustomer.model.distance.ResponseDistance
import com.pexpress.pexpresscustomer.model.fix_rate.ongkir.ResponseCheckOngkirFixRate
import com.pexpress.pexpresscustomer.model.kilometer.ongkir.ResponseCheckOngkirKilometer
import com.pexpress.pexpresscustomer.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OngkirViewModel : ViewModel() {

    private var _distance: MutableLiveData<ResponseDistance?>? = null
    private var _cekOngkirFixRate: MutableLiveData<ResponseCheckOngkirFixRate?>? = null
    private var _cekOngkirKilometer: MutableLiveData<ResponseCheckOngkirKilometer?>? = null

    fun checkOngkirFixRate(params: HashMap<String, String>): LiveData<ResponseCheckOngkirFixRate?> {
        _cekOngkirFixRate = MutableLiveData()
        ongkirFixRate(params)
        return _cekOngkirFixRate as MutableLiveData<ResponseCheckOngkirFixRate?>
    }

    fun checkOngkirKilometer(params: HashMap<String, String>): LiveData<ResponseCheckOngkirKilometer?> {
        _cekOngkirKilometer = MutableLiveData()
        ongkirKilometer(params)
        return _cekOngkirKilometer as MutableLiveData<ResponseCheckOngkirKilometer?>
    }

    private fun ongkirFixRate(params: HashMap<String, String>) {
        val client = ApiConfig.getApiService().checkOngkirFixRate(params)
        client.enqueue(object : Callback<ResponseCheckOngkirFixRate> {
            override fun onResponse(
                call: Call<ResponseCheckOngkirFixRate>,
                response: Response<ResponseCheckOngkirFixRate>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    result.also {
                        _cekOngkirFixRate!!.postValue(it)
                    }
                } else {
                    val error =
                        Gson().fromJson(
                            response.errorBody()?.string(),
                            ResponseCheckOngkirFixRate::class.java
                        )
                    _cekOngkirFixRate!!.postValue(error)
                }
            }

            override fun onFailure(call: Call<ResponseCheckOngkirFixRate>, t: Throwable) {
                _cekOngkirFixRate!!.postValue(null)
            }
        })
    }

    private fun ongkirKilometer(params: HashMap<String, String>) {
        val client = ApiConfig.getApiService().checkOngkirKilometer(params)
        client.enqueue(object : Callback<ResponseCheckOngkirKilometer> {
            override fun onResponse(
                call: Call<ResponseCheckOngkirKilometer>,
                response: Response<ResponseCheckOngkirKilometer>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    result.also {
                        _cekOngkirKilometer!!.postValue(it)
                    }
                } else {
                    val error =
                        Gson().fromJson(
                            response.errorBody()?.string(),
                            ResponseCheckOngkirKilometer::class.java
                        )
                    _cekOngkirKilometer!!.postValue(error)
                }
            }

            override fun onFailure(call: Call<ResponseCheckOngkirKilometer>, t: Throwable) {
                _cekOngkirKilometer!!.postValue(null)
            }
        })
    }

    fun checkDistance(params: HashMap<String, String>): LiveData<ResponseDistance?> {
        _distance = MutableLiveData()
        val client = ApiConfig.getApiService(true).checkDirections(params)
        client.enqueue(object : Callback<ResponseDistance> {
            override fun onResponse(
                call: Call<ResponseDistance>,
                response: Response<ResponseDistance>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    result.also {
                        _distance?.postValue(it)
                    }
                } else {
                    _distance?.postValue(ResponseDistance(status = "FAILED"))
                }
            }

            override fun onFailure(call: Call<ResponseDistance>, t: Throwable) {
                _distance?.postValue(null)
            }
        })
        return _distance as MutableLiveData<ResponseDistance?>
    }
}