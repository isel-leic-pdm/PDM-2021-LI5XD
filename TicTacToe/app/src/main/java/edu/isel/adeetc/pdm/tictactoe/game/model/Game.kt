package edu.isel.adeetc.pdm.tictactoe.game.model

import android.os.Parcelable
import edu.isel.adeetc.pdm.tictactoe.game.model.Game.State
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

private const val STATE_FIELD = "state"
private const val NEXT_TURN_FIELD = "nextTurn"
private const val WINNER_FIELD = "winner"
private const val BOARD_FIELD = "contents"


/**
 * The Tic-Tac-Toe full viewModel state.
 *
 * The viewModel is in one of three states: [State.NOT_STARTED], [State.STARTED] and [State.FINISHED]
 *
 * Upon instantiation the viewModel is placed in the [State.NOT_STARTED] state. Subsequent calls to the
 * [reset] method also place the viewModel in the same state. The transition to the [State.STARTED] is
 * then promoted by a call to the [start] method, where the first player to move is specified.
 * The viewModel remains in this state until the viewModel is finished, either because a winner emerged or
 * the viewModel was tied. Either way, the viewModel is placed in the [State.FINISHED] state.
 *
 * Valid state transitions are:
 * [State.NOT_STARTED]  -> [State.STARTED]
 * [State.STARTED]      -> [State.FINISHED]
 * [State.FINISHED]     -> [State.STARTED] (through a call to [start])
 * [State.FINISHED]     -> [State.NOT_STARTED] (through a call to [reset])
 */
@Parcelize
data class Game(
    private var board: Board = Board(),
    private var turn: Player? = null,
    private var winner: Player? = null,
    private var currState: State = State.NOT_STARTED
) : Parcelable {

    companion object {
        fun fromString(content: String): Game {
            // TODO
            return Game()
        }
    }
    /**
     * Enumeration of the game's possible states
     */
    enum class State { NOT_STARTED, STARTED, FINISHED }

    /**
     * Gets the move at the given position.
     * @return  the player that made the move, or null if there's no move at the position
     * @throws IllegalStateException if the viewModel is the [State.NOT_STARTED] state
     */
    fun getMoveAt(x: Int, y: Int): Player? {
        check(currState != State.NOT_STARTED)
        return board[x, y]
    }

    /**
     * Makes a move at the given position.
     * @return  the player that made the move, or null if the move was not legal
     * @throws IllegalStateException if the viewModel is NOT in the [State.STARTED] state
     */
    fun makeMoveAt(x: Int, y: Int): Player? {

        check(currState == State.STARTED)

        return if (board[x, y] == null) {
            val playerThatMoved = turn as Player
            board[x, y] = playerThatMoved
            turn = if (playerThatMoved == Player.P1) Player.P2 else Player.P1
            winner = board.getWinner()
            if (winner != null || board.isTied()) currState =
                State.FINISHED
            playerThatMoved
        } else null
    }

    /**
     * Gets a boolean value indicating whether the viewModel is tied or not
     * @throws IllegalStateException if the viewModel is NOT in the [State.FINISHED] state
     */
    fun isTied(): Boolean {
        check(currState == State.FINISHED)
        return board.isTied()
    }

    /**
     * Causes the player whose turn is the current turn to forfeit the viewModel.
     * @throws IllegalStateException if the viewModel is NOT in the [State.STARTED] state
     */
    fun forfeit() {
        check(currState == State.STARTED)
        winner = if (turn == Player.P1) Player.P2 else Player.P1
        currState = State.FINISHED
    }

    /**
     * Resets the viewModel placing it in the [State.NOT_STARTED] state.
     * @throws IllegalStateException if the viewModel is in the [State.STARTED] state
     */
    fun reset() {
        check(currState != State.STARTED)
        board = Board()
        turn = null
        winner = null
        currState = State.NOT_STARTED
    }

    /**
     * Starts the viewModel assigning the first turn to the given player
     * @throws IllegalStateException if the viewModel is in the [State.STARTED] state
     */
    fun start(firstToMove: Player) {
        check(currState != State.STARTED)
        if (currState == State.FINISHED) {
            board = Board()
            winner = null
        }

        turn = firstToMove
        currState = State.STARTED
    }

    /**
     * The next player to make a move, or null if no one is expected to make a move (i.e. because
     * the viewModel is NOT in the [State.STARTED] state)
     */
    @IgnoredOnParcel
    val nextTurn: Player?
        get() = turn

    /**
     * The viewModel winner, or null if there is no winner (either because the viewModel has not started, or
     * because the viewModel is tied)
     */
    @IgnoredOnParcel
    val theWinner: Player?
        get() = winner


    /**
     * The viewModel's current state
     */
    @IgnoredOnParcel
    val state: State
        get() = currState
}