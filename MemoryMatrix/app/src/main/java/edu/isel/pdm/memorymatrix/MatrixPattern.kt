package edu.isel.pdm.memorymatrix

import kotlin.random.Random

data class Position(val x: Int, val y: Int)

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
}