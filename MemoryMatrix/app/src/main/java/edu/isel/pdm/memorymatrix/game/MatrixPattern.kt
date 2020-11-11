package edu.isel.pdm.memorymatrix.game

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlin.random.Random

/**
 * Represents occupied positions in a [MatrixPattern]
 *
 * @property x  the position's horizontal coordinate
 * @property y  the position's vertical coordinate
 */
@Parcelize
data class Position(val x: Int, val y: Int) : Parcelable

/**
 * Represents bi-dimensional patterns to be memorized. Instances are immutable.
 */
@Parcelize
data class MatrixPattern(private val pattern: List<Position>, val side: Int) : Iterable<Position>,
    Parcelable {

    /**
     * Enables iteration over the pattern's elements
     */
    override fun iterator() = pattern.iterator()

    /**
     * Adds the given element to the current pattern and returns the new instance
     * @return the resulting pattern instance
     */
    operator fun plus(position: Position) = MatrixPattern(pattern + position, side)

    /**
     * The number of elements in the pattern.
     */
    val count: Int
        get() = pattern.size

    companion object {

        /**
         * Generates a random pattern.
         * @param count the number of elements of the pattern
         * @param side  the side of the matrix
         * @return the pattern instance
         */
        fun fromRandom(count: Int, side: Int): MatrixPattern {
            val availablePositions = mutableListOf<Position>()
            for (x in 0 until side)
                for (y in 0 until side) {
                    availablePositions.add(Position(x, y))
                }

            val generatedPositions = mutableListOf<Position>()
            repeat(count) {
                val randomIdx = Random.nextInt(availablePositions.size)
                generatedPositions.add(availablePositions.removeAt(randomIdx))
            }

            return MatrixPattern(generatedPositions, side)
        }

        /**
         * Generates an empty pattern.
         * @param side  the side of the matrix holding the (currently) empty pattern
         * @return the pattern instance
         */
        fun empty(side: Int) = MatrixPattern(emptyList(), side)
    }
}