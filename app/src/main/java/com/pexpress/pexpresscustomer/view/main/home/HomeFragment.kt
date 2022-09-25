package com.pexpress.pexpresscustomer.view.main.home

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.pexpress.pexpresscustomer.R
import com.pexpress.pexpresscustomer.databinding.FragmentHomeBinding
import com.pexpress.pexpresscustomer.db.User
import com.pexpress.pexpresscustomer.session.UserPreference
import com.pexpress.pexpresscustomer.utils.UtilsCode.TAG
import com.pexpress.pexpresscustomer.view.auth.AuthActivity
import com.pexpress.pexpresscustomer.view.main.home.adapter.HomeSliderAdapter
import com.pexpress.pexpresscustomer.view.main.home.viewmodel.HomeViewModel
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<HomeViewModel>()

    private lateinit var adapterSlider: HomeSliderAdapter
    private lateinit var userPreference: UserPreference

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
        userPreference = UserPreference(requireContext())

        prepareSlider()
        getDataProfile()
        prepareMainMenu()
    }

    private fun getDataProfile() {
        viewModel.profile.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                if (response.success!!) {
                    val responseDetail = response.detail?.get(0)
                    userPreference.setUser(
                        User(
                            id = responseDetail?.id,
                            name = responseDetail?.fullname,
                            numberPhone = responseDetail?.contact,
                            email = responseDetail?.email.toString(),
                            otp = responseDetail?.otp
                        )
                    )
                    checkCompletedProfileUser(responseDetail?.fullname)
                } else {
                    moveToAuth()
                }
            } else {
                disabledMenuOrder()
            }
        }
    }

    private fun prepareSlider() {
        with(binding) {
            adapterSlider = HomeSliderAdapter()
            viewModel.getBanner().observe(viewLifecycleOwner) { response ->
                if (response != null) {
                    if (response.success!!) {
                        adapterSlider.setData(response.banner)
                    }
                }
            }

            sliderView.apply {
                setSliderAdapter(adapterSlider)
                setIndicatorAnimation(IndicatorAnimationType.WORM)
                setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
                startAutoCycle()
            }
        }
    }

    private fun checkCompletedProfileUser(name: String?) {
        with(binding) {
            if (name.isNullOrEmpty()) {
                layoutUncompletedProfile.visibility = VISIBLE
                menuTarifFlat.apply {
                    isEnabled = false
                    alpha = 0.5f
                }
                menuTarifKilometer.apply {
                    isEnabled = false
                    alpha = 0.5f
                }
            } else {
                layoutUncompletedProfile.visibility = GONE
                menuTarifFlat.apply {
                    isEnabled = true
                    alpha = 1f
                }
                menuTarifKilometer.apply {
                    isEnabled = true
                    alpha = 1f
                }
            }
        }
    }

    private fun disabledMenuOrder() {
        with(binding) {
            menuTarifFlat.apply {
                isEnabled = false
                alpha = 0.5f
            }
            menuTarifKilometer.apply {
                isEnabled = false
                alpha = 0.5f
            }
        }
    }

    private fun prepareMainMenu() {
        with(binding) {
            menuTarifFlat.setOnClickListener { moveToTarifFlat() }
            menuTarifKilometer.setOnClickListener { moveToTarifKilometer() }
            menuPexCargo.setOnClickListener {
                showPopUp(
                    getString(R.string.home_menu_pex_chargo),
                    getString(R.string.home_menu_pex_chargo_number_phone)
                )
            }
            menuMitra.setOnClickListener {
                showPopUp(
                    getString(R.string.home_menu_mitra),
                    getString(R.string.home_menu_mitra_number_phone)
                )
            }
            menuCekTarif.setOnClickListener { moveToCekTarif() }
            btnStatusOrder.setOnClickListener { moveToHistory() }
            btnCompletingProfile.setOnClickListener { moveToProfile() }
        }
    }

    private fun moveToHistory() {
        findNavController().navigate(R.id.action_navigation_home_to_historyFragment)
    }

    private fun moveToProfile() {
        findNavController().navigate(R.id.action_navigation_home_to_navigation_account)
    }

    private fun moveToTarifFlat() {
        findNavController().navigate(R.id.action_navigation_home_to_PFixRateFragment)
    }

    private fun moveToTarifKilometer() {
        val toResi = HomeFragmentDirections.actionNavigationHomeToPKilometerFragment()
        findNavController().navigate(toResi)
    }

    private fun moveToCekTarif() {
        findNavController().navigate(R.id.action_navigation_home_to_ongkirFragment)
    }

    private fun moveToAuth() {
        userPreference.removeLogin()
        userPreference.removeUser()
        userPreference.removeDeviceId()
        startActivity(Intent(requireContext(), AuthActivity::class.java))
        activity?.finish()
    }

    private fun moveToWhatsapp(numberPhone: String) {
        if (isWhatsappInstalled()) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://api.whatsapp.com/send?phone=$numberPhone")
                )
            )
        }
    }

    private fun showPopUp(message: String, numberPhone: String? = null) {
        AlertDialog.Builder(requireContext()).apply {
            if (numberPhone.isNullOrEmpty()) {
                setTitle("Coming Soon!")
                setPositiveButton("Oke") { dialogInterface, _ ->
                    dialogInterface.dismiss()
                }
            } else {
                setTitle("Pemberitahuan!")
                setPositiveButton("Hubungi") { dialogInterface, _ ->
                    moveToWhatsapp(numberPhone)
                    dialogInterface.dismiss()
                }
                setNegativeButton("Tutup") { dialogInterface, _ ->
                    dialogInterface.dismiss()
                }
            }
            setMessage(message)
            show()
        }
    }

    private fun isWhatsappInstalled(): Boolean {
        val packageManager = requireActivity().packageManager
        var whatsappInstalled = false

        try {
            packageManager.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
            whatsappInstalled = true
        } catch (e: PackageManager.NameNotFoundException) {
            Log.d(TAG, "isWhatsappInstalled: ${e.printStackTrace()}")
        }
        return whatsappInstalled
    }

//    private fun stateMenu(isEnabled: Boolean) {
//        with(binding) {
//            if (isEnabled) {
//                menuTarifFlat.isEnabled = true
//                menuTarifKilometer.isEnabled = true
//            } else {
//                menuTarifFlat.isEnabled = false
//                menuTarifKilometer.isEnabled = false
//            }
//        }
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}