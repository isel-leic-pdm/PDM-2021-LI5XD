package edu.isel.pdm.memorymatrix

import android.os.Parcel
import android.os.Parcelable
import kotlin.random.Random

/**
 * Represents occupied positions in a [MatrixPattern]
 *
 * @property x  the position's horizontal coordinate
 * @property y  the position's vertical coordinate
 */
data class Position(val x: Int, val y: Int) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(x)
        parcel.writeInt(y)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<Position> {
        override fun createFromParcel(parcel: Parcel) = Position(parcel)
        override fun newArray(size: Int) = arrayOfNulls<Position>(size)
    }
}

/**
 * Represents bi-dimensional patterns to be memorized. Instances are immutable.
 */
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

    constructor(parcel: Parcel) : this(
        parcel.createTypedArrayList(Position)?.toList() ?: emptyList(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(pattern)
        parcel.writeInt(side)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MatrixPattern> {
        override fun createFromParcel(parcel: Parcel): MatrixPattern {
            return MatrixPattern(parcel)
        }

        override fun newArray(size: Int): Array<MatrixPattern?> {
            return arrayOfNulls(size)
        }

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