package edu.isel.adeetc.pdm.tictactoe.game

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.app.Service
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.os.IBinder
import android.os.Parcelable
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.firestore.ListenerRegistration
import edu.isel.adeetc.pdm.tictactoe.R
import edu.isel.adeetc.pdm.tictactoe.TAG
import edu.isel.adeetc.pdm.tictactoe.TicTacToeApplication
import edu.isel.adeetc.pdm.tictactoe.challenges.ChallengeInfo
import edu.isel.adeetc.pdm.tictactoe.game.model.Player

/**
 * Service used to host the listener of changes on the state of the game. This ensures
 * that the notification is received even if the user has navigated away from the user task.
 */
class GameStateListener : Service() {

    /**
     * The listener registration
     */
    private var listenerRegistration: ListenerRegistration? = null

    private lateinit var challenge: ChallengeInfo
    private lateinit var localPlayer: Player
    private lateinit var nextTurn: Player

    private val notificationManager by lazy {
        getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }

    private fun initializeFromIntent(intent: Intent?) {
        challenge = intent?.extras?.getParcelable(ACCEPTED_CHALLENGE_EXTRA)
            ?: throw IllegalArgumentException("Mandatory extra $ACCEPTED_CHALLENGE_EXTRA not present")

        localPlayer = intent.extras?.getParcelable(LOCAL_PLAYER_EXTRA)
            ?: throw IllegalArgumentException("Mandatory extra $LOCAL_PLAYER_EXTRA not present")

        nextTurn = intent.extras?.getParcelable(TURN_PLAYER_EXTRA)
            ?: throw IllegalArgumentException("Mandatory extra $TURN_PLAYER_EXTRA not present")
    }

    private fun buildNotification(nextTurn: Player, gameStateChanged: Boolean): Notification {
        val app = (application as TicTacToeApplication)
        val pendingIntent: PendingIntent =
            Intent(this, GameActivity::class.java).let { notificationIntent ->
                notificationIntent
                    .addFlags(FLAG_ACTIVITY_CLEAR_TOP)
                    .putExtra(ACCEPTED_CHALLENGE_EXTRA, challenge)
                    .putExtra(LOCAL_PLAYER_EXTRA, localPlayer as Parcelable)
                    .putExtra(TURN_PLAYER_EXTRA, nextTurn as Parcelable)
                PendingIntent.getActivity(this, 0, notificationIntent, FLAG_UPDATE_CURRENT)
            }

        return NotificationCompat.Builder(app, app.MOVES_NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(getString(
                if(gameStateChanged) R.string.move_made_notification_title
                else R.string.ongoing_game_notification_title
            ))
            .setContentText(
                if (gameStateChanged) getString(R.string.move_made_notification_text)
                else getString(R.string.ongoing_game_notification_text, challenge.challengerMessage)
            )
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }

    /**
     * Callback method executed whenever an intent is sent to the service
     */
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.v(TAG, "GameStateListener.onStartCommand()")

        initializeFromIntent(intent)

        val shouldListen: Boolean = localPlayer != nextTurn
        if (shouldListen) {
            val app = application as TicTacToeApplication
            Log.d(TAG, "Registered game state listener")
            listenerRegistration = app.repository.subscribeTo(
                challenge.id,
                onSubscriptionError = {
                    Log.w(TAG, "Listen failed.", it)
                    stopSelfResult(startId)
                },
                onStateChanged = {
                    Log.d(TAG, "Current data: $it")
                    // Publish notification
                    notificationManager.notify(
                        app.MOVES_NOTIFICATION_ID,
                        buildNotification(it.nextTurn ?: nextTurn, true)
                    )
                }
            )
        }

        startForeground(startId, buildNotification(nextTurn, false))
        return START_REDELIVER_INTENT
    }

    /**
     * Callback method that signals the service termination
     */
    override fun onDestroy() {
        Log.v(TAG, "GameStateListener.onDestroy()")
        listenerRegistration?.remove()
        notificationManager.cancelAll()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
