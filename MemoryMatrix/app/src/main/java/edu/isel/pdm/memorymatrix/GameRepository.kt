package edu.isel.pdm.memorymatrix

import android.content.SharedPreferences
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
        /*
        TODO: Uncomment this code block and see what happens... ;)
        db.getGameResultsDao().insertGame(GameResult(
            side = toGuess.side,
            toGuess = toGuess.toList(),
            guesses = guesses.toList(),
            score = score,
            date = Date()
        ))
         */

        if (score == toGuess.count && score > highestLevel)
            highestLevel = score
    }

    fun getAllScores(): List<GameResult> = db.getGameResultsDao().loadAllGames()
}