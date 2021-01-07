package edu.isel.adeetc.pdm.tictactoe

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

/**
 * Tag to be used in all the application's log messages
 */
const val TAG = "TicTacToe"

/**
 * Contains the globally accessible objects
 */
class TicTacToeApplication : Application() {

    val MOVES_NOTIFICATION_CHANNEL_ID: String = "MovesNotificationChannelId"
    val MOVES_NOTIFICATION_ID: Int = 14434

    /**
     * Function used to create the channel to where game state change notifications will be sent
     */
    private fun createNotificationChannels() {
        // Create notification channel if we are running on a O+ device
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                MOVES_NOTIFICATION_CHANNEL_ID,
                getString(R.string.moves_channel_name),
                NotificationManager.IMPORTANCE_LOW).apply {
                    description = getString(R.string.moves_channel_description)
                }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }

    }

    /**
     * The game's repository
     */
    val repository by lazy {
        Repository(
            jacksonObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        )
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }
}