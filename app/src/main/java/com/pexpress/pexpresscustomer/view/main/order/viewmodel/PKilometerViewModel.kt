package com.pexpress.pexpresscustomer.view.main.order.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.pexpress.pexpresscustomer.model.checkout.ResponseEditCheckout
import com.pexpress.pexpresscustomer.model.checkout.kilometer.ResponseCheckoutKilometer
import com.pexpress.pexpresscustomer.model.distance.ResponseDistance
import com.pexpress.pexpresscustomer.model.kilometer.ongkir.ResponseCheckOngkirKilometer
import com.pexpress.pexpresscustomer.model.order.ResultJenisBarang
import com.pexpress.pexpresscustomer.model.order.ResultJenisLayanan
import com.pexpress.pexpresscustomer.model.order.ResultJenisUkuran
import com.pexpress.pexpresscustomer.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PKilometerViewModel : ViewModel() {

    private val _stateInfoPengirim = MutableLiveData<Boolean>().apply {
        value = false
    }

    private val _stateInfoPenerima = MutableLiveData<Boolean>().apply {
        value = false
    }

    private val _stateInfoPickup = MutableLiveData<Boolean>().apply {
        value = false
    }

    private val _formAsalPengirim = MutableLiveData<HashMap<String, Any>>().apply {
        value = hashMapOf(
            "id_cabang_asal" to 0,
            "alamatpengirim" to "",
            "gkecpengirim" to "",
            "kecamatanpengirim" to ""
        )
    }
    private val _formLatLongPengirim = MutableLiveData<HashMap<String, String>>().apply {
        value = hashMapOf(
            "latpengirim" to "",
            "longpengirim" to ""
        )
    }
    private val _formAsalPenerima = MutableLiveData<HashMap<String, Any>>().apply {
        value = hashMapOf(
            "id_cabang_tujuan" to 0,
            "alamatpenerima" to "",
            "gkecpenerima" to "",
            "kecamatanpenerima" to ""
        )
    }
    private val _formLatLongPenerima = MutableLiveData<HashMap<String, String>>().apply {
        value = hashMapOf(
            "latpenerima" to "",
            "longpenerima" to ""
        )
    }
    private val _formJenisLayanan = MutableLiveData<ResultJenisLayanan>()
    private val _formUkuranBarang = MutableLiveData<ResultJenisUkuran>()
    private val _formJenisBarang = MutableLiveData<ResultJenisBarang>()

    private var _distance: MutableLiveData<ResponseDistance?>? = null
    private var _checkSubTotal = MutableLiveData<Boolean>()
    private var _cekOngkirKilometer: MutableLiveData<ResponseCheckOngkirKilometer?>? = null
    private var _checkout: MutableLiveData<ResponseCheckoutKilometer?>? = null
    private var _editCheckout: MutableLiveData<ResponseEditCheckout?>? = null

    private val _stateChangePaket = MutableLiveData<HashMap<String, Any>>().apply {
        value = hashMapOf(
            "id" to 0,
            "state" to false
        )
    }

    fun changeOrderPaket(id: Int, state: Boolean) {
        _stateChangePaket.value = hashMapOf(
            "id" to id,
            "state" to state
        )
    }

    fun setStateInfoPengirim(state: Boolean) {
        _stateInfoPengirim.value = state
    }

    fun setStateInfoPenerima(state: Boolean) {
        _stateInfoPenerima.value = state
    }

    fun setStateInfoPickup(state: Boolean) {
        _stateInfoPickup.value = state
    }

    fun setFormAsalPengirim(value: HashMap<String, Any>) {
        _formAsalPengirim.value = value
    }

    fun removeFormAsalPengirim() {
        _formAsalPengirim.value = hashMapOf(
            "id_cabang_asal" to 0,
            "alamatpengirim" to "",
            "gkecpengirim" to "",
            "kecamatanpengirim" to ""
        )
    }

    fun setFormLatLongPengirim(value: HashMap<String, String>) {
        _formLatLongPengirim.value = value
    }

    fun removeFormLatLongPengirim() {
        _formLatLongPengirim.value = hashMapOf(
            "latpengirim" to "",
            "longpengirim" to ""
        )
    }

    fun setFormAsalPenerima(value: HashMap<String, Any>) {
        _formAsalPenerima.value = value
    }

    fun removeFormAsalPenerima() {
        _formAsalPenerima.value = hashMapOf(
            "id_cabang_tujuan" to 0,
            "alamatpenerima" to "",
            "gkecpenerima" to "",
            "kecamatanpenerima" to ""
        )
    }

    fun setFormLatLongPenerima(value: HashMap<String, String>) {
        _formLatLongPenerima.value = value
    }

    fun removeFormLatLongPenerima() {
        _formLatLongPenerima.value = hashMapOf(
            "latpenerima" to "",
            "longpenerima" to ""
        )
    }

    fun setFormJenisLayanan(value: ResultJenisLayanan) {
        _formJenisLayanan.value = value
    }

    fun removeFormJenisLayanan() {
        _formJenisLayanan.value = ResultJenisLayanan()
    }

    fun setFormUkuranBarang(value: ResultJenisUkuran) {
        _formUkuranBarang.value = value
    }

    fun removeFormUkuranBarang() {
        _formUkuranBarang.value = ResultJenisUkuran()
    }

    fun setFormJenisBarang(value: ResultJenisBarang) {
        _formJenisBarang.value = value
    }

    fun removeFormJenisBarang() {
        _formJenisBarang.value = ResultJenisBarang()
    }

    fun checkStateSubTotal() {
        _checkSubTotal.value = _formAsalPengirim.value?.get("id_cabang_asal") != 0 &&
                _formAsalPenerima.value?.get("id_cabang_tujuan") != 0 &&
                _formJenisLayanan.value?.idlayanan != null &&
                _formUkuranBarang.value?.idjenisukuran != null
    }

    fun checkOngkirKilometer(params: HashMap<String, String>): LiveData<ResponseCheckOngkirKilometer?> {
        _cekOngkirKilometer = MutableLiveData()
        ongkirKilometer(params)
        return _cekOngkirKilometer as MutableLiveData<ResponseCheckOngkirKilometer?>
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

    fun checkout(params: HashMap<String, String>): LiveData<ResponseCheckoutKilometer?> {
        _checkout = MutableLiveData()

        val client = ApiConfig.getApiService().checkoutKilometer(params)
        client.enqueue(object : Callback<ResponseCheckoutKilometer> {
            override fun onResponse(
                call: Call<ResponseCheckoutKilometer>,
                response: Response<ResponseCheckoutKilometer>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    result.also {
                        _checkout!!.postValue(it)
                    }
                } else {
                    val error =
                        Gson().fromJson(
                            response.errorBody()?.string(),
                            ResponseCheckoutKilometer::class.java
                        )
                    _checkout!!.postValue(error)
                }
            }

            override fun onFailure(call: Call<ResponseCheckoutKilometer>, t: Throwable) {
                _checkout!!.postValue(null)
            }
        })
        return _checkout as MutableLiveData<ResponseCheckoutKilometer?>
    }

    fun editCheckout(id: Int, params: HashMap<String, String>): LiveData<ResponseEditCheckout?> {
        _editCheckout = MutableLiveData()
        val client = ApiConfig.getApiService().editCheckout(id, params)
        client.enqueue(object : Callback<ResponseEditCheckout> {
            override fun onResponse(
                call: Call<ResponseEditCheckout>,
                response: Response<ResponseEditCheckout>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    result.also {
                        _editCheckout!!.postValue(it)
                    }
                } else {
                    val error =
                        Gson().fromJson(
                            response.errorBody()?.string(),
                            ResponseEditCheckout::class.java
                        )
                    _editCheckout!!.postValue(error)
                }
            }

            override fun onFailure(call: Call<ResponseEditCheckout>, t: Throwable) {
                _editCheckout!!.postValue(null)
            }
        })
        return _editCheckout as MutableLiveData<ResponseEditCheckout?>
    }

    val stateInfoPengirim: LiveData<Boolean> = _stateInfoPengirim
    val stateInfoPenerima: LiveData<Boolean> = _stateInfoPenerima
    val stateInfoPickup: LiveData<Boolean> = _stateInfoPickup

    val formAsalPengirim: LiveData<HashMap<String, Any>> = _formAsalPengirim
    val formAsalPenerima: LiveData<HashMap<String, Any>> = _formAsalPenerima
    val formLatLongPengirim: LiveData<HashMap<String, String>> = _formLatLongPengirim
    val formLatLongPenerima: LiveData<HashMap<String, String>> = _formLatLongPenerima

    val formJenisLayanan: LiveData<ResultJenisLayanan> = _formJenisLayanan
    val formUkuranBarang: LiveData<ResultJenisUkuran> = _formUkuranBarang
    val formJenisBarang: LiveData<ResultJenisBarang> = _formJenisBarang

    val checkSubtotal: LiveData<Boolean> = _checkSubTotal

    val changeOrderPaket: LiveData<HashMap<String, Any>> = _stateChangePaket

}