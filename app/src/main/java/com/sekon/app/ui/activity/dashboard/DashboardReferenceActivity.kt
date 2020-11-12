package com.sekon.app.ui.activity.dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.sekon.app.R
import com.sekon.app.model.Resource
import com.sekon.app.model.reference.PostReferenceBody
import com.sekon.app.viewmodel.DashboardReferenceViewModel
import kotlinx.android.synthetic.main.activity_dashboard_reference.*
import java.util.*

class DashboardReferenceActivity : AppCompatActivity() {

    private lateinit var dashboardReferenceViewModel: DashboardReferenceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_reference)

        dashboardReferenceViewModel = ViewModelProvider(this).get(DashboardReferenceViewModel::class.java)

        autoCompleteTextView()
        bt_post_referensi.setOnClickListener {
            validationData()
        }
    }

    private fun autoCompleteTextView() {
        val kelas = arrayOf("RPL", "TKJ", "TEI", "TP", "TKR", "TBSM")
        val actvAdapter = ArrayAdapter(this, android.R.layout.select_dialog_item, kelas)

        actv_referensi.threshold = 0
        actv_referensi.setAdapter(actvAdapter)
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

    private fun showLoading(state: Boolean) {
        pb_post_referensi.isVisible = state
        ti_judul_referensi.isInvisible = state
        ti_deskripsi_referensi.isInvisible = state
        ti_url_referensi.isInvisible = state
        ti_jurusan_referensi.isInvisible = state
        bt_post_referensi.isInvisible = state
    }

    private fun resetInput() {
        et_judul_referensi.setText("")
        et_deskripsi_referensi.setText("")
        et_url_referensi.setText("")
        actv_referensi.setText("")
    }

    private fun validationData() {
        val judul = et_judul_referensi.text
        val deskripsi = et_deskripsi_referensi.text
        val url = et_url_referensi.text
        val jurusan = actv_referensi.text

        if (judul != null && deskripsi != null && url != null && jurusan != null) {
            if (judul.isNotEmpty() && deskripsi.isNotEmpty() && url.isNotEmpty() && jurusan.isNotEmpty()) {
                val reference = PostReferenceBody(
                    deskripsi = deskripsi.toString(),
                    judul = judul.toString(),
                    src = url.toString(),
                    jurusan = jurusan.toString().toLowerCase(Locale.ROOT),
                    photo = ""
                )
                postRefence(reference)
            } else {
                Toast.makeText(this, "Data yang di upload harus lengkap", Toast.LENGTH_LONG).show()
            }
        }
    }
}