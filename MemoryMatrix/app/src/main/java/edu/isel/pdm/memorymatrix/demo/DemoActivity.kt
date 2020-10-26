package edu.isel.pdm.memorymatrix.demo

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_UP
import androidx.appcompat.app.AppCompatActivity
import edu.isel.pdm.memorymatrix.R

class DemoActivity : AppCompatActivity() {
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)
        var initialPoint = Point(0f, 0f)
        findViewById<DemoView>(R.id.demoView).setOnTouchListener { v, event ->
            when(event.action) {
                ACTION_DOWN -> initialPoint = Point(event.x, event.y)
                ACTION_UP -> {
                    val demoView = findViewById<DemoView>(R.id.demoView)
                    demoView.model = DemoModel(initialPoint, Point(event.x, event.y))
                }

            }
            true
        }

    }
}