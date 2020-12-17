package edu.isel.adeetc.pdm.tictactoe.challenges.create

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import edu.isel.adeetc.pdm.tictactoe.R
import edu.isel.adeetc.pdm.tictactoe.State

import edu.isel.adeetc.pdm.tictactoe.challenges.ChallengeInfo
import edu.isel.adeetc.pdm.tictactoe.databinding.ActivityCreateChallengeBinding

/**
 * The key used to identify the result extra bearing the [ChallengeInfo] instance
 */
const val RESULT_EXTRA = "CCA.Result"

/**
 * The activity used to create a new challenge.
 *
 * The result of the creation operation is made available through [setResult] and if the creation
 * was successful, the corresponding [ChallengeInfo] instance is placed as the extra [RESULT_EXTRA]
 * of the result [Intent].
 */
class CreateChallengeActivity : AppCompatActivity() {

    /**
     * The associated view model instance
     */
    private val viewModel: CreateChallengeViewModel by viewModels()

    /**
     * The associated View Binding
     */
    private val binding: ActivityCreateChallengeBinding by lazy {
        ActivityCreateChallengeBinding.inflate(layoutInflater)
    }

    /**
     * Callback method that handles the activity initiation
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel.result.observe(this) {
            if (it.state == State.COMPLETE) {
                if (it.result != null) {
                    setResult(Activity.RESULT_OK, Intent().putExtra(RESULT_EXTRA, it.result))
                    finish()
                } else {
                    Toast.makeText(this, R.string.error_creating_challenge, Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.create.setOnClickListener {
            viewModel.createChallenge(
                binding.name.text.toString(),
                binding.message.text.toString()
            )
        }
    }
}
