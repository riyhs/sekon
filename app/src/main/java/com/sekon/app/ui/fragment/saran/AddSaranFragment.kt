package com.sekon.app.ui.fragment.saran

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.nbsp.materialfilepicker.MaterialFilePicker
import com.nbsp.materialfilepicker.ui.FilePickerActivity
import com.sekon.app.R
import com.sekon.app.model.Resource
import com.sekon.app.model.saran.PostSaran
import com.sekon.app.utils.Preference
import com.sekon.app.viewmodel.SaranViewModel
import kotlinx.android.synthetic.main.fragment_add_saran.*
import java.util.regex.Pattern

@Suppress("DEPRECATION")
class AddSaranFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_saran, container, false)
    }

    companion object {
        private const val PICK_IMAGE = 664
    }

    private var mFilePath: String? = null
    private lateinit var saranViewModel: SaranViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        saranViewModel = ViewModelProvider(this).get(SaranViewModel::class.java)

        tv_saran_pick_image.setOnClickListener {
            requestPermission()
        }

        bt_saran_upload.setOnClickListener {
            showLoading(true)
            if (mFilePath == null) {
                postSaran(url = "", initId(requireContext())!!)
            } else {
                uploadToCloudinary(mFilePath.toString(), initId(requireContext())!!)
            }
        }

    }

    private fun uploadToCloudinary(filePath: String, id: String) {
        saranViewModel.uploadToCloudinary(filePath)
        saranViewModel.getImageUrl().observe(viewLifecycleOwner, { url ->
            postSaran(url, id)
        })
    }

    private fun postSaran(url: String, id: String) {

        val judul = ti_judul_saran.text
        val deskripsi = ti_deskripsi_saran.text

        Log.d("SARAN", "post Saran")

        if (judul != null && deskripsi != null) {
            if (judul.isNotEmpty() && deskripsi.isNotEmpty()) {

                Log.d("SARAN", "masuk")

                val saran = PostSaran(
                    saran = judul.toString(),
                    deskripsi = deskripsi.toString(),
                    kelas = id,
                    nama = id,
                    photo = url
                )

                saranViewModel.postSaran(saran)
                saranViewModel.getPostSaranResponse().observe(viewLifecycleOwner, {
                    when (it) {
                        is Resource.Success -> {
                            showLoading(false)
                            Toast.makeText(activity?.applicationContext, "berhasil", Toast.LENGTH_SHORT).show()
                            Log.d("SARAN", "berhasil")
                        }
                        is Resource.Loading -> {
                            showLoading(true)
                            Toast.makeText(activity?.applicationContext, "loading", Toast.LENGTH_SHORT).show()
                            Log.d("SARAN", "loading")
                        }
                        is Resource.Error -> {
                            showLoading(false)
                            Toast.makeText(activity?.applicationContext, it.message.toString(), Toast.LENGTH_SHORT).show()
                            Log.d("SARAN", "error")
                        }
                    }
                })
            } else {
                Log.d("SARAN", "else")
                showLoading(false)
                Toast.makeText(activity?.applicationContext, "kosong", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initId(context: Context): String? {
        val sharedPref = Preference.initPref(context, "onSignIn")
        return sharedPref.getString("id", "id")
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
            .withSupportFragment(this)
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

    private fun showLoading(state: Boolean) {
        pb_saran.isVisible = state
        ti_saran_title.isInvisible = state
        ti_saran_description.isInvisible = state
        tv_saran_pick_image.isInvisible = state
        bt_saran_upload.isInvisible = state
        iv_saran.isInvisible = state
    }
}