package edu.isel.adeetc.pdm.tictactoe

import android.app.Application

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
    val repository by lazy { Repository() }
}