package com.pexpress.pexpresscustomer.view.main.status_order.detail.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.pexpress.pexpresscustomer.R
import com.pexpress.pexpresscustomer.databinding.FragmentMilestoneBinding
import com.pexpress.pexpresscustomer.utils.showMessage
import com.pexpress.pexpresscustomer.view.main.status_order.adapter.MilestoneAdapter
import com.pexpress.pexpresscustomer.view.main.status_order.viewmodel.HistoryViewModel
import www.sanju.motiontoast.MotionToast

class MilestoneFragment : Fragment() {

    private var _binding: FragmentMilestoneBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<HistoryViewModel>()
    private lateinit var milestoneAdapter: MilestoneAdapter
    private var idPengiriman = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMilestoneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        idPengiriman = MilestoneFragmentArgs.fromBundle(arguments as Bundle).id

        setToolbar()
        setupView()
        prepareData()
    }

    private fun setupView() {
        with(binding) {
            milestoneAdapter = MilestoneAdapter()
            with(rvMilestone) {
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
                addItemDecoration(
                    DividerItemDecoration(requireContext(), DividerItemDecoration.HORIZONTAL)
                )
                adapter = milestoneAdapter
            }
        }

        milestoneAdapter.setOnItemClickCallBack(object : MilestoneAdapter.OnItemClickCallBack {
            override fun onItemClicked(fotoPenerima: String) {
                moveToBuktiPengiriman(fotoPenerima)
            }
        })
    }

    private fun moveToBuktiPengiriman(fotoPenerima: String) {
        val toProofDelivery =
            MilestoneFragmentDirections.actionMilestoneFragmentToProofDeliveryFragment(fotoPenerima)
        findNavController().navigate(toProofDelivery)
    }

    private fun prepareData() {
//        Log.d(TAG, "prepareData: $idPengiriman")
        with(binding) {
            viewModel.milestone(idPengiriman)
            viewModel.milestone.observe(viewLifecycleOwner) { response ->
                pbMilestone.visibility = View.GONE
                if (response != null) {
                    if (response.success!!) {
                        val result = response.data
                        milestoneAdapter.setDataMilestone(result)

                        rvMilestone.visibility = View.VISIBLE
                    } else {
                        layoutEmptyMilestone.visibility = View.VISIBLE
                    }
                } else {
                    layoutEmptyMilestone.visibility = View.VISIBLE
                    showMessage(
                        requireActivity(),
                        getString(R.string.failed_title),
                        getString(R.string.failed_description),
                        MotionToast.TOAST_ERROR
                    )
                }
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