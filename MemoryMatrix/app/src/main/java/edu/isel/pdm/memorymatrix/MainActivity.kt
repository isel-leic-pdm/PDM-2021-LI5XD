package edu.isel.pdm.memorymatrix

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startButton.setOnClickListener {
            val toGuess = MatrixPattern.fromRandom(8, matrixView.widthInTiles)
            drawPattern(matrixView, toGuess)
        }
    }
}