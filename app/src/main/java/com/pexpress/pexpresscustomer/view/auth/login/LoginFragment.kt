package com.pexpress.pexpresscustomer.view.auth.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.pexpress.pexpresscustomer.R
import com.pexpress.pexpresscustomer.databinding.FragmentLoginBinding
import com.pexpress.pexpresscustomer.utils.showMessage
import com.pexpress.pexpresscustomer.view.auth.viewmodel.AuthViewModel
import com.pexpress.pexpresscustomer.view.main.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import www.sanju.motiontoast.MotionToast

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<AuthViewModel>()

    private val activityScope = CoroutineScope(Dispatchers.Main)

    private companion object {
        const val REQUIRED_NUMBER_PHONE = "Nomor Handphone Harus Di isi"
        private const val TIME_DELAY = 1000L
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val TAG = LoginFragment::class.simpleName

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        numberPhoneObserve()

        with(binding) {
            checkLogin()
            btnRegister.setOnClickListener { moveToSendCode() }
        }
    }

    private fun checkLogin() {
        with(binding) {
            cardBtnLogin.setOnClickListener {
                setButtonState(true)

                val numberPhone = binding.edtNumberPhone.text.toString().trim()
                viewModel.checkLogin(numberPhone)

//                val numberPhoneDummy = "082113032502"

                viewModel.auth.observe(viewLifecycleOwner, { response ->
                    setButtonState(false)
                    if (response != null) {
                        if (response.success!!) {
                            startActivity(Intent(requireContext(), MainActivity::class.java))
                            activity?.finish()
                        } else {
                            tiNumberPhone.error = response.message
                        }
                    } else {
                        showMessage(
                            requireActivity(),
                            getString(R.string.problem_title),
                            getString(R.string.problem_message),
                            MotionToast.TOAST_WARNING
                        )
                    }
                })
            }
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
                    if (p0?.length!! > 0) {
                        cardBtnLogin.isEnabled = true
                        cardBtnLogin.setCardBackgroundColor(
                            ResourcesCompat.getColor(
                                resources,
                                R.color.primaryColor,
                                null
                            )
                        )
                    } else {
                        cardBtnLogin.isEnabled = false
                        cardBtnLogin.setCardBackgroundColor(
                            ResourcesCompat.getColor(
                                resources,
                                R.color.gray,
                                null
                            )
                        )
                    }
                }
            })

        }
    }

    private fun setButtonState(state: Boolean) {
        with(binding) {
            if (state) {
                pbLoading.visibility = View.VISIBLE
                tvLogin.visibility = View.GONE
                tiNumberPhone.error = ""
            } else {
                pbLoading.visibility = View.GONE
                tvLogin.visibility = View.VISIBLE
            }
        }
    }

    private fun moveToSendCode() {
        findNavController().navigate(R.id.action_loginFragment_to_sendCodeFragment)
    }

    private fun moveToOTP(numberPhone: String) {
        val toOTP =
            LoginFragmentDirections.actionLoginFragmentToOTPFragment(numberPhone)
        findNavController().navigate(toOTP)
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.viewModelStore?.clear()
        _binding = null
    }
}