package edu.isel.pdm.memorymatrix

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.android.parcel.Parcelize

/**
 * Data class used to represent UI saved state
 */
@Parcelize
private data class MatrixState(
    val toGuess: MatrixPattern?,
    val currentGuess: MatrixPattern?) : Parcelable

private const val SAVED_STATE_KEY = "MatrixViewModel.SavedState"

/**
 * View model for the memory game main activity
 */
class MatrixViewModel(private val savedState: SavedStateHandle) : ViewModel() {

    /**
     * The pattern to be guessed
     */
    var toGuess: MatrixPattern? = savedState.get<MatrixState>(SAVED_STATE_KEY)?.toGuess
        private set

    /**
     * The current user guesses
     */
    var current: MatrixPattern? = savedState.get<MatrixState>(SAVED_STATE_KEY)?.currentGuess
        private set

    /**
     * Starts a new guessing game.
     */
    fun startGame(guessCount: Int, matrixSide: Int): MatrixViewModel {
        toGuess = MatrixPattern.fromRandom(guessCount, matrixSide)
        current = MatrixPattern.empty(matrixSide)
        savedState[SAVED_STATE_KEY] = MatrixState(toGuess, current)
        return this
    }

    /**
     * Checks whether a guessing game is currently in progress or not.
     */
    fun isGameOngoing() = toGuess != null && toGuess?.count != current?.count

    /**
     * Adds a new guess to the current set of guesses.
     */
    fun addGuess(guess: Position): MatrixViewModel {
        current = current?.plus(guess)
        savedState[SAVED_STATE_KEY] = MatrixState(toGuess, current)
        return this
    }
}
