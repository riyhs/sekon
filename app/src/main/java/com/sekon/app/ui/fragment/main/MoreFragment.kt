package com.sekon.app.ui.fragment.main

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
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
import com.sekon.app.model.SiswaResponseDetail
import com.sekon.app.model.SiswaUpdateBody
import com.sekon.app.ui.activity.SplashActivity
import com.sekon.app.utils.Preference
import com.sekon.app.viewmodel.MainViewModel
import com.sekon.app.viewmodel.MoreViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_more.*
import java.util.regex.Pattern

@Suppress("DEPRECATION")
class MoreFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_more, container, false)
    }

    private lateinit var mainViewModel: MainViewModel
    private lateinit var moreViewModel: MoreViewModel

    companion object {
        private const val PICK_IMAGE = 665
    }

    private var idSiswa: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onViewCreated(view, savedInstanceState)

        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        moreViewModel = ViewModelProvider(requireActivity()).get(MoreViewModel::class.java)

        getMainViewModel()
        onBackPressed()

        tv_change_profile_image.setOnClickListener {
            requestPermission()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.action_bar_nav_profile, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_bar_log_out -> {
                Toast.makeText(context, "Log Out", Toast.LENGTH_SHORT).show()
                Preference.logOut(requireContext())
                startActivity(
                    Intent(activity?.applicationContext, SplashActivity::class.java)
                )
                activity?.finish()
            }

            R.id.action_edit_profile -> {
                Toast.makeText(context, "Edit Profile", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun uploadToCloudinary(filePath: String) {
        moreViewModel.uploadToCloudinary(filePath)
        moreViewModel.getSiswaUrl().observe(viewLifecycleOwner, {
            Log.d("PHOTO_URL", it.toString())
            val siswaUpdateBody = SiswaUpdateBody(it)
            Log.d("PHOTO_URL", siswaUpdateBody.toString())
            moreViewModel.setUpdatePhoto(idSiswa.toString(), siswaUpdateBody, requireContext(), iv_profile)
        })
    }

    private fun getMainViewModel() {
        mainViewModel.getSiswaDetail().observe(viewLifecycleOwner, {
            if (it != null) {
                setupLayout(it.result)
                idSiswa = it.result._id

                moreViewModel.setupGlide(requireContext(), it.result.photo, iv_profile)

            } else {
                Toast.makeText(context, "Tidak dapat mengambil data siswa", Toast.LENGTH_SHORT)
                    .show()
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
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    activity?.supportFragmentManager
                        ?.beginTransaction()
                        ?.replace(
                            R.id.frame_container,
                            HomeFragment(),
                            HomeFragment::class.java.simpleName
                        )
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
                    Toast.makeText(
                        requireContext(),
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

    private fun pickImage() {
        val externalStorage = Environment.getExternalStorageDirectory()

        MaterialFilePicker()
            .withSupportFragment(this)
            .withCloseMenu(true)
            .withFilter(Pattern.compile(".*\\.(jpg|jpeg|png)$"))
            .withFilterDirectories(false)
            .withRootPath(externalStorage.absolutePath)
            .withTitle("Pick a image ds")
            .withRequestCode(PICK_IMAGE)
            .start()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            val filePath = data?.getStringExtra(FilePickerActivity.RESULT_FILE_PATH)

            if (filePath != null) {
                uploadToCloudinary(filePath)
            }
        }
    }
}