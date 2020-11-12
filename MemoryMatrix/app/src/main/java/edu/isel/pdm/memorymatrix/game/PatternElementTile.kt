package edu.isel.pdm.memorymatrix.game

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import pt.isel.poo.tile.Tile

private const val MARGIN: Float = 20f

/**
 * Tile implementation used to display memory matrix pattern elements.
 */
class PatternElementTile(private val tileColor: Int) : Tile {

    private companion object {
        val brush: Paint = Paint().apply {
            strokeWidth = 5f
            style = Paint.Style.FILL_AND_STROKE
        }
    }

    override fun draw(canvas: Canvas, side: Int) {
        brush.color = tileColor
        canvas.drawRect(MARGIN, MARGIN, side - MARGIN, side - MARGIN, brush)
    }

    override fun setSelect(selected: Boolean) = false
}