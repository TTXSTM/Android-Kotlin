package space.mairi.application_architecture

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        private const val PUSH_KEY_TITLE = "title"
        private const val PUSH_KEY_MESSAGE = "message"

        private const val CHANNEL_ID = "channel_id"
        private const val NOTIFICATION_ID = 37
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Log.v("MyFirebaseMessagingService", "onNewToken " + token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        Log.v("MyFirebaseMessagingService", "onMessageReceived")

        val remoteMessageData = message.data

        if (remoteMessageData.isNotEmpty()) {
            handleDataMessage(remoteMessageData.toMap())
        }
    }

    private fun handleDataMessage(data : Map<String, String>) {
        val title = data[PUSH_KEY_TITLE]
        val message = data[PUSH_KEY_MESSAGE]

        if (!title.isNullOrBlank() && !message.isNullOrBlank()){
            showNotification(title, message)
        }
    }

    private fun showNotification(title: String, message: String) {
        val notificationBuilder = NotificationCompat.Builder(applicationContext, CHANNEL_ID).apply {
            setSmallIcon(R.drawable.ic_earth)
            setContentTitle(title)
            setContentText(message)
            priority = NotificationCompat.PRIORITY_DEFAULT
        }

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val name = "Cannel name"
        val descriptionText = "Channel Description"

        val importance = NotificationManager.IMPORTANCE_DEFAULT

        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }

        notificationManager.createNotificationChannel(channel)
    }
}