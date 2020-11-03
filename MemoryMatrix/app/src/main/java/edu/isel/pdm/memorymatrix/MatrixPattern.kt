package edu.isel.pdm.memorymatrix

import android.os.Parcel
import android.os.Parcelable
import kotlin.random.Random

data class Position(val x: Int, val y: Int) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(x)
        parcel.writeInt(y)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Position> {
        override fun createFromParcel(parcel: Parcel): Position {
            return Position(parcel)
        }

        override fun newArray(size: Int): Array<Position?> {
            return arrayOfNulls(size)
        }
    }
}

data class MatrixPattern(private val pattern: List<Position>, val side: Int) : Iterable<Position> {

    companion object {
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
        fun empty(side: Int) = MatrixPattern(emptyList(), side)
    }

    override fun iterator() = pattern.iterator()
    operator fun plus(position: Position) = MatrixPattern(pattern + position, side)
    val count: Int
        get() = pattern.size

    fun toList() = pattern.toList()
}