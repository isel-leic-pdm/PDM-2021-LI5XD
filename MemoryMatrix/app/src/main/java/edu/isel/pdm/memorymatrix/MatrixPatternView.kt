package edu.isel.pdm.memorymatrix

import pt.isel.poo.tile.OnTileTouchListener
import pt.isel.poo.tile.TilePanel

/**
 * Helper class used to provide an empty implementation of [OnTileTouchListener].
 */
private open class OnTileTouchAdapter : OnTileTouchListener {
    override fun onClick(xTile: Int, yTile: Int) = false
    override fun onDrag(xFrom: Int, yFrom: Int, xTo: Int, yTo: Int) = false
    override fun onDragEnd(x: Int, y: Int) {}
    override fun onDragCancel() { }
}

/**
 * Extension method used to improve (from a Kotlin consumer code's perspective) the [TilePanel]'s
 * public interface.
 */
fun TilePanel.setTileListener(listener: (xTile: Int, yTile: Int) -> Boolean) {
    setListener(object : OnTileTouchAdapter() {
        override fun onClick(xTile: Int, yTile: Int): Boolean {
            return listener(xTile, yTile)
        }
    })
}

/**
 * Extension method to [TilePanel] that clears it, that is, removes all of its tiles.
 */
fun TilePanel.clear() {
    for (x in 0 until widthInTiles)
        for (y in 0 until heightInTiles) {
            setTile(x, y, null)
        }
}

/**
 * Extension method to [TilePanel] that draws the given [MatrixPattern].
 */
fun TilePanel.drawPattern(model: MatrixPattern?) {
    clear()
    model?.forEach { setTile(it.x, it.y, PatternElementTile()) }
}
