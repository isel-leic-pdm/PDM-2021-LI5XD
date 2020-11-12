package edu.isel.pdm.memorymatrix.game

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import pt.isel.poo.tile.Tile

private const val MARGIN: Float = 20f

/**
 * Tile implementation used to display memory matrix pattern elements.
 *
 * @property isCorrect      Indicates whether the associated guess is correct or not.
 * @property correctColor   The color to be used if the associated guess is correct.
 */
class GuessElementTile(private val isCorrect: Boolean, private val correctColor: Int) : Tile {

    private companion object {
        const val NOT_OK_COLOR = Color.RED
        val brush: Paint = Paint().apply {
            strokeWidth = 7f
            style = Paint.Style.FILL_AND_STROKE
        }
    }

    override fun draw(canvas: Canvas, side: Int) {
        if (isCorrect) {
            brush.color = correctColor
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