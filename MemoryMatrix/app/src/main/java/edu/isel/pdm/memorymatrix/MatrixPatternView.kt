package edu.isel.pdm.memorymatrix

import pt.isel.poo.tile.TilePanel

class MatrixPatternView(
    private val matrixView: TilePanel,
    private val model: MatrixPattern) {

    fun draw() {
        model.forEach {
            matrixView.setTile(it.x, it.y, PatternElementTile())
        }


    }
}
