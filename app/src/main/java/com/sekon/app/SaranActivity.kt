package com.sekon.app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.nbsp.materialfilepicker.MaterialFilePicker
import com.nbsp.materialfilepicker.ui.FilePickerActivity
import com.sekon.app.utils.Cloudinary.API_KEY
import com.sekon.app.utils.Cloudinary.API_SECRET
import com.sekon.app.utils.Cloudinary.CLOUD_NAME
import kotlinx.android.synthetic.main.activity_saran.*
import java.util.regex.Pattern

@Suppress("DEPRECATION")
class SaranActivity : AppCompatActivity() {

    companion object {
        private const val PICK_IMAGE = 664
    }

    private var config: MutableMap<String, String> = HashMap()
    private var url: String? = null
    private var mFilePath: String? = null


    private fun configCloudinary() {
        config["cloud_name"] = CLOUD_NAME
        config["api_key"] = API_KEY
        config["api_secret"] = API_SECRET
        MediaManager.init(this@SaranActivity, config)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saran)
        configCloudinary()

        tv_saran_pick_image.setOnClickListener {
            requestPermission()
        }

        bt_saran_upload.setOnClickListener {
            showLoading(true)
            uploadToCloudinary(mFilePath)
        }

    }

    private fun requestPermission() {
        Dexter.withActivity(this)
            .withPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    pickImage()
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    Toast.makeText(applicationContext, "Tidak bisa memilih gambar", Toast.LENGTH_SHORT).show()
                }

                override fun onPermissionRationaleShouldBeShown(p0: PermissionRequest?, p1: PermissionToken?) {
                    TODO("Not yet implemented")
                }
            })
            .check()
    }

    private fun pickImage() {
        val externalStorage = Environment.getExternalStorageDirectory()

        MaterialFilePicker()
            .withActivity(this@SaranActivity)
            .withCloseMenu(true)
            .withFilter(Pattern.compile(".*\\.(jpg|jpeg|png)$"))
            .withFilterDirectories(false)
            .withRootPath(externalStorage.absolutePath)
            .withTitle("Pick a image")
            .withRequestCode(PICK_IMAGE)
            .start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            val filePath = data?.getStringExtra(FilePickerActivity.RESULT_FILE_PATH)

            if (filePath != null) {
                getImageFilePath(filePath)
                mFilePath = filePath
            }
        }
    }

    private fun getImageFilePath(filePath: String?) {
        iv_saran.setImageURI(filePath?.toUri())
        iv_saran.isVisible = true
    }

    private fun uploadToCloudinary(filePath: String?) {
        MediaManager.get().upload(filePath).callback(object : UploadCallback {
            override fun onStart(requestId: String) {
            }

            override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {
            }

            override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                url = resultData["url"].toString()
                Log.d("URL", url!!)
                Toast.makeText(applicationContext, url, Toast.LENGTH_SHORT).show()

            }

            override fun onError(requestId: String, error: ErrorInfo) {
            }

            override fun onReschedule(requestId: String, error: ErrorInfo) {
            }
        }).dispatch()

        showLoading(false)
    }

    private fun showLoading(state: Boolean) {
        pb_saran.isVisible = state
        ti_saran_title.isInvisible = state
        ti_saran_description.isInvisible = state
        tv_saran_pick_image.isInvisible = state
        bt_saran_upload.isInvisible = state
        iv_saran.isInvisible = state
    }
}