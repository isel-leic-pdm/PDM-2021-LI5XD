package edu.isel.pdm.memorymatrix

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import edu.isel.pdm.memorymatrix.databinding.ActivityMainBinding

private fun View.postDelayed(delay: Long, action: Runnable) {
    postDelayed(action, delay)
}

private const val PATTERN_SIZE = 8

class MainActivity : AppCompatActivity() {

    /**
     * Displays the UI associated to the game's ToGuess state, that is, when the user elected to
     * start the game and the pattern that is to be guessed will be displayed for a predetermined
     * time interval.
     */
    private fun drawToGuess() {
        viewModel.startGame(PATTERN_SIZE, binding.matrixView.widthInTiles)
        binding.matrixView.drawPattern(viewModel.toGuess)
        binding.startButton.isEnabled = false
        binding.startButton.setOnClickListener(null)
        binding.startButton.postDelayed(5000) { drawGuessing() }
    }

    /**
     * Displays the UI associated to the game's Guessing state, that is, the user is adding new
     * entries to his current guess.
     */
    private fun drawGuessing() {
        binding.matrixView.drawPattern(viewModel.current)

        binding.startButton.isEnabled = false
        binding.startButton.setOnClickListener(null)
        binding.matrixView.setTileListener { xTile, yTile ->
            viewModel.addGuess(Position(xTile, yTile))
            if (!viewModel.isGameOngoing()) drawHasEnded()
            else drawGuessing()
            true
        }
    }

    /**
     * Displays the UI associated to the game's NotStarted state, that is, the user has not yet
     * started a game.
     */
    private fun drawNotStarted() {
        binding.startButton.isEnabled = true
        binding.startButton.setOnClickListener { drawToGuess() }
        binding.matrixView.setListener(null)
    }

    /**
     * Displays the UI associated to the game's HasEnded state, that is, the user has just ended
     * his guess and the result of his efforts is being displayed.
     */
    private fun drawHasEnded() {
        binding.matrixView.drawPattern(viewModel.current)
        binding.startButton.isEnabled = true
        binding.startButton.setOnClickListener { drawToGuess() }
        binding.matrixView.setListener(null)
    }

    private val viewModel: MatrixViewModel by viewModels()
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        Log.v("MemoryMatrix", "onCreate() with activity ${this.hashCode()}")
        Log.v("MemoryMatrix", "onCreate() with viewModel ${viewModel.hashCode()}")

        // What should we draw?
        // TODO: Fix for the case where the drawToGuess has already been made
        when {
            viewModel.isGameOngoing() && viewModel.current?.count == 0 -> drawToGuess()
            viewModel.isGameOngoing() -> drawGuessing()
            else -> drawNotStarted()
        }
    }
}
