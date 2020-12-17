package edu.isel.adeetc.pdm.tictactoe.challenges.create

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import edu.isel.adeetc.pdm.tictactoe.Result
import edu.isel.adeetc.pdm.tictactoe.State
import edu.isel.adeetc.pdm.tictactoe.TicTacToeApplication
import edu.isel.adeetc.pdm.tictactoe.challenges.ChallengeInfo

/**
 * The View Model to be used in the [CreateChallengeActivity].
 *
 * Challenges are created by participants and are posted on the server, awaiting acceptance.
 */
class CreateChallengeViewModel(app: Application) : AndroidViewModel(app) {

    val result: LiveData<Result<ChallengeInfo, Exception>> = MutableLiveData()

    /**
     * Creates a challenge with the given arguments. The result is placed in [result]
     */
    fun createChallenge(name: String, message: String) {
        val app = getApplication<TicTacToeApplication>()
        val mutableResult = result as MutableLiveData<Result<ChallengeInfo, Exception>>
        app.repository.publishChallenge(name, message,
            onSuccess = { mutableResult.value = Result(State.COMPLETE, result = it) },
            onError = { mutableResult.value = Result(State.COMPLETE, error = it) }
        )
    }
}