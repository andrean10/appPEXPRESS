package com.pexpress.pexpresscustomer.view.main.order.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.pexpress.pexpresscustomer.model.checkout.ResponseCheckPembayaran
import com.pexpress.pexpresscustomer.model.checkout.ResponseCheckoutUpdate
import com.pexpress.pexpresscustomer.model.checkout.asuransi.ResponseAsuransi
import com.pexpress.pexpresscustomer.model.type_payments.ResponseEWallet
import com.pexpress.pexpresscustomer.model.type_payments.ResponseVA
import com.pexpress.pexpresscustomer.model.type_payments.ResultEWallet
import com.pexpress.pexpresscustomer.model.type_payments.ResultVA
import com.pexpress.pexpresscustomer.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckoutViewModel : ViewModel() {

    var isReadyDataAsuransi = MutableLiveData<Boolean>()

    init {
        isReadyDataAsuransi.value = false
    }

    private var _asuransi = MutableLiveData<ResponseAsuransi?>()
    private val _va = MutableLiveData<ResponseVA?>()
    private val _ewallet = MutableLiveData<ResponseEWallet?>()

    private val _vaPayment = MutableLiveData<ResultVA?>()
    private val _ewalletPayment = MutableLiveData<ResultEWallet?>()

    private var _checkoutUpdate: MutableLiveData<ResponseCheckoutUpdate?>? = null

    private var _checkPembayaran: MutableLiveData<ResponseCheckPembayaran?>? = null

    fun setVAPayment(value: ResultVA) {
        _vaPayment.value = value
    }

    fun removeVAPayment() {
        _vaPayment.value = null
    }

    fun setEWalletPayment(value: ResultEWallet) {
        _ewalletPayment.value = value
    }

    fun removeEWalletPayment() {
        _ewalletPayment.value = null
    }

    fun checkoutAddAsuransi(params: HashMap<String, String>): LiveData<ResponseCheckoutUpdate?> {
        _checkoutUpdate = MutableLiveData()
        checkoutUpdate(params)
        return _checkoutUpdate as MutableLiveData<ResponseCheckoutUpdate?>
    }

    fun checkPembayaran(noInvoice: String): LiveData<ResponseCheckPembayaran?> {
        _checkPembayaran = MutableLiveData()
        pembayaran(noInvoice)
        return _checkPembayaran as MutableLiveData<ResponseCheckPembayaran?>
    }

    private fun checkoutUpdate(params: HashMap<String, String>) {
        val client = ApiConfig.getApiService().checkoutUpdate(params)
        client.enqueue(object : Callback<ResponseCheckoutUpdate> {
            override fun onResponse(
                call: Call<ResponseCheckoutUpdate>,
                response: Response<ResponseCheckoutUpdate>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    result.also {
                        _checkoutUpdate!!.postValue(it)
                    }
                } else {
                    val error =
                        Gson().fromJson(
                            response.errorBody()?.string(),
                            ResponseCheckoutUpdate::class.java
                        )
                    _checkoutUpdate!!.postValue(error)
                }
            }

            override fun onFailure(call: Call<ResponseCheckoutUpdate>, t: Throwable) {
                _checkoutUpdate!!.postValue(null)
            }
        })
    }

    fun asuransi() {
        val client = ApiConfig.getApiService().asuransi()
        client.enqueue(object : Callback<ResponseAsuransi> {
            override fun onResponse(
                call: Call<ResponseAsuransi>,
                response: Response<ResponseAsuransi>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    result.also {
                        _asuransi.postValue(it)
                        isReadyDataAsuransi.postValue(true)
                    }
                } else {
                    val error =
                        Gson().fromJson(
                            response.errorBody()?.string(),
                            ResponseAsuransi::class.java
                        )
                    _asuransi.postValue(error)
                }
            }

            override fun onFailure(call: Call<ResponseAsuransi>, t: Throwable) {
                _asuransi.postValue(null)
            }
        })
    }

    fun listVA() {
        val client = ApiConfig.getApiService().listVA()
        client.enqueue(object : Callback<ResponseVA> {
            override fun onResponse(
                call: Call<ResponseVA>,
                response: Response<ResponseVA>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    result.also {
                        _va.postValue(it)
                    }
                } else {
                    val error =
                        Gson().fromJson(response.errorBody()?.string(), ResponseVA::class.java)
                    _va.postValue(error)
                }
            }

            override fun onFailure(call: Call<ResponseVA>, t: Throwable) {
                _va.postValue(null)
            }
        })
    }

    fun listEWallet() {
        val client = ApiConfig.getApiService().listEWallet()
        client.enqueue(object : Callback<ResponseEWallet> {
            override fun onResponse(
                call: Call<ResponseEWallet>,
                response: Response<ResponseEWallet>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    result.also {
                        _ewallet.postValue(it)
                    }
                } else {
                    val error =
                        Gson().fromJson(response.errorBody()?.string(), ResponseEWallet::class.java)
                    _ewallet.postValue(error)
                }
            }

            override fun onFailure(call: Call<ResponseEWallet>, t: Throwable) {
                _ewallet.postValue(null)
            }
        })
    }

    private fun pembayaran(noInvoice: String) {
        val client = ApiConfig.getApiService().checkPembayaran(noInvoice)
        client.enqueue(object : Callback<ResponseCheckPembayaran> {
            override fun onResponse(
                call: Call<ResponseCheckPembayaran>,
                response: Response<ResponseCheckPembayaran>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    result.also {
                        _checkPembayaran?.postValue(it)
                    }
                } else {
                    val error =
                        Gson().fromJson(
                            response.errorBody()?.string(),
                            ResponseCheckPembayaran::class.java
                        )
                    _checkPembayaran?.postValue(error)
                }
            }

            override fun onFailure(call: Call<ResponseCheckPembayaran>, t: Throwable) {
                _checkPembayaran?.postValue(null)
            }
        })
    }

    val asuransi: LiveData<ResponseAsuransi?> = _asuransi

    val listVA: LiveData<ResponseVA?> = _va
    val listEWallet: LiveData<ResponseEWallet?> = _ewallet

    val vaPayment: LiveData<ResultVA?> = _vaPayment
    val ewalletPayment: LiveData<ResultEWallet?> = _ewalletPayment
}