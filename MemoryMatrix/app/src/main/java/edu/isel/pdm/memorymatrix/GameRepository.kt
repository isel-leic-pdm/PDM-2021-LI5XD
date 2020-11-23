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

    fun getAllScores(): LiveData<List<GameResult>> {
        val results = MutableLiveData<List<GameResult>>()
        worker.execute {
            results.postValue(db.getGameResultsDao().loadAllGames())
        }
        return results
    }


    fun getNScores(count: Int): LiveData<List<GameResult>> =
        db.getGameResultsDao().loadLastGames(count)
}