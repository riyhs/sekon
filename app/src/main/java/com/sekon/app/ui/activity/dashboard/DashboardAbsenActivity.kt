package com.sekon.app.ui.activity.dashboard

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import com.sekon.app.R
import kotlinx.android.synthetic.main.activity_dashboard_absen.*
import java.io.File

@Suppress("DEPRECATION")
class DashboardAbsenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_absen)

        bt_absen_download.setOnClickListener {
            downloadPdf(this, "https://repository.dinus.ac.id/docs/ajar/Mastering_Kode_HTML_-_Full.pdf", "Testing")
        }
    }

    private fun downloadPdf(baseActivity: Context, url: String?, title: String?): Long {
        val direct = File(Environment.getExternalStorageDirectory().toString() + "/your_folder")

        if (!direct.exists()) {
            direct.mkdirs()
        }
        val extension = url?.substring(url.lastIndexOf("."))
        val downloadReference: Long
        val dm = baseActivity.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val uri = Uri.parse(url)
        val request = DownloadManager.Request(uri)
        request.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOCUMENTS,
            "pdf" + System.currentTimeMillis() + extension
        )
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setTitle(title)
        Toast.makeText(baseActivity, "start Downloading..", Toast.LENGTH_SHORT).show()

        downloadReference = dm.enqueue(request)

        return downloadReference

    }
}