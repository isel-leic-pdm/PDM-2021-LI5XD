package edu.isel.pdm.memorymatrix

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import pt.isel.poo.tile.Tile

private const val MARGIN: Float = 20f

/**
 * Tile implementation used to display memory matrix pattern elements.
 */
class GuessElementTile(private val isCorrect: Boolean) : Tile {

    private companion object {
        val OK_COLOR = Color.parseColor("#558BC2")
        const val NOT_OK_COLOR = Color.RED
        val brush: Paint = Paint().apply {
            strokeWidth = 7f
            style = Paint.Style.FILL_AND_STROKE
        }
    }

    override fun draw(canvas: Canvas, side: Int) {
        if (isCorrect) {
            brush.color = OK_COLOR
            canvas.drawRect(MARGIN, MARGIN, side - MARGIN, side - MARGIN, brush)
        }
        else {
            brush.color = NOT_OK_COLOR
            canvas.drawLine(MARGIN, MARGIN, side - MARGIN, side - MARGIN, brush)
            canvas.drawLine(side - MARGIN, MARGIN, MARGIN, side - MARGIN, brush)
        }
    }

    override fun setSelect(selected: Boolean) = false
}