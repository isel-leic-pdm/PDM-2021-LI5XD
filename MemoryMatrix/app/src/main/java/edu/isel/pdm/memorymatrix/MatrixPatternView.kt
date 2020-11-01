package edu.isel.pdm.memorymatrix

import pt.isel.poo.tile.TilePanel

fun TilePanel.clear() {
    for (x in 0 until widthInTiles)
        for (y in 0 until heightInTiles) {
            setTile(x, y, null)
        }
}

fun drawPattern(matrixView: TilePanel, model: MatrixPattern?) {
    matrixView.clear()
    model?.forEach {
        matrixView.setTile(it.x, it.y, PatternElementTile())
    }
}
