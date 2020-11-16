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
    private var absenId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_absen)

        absenViewModel = ViewModelProvider(this).get(AbsenViewModel::class.java)

        getCheckedRadioButton()

        bt_post_absen.setOnClickListener {
            val id = getId(this)
            val deskripsi = et_deskripsi_absen.text.toString()

            if (id != null) {
                val absenBody = PostAbsenBody(id, id, deskripsi, absenId)
                postAbsen(absenBody)
            } else {
                Toast.makeText(this, "Tidak dapat absen, coba keluar aplikasi, lalu masuk lagi", Toast.LENGTH_LONG).show()
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
        rg_absen.setOnCheckedChangeListener{_, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            when (radio) {
                rb_hadir -> {
                    absenId = 1
                }

                rb_sakit -> {
                    absenId = 2
                }

                rb_izin -> {
                    absenId = 3
                }
            }
        }
    }

    private fun getId(context: Context): String? {
        val sharedPref = Preference.initPref(context, "onSignIn")
        return sharedPref.getString("id", "id")
    }
}