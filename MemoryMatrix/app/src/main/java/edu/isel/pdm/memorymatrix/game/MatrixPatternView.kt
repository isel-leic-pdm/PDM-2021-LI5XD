package edu.isel.pdm.memorymatrix.game

import android.content.res.Resources
import edu.isel.pdm.memorymatrix.R
import pt.isel.poo.tile.OnTileTouchListener
import pt.isel.poo.tile.TilePanel

/**
 * Extension method to hide the following ugly code. Real life code sometimes needs to be ugly...
 */
private fun TilePanel.getColorFromId(colorResourceId: Int) =
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
        context.getColor(colorResourceId)
    } else context.resources.getColor(colorResourceId)

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
 * public interface. It registers the given listener to be called whenever a tile is clicked.
 */
fun TilePanel.setTileListener(listener: (xTile: Int, yTile: Int) -> Boolean) {
    setListener(object : OnTileTouchAdapter() {
        override fun onClick(xTile: Int, yTile: Int): Boolean {
            return listener(xTile, yTile)
        }
    })
}

/**
 * Extension method used to remove the current tile listener from the [TilePanel].
 */
fun TilePanel.unsetTileListener() {
    setListener(null)
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
fun TilePanel.drawPattern(toGuess: MatrixPattern?) {
    clear()
    val color = getColorFromId(R.color.previewPatternColor)
    toGuess?.forEach { setTile(it.x, it.y, PatternElementTile(color)) }
}

fun TilePanel.drawGuessingPattern(guessing: MatrixPattern, toGuess: MatrixPattern) {
    clear()
    val color = getColorFromId(R.color.guessPatternColor)
    guessing.forEach {
        setTile(it.x, it.y, GuessElementTile(toGuess.contains(Position(it.x, it.y)), color))
    }
}
