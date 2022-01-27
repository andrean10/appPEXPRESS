package com.pexpress.pexpresscustomer.view.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.pexpress.pexpresscustomer.R
import com.pexpress.pexpresscustomer.databinding.FragmentHomeBinding
import com.pexpress.pexpresscustomer.utils.ItemsDummy
import com.pexpress.pexpresscustomer.view.main.home.adapter.HomeSliderAdapter
import com.rprojects.apppickupcustomer.view.main.home.viewmodel.HomeViewModel
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<HomeViewModel>()

    private lateinit var adapterSlider: HomeSliderAdapter

    private val TAG = HomeFragment::class.simpleName

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewHome()
    }

    private fun setupViewHome() {
        showBottomNav()
        prepareSlider()
        prepareMenuHeader()
        prepareMainMenu()
    }

    private fun prepareSlider() {
        with(binding) {
            adapterSlider = HomeSliderAdapter(ItemsDummy.listSlider)
            sliderView.apply {
                setSliderAdapter(adapterSlider)
                setIndicatorAnimation(IndicatorAnimationType.WORM)
                setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
                startAutoCycle()
            }
        }
    }

    private fun prepareMenuHeader() {
        with(binding) {
//            btnTracking.setOnClickListener { moveToTracking() }
            btnHistory.setOnClickListener { moveToHistory() }
//            btnAccount.setOnClickListener { moveToMyAccount() }
        }
    }

    private fun prepareMainMenu() {
        with(binding) {
            menuTarifFlat.setOnClickListener { moveToTarifFlat() }
//            menuTarifKilometer.setOnClickListener { moveToTarifKilometer() }
//            menuPexChargo.setOnClickListener { showPopupCS }
//            menuMitra.setOnClickListener { showPopupCS }
            menuCekTarif.setOnClickListener { moveToCekTarif() }
        }
    }

//    private fun moveToTracking() {
//        findNavController().navigate(R.id.)
//    }

    private fun moveToHistory() {
        findNavController().navigate(R.id.action_navigation_home_to_historyFragment)
    }

//    private fun moveToMyAccount() {
//        findNavController().navigate(R.id)
//    }

    private fun moveToTarifFlat() {
        findNavController().navigate(R.id.action_navigation_home_to_PFixRateFragment)
    }

//    private fun moveToTarifKilometer() {
//        findNavController().navigate(R.id)
//    }

    private fun moveToCekTarif() {
//        startActivity(Intent(requireContext(), PriceActivity::class.java),
//            ActivityOptions.makeSceneTransitionAnimation(requireActivity()).toBundle())
//        findNavController().navigate(R.id.priceFragment)
    }

    private fun showBottomNav() {
        activity?.findViewById<BottomNavigationView>(R.id.nav_view)?.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}