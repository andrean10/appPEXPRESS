package com.pexpress.pexpresscustomer.view.main.ongkir.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pexpress.pexpresscustomer.model.order.ResultJenisLayanan
import com.pexpress.pexpresscustomer.model.order.ResultJenisUkuran

class TarifKilometerViewModel : ViewModel() {

    private val _formJenisLayanan = MutableLiveData<ResultJenisLayanan>()
    private val _formUkuranBarang = MutableLiveData<ResultJenisUkuran>()

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

    val formJenisLayanan: LiveData<ResultJenisLayanan> = _formJenisLayanan
    val formUkuranBarang: LiveData<ResultJenisUkuran> = _formUkuranBarang
}