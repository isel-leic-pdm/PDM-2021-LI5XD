package edu.isel.pdm.memorymatrix.game

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import edu.isel.pdm.memorymatrix.AboutActivity
import edu.isel.pdm.memorymatrix.R
import edu.isel.pdm.memorymatrix.databinding.ActivityGameBinding
import edu.isel.pdm.memorymatrix.game.data.Position
import edu.isel.pdm.memorymatrix.utils.BaseActivity

const val PATTERN_SIZE_EXTRA = "PatternSizeExtra"
private const val DEFAULT_PATTERN_SIZE = 8

/**
 * The main game screen.
 */
class GameActivity : BaseActivity() {

    private val patternSize: Int by lazy {
        intent.getIntExtra(PATTERN_SIZE_EXTRA, DEFAULT_PATTERN_SIZE)
    }

    /**
     * Displays the UI associated to the game's ToGuess state, that is, when the user elected to
     * start the game and the pattern that is to be guessed will be displayed for a predetermined
     * time interval.
     */
    private fun drawToGuess() {
        binding.matrixView.drawPattern(viewModel.game.value?.toGuess)
        binding.startButton.isEnabled = false
        binding.startButton.setOnClickListener(null)
    }

    /**
     * Displays the UI associated to the game's Guessing state, that is, the user is adding new
     * entries to his current guess.
     */
    private fun drawGuessing() {
        val userGuess = viewModel.game.value?.currentGuess ?: throw IllegalStateException()
        val toGuess = viewModel.game.value?.toGuess ?: throw IllegalStateException()
        binding.matrixView.drawGuessingPattern(userGuess, toGuess)

        binding.startButton.isEnabled = false
        binding.startButton.setOnClickListener(null)
        binding.matrixView.setTileListener { xTile, yTile ->
            viewModel.addGuess(Position(xTile, yTile))
            true
        }
    }

    /**
     * Displays the UI associated to the game's NotStarted state, that is, the user has not yet
     * started a game.
     */
    private fun drawNotStarted() {
        binding.startButton.isEnabled = true
        binding.matrixView.unsetTileListener()
        binding.startButton.setOnClickListener {
            viewModel.startGame(patternSize, binding.matrixView.widthInTiles, 5)
        }
    }

    /**
     * Displays the UI associated to the game's HasEnded state, that is, the user has just ended
     * his guess and the result of his efforts is being displayed.
     */
    private fun drawHasEnded() {
        val userGuess = viewModel.game.value?.currentGuess ?: throw IllegalStateException()
        val toGuess = viewModel.game.value?.toGuess ?: throw IllegalStateException()
        binding.matrixView.drawGuessingPattern(userGuess, toGuess)

        binding.startButton.isEnabled = true
        binding.startButton.setOnClickListener {
            viewModel.startGame(patternSize, binding.matrixView.widthInTiles, 5)
        }
        binding.matrixView.unsetTileListener()
    }

    private val viewModel: MatrixViewModel by viewModels()
    private val binding: ActivityGameBinding by lazy { ActivityGameBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel.game.observe(this) {
            when (viewModel.game.value?.state) {
                GameState.State.NOT_STARTED -> drawNotStarted()
                GameState.State.MEMORIZING -> drawToGuess()
                GameState.State.GUESSING -> drawGuessing()
                else -> drawHasEnded()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_game, menu)
        menu.findItem(R.id.history).setOnMenuItemClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
            true
        }
        return true
    }
}
