package com.sekon.app.ui.activity

import android.content.Context
import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.sekon.app.R
import com.sekon.app.model.Resource
import com.sekon.app.model.absen.PostAbsenBody
import com.sekon.app.utils.Preference
import com.sekon.app.viewmodel.AbsenViewModel
import kotlinx.android.synthetic.main.activity_absen.*

class AbsenActivity : AppCompatActivity() {

    private lateinit var absenViewModel: AbsenViewModel
    private var absenStatus = "alpha"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_absen)

        absenViewModel = ViewModelProvider(this).get(AbsenViewModel::class.java)

        getCheckedRadioButton()

        bt_post_absen.setOnClickListener {
            val deskripsi = et_deskripsi_absen.text.toString()

            if (getData("id", this, false) != null) {
                val absenBody = PostAbsenBody(
                    nama = getData("name", this, false).toString(),
                    kelas = getData("kelas", this, false).toString(),
                    nis = getData("nis", this, true) as Int,
                    deskripsi = deskripsi,
                    status = absenStatus
                )
                postAbsen(absenBody)
            } else {
                Toast.makeText(
                    this,
                    "Tidak dapat absen, coba keluar aplikasi, lalu masuk lagi",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun postAbsen(absenBody: PostAbsenBody) {
        absenViewModel.postAbsen(absenBody)
        absenViewModel.getPostAbsenResponse().observe(this, {
            when (it) {
                is Resource.Loading -> {
                    showLoading(true)
                }

                is Resource.Success -> {
                    Toast.makeText(applicationContext, it.data?.status, Toast.LENGTH_SHORT).show()
                    showLoading(false)
                }

                is Resource.Error -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    showLoading(false)
                }
            }
        })
    }

    private fun showLoading(state: Boolean) {
        pb_absen.isVisible = state
        card_absen.isInvisible = state
        bt_post_absen.isInvisible = state
    }

    private fun getCheckedRadioButton() {
        rg_absen.setOnCheckedChangeListener{ _, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            when (radio) {
                rb_hadir -> {
                    absenStatus = "Hadir"
                }

                rb_sakit -> {
                    absenStatus = "Sakit"
                }

                rb_izin -> {
                    absenStatus = "Izin"
                }
            }
        }
    }

    private fun getData(name: String, context: Context, isInt: Boolean): Any? {
        val sharedPref = Preference.initPref(context, "onSignIn")
        return if (isInt) {
            sharedPref.getInt(name, 0)
        } else {
            sharedPref.getString(name, name)
        }
    }
}