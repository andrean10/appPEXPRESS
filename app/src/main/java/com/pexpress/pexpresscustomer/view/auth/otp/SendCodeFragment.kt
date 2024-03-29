package com.pexpress.pexpresscustomer.view.auth.otp

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
import com.pexpress.pexpresscustomer.databinding.FragmentSendCodeBinding
import com.pexpress.pexpresscustomer.view.auth.viewmodel.AuthViewModel

class SendCodeFragment : Fragment() {

    private var _binding: FragmentSendCodeBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<AuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSendCodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar()

        numberPhoneObserve()

        with(binding) {
            btnSendCode.setOnClickListener { moveToOTP() }
        }
    }

    private fun numberPhoneObserve() {
        with(binding) {
            edtNumberPhone.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    btnSendCode.isEnabled = p0?.length == 11
                }
            })
        }
    }

    private fun moveToOTP() {
        val numberPhone = "+62${binding.edtNumberPhone.text.toString().trim()}"
        val toOTP =
            SendCodeFragmentDirections.actionSendCodeFragmentToOTPFragment(numberPhone)
        findNavController().navigate(toOTP)
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