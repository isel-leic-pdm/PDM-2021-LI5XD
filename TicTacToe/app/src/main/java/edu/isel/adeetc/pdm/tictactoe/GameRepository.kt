package edu.isel.adeetc.pdm.tictactoe

import android.util.Log
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.isel.adeetc.pdm.tictactoe.challenges.ChallengeInfo
import edu.isel.adeetc.pdm.tictactoe.game.model.Board
import edu.isel.adeetc.pdm.tictactoe.game.model.Game
import edu.isel.adeetc.pdm.tictactoe.game.model.GameDTO
import edu.isel.adeetc.pdm.tictactoe.game.model.toGame

/**
 * The path of the Firestore collection that contains all the challenges
 */
private const val CHALLENGES_COLLECTION = "challenges"

/**
 * The name of the Firestore collection that contains all the games
 */
private const val GAMES_COLLECTION = "games"

private const val CHALLENGER_NAME = "challengerName"
private const val CHALLENGER_MESSAGE = "challengerMessage"

private const val GAME_STATE_KEY = "game"
private const val CHALLENGE_INFO_KEY = "challenge"


/**
 * Extension function used to convert createdChallenge documents stored in the Firestore DB into
 * [ChallengeInfo] instances
 */
private fun QueryDocumentSnapshot.toChallengeInfo() =
    ChallengeInfo(
        id,
        data[CHALLENGER_NAME] as String,
        data[CHALLENGER_MESSAGE] as String
    )

/**
 * The repository for the distributed Tic Tac Toe.
 */
class Repository(private val mapper: ObjectMapper) {

    /**
     * Fetches the list of open challenges from the backend
     *
     * Implementation note: This fetches ALL open challenges. In realistic scenarios this is
     * a poor design decision because the resulting data set size is unbounded!
     */
    fun fetchChallenges(onSuccess: (List<ChallengeInfo>) -> Unit, onError: (Exception) -> Unit) {
        Firebase.firestore.collection(CHALLENGES_COLLECTION)
            .get()
            .addOnSuccessListener { result ->
                Log.v(TAG, "Repo got list from Firestore")
                onSuccess(result.map { it.toChallengeInfo() }.toList())
            }
            .addOnFailureListener {
                Log.e(TAG, "Repo: An error occurred while fetching list from Firestore")
                Log.e(TAG, "Error was $it")
                onError(it)
            }
    }

    /**
     * Unpublishes the challenge with the given identifier.
     */
    fun unpublishChallenge(challengeId: String, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        Firebase.firestore
            .collection(CHALLENGES_COLLECTION)
            .document(challengeId)
            .delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener(onError)
    }

    /**
     * Publishes a challenge with the given [name] and [message].
     */
    fun publishChallenge(
        name: String,
        message: String,
        onSuccess: (ChallengeInfo) -> Unit,
        onError: (Exception) -> Unit) {

        Firebase.firestore.collection(CHALLENGES_COLLECTION)
            .add(hashMapOf(CHALLENGER_NAME to name, CHALLENGER_MESSAGE to message))
            .addOnSuccessListener { onSuccess(ChallengeInfo(it.id, name, message)) }
            .addOnFailureListener { onError(it) }
    }


    /**
     * Subscribes for changes in the game with the given identifier (i.e. [challengeId])
     */
    fun subscribeTo(
        challengeId: String,
        onSubscriptionError: (Exception) -> Unit,
        onStateChanged: (Game) -> Unit
    ): ListenerRegistration {

        return Firebase.firestore
            .collection(GAMES_COLLECTION)
            .document(challengeId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    onSubscriptionError(error)
                    return@addSnapshotListener
                }

                if (snapshot?.exists() == true) {
                    val gameDTO = mapper.readValue(
                        snapshot.get(GAME_STATE_KEY) as String,
                        GameDTO::class.java
                    )
                    onStateChanged(gameDTO.toGame())
                }
            }
    }

    /**
     * Updates the shared game state
     */
    fun updateGameState(
        game: Game,
        challenge: ChallengeInfo,
        onSuccess: (Game) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val gameStateBlob = mapper.writeValueAsString(game.toGameDTO())
        val challengeBlob = mapper.writeValueAsString(challenge)

        Firebase.firestore.collection(GAMES_COLLECTION)
            .document(challenge.id)
            .set(hashMapOf(
                GAME_STATE_KEY to gameStateBlob,
                CHALLENGE_INFO_KEY to challengeBlob
            ))
            .addOnSuccessListener { onSuccess(game) }
            .addOnFailureListener { onError(it) }
    }
}