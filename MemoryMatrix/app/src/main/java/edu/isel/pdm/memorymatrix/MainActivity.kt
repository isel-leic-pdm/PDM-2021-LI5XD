package edu.isel.pdm.memorymatrix

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val p1 = Position(2, 3)
        val p2 = Position(2, 3)

        Log.v("MemoryMatrix", "p1 == p2 -> ${p1 == p2}")

        startButton.setOnClickListener {
            val toGuess = MatrixPattern()
            val view = MatrixPatternView(matrixView, toGuess)
            view.draw()
        }
    }
}