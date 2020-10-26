package edu.isel.pdm.memorymatrix

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import edu.isel.pdm.memorymatrix.databinding.ActivityMainBinding
import pt.isel.poo.tile.OnTileTouchListener

private fun View.postDelayed(delay: Long, action: Runnable) {
    postDelayed(action, delay)
}

private open class OnTileTouchAdapter : OnTileTouchListener {
    override fun onClick(xTile: Int, yTile: Int) = false
    override fun onDrag(xFrom: Int, yFrom: Int, xTo: Int, yTo: Int) = false
    override fun onDragEnd(x: Int, y: Int) {}
    override fun onDragCancel() { }
}

private const val PATTERN_SIZE = 8

class MainActivity : AppCompatActivity() {

    private fun startGame(binding: ActivityMainBinding, toGuess: MatrixPattern) {
        var current = MatrixPattern.empty(binding.matrixView.widthInTiles)
        drawPattern(binding.matrixView, current)

        binding.matrixView.setListener(object : OnTileTouchAdapter() {
            override fun onClick(xTile: Int, yTile: Int): Boolean {
                current += Position(xTile, yTile)
                drawPattern(binding.matrixView, current)
                if (current.count == toGuess.count)
                    endGame(binding, toGuess)
                return true
            }
        })
    }

    private fun endGame(binding: ActivityMainBinding, toGuess: MatrixPattern) {
        binding.matrixView.setListener(null)
        binding.startButton.postDelayed(5000) {
            binding.startButton.isEnabled = true
            drawPattern(binding.matrixView, toGuess)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v("MemoryMatrix", "onCreate() with activity ${this.hashCode()}")
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.v("MemoryMatrix", "onCreate() with startButton ${binding.startButton.hashCode()}")
        Log.v("MemoryMatrix", "onCreate() with matrixView ${binding.matrixView.hashCode()}")

        binding.startButton.setOnClickListener {
            val toGuess = MatrixPattern.fromRandom(PATTERN_SIZE, binding.matrixView.widthInTiles)
            drawPattern(binding.matrixView, toGuess)
            binding.startButton.isEnabled = false

            binding.startButton.postDelayed(10000) {
                startGame(binding, toGuess)
            }
        }
    }
}
