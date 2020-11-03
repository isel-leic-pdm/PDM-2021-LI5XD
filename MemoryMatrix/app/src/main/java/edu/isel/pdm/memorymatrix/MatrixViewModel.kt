package edu.isel.pdm.memorymatrix

import androidx.lifecycle.ViewModel

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
}
