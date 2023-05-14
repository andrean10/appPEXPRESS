package com.pexpress.pexpresscustomer.view.main.status_order.resi

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.pexpress.pexpresscustomer.BuildConfig
import com.pexpress.pexpresscustomer.R
import com.pexpress.pexpresscustomer.databinding.FragmentResiBinding
import com.pexpress.pexpresscustomer.model.resi.ResultResi
import com.pexpress.pexpresscustomer.utils.*
import com.pexpress.pexpresscustomer.view.dialog.DialogLoadingFragment
import com.pexpress.pexpresscustomer.view.main.order.viewmodel.OrderPaketViewModel
import www.sanju.motiontoast.MotionToast
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class ResiFragment : Fragment() {

    private var _binding: FragmentResiBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<OrderPaketViewModel>()

    private var readPermissionGranted = false
    private var writePermissionGranted = false
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var loadingFragment: DialogLoadingFragment

    var path: String? = null
    var bitmapResi: Bitmap? = null

    private var file_name = "Screenshot"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar()
        setupView()
    }

    private fun setupView() {
        loadingFragment = DialogLoadingFragment()
        loadingFragment.loader(parentFragmentManager, true)

        val noInvoice = ResiFragmentArgs.fromBundle(arguments as Bundle).noInvoice
        observerResi(noInvoice)

        resultPermission()
    }

    private fun observerResi(noInvoince: String) {
        with(binding) {
            viewModel.checkResi(noInvoince).observe(viewLifecycleOwner) { response ->
                loadingFragment.loader(parentFragmentManager, false)
                if (response != null) {
                    if (response.success!!) {
                        response.data?.let {
                            val result = it[0]
                            prepareResi(result)
                            observeJenisLayanan(result.jenispengiriman)
                            if (result.jenisbarang.isNullOrEmpty()) {
                                tvJenisBarang.text = result.jenisbaranglainnya
                            } else {
                                observeJenisBarang(result.jenisbarang.toString())
                            }
                            observeJenisUkuran(result.jenisukuran, result)
                        }
                        layoutReceipt.visibility = View.VISIBLE
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
    }

    private fun observeJenisLayanan(idLayanan: Int?) {
        with(binding) {
            viewModel.layanan()
            viewModel.jenisLayanan.observe(viewLifecycleOwner) { response ->
                if (response != null) {
                    if (response.success!!) {
                        if (idLayanan != null) {
                            for (result in response.detail!!) {
                                if (result.idlayanan == idLayanan) {
                                    tvJenisLayanan.text = result.layanan
                                    break
                                }
                            }
                        } else {
                            tvJenisLayanan.visibility = View.GONE
                        }
                    } else {
                        tvJenisLayanan.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun observeJenisBarang(idBarang: String?) {
        with(binding) {
            viewModel.barang()
            viewModel.jenisBarang.observe(viewLifecycleOwner) { response ->
                if (response != null) {
                    if (response.success!!) {
                        if (!idBarang.isNullOrEmpty()) {
                            for (result in response.detail!!) {
                                if (result.id == idBarang.toInt()) {
                                    tvJenisBarang.text = result.namajenisbarang
                                    break
                                }
                            }
                        } else {
                            tvJenisBarang.visibility = View.GONE
                        }
                    } else {
                        tvJenisBarang.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun observeJenisUkuran(idUkuran: Int?, resi: ResultResi) {
        with(binding) {
            viewModel.ukuran()
            viewModel.jenisUkuran.observe(viewLifecycleOwner) { response ->
                if (response != null) {
                    if (response.success!!) {
                        if (idUkuran != null) {
                            for (result in response.detail!!) {
                                if (result.idjenisukuran == idUkuran) {
                                    tvBerat.text = getString(
                                        R.string.dimension_weight_size,
                                        result.jenisukuran,
                                        resi.berat.toString()
                                    )
                                    break
                                }
                            }
                        } else {
                            tvBerat.visibility = View.GONE
                        }
                    } else {
                        tvBerat.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun prepareResi(resi: ResultResi) {
        with(binding) {
            tvNoResi.apply {
                text = resi.nomortracking
                setOnClickListener { requireContext().copyText(tvNoResi.text) }
            }
            tvTanggalTransaction.text = resi.tglcreate
            tvAsalPengirim.text =
                "${resi.district?.capitalizeEachWords}, ${resi.regencies?.capitalizeEachWords}"
            tvNamaPengirim.text = resi.namapengirim
            tvTelponPengirim.text = resi.teleponpengirim
            tvNamaPenerima.text = resi.namapenerima
            tvTelponPenerima.text = resi.teleponpenerima
            tvAlamatPenerima.text = resi.gkecamatanpenerima
            tvPatokan.text = resi.alamatpenerima
            tvCatatanPengiriman.text = resi.catatanpengirim
            tvOngkir.text = formatRupiah(resi.biaya?.toDouble() ?: 0.0)

            val writer = MultiFormatWriter()
            try {
                val matrix = writer.encode(resi.nomortracking, BarcodeFormat.QR_CODE, 350, 350)
                val encoder = BarcodeEncoder()
                val bitmap = encoder.createBitmap(matrix)
                imgBarcodeResi.setImageBitmap(bitmap)
            } catch (e: WriterException) {
                imgBarcodeResi.setImageResource(R.color.bg_gray)
                e.printStackTrace()
            }
        }
    }

    private fun setupDownloadResi() {
        val u: View = binding.receipt
        val z = binding.receipt
        val totalHeight = z.getChildAt(0).height
        val totalWidth = z.getChildAt(0).width
        val isSavedSuccesfully = if (writePermissionGranted) {
            bitmapResi = getBitmapFromView(u, totalHeight, totalWidth)
            saveResi(bitmapResi!!)
        } else {
            false
        }

        if (isSavedSuccesfully) {
            showMessage(
                requireActivity(),
                getString(R.string.text_success),
                "Berhasil Menyimpan Resi",
                MotionToast.TOAST_SUCCESS
            )
        } else {
            showMessage(
                requireActivity(),
                getString(R.string.failed_title),
                "Gagal Menyimpan Resi",
                MotionToast.TOAST_ERROR
            )
        }
    }

    private fun resultPermission() {
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                readPermissionGranted =
                    permissions[Manifest.permission.READ_EXTERNAL_STORAGE] ?: readPermissionGranted
                writePermissionGranted = permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE]
                    ?: writePermissionGranted
            }
        updateOrRequestPermission()
    }

    private fun getBitmapFromView(view: View, totalHeight: Int, totalWidth: Int): Bitmap {
        val returnedBitmap = Bitmap.createBitmap(totalWidth, totalHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(returnedBitmap)
        val bgDrawable = view.background
        if (bgDrawable != null) {
            bgDrawable.draw(canvas)
        } else {
            canvas.drawColor(Color.GRAY)
        }
        view.draw(canvas)
        return returnedBitmap
    }

    private fun updateOrRequestPermission() {
        val hasReadPermission = checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE,
        ) == PackageManager.PERMISSION_GRANTED
        val hasWritePermission = checkSelfPermission(
            requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        val minSdk29 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
        readPermissionGranted = hasReadPermission
        writePermissionGranted = hasWritePermission || minSdk29

        val permissionToRequest = mutableListOf<String>()
        if (!writePermissionGranted) {
            permissionToRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (!readPermissionGranted) {
            permissionToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (permissionToRequest.isNotEmpty()) {
            permissionLauncher.launch(permissionToRequest.toTypedArray())
        }
    }

    private fun saveResi(bmp: Bitmap): Boolean {
        val imageCollection = sdk29AndUp {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } ?: MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val contentValues = ContentValues().apply {
            put(
                MediaStore.Images.Media.DISPLAY_NAME,
                "${file_name}${System.currentTimeMillis()}.jpg"
            )
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.WIDTH, bmp.width)
            put(MediaStore.Images.Media.HEIGHT, bmp.height)
        }

        return try {
            requireActivity().contentResolver.insert(imageCollection, contentValues)?.also { uri ->
                requireActivity().contentResolver.openOutputStream(uri).use { outputStream ->
                    if (!bmp.compress(Bitmap.CompressFormat.JPEG, 95, outputStream)) {
                        throw IOException("Tidak bisa menyimpan gambar")
                    }
                }
            } ?: throw IOException("Tidak bisa membuat Entry Mediastore")
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.resi_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> findNavController().navigateUp()
            R.id.receipt_download -> {
                showMessage(
                    requireActivity(),
                    getString(R.string.text_success),
                    "Sedang mendownload resi",
                    MotionToast.TOAST_INFO
                )
                setupDownloadResi()
            }
            R.id.receipt_share -> {
                val u: View = binding.receipt
                val z = binding.receipt
                val totalHeight = z.getChildAt(0).height
                val totalWidth = z.getChildAt(0).width
                shareReceipt(bitmapResi ?: getBitmapFromView(u, totalHeight, totalWidth))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun shareReceipt(bitmap: Bitmap) {
        //---Save bitmap to external cache directory---//
        //get cache directory
        val cachePath = File(requireActivity().getExternalFilesDir(null), "cache_img/")
        cachePath.mkdirs()

        //create png file
        val file = File(cachePath, "${file_name}${System.currentTimeMillis()}.png")
        val fileOutputStream: FileOutputStream
        try {
            fileOutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        //---Share File---//
        //get file uri
        val myImageFileUri = FileProvider.getUriForFile(
            requireContext(),
            BuildConfig.APPLICATION_ID + ".provider",
            file
        )

        //create a intent
        val intent = Intent(Intent.ACTION_SEND)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.putExtra(Intent.EXTRA_STREAM, myImageFileUri)
        intent.type = "image/png"
        startActivity(Intent.createChooser(intent, "Share with"))
    }

    private fun setToolbar() {
        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.toolbar)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}