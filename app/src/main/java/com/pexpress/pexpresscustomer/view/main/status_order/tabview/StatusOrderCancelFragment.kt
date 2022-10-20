package com.pexpress.pexpresscustomer.view.main.status_order.tabview

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.pexpress.pexpresscustomer.R
import com.pexpress.pexpresscustomer.databinding.FragmentStatusOrderCancelBinding
import com.pexpress.pexpresscustomer.model.status_order.ResultStatusOrder
import com.pexpress.pexpresscustomer.session.UserPreference
import com.pexpress.pexpresscustomer.utils.UtilsCode.TAG
import com.pexpress.pexpresscustomer.utils.showMessage
import com.pexpress.pexpresscustomer.view.main.status_order.adapter.StatusOrderCancelAdapter
import com.pexpress.pexpresscustomer.view.main.status_order.detail.DetailStatusOrderActivity
import com.pexpress.pexpresscustomer.view.main.status_order.viewmodel.HistoryViewModel
import www.sanju.motiontoast.MotionToast

class StatusOrderCancelFragment : Fragment() {

    private var _binding: FragmentStatusOrderCancelBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<HistoryViewModel>()
    private lateinit var statusOrderAdapter: StatusOrderCancelAdapter
    private lateinit var userPreference: UserPreference
    private lateinit var params: HashMap<String, String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatusOrderCancelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {
        statusOrderAdapter = StatusOrderCancelAdapter()
        userPreference = UserPreference(requireContext())

        prepareData()

        with(binding) {
            with(rvHistoryStatusOrder) {
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
                addItemDecoration(
                    DividerItemDecoration(requireContext(), DividerItemDecoration.HORIZONTAL)
                )
                adapter = statusOrderAdapter
            }

            statusOrderAdapter.setOnItemClickCallBack(object :
                StatusOrderCancelAdapter.OnItemClickCallBack {
                override fun onItemClicked(detailOrder: ResultStatusOrder) {
                    moveToDetailStatusOrder(detailOrder)
                }
            })
        }
    }

    private fun prepareData() {
        params = hashMapOf(
            "id" to userPreference.getUser().id.toString(),
            "status" to "cancel"
        )
    }

    private fun observeStatusOrder() {
        with(binding) {
            viewModel.statusOrder(params).observe(viewLifecycleOwner) { response ->
                pbStatusOrder.visibility = View.GONE
                if (response != null) {
                    if (response.success!!) {
                        val result = response.data

                        Log.d(TAG, "observeStatusOrder: ${result?.isNotEmpty()}")

                        if (result!!.isNotEmpty()) {
                            statusOrderAdapter.setDataOrder(result as List<ResultStatusOrder>?)
                            isNotEmptyStatusOrder(true)
                        } else {
                            isNotEmptyStatusOrder(false)
                        }
                    }
                } else {
                    showMessage(
                        requireActivity(),
                        getString(R.string.failed_title),
                        getString(R.string.failed_description),
                        MotionToast.TOAST_ERROR
                    )

                    layoutEmptyStatusOrder.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun moveToDetailStatusOrder(detailOrder: ResultStatusOrder) {
        val intent = Intent(requireContext(), DetailStatusOrderActivity::class.java).apply {
            putExtra(DetailStatusOrderActivity.EXTRA_DATA, detailOrder)
        }
        startActivity(intent)
    }

    private fun isNotEmptyStatusOrder(state: Boolean) {
        with(binding) {
            if (state) {
                rvHistoryStatusOrder.visibility = View.VISIBLE
                layoutEmptyStatusOrder.visibility = View.GONE
            } else {
                rvHistoryStatusOrder.visibility = View.GONE
                layoutEmptyStatusOrder.visibility = View.VISIBLE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        observeStatusOrder()
    }

    override fun onPause() {
        super.onPause()
        with(binding) {
            rvHistoryStatusOrder.visibility = View.GONE
            layoutEmptyStatusOrder.visibility = View.GONE
            pbStatusOrder.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}