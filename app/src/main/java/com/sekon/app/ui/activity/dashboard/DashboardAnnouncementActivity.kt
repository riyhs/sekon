package com.sekon.app.ui.activity.dashboard

import android.app.Activity
import android.content.Intent
import android.os.Bundle
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
import com.sekon.app.model.announcement.AnnouncementPostModel
import com.sekon.app.model.fcm.PostFCMBody
import com.sekon.app.model.fcm.PostFCMBodyDetail
import com.sekon.app.viewmodel.DashboardAnnouncementViewModel
import kotlinx.android.synthetic.main.activity_dashboard_pengumuman.*

class DashboardAnnouncementActivity : AppCompatActivity() {

    private lateinit var dashboardAnnouncementViewModel: DashboardAnnouncementViewModel
    private var filePath: String = "filepath"
    private var urlPhoto: String = "urlPhoto"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_pengumuman)

        dashboardAnnouncementViewModel = ViewModelProvider(this).get(DashboardAnnouncementViewModel::class.java)

        tv_add_photo_pengumuman.setOnClickListener {
            requestPermission()
        }

        bt_post_pengumuman.setOnClickListener {
            showLoading(true)

            val judul = et_judul_pengumuman.text
            val deskripsi = et_deskripsi_pengumuman.text

            if (judul != null && deskripsi != null && filePath != "filepath") {
                if (judul.isNotEmpty() && deskripsi.isNotEmpty()) {
                    uploadToCloudinary()
                }
            } else {
                Toast.makeText(this, "Data yang dimasukan harus lengkap!", Toast.LENGTH_SHORT).show()
                showLoading(false)
            }
        }
    }

    private fun initData(): AnnouncementPostModel {
        val judul = et_judul_pengumuman.text
        val deskripsi = et_deskripsi_pengumuman.text
        return AnnouncementPostModel(judul = judul.toString(), deskripsi = deskripsi.toString(), photo = urlPhoto)
    }

    private fun uploadToCloudinary() {
        dashboardAnnouncementViewModel.uploadToCloudinary(filePath)
        dashboardAnnouncementViewModel.getPhotoUrl().observe(this, {
            when(it) {
                is Resource.Loading -> {
                    showLoading(true)
                }
                is Resource.Success -> {
                    urlPhoto = it.data.toString()

                    uploadPengumuman(initData())

                    showLoading(false)
                }
                is Resource.Error -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    showLoading(false)
                }
            }
        })
    }

    private fun uploadPengumuman(announcement: AnnouncementPostModel) {
        dashboardAnnouncementViewModel.setPengumuman(announcement)
        dashboardAnnouncementViewModel.getPengumuman().observe(this, {
            when(it) {
                is Resource.Success -> {
                    if (it.data != null) {
                        val fcmBody = PostFCMBody(to = "/topics/pengumuman", notification = PostFCMBodyDetail(
                            body = it.data.result.deskripsi,
                            title = it.data.result.judul
                        ))

                        postFCM(fcmBody)

                        Toast.makeText(this, "Sukses Menambah Pengumuman", Toast.LENGTH_SHORT).show()
                        resetInputText()
                        showLoading(false)
                    }
                }

                is Resource.Loading -> {
                    showLoading(true)
                }

                is Resource.Error -> {
                    Toast.makeText(this, "Gagal : ${it.message}", Toast.LENGTH_SHORT).show()
                    showLoading(false)
                }
            }
        })
    }

    private fun postFCM(fcmBody: PostFCMBody) {
        dashboardAnnouncementViewModel.setFCM(fcmBody)
        dashboardAnnouncementViewModel.getFCM().observe(this, {
            when(it) {
                is Resource.Success -> {
                    showLoading(false)
                }
                is Resource.Loading -> {
                    showLoading(true)
                }
                is Resource.Error -> {
                    Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
                    showLoading(false)
                }
            }
        })
    }

    // image
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

                iv_add_penugumuman.setImageURI(fileUri)

                filePath = ImagePicker.getFilePath(data).toString()

                tv_add_photo_pengumuman.text = filePath
            }
            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun requestPermission() {
        Dexter.withContext(this)
            .withPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    pickImage()
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    Toast.makeText(
                        this@DashboardAnnouncementActivity,
                        "Tidak bisa memilih gambar",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?
                ) {
                    TODO("Not yet implemented")
                }
            })
            .check()
    }

    private fun resetInputText() {
        et_judul_pengumuman.setText("")
        et_deskripsi_pengumuman.setText("")
        tv_add_photo_pengumuman.text = "tambah foto"
        iv_add_penugumuman.setImageResource(0)
    }

    private fun showLoading(state: Boolean) {
        pb_pengumuman.isVisible = state
        ti_deskripsi_pengumuman.isInvisible = state
        ti_judul_pengumuman.isInvisible = state
        bt_post_pengumuman.isInvisible = state
        tv_add_photo_pengumuman.isInvisible = state
        iv_add_penugumuman.isInvisible = state
    }
}