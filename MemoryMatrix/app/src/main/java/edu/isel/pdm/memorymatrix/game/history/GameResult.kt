package edu.isel.pdm.memorymatrix.game.history

import androidx.room.Entity
import androidx.room.PrimaryKey
import edu.isel.pdm.memorymatrix.game.data.Position
import java.util.*

@Entity(tableName = "games")
data class GameResult(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val side: Int,
    val toGuess: List<Position>,
    val guesses: List<Position>,
    val score: Int,
    val date: Date = Date()
)