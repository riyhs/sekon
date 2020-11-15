package com.sekon.app.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.sekon.app.R
import com.sekon.app.model.Resource
import com.sekon.app.utils.Preference
import com.sekon.app.viewmodel.EditProfileViewModel
import kotlinx.android.synthetic.main.activity_edit_profile.*

class EditProfileActivity : AppCompatActivity() {

    private lateinit var editProfileViewModel: EditProfileViewModel

    companion object {
        const val TITLE = "title"
        const val ID = "id"

        fun getIntent(context: Context, postId: Int): Intent {
            return Intent(context, EditProfileActivity::class.java).apply {
                putExtra(ID, postId)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        editProfileViewModel = ViewModelProvider(this).get(EditProfileViewModel::class.java)

        val id = getId(this)
        if (id != null) {
            getUrlProfileImage(id)
        }
    }

    private fun getUrlProfileImage(id: String) {
        editProfileViewModel.setProfileUrl(id)
        editProfileViewModel.getProfileUrl().observe(this, {
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
                        .load(it.data)
                        .into(iv_edit_profile)

                    showLoading(false)
                }
            }
        })
    }

    private fun showLoading(state: Boolean) {
        pb_edit_profile.isVisible = state
        iv_edit_profile.isInvisible = state
        et_edit_tagline.isInvisible = state
    }

    private fun getId(context: Context): String? {
        val sharedPref = Preference.initPref(context, "onSignIn")
        return sharedPref.getString("id", "id")
    }
}