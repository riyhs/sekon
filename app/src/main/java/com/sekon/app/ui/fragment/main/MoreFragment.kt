package com.sekon.app.ui.fragment.main

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.firebase.messaging.FirebaseMessaging
import com.sekon.app.R
import com.sekon.app.model.SiswaResponseDetail
import com.sekon.app.ui.activity.EditProfileActivity
import com.sekon.app.ui.activity.SplashActivity
import com.sekon.app.utils.Preference
import com.sekon.app.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_more.*

@Suppress("DEPRECATION")
class MoreFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_more, container, false)
    }

    override fun onResume() {
        super.onResume()
        getId(requireActivity())?.let { mainViewModel.setSiswaDetail(it) }
        mainViewModel.getSiswaDetail().observe(viewLifecycleOwner, {
            if (it != null) {
                setupLayout(it.result)

                Glide.with(this)
                    .load(it.result.photo)
                    .centerCrop()
                    .into(iv_profile)

            } else {
                Toast.makeText(context, "Tidak dapat mengambil data siswa", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private lateinit var mainViewModel: MainViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onViewCreated(view, savedInstanceState)

        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        val idSiswa: String = getId(requireContext()).toString()

        getMainViewModel(idSiswa)
        onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.action_bar_nav_profile, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_bar_log_out -> {
                Preference.logOut(requireContext())
                FirebaseMessaging.getInstance().unsubscribeFromTopic("pengumuman")
                startActivity(
                    Intent(activity?.applicationContext, SplashActivity::class.java)
                )
                activity?.finish()
            }

            R.id.action_edit_profile -> {
                activity?.startActivity(
                    Intent(context, EditProfileActivity::class.java)
                )
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getMainViewModel(id: String) {
        mainViewModel.setSiswaDetail(id)
        mainViewModel.getSiswaDetail().observe(viewLifecycleOwner, {

            if (it != null) {
                setupLayout(it.result)

                Glide.with(this)
                    .load(it.result.photo)
                    .centerCrop()
                    .into(iv_profile)

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

    private fun getId(context: Context): String? {
        val sharedPref = Preference.initPref(context, "onSignIn")
        return sharedPref.getString("id", "id")
    }
}