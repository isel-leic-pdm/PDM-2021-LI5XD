package edu.isel.pdm.memorymatrix

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

private fun View.postDelayed(delay: Long, action: Runnable) {
    postDelayed(action, delay)
}

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toGuess = MatrixPattern.fromRandom(8, matrixView.widthInTiles)
        val current = MatrixPattern.empty(matrixView.widthInTiles)

        Log.v("MemoryMatrix", "onCreate on thread ${Thread.currentThread().name}")

        startButton.setOnClickListener {
            Log.v("MemoryMatrix", "startButton.onClick on thread ${Thread.currentThread().name}")
            drawPattern(matrixView, toGuess)

            startButton.postDelayed(10000) {
                Log.v("MemoryMatrix", "postDelayed on thread ${Thread.currentThread().name}")
                drawPattern(matrixView, current)
            }
        }
    }
}
