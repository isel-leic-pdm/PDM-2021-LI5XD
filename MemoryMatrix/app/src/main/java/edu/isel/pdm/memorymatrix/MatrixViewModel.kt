package edu.isel.pdm.memorymatrix

import android.os.Parcelable
import androidx.lifecycle.ViewModel
import kotlinx.android.parcel.Parcelize

/**
 * Data class used to represent UI saved state
 */
@Parcelize
data class MatrixState(
    val toGuess: MatrixPattern?,
    val currentGuess: MatrixPattern?) : Parcelable

/**
 * View model for the memory game main activity
 */
class MatrixViewModel : ViewModel() {

    /**
     * The pattern to be guessed
     */
    var toGuess: MatrixPattern? = null
        private set

    /**
     * The current user guesses
     */
    var current: MatrixPattern? = null
        private set

    /**
     * Starts a new guessing game.
     */
    fun startGame(guessCount: Int, matrixSide: Int): MatrixViewModel {
        toGuess = MatrixPattern.fromRandom(guessCount, matrixSide)
        current = MatrixPattern.empty(matrixSide)
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
        return this
    }

    /**
     * Creates the [MatrixState] holding the current UI state
     * @return the UI state to be saved
     */
    fun toMatrixState() = MatrixState(toGuess, current)

    /**
     * Loads the view model state from the given saved UI state
     * @param savedState    the saved UI state
     */
    fun fromMatrixState(savedState: MatrixState) {
        toGuess = savedState.toGuess
        current = savedState.currentGuess
    }
}
