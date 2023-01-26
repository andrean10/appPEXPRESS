package com.pexpress.pexpresscustomer.view.main.order.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.pexpress.pexpresscustomer.model.checkout.fix_rate.ResponseCheckout
import com.pexpress.pexpresscustomer.model.checkout.fix_rate.ResponseEditCheckoutFixRate
import com.pexpress.pexpresscustomer.model.checkout.hari_libur.ResponseCheckHariLibur
import com.pexpress.pexpresscustomer.model.diskon.ResponseCheckDiskon
import com.pexpress.pexpresscustomer.model.fix_rate.ongkir.ResponseCheckOngkirFixRate
import com.pexpress.pexpresscustomer.model.order.ResponseCheckCutOff
import com.pexpress.pexpresscustomer.model.order.ResultJenisBarang
import com.pexpress.pexpresscustomer.model.order.ResultJenisLayanan
import com.pexpress.pexpresscustomer.model.order.ResultJenisUkuran
import com.pexpress.pexpresscustomer.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PFixRateViewModel : ViewModel() {

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
            "placeId" to "",
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
            "placeId" to "",
            "latpenerima" to "",
            "longpenerima" to ""
        )
    }

    private val _formJenisLayanan = MutableLiveData<ResultJenisLayanan>()
    private val _formUkuranBarang = MutableLiveData<ResultJenisUkuran>()
    private val _formJenisBarang = MutableLiveData<ResultJenisBarang>()

    private var _checkStateCutOff = MutableLiveData<HashMap<String, Boolean>?>()
    private var _checkStateSubTotal = MutableLiveData<Boolean>()
    private var _checkStateDiskon = MutableLiveData<Boolean>()

    private var _checkCutOff: MutableLiveData<ResponseCheckCutOff?>? = null
    private var _checkHariLibur: MutableLiveData<ResponseCheckHariLibur?>? = null
    private var _checkDiskonFixRate: MutableLiveData<ResponseCheckDiskon?>? = null
    private var _cekOngkirFixRate: MutableLiveData<ResponseCheckOngkirFixRate?>? = null

    private var _checkout: MutableLiveData<ResponseCheckout?>? = null
    private var _editCheckout: MutableLiveData<ResponseEditCheckoutFixRate?>? = null

    private val _stateChangePaket = MutableLiveData<HashMap<String, Any>>().apply {
        value = hashMapOf(
            "id" to 0,
            "state" to false
        )
    }

    fun changeOrderPaket(id: Int = 0, state: Boolean) {
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

    fun setStateCutOff() {
        _checkStateCutOff.value = null
    }

    fun setStateSubTotal(state: Boolean) {
        _checkStateSubTotal.value = state
    }

    fun setStateDiskon(state: Boolean) {
        _checkStateDiskon.value = state
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

    fun checkStateCutOff(isFromPickDate: Boolean = false) {
        _checkStateCutOff.value = hashMapOf(
            "isFromPickDate" to isFromPickDate,
            "jenisLayanan" to (_formJenisLayanan.value?.idlayanan != null)
        )
    }

    fun checkStateSubTotal() {
        _checkStateSubTotal.value = _formAsalPengirim.value?.get("id_cabang_asal") != 0 &&
                _formAsalPenerima.value?.get("id_cabang_tujuan") != 0 &&
                _formJenisLayanan.value?.idlayanan != null &&
                _formUkuranBarang.value?.idjenisukuran != null
    }

    fun checkStateDiskon() {
        _checkStateDiskon.value = _formAsalPengirim.value?.get("id_cabang_asal") != 0 &&
                _formAsalPenerima.value?.get("id_cabang_tujuan") != 0 &&
                _formJenisLayanan.value?.idlayanan != null &&
                _formUkuranBarang.value?.idjenisukuran != null
    }

    fun checkOngkirFixRate(params: HashMap<String, String>): LiveData<ResponseCheckOngkirFixRate?> {
        _cekOngkirFixRate = MutableLiveData()
        ongkirFixRate(params)
        return _cekOngkirFixRate as MutableLiveData<ResponseCheckOngkirFixRate?>
    }

    fun checkCutOff(layanan: Int): LiveData<ResponseCheckCutOff?> {
        _checkCutOff = MutableLiveData()
        cutOff(layanan)
        return _checkCutOff as MutableLiveData<ResponseCheckCutOff?>
    }

    fun checkHariLibur(tanggal: String): LiveData<ResponseCheckHariLibur?> {
        _checkHariLibur = MutableLiveData()
        hariLibur(tanggal)
        return _checkHariLibur as MutableLiveData<ResponseCheckHariLibur?>
    }

    fun checkDiskonFixRate(params: HashMap<String, Any>): LiveData<ResponseCheckDiskon?> {
        _checkDiskonFixRate = MutableLiveData()
        diskon(params)
        return _checkDiskonFixRate as MutableLiveData<ResponseCheckDiskon?>
    }

    private fun cutOff(layanan: Int) {
        val client = ApiConfig.getApiService().checkCutOff(layanan)
        client.enqueue(object : Callback<ResponseCheckCutOff> {
            override fun onResponse(
                call: Call<ResponseCheckCutOff>,
                response: Response<ResponseCheckCutOff>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    result.also {
                        _checkCutOff!!.postValue(it)
                    }
                } else {
                    val error =
                        Gson().fromJson(
                            response.errorBody()?.string(),
                            ResponseCheckCutOff::class.java
                        )
                    _checkCutOff!!.postValue(error)
                }
            }

            override fun onFailure(call: Call<ResponseCheckCutOff>, t: Throwable) {
                _checkCutOff!!.postValue(null)
            }
        })
    }

    private fun hariLibur(tanggal: String) {
        val client = ApiConfig.getApiService().checkLibur(tanggal)
        client.enqueue(object : Callback<ResponseCheckHariLibur> {
            override fun onResponse(
                call: Call<ResponseCheckHariLibur>,
                response: Response<ResponseCheckHariLibur>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    result.also {
                        _checkHariLibur!!.postValue(it)
                    }
                } else {
                    val error =
                        Gson().fromJson(
                            response.errorBody()?.string(),
                            ResponseCheckHariLibur::class.java
                        )
                    _checkHariLibur!!.postValue(error)
                }
            }

            override fun onFailure(call: Call<ResponseCheckHariLibur>, t: Throwable) {
                _checkHariLibur!!.postValue(null)
            }
        })
    }

    private fun diskon(params: HashMap<String, Any>) {
        val client = ApiConfig.getApiService().checkDiskon(params)
        client.enqueue(object : Callback<ResponseCheckDiskon> {
            override fun onResponse(
                call: Call<ResponseCheckDiskon>,
                response: Response<ResponseCheckDiskon>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    result.also {
                        _checkDiskonFixRate?.postValue(it)
                    }
                } else {
                    val error =
                        Gson().fromJson(
                            response.errorBody()?.string(),
                            ResponseCheckDiskon::class.java
                        )
                    _checkDiskonFixRate?.postValue(error)
                }
            }

            override fun onFailure(call: Call<ResponseCheckDiskon>, t: Throwable) {
                _checkDiskonFixRate?.postValue(null)
            }
        })
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

    fun checkout(params: HashMap<String, String>): LiveData<ResponseCheckout?> {
        _checkout = MutableLiveData()
        val client = ApiConfig.getApiService().checkout(params)
        client.enqueue(object : Callback<ResponseCheckout> {
            override fun onResponse(
                call: Call<ResponseCheckout>,
                response: Response<ResponseCheckout>
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
                            ResponseCheckout::class.java
                        )
                    _checkout!!.postValue(error)
                }
            }

            override fun onFailure(call: Call<ResponseCheckout>, t: Throwable) {
                _checkout!!.postValue(null)
            }
        })
        return _checkout as MutableLiveData<ResponseCheckout?>
    }

    fun editCheckout(
        id: Int,
        params: HashMap<String, String>
    ): LiveData<ResponseEditCheckoutFixRate?> {
        _editCheckout = MutableLiveData()
        val client = ApiConfig.getApiService().editCheckoutFixRate(id, params)
        client.enqueue(object : Callback<ResponseEditCheckoutFixRate> {
            override fun onResponse(
                call: Call<ResponseEditCheckoutFixRate>,
                response: Response<ResponseEditCheckoutFixRate>
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
                            ResponseEditCheckoutFixRate::class.java
                        )
                    _editCheckout!!.postValue(error)
                }
            }

            override fun onFailure(call: Call<ResponseEditCheckoutFixRate>, t: Throwable) {
                _editCheckout!!.postValue(null)
            }
        })
        return _editCheckout as MutableLiveData<ResponseEditCheckoutFixRate?>
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

    val checkStateCutOff: MutableLiveData<HashMap<String, Boolean>?> = _checkStateCutOff
    val checkStateSubtotal: LiveData<Boolean> = _checkStateSubTotal
    val checkStateDiskon: LiveData<Boolean> = _checkStateDiskon

    val changeOrderPaket: LiveData<HashMap<String, Any>> = _stateChangePaket
}