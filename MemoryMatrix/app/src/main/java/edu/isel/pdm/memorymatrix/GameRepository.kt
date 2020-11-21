package edu.isel.pdm.memorymatrix

import android.content.SharedPreferences

private const val HIGHEST_LEVEL_KEY = "HighestLevelKey"

/**
 * The game repository holding globally relevant information.
 */
class GameRepository(
    private val sharedPreferences: SharedPreferences
) {

    /**
     * The highest reached level
     */
    var highestLevel: Int
        get() = sharedPreferences.getInt(HIGHEST_LEVEL_KEY, 1)
        set(value) {
            sharedPreferences
                .edit()
                .putInt(HIGHEST_LEVEL_KEY, value)
                .apply()
        }
}