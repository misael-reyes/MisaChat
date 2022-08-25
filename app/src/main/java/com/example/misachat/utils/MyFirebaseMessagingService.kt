package com.example.misachat.utils

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.misachat.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * clase que sera capas de recibir las notificaciones y mediante la cual
 * podremos manejarlas a nuestro antojo
 */

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MyFirebaseMessagingService: FirebaseMessagingService() {

    // properties for our notification channel

    private val channelId = "channelId"
    private val channelName = "channelName"
    private val notificationId  = 1

    /**
     * called when a new token for the default Firebase project is generated
     */
    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Log.i("token", "este es mi token $token")

        //saveToken(token)
    }

    private fun saveToken(token: String) {
        // guardaremos el token en la base de datos
        Firebase.firestore.collection("tokens").document().set(token)
    }

    /**
     * called when a message is received
     */
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        var title: String = ""
        var body: String = ""

        if (message.data.size >= 0) {
            title = message.data["title"].toString()
            body = message.data["detail"].toString()
        }

        createNotificationChannel()
        notify(title, body)
    }

    private fun notify(title: String, body: String) {
        // configuración de la notificación
        val notification = NotificationCompat.Builder(this, channelId).also {
            it.setContentTitle(title)
            it.setContentText(body)
            it.setSmallIcon(R.drawable.ic_email)
            it.priority = NotificationCompat.PRIORITY_HIGH
        }.build()

        //
        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(notificationId, notification)
    }

    private fun createNotificationChannel() {
        /**
         * a partir de android o (8) se implementaron los canales para las
         * notificaciones
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // importancia de la notificación
            val importance: Int = NotificationManager.IMPORTANCE_HIGH

            // creamos el canal
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                lightColor = Color.RED
                enableLights(true)
                enableVibration(true)
            }

            // necesitamos un notification manager para construir el canal
            // getSystemService regresa un any, por eso tenemos que haer el casteo a notification manager
            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }
}