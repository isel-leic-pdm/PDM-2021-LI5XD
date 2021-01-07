package edu.isel.adeetc.pdm.tictactoe.game

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TableRow
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.*
import edu.isel.adeetc.pdm.tictactoe.R
import edu.isel.adeetc.pdm.tictactoe.TAG
import edu.isel.adeetc.pdm.tictactoe.challenges.ChallengeInfo
import edu.isel.adeetc.pdm.tictactoe.databinding.ActivityGameBinding
import edu.isel.adeetc.pdm.tictactoe.game.model.Game
import edu.isel.adeetc.pdm.tictactoe.game.model.Player
import edu.isel.adeetc.pdm.tictactoe.game.view.CellView

/**
 * Key to be used when adding the accepted challenge info (a [ChallengeInfo] instance) as an extra
 * of the intent to be sent to the [GameActivity]. The activity MUST receive this extra.
 */
const val ACCEPTED_CHALLENGE_EXTRA = "GameActivity.AcceptedChallengeExtra"

/**
 * Key to be used when adding the [Player] instance that represents the local player
 */
const val LOCAL_PLAYER_EXTRA = "GameActivity.LocalPlayerListenExtra"

/**
 * Key to be used when adding the [Player] instance that represents the next player to make a move
 */
const val TURN_PLAYER_EXTRA = "GameActivity.TurnPlayerListenExtra"

/**
 * The activity that displays the board.
 */
class GameActivity : AppCompatActivity() {

    private val localPlayer: Player by lazy {
        intent.getParcelableExtra<Player>(LOCAL_PLAYER_EXTRA) ?:
            throw IllegalArgumentException("Mandatory extra $LOCAL_PLAYER_EXTRA not present")
    }

    private val turnPlayer: Player by lazy {
        intent.getParcelableExtra<Player>(TURN_PLAYER_EXTRA) ?:
        throw IllegalArgumentException("Mandatory extra $TURN_PLAYER_EXTRA not present")
    }

    private val challengeInfo: ChallengeInfo by lazy {
        intent.getParcelableExtra<ChallengeInfo>(ACCEPTED_CHALLENGE_EXTRA) ?:
            throw IllegalArgumentException("Mandatory extra $ACCEPTED_CHALLENGE_EXTRA not present")
    }

    private val viewModel: GameViewModel by viewModels {
        @Suppress("UNCHECKED_CAST")
        object: ViewModelProvider.Factory {
            override fun <VM : ViewModel?> create(modelClass: Class<VM>): VM {
                return GameViewModel(application, localPlayer, challengeInfo) as VM
            }
        }
    }
    private val binding: ActivityGameBinding by lazy {
        ActivityGameBinding.inflate(layoutInflater)
    }

    /**
     * Callback method that handles the activity initiation
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "GameActivity.onCreate()")
        Log.v(TAG, "Local player is $localPlayer")
        Log.v(TAG, "Turn player is $turnPlayer")
        setContentView(binding.root)
        viewModel.gameState.observe(this) { updateUI() }
        binding.startButton.setOnClickListener { viewModel.start() }
        binding.forfeitButton.setOnClickListener { viewModel.forfeit() }
    }

    /**
     * Used to update de board view according to the current state of the game
     */
    private fun updateBoard() {
        if (viewModel.gameState.value?.state != Game.State.NOT_STARTED)
            binding.board.children.forEach { row ->
                (row as? TableRow)?.children?.forEach {
                    if (it is CellView)
                        it.updateDisplayMode(viewModel.gameState.value?.getMoveAt(it.column, it.row))
                }
            }
    }

    /**
     * Used to render the game view when the game has not been started yet
     */
    private fun renderNotStarted() {
        Log.v(TAG, "renderNotStarted() with local $localPlayer and turn $turnPlayer")

        if (viewModel.localPlayer == Player.P2) {
            title = getString(R.string.game_screen_title_distributed_challenger_waiting)
            binding.messageBoard.text = getString(R.string.game_turn_message_contender)
            binding.startButton.isEnabled = false
            binding.forfeitButton.isEnabled = false
        }
        else {
            title = getString(
                R.string.game_screen_title_distributed_contender,
                viewModel.challengeInfo.challengerName
            )
            binding.messageBoard.text = getString(R.string.game_turn_message_self)
            binding.startButton.isEnabled = true
            binding.forfeitButton.isEnabled = false
        }
    }

    /**
     * Used to render the game view when the game is in progress
     */
    private fun renderStarted() {
        Log.v(TAG, "renderStarted() with local $localPlayer and turn $turnPlayer")
        title = if (localPlayer == Player.P2)
            getString(R.string.game_screen_title_distributed_challenger_playing)
        else
            getString(
                R.string.game_screen_title_distributed_contender,
                viewModel.challengeInfo.challengerName
            )

        if (localPlayer == viewModel.gameState.value?.nextTurn) {
            binding.messageBoard.text = getString(R.string.game_turn_message_self)
            binding.forfeitButton.isEnabled = true
        } else {
            binding.messageBoard.text = if (localPlayer == Player.P1)
                getString(
                    R.string.game_turn_message,
                    viewModel.challengeInfo.challengerName
                )
            else getString(R.string.game_turn_message_contender)

            binding.forfeitButton.isEnabled = false
        }
        binding.startButton.isEnabled = false
    }

    /**
     * Used to render the game view when the game is in progress
     */
    private fun renderFinished() {
        Log.v(TAG, "renderFinished() with local $localPlayer and turn $turnPlayer")
        binding.startButton.isEnabled = false
        binding.forfeitButton.isEnabled = false
        binding.messageBoard.text = when {
            viewModel.gameState.value?.isTied() == true -> getString(R.string.game_tied_message)
            viewModel.gameState.value?.theWinner == viewModel.localPlayer -> getString(R.string.game_winner_message_self)
            else -> getString(R.string.game_looser_message_self)
        }

        viewModel.cleanupGame()
    }

    /**
     * Used to update the UI according to the current state of the game
     */
    private fun updateUI() {
        when (viewModel.gameState.value?.state) {
            Game.State.NOT_STARTED -> renderNotStarted()
            Game.State.STARTED -> renderStarted()
            Game.State.FINISHED -> renderFinished()
        }
        updateBoard()
    }

    /**
     * Makes a move on the given cell
     *
     * @param [view] The cell where the move has been made on
     */
    fun handleMove(view: View) {
        if (viewModel.gameState.value?.state != Game.State.STARTED)
            return

        val cell = view as CellView
        viewModel.makeMoveAt(cell.column, cell.row) ?: return
    }
}