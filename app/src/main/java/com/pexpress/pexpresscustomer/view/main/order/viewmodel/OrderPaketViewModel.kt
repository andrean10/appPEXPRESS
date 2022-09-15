package com.pexpress.pexpresscustomer.view.main.order.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.pexpress.pexpresscustomer.model.ResponseCreateVA
import com.pexpress.pexpresscustomer.model.checkout.ResponseCheckPembayaran
import com.pexpress.pexpresscustomer.model.checkout.ResponseCheckoutUpdate
import com.pexpress.pexpresscustomer.model.checkout.asuransi.ResponseAsuransi
import com.pexpress.pexpresscustomer.model.checkout.fix_rate.ResponseCheckout
import com.pexpress.pexpresscustomer.model.checkout.kilometer.ResponseCheckoutKilometer
import com.pexpress.pexpresscustomer.model.distance.ResponseDistance
import com.pexpress.pexpresscustomer.model.fix_rate.ongkir.ResponseCheckOngkirFixRate
import com.pexpress.pexpresscustomer.model.kilometer.ongkir.ResponseCheckOngkirKilometer
import com.pexpress.pexpresscustomer.model.order.*
import com.pexpress.pexpresscustomer.model.resi.ResponseResi
import com.pexpress.pexpresscustomer.model.type_payments.*
import com.pexpress.pexpresscustomer.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderPaketViewModel : ViewModel() {

    var isReadyDataAsuransi = MutableLiveData<Boolean>()

    init {
        isReadyDataAsuransi.value = false
    }

//    private val _stateInfoPengirim = MutableLiveData<Boolean>().apply {
//        value = false
//    }

//    private val _stateInfoPenerima = MutableLiveData<Boolean>().apply {
//        value = false
//    }

//    private val _stateInfoPickup = MutableLiveData<Boolean>().apply {
//        value = false
//    }

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

    private val _jenisLayanan = MutableLiveData<ResponseJenisLayanan?>()
    private val _jenisUkuran = MutableLiveData<ResponseJenisUkuran?>()
    private val _jenisBarang = MutableLiveData<ResponseJenisBarang?>()

    private var _district: MutableLiveData<ResponseDistrict?>? = null
    private var _cabang: MutableLiveData<ResponseCabang?>? = null
    private var _distance: MutableLiveData<ResponseDistance?>? = null

    private var _checkSubTotal = MutableLiveData<Boolean>().apply {
        value = _formAsalPengirim.value?.get("id_cabang_asal") != 0 &&
                formAsalPenerima.value?.get("id_cabang_tujuan") != 0 &&
                _formJenisLayanan.value != null &&
                _formUkuranBarang.value != null
    }
    private var _cekOngkirFixRate: MutableLiveData<ResponseCheckOngkirFixRate?>? = null
    private var _cekOngkirKilometer: MutableLiveData<ResponseCheckOngkirKilometer?>? = null

    private val _checkout = MutableLiveData<ResponseCheckout?>()
    private var _checkoutKilometer: MutableLiveData<ResponseCheckoutKilometer?>? = null
    private var _checkoutUpdate: MutableLiveData<ResponseCheckoutUpdate?>? = null

    private var _asuransi = MutableLiveData<ResponseAsuransi?>()
    private val _va = MutableLiveData<ResponseVA?>()
    private val _ewallet = MutableLiveData<ResponseEWallet?>()

    private val _vaPayment = MutableLiveData<ResultVA?>()
    private val _ewalletPayment = MutableLiveData<ResultEWallet?>()

    private var _resi: MutableLiveData<ResponseResi?>? = null

    private var _createCash: MutableLiveData<ResponseCreateCash?>? = null
    private var _createVa: MutableLiveData<ResponseCreateVA?>? = null
    private var _createEWallet: MutableLiveData<ResponseCreateEWallet?>? = null

    private var _checkPembayaran: MutableLiveData<ResponseCheckPembayaran?>? = null

//    fun setStateInfoPengirim(state: Boolean) {
//        _stateInfoPengirim.value = state
//    }

//    fun setStateInfoPenerima(state: Boolean) {
//        _stateInfoPenerima.value = state
//    }

//    fun setStateInfoPickup(state: Boolean) {
//        _stateInfoPickup.value = state
//    }

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
                formAsalPenerima.value?.get("id_cabang_tujuan") != 0 &&
                _formJenisLayanan.value?.idlayanan != null &&
                _formUkuranBarang.value?.idjenisukuran != null
    }

    fun setVAPayment(value: ResultVA) {
        _vaPayment.value = value
    }

    fun removeVAPayment() {
        _vaPayment.value = ResultVA()
    }

    fun setEWalletPayment(value: ResultEWallet) {
        _ewalletPayment.value = value
    }

    fun removeEWalletPayment() {
        _ewalletPayment.value = ResultEWallet()
    }

    fun layanan() {
        val client = ApiConfig.getApiService().jenisLayanan()
        client.enqueue(object : Callback<ResponseJenisLayanan> {
            override fun onResponse(
                call: Call<ResponseJenisLayanan>,
                response: Response<ResponseJenisLayanan>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    result.also {
                        _jenisLayanan.postValue(it)
                    }
                } else {
                    val error =
                        Gson().fromJson(
                            response.errorBody()?.string(),
                            ResponseJenisLayanan::class.java
                        )
                    _jenisLayanan.postValue(error)
                }
            }

            override fun onFailure(call: Call<ResponseJenisLayanan>, t: Throwable) {
                _jenisLayanan.postValue(null)
            }
        })
    }

    fun ukuran() {
        val client = ApiConfig.getApiService().jenisUkuran()
        client.enqueue(object : Callback<ResponseJenisUkuran> {
            override fun onResponse(
                call: Call<ResponseJenisUkuran>,
                response: Response<ResponseJenisUkuran>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    result.also {
                        _jenisUkuran.postValue(it)
                    }
                } else {
                    val error =
                        Gson().fromJson(
                            response.errorBody()?.string(),
                            ResponseJenisUkuran::class.java
                        )
                    _jenisUkuran.postValue(error)
                }
            }

            override fun onFailure(call: Call<ResponseJenisUkuran>, t: Throwable) {
                _jenisUkuran.postValue(null)
            }
        })
    }

    fun barang() {
        val client = ApiConfig.getApiService().jenisBarang()
        client.enqueue(object : Callback<ResponseJenisBarang> {
            override fun onResponse(
                call: Call<ResponseJenisBarang>,
                response: Response<ResponseJenisBarang>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    result.also {
                        _jenisBarang.postValue(it)
                    }
                } else {
                    val error =
                        Gson().fromJson(
                            response.errorBody()?.string(),
                            ResponseJenisBarang::class.java
                        )
                    _jenisBarang.postValue(error)
                }
            }

            override fun onFailure(call: Call<ResponseJenisBarang>, t: Throwable) {
                _jenisBarang.postValue(null)
            }
        })
    }

    private fun district(kecamatan: String, layanan: String) {
        val client = ApiConfig.getApiService().district(kecamatan, layanan)
        client.enqueue(object : Callback<ResponseDistrict> {
            override fun onResponse(
                call: Call<ResponseDistrict>,
                response: Response<ResponseDistrict>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    result.also {
                        _district!!.postValue(it)
                    }
                } else {
                    val error =
                        Gson().fromJson(
                            response.errorBody()?.string(),
                            ResponseDistrict::class.java
                        )
                    _district!!.postValue(error)
                }
            }

            override fun onFailure(call: Call<ResponseDistrict>, t: Throwable) {
                _district!!.postValue(null)
            }
        })
    }

    private fun cabang(idDistrict: Int) {
        val client = ApiConfig.getApiService().cabang(idDistrict)
        client.enqueue(object : Callback<ResponseCabang> {
            override fun onResponse(
                call: Call<ResponseCabang>,
                response: Response<ResponseCabang>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    result.also {
                        _cabang!!.postValue(it)
                    }
                } else {
                    val error =
                        Gson().fromJson(response.errorBody()?.string(), ResponseCabang::class.java)
                    _cabang!!.postValue(error)
                }
            }

            override fun onFailure(call: Call<ResponseCabang>, t: Throwable) {
                _cabang!!.postValue(null)
            }
        })
    }

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

    fun checkout(params: HashMap<String, String>) {
        val client = ApiConfig.getApiService().checkout(params)
        client.enqueue(object : Callback<ResponseCheckout> {
            override fun onResponse(
                call: Call<ResponseCheckout>,
                response: Response<ResponseCheckout>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    result.also {
                        _checkout.postValue(it)
                    }
                } else {
                    val error =
                        Gson().fromJson(
                            response.errorBody()?.string(),
                            ResponseCheckout::class.java
                        )
                    _checkout.postValue(error)
                }
            }

            override fun onFailure(call: Call<ResponseCheckout>, t: Throwable) {
                _checkout.postValue(null)
            }
        })
    }

    fun checkoutKilometer(params: HashMap<String, String>): LiveData<ResponseCheckoutKilometer?> {
        _checkoutKilometer = MutableLiveData()

        val client = ApiConfig.getApiService().checkoutKilometer(params)
        client.enqueue(object : Callback<ResponseCheckoutKilometer> {
            override fun onResponse(
                call: Call<ResponseCheckoutKilometer>,
                response: Response<ResponseCheckoutKilometer>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    result.also {
                        _checkoutKilometer!!.postValue(it)
                    }
                } else {
                    val error =
                        Gson().fromJson(
                            response.errorBody()?.string(),
                            ResponseCheckoutKilometer::class.java
                        )
                    _checkoutKilometer!!.postValue(error)
                }
            }

            override fun onFailure(call: Call<ResponseCheckoutKilometer>, t: Throwable) {
                _checkoutKilometer!!.postValue(null)
            }
        })
        return _checkoutKilometer as MutableLiveData<ResponseCheckoutKilometer?>
    }

    fun checkoutAddAsuransi(params: HashMap<String, String>): LiveData<ResponseCheckoutUpdate?> {
        _checkoutUpdate = MutableLiveData()
        checkoutUpdate(params)
        return _checkoutUpdate as MutableLiveData<ResponseCheckoutUpdate?>
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

    fun checkDistrict(kecamatan: String, layanan: String): LiveData<ResponseDistrict?> {
        _district = MutableLiveData()
        district(kecamatan, layanan)
        return _district as MutableLiveData<ResponseDistrict?>
    }

    fun checkCabang(idDistrict: Int): LiveData<ResponseCabang?> {
        _cabang = MutableLiveData()
        cabang(idDistrict)
        return _cabang as MutableLiveData<ResponseCabang?>
    }

    fun checkPembayaran(noInvoice: String): LiveData<ResponseCheckPembayaran?> {
        _checkPembayaran = MutableLiveData()
        pembayaran(noInvoice)
        return _checkPembayaran as MutableLiveData<ResponseCheckPembayaran?>
    }

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

    fun checkResi(noInvoice: String): LiveData<ResponseResi?> {
        _resi = MutableLiveData()
        resi(noInvoice)
        return _resi as MutableLiveData<ResponseResi?>
    }

    private fun resi(noInvoice: String) {
        val client = ApiConfig.getApiService().checkResi(noInvoice)
        client.enqueue(object : Callback<ResponseResi> {
            override fun onResponse(
                call: Call<ResponseResi>,
                response: Response<ResponseResi>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    result.also {
                        _resi?.postValue(it)
                    }
                } else {
                    val error =
                        Gson().fromJson(
                            response.errorBody()?.string(),
                            ResponseResi::class.java
                        )
                    _resi?.postValue(error)
                }
            }

            override fun onFailure(call: Call<ResponseResi>, t: Throwable) {
                _resi?.postValue(null)
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
                    val error =
                        Gson().fromJson(
                            response.errorBody()?.string(),
                            ResponseDistance::class.java
                        )
                    _distance?.postValue(error)
                }
            }

            override fun onFailure(call: Call<ResponseDistance>, t: Throwable) {
                _distance?.postValue(null)
            }
        })
        return _distance as MutableLiveData<ResponseDistance?>
    }

    //    val stateInfoPengirim: LiveData<Boolean> = _stateInfoPengirim
