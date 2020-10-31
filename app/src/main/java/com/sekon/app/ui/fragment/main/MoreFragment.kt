package com.sekon.app.ui.fragment.main

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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
import com.sekon.app.R
import com.sekon.app.model.SiswaResponseDetail
import com.sekon.app.utils.Cloudinary
import com.sekon.app.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_more.*
import java.util.regex.Pattern

class MoreFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_more, container, false)
    }

    private lateinit var mainViewModel: MainViewModel

    companion object {
        private const val PICK_IMAGE = 665
    }

    private var config: MutableMap<String, String> = HashMap()
    private var url: String? = null
    private var mFilePath: String? = null


    private fun configCloudinary() {
        config["cloud_name"] = Cloudinary.CLOUD_NAME
        config["api_key"] = Cloudinary.API_KEY
        config["api_secret"] = Cloudinary.API_SECRET
        MediaManager.init(requireContext(), config)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configCloudinary()
        setupViewModel()
        onBackPressed()

        tv_change_profile_image.setOnClickListener {
            requestPermission()
        }

    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        mainViewModel.getSiswaDetail().observe(viewLifecycleOwner, {
            if (it != null) {
                setupLayout(it.result)
            } else {
                Toast.makeText(context, "Tidak dapat mengambil data siswa", Toast.LENGTH_SHORT).show()
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun setupLayout(siswa: SiswaResponseDetail) {
        tv_more_siswa_name.text = siswa.nama
        tv_more_siswa_tagline.text = "\"${siswa.tagline}\""
        tv_more_siswa_nis.text = siswa.nis.toString()
        tv_more_siswa_kelas.text = siswa.kelas
    }

    private fun onBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.replace(R.id.frame_container, HomeFragment(), HomeFragment::class.java.simpleName)
                    ?.commit()

                activity?.bottomNavigationView?.selectedItemId = R.id.bottom_nav_home
            }
        })
    }

    private fun requestPermission() {
        Dexter.withActivity(requireActivity())
            .withPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    pickImage()
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    Toast.makeText(requireContext(), "Tidak bisa memilih gambar", Toast.LENGTH_SHORT).show()
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
            .withActivity(activity)
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
                uploadToCloudinary(mFilePath)
            }
        }
    }

    private fun getImageFilePath(filePath: String?) {
        iv_profile.setImageURI(filePath?.toUri())
        iv_profile.isVisible = true
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
                Toast.makeText(requireContext(), url, Toast.LENGTH_SHORT).show()

            }

            override fun onError(requestId: String, error: ErrorInfo) {
            }

            override fun onReschedule(requestId: String, error: ErrorInfo) {
            }
        }).dispatch()
    }

}