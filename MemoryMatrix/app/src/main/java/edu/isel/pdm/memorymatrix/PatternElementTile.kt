package edu.isel.pdm.memorymatrix

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import pt.isel.poo.tile.Tile

class PatternElementTile : Tile {

    private val brush: Paint = Paint().apply {
        color = Color.RED
        strokeWidth = 5f
        style = Paint.Style.FILL_AND_STROKE
    }

    override fun draw(canvas: Canvas?, side: Int) {
        canvas?.drawLine(
            0f, 0f, side.toFloat(), side.toFloat(), brush
        )
    }

    override fun setSelect(selected: Boolean) = false
}