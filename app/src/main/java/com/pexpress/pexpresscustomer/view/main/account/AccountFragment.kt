package com.pexpress.pexpresscustomer.view.main.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.pexpress.pexpresscustomer.R
import com.pexpress.pexpresscustomer.databinding.FragmentAccountBinding
import com.pexpress.pexpresscustomer.session.UserPreference
import com.pexpress.pexpresscustomer.utils.showMessage
import com.pexpress.pexpresscustomer.view.auth.AuthActivity
import com.pexpress.pexpresscustomer.view.main.account.viewmodel.AccountViewModel
import www.sanju.motiontoast.MotionToast

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<AccountViewModel>()

    private lateinit var userPreference: UserPreference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userPreference = UserPreference(requireContext())

        with(binding) {
            btnEditProfile.setOnClickListener { moveToManageProfile() }
            btnSignOut.setOnClickListener { exitApp() }
        }
    }

    private fun moveToManageProfile() {
        findNavController().navigate(R.id.action_navigation_account_to_manageProfileFragment)
    }

    private fun exitApp() {
        AlertDialog.Builder(requireContext())
            .setTitle("Perhatian")
            .setMessage("Apakah anda yakin ingin logout akun?")
            .setPositiveButton(
                "Keluar"
            ) { _, _ ->
                isLoading(true)
                logout(userPreference.getUser().id.toString())
            }
            .setNegativeButton("Tidak") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .create()
            .show()
    }

    private fun logout(idUser: String) {
        viewModel.logout(idUser).observe(viewLifecycleOwner) { response ->
            isLoading(false)
            if (response != null) {
                if (response.success) {
                    userPreference.removeLogin()
                    userPreference.removeUser()
                    startActivity(Intent(requireContext(), AuthActivity::class.java))
                    requireActivity().finish()
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

    private fun isLoading(state: Boolean) {
        with(binding) {
            if (state) {
                layoutLoading.visibility = View.VISIBLE
                requireActivity().window.setFlags(
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                )
            } else {
                layoutLoading.visibility = View.GONE
                requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}