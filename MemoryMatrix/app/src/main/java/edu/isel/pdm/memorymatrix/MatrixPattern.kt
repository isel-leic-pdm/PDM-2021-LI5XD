package edu.isel.pdm.memorymatrix

data class Position(val x: Int, val y: Int)

class MatrixPattern : Iterable<Position> {
    private val pattern = listOf(Position(0,0), Position(1,0))

    override fun iterator() = pattern.iterator()
}