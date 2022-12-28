package com.pexpress.pexpresscustomer.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.pexpress.pexpresscustomer.databinding.FragmentDialogLoadingBinding

class DialogLoadingFragment : DialogFragment() {

    private var _binding: FragmentDialogLoadingBinding? = null
    private val binding get() = _binding!!

    companion object {
        val TAG = DialogLoadingFragment::class.simpleName
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_CANCELED_TOUCH_OUTSIDE = "extra_canceled_touch_outside"
    }

    fun newInstance(title: String = "Loading...", isCanceledTouchOutside: Boolean = false): DialogLoadingFragment {
        val fragment = DialogLoadingFragment()
        val args = Bundle()
        args.putString(EXTRA_TITLE, title)
        args.putBoolean(EXTRA_CANCELED_TOUCH_OUTSIDE, isCanceledTouchOutside)
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.setCanceledOnTouchOutside(
            arguments?.getBoolean(EXTRA_CANCELED_TOUCH_OUTSIDE, false) ?: false
        )
        _binding = FragmentDialogLoadingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvTitleDialog.text = arguments?.getString(EXTRA_TITLE) ?: "Loading..."
    }

}