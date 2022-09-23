package com.pexpress.pexpresscustomer.view.main.ongkir.detail

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.pexpress.pexpresscustomer.R
import com.pexpress.pexpresscustomer.databinding.ActivityDetailOngkirBinding
import com.pexpress.pexpresscustomer.model.fix_rate.ongkir.ResultCheckOngkirFixRate
import com.pexpress.pexpresscustomer.model.kilometer.ongkir.ResultCheckOngkirKilometer
import com.pexpress.pexpresscustomer.utils.UtilsCode
import com.pexpress.pexpresscustomer.utils.UtilsCode.TYPE_PACKAGE_FIXRATE
import com.pexpress.pexpresscustomer.utils.UtilsCode.TYPE_PACKAGE_KILOMETER
import com.pexpress.pexpresscustomer.utils.formatRupiah

class DetailOngkirActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailOngkirBinding

    companion object {
        const val EXTRA_TYPE_PACKAGE = "extra_type_package"
        const val EXTRA_DATA = "extra_data"
        const val EXTRA_DATA_ASAL = "extra_data_asal"
        const val EXTRA_DATA_TUJUAN = "extra_data_tujuan"
        const val EXTRA_DATA_JARAK = "extra_data_jarak"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailOngkirBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val idTypePackage = intent.getIntExtra(EXTRA_TYPE_PACKAGE, 0)

        setToolbar()
        setupView(idTypePackage)
    }

    private fun setupView(idTypePackage: Int) {
        with(binding) {
            val asal = intent.getStringExtra(EXTRA_DATA_ASAL)
            val tujuan = intent.getStringExtra(EXTRA_DATA_TUJUAN)

            when (idTypePackage) {
                TYPE_PACKAGE_FIXRATE -> {
                    val data = intent.getParcelableExtra<ResultCheckOngkirFixRate>(EXTRA_DATA)
                    data?.also {
                        tvOngkirAsal.text = asal
                        tvOngkirTujuan.text = tujuan
                        tvOngkirJenisLayanan.text = it.layanan
                        tvOngkirInfoLayanan.text = it.informasipengiriman
                        tvOngkirUkuranBarang.text = it.jenisukuran
                        tvOngkirBeratBarang.text =
                            getString(R.string.dimension_weight_size_2, it.maksimalberat)
                        tvOngkir.text = formatRupiah(it.tarif?.toDouble() ?: 0.0)
                    }

                    placeholderJarakTempuh.visibility = View.GONE
                    tvOngkirJarakTempuh.visibility = View.GONE
                }
                TYPE_PACKAGE_KILOMETER -> {
                    val data = intent.getParcelableExtra<ResultCheckOngkirKilometer>(EXTRA_DATA)
                    val jarak = intent.getStringExtra(EXTRA_DATA_JARAK)
                    data?.also {
                        tvOngkirAsal.text = asal
                        tvOngkirTujuan.text = tujuan
                        tvOngkirJenisLayanan.text = it.layanan
                        tvOngkirInfoLayanan.text = it.informasipengiriman
                        tvOngkirUkuranBarang.text = it.jenisukuran
                        tvOngkirBeratBarang.text =
                            getString(R.string.dimension_weight_size_2, it.maksimalberat)
                        tvOngkirJarakTempuh.text =
                            getString(R.string.kilometer_jarak, jarak?.toDouble().toString())
                        tvOngkir.text = formatRupiah(it.biaya?.toDouble() ?: 0.0)
                    }
                }
                else -> {}
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

    private fun setToolbar() {
        setSupportActionBar(binding.toolbar)
    }
}