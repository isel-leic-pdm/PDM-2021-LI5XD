package edu.isel.adeetc.pdm.tictactoe.game.model

import android.os.Parcelable
import edu.isel.adeetc.pdm.tictactoe.game.model.Game.State
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize


/**
 * External representation of the game.
 */
data class GameDTO(
    val movesDTO: MovesDTO,
    val turn: Player?,
    val winner: Player?,
    val state: State
)

fun GameDTO.toGame() = Game(Board(movesDTO.moves.toMutableList()), turn, winner, state)

/**
 * The Tic-Tac-Toe full game state.
 *
 * The game is in one of three states: [State.NOT_STARTED], [State.STARTED] and [State.FINISHED]
 *
 * Upon instantiation the game is placed in the [State.NOT_STARTED] state. The transition to
 * the [State.STARTED] is promoted by a call to the [start] method, where the first player to move
 * is specified.The game remains in this state until the game is finished, either because a winner
 * emerged or the game was tied. Either way, the game is placed in the [State.FINISHED] state. Once
 * this state is reached, the game can no longer be restarted.
 */
@Parcelize
data class Game(
    private var board: Board = Board(),
    private var turn: Player? = null,
    private var winner: Player? = null,
    private var currState: State = State.NOT_STARTED
) : Parcelable {

    /**
     * Enumeration of the game's possible states
     */
    enum class State { NOT_STARTED, STARTED, FINISHED }

    /**
     * Creates an external representation of the game
     */
    fun toGameDTO() = GameDTO(board.toMovesDTO(), turn, winner, currState)

    /**
     * Gets the move at the given position.
     * @return  the player that made the move, or null if there's no move at the position
     * @throws IllegalStateException if the game is the [State.NOT_STARTED] state
     */
    fun getMoveAt(x: Int, y: Int): Player? {
        check(currState != State.NOT_STARTED)
        return board[x, y]
    }

    /**
     * Makes a move at the given position.
     * @return  the player that made the move, or null if the move was not legal
     * @throws IllegalStateException if the game is NOT in the [State.STARTED] state
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
     * Gets a boolean value indicating whether the game is tied or not
     * @throws IllegalStateException if the game is NOT in the [State.FINISHED] state
     */
    fun isTied(): Boolean {
        check(currState == State.FINISHED)
        return board.isTied()
    }

    /**
     * Causes the player whose turn is the current turn to forfeit the game.
     * @throws IllegalStateException if the game is NOT in the [State.STARTED] state
     */
    fun forfeit() {
        check(currState == State.STARTED)
        winner = if (turn == Player.P1) Player.P2 else Player.P1
        currState = State.FINISHED
    }

    /**
     * Starts the game assigning the first turn to the given player
     * @throws IllegalStateException if the game is NOT in the [State.NOT_STARTED] state
     */
    fun start(firstToMove: Player) {
        check(currState == State.NOT_STARTED)
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