package com.sekon.app.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.bumptech.glide.Glide
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.sekon.app.R
import com.sekon.app.ui.activity.SplashActivity
import com.sekon.app.utils.Preference


class FirebaseCMService: FirebaseMessagingService() {
    companion object {
        private const val TAG = "FIREBASE_CM_TAG"
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        Log.d(TAG, "Refreshed token: $p0")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        sendNotification(remoteMessage)
        countNotif()
    }

    private fun countNotif() {
        val sharedPref = Preference.initPref(this, "notification")
        val oldTotalNotif = sharedPref.getInt("total", 0)
        val total = oldTotalNotif + 1

        Preference.addNotif(total, this)
    }

    private fun sendNotification(message: RemoteMessage) {
        val channelId = getString(R.string.default_notification_channel_id)
        val channelName = getString(R.string.default_notification_channel_name)

        val intent = Intent(this, SplashActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("fragment", "pengumuman")
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_s_blue_logo)
            .setContentText(message.data["title"])
            .setContentTitle(message.data["body"])
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val futureTarget = Glide.with(this)
            .asBitmap()
            .load(message.data["image"].toString())
            .submit()

        val bitmap = futureTarget.get()

        notificationBuilder.setLargeIcon(bitmap)
        notificationBuilder.setStyle(
            NotificationCompat.BigPictureStyle()
                .bigPicture(bitmap)
                .bigLargeIcon(null)
        )

        Glide.with(this).clear(futureTarget)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationBuilder.setChannelId(channelId)
            mNotificationManager.createNotificationChannel(channel)
        }
        val notification = notificationBuilder.build()
        mNotificationManager.notify(0, notification)
    }

}