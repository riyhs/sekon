package com.sekon.app.ui.activity.dashboard

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.github.dhaval2404.imagepicker.ImagePicker
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.sekon.app.R
import com.sekon.app.model.Resource
import com.sekon.app.model.reference.PostReferenceBody
import com.sekon.app.viewmodel.DashboardReferenceViewModel
import kotlinx.android.synthetic.main.activity_dashboard_reference.*
import java.util.*

class DashboardReferenceActivity : AppCompatActivity() {

    private lateinit var dashboardReferenceViewModel: DashboardReferenceViewModel
    private var filePath: String = "filepath"
    private var urlPhoto: String = "urlPhoto"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_reference)

        dashboardReferenceViewModel = ViewModelProvider(this).get(DashboardReferenceViewModel::class.java)

        autoCompleteTextView()

        tv_add_reference_photo.setOnClickListener {
            requestPermission()
        }

        bt_post_referensi.setOnClickListener {
            validationData()
        }
    }

    private fun validationData() {
        val judul = et_judul_referensi.text
        val deskripsi = et_deskripsi_referensi.text
        val url = et_url_referensi.text
        val jurusan = actv_referensi.text

        if (judul != null && deskripsi != null && url != null && jurusan != null && filePath != "filepath") {
            if (judul.isNotEmpty() && deskripsi.isNotEmpty() && url.isNotEmpty() && jurusan.isNotEmpty()) {
                uploadToCloudinary()
            } else {
                Toast.makeText(this, "Data yang di upload harus lengkap", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun postRefence(referenceBody: PostReferenceBody) {
        dashboardReferenceViewModel.postReference(referenceBody)
        dashboardReferenceViewModel.getReference().observe(this, {
            when (it) {
                is Resource.Success -> {
                    if (it.data != null) {
                        Toast.makeText(this, it.data.message, Toast.LENGTH_LONG).show()
                        resetInput()
                        showLoading(false)
                    }
                }

                is Resource.Error -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    showLoading(false)
                }

                is Resource.Loading -> {
                    showLoading(true)
                }
            }
        })
    }

    private fun uploadToCloudinary() {
        dashboardReferenceViewModel.uploadToCloudinary(filePath)
        dashboardReferenceViewModel.getPhotoUrl().observe(this, {
            when (it) {
                is Resource.Loading -> {
                    showLoading(true)
                }
                is Resource.Success -> {
                    urlPhoto = it.data.toString()

                    postRefence(initData())

                    showLoading(false)
                }
                is Resource.Error -> {
                    Toast.makeText(this, it.message.toString(), Toast.LENGTH_LONG).show()
                    showLoading(false)
                }
            }
        })
    }

    private fun initData(): PostReferenceBody {
        val judul = et_judul_referensi.text
        val deskripsi = et_deskripsi_referensi.text
        val url = et_url_referensi.text
        val jurusan = actv_referensi.text

        return PostReferenceBody(
            deskripsi = deskripsi.toString(),
            judul = judul.toString(),
            src = url.toString(),
            jurusan = jurusan.toString().toLowerCase(Locale.ROOT),
            photo = urlPhoto
        )
    }

    // image

    private fun requestPermission() {
        Dexter.withContext(this)
            .withPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    pickImage()
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    Toast.makeText(this@DashboardReferenceActivity, "Izinkan Aplikasi mengakses Penyimpanan melalui pengaturan", Toast.LENGTH_LONG).show()
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?
                ) {
                    p1?.continuePermissionRequest()
                }
            })
            .check()
    }

    private fun pickImage() {
        ImagePicker.with(this)
            .crop(3f, 2f)
            .compress(100)
            .maxResultSize(1440, 1440)
            .galleryMimeTypes(
                mimeTypes = arrayOf(
                    "image/png",
                    "image/jpg",
                    "image/jpeg"
                )
            )
            .start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                val fileUri = data?.data

                iv_add_reference.setImageURI(fileUri)

                filePath = ImagePicker.getFilePath(data).toString()

                tv_add_reference_photo.text = filePath
            }
            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun autoCompleteTextView() {
        val kelas = arrayOf("RPL", "TKJ", "TEI", "TP", "TKR", "TBSM")
        val actvAdapter = ArrayAdapter(this, android.R.layout.select_dialog_item, kelas)

        actv_referensi.threshold = 0
        actv_referensi.setAdapter(actvAdapter)
    }

    private fun showLoading(state: Boolean) {
        pb_post_referensi.isVisible = state
        ti_judul_referensi.isInvisible = state
        ti_deskripsi_referensi.isInvisible = state
        ti_url_referensi.isInvisible = state
        ti_jurusan_referensi.isInvisible = state
        bt_post_referensi.isInvisible = state
        tv_add_reference_photo.isInvisible = state
        iv_add_reference.isInvisible = state
    }

    private fun resetInput() {
        et_judul_referensi.setText("")
        et_deskripsi_referensi.setText("")
        et_url_referensi.setText("")
        actv_referensi.setText("")
        tv_add_reference_photo.text = "tambah foto"
        iv_add_reference.setImageResource(0)
    }
}