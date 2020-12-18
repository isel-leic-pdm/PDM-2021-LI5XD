package edu.isel.adeetc.pdm.tictactoe.game.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

private const val BOARD_SIDE = 3
private const val MAX_MOVES = BOARD_SIDE * BOARD_SIDE


/**
 * External representation of the board moves.
 */
data class MovesDTO(val moves: List<Board.Move>)

/**
 * The Tic-Tac-Toe viewModel board. It represents the ongoing game's state.
 */
@Parcelize
class Board(private val moves: MutableList<Move> = mutableListOf()) : Parcelable {

    /**
     * Creates an external representation of the board moves
     */
    fun toMovesDTO() = MovesDTO(moves)

    @Parcelize
    data class Move(val player: Player, val x: Int, val y: Int) : Parcelable

    /**
     * Gets the move, represented by the [Player] instance that made it, at the given position.
     *
     * @param   [x] the horizontal coordinate in the interval [0..2]
     * @param   [y] the vertical coordinate in the interval [0..2]
     * @return  the [Player] instance corresponding to the move at the position, or null
     */
    operator fun get(x: Int, y: Int): Player? {
        require(x in 0 until BOARD_SIDE)
        require(y in 0 until BOARD_SIDE)
        return moves.find { it.x == x && it.y == y }?.player
    }

    /**
     * Sets the move, represented by the [Player] instance that made it, at the given position.
     *
     * @param   [x] the horizontal coordinate in the interval [0..2]
     * @param   [y] the vertical coordinate in the interval [0..2]
     * @param   [player] the [Player] instance that made the move
     * @return  the current instance, for fluent use
     * @throws  [IllegalArgumentException] if the specified position is not valid
     * @throws  [IllegalStateException] if the specified position is already in use
     */
    operator fun set(x: Int, y: Int, player: Player): Board {
        require(x in 0 until BOARD_SIDE)
        require(y in 0 until BOARD_SIDE)
        check(this[x, y] == null)

        moves.add(Move(player, x, y))
        return this
    }

    /**
     * Gets the winner, or null if the game is tied or not over yet
     */
    fun getWinner(): Player? {

        fun verify(startX: Int, startY: Int, dx: Int, dy: Int): Player? {

            val candidateWinner = this[startX, startY] ?: return null

            for (i in 1 until BOARD_SIDE) {
                if (this[startX + i * dx, startY + i * dy] != candidateWinner)
                    return null
            }

            return candidateWinner
        }

        return (
                verify(startX = 0, startY = 0, dx = 1, dy = 0) ?: verify(
                    startX = 0,
                    startY = 1,
                    dx = 1,
                    dy = 0
                ) ?: verify(startX = 0, startY = 2, dx = 1, dy = 0) ?: verify(
                    startX = 0,
                    startY = 0,
                    dx = 1,
                    dy = 1
                ) ?: verify(startX = 2, startY = 0, dx = -1, dy = 1) ?: verify(
                    startX = 0,
                    startY = 0,
                    dx = 0,
                    dy = 1
                ) ?: verify(startX = 1, startY = 0, dx = 0, dy = 1) ?: verify(
                    startX = 2,
                    startY = 0,
                    dx = 0,
                    dy = 1
                )
        )
    }

    /**
     * Gets a boolean value indicating whether the viewModel is tied or not
     */
    fun isTied() = moves.size == MAX_MOVES && getWinner() == null
}