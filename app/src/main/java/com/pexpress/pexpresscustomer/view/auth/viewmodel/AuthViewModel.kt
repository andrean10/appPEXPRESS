package com.pexpress.pexpresscustomer.view.auth.viewmodel

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.pexpress.pexpresscustomer.model.auth.ResponseLogin
import com.pexpress.pexpresscustomer.model.auth.ResponseOTP
import com.pexpress.pexpresscustomer.model.auth.ResponseRegister
import com.pexpress.pexpresscustomer.network.ApiConfig
import com.pexpress.pexpresscustomer.utils.UtilsCode.DEFAULT_TIMER
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.util.concurrent.TimeUnit

class AuthViewModel : ViewModel() {

    private val DURATION = TimeUnit.MINUTES.toMillis(1)

    private val _countDownTimer = MutableLiveData<String>()
    private var _login: MutableLiveData<ResponseLogin?>? = null
    private var _register: MutableLiveData<ResponseRegister?>? = null
    private var _otp: MutableLiveData<ResponseOTP?>? = null

    private val TAG = AuthViewModel::class.simpleName

    fun login(contact: String): LiveData<ResponseLogin?> {
        _login = MutableLiveData()
        checkLogin(contact)
        return _login as MutableLiveData<ResponseLogin?>
    }

    fun register(contact: String): LiveData<ResponseRegister?> {
        _register = MutableLiveData()
        checkRegister(contact)
        return _register as MutableLiveData<ResponseRegister?>
    }

    fun otp(params: HashMap<String, String>): LiveData<ResponseOTP?> {
        _otp = MutableLiveData()
        verifyOTP(params)
        return _otp as MutableLiveData<ResponseOTP?>
    }

    fun getOtpAgain(numberPhone: String): LiveData<ResponseOTP?> {
        _otp = MutableLiveData()
        retryOTP(numberPhone)
        return _otp as MutableLiveData<ResponseOTP?>
    }

    fun startCountDownTimer() {
        setCountDownTimer.start()
    }

    private val setCountDownTimer = object : CountDownTimer(DURATION, 1000) {

        // Callback function, fired on regular interval
        override fun onTick(l: Long) {
            val elapsedMinutes = String.format(
                Locale.ENGLISH, "%02d : %02d",
                TimeUnit.MILLISECONDS.toMinutes(l),
                TimeUnit.MILLISECONDS.toSeconds(l) -
                        TimeUnit.MILLISECONDS.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l))
            )

            _countDownTimer.postValue(elapsedMinutes)
        }

        // Callback function, fired
        // when the time is up
        override fun onFinish() {
            _countDownTimer.postValue(DEFAULT_TIMER)
        }
    }


    private fun checkLogin(contact: String) {
        val client = ApiConfig.getApiService().login(contact)
        client.enqueue(object : Callback<ResponseLogin> {
            override fun onResponse(call: Call<ResponseLogin>, response: Response<ResponseLogin>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null) {
                        _login!!.postValue(result)
                    }
                } else {
                    val error = Gson().fromJson(
                        response.errorBody()?.string(),
                        ResponseLogin::class.java
                    )
                    _login!!.postValue(error)
                }
            }

            override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                _login!!.postValue(null)
            }
        })
    }

    private fun checkRegister(contact: String) {
        val client = ApiConfig.getApiService().register(contact)
        client.enqueue(object : Callback<ResponseRegister> {
            override fun onResponse(
                call: Call<ResponseRegister>,
                response: Response<ResponseRegister>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null) {
                        _register!!.postValue(result)
                    }
                } else {
                    val error =
                        Gson().fromJson(
                            response.errorBody()?.string(),
                            ResponseRegister::class.java
                        )
                    _register!!.postValue(error)
                }
            }

            override fun onFailure(call: Call<ResponseRegister>, t: Throwable) {
                _register!!.postValue(null)

                Log.d(TAG, "onFailure: Error Dijalankan")
            }
        })
    }

    private fun verifyOTP(params: HashMap<String, String>) {
        val client = ApiConfig.getApiService().verifyOTP(params)
        client.enqueue(object : Callback<ResponseOTP> {
            override fun onResponse(call: Call<ResponseOTP>, response: Response<ResponseOTP>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null) {
                        _otp!!.postValue(result)
                    }
                } else {
                    val error =
                        Gson().fromJson(response.errorBody()?.string(), ResponseOTP::class.java)
                    _otp!!.postValue(error)
                }
            }

            override fun onFailure(call: Call<ResponseOTP>, t: Throwable) {
                _otp!!.postValue(null)
            }
        })
    }

    private fun retryOTP(numberPhone: String) {
        val client = ApiConfig.getApiService().retryOTP(numberPhone)
        client.enqueue(object : Callback<ResponseOTP> {
            override fun onResponse(call: Call<ResponseOTP>, response: Response<ResponseOTP>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    if (result != null) {
                        _otp!!.postValue(result)
                    }
                } else {
                    val error =
                        Gson().fromJson(response.errorBody()?.string(), ResponseOTP::class.java)
                    _otp!!.postValue(error)
                }
            }

            override fun onFailure(call: Call<ResponseOTP>, t: Throwable) {
                _otp!!.postValue(null)
            }
        })
    }

    val countDownTimer: LiveData<String> = _countDownTimer
}