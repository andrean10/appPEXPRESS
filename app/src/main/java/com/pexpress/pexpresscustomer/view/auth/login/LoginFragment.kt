package com.pexpress.pexpresscustomer.view.auth.login

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
import com.pexpress.pexpresscustomer.session.UserPreference
import com.pexpress.pexpresscustomer.utils.showMessage
import com.pexpress.pexpresscustomer.view.auth.viewmodel.AuthViewModel
import www.sanju.motiontoast.MotionToast

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<AuthViewModel>()
    private lateinit var userPreference: UserPreference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userPreference = UserPreference(requireContext())
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
                viewModel.login(numberPhone).observe(viewLifecycleOwner) { response ->
                    setButtonState(false)
                    if (response != null) {
                        if (response.success!!) {
                            moveToOTP(numberPhone)
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
                }
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
                                R.color.bg_gray,
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

    private fun moveToOTP(numberPhone: String) {
        val toOTP = LoginFragmentDirections.actionLoginFragmentToVerifyOTPFragment(numberPhone)
        findNavController().navigate(toOTP)
    }

    private fun moveToSendCode() {
        findNavController().navigate(R.id.action_loginFragment_to_sendCodeFragment)
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.viewModelStore?.clear()
        _binding = null
    }
}