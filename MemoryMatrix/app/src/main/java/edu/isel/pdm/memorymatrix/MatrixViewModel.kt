package edu.isel.pdm.memorymatrix

import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import edu.isel.pdm.memorymatrix.GameState.State.*
import kotlinx.android.parcel.Parcelize

private fun runDelayed(delay: Long, action: ()-> Unit) {
    Handler(Looper.getMainLooper()).postDelayed({ action() }, delay)
}

/**
 * Data class used to represent the game saved state
 */
@Parcelize
data class GameState(
    val toGuess: MatrixPattern? = null,
    val currentGuess: MatrixPattern? = null,
    val state: State = NOT_STARTED) : Parcelable {

    enum class State { NOT_STARTED, MEMORIZING, GUESSING, ENDED }
}

private const val SAVED_STATE_KEY = "MatrixViewModel.SavedState"

/**
 * View model for the memory game main activity
 */
class MatrixViewModel(private val savedState: SavedStateHandle) : ViewModel() {

    /**
     * The game state.
     */
    var game: GameState = savedState.get<GameState>(SAVED_STATE_KEY) ?: GameState()
        private set

    /**
     * The game state listener.
     * TODO: This is broken. Fix it!
     * (A small teaser: what happens if a reconfiguration occurs while the event is being dispatched?)
     */
    var gameListener: ((GameState) -> Unit)? = null

    /**
     * Starts a new guessing game. The game will be in the [GameState.State.MEMORIZING] for the next
     * [memorizeFor] seconds
     *
     * @param guessCount    the number of elements in the pattern to be memorized
     * @param matrixSide    the side of the matrix containing the pattern to be memorized
     * @param memorizeFor   the number of seconds that the game remains in the [GameState.State.MEMORIZING]
     * state before moving on to the [GameState.State.GUESSING].
     */
    fun startGame(guessCount: Int, matrixSide: Int, memorizeFor: Int): MatrixViewModel {
        if (game.state != NOT_STARTED && game.state != ENDED)
            throw IllegalStateException()

        game = GameState(
            toGuess = MatrixPattern.fromRandom(guessCount, matrixSide),
            currentGuess = MatrixPattern.empty(matrixSide),
            state = MEMORIZING)
        gameListener?.invoke(game)

        runDelayed(memorizeFor * 1000L) {
            game = GameState(game.toGuess, game.currentGuess, GUESSING)
            gameListener?.invoke(game)
            savedState[SAVED_STATE_KEY] = game
        }
        return this
    }

    /**
     * Adds a new guess to the current set of guesses.
     */
    fun addGuess(guess: Position): MatrixViewModel {
        if (game.state != GUESSING)
            throw IllegalStateException()

        val toGuess = game.toGuess ?: throw IllegalStateException()
        val current = game.currentGuess?.plus(guess) ?: throw IllegalStateException()
        game = GameState(toGuess, current, if (toGuess.count == current.count) ENDED else GUESSING)
        gameListener?.invoke(game)
        savedState[SAVED_STATE_KEY] = game
        return this
    }
}
