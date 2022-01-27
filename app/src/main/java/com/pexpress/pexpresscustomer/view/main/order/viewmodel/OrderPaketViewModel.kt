package com.pexpress.pexpresscustomer.view.main.order.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class OrderPaketViewModel : ViewModel() {

    private val TAG = OrderPaketViewModel::class.simpleName

    private val _stateInfoPengirim = MutableLiveData<Boolean>().apply {
        value = false
    }

    private val _stateInfoPenerima = MutableLiveData<Boolean>().apply {
        value = false
    }

    private val _stateInfoPickup = MutableLiveData<Boolean>().apply {
        value = false
    }

    private val _formAsalPenerima = MutableLiveData<String>().apply {
        value = ""
    }

    private val _formAsalPengirim = MutableLiveData<String>().apply {
        value = ""
    }

    private val _formJenisLayanan = MutableLiveData<String>()
    private val _formUkuranBarang = MutableLiveData<String>()

    fun setStateInfoPengirim(state: Boolean) {
        _stateInfoPengirim.value = state
    }

    fun setStateInfoPenerima(state: Boolean) {
        _stateInfoPenerima.value = state
    }

    fun setStateInfoPickup(state: Boolean) {
        _stateInfoPickup.value = state
    }

    fun setFormAsalPenerima(value: String) {
        _formAsalPenerima.value = value
    }

    fun setFormAsalPengirim(value: String) {
        _formAsalPengirim.value = value
    }

    fun setFormJenisLayanan(value: String) {
        _formJenisLayanan.value = value
    }

    fun setFormUkuranBarang(value: String) {
        _formUkuranBarang.value = value
    }

    val stateInfoPengirim: LiveData<Boolean> = _stateInfoPengirim
    val stateInfoPenerima: LiveData<Boolean> = _stateInfoPenerima
    val stateInfoPickup: LiveData<Boolean> = _stateInfoPickup
    val formAsalPenerima: LiveData<String> = _formAsalPenerima
    val formAsalPengirim: LiveData<String> = _formAsalPengirim
    val formJenisLayanan: LiveData<String> = _formJenisLayanan
    val formUkuranBarang: LiveData<String> = _formUkuranBarang
}