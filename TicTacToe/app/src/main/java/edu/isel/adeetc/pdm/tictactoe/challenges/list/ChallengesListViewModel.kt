package edu.isel.adeetc.pdm.tictactoe.challenges.list

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import edu.isel.adeetc.pdm.tictactoe.R
import edu.isel.adeetc.pdm.tictactoe.Result
import edu.isel.adeetc.pdm.tictactoe.State
import edu.isel.adeetc.pdm.tictactoe.TicTacToeApplication
import edu.isel.adeetc.pdm.tictactoe.challenges.ChallengeInfo
import java.lang.Exception


/**
 * The View Model used in the [ChallengesListActivity].
 *
 * Challenges are created by participants and are posted to the server, awaiting acceptance.
 */
class ChallengesViewModel(app: Application) : AndroidViewModel(app) {

    private val app = getApplication<TicTacToeApplication>()

    /**
     * Contains the last fetched challenge list
     */
    val challenges: LiveData<List<ChallengeInfo>> = MutableLiveData()

    /**
     * Gets the challenges list by fetching them from the server. The operation's result is exposed
     * through [challenges]
     */
    fun fetchChallenges() {
        app.repository.fetchChallenges(
            onSuccess = {
                (challenges as MutableLiveData<List<ChallengeInfo>>).value = it
            },
            onError = {
                Toast.makeText(app, R.string.error_getting_list, Toast.LENGTH_LONG).show()
            }
        )
    }

    /**
     * Contains information about the enrolment in a challenge.
     */
    val enrolmentResult: LiveData<Result<ChallengeInfo, Exception>> = MutableLiveData()

    /**
     * Tries to accepts the given challenge. The result of the asynchronous operation is exposed
     * through [enrolmentResult]
     */
    fun tryAcceptChallenge(challengeInfo: ChallengeInfo) {
        val state = enrolmentResult as MutableLiveData<Result<ChallengeInfo, Exception>>
        state.value = Result(State.ONGOING, challengeInfo)
        app.repository.unpublishChallenge(
            challengeInfo.id,
            { state.value = Result(State.COMPLETE, challengeInfo) },
            { error -> state.value = Result(State.COMPLETE, challengeInfo, error) }
        )
    }

    /**
     * Resets the state of the enrolment
     */
    fun resetEnrolmentResult() {
        (enrolmentResult as MutableLiveData<Result<ChallengeInfo, Exception>>).value = Result()
    }
}
