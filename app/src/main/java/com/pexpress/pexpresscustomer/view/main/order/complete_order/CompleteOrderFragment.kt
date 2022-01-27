package com.pexpress.pexpresscustomer.view.main.order.complete_order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.pexpress.pexpresscustomer.R
import com.pexpress.pexpresscustomer.databinding.FragmentCompleteOrderBinding
import com.pexpress.pexpresscustomer.utils.UtilsCode
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CompleteOrderFragment : Fragment() {

    private var _binding: FragmentCompleteOrderBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCompleteOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            delay(UtilsCode.TIME_DELAY_SCREEN)
            findNavController().navigate(R.id.action_completeOrderFragment_to_navigation_home)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}