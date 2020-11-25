package edu.isel.pdm.memorymatrix.game

import android.app.Application
import android.os.Parcelable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import edu.isel.pdm.memorymatrix.MemoryMatrixApplication
import edu.isel.pdm.memorymatrix.game.GameState.State.*
import edu.isel.pdm.memorymatrix.game.data.MatrixPattern
import edu.isel.pdm.memorymatrix.game.data.Position
import edu.isel.pdm.memorymatrix.utils.confinedLazy
import edu.isel.pdm.memorymatrix.utils.runDelayed
import kotlinx.android.parcel.Parcelize

private const val SAVED_STATE_KEY = "MatrixViewModel.SavedState"

/**
 * Data class used to represent the game saved state
 */
@Parcelize
data class GameState(
    val toGuess: MatrixPattern? = null,
    val currentGuess: MatrixPattern? = null,
    val state: State = NOT_STARTED) : Parcelable {

    enum class State { NOT_STARTED, MEMORIZING, GUESSING, ENDED }

    fun correctResultsCount(): Int? {
        val correctGuesses = toGuess?.filter { currentGuess?.contains(it) ?: false }?.count()
        return if (correctGuesses == toGuess?.count) correctGuesses else null
    }
}

/**
 * View model for the memory game main activity
 */
class MatrixViewModel(
    application: Application,
    private val savedState: SavedStateHandle
) : AndroidViewModel(application) {

    /**
     * The game repository.
     */
    private val gameRepository by confinedLazy {
        getApplication<MemoryMatrixApplication>().gameRepository
    }

    /**
     * The game state.
     */
    val game: MutableLiveData<GameState> by confinedLazy {
        MutableLiveData<GameState>(savedState.get<GameState>(SAVED_STATE_KEY) ?: GameState())
    }

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
        if (game.value?.state != NOT_STARTED && game.value?.state != ENDED)
            throw IllegalStateException()

        game.value = GameState(
            toGuess = MatrixPattern.fromRandom(guessCount, matrixSide),
            currentGuess = MatrixPattern.empty(matrixSide),
            state = MEMORIZING)

        runDelayed(memorizeFor * 1000L) {
            game.value = GameState(game.value?.toGuess, game.value?.currentGuess, GUESSING)
            savedState[SAVED_STATE_KEY] = game.value
        }
        return this
    }

    /**
     * Adds a new guess to the current set of guesses.
     */
    fun addGuess(guess: Position): MatrixViewModel {
        if (game.value?.state != GUESSING)
            throw IllegalStateException()

        val toGuess = game.value?.toGuess ?: throw IllegalStateException()
        val current = game.value?.currentGuess?.plus(guess) ?: throw IllegalStateException()
        game.value = GameState(toGuess, current, if (toGuess.count == current.count) ENDED else GUESSING)

        if (game.value?.state == ENDED ) {
            gameRepository.saveResult(toGuess, current, game.value?.correctResultsCount() ?: 0)
        }

        savedState[SAVED_STATE_KEY] = game.value
        return this
    }
}
