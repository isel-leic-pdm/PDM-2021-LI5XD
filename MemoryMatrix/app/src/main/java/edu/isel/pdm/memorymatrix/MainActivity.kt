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

    private fun startGame(binding: ActivityMainBinding, viewModel: MatrixViewModel) {
        viewModel.current = MatrixPattern.empty(binding.matrixView.widthInTiles)
        drawPattern(binding.matrixView, viewModel.current)

        binding.matrixView.setListener(object : OnTileTouchAdapter() {
            override fun onClick(xTile: Int, yTile: Int): Boolean {
                viewModel.current = viewModel.current?.plus(Position(xTile, yTile))
                drawPattern(binding.matrixView, viewModel.current)
                if (viewModel.current?.count == viewModel.toGuess?.count)
                    endGame(binding, viewModel)
                return true
            }
        })
    }

    private fun endGame(binding: ActivityMainBinding, viewModel: MatrixViewModel) {
        binding.matrixView.setListener(null)
        binding.startButton.postDelayed(5000) {
            binding.startButton.isEnabled = true
            drawPattern(binding.matrixView, viewModel.toGuess)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v("MemoryMatrix", "onCreate() with activity ${this.hashCode()}")
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel = MatrixViewModel()
        binding.startButton.setOnClickListener {
            viewModel.toGuess = MatrixPattern.fromRandom(PATTERN_SIZE, binding.matrixView.widthInTiles)
            drawPattern(binding.matrixView, viewModel.toGuess)
            binding.startButton.isEnabled = false

            binding.startButton.postDelayed(5000) {
                startGame(binding, viewModel)
            }
        }
    }
}
