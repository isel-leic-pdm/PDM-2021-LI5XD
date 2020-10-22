package edu.isel.pdm.memorymatrix.demo

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View

class DemoView(ctx: Context, attrs: AttributeSet?) : View(ctx, attrs) {

    private val brush: Paint = Paint().apply {
        color = Color.parseColor("#FF0000")
        strokeWidth = 10f
        style = Paint.Style.FILL_AND_STROKE
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawLine(0f, 0f, width.toFloat(), height.toFloat(), brush)
        Log.v("MemoryMatrix", "onDraw on thread ${Thread.currentThread().name}")
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Log.v("MemoryMatrix", "onMeasure on thread ${Thread.currentThread().name}")
    }
}