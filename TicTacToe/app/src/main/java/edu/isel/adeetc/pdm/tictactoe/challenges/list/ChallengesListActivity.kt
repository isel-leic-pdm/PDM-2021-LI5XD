package edu.isel.adeetc.pdm.tictactoe.challenges.list

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import edu.isel.adeetc.pdm.tictactoe.R
import edu.isel.adeetc.pdm.tictactoe.State
import edu.isel.adeetc.pdm.tictactoe.about.AboutActivity
import edu.isel.adeetc.pdm.tictactoe.challenges.ChallengeInfo
import edu.isel.adeetc.pdm.tictactoe.challenges.create.CreateChallengeActivity
import edu.isel.adeetc.pdm.tictactoe.challenges.create.RESULT_EXTRA
import edu.isel.adeetc.pdm.tictactoe.challenges.list.view.ChallengesListAdapter
import edu.isel.adeetc.pdm.tictactoe.databinding.ActivityChallengesListBinding
import edu.isel.adeetc.pdm.tictactoe.game.*
import edu.isel.adeetc.pdm.tictactoe.game.model.Player

private const val CREATE_CODE = 10001

/**
 * The activity used to display the list of existing challenges.
 */
class ChallengesListActivity : AppCompatActivity() {

    /**
     * Called whenever the challenges list is to be fetched again.
     */
    private fun updateChallengesList() {
        binding.refreshLayout.isRefreshing = true
        viewModel.fetchChallenges()
    }

    /**
     * Called whenever a list element is selected. The player that accepts the challenge is the
     * first to make a move.
     *
     * @param challenge the selected challenge
     */
    private fun challengeSelected(challenge: ChallengeInfo) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.accept_challenge_dialog_title, challenge.challengerName))
            .setPositiveButton(R.string.accept_challenge_dialog_ok) { _, _ -> viewModel.tryAcceptChallenge(challenge) }
            .setNegativeButton(R.string.accept_challenge_dialog_cancel, null)
            .create()
            .show()
    }

    /**
     * The associated view model instance
     */
    private val viewModel: ChallengesViewModel by viewModels()

    /**
     * The associated view binding
     */
    private val binding: ActivityChallengesListBinding by lazy {
        ActivityChallengesListBinding.inflate(layoutInflater)
    }

    /**
     * Callback method that handles the activity initiation
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.challengesList.setHasFixedSize(true)
        binding.challengesList.layoutManager = LinearLayoutManager(this)

        // Get view model instance and add its contents to the recycler view
        viewModel.challenges.observe(this) {
            binding.challengesList.adapter = ChallengesListAdapter(it, ::challengeSelected)
            binding.refreshLayout.isRefreshing = false
        }

        // Setup ui event handlers
        binding.refreshLayout.setOnRefreshListener {
            updateChallengesList()
        }

        binding.createChallengeButton.setOnClickListener {
            startActivityForResult(Intent(this, CreateChallengeActivity::class.java),
                CREATE_CODE
            )
        }

        viewModel.enrolmentResult.observe(this) {

            if (it.state == State.COMPLETE) {
                if (it.result != null) {
                    startActivity(Intent(this, GameActivity::class.java).apply {
                        putExtra(ACCEPTED_CHALLENGE_EXTRA, it.result)
                        putExtra(LOCAL_PLAYER_EXTRA, Player.P1 as Parcelable)
                        putExtra(TURN_PLAYER_EXTRA, Player.P1 as Parcelable)
                    })
                } else {
                    Toast.makeText(this, R.string.error_accepting_challenge, Toast.LENGTH_LONG).show()
                }
                viewModel.resetEnrolmentResult()
                binding.challengesList.isEnabled = true
            } else {
                binding.challengesList.isEnabled = false
            }
        }
    }

    /**
     * Callback method that handles menu creation
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_challenges_list, menu)
        return true
    }

    /**
     * Callback method that handles menu item selection
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.about -> {
            startActivity(Intent(this, AboutActivity::class.java))
            true
        }
        R.id.challenges_list_update -> {
            updateChallengesList()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    /**
     * Callback method that handles the result obtained from activities launched to collect user
     * input.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            CREATE_CODE -> if (resultCode == Activity.RESULT_OK) {
                updateChallengesList()
                val createdChallenge = data?.getParcelableExtra<ChallengeInfo>(RESULT_EXTRA)
                startActivity(Intent(this, GameActivity::class.java).apply {
                    putExtra(ACCEPTED_CHALLENGE_EXTRA, createdChallenge)
                    putExtra(LOCAL_PLAYER_EXTRA, Player.P2 as Parcelable)
                    putExtra(TURN_PLAYER_EXTRA, Player.P1 as Parcelable)
                })
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onStart() {
        super.onStart()
        updateChallengesList()
    }
}
