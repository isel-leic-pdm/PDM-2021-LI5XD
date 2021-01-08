package edu.isel.adeetc.pdm.tictactoe.game

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.work.*
import edu.isel.adeetc.pdm.tictactoe.TAG
import edu.isel.adeetc.pdm.tictactoe.TicTacToeApplication
import edu.isel.adeetc.pdm.tictactoe.challenges.ChallengeInfo
import edu.isel.adeetc.pdm.tictactoe.game.model.Game
import edu.isel.adeetc.pdm.tictactoe.game.model.Player


/**
 * The Game view model
 */
class GameViewModel(
    app: Application,
    val localPlayer: Player,
    val challengeInfo: ChallengeInfo
) : AndroidViewModel(app) {

    private val subscription = getApplication<TicTacToeApplication>().repository.subscribeTo(
            challengeInfo.id,
            onSubscriptionError = { TODO() },
            onStateChanged = {
                (gameState as MutableLiveData<Game>).value = it
            }
    )

    override fun onCleared() {
        super.onCleared()
        Log.v(TAG, "GameViewModel.onCleared()")
        subscription.remove()
    }

    private val app = getApplication<TicTacToeApplication>()
    val gameState: LiveData<Game> = MutableLiveData(Game())
    private var cleanupScheduled = false

    /**
     * Makes a move at the given position.
     */
    fun makeMoveAt(x: Int, y: Int): Player? {
        var played: Player? = null
        if (localPlayer == gameState.value?.nextTurn) {
            played = gameState.value?.makeMoveAt(x, y)
            app.repository.updateGameState(
                gameState.value ?: throw IllegalStateException(),
                challengeInfo,
                onSuccess = { (gameState as MutableLiveData<Game>).value = it },
                onError = { TODO() }
            )
        }
        return played
    }

    /**
     * Starts the game
     */
    fun start() {
        gameState.value?.start(Player.P1)
        app.repository.updateGameState(
            gameState.value ?: throw IllegalStateException(),
            challengeInfo,
            onSuccess = { (gameState as MutableLiveData<Game>).value = it },
            onError = { TODO() }
        )
    }

    /**
     * Forfeits the game if it's the local player's turn
     */
    fun forfeit() {
        if (localPlayer == gameState.value?.nextTurn) {
            gameState.value?.forfeit()
            app.repository.updateGameState(
                gameState.value ?: throw IllegalStateException(),
                challengeInfo,
                onSuccess = { (gameState as MutableLiveData<Game>).value = it },
                onError = { TODO() }
            )
        }
    }

    /**
     * Cleans up the shared game information
     */
    fun cleanupGame() {
        // Did we created the game? Has it already been scheduled?
        if (!cleanupScheduled && localPlayer == Player.P2) {

            cleanupScheduled = true
            val challengeID = challengeInfo.id
            val cleanupJob = OneTimeWorkRequestBuilder<AsyncGameStateCleanup>()
                .setInputData(Data.Builder().putString(CHALLENGE_ID_KEY, challengeID).build())
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.UNMETERED)
                        .setRequiresBatteryNotLow(true)
                        .build()
                )
                .build()

            WorkManager.getInstance(app).enqueue(cleanupJob)
        }
    }
}