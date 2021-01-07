package edu.isel.adeetc.pdm.tictactoe.game

import android.content.Context
import android.util.Log
import androidx.concurrent.futures.CallbackToFutureAdapter
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.common.util.concurrent.ListenableFuture
import edu.isel.adeetc.pdm.tictactoe.TAG
import edu.isel.adeetc.pdm.tictactoe.TicTacToeApplication
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

const val CHALLENGE_ID_KEY = "CHALLENGE_ID"

private fun logWithThreadInfo(message: String) {
    Log.v(TAG, "$message. Thread is ${Thread.currentThread().name}")
}

/**
 * For demo purposes only.
 */
class SynchronousGameStateCleanup(
    ctx: Context,
    params: WorkerParameters
) : Worker(ctx, params) {

    override fun doWork(): Result {

        val challengeId = inputData.getString(CHALLENGE_ID_KEY) ?: ""
        logWithThreadInfo("Cleaning up of $challengeId")

        var error: Exception? = null
        val done = CountDownLatch(1)

        (applicationContext as TicTacToeApplication).repository.deleteGame(
            challengeId,
            {
                logWithThreadInfo("Cleanup of $challengeId succeeded")
                done.countDown()
            },
            {
                logWithThreadInfo("Cleanup of $challengeId failed")
                error = it
                done.countDown()
            }
        )

        return if ( !done.await(30, TimeUnit.SECONDS) || error != null)
            Result.retry()
        else Result.success()
    }
}

/**
 * An asynchronous implementation using a callback to ListenableFuture adaptation.
 */
class AsyncGameStateCleanup(
    ctx: Context,
    params: WorkerParameters
) : ListenableWorker(ctx, params) {

    override fun startWork(): ListenableFuture<Result> {

        val challengeId = inputData.getString(CHALLENGE_ID_KEY) ?: ""
        logWithThreadInfo("Cleaning up of $challengeId")

        return CallbackToFutureAdapter.getFuture { publisher ->
            (applicationContext as TicTacToeApplication).repository.deleteGame(
                challengeId,
                {
                    logWithThreadInfo("Cleanup of $challengeId succeeded")
                    publisher.set(Result.success())
                },
                {
                    logWithThreadInfo("Cleanup of $challengeId failed")
                    publisher.set(Result.retry())
                }
            )
        }
    }
}