//    val stateInfoPenerima: LiveData<Boolean> = _stateInfoPenerima
//    val stateInfoPickup: LiveData<Boolean> = _stateInfoPickup
    val formAsalPengirim: LiveData<HashMap<String, Any>> = _formAsalPengirim
    val formAsalPenerima: LiveData<HashMap<String, Any>> = _formAsalPenerima

    val formLatLongPengirim: LiveData<HashMap<String, String>> = _formLatLongPengirim
    val formLatLongPenerima: LiveData<HashMap<String, String>> = _formLatLongPenerima

    val formJenisLayanan: LiveData<ResultJenisLayanan> = _formJenisLayanan
    val formUkuranBarang: LiveData<ResultJenisUkuran> = _formUkuranBarang
    val formJenisBarang: LiveData<ResultJenisBarang> = _formJenisBarang

    val jenisLayanan: LiveData<ResponseJenisLayanan?> = _jenisLayanan
    val jenisUkuran: LiveData<ResponseJenisUkuran?> = _jenisUkuran
    val jenisBarang: LiveData<ResponseJenisBarang?> = _jenisBarang
    val checkout: LiveData<ResponseCheckout?> = _checkout

    val checkSubtotal: LiveData<Boolean> = _checkSubTotal

    val asuransi: LiveData<ResponseAsuransi?> = _asuransi
    val listVA: LiveData<ResponseVA?> = _va
    val listEWallet: LiveData<ResponseEWallet?> = _ewallet

    val vaPayment: LiveData<ResultVA?> = _vaPayment
    val ewalletPayment: LiveData<ResultEWallet?> = _ewalletPayment
}