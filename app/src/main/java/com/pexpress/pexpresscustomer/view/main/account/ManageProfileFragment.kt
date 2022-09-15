package com.pexpress.pexpresscustomer.view.main.account

import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputLayout
import com.pexpress.pexpresscustomer.R
import com.pexpress.pexpresscustomer.databinding.FragmentManageProfileBinding
import com.pexpress.pexpresscustomer.model.profile.ResultProfile
import com.pexpress.pexpresscustomer.session.UserPreference
import com.pexpress.pexpresscustomer.utils.*
import com.pexpress.pexpresscustomer.view.main.account.viewmodel.AccountViewModel
import www.sanju.motiontoast.MotionToast


class ManageProfileFragment : Fragment() {

    private var _binding: FragmentManageProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<AccountViewModel>()
    private lateinit var formatDate: FormatDate
    private var idUser: Int = 0

    private val gender = arrayOf("Laki-laki", "Perempuan")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentManageProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar()
        setupView()
        observeDetailProfile()
    }

    override fun onResume() {
        super.onResume()
        setVisibilityBottomHead(requireActivity(), false)
    }

    private fun setupView() {
        formatDate = FormatDate()
        val adapter =
            ArrayAdapter<Any?>(requireContext(), android.R.layout.simple_list_item_1, gender)
        with(binding) {
            tiNumberPhone.setEndIconOnClickListener {
                pickContact.launch(0)
            }

            edtGender.setAdapter(adapter)
            edtBirthdate.setOnClickListener {
                val datePicker =
                    MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select date")
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .build()

                datePicker.show(parentFragmentManager, "TAG")

                datePicker.addOnPositiveButtonClickListener { selection ->
                    edtBirthdate.setText(
                        formatDate.outputDateFormat(UtilsCode.PATTERN_DATE_VIEW).format(selection)
                    )
                }
            }
            btnUpdateProfile.setOnClickListener { checkDataUpdate() }
        }
    }

    private fun prepareData(profile: ResultProfile) {
        with(binding) {
            edtFullname.setText(profile.fullname)
            edtAddress.setText(profile.address)
            edtEmail.setText(profile.email)
            edtNumberPhone.setText(profile.contact)
            edtGender.setText(profile.gender, false)
            edtBirthdate.setText(
                FormatDate().formatedDate(
                    profile.birthday,
                    UtilsCode.PATTERN_DATE_POST,
                    UtilsCode.PATTERN_DATE_VIEW
                )
            )
        }
    }

    private fun checkDataUpdate() {
        with(binding) {
            val fullName = edtFullname.text.toString().trim()
            val email = edtEmail.text.toString().trim()
            val numberPhone = edtNumberPhone.text.toString().trim()
            val gender = edtGender.text.toString().trim()
            val address = edtAddress.text.toString().trim()
            val birthdate = edtBirthdate.text.toString().trim()

            when {
                fullName.isEmpty() -> {
                    showMessageFieldRequired(tiFullname)
                    return@with
                }
                email.isEmpty() -> {
                    showMessageFieldRequired(tiEmail)
                    return@with
                }
                numberPhone.isEmpty() -> {
                    showMessageFieldRequired(tiNumberPhone)
                    return@with
                }
                address.isEmpty() -> {
                    showMessageFieldRequired(tiAddress)
                    return@with
                }
                gender.isEmpty() -> {
                    showMessageFieldRequired(tiGender)
                    return@with
                }
                birthdate.isEmpty() -> {
                    showMessageFieldRequired(tiBirthdate)
                    return@with
                }
                else -> {
                    tiFullname.error = ""
                    tiEmail.error = ""
                    tiNumberPhone.error = ""
                    tiGender.error = ""
                    tiAddress.error = ""
                    tiBirthdate.error = ""

                    val params = hashMapOf(
                        "idcustomer" to idUser.toString(),
                        "fullname" to fullName,
                        "contact" to numberPhone,
                        "email" to email,
                        "gender" to gender,
                        "address" to address,
                        "birthday" to formatDate.formatedDate(
                            birthdate,
                            UtilsCode.PATTERN_DATE_VIEW,
                            UtilsCode.PATTERN_DATE_POST
                        )
                    )

                    observeUpdateProfile(params)
                }
            }
        }
    }

    private fun observeDetailProfile() {
        val userPreference = UserPreference(requireContext())
        viewModel.profile(userPreference.getUser().numberPhone.toString())
            .observe(viewLifecycleOwner) { response ->
                if (response != null) {
                    if (response.success!!) {
                        val result = response.detail?.get(0)
                        idUser = result?.id ?: 0
                        result?.also { prepareData(it) }
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

    private fun observeUpdateProfile(params: HashMap<String, String>) {
        viewModel.updateProfile(params).observe(viewLifecycleOwner) { response ->
            if (response != null) {
                if (response.success!!) {
                    val result = response.detail?.get(0)
                    prepareData(
                        ResultProfile(
                            id = result?.id,
                            fullname = result?.fullName,
                            contact = result?.contact,
                            email = result?.email,
                            gender = result?.gender,
                            birthday = result?.birthday,
                            address = result?.address
                        )
                    )
                    showMessage(
                        requireActivity(),
                        getString(R.string.text_success),
                        response.message.toString(),
                        MotionToast.TOAST_SUCCESS
                    )
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

    private fun showMessageFieldRequired(ti: TextInputLayout) {
        ti.error = "Field tidak boleh kosong!"
    }

    private val pickContact = registerForActivityResult(PickContact()) {
        it?.also { contactUri ->
            val projection = arrayOf(
                ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER
            )

            context?.contentResolver?.query(contactUri, projection, null, null, null)?.apply {
                moveToFirst()
                binding.edtNumberPhone.setText(normalizedNumber2(getString(0)))
                close()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) findNavController().navigateUp()
        return super.onOptionsItemSelected(item)
    }

    private fun setToolbar() {
        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.toolbar)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}