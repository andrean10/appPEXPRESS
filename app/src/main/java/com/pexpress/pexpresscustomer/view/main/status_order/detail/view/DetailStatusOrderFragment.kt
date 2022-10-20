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
import com.pexpress.pexpresscustomer.R
import com.pexpress.pexpresscustomer.databinding.FragmentDetailStatusOrderBinding
import com.pexpress.pexpresscustomer.model.status_order.ResultStatusOrder
import com.pexpress.pexpresscustomer.utils.FormatDate
import com.pexpress.pexpresscustomer.utils.UtilsCode.PATTERN_DATE_POST
import com.pexpress.pexpresscustomer.utils.UtilsCode.PATTERN_DATE_VIEW
import com.pexpress.pexpresscustomer.utils.formatRupiah
import com.pexpress.pexpresscustomer.view.main.order.viewmodel.OrderPaketViewModel
import com.pexpress.pexpresscustomer.view.main.status_order.detail.DetailStatusOrderActivity.Companion.EXTRA_DATA

class DetailStatusOrderFragment : Fragment() {

    private var _binding: FragmentDetailStatusOrderBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<OrderPaketViewModel>()
    private var dataStatusOrder: ResultStatusOrder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailStatusOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataStatusOrder = requireActivity().intent.getParcelableExtra(EXTRA_DATA)

        setToolbar()
        observeJenisLayanan()
        observeUkuranBarang()
        observeJenisBarang()
        setupView()

        with(binding) {
            tvDetailOrder.setOnClickListener { moveToMilestone() }
            tvDetailResi.setOnClickListener { moveToResi() }
        }
    }

    private fun moveToMilestone() {
        val toMilestone =
            DetailStatusOrderFragmentDirections.actionDetailStatusOrderFragmentToMilestoneFragment()
                .apply {
                    id = dataStatusOrder?.reffno ?: 0
                }
        findNavController().navigate(toMilestone)
    }

    private fun moveToResi() {
        val toResi =
            DetailStatusOrderFragmentDirections.actionDetailStatusOrderFragmentToResiFragment()
                .apply {
                    noInvoice = dataStatusOrder?.nomorpemesanan.toString()
                }
        findNavController().navigate(toResi)
    }

    private fun setupView() {
        with(binding) {
            dataStatusOrder?.also {
                val tanggalPickup = FormatDate().formatedDate(
                    it.tanggalpickup,
                    PATTERN_DATE_POST,
                    PATTERN_DATE_VIEW
                )

                tvStatusOrder.text = it.namastatuspengiriman.toString()
                tvNomorPemesanan.text = it.nomorpemesanan.toString()
                tvPengirim.text = it.namapengirim.toString()
                tvPenerima.text = it.namapenerima.toString()
                tvTanggalPickup.text = tanggalPickup
                tvTelponPengirim.text = it.teleponpengirim.toString()
                tvTelponPenerima.text = it.teleponpenerima.toString()
                tvInformasiAsalPengirim.text = it.gkecamatanpengirim.toString()
                tvInformasiTujuanPenerima.text = it.gkecamatanpenerima.toString()
                tvPatokanAlamatPengirim.text = it.alamatpengirim
                tvPatokanAlamatPenerima.text = it.alamatpenerima
                tvPembayaran.text = "${it.jenispembayaran?.replace("Pembayaran ", "")} " +
                        if (it.type != "Tunai") {
                            "(${it.bank?.replace("ID_", "")})"
                        } else {
                            ""
                        }
                tvBerat.text = getString(R.string.dimension_weight_size_2, it.berat)
                tvOngkir.text = formatRupiah(it.biaya?.toDouble() ?: 0.0)
            }
        }
    }

    private fun observeJenisLayanan() {
        viewModel.layanan()
        viewModel.jenisLayanan.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                val result = response.detail
                dataStatusOrder?.also { detailOrder ->
                    result?.also {
                        for (dataLayanan in it) {
                            if (dataLayanan.idlayanan == detailOrder.jenispengiriman) {
                                binding.tvJenisLayanan.text = dataLayanan.layanan!!
                                break
                            }
                        }
                    }
                }
            }
        }
    }

    private fun observeUkuranBarang() {
        viewModel.ukuran()
        viewModel.jenisUkuran.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                val result = response.detail
                dataStatusOrder?.also { detailOrder ->
                    result?.also {
                        for (dataUkuran in it) {
                            if (dataUkuran.idjenisukuran == detailOrder.jenisukuran) {
                                binding.tvUkuranBarang.text = dataUkuran.jenisukuran!!
                                break
                            }
                        }
                    }
                }
            }
        }
    }

    private fun observeJenisBarang() {
        viewModel.barang()
        viewModel.jenisBarang.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                val result = response.detail
                dataStatusOrder?.also { detailOrder ->
                    result?.also {
                        for (dataBarang in it) {
                            if (dataBarang.id == detailOrder.jenisbarang?.toInt()) {
                                binding.tvJenisBarang.text = dataBarang.namajenisbarang
                                break
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) requireActivity().finish()
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