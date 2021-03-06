package com.sekon.app.ui.fragment.main

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sekon.app.R
import com.sekon.app.adapter.AnnouncementAdapter
import com.sekon.app.adapter.decoration.MarginItemDecorationVertical
import com.sekon.app.model.Resource
import com.sekon.app.model.announcement.AnnouncementResponseDetail
import com.sekon.app.utils.Preference
import com.sekon.app.viewmodel.AnnouncementViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_announcement.*

class AnnouncementFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_announcement, container, false)
    }

    private lateinit var announcementViewModel: AnnouncementViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        announcementViewModel = ViewModelProvider(this).get(AnnouncementViewModel::class.java)

        onBackPressed()

        setupViewModel()

        Preference.addNotif(0, requireContext())
        activity?.bottomNavigationView?.removeBadge(R.id.bottom_nav_announcement)
    }

    private fun setupViewModel() {
        announcementViewModel.setAnnouncement()
        announcementViewModel.getAnnouncement().observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    val announcements = response.data?.result

                    if (announcements != null && announcements.isNotEmpty()) {
                        setupAdapter(announcements)
                        showLoading(false)
                    } else {
                        Toast.makeText(context, "Tidak ada pengumuman", Toast.LENGTH_SHORT).show()
                        showLoading(false)
                    }
                }

                is Resource.Loading -> {
                    showLoading(true)
                }

                is Resource.Error -> {
                    val err = response.message
                    Toast.makeText(context, err.toString(), Toast.LENGTH_SHORT).show()
                    showLoading(false)
                }
            }
        })
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            pb_announcement.isVisible = state
            rv_announcment.isInvisible = state
        } else {
            pb_announcement.isVisible = state
            rv_announcment.isInvisible = state
        }
    }

    private fun setupAdapter(list: List<AnnouncementResponseDetail>?) {
        val margin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16f, resources.displayMetrics)
        val layoutManager = LinearLayoutManager(context)

        layoutManager.stackFromEnd = true
        layoutManager.reverseLayout = true

        rv_announcment.addItemDecoration(MarginItemDecorationVertical(margin.toInt(), true))
        rv_announcment.setHasFixedSize(true)
        rv_announcment.layoutManager = layoutManager
        rv_announcment.adapter = AnnouncementAdapter(list)
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
}