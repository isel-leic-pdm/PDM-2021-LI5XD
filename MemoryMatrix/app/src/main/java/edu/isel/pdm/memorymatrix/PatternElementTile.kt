package edu.isel.pdm.memorymatrix

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import pt.isel.poo.tile.Tile

private const val MARGIN: Float = 20f

class PatternElementTile : Tile {

    private val brush: Paint = Paint().apply {
        color = Color.parseColor("#AA8BC2")
        strokeWidth = 5f
        style = Paint.Style.FILL_AND_STROKE
    }

    override fun draw(canvas: Canvas?, side: Int) {
        canvas?.drawRect(
            MARGIN, MARGIN,
            side.toFloat() - MARGIN,
            side.toFloat() - MARGIN,
            brush
        )
    }

    override fun setSelect(selected: Boolean) = false
}