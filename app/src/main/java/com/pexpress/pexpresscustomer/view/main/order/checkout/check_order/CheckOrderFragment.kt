package com.pexpress.pexpresscustomer.view.main.order.checkout.check_order

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.pexpress.pexpresscustomer.R
import com.pexpress.pexpresscustomer.databinding.FragmentCheckOrderBinding
import com.pexpress.pexpresscustomer.utils.UtilsCode.TIME_DELAY_SCREEN
import com.pexpress.pexpresscustomer.view.main.MainActivity
import com.pexpress.pexpresscustomer.view.main.order.viewmodel.CheckoutViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CheckOrderFragment : Fragment() {

    private var _binding: FragmentCheckOrderBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<CheckoutViewModel>()

    private lateinit var args: CheckOrderFragmentArgs

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        args = CheckOrderFragmentArgs.fromBundle(arguments as Bundle)
        prepareCheckOrder()
    }

    private fun prepareCheckOrder() {
        with(binding) {
            val currentDest = findNavController().currentDestination?.parent?.id
            if (args.isCashPayment) {
                lifecycleScope.launch {
                    layoutCheckingPayment.visibility = GONE
                    layoutOrderComplete.visibility = VISIBLE
                    delay(TIME_DELAY_SCREEN)

                    when (currentDest) {
                        R.id.main_navigation -> {
                            if (args.isFromOrder) {
                                findNavController().navigate(R.id.action_checkOrderFragment_to_navigation_home)
                            } else {
                                findNavController().navigateUp()
                            }
                        }
                        R.id.history_navigation -> {
                            startActivity(Intent(requireContext(), MainActivity::class.java))
                        }
                    }

                }
            } else {
                viewModel.checkPembayaran(args.noInvoice).observe(viewLifecycleOwner) { response ->
                    layoutCheckingPayment.visibility = GONE
                    if (response != null) {
                        lifecycleScope.launch {
                            if (response.success!!) {
                                layoutOrderComplete.visibility = VISIBLE
                                delay(TIME_DELAY_SCREEN)
                                when (currentDest) {
                                    R.id.main_navigation -> findNavController().navigate(R.id.action_checkOrderFragment_to_navigation_home)
                                    R.id.history_navigation -> {
                                        startActivity(
                                            Intent(
                                                requireContext(),
                                                MainActivity::class.java
                                            )
                                        )
                                    }
                                }
                            } else {
                                layoutOrderFailed.visibility = VISIBLE
                                delay(TIME_DELAY_SCREEN)
                                findNavController().navigateUp()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}