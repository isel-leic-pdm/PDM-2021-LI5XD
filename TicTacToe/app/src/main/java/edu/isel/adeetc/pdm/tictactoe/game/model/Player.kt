package edu.isel.adeetc.pdm.tictactoe.game.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Represents the viewModel players.
 *
 * @constructor Creates a player with the given identifier
 */
@Parcelize
enum class Player : Parcelable {
    P1,
    P2
}