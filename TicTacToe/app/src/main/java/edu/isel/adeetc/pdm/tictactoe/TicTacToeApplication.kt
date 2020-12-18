package edu.isel.adeetc.pdm.tictactoe

import android.app.Application
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

/**
 * Tag to be used in all the application's log messages
 */
const val TAG = "TicTacToe"

/**
 * Contains the globally accessible objects
 */
class TicTacToeApplication : Application() {

    /**
     * The game's repository
     */
    val repository by lazy {
        Repository(
            jacksonObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        )
    }
}