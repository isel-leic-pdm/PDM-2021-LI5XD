package edu.isel.adeetc.pdm.tictactoe.game.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import edu.isel.adeetc.pdm.tictactoe.R
import edu.isel.adeetc.pdm.tictactoe.game.model.Player
import kotlin.math.min

private const val MIN_SIDE = 300

/**
 * Class for views that display Tic-Tac-Toe moves on the board
 */
class CellView(ctx: Context, attrs: AttributeSet?) : View(ctx, attrs) {

    /**
     * Used to control how the view is displayed
     */
    enum class DisplayMode { NONE, CROSS, CIRCLE }

    /**
     * Singleton instance used to paint the view's frame
     */
    private object OutlineBrush : Paint() {
        init {
            color = Color.BLACK
            style = Style.FILL_AND_STROKE
            strokeWidth = 6F
        }
    }

    /**
     * Singleton instance used to paint the view's createdChallenge
     */
    private object ContentBrush : Paint() {
        init {
            color = Color.RED
            style = Style.STROKE
            strokeWidth = 10F
        }
    }

    /**
     * The cell's column
     */
    val column: Int

    /**
     * The cell's row
     */
    val row: Int

    init {

        ctx.theme.obtainStyledAttributes(
            attrs, R.styleable.CellView, 0, 0).apply {

            try {
                column = getInt(R.styleable.CellView_column, 0)
                row = getInt(R.styleable.CellView_row, 0)
            }
            finally {
                recycle()
            }
        }
    }

    /**
     * Draws the cell's frame
     *
     * @param [canvas]  The [Canvas] where the frame is to be drawn
     */
    private fun drawFrame(canvas: Canvas) {
        with (canvas) {
            if (row == 1) {
                drawLine(0.toFloat(), 0.toFloat(), width.toFloat(), 0.toFloat(),
                    OutlineBrush
                )
                drawLine(0.toFloat(), height.toFloat(), width.toFloat(), height.toFloat(),
                    OutlineBrush
                )
            }
            if (column == 1) {
                drawLine(0.toFloat(), 0.toFloat(), 0.toFloat(), height.toFloat(),
                    OutlineBrush
                )
                drawLine(width.toFloat(), 0.toFloat(), width.toFloat(), height.toFloat(),
                    OutlineBrush
                )
            }
        }
    }

    /**
     * Draws the cell's createdChallenge, according to the current display mode
     *
     * @param [canvas]  The [Canvas] where the frame is to be drawn
     */
    private fun drawContent(canvas: Canvas) {

        if (displayMode == DisplayMode.NONE)
            return

        val availableHeight = height - paddingTop - paddingBottom
        val availableWidth = width - paddingStart - paddingEnd

        val side = min(availableHeight, availableWidth).toFloat()
        val marginW = (width - side) / 2
        val marginH = (height - side) / 2

        fun drawCircle() {
            canvas.drawCircle(width.toFloat() / 2, height.toFloat() / 2, side / 2,
                ContentBrush
            )
        }

        fun drawCross() {
            canvas.drawLine(marginW, marginH, marginW + side, marginH + side,
                ContentBrush
            )
            canvas.drawLine(marginW + side, marginH, marginW, marginH + side,
                ContentBrush
            )
        }

        when (displayMode) {
            DisplayMode.CROSS -> drawCross()
            DisplayMode.CIRCLE -> drawCircle()
            else -> throw Error("Impossible! STOP NOW!")
        }
    }

    /**
     * The view display mode.
     */
    var displayMode: DisplayMode =
        DisplayMode.NONE
        set(value) {
            field = value
            invalidate()
        }

    /**
     * Method used to update a given [CellView] instance's display mode
     *
     * @param   [player]    the player information
     */
    fun updateDisplayMode(player: Player?) {
        displayMode = when (player) {
            Player.P1 -> DisplayMode.CROSS
            Player.P2 -> DisplayMode.CIRCLE
            null -> DisplayMode.NONE
        }
    }

    override fun onDraw(canvas: Canvas) {
        drawFrame(canvas)
        drawContent(canvas)
    }

    override fun getSuggestedMinimumWidth() = MIN_SIDE

    override fun getSuggestedMinimumHeight() = MIN_SIDE
}