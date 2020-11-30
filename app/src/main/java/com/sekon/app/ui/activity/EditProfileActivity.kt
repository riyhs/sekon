package com.sekon.app.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.sekon.app.R
import com.sekon.app.model.Resource
import com.sekon.app.model.SiswaUpdateBody
import com.sekon.app.utils.Preference
import com.sekon.app.viewmodel.EditProfileViewModel
import kotlinx.android.synthetic.main.activity_edit_profile.*

class EditProfileActivity : AppCompatActivity() {

    private lateinit var editProfileViewModel: EditProfileViewModel
    private var filePath: String = "filepath"
    private var urlPhoto: String = "urlPhoto"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        editProfileViewModel = ViewModelProvider(this).get(EditProfileViewModel::class.java)

        val id = getId(this)
        if (id != null) {
            setSiswaDetail(id)
        }

        floatingActionButton.setOnClickListener {
            requestPermission()
        }

        bt_save_edit.setOnClickListener {
            if (id != null) {
                if (filePath == "filepath") {
                    editTagLineOnly(id)
                } else {
                    uploadToCloudinary(filePath, id)
                }
            }
        }
    }

    private fun editTagLineOnly(id: String) {
        val tagline = et_edit_tagline.text.toString()
        val body = SiswaUpdateBody(urlPhoto, tagline)

        editProfileViewModel.setUpdatePhoto(id, body)
        editProfileViewModel.getSiswa().observe(this, {
            when (it) {
                is Resource.Loading -> {
                    showLoading(true)
                }
                is Resource.Success -> {
                    Toast.makeText(this, "Berhasil", Toast.LENGTH_SHORT).show()
                    showLoading(false)
                }
                is Resource.Error -> {
                    Toast.makeText(
                        this,
                        "Gagal, coba lagi, pastikan tersedia internet!",
                        Toast.LENGTH_LONG
                    ).show()
                    showLoading(false)
                }
            }
        })
    }

    private fun pickImage() {
        ImagePicker.with(this)
            .crop(1f, 1f)
            .compress(50)
            .maxResultSize(256, 256)
            .galleryMimeTypes(  //Exclude gif images
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
                iv_edit_profile.setImageURI(fileUri)

                filePath = ImagePicker.getFilePath(data)!!
            }
            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            }
            else -> {
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
                        this@EditProfileActivity,
                        "Izinkan Aplikasi mengakses Penyimpanan melalui pengaturan",
                        Toast.LENGTH_SHORT
                    ).show()
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

    private fun setSiswaDetail(id: String) {
        editProfileViewModel.setProfileUrl(id)
        editProfileViewModel.getSiswa().observe(this, {
            when (it) {
                is Resource.Loading -> {
                    showLoading(true)
                }

                is Resource.Error -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    showLoading(false)
                }

                is Resource.Success -> {
                    Glide.with(this)
                        .load(it.data?.photo)
                        .into(iv_edit_profile)

                    et_edit_tagline.setText(it.data?.tagline)

                    urlPhoto = it.data?.photo.toString()

                    showLoading(false)
                }
            }
        })
    }

    private fun uploadToCloudinary(mFilePath: String, id: String) {
        editProfileViewModel.uploadToCloudinary(mFilePath)
        editProfileViewModel.getNewUrl().observe(this, {
            when (it) {
                is Resource.Loading -> {
                    showLoading(true)
                }
                is Resource.Success -> {
                    val url = it.data.toString()
                    val tagline = et_edit_tagline.text.toString()

                    val body = SiswaUpdateBody(url, tagline)

                    editProfileViewModel.setUpdatePhoto(id, body)

                    Toast.makeText(this, "Berhasil", Toast.LENGTH_SHORT).show()

                    showLoading(false)
                }
                is Resource.Error -> {
                    Toast.makeText(this, "Gagal, coba lagi, pastikan tersedia internet!", Toast.LENGTH_LONG).show()
                    showLoading(false)
                }
            }
        })
    }

    private fun showLoading(state: Boolean) {
        pb_edit_profile.isVisible = state
        iv_edit_profile.isInvisible = state
        ti_edit_tagline.isInvisible = state
        bt_save_edit.isInvisible = state
        floatingActionButton.isInvisible = state
    }

    private fun getId(context: Context): String? {
        val sharedPref = Preference.initPref(context, "onSignIn")
        return sharedPref.getString("id", "id")
    }
}