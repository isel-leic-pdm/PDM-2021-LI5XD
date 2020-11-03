package edu.isel.pdm.memorymatrix

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
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
private const val MATRIX_STATE = "MatrixViewModel"

class MainActivity : AppCompatActivity() {

    private fun drawToGuess(viewModel: MatrixViewModel, binding: ActivityMainBinding) {
        viewModel.startGame(PATTERN_SIZE, binding.matrixView.widthInTiles)
        drawPattern(binding.matrixView, viewModel.toGuess)

        binding.startButton.isEnabled = false
        binding.startButton.setOnClickListener(null)

        binding.startButton.postDelayed(5000) {
            drawCurrent(viewModel, binding)
        }
    }

    private fun drawCurrent(viewModel: MatrixViewModel, binding: ActivityMainBinding) {
        drawPattern(binding.matrixView, viewModel.current)

        binding.startButton.isEnabled = false
        binding.startButton.setOnClickListener(null)
        binding.matrixView.setListener(object : OnTileTouchAdapter() {
            override fun onClick(xTile: Int, yTile: Int): Boolean {
                viewModel.addGuess(Position(xTile, yTile))
                if (!viewModel.isGameOngoing()) drawHasEnded(viewModel, binding)
                else drawCurrent(viewModel, binding)
                return true
            }
        })
    }

    private fun drawNotStarted(viewModel: MatrixViewModel, binding: ActivityMainBinding) {
        binding.startButton.isEnabled = true
        binding.startButton.setOnClickListener { drawToGuess(viewModel, binding) }
        binding.matrixView.setListener(null)
    }

    private fun drawHasEnded(viewModel: MatrixViewModel, binding: ActivityMainBinding) {
        drawPattern(binding.matrixView, viewModel.current)
        binding.startButton.isEnabled = true
        binding.startButton.setOnClickListener { drawToGuess(viewModel, binding) }
        binding.matrixView.setListener(null)
    }

    private val viewModel: MatrixViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v("MemoryMatrix", "onCreate() with activity ${this.hashCode()}")

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.v("MemoryMatrix", "onCreate() with viewModel ${viewModel.hashCode()}")

        if (savedInstanceState != null) {
            Log.v("MemoryMatrix", "onCreate() with saved state")
            val state = savedInstanceState.getParcelable<MatrixState>(MATRIX_STATE)
            if (state != null) {
                Log.v("MemoryMatrix", "onCreate() with MY saved state")
                viewModel.toGuess = state?.toGuess
                viewModel.current = state?.current
            }
        }

        if (viewModel.isGameOngoing()) {
            // TODO: Fix for the case where the drawToGuess has already been made
            if (viewModel.current?.count == 0)
                drawToGuess(viewModel, binding)
            else drawCurrent(viewModel, binding)
        } else {
            drawNotStarted(viewModel, binding)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.v("MemoryMatrix", "onSaveInstanceState()")
        outState.putParcelable(MATRIX_STATE, viewModel.getState())
    }
}
