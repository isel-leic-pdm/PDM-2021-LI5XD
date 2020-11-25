package edu.isel.pdm.memorymatrix

import android.content.SharedPreferences
import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import edu.isel.pdm.memorymatrix.game.data.MatrixPattern
import edu.isel.pdm.memorymatrix.game.history.GameResult
import edu.isel.pdm.memorymatrix.game.history.HistoryDatabase
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors

private const val HIGHEST_LEVEL_KEY = "HighestLevelKey"

/**
 * The game repository holding globally relevant information.
 *
 * Implementation note: I chose to use two different data sources (SQLite DB and SharedPreferences)
 * for demonstration purposes.
 */
class GameRepository(
    private val sharedPreferences: SharedPreferences,
    private val db: HistoryDatabase
) {

    companion object {
        private val worker: Executor = Executors.newSingleThreadExecutor()
    }

    /**
     * The highest reached level
     */
    var highestLevel: Int
        get() = sharedPreferences.getInt(HIGHEST_LEVEL_KEY, 1)
        private set(value) {
            sharedPreferences
                .edit()
                .putInt(HIGHEST_LEVEL_KEY, value)
                .apply()
        }

    /**
     * Saves the given game result in the history DB and, if it's the case, registers it as an
     * high score.
     *
     * @param toGuess   the original pattern
     * @param guesses   the player's guesses
     * @param score     the number of correct guesses
     */
    fun saveResult(toGuess: MatrixPattern, guesses: MatrixPattern, score: Int) {
        worker.execute {
            Log.v("GameRepository", "Executing Saving Result on thread ${Thread.currentThread().name}")
            db.getGameResultsDao().insertGame(GameResult(
                side = toGuess.side,
                toGuess = toGuess.toList(),
                guesses = guesses.toList(),
                score = score,
                date = Date()
            ))
        }
        Log.v("GameRepository", "Scheduled saving Result on thread ${Thread.currentThread().name}")
        if (score == toGuess.count && score > highestLevel)
            highestLevel = score
    }

    /**
     * Asynchronously fetches all existing scored (an unrealistic approach)
     *
     * @return the LiveData instance that will contain the data once the async operation is completed
     */
    fun getAllScores(): LiveData<List<GameResult>> {
        val results = MutableLiveData<List<GameResult>>()
        worker.execute {
            results.postValue(db.getGameResultsDao().loadAllGames())
        }
        return results
    }

    /**
     * Asynchronously fetches the most recent [count] results
     *
     * @param count the maximum number of results to be produced
     * @return the LiveData instance that will contain the data once the async operation is completed
     */
    fun getNScores(count: Int): LiveData<List<GameResult>> =
        db.getGameResultsDao().loadLastGames(count)
}