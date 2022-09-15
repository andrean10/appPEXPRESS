package com.pexpress.pexpresscustomer.view.auth.otp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.pexpress.pexpresscustomer.R
import com.pexpress.pexpresscustomer.databinding.FragmentVerifyOtpBinding
import com.pexpress.pexpresscustomer.db.User
import com.pexpress.pexpresscustomer.db.UtilsApplications
import com.pexpress.pexpresscustomer.session.UserPreference
import com.pexpress.pexpresscustomer.utils.showMessage
import com.pexpress.pexpresscustomer.view.auth.viewmodel.AuthViewModel
import com.pexpress.pexpresscustomer.view.main.MainActivity
import www.sanju.motiontoast.MotionToast

class VerifyOTPFragment : Fragment() {

    private var _binding: FragmentVerifyOtpBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<AuthViewModel>()
    private lateinit var userPreference: UserPreference

    private lateinit var numberPhone: String
    private var otp = false
    private var otp2 = false
    private var otp3 = false
    private var otp4 = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVerifyOtpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar()

        numberPhone = VerifyOTPFragmentArgs.fromBundle(arguments as Bundle).numberPhone
        setupViewOTP()

        observeInputOTP()

        with(binding) {
            btnVerification.setOnClickListener { moveToMain() }
            btnRetrySendCode.setOnClickListener { getOTPAgain() }
        }
    }

    private fun setupViewOTP() {
        userPreference = UserPreference(requireContext())
        binding.tvOtpDesc.text = getString(R.string.otp_desc, numberPhone)
    }

    private fun getOTPAgain() {
        with(binding) {
            edtOtpDigit.setText("")
            edtOtpDigit2.setText("")
            edtOtpDigit3.setText("")
            edtOtpDigit4.setText("")
            edtOtpDigit.requestFocus()
        }
        // Mengambil OTP kembali
        viewModel.getOtpAgain(numberPhone)
    }

    private fun observeInputOTP() {
        with(binding) {
            edtOtpDigit.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    if (p0?.length!! > 0) {
                        edtOtpDigit2.requestFocus()
                        otp = true

                        if (otp && otp2 && otp3 && otp4) {
                            btnVerification.isEnabled = true
                        }
                    } else {
                        otp = false
                        btnVerification.isEnabled = false
                    }
                }

            })
            edtOtpDigit2.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    if (p0?.length!! > 0) {
                        edtOtpDigit3.requestFocus()
                        otp2 = true

                        if (otp && otp2 && otp3 && otp4) {
                            btnVerification.isEnabled = true
                        }
                    } else {
                        edtOtpDigit.requestFocus()
                        otp2 = false
                        btnVerification.isEnabled = false
                    }
                }
            })
            edtOtpDigit3.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    if (p0?.length!! > 0) {
                        edtOtpDigit4.requestFocus()
                        otp3 = true

                        if (otp && otp2 && otp3 && otp4) {
                            btnVerification.isEnabled = true
                        }
                    } else {
                        edtOtpDigit2.requestFocus()
                        otp3 = false
                        btnVerification.isEnabled = false
                    }
                }
            })

            edtOtpDigit4.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    if (p0?.length!! > 0) {
                        otp4 = true

                        if (otp && otp2 && otp3 && otp4) {
                            btnVerification.isEnabled = true
                        }
                    } else {
                        edtOtpDigit3.requestFocus()
                        otp4 = false
                        btnVerification.isEnabled = false
                    }
                }
            })
        }
    }

    private fun moveToMain() {
        // check verify otp when succes move to main
        with(binding) {
            val otpDigit = edtOtpDigit.text.toString().trim()
            val otpDigit2 = edtOtpDigit2.text.toString().trim()
            val otpDigit3 = edtOtpDigit3.text.toString().trim()
            val otpDigit4 = edtOtpDigit4.text.toString().trim()

            val otp = otpDigit + otpDigit2 + otpDigit3 + otpDigit4

            val params = hashMapOf(
                "contact" to numberPhone,
                "otp" to otp
            )

            viewModel.otp(params).observe(viewLifecycleOwner) { response ->
                if (response != null) {
                    if (response.success!!) {
                        // store number to userpreferences
                        userPreference.apply {
                            setLogin(UtilsApplications(isLoginValid = true))
                            setUser(
                                User(
                                    numberPhone = numberPhone
                                )
                            )
                        }
                        moveToMainActivity()
                    } else {
                        showMessage(
                            requireActivity(),
                            getString(R.string.failed_title),
                            response.message.toString(),
                            MotionToast.TOAST_ERROR
                        )
                    }
                } else {
                    showMessage(
                        requireActivity(),
                        getString(R.string.failed_title),
                        getString(R.string.failed_description),
                        MotionToast.TOAST_ERROR
                    )
                }
            }
        }
    }

    private fun moveToMainActivity() {
        startActivity(Intent(requireContext(), MainActivity::class.java))
        activity?.finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            findNavController().navigateUp()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setToolbar() {
        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.toolbar)
        if ((activity as AppCompatActivity?)!!.supportActionBar != null) {
            (activity as AppCompatActivity?)!!.supportActionBar!!.title = ""
            (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}