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
import com.pexpress.pexpresscustomer.databinding.FragmentOtpBinding
import com.pexpress.pexpresscustomer.view.auth.viewmodel.AuthViewModel
import com.pexpress.pexpresscustomer.view.main.MainActivity

class OTPFragment : Fragment() {

    private var _binding: FragmentOtpBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<AuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentOtpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar()

        val numberPhone = OTPFragmentArgs.fromBundle(arguments as Bundle).numberPhone
        setupViewOTP(numberPhone)

        observeInputOTP()

        with(binding) {
            btnVerification.setOnClickListener { moveToMain() }
            btnRetrySendCode.setOnClickListener { getOTPAgain() }
        }
    }

    private fun setupViewOTP(numberPhone: String) {
        binding.tvOtpDesc.text = getString(R.string.otp_desc, numberPhone)
    }

    private fun getOTPAgain() {
        // hapus otp yang diketik
        with(binding) {
            edtOtpDigit.setText("")
            edtOtpDigit2.setText("")
            edtOtpDigit3.setText("")
            edtOtpDigit4.setText("")
            edtOtpDigit.requestFocus()
        }
        // Mengambil OTP kembali
    }

    private fun moveToMain() {
        startActivity(Intent(requireContext(), MainActivity::class.java))
        activity?.finish()
    }

    private fun checkInputOTP() {
        with(binding) {
            val otpDigit = edtOtpDigit.text.toString().trim()
            val otpDigit2 = edtOtpDigit2.text.toString().trim()
            val otpDigit3 = edtOtpDigit3.text.toString().trim()
            val otpDigit4 = edtOtpDigit4.text.toString().trim()
        }
    }

    private fun observeInputOTP() {
        with(binding) {
            edtOtpDigit.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    edtOtpDigit2.requestFocus()
                }

                override fun afterTextChanged(p0: Editable?) {
                }

            })
            edtOtpDigit2.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    edtOtpDigit3.requestFocus()
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            })
            edtOtpDigit3.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    edtOtpDigit4.requestFocus()
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            })
        }
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